package com.ikbo0621.anitree.fragments

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import androidx.viewpager2.widget.ViewPager2
import com.ikbo0621.anitree.AvatarAdapter
import com.ikbo0621.anitree.databinding.FragmentAvatarBinding
import com.ikbo0621.anitree.util.UiState
import com.ikbo0621.anitree.util.fillImageList
import com.ikbo0621.anitree.util.toast
import com.ikbo0621.anitree.viewModel.UserViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlin.math.abs

@AndroidEntryPoint
class AvatarFragment : Fragment() {
    private var _binding: FragmentAvatarBinding? = null
    private val binding get() = _binding!!
    private lateinit var handler: Handler

    val userViewModel: UserViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentAvatarBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observer()

        userViewModel.getSession {
            if (it != null) {
                init(it.iconId.toInt())
            } else {
                init(0)
            }
            setUpTransformer()
        }

        binding.viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                binding.textView.text = binding.viewPager.currentItem.toString()
            }
        })
        binding.nextBtn.setOnClickListener {
            if (binding.viewPager.currentItem < 23) {
                handler.postDelayed(runnableNext, 50)
            }
        }
        binding.prevBtn.setOnClickListener {
            if (binding.viewPager.currentItem > 0) {
                handler.postDelayed(runnablePrev, 50)
            }
        }

        binding.saveBtn.setOnClickListener {
            userViewModel.getSession { curUser ->
                if (curUser != null) {
                    curUser.iconId = binding.textView.text.toString()
                    userViewModel.updateUserInfo(curUser)
                }
            }
        }

    }

    private fun observer() {
        userViewModel.user.observe(viewLifecycleOwner) { state ->
            when (state) {
                is UiState.Loading -> {
                    //TODO: binding.pb.show()
                }
                is UiState.Failure -> {
                    // TODO: binding.pb.hide()
                    toast(state.error)
                }
                is UiState.Success -> {
                    // TODO: binding.pb.hide()
                    toast(state.data)
                }
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
        transformer.addTransformer { page, position ->
            val r = 1 - abs(position)
            page.scaleY = 0.85f + r * 0.12f
        }
        binding.viewPager.setPageTransformer(transformer)
    }

    private fun init(avatarNum: Int) {
        handler = Handler(Looper.myLooper()!!)
        var avatarList = ArrayList<Int>()
        fillImageList(avatarList)
        var adapter = AvatarAdapter(avatarList, binding.viewPager)

        binding.viewPager.adapter = adapter
        binding.viewPager.offscreenPageLimit = 3
        binding.viewPager.clipToPadding = false
        binding.viewPager.clipChildren = false
        binding.viewPager.getChildAt(0).overScrollMode = RecyclerView.OVER_SCROLL_NEVER
        binding.viewPager.currentItem = avatarNum
        binding.textView.text = binding.viewPager.currentItem.toString()
    }
}