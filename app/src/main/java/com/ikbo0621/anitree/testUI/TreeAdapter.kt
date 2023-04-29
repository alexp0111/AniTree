package com.ikbo0621.anitree.testUI

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ikbo0621.anitree.databinding.ItemTreeBinding
import com.ikbo0621.anitree.structure.Tree

class TreeAdapter(
    val onItemClicked: (Int, Tree) -> Unit
) : RecyclerView.Adapter<TreeAdapter.MyViewHolder>() {

    private var list: List<Tree> = arrayListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = ItemTreeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val item = list[position]
        holder.bind(item)
    }

    fun updateList(list: List<Tree>) {
        this.list = list
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class MyViewHolder(val binding: ItemTreeBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(tree: Tree) {
            binding.tvSomeInfo.text = tree.toString()
            binding.itemLayout.setOnClickListener { onItemClicked.invoke(adapterPosition, tree) }
        }
    }
}