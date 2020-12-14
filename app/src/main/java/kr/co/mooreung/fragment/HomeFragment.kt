package kr.co.mooreung.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_settings.*
import kr.co.mooreung.R
import kr.co.mooreung.activity.HeartScanActivity
import kr.co.mooreung.activity.LoginActivity

class HomeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        // 로그아웃 버튼
        heartScan.setOnClickListener { view ->
            startActivity(Intent(context, HeartScanActivity::class.java))
        }
        super.onViewCreated(view, savedInstanceState)
    }
}