package kr.co.mooreung.activity

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.util.Log
import android.view.Surface
import android.view.TextureView
import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityCompat.OnRequestPermissionsResultCallback
import androidx.core.content.ContextCompat
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_heartscan.*
import kr.co.mooreung.CameraService
import kr.co.mooreung.OutputAnalyzer
import kr.co.mooreung.R

class HeartrateActivity : Activity(), OnRequestPermissionsResultCallback,
    OnChartValueSelectedListener {


    private val TAG = HeartrateActivity::class.java.simpleName

    private val cameraService = CameraService(this)
    private val REQUEST_CODE_CAMERA = 0
    private var menuNewMeasurementEnabled = false

    @SuppressLint("HandlerLeak")
    private val mainHandler: Handler = object : Handler() {
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            if (msg.what == MESSAGE_UPDATE_REALTIME) {
                (findViewById<View>(R.id.textView) as TextView).text = msg.obj.toString()
            }
            if (msg.what == MESSAGE_UPDATE_FINAL) {
                (findViewById<View>(R.id.editText) as EditText).setText(msg.obj.toString())
                // make sure menu items are enabled when it opens.
                menuNewMeasurementEnabled = true
            }
        }
    }
    private var analyzer: OutputAnalyzer? = null
    override fun onResume() {
        super.onResume()
        analyzer = OutputAnalyzer(this, heartChart, mainHandler)
        val cameraTextureView = findViewById<TextureView>(R.id.textureView2)
        val previewSurfaceTexture = cameraTextureView.surfaceTexture

        // justShared is set if one clicks the share button.
        if (previewSurfaceTexture != null) {
            // this first appears when we close the application and switch back - TextureView isn't quite ready at the first onResume.
            val previewSurface = Surface(previewSurfaceTexture)

            // show warning when there is no flash
            if (!this.packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH)) {
                Snackbar.make(
                    findViewById(R.id.constraintLayout),
                    getString(R.string.noFlashWarning),
                    Snackbar.LENGTH_LONG
                )
            }
            cameraService.start(previewSurface)
            analyzer!!.measurePulse(cameraTextureView, cameraService, this)
        }
    }

    override fun onPause() {
        Log.d(TAG, "onPause()")
        super.onPause()
        cameraService.stop()
        if (analyzer != null) analyzer!!.stop()
        analyzer = OutputAnalyzer(this, heartChart, mainHandler)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d(TAG, "onCreate()")
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_heartrate)
        ActivityCompat.requestPermissions(
            this, arrayOf(Manifest.permission.CAMERA),
            REQUEST_CODE_CAMERA
        )

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
        // 그리드 설정
        heartChart.setDrawGridBackground(false)
        // 줌 인아웃 설정
        heartChart.setPinchZoom(true)

        // 배경 색상 설정
        // heartChart.setBackgroundColor(Color.RED)
        val data = LineData()
        data.setValueTextColor(Color.BLACK)
        // add empty data
        heartChart.data = data

        // 개요 설정
        val l: Legend = heartChart.legend
        // modify the legend ...
        l.form = Legend.LegendForm.CIRCLE
        l.textColor = ContextCompat.getColor(this, R.color.colorAccent)

        // X축 설정
        val xl: XAxis = heartChart.xAxis
        xl.textColor = Color.BLACK
        xl.setDrawGridLines(false)
        xl.setAvoidFirstLastClipping(true)
        xl.isEnabled = false

        // Y축 좌측
        val leftAxis: YAxis = heartChart.axisLeft
        leftAxis.textColor = Color.BLACK
//        leftAxis.axisMaximum = 100f
//        leftAxis.axisMinimum = 30f
        leftAxis.setDrawGridLines(false)
        leftAxis.setDrawAxisLine(false)
        leftAxis.isEnabled = true

        // Y축 우측
        val rightAxis: YAxis = heartChart.axisRight
        rightAxis.isEnabled = false
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_CAMERA) {
            if (!(grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                Snackbar.make(
                    findViewById(R.id.constraintLayout),
                    getString(R.string.cameraPermissionRequired),
                    Snackbar.LENGTH_LONG
                ).show()
            }
        }
    }


    //    @Override
    //    public boolean onPrepareOptionsMenu(Menu menu) {
    //        Log.i("MENU","menu is being prepared");
    //
    //        MenuInflater inflater = getMenuInflater();
    //        inflater.inflate(R.menu.menu, menu);
    //
    //        menu.findItem(R.id.menuNewMeasurement).setEnabled(menuNewMeasurementEnabled);
    //        return super.onPrepareOptionsMenu(menu);
    //    }
//    fun onClickNewMeasurement(item: MenuItem?) {
//        analyzer = OutputAnalyzer(this, findViewById(R.id.graphTextureView), mainHandler)
//        val cameraTextureView = findViewById<TextureView>(R.id.textureView2)
//        val previewSurfaceTexture = cameraTextureView.surfaceTexture
//        if (previewSurfaceTexture != null) {
//            // this first appears when we close the application and switch back - TextureView isn't quite ready at the first onResume.
//            val previewSurface = Surface(previewSurfaceTexture)
//            cameraService.start(previewSurface)
//            analyzer!!.measurePulse(cameraTextureView, cameraService)
//        }
//    }

    companion object {
        const val MESSAGE_UPDATE_REALTIME = 1
        const val MESSAGE_UPDATE_FINAL = 2
    }

    // 그래프 설정
    private fun createSet(): LineDataSet {

        val set = LineDataSet(null, "BPM")
        set.mode = LineDataSet.Mode.CUBIC_BEZIER
        set.cubicIntensity = 0.2f
        set.axisDependency = YAxis.AxisDependency.LEFT
        set.color = ContextCompat.getColor(this, R.color.colorAccent)
        set.lineWidth = 2f

        // 점
        set.setDrawCircles(false)
        set.circleRadius = 4f
        set.setCircleColor(ContextCompat.getColor(this, R.color.colorAccent))
        set.highLightColor = ContextCompat.getColor(this, R.color.colorAccent)


        // 채우기
        set.fillDrawable = ContextCompat.getDrawable(this, R.drawable.fade_red)
        set.setDrawFilled(true)
//        set.fillAlpha = 65
//        set.fillColor = resources.getColor(R.color.colorAccent)
        set.valueTextColor = ContextCompat.getColor(this, R.color.colorAccent)
        set.valueTextSize = 16f
        set.setDrawValues(false)

        return set
    }

    fun addEntry(bpm: Float) {
        val data: LineData = heartChart.data
        var set = data.getDataSetByIndex(0)

        if (set == null) {
            Log.d(TAG, "set이 NULL이다!")
            set = createSet()
            data.addDataSet(set)
        }

        data.addEntry(Entry(set.entryCount.toFloat(), bpm), 0)
        data.notifyDataChanged()

        // 차트 뷰에게 데이터가 변경되었음을 알림
        heartChart.notifyDataSetChanged()

        // 그래프 최대 출력 개수
        heartChart.setVisibleXRangeMaximum(15f)
        // chart.setVisibleYRange(30, AxisDependency.LEFT);

        // 마지막 지점으로 뷰 이동
        heartChart.moveViewToX(data.entryCount.toFloat())

        // this automatically refreshes the chart (calls invalidate())
        // chart.moveViewTo(data.getXValCount()-7, 55f,
        // AxisDependency.LEFT);
    }

    // 그래프 점이 선택되었을 때
    override fun onValueSelected(e: Entry, h: Highlight?) {
        Log.i("Entry selected", e.toString())
    }

    // 아무것도 선택된 것이 없을 때
    override fun onNothingSelected() {
        Log.i("Nothing selected", "Nothing selected.")
    }
}