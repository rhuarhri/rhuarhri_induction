package com.three_squared.rhuarhri_induction.view_commit_screen

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.three_squared.rhuarhri_induction.R
import com.three_squared.rhuarhri_induction.data.Commit

class CommitListAdapter(private val commitList : List<Commit>, private val itemClicked : (commit : Commit) -> Unit) : RecyclerView.Adapter<CommitListAdapter.ItemViewHolder>() {

    class ItemViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
        val messageTXT : TextView = itemView.findViewById(R.id.commitMessageTXT)
        val committerNameTXT : TextView = itemView.findViewById(R.id.committerNameTXT)
        val committerAvatarIV : ImageView = itemView.findViewById(R.id.committerAvatarIV)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.commit_item_layout, parent, false)
        return ItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item = commitList[position]
        holder.messageTXT.text = item.message
        holder.committerNameTXT.text = item.committerName

        if (item.committerAvatar.isNotBlank()) {
            Glide.with(holder.itemView).load(item.committerAvatar).into(holder.committerAvatarIV)
        }

        val onClick = {
            itemClicked.invoke(item)
        }

        holder.itemView.setOnClickListener {
            onClick.invoke()
        }
    }

    override fun getItemCount(): Int {
        return commitList.size
    }
}