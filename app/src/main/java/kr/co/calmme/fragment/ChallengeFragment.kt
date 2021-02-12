package kr.co.calmme.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_challenge.*
import kr.co.calmme.ProgressDialog
import kr.co.calmme.R
import kr.co.calmme.adapter.OngoingChallengeListAdapter
import kr.co.calmme.model.Challenge
import kr.co.calmme.model.CheckList
import kr.co.calmme.server.Retrofit.service
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ChallengeFragment : Fragment() {
    var list: ArrayList<Challenge> = ArrayList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_challenge, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val dialog = ProgressDialog(view.context)
        dialog.progress()

        val call = service.onGoingChallengeList()
        call.enqueue(object : Callback<List<Challenge>> {
            override fun onResponse(call: Call<List<Challenge>>, response: Response<List<Challenge>>) {
                val challengeList: List<Challenge>? = response.body()    //placeList에 값 담겨있음 List형태
                Log.d("결과", "성공 : ${response.raw()}\n") //성공여부 로그
                Log.d("결과", "성공 : ${response.body()}\n") //성공여부 로그

                //Log.d("결과", "성공 : " + challengeList!!.checkRoomList[0].Name) //성공여부 로그

                /*
                for (p in challengeList!!) {
                    list.add(p)
                }
                */

            }

            override fun onFailure(call: Call<List<Challenge>>, t: Throwable) {
                Log.d("결과", "실패 : ${t.message}")
            }
        })

        for (i in 1..11) {
            list.add(Challenge(i.toLong(), "Test$i", "https://", "D-3", true, "2020-01-01"))
            list.add(Challenge(i.toLong(), "Test$i", "https://", "D-3", true, "2020-01-01"))

        }
        val adapter = OngoingChallengeListAdapter(list)
        ongoing_challenge_list.adapter = adapter
        val layoutManager = GridLayoutManager(this.context, 2)
        layoutManager.orientation = GridLayoutManager.HORIZONTAL
        ongoing_challenge_list.layoutManager = layoutManager

    }
}