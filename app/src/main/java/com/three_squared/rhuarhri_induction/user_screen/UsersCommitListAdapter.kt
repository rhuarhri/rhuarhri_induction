package com.three_squared.rhuarhri_induction.user_screen

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.three_squared.rhuarhri_induction.R
import com.three_squared.rhuarhri_induction.data.Commit

class UsersCommitListAdapter(private val commitList : List<Commit>) : RecyclerView.Adapter<UsersCommitListAdapter.ItemViewHolder>() {

    class ItemViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
        val commitMessageTXT : TextView = itemView.findViewById(R.id.commitItemMessageTXT)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.user_commits_item_layout, parent, false)
        return ItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item = commitList[position]
        holder.commitMessageTXT.text = item.message
    }

    override fun getItemCount(): Int {
        return commitList.size
    }
}