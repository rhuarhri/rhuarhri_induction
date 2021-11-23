package com.three_squared.rhuarhri_induction.search_screen

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.three_squared.rhuarhri_induction.R
import com.three_squared.rhuarhri_induction.data.Repository

class SearchListAdapter(private val items : List<Repository>, private val itemClicked : (id : String, name : String) -> Unit) : RecyclerView.Adapter<SearchListAdapter.ItemViewHolder>() {

    class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleTXT : TextView = itemView.findViewById(R.id.itemTitleTXT)
        val visibilityTXT : TextView = itemView.findViewById(R.id.repoTypeTXT)

        /*fun bind(clickListener: () -> Unit) {
            itemView.setOnClickListener {
                clickListener.invoke()
            }
        }*/
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.repositiory_item_layout, parent, false)
        return ItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item = items[position]
        holder.titleTXT.text = item.name
        holder.visibilityTXT.text = item.visibility
        val onClick = {
            itemClicked.invoke(item.id, item.name)
        }

        holder.itemView.setOnClickListener {
            onClick.invoke()
        }
        //holder.bind(onClick)
    }

    override fun getItemCount(): Int {
        return items.size
    }


}