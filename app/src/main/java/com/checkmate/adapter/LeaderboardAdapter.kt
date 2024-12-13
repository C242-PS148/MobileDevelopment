package com.checkmate.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.checkmate.data.model.leaderboard.Leaderboard
import com.checkmate.databinding.ItemLeaderboardsBinding

class LeaderboardAdapter() : RecyclerView.Adapter<LeaderboardAdapter.ViewHolder>() {
    var leaderboardList: List<Leaderboard> = ArrayList()

    fun setList(leaderboardList: List<Leaderboard>){
        this.leaderboardList = leaderboardList
        notifyDataSetChanged()
    }

    inner class ViewHolder(private val binding: ItemLeaderboardsBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bindData(leaderboard: Leaderboard, rank: Int) {
            with(binding){
                tvUsername.text = leaderboard.studentName
                tvPoint.text = leaderboard.semesterTotalPoint.toString()
                tvRank.text = rank.toString()
                Glide.with(binding.root.context).load(leaderboard.imageUrl).into(ivProfile)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val bind = ItemLeaderboardsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(bind)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = leaderboardList[position]
        holder.bindData(item, position+1)
    }

    override fun getItemCount(): Int {
        return leaderboardList.size
    }
}