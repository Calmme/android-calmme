package kr.co.calmme.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_home.view.*
import kotlinx.android.synthetic.main.item_challenge.view.*
import kotlinx.android.synthetic.main.view_challenge.view.*
import kr.co.calmme.R
import kr.co.calmme.model.Challenge

class OngoingChallengeListAdapter(private val itemList: List<Challenge>) :
    RecyclerView.Adapter<ChallengeViewHolder>() {

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChallengeViewHolder {
        val inflatedView =
            LayoutInflater.from(parent.context).inflate(R.layout.item_challenge, parent, false)
        return ChallengeViewHolder(inflatedView)
    }

    override fun onBindViewHolder(holder: ChallengeViewHolder, position: Int) {
        val item = itemList[position]
        holder.itemView.setOnClickListener {
            itemClickListener.onClick(it, position)
        }
        holder.apply {
            bind(item)
        }
    }

    interface OnItemClickListener {
        fun onClick(v: View, position: Int)
    }

    private lateinit var itemClickListener: OnItemClickListener

    fun setItemClickListener(itemClickListener: OnItemClickListener) {
        this.itemClickListener = itemClickListener
    }
}

class ChallengeViewHolder(v: View) : RecyclerView.ViewHolder(v) {
    var view: View = v

    fun bind(item: Challenge) {
        view.view.challenge_title.text = item.Name
/*
        if(item.totalDay <= 3) {
            view.view.fourth.visibility = View.INVISIBLE
            view.view.fifth.visibility = View.INVISIBLE
            view.view.sixth.visibility = View.INVISIBLE
            view.view.seventh.visibility = View.INVISIBLE
        }

        if(item.completeNum > 0)
            view.view.totalCount.text = item.completeNum.toString()*/
//        view.place_tag.text = item.tag
    }
}