package kr.co.calmme.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_home.*
import kr.co.calmme.R
import kr.co.calmme.activity.HeartrateActivity

class HomeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        // 심박수 측정 화면 버튼
        heartScan.setOnClickListener {
            startActivity(Intent(context, HeartrateActivity::class.java))
        }

        super.onViewCreated(view, savedInstanceState)
    }
}