package com.ikbo0621.anitree.fragments

import android.annotation.SuppressLint
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
import com.ikbo0621.anitree.R
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
        inflater: LayoutInflater , container: ViewGroup? ,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentAvatarBinding.inflate(layoutInflater , container , false)
        return binding.root
    }

    override fun onViewCreated(view: View , savedInstanceState: Bundle?) {
        super.onViewCreated(view , savedInstanceState)

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
            @SuppressLint("ResourceAsColor")
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                when (binding.viewPager.currentItem) {
                    0 -> {
                        binding.firstName.text = "Retsuko"
                        binding.secondName.text = "uko"
                    }
                    1 -> {
                        binding.firstName.text = "Asta"
                        binding.secondName.text = "sta"
                    }
                    2 -> {
                        binding.firstName.text = "Hanma"
                        binding.secondName.text = "aki"
                    }
                    3 -> {
                        binding.firstName.text = "Cowboy"
                        binding.secondName.text = "bop"
                    }
                    4 -> {
                        binding.firstName.text = "GU"
                        binding.secondName.text = "UTS"
                    }
                    5 -> {
                        binding.firstName.text = "Da"
                        binding.secondName.text = "zai"
                    }
                    6 -> {
                        binding.firstName.text = "De"
                        binding.secondName.text = "nji"
                    }
                    7 -> {
                        binding.firstName.text = "Diav"
                        binding.secondName.text = "olo"

                    }
                    8 -> {
                        binding.firstName.text = "Eren"
                        binding.secondName.text = "ger"
                    }
                    9 -> {
                        binding.firstName.text = "Go"
                        binding.secondName.text = "Jo"
                    }
                    10 -> {
                        binding.firstName.text = "hisoka"
                        binding.secondName.text = "oka"
                    }
                    11 -> {
                        binding.firstName.text = "itad"
                        binding.secondName.text = "ori"
                    }
                    12 -> {
                        binding.firstName.text = "izu"
                        binding.secondName.text = "uku"
                    }
                    13 -> {
                        binding.firstName.text = "KUJo"
                        binding.secondName.text = "aro"
                    }
                    14 -> {
                        binding.firstName.text = "LLL"
                        binding.secondName.text = "LLL"
                    }
                    15 -> {
                        binding.firstName.text = "KIRA"
                        binding.secondName.text = "ira"
                    }
                    16 -> {
                        binding.firstName.text = "Luffy"
                        binding.secondName.text = "ffy"
                    }
                    17 -> {
                        binding.firstName.text = "MIS"
                        binding.secondName.text = "ATO"
                    }
                    18 -> {
                        binding.firstName.text = "Nar"
                        binding.secondName.text = "UTO"
                    }
                    19 -> {
                        binding.firstName.text = "Ayanami"
                        binding.secondName.text = "REY"
                    }

                    20 -> {
                        binding.firstName.text = "Sai"
                        binding.secondName.text = "ki"
                    }
                    21 -> {
                        binding.firstName.text = "Dr.St"
                        binding.secondName.text = "one"
                    }
                    22 -> {
                        binding.firstName.text = "Tanj"
                        binding.secondName.text = "iro"
                    }
                    23 -> {
                        binding.firstName.text = "Thor"
                        binding.secondName.text = "finn"
                    }
                }
            }
        })


        binding.saveBtn.setOnClickListener {
            userViewModel.getSession { curUser ->
                if (curUser != null) {
                    curUser.iconId = binding.viewPager.currentItem.toString()
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
        transformer.addTransformer { page , position ->
            val r = 1 - abs(position)
            page.scaleY = 0.85f + r * 0.12f
        }
        binding.viewPager.setPageTransformer(transformer)

    }

    @SuppressLint("ResourceAsColor")
    private fun init(avatarNum: Int) {
        handler = Handler(Looper.myLooper()!!)
        var avatarList = ArrayList<Int>()
        fillImageList(avatarList)

        var adapter = AvatarAdapter(avatarList , binding.viewPager)
        binding.viewPager.adapter = adapter
        binding.viewPager.offscreenPageLimit = 3
        binding.viewPager.clipToPadding = false
        binding.viewPager.clipChildren = false
        binding.viewPager.getChildAt(0).overScrollMode = RecyclerView.OVER_SCROLL_NEVER
        binding.viewPager.currentItem = avatarNum
        when (binding.viewPager.currentItem) {
            0 -> {
                binding.firstName.text = "Retsuko"
                binding.secondName.text = "uko"
            }
            1 -> {
                binding.firstName.text = "Asta"
                binding.secondName.text = "sta"
            }
            2 -> {
                binding.firstName.text = "Hanma"
                binding.secondName.text = "aki"
            }
            3 -> {
                binding.firstName.text = "Cowboy"
                binding.secondName.text = "bop"
            }
            4 -> {
                binding.firstName.text = "GU"
                binding.secondName.text = "UTS"
            }
            5 -> {
                binding.firstName.text = "Da"
                binding.secondName.text = "zai"
            }
            6 -> {
                binding.firstName.text = "De"
                binding.secondName.text = "nji"
            }
            7 -> {
                binding.firstName.text = "Diav"
                binding.secondName.text = "olo"

            }
            8 -> {
                binding.firstName.text = "Eren"
                binding.secondName.text = "ger"
            }
            9 -> {
                binding.firstName.text = "Go"
                binding.secondName.text = "Jo"
            }
            10 -> {
                binding.firstName.text = "hisoka"
                binding.secondName.text = "oka"
            }
            11 -> {
                binding.firstName.text = "itad"
                binding.secondName.text = "ori"
            }
            12 -> {
                binding.firstName.text = "izu"
                binding.secondName.text = "uku"
            }
            13 -> {
                binding.firstName.text = "KUJo"
                binding.secondName.text = "aro"
            }
            14 -> {
                binding.firstName.text = "LLL"
                binding.secondName.text = "LLL"
            }
            15 -> {
                binding.firstName.text = "KIRA"
                binding.secondName.text = "ira"
            }
            16 -> {
                binding.firstName.text = "Luffy"
                binding.secondName.text = "ffy"
            }
            17 -> {
                binding.firstName.text = "MIS"
                binding.secondName.text = "ATO"
            }
            18 -> {
                binding.firstName.text = "Nar"
                binding.secondName.text = "UTO"
            }
            19 -> {
                binding.firstName.text = "Ayanami"
                binding.secondName.text = "REY"
            }

            20 -> {
                binding.firstName.text = "Sai"
                binding.secondName.text = "ki"
            }
            21 -> {
                binding.firstName.text = "Dr.St"
                binding.secondName.text = "one"
            }
            22 -> {
                binding.firstName.text = "Tanj"
                binding.secondName.text = "iro"
            }
            23 -> {
                binding.firstName.text = "Thor"
                binding.secondName.text = "finn"
            }

        }
        //binding.textView.text = binding.viewPager.currentItem.toString()
    }
}