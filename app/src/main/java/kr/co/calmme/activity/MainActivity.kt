package kr.co.calmme.activity


import android.os.Build
import android.os.Bundle
import android.view.MenuItem
import android.view.WindowManager
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_settings.view.*
import kr.co.calmme.fragment.CommunityFragment
import kr.co.calmme.R
import kr.co.calmme.fragment.*

class MainActivity : AppCompatActivity(), BottomNavigationView.OnNavigationItemSelectedListener {

    // Fragment 선언
    lateinit var homeFragment: HomeFragment
    lateinit var challengeFragment: ChallengeFragment
    lateinit var communityFragment: CommunityFragment
    lateinit var settingsFragment: SettingsFragment

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN)
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
//                supportFragmentManager.popBackStack()
                val transaction = supportFragmentManager.beginTransaction()
                transaction.replace(R.id.fragmentLayout, ChallengeFragment())
                transaction.commit()
//                supportFragmentManager.popBackStack("my_challenge", FragmentManager.POP_BACK_STACK_INCLUSIVE)
//                supportFragmentManager.popBackStack("my_page", FragmentManager.POP_BACK_STACK_INCLUSIVE)
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