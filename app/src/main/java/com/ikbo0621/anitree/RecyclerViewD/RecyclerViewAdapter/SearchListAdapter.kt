package com.ikbo0621.anitree.RecyclerViewD.RecyclerViewAdapter

import com.ikbo0621.anitree.R
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ikbo0621.anitree.RecyclerViewD.RecyclerViewItems.SearchListItem
import com.ikbo0621.anitree.databinding.SearchItemViewBinding

class SearchListAdapter(
    private val context: Context ,
    private val items: ArrayList<SearchListItem> ,
    val listener: Listener
) : RecyclerView.Adapter<SearchListAdapter.SearchListViewHolder>() {
    class SearchListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val textView = itemView.findViewById<TextView>(R.id.anime_title_item)
        fun bind(searchListItem: SearchListItem , listener: Listener) {

            textView.text = searchListItem.text
            itemView.setOnClickListener {
                listener.OnClick(searchListItem)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup , viewType: Int): SearchListViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.search_item_view , parent , false)
        return SearchListViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: SearchListViewHolder , position: Int) {
        holder.bind(items[position] , listener)
    }

    interface Listener {
        fun OnClick(searchListItem: SearchListItem)
    }

}