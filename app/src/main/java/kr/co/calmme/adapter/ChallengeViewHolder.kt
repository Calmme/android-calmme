package kr.co.calmme.adapter

import android.os.Handler
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_challenge.view.*
import kotlinx.android.synthetic.main.view_challenge.view.*
import kr.co.calmme.model.Challenge

class ChallengeViewHolder(v: View) : RecyclerView.ViewHolder(v) {
    var view: View = v

    fun bind(item: Challenge) {

        view.view.challenge_title.text = item.Name

        if (item.Total <= 3) {
            view.view.fourth.visibility = View.INVISIBLE
            view.view.fifth.visibility = View.INVISIBLE
            view.view.sixth.visibility = View.INVISIBLE
            view.view.seventh.visibility = View.INVISIBLE
        } else {
            view.view.fourth.visibility = View.VISIBLE
            view.view.fifth.visibility = View.VISIBLE
            view.view.sixth.visibility = View.VISIBLE
            view.view.seventh.visibility = View.VISIBLE
        }

        if (item.completeNum > 0)
            view.view.totalCount.text = item.completeNum.toString()

        if(item.isScraped)
            view.view.star.visibility = View.VISIBLE
        else
            view.view.star.visibility = View.INVISIBLE

    }

}