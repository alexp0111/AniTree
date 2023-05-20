package com.ikbo0621.anitree.RecyclerViewD.RecyclerViewAdapter
import com.ikbo0621.anitree.R
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView
import com.ikbo0621.anitree.RecyclerViewD.RecyclerViewHolder.SearchListViewHolder
import com.ikbo0621.anitree.RecyclerViewD.RecyclerViewItems.SearchListItem

class SearchListAdapter(private val context: Context, private val items: ArrayList<SearchListItem>) : RecyclerView.Adapter<SearchListViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchListViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.search_item_view,parent,false)
        return SearchListViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: SearchListViewHolder , position: Int) {
       val currentItem = items[position]
        holder.textView.text = currentItem.title
    }
}