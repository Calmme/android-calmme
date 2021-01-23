package kr.co.calmme.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_select_camera.*
import kr.co.calmme.R

class SelectCameraActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_camera)

        cameraSelectionBox1.setOnClickListener(BoxSelectListener())
        cameraSelectionBox2.setOnClickListener(BoxSelectListener())

        checkButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    inner class BoxSelectListener : View.OnClickListener {
        override fun onClick(v:View?) {
            when (v?.id) {
                R.id.cameraSelectionBox1 -> {
                    v.setBackgroundResource(R.drawable.layout_primary_stroke_background)
                    cameraSelectionBox2.setBackgroundResource(R.drawable.layout_grey_stroke_background)
                }
                R.id.cameraSelectionBox2 -> {
                    v.setBackgroundResource(R.drawable.layout_primary_stroke_background)
                    cameraSelectionBox1.setBackgroundResource(R.drawable.layout_grey_stroke_background)
                }
            }
        }
    }
}