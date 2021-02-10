package kr.co.calmme.activity


import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentTransaction
import com.fxn.OnBubbleClickListener
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_main.*
import kr.co.calmme.fragment.CommunityFragment
import kr.co.calmme.R
import kr.co.calmme.fragment.*

class MainActivity : AppCompatActivity(), BottomNavigationView.OnNavigationItemSelectedListener {

    // Fragment 선언
    lateinit var homeFragment: HomeFragment
    lateinit var meditationFragment: MeditationFragment
    lateinit var communityFragment: CommunityFragment
    lateinit var settingsFragment: SettingsFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main) // activity_main.xml에 존재하는 리소스를 액티비티에 연결

//             val navigation: BottomNavigationView = findViewById(R.id.navigation)
        navigationBar.setOnNavigationItemSelectedListener(this)

        // 최초 시작 시 보여줄 Fragment 설정
        homeFragment = HomeFragment()
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragmentLayout, homeFragment)
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
            .commit()
/*
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
*/
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.main -> {
                val transaction = supportFragmentManager.beginTransaction()
                transaction.replace(R.id.fragmentLayout, HomeFragment())
                transaction.commit()
                return true
            }
            R.id.challenge -> {
                val transaction = supportFragmentManager.beginTransaction()
                transaction.replace(R.id.fragmentLayout, MeditationFragment())
                transaction.commit()
                return true
            }
            R.id.analysis -> {
                val transaction = supportFragmentManager.beginTransaction()
                transaction.replace(R.id.fragmentLayout, CommunityFragment())
                transaction.commit()
                return true
            }
            R.id.setting -> {
                val transaction = supportFragmentManager.beginTransaction()
                transaction.replace(R.id.fragmentLayout, SettingsFragment())
                transaction.commit()
                return true
            }
        }
        return false
    }
}