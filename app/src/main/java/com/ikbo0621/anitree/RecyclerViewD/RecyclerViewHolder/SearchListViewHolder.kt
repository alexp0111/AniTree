package com.ikbo0621.anitree.RecyclerViewD.RecyclerViewHolder

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ikbo0621.anitree.R

class SearchListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    val textView = itemView.findViewById<TextView>(R.id.anime_title_item)

}