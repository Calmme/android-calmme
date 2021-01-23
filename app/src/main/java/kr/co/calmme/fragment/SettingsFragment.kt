package kr.co.mooreung.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.fragment_settings.*
import kr.co.mooreung.R
import kr.co.mooreung.activity.LoginActivity
import kr.co.mooreung.activity.MainActivity

class SettingsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_settings, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        // 로그아웃 버튼
        signOutButton.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            // 현재 프래그먼트 삭제
            activity?.supportFragmentManager
                ?.beginTransaction()
                ?.remove(this)
                ?.commit()
            startActivity(Intent(context, LoginActivity::class.java))
            activity?.finish()
        }
        super.onViewCreated(view, savedInstanceState)
    }

}