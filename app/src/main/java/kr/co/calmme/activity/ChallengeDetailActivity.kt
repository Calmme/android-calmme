package kr.co.calmme.activity

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.skydoves.balloon.*
import kr.co.calmme.ChallengeDetailAdapter
import kr.co.calmme.ChallengeDetailData
import kr.co.calmme.R
import kr.co.calmme.model.Challenge
import kotlin.collections.ArrayList

class ChallengeDetailActivity : AppCompatActivity(), View.OnClickListener{
    private lateinit var challengeData : Challenge
    private val  recyclerItems = ArrayList<ChallengeDetailData>()
    private lateinit var recyclerAdapter: ChallengeDetailAdapter
    private var isStart = false
    private val TAG = "ChallengeDetailActivity"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_challenge_detail)

        findViewById<ImageView>(R.id.challenge_detail_back).setOnClickListener(this)
        findViewById<ImageView>(R.id.challenge_detail_tip).setOnClickListener(this)
        findViewById<Button>(R.id.challenge_detail_state).setOnClickListener(this)
        getExtra()
        setDisplay()
    }
    override fun onClick(v: View?) {
        when(v?.id){
            R.id.challenge_detail_back -> finish()
            R.id.challenge_detail_tip -> showTip(v)
            R.id.challenge_detail_state -> clickStateButton()
        }
    }

    fun getExtra(){
        if(intent.hasExtra("challenge")){
            challengeData = intent.getSerializableExtra("challenge") as Challenge
            Log.e(TAG,"\ncategory : ${challengeData.Category}\nCreatedAt : ${challengeData.CreatedAt}\nName : ${challengeData.Name}\nID : ${challengeData.Id}")
            Log.e(TAG,"\nRecommend : ${challengeData.Recommend}\nTotal : ${challengeData.Total}\nCompleteNum : ${challengeData.completeNum}")
        }
    }

    fun setDisplay(){
        if(challengeData != null){
            findViewById<TextView>(R.id.challenge_detail_name).text = challengeData.Name
            setProgressBar(challengeData.completeNum, challengeData.Total)
            setRecylerView(challengeData.Total)
            //button start 여부 처리필요
        }
    }

    fun showTip(view: View){
        val balloon = createBalloon(this) {
            setWidth(170)
            setTextSize(9f)
            setArrowSize(10)
            setPadding(7)
            setText("#우울할때 #힘들때 #사과먹고싶을때\n\n" + " ‘3분 나를 위한 명상’은 일하다 우울할 때나 울고 싶을때 감정이 힘들 때 들으면 효과 좋은 명상이에요")
            setArrowOrientation(ArrowOrientation.TOP)
            setArrowPositionRules(ArrowPositionRules.ALIGN_ANCHOR)
            setArrowPosition(0.5f)
            setCornerRadius(4f)
            setElevation(6)

            setBackgroundColorResource(R.color.originBlack)
            setBalloonAnimation(BalloonAnimation.FADE)
            setDismissWhenTouchOutside(true)
            setDismissWhenShowAgain(true)
        }

        balloon.showAlignBottom(view)
    }

    fun setRecylerView(total : Int){

        for(i in 0 until total)
            recyclerItems.add(ChallengeDetailData("2일차, 3분 나를 위한 명상", "lightBlack", true))

        recyclerAdapter = ChallengeDetailAdapter(recyclerItems)
        val recyclerView = findViewById<RecyclerView>(R.id.challenge_detail_recyler)
        recyclerView.adapter = recyclerAdapter
        val lm = LinearLayoutManager(this)
        recyclerView.layoutManager = lm
    }

    fun setProgressBar(complete : Int, total : Int){
        val progress = intArrayOf(
            R.id.challenge_detail_progress1,
            R.id.challenge_detail_progress2,
            R.id.challenge_detail_progress3,
            R.id.challenge_detail_progress4,
            R.id.challenge_detail_progress5,
            R.id.challenge_detail_progress6,
            R.id.challenge_detail_progress7
        )

        for(i in total until 7){
            findViewById<ImageView>(progress[i]).visibility = View.GONE
        }
        for(i in 0 until complete){
            Log.e(TAG,"TEST")
            findViewById<ImageView>(progress[i]).setBackgroundColor(Color.parseColor("#FFDF8E"))
        }
    }

    fun clickStateButton(){
        if(isStart == false){
            //progressbar 1칸 업데이트, 첫번째 컨텐츠 활성화
            findViewById<ImageView>(R.id.challenge_detail_progress1).setBackgroundColor(Color.parseColor("#FFDF8E"))
            findViewById<Button>(R.id.challenge_detail_state).setText("챌린지 도전중")
            recyclerItems[0].background = "darkGrey"
            recyclerItems[0].lock = false
            recyclerAdapter.notifyDataSetChanged()
            isStart = true
        }
    }
}