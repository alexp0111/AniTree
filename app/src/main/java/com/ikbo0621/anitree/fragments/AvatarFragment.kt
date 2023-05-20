package com.ikbo0621.anitree.fragments

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import androidx.viewpager2.widget.ViewPager2
import com.ikbo0621.anitree.AvatarAdapter
import com.ikbo0621.anitree.R
import com.ikbo0621.anitree.databinding.FragmentAvatarBinding
import com.ikbo0621.anitree.databinding.FragmentSearchBinding
import kotlin.math.abs

class AvatarFragment : Fragment() {
    private var _binding: FragmentAvatarBinding? = null
    private val binding get() = _binding!!
    private lateinit var handler: Handler
    override fun onCreateView(
        inflater: LayoutInflater , container: ViewGroup? ,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentAvatarBinding.inflate(layoutInflater , container , false)
        return binding.root
    }

    override fun onViewCreated(view: View , savedInstanceState: Bundle?) {
        super.onViewCreated(view , savedInstanceState)
        val avatarNum = 10
        init(avatarNum)
        setUpTransformer()
        binding.viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback(){
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                binding.textView.text = binding.viewPager.currentItem.toString()
            }
        })
        binding.nextBtn.setOnClickListener {
            if(binding.viewPager.currentItem<23){
                handler.postDelayed(runnableNext,50)
            }
        }
        binding.prevBtn.setOnClickListener {
            if(binding.viewPager.currentItem>0){
                handler.postDelayed(runnablePrev,50)
            }
        }

    }


    private val runnableNext = Runnable {
        binding.viewPager.currentItem += 1
    }
    private val runnablePrev = Runnable {
        binding.viewPager.currentItem -= 1
    }

    private fun setUpTransformer() {
        val transformer = CompositePageTransformer()
        transformer.addTransformer(MarginPageTransformer(40))
        transformer.addTransformer{ page, position ->
            val r = 1 - abs(position)
            page.scaleY = 0.85f + r * 0.14f
        }
        binding.viewPager.setPageTransformer(transformer)
    }

    private fun init(avatarNum: Int) {
        handler = Handler(Looper.myLooper()!!)
        var avatarList = ArrayList<Int>()
        fillImageList(avatarList)
        var adapter = AvatarAdapter(avatarList , binding.viewPager)

        binding.viewPager.adapter = adapter
        binding.viewPager.offscreenPageLimit = 3
        binding.viewPager.clipToPadding = false
        binding.viewPager.clipChildren =false
        binding.viewPager.getChildAt(0).overScrollMode = RecyclerView.OVER_SCROLL_NEVER
        binding.viewPager.currentItem = avatarNum
        binding.textView.text = binding.viewPager.currentItem.toString()
    }


    private fun fillImageList(imageList: ArrayList<Int>) {
        imageList.add(R.drawable.agressive_retsuko)
        imageList.add(R.drawable.asta)
        imageList.add(R.drawable.baki)
        imageList.add(R.drawable.bebop)
        imageList.add(R.drawable.berserk)
        imageList.add(R.drawable.dazai)
        imageList.add(R.drawable.denji)
        imageList.add(R.drawable.diavolo)
        imageList.add(R.drawable.eren)
        imageList.add(R.drawable.gojo)
        imageList.add(R.drawable.hisoka)
        imageList.add(R.drawable.itadori)
        imageList.add(R.drawable.izuku)
        imageList.add(R.drawable.jotaro)
        imageList.add(R.drawable.l)
        imageList.add(R.drawable.lite)
        imageList.add(R.drawable.luffy)
        imageList.add(R.drawable.misato)
        imageList.add(R.drawable.naruto)
        imageList.add(R.drawable.rey)
        imageList.add(R.drawable.saiki)
        imageList.add(R.drawable.stone)
        imageList.add(R.drawable.tandjiro)
        imageList.add(R.drawable.vinland)
    }


}