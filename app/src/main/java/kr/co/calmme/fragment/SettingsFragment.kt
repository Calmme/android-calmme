package kr.co.calmme.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.kakao.sdk.user.UserApiClient
import kotlinx.android.synthetic.main.fragment_settings.*
import kr.co.calmme.R

class SettingsFragment : Fragment() {

    companion object {
        private const val TAG = "SettingsFragment"
    }
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
            UserApiClient.instance.logout { error ->
                if (error != null) {
                    Log.e(SettingsFragment.TAG, "로그아웃 실패. SDK에서 토큰 삭제됨", error)
                }
                else {
                    Log.i(SettingsFragment.TAG, "로그아웃 성공. SDK에서 토큰 삭제됨")
                    System.exit(0)
                }
            }
        }
        //탈퇴 버튼
        byeButton.setOnClickListener {
            UserApiClient.instance.unlink { error ->
                if (error != null) {
                    Log.e(SettingsFragment.TAG, "연결 끊기 실패", error)
                }
                else {
                    Log.i(SettingsFragment.TAG, "연결 끊기 성공. SDK에서 토큰 삭제 됨")
                    System.exit(0)
                }
            }
        }
        super.onViewCreated(view, savedInstanceState)
    }

}