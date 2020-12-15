package kr.co.mooreung.activity

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.Legend.LegendForm
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.components.YAxis.AxisDependency
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import com.github.mikephil.charting.utils.ColorTemplate
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.activity_heartscan.*
import kr.co.mooreung.R
import net.kibotu.heartrateometer.HeartRateOmeter
import net.kibotu.kalmanrx.jama.Matrix
import net.kibotu.kalmanrx.jkalman.JKalman


class HeartScanActivity : AppCompatActivity(), OnChartValueSelectedListener {

    private var subscription: CompositeDisposable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_heartscan)

        // 차트 그래프
        heartChart.setOnChartValueSelectedListener(this)
        // 차트 설명 텍스트 설정
        heartChart.description.isEnabled = true
        heartChart.description.text = "심박수 측정 그래프"
        // 터치 제스쳐 설정
        heartChart.setTouchEnabled(true)
        // 스케일, 드래그 설정
        heartChart.isDragEnabled = true
        heartChart.setScaleEnabled(true)
        heartChart.setDrawGridBackground(false)
        // if disabled, scaling can be done on x- and y-axis separately
        heartChart.setPinchZoom(true)
        // 배경 색상 설정
        // heartChart.setBackgroundColor(Color.LTGRAY)
        val data = LineData()
        data.setValueTextColor(Color.BLACK)
        // add empty data
        heartChart.data = data
        // get the legend (only possible after setting data)
        val l: Legend = heartChart.legend
        // modify the legend ...
        l.form = LegendForm.LINE
        l.textColor = Color.BLACK

        val xl: XAxis = heartChart.xAxis
        xl.textColor = Color.BLACK
        xl.setDrawGridLines(false)
        xl.setAvoidFirstLastClipping(true)
        xl.isEnabled = true

        val leftAxis: YAxis = heartChart.axisLeft
        leftAxis.textColor = Color.BLACK
//        leftAxis.axisMaximum = 100f
//        leftAxis.axisMinimum = 0f
        leftAxis.setDrawGridLines(false)

        val rightAxis: YAxis = heartChart.axisRight
        rightAxis.isEnabled = false
    }

    // 권한 확인
    private fun startWithPermissionCheck() {
        if (!hasPermission(Manifest.permission.CAMERA)) {
            checkPermissions(REQUEST_CAMERA_PERMISSION, Manifest.permission.CAMERA)
            return
        }

        val kalman = JKalman(2, 1)

        // measurement [x]
        val m = Matrix(1, 1)

        // transitions for x, dx
        val tr = arrayOf(doubleArrayOf(1.0, 0.0), doubleArrayOf(0.0, 1.0))
        kalman.transition_matrix = Matrix(tr)

        // 1s somewhere?
        kalman.error_cov_post = kalman.error_cov_post.identity()

        // Thread 형태로 동작
        val bpmUpdates = HeartRateOmeter()
            .withAverageAfterSeconds(10) // BPM 측정을 위한 캘리브리션 대기시간
            .setFingerDetectionListener(this::onFingerChange)
            .bpmUpdates(preview)
            .subscribe({

                if (it.value == 0)
                    return@subscribe

                m.set(0, 0, it.value.toDouble())

                // state [x, dx]
                val s = kalman.Predict()

                // corrected state [x, dx]
                val c = kalman.Correct(m)

                val bpm = it.copy(value = c.get(0, 0).toInt())
                // BPM 변화 감지
                Log.v(
                    "HeartScanActivity",
                    "BPM: ${it.value} => ${bpm.value}, Finger status: ${bpm.type}"
                )
                if (bpm.type.toString() == "ON") {
                    onBpm(bpm)
                    addEntry(bpm.value.toFloat())
                }

            }, Throwable::printStackTrace)

        subscription?.add(bpmUpdates)
    }

    @SuppressLint("SetTextI18n")
    private fun onBpm(bpm: HeartRateOmeter.Bpm) {
        label.text = "$bpm bpm"
    }

    // 손가락 감지 여부
    private fun onFingerChange(fingerDetected: Boolean) {
        finger.text = "$fingerDetected"
    }

    private fun addEntry(bpm: Float) {
        val data: LineData = heartChart.data
        var set = data.getDataSetByIndex(0)
        // set.addEntry(...); // can be called as well
        if (set == null) {
            set = createSet()
            data.addDataSet(set)
        }
        data.addEntry(Entry(set.entryCount.toFloat(), bpm), 0)
        data.notifyDataChanged()

        // 차트 뷰에게 데이터가 변경되었음을 알림
        heartChart.notifyDataSetChanged()

        // 그래프 최대 출력 개수
        heartChart.setVisibleXRangeMaximum(30f)
        // chart.setVisibleYRange(30, AxisDependency.LEFT);

        // 마지막 지점으로 뷰 이동
        heartChart.moveViewToX(data.entryCount.toFloat())

        // this automatically refreshes the chart (calls invalidate())
        // chart.moveViewTo(data.getXValCount()-7, 55f,
        // AxisDependency.LEFT);
    }

    private fun createSet(): LineDataSet {
        val set = LineDataSet(null, "Dynamic Data")
        set.axisDependency = AxisDependency.LEFT
        set.color = ColorTemplate.PASTEL_COLORS[1]
        set.setCircleColor(ColorTemplate.PASTEL_COLORS[1])
        set.lineWidth = 2f
        set.circleRadius = 4f
        set.fillAlpha = 65
        set.fillColor = ColorTemplate.getHoloBlue()
        set.highLightColor = Color.rgb(244, 117, 117)
        set.valueTextColor = Color.WHITE
        set.valueTextSize = 9f
        set.setDrawValues(false)
        return set
    }

    override fun onResume() {
        super.onResume()

        dispose()
        subscription = CompositeDisposable()

        startWithPermissionCheck()
    }

    override fun onPause() {
        dispose()
        super.onPause()
    }

    private fun dispose() {
        if (subscription?.isDisposed == false)
            subscription?.dispose()
    }

    companion object {
        private const val REQUEST_CAMERA_PERMISSION = 123
    }

    private fun checkPermissions(callbackId: Int, vararg permissionsId: String) {
        when {
            !hasPermission(*permissionsId) -> try {
                ActivityCompat.requestPermissions(this, permissionsId, callbackId)
            } catch (ex: Exception) {
                ex.printStackTrace()
            }
        }
    }

    private fun hasPermission(vararg permissionsId: String): Boolean {
        var hasPermission = true

        permissionsId.forEach { permission ->
            hasPermission = hasPermission
                    && ContextCompat.checkSelfPermission(
                this,
                permission
            ) == PackageManager.PERMISSION_GRANTED
        }

        return hasPermission
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            REQUEST_CAMERA_PERMISSION -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    startWithPermissionCheck()
                }
            }
        }
    }

    override fun onValueSelected(e: Entry, h: Highlight?) {
        Log.i("Entry selected", e.toString())
    }

    override fun onNothingSelected() {
        Log.i("Nothing selected", "Nothing selected.")
    }
}
