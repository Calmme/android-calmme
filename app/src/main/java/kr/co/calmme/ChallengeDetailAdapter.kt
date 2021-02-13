package kr.co.calmme

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView


class ChallengeDetailAdapter(val items: ArrayList<ChallengeDetailData>) :  RecyclerView.Adapter<ChallengeDetailAdapter.ViewHolder>(){
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ChallengeDetailAdapter.ViewHolder {
        val view: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_challenge_detail, parent, false)
        val viewHolder = ViewHolder(view)
        return viewHolder
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val description = itemView.findViewById<TextView>(R.id.challenge_item_text)
        val jelly = itemView.findViewById<ImageView>(R.id.challenge_item_jelly)
        val icon = itemView.findViewById<ImageView>(R.id.challenge_item_icon)
        val layout = itemView.findViewById<androidx.constraintlayout.widget.ConstraintLayout>(R.id.challenge_item_layout)
    }

    override fun onBindViewHolder(holder: ChallengeDetailAdapter.ViewHolder, position: Int) {
        val item = items[position]

        holder.layout.setOnClickListener {
            holder.icon.setImageResource(R.drawable.ic_challenge_play)
            holder.layout.setBackgroundColor(Color.parseColor("#806E42"))
            holder.description.setTextColor(Color.parseColor("#FFFFFF"))
            holder.jelly.setImageResource(R.drawable.ic_challenge_full_jelly)
        }
        holder.description.text = item.description
        if(item.lock) {
            holder.icon.setImageResource(R.drawable.ic_challenge_lock)
            holder.description.setTextColor(Color.parseColor("#757575"))
        }

        if(item.background == "lightBlack")
            holder.layout.setBackgroundResource(R.color.lightBlack)
    }

    override fun getItemCount(): Int = items.size

}
class ChallengeDetailData(val description: String, val background: String, val lock: Boolean)
