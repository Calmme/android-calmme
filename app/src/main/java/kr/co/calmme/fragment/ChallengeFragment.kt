package kr.co.calmme.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_challenge.*
import kr.co.calmme.R
import kr.co.calmme.adapter.OngoingChallengeListAdapter
import kr.co.calmme.model.Challenge

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

        for (i in 1..11) {
            list.add(Challenge(i, "Test$i", "https://", 1, 0))
            list.add(Challenge(i, "Test$i", "https://", 7, 2))

        }
        val adapter = OngoingChallengeListAdapter(list)
        ongoing_challenge_list.adapter = adapter
        val layoutManager = GridLayoutManager(this.context, 2)
        layoutManager.orientation = GridLayoutManager.HORIZONTAL
        ongoing_challenge_list.layoutManager = layoutManager

    }
}