package com.ikbo0621.anitree

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2

class AvatarAdapter(private val avatarList: ArrayList<Int>, private val viewPager2: ViewPager2) :
    RecyclerView.Adapter<AvatarAdapter.AvatarViewHolder>() {
    class AvatarViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        val avatarView: ImageView = itemView.findViewById(R.id.avatar_image)
    }

    override fun onCreateViewHolder(parent: ViewGroup , viewType: Int): AvatarViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.avatar_container,parent,false)
        return AvatarViewHolder(view)
    }

    override fun getItemCount(): Int {
       return avatarList.size
    }

    override fun onBindViewHolder(holder: AvatarViewHolder , position: Int) {
        holder.avatarView.setImageResource(avatarList[position])

    }
    @SuppressLint("NotifyDataSetChanged")
    private val runnable = Runnable {
        avatarList.addAll(avatarList)
        notifyDataSetChanged()
    }
}