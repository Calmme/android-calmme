package kr.co.calmme.activity

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.skydoves.balloon.*
import kotlinx.android.synthetic.main.activity_challenge_detail.*
import kr.co.calmme.ChallengeDetailAdapter
import kr.co.calmme.ChallengeDetailData
import kr.co.calmme.R
import java.util.*
import kotlin.collections.ArrayList

class ChallengeDetailActivity : AppCompatActivity(), View.OnClickListener{
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_challenge_detail)

        findViewById<ImageView>(R.id.challenge_detail_back).setOnClickListener(this)
        findViewById<ImageView>(R.id.challenge_detail_tip).setOnClickListener(this)
        setRecylerView()
        setProgressBar()
    }
    override fun onClick(v: View?) {
        when(v?.id){
            R.id.challenge_detail_back -> finish()
            R.id.challenge_detail_tip -> showTip(v)
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

    fun setRecylerView(){
        val items = ArrayList<ChallengeDetailData>()

        items.add(ChallengeDetailData("1일차, 3분 나를 위한 명상", "Grey", false))
        items.add(ChallengeDetailData("2일차, 3분 나를 위한 명상", "lightBlack", true))
        items.add(ChallengeDetailData("2일차, 3분 나를 위한 명상", "lightBlack", true))
        items.add(ChallengeDetailData("2일차, 3분 나를 위한 명상", "lightBlack", true))
        items.add(ChallengeDetailData("2일차, 3분 나를 위한 명상", "lightBlack", true))
        items.add(ChallengeDetailData("2일차, 3분 나를 위한 명상", "lightBlack", true))

        val ChallengeDetailAdapter = ChallengeDetailAdapter(items)
        val recyclerView = findViewById<RecyclerView>(R.id.challenge_detail_recyler)
        recyclerView.adapter = ChallengeDetailAdapter
        val lm = LinearLayoutManager(this)
        recyclerView.layoutManager = lm
    }

    fun setProgressBar(){
        val progress = intArrayOf(
            R.id.challenge_detail_progress1,
            R.id.challenge_detail_progress2,
            R.id.challenge_detail_progress3,
            R.id.challenge_detail_progress4,
            R.id.challenge_detail_progress5,
            R.id.challenge_detail_progress6,
            R.id.challenge_detail_progress7
        )

        for(i in 0..5){
            findViewById<ImageView>(progress[i]).setBackgroundColor(Color.parseColor("#FFDF8E"))
        }

    }
}