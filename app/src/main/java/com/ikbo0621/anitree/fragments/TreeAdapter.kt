package com.ikbo0621.anitree.fragments

import android.content.Context
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ikbo0621.anitree.databinding.ItemTreeBinding
import com.ikbo0621.anitree.structure.Tree

class TreeAdapter(
    val context: Context,
    val onItemClicked: (Int, Tree) -> Unit,
    val isProfile: Boolean
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
            if (isProfile) {
                binding.authorName.text = tree.children[0]
                Glide.with(context)
                    .load(tree.urls[0])
                    .into(binding.firstChildImageView)
            } else {
                binding.authorName.text = tree.children[1]
                Glide.with(context)
                    .load(tree.urls[1])
                    .into(binding.firstChildImageView)
            }
            binding.likeNumber.text = tree.likers.size.toString()
            binding.authorName.isSelected = true
            binding.authorName.ellipsize = TextUtils.TruncateAt.MARQUEE

            binding.itemLayout.setOnClickListener { onItemClicked.invoke(adapterPosition, tree) }
        }
    }
}