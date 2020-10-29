package kr.co.mureung


import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.fxn.OnBubbleClickListener
import com.fxn.ariana.ArianaBackgroundListener
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    // 화면이 생성되면 처음으로 호출되는 함수
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main) // activity_main.xml에 존재하는 리소스를 액티비티에 연결

        // 네비게이션의 각 버튼에 대한 리스너 설정
        navigationbar.addBubbleListener(object : OnBubbleClickListener {
            override fun onBubbleClick(id: Int) {
                when (id) {
                    R.id.home -> viewpager.currentItem = 0  // 메인 버튼
                    R.id.meditation -> viewpager.currentItem = 1  // 명상 버튼
                    R.id.group -> viewpager.currentItem = 2  // 그룹 버튼
                    R.id.settings -> viewpager.currentItem = 3  // 설정 버튼
                }
            }
        })

        navigationbar.setupBubbleTabBar(viewpager)  // 네비게이션에 뷰 페이저 설정
        viewpager.setDurationScroll(1000)  // 뷰 페이저의 화면 넘어가는 시간 설정: 1000 == 1초
        viewpager.adapter = ViewPagerAdapter(supportFragmentManager).apply {
            list = ArrayList<String>().apply {
                add("내가 메인화면이다")
                add("너는 뭔데")
                add("박다정 바보")
                add("내가더 ㅎㅎㅎ")
            }
        }

        // 화면 변경시 동작할 함수 적용
        viewpager.addOnPageChangeListener(
            ArianaBackgroundListener(
                getColors(),
                iv,
                viewpager
            )
        )

    }

    private fun getColors(): IntArray {
        return intArrayOf(
            ContextCompat.getColor(this, R.color.home),
            ContextCompat.getColor(this, R.color.logger),
            ContextCompat.getColor(this, R.color.documents),
            ContextCompat.getColor(this, R.color.settings),
            ContextCompat.getColor(this, R.color.home)
        )
    }
}