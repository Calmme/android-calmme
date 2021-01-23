package kr.co.calmme

import android.app.Activity
import android.os.CountDownTimer
import android.os.Handler
import android.os.Message
import android.util.Log
import android.view.TextureView
import kr.co.calmme.activity.HeartrateActivity
import java.util.*
import java.util.concurrent.CopyOnWriteArrayList
import kotlin.math.ceil
import kotlin.math.max

class OutputAnalyzer     // 생성자
    (private val activity: Activity, private val mainHandler: Handler) {
    private var store: MeasureStore? = null

    // 측정 주기
    private val measurementInterval = 45

    // 측정 길이?
    private val measurementLength = 15000 // ensure the number of data points is the power of two
    private val clipLength = 3500 // 3500
    private var detectedValleys = 0

    // 측정을 건너뛸 시간
    private var ticksPassed = 0
    private val valleys = CopyOnWriteArrayList<Long>()
    private var timer: CountDownTimer? = null
    private fun detectValley(): Boolean {
        val valleyDetectionWindowSize = 13
        val subList = store!!.getLastStdValues(valleyDetectionWindowSize)
        return if (subList.size < valleyDetectionWindowSize) {
            false
        } else {
            val referenceValue = subList[ceil((valleyDetectionWindowSize / 2f).toDouble())
                .toInt()].measurement
            for (measurement in subList) {
                if (measurement.measurement < referenceValue) return false
            }

            // filter out consecutive measurements due to too high measurement rate
            subList[ceil((valleyDetectionWindowSize / 2f).toDouble())
                .toInt()].measurement != subList[ceil((valleyDetectionWindowSize / 2f).toDouble())
                .toInt() - 1].measurement
        }
    }

    // 심박수 측정 메인 함수
    fun measurePulse(
        textureView: TextureView,
        cameraService: CameraService,
        mContext: HeartrateActivity
    ) {

        // 20 times a second, get the amount of red on the picture.
        // detect local minimums, calculate pulse.
        store = MeasureStore()
        detectedValleys = 0
        timer = object : CountDownTimer(measurementLength.toLong(), measurementInterval.toLong()) {
            // 초당 카메라 화면 분석 및 심박수 결과 저장
            override fun onTick(millisUntilFinished: Long) {

                // 설정한 측정 대기 시간에 도달하지 않았을 경우 함수 반환
                if (clipLength > ++ticksPassed * measurementInterval) return
                val thread = Thread {
                    val currentBitmap = textureView.bitmap
                    val pixelCount = textureView.width * textureView.height
                    var measurement = 0
                    val pixels = IntArray(pixelCount)
                    currentBitmap!!.getPixels(
                        pixels,
                        0,
                        textureView.width,
                        0,
                        0,
                        textureView.width,
                        textureView.height
                    )

                    // extract the red component
                    // https://developer.android.com/reference/android/graphics/Color.html#decoding
                    for (pixelIndex in 0 until pixelCount) {
                        measurement += pixels[pixelIndex] shr 16 and 0xff
                    }
                    // max int is 2^31 (2147483647) , so width and height can be at most 2^11,
                    // as 2^8 * 2^11 * 2^11 = 2^30, just below the limit
                    store!!.add(measurement)
                    if (detectValley()) {
                        detectedValleys += 1
                        valleys.add(store!!.lastTimestamp.time)
                        // in 13 seconds (13000 milliseconds), I expect 15 valleys. that would be a pulse of 15 / 130000 * 60 * 1000 = 69
                        val currentValue = String.format(
                            Locale.getDefault(),
                            activity.resources.getQuantityString(
                                R.plurals.measurement_output_template,
                                detectedValleys
                            ),
                            if (valleys.size == 1) 60f * detectedValleys / max(
                                1f,
                                (measurementLength - millisUntilFinished - clipLength) / 1000f
                            ) else 60f * (detectedValleys - 1) / max(
                                1f,
                                (valleys[valleys.size - 1] - valleys[0]) / 1000f
                            ),
                            detectedValleys,
                            1f * (measurementLength - millisUntilFinished - clipLength) / 1000f
                        )
                        sendMessage(HeartrateActivity.MESSAGE_UPDATE_REALTIME, currentValue)
                    }

                    // 분리된 스레드에서 심박수 결과 그래프로 출력
                    val chartDrawerThread =
                        Thread {
                            val bpm =
                                store!!.stdValues[store!!.stdValues.size - 1].measurement * 100
                            Log.d("Debug", bpm.toString())
                            mContext.addEntry(bpm)
                        }
                    chartDrawerThread.start()
                }

                // 측정 스레드 시
                thread.start()
            }

            override fun onFinish() {
                val stdValues = store!!.stdValues

                // clip the interval to the first till the last one - on this interval, there were detectedValleys - 1 periods
                val currentValue = String.format(
                    Locale.getDefault(),
                    activity.resources.getQuantityString(
                        R.plurals.measurement_output_template,
                        detectedValleys - 1
                    ),
                    60f * (detectedValleys - 1) / max(
                        1f,
                        (valleys[valleys.size - 1] - valleys[0]) / 1000f
                    ),
                    detectedValleys - 1,
                    1f * (valleys[valleys.size - 1] - valleys[0]) / 1000f
                )
                sendMessage(HeartrateActivity.MESSAGE_UPDATE_REALTIME, currentValue)
                val returnValueSb = StringBuilder()
                returnValueSb.append(currentValue)
                returnValueSb.append(activity.getString(R.string.row_separator))

//                returnValueSb.append(activity.getString(R.string.raw_values));
//                returnValueSb.append(activity.getString(R.string.row_separator));
                for (stdValueIdx in stdValues.indices) {
                    // stdValues.forEach((value) -> { // would require API level 24 instead of 21.
                    val value = stdValues[stdValueIdx]
                    returnValueSb.append(value.timestamp.time)
                    returnValueSb.append(activity.getString(R.string.separator))
                    returnValueSb.append(value.measurement)
                    returnValueSb.append(activity.getString(R.string.row_separator))
                }
                returnValueSb.append(activity.getString(R.string.output_detected_peaks_header))
                returnValueSb.append(activity.getString(R.string.row_separator))

                // add detected valleys location
                for (tick in valleys) {
                    returnValueSb.append(tick)
                    returnValueSb.append(activity.getString(R.string.row_separator))
                }
                sendMessage(HeartrateActivity.MESSAGE_UPDATE_FINAL, returnValueSb.toString())
                cameraService.stop()
            }
        }
        (timer as CountDownTimer).start()
    }

    fun stop() {
        if (timer != null) {
            timer!!.cancel()
        }
    }

    fun sendMessage(what: Int, message: Any?) {
        val msg = Message()
        msg.what = what
        msg.obj = message
        mainHandler.sendMessage(msg)
    }
}