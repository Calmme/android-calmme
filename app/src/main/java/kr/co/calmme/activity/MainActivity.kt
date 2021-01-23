package kr.co.mooreung.activity


import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentTransaction
import com.fxn.OnBubbleClickListener
import kotlinx.android.synthetic.main.activity_main.*
import kr.co.mooreung.R
import kr.co.mooreung.fragment.*

class MainActivity : AppCompatActivity() {

    // Fragment 선언
    lateinit var homeFragment: HomeFragment
    lateinit var meditationFragment: MeditationFragment
    lateinit var communityFragment: CommunityFragment
    lateinit var settingsFragment: SettingsFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main) // activity_main.xml에 존재하는 리소스를 액티비티에 연결

        // 최초 시작 시 보여줄 Fragment 설정
        homeFragment = HomeFragment()
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.frameLayout, homeFragment)
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
            .commit()

        // 하단 Navigation 버튼에 대한 리스너 및 Fragment 호출 등록
        navigationBar.addBubbleListener(object : OnBubbleClickListener {
            override fun onBubbleClick(id: Int) {
                when (id) {
                    // 메인 버튼
                    R.id.home -> {
                        homeFragment = HomeFragment()
                        supportFragmentManager
                            .beginTransaction()
                            .replace(R.id.frameLayout, homeFragment)
                            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                            .commit()
                    }
                    // 명상 버튼
                    R.id.meditation -> {
                        meditationFragment = MeditationFragment()
                        supportFragmentManager
                            .beginTransaction()
                            .replace(R.id.frameLayout, meditationFragment)
                            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                            .commit()
                    }
                    // 커뮤니티 버튼
                    R.id.group -> {
                        communityFragment = CommunityFragment()
                        supportFragmentManager
                            .beginTransaction()
                            .replace(R.id.frameLayout, communityFragment)
                            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                            .commit()
                    }
                    // 설정 버튼
                    R.id.settings -> {
                        settingsFragment = SettingsFragment()
                        supportFragmentManager
                            .beginTransaction()
                            .replace(R.id.frameLayout, settingsFragment)
                            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                            .commit()
                    }
                }
            }
        })

    }
}