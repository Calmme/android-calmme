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

    // 저점 찾기
    private fun detectValley(): Boolean {
        val valleyDetectionWindowSize = 13
        val subList = store!!.getLastStdValues(valleyDetectionWindowSize) // 마지막 13개 값 추출

        // 만약 저장된 색상값이 13개 미만일 때
        return if (subList.size < valleyDetectionWindowSize) {
            false
        // 이상일 때
        } else {
            val referenceValue = subList[ceil((valleyDetectionWindowSize / 2f).toDouble()) // 중간값 뽑아내고
                .toInt()].measurement
            for (measurement in subList) { // 13개값에서 탐색
                if (measurement.measurement < referenceValue) return false // 최저점이 아니면 반환
            }

            // 평지 저점 거르기 (노확실)
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
        store = MeasureStore() // 측정 데이터 담는 오브젝트
        detectedValleys = 0

        // 타이머 스레드 선언
        timer = object : CountDownTimer(measurementLength.toLong(), measurementInterval.toLong()) {
            // 초당 카메라 화면 분석 및 심박수 결과 저장
            override fun onTick(millisUntilFinished: Long) {

                // 설정한 측정 대기 시간에 도달하지 않았을 경우 함수 반환
                if (clipLength > ++ticksPassed * measurementInterval) return

                // 타이머 스레드 안에서 내부 스레드 생성
                val thread = Thread {
                    val currentBitmap = textureView.bitmap
                    val pixelCount = textureView.width * textureView.height
                    var measurement = 0
                    val pixels = IntArray(pixelCount) // 픽셀 초기화
                    currentBitmap!!.getPixels(
                        pixels,
                        0,
                        textureView.width,
                        0,
                        0,
                        textureView.width,
                        textureView.height
                    )

                    // 빨간색 화면에대해서 픽셀 색상값 추출
                    // https://developer.android.com/reference/android/graphics/Color.html#decoding
                    for (pixelIndex in 0 until pixelCount) {
                        measurement += pixels[pixelIndex] shr 16 and 0xff
                    }
                    // max int is 2^31 (2147483647) , so width and height can be at most 2^11,
                    // as 2^8 * 2^11 * 2^11 = 2^30, just below the limit
                    store!!.add(measurement) // 추출한 색상 값을 저장
                    // 저점을 찾았으면 bpm 계
                    if (detectValley()) {

                        detectedValleys += 1
                        valleys.add(store!!.lastTimestamp.time)
                        // 저점 다 찾으면,
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
                        // 최종 계산된 bpm을 메인핸들러로 전달
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