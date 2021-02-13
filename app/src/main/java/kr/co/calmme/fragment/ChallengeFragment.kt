package kr.co.calmme.fragment

import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_challenge.*
import kotlinx.android.synthetic.main.view_challenge.view.*
import kr.co.calmme.ProgressDialog
import kr.co.calmme.R
import kr.co.calmme.adapter.FindChallengeListAdapter
import kr.co.calmme.adapter.OngoingChallengeListAdapter
import kr.co.calmme.localDB.MyChallenge.MyChallenge
import kr.co.calmme.localDB.MyChallenge.MyChallengeDatabase
import kr.co.calmme.model.Challenge
import kr.co.calmme.model.CheckList
import kr.co.calmme.server.Retrofit.service
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ChallengeFragment : Fragment() {
    var list: List<Challenge> = ArrayList()
    var recommendList: ArrayList<Challenge> = ArrayList()
    var threeDayList: ArrayList<Challenge> = ArrayList()
    var oneWeekList: ArrayList<Challenge> = ArrayList()
    var specialList: ArrayList<Challenge> = ArrayList()
    var newList: ArrayList<Challenge> = ArrayList()
    lateinit var adapter: OngoingChallengeListAdapter
    lateinit var adapter2: FindChallengeListAdapter

    var db: MyChallengeDatabase? = null
    var myChallengeList = mutableListOf<MyChallenge>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        return inflater.inflate(R.layout.fragment_challenge, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //초기화
        db = MyChallengeDatabase.getInstance(view.context)
        //이전에 저장한 내용 모두 불러와서 추가하기
        val savedMyChallenges = db!!.myChallengeDao().getAll()
        if (savedMyChallenges.isNotEmpty()) {
            myChallengeList.addAll(savedMyChallenges)
        }


        star_button.setOnClickListener {
            val transaction = activity!!.supportFragmentManager.beginTransaction()
            transaction.replace(R.id.fragmentLayout, MyChallengeFragment())
            transaction.addToBackStack("my_challenge")
            transaction.commit()
        }
        my_page_button.setOnClickListener {
            val transaction = activity!!.supportFragmentManager.beginTransaction()
            transaction.replace(R.id.fragmentLayout, MyPageFragment())
            transaction.addToBackStack("my_page")
            transaction.commit()
        }

        val layoutManager = GridLayoutManager(this.context, 2)
        layoutManager.orientation = GridLayoutManager.HORIZONTAL
        ongoing_challenge_list.layoutManager = layoutManager
        val layoutManager2 = LinearLayoutManager(this.context)
        layoutManager2.orientation = GridLayoutManager.HORIZONTAL
        find_challenge_list.layoutManager = layoutManager2

        val dialog = ProgressDialog(view.context)
        dialog.progress()

        val call = service.onGoingChallengeList()
        call.enqueue(object : Callback<CheckList> {
            override fun onResponse(call: Call<CheckList>, response: Response<CheckList>) {
                val challengeList: CheckList? = response.body()    //placeList에 값 담겨있음 List형태
                Log.d("결과", "성공 : ${response.raw()}\n") //성공여부 로그
                list = challengeList!!.list

                for ((cnt, item) in list.withIndex()) {
                    if (item.Recommend == "1") {
                        recommendList.add(item)
                    }
                    if (item.Total == 3) {
                        threeDayList.add(item)
                    }
                    if (item.Total == 7) {
                        oneWeekList.add(item)
                    }
                    if (item.Category == "special") {
                        specialList.add(item)
                    }
                    if (cnt < 4) {
                        newList.add(item)
                    }

                    Log.d("list", item.Name + "/" + item.Total)
                }

                for(tmp in myChallengeList) {
                    Log.d("DEBUG", tmp.name)
                    for(item in list) {
                        if(item.Id.toLong() == tmp.id) {
                            item.isScraped = true
                        }
                    }
                }

                adapter = OngoingChallengeListAdapter(list)
                adapter.setItemClickListener(ChallengeClickListener())

                adapter2 = FindChallengeListAdapter(recommendList)
                adapter2.setItemClickListener(ChallengeClickListener())

                ongoing_challenge_list.adapter = adapter
                find_challenge_list.adapter = adapter2

                dialog.finish()
            }

            override fun onFailure(call: Call<CheckList>, t: Throwable) {
                Log.d("결과", "실패 : ${t.message}")
                dialog.finish()
            }
        })

        recommend_tab.setOnClickListener(TabClickListener())
        new_tab.setOnClickListener(TabClickListener())
        three_days_tab.setOnClickListener(TabClickListener())
        week_tab.setOnClickListener(TabClickListener())
        special_tab.setOnClickListener(TabClickListener())
    }

    inner class TabClickListener : View.OnClickListener {
        override fun onClick(v: View?) {
            var adapter2: FindChallengeListAdapter? = null
            when (v?.id) {
                R.id.recommend_tab -> {
                    adapter2 = FindChallengeListAdapter(recommendList)
                    clipTab(v.id)
                }
                R.id.new_tab -> {
                    adapter2 = FindChallengeListAdapter(newList)
                    clipTab(v.id)
                }
                R.id.three_days_tab -> {
                    adapter2 = FindChallengeListAdapter(threeDayList)
                    clipTab(v.id)
                }
                R.id.week_tab -> {
                    adapter2 = FindChallengeListAdapter(oneWeekList)
                    clipTab(v.id)
                }
                R.id.special_tab -> {
                    adapter2 = FindChallengeListAdapter(specialList)
                    clipTab(v.id)
                }
            }

            adapter2!!.setItemClickListener(ChallengeClickListener())
            adapter2!!.setItemClickListener(ChallengeClickListener())
            find_challenge_list.adapter = adapter2
        }
    }

    // 챌린지 클릭 리스너
    inner class ChallengeClickListener : FindChallengeListAdapter.OnItemClickListener,
        OngoingChallengeListAdapter.OnItemClickListener {
        private var doubleClickFlag = 0
        private var doubleClicked = 0
        private val CLICK_DELAY: Long = 300
        override fun onClick(v: View, position: Int) {
            val item = list[position]
            doubleClickFlag++
            doubleClicked = 0
            val handler = Handler()
            val clickRunnable = Runnable {
                doubleClickFlag = 0
                if (doubleClicked == 0) {
                    //item.title = item.title + "1"
                    /* 챌린지 클릭 시 세부 정보 표기로 ..
            activity?.let {
                val intent = Intent(context, EntranceActivity::class.java)
                intent.putExtra("challenge", item)
                startActivity(intent)
            }*/
                    Toast.makeText(context, "그냥 클릭이 되었음.", Toast.LENGTH_LONG)
                    Log.d("CLICK", "그냥 클릭이 되었음.")
                    adapter.notifyDataSetChanged()
                }
            }
            if (doubleClickFlag == 1) {
                handler.postDelayed(clickRunnable, CLICK_DELAY)
            } else if (doubleClickFlag == 2) {

                doubleClickFlag = 0
                doubleClicked = 1

                // 해당 챌린지 즐겨찾기 효과 주기
                for ((idx, c) in list.withIndex()) {
                    if (c.Name == v.challenge_title.text) {
                        var data = MyChallenge(0,"","","",0,"",0)
                        list[idx].isScraped = true
                        list[idx].toMyChallenge(data)
                        Log.d("DEBUG", data.name)
                        db?.myChallengeDao()?.insertAll(data)
                        break
                    }
                }

                adapter.notifyDataSetChanged()
                adapter2.notifyDataSetChanged()
                Log.d("CLICK", "더블 클릭이 되었음.")
            }
        }

    }

    // 탭 변경 시 뷰 변경 함수 (수정 필요)
    fun clipTab(clickedId: Int) {
        val tabList: List<Int> = listOf(
            R.id.recommend_tab,
            R.id.new_tab,
            R.id.three_days_tab,
            R.id.week_tab,
            R.id.special_tab
        )
        val viewList: List<CardView> =
            listOf(recommend_tab, new_tab, three_days_tab, week_tab, special_tab)
        val textList: List<TextView> =
            listOf(recommend_text, new_text, three_days_text, week_text, special_text)

        for ((cnt, item) in viewList.withIndex()) {
            if (clickedId == tabList[cnt])
                item.setCardBackgroundColor(context!!.getColor(R.color.middleGrey))
            else
                item.setCardBackgroundColor(context!!.getColor(R.color.lightBlack))
        }
        for ((cnt, item) in textList.withIndex()) {
            if (clickedId == tabList[cnt])
                item.setTextColor(context!!.getColor(R.color.black))
            else
                item.setTextColor(context!!.getColor(R.color.modernGrey))
        }
    }


}

