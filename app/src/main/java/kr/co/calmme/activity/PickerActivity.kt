package kr.co.calmme.activity

import android.graphics.Typeface
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.igalata.bubblepicker.BubblePickerListener
import com.igalata.bubblepicker.adapter.BubblePickerAdapter
import com.igalata.bubblepicker.model.BubbleGradient
import com.igalata.bubblepicker.model.PickerItem
import kotlinx.android.synthetic.main.activity_picker.*
import kr.co.calmme.R


class PickerActivity : AppCompatActivity() {

    private val boldTypeface by lazy { Typeface.createFromAsset(assets, ROBOTO_BOLD) }
    private val mediumTypeface by lazy { Typeface.createFromAsset(assets, ROBOTO_MEDIUM) }
    private val regularTypeface by lazy { Typeface.createFromAsset(assets, ROBOTO_REGULAR) }

    // 폰트 설정
    companion object {
        private const val ROBOTO_BOLD = "kakao_bold.otf"
        private const val ROBOTO_MEDIUM = "kakao_regular.otf"
        private const val ROBOTO_REGULAR = "kakao_light.otf"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_picker)

        // 레이아웃 텍스트 폰트 연결
        titleTextView.typeface = mediumTypeface
        subtitleTextView.typeface = boldTypeface
        hintTextView.typeface = regularTypeface
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            subtitleTextView.letterSpacing = 0.06f
            hintTextView.letterSpacing = 0.05f
        }

        val titles = resources.getStringArray(R.array.countries)
        val colors = resources.obtainTypedArray(R.array.colors)
        val images = resources.obtainTypedArray(R.array.images)

        picker.adapter = object : BubblePickerAdapter {

            override val totalCount = titles.size

            override fun getItem(position: Int): PickerItem {
                return PickerItem().apply {
                    title = titles[position]
                    gradient = BubbleGradient(
                        colors.getColor((position * 2) % 8, 0),
                        colors.getColor((position * 2) % 8 + 1, 0), BubbleGradient.VERTICAL
                    )
                    typeface = mediumTypeface
                    textColor = ContextCompat.getColor(this@PickerActivity, android.R.color.white)
                    backgroundImage = ContextCompat.getDrawable(
                        this@PickerActivity,
                        images.getResourceId(position, 0)
                    )
                }
            }
        }

        colors.recycle()
        images.recycle()

        // 버블 오브젝트 크기
        picker.bubbleSize = 1
        // 버블 오브젝트 가장자리에서 생성
        picker.centerImmediately = true
        // 버블 오브젝트가 클릭되었을 경우 동작할 함수
        picker.listener = object : BubblePickerListener {
            // 버블 오브젝트 선택
            override fun onBubbleSelected(item: PickerItem) = toast("${item.title} selected")

            // 버블 오브젝트 선택 해제
            override fun onBubbleDeselected(item: PickerItem) = toast("${item.title} deselected")
        }
    }

    override fun onResume() {
        super.onResume()
        picker.onResume()
    }

    override fun onPause() {
        super.onPause()
        picker.onPause()
    }

    private fun toast(text: String) = Toast.makeText(this, text, Toast.LENGTH_SHORT).show()

}