package com.three_squared.rhuarhri_induction.search_screen

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.three_squared.rhuarhri_induction.R

class SearchListAdapter(private val items : List<String>, private val itemClicked : (name : String) -> Unit) : RecyclerView.Adapter<SearchListAdapter.ItemViewHolder>() {

    class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleTXT = itemView.findViewById<TextView>(R.id.itemTitleTXT)

        fun bind(clickListener: () -> Unit) {
            itemView.setOnClickListener {
                clickListener.invoke()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.repositiory_item_layout, parent, false)
        return ItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item = items[position]
        holder.titleTXT.text = item
        val onClick = {
            itemClicked.invoke(item)
        }

        holder.bind(onClick)
    }

    override fun getItemCount(): Int {
        return items.size
    }


}