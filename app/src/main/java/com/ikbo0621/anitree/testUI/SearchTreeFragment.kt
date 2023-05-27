package com.ikbo0621.anitree.testUI

import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationSet
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.ikbo0621.anitree.R
import com.ikbo0621.anitree.RecyclerViewD.RecyclerViewAdapter.SearchListAdapter
import com.ikbo0621.anitree.RecyclerViewD.RecyclerViewItems.SearchListItem
import com.ikbo0621.anitree.databinding.FragmentSearchBinding
import com.ikbo0621.anitree.databinding.FragmentSearchTreeBinding
import com.ikbo0621.anitree.structure.Anime
import com.ikbo0621.anitree.util.UiState
import com.ikbo0621.anitree.util.hide
import com.ikbo0621.anitree.util.show
import com.ikbo0621.anitree.util.toast
import com.ikbo0621.anitree.viewModel.ParsingViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchTreeFragment : Fragment() , SearchListAdapter.Listener {

    private val TAG: String = "SEARCH_FRAGMENT"

    private lateinit var titleGuessesArrayList: ArrayList<SearchListItem>
    private lateinit var titleList: Array<String>

    private var _binding: FragmentSearchTreeBinding? = null
    private val binding get() = _binding!!

    val viewModel: ParsingViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.clearAnimeValue()
    }

    override fun onCreateView(
        inflater: LayoutInflater , container: ViewGroup? ,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSearchTreeBinding.inflate(layoutInflater , container , false)
        animationsInit()
        return binding.root
    }

    private fun animationsInit() {

        val slideLeftAnimator: Animation =
            AnimationUtils.loadAnimation(requireContext() , R.anim.slide_out_left)

        val alphaInAnimator = AnimationUtils.loadAnimation(requireContext() , R.anim.alpha_in)

        val animeFrameAnimator =
            AnimationUtils.loadAnimation(requireContext() , R.anim.search_fragment_anime_frame_anim)

        val slideLeftAnimatorWithOffset =
            AnimationUtils.loadAnimation(requireContext() , R.anim.slide_out_left)
        slideLeftAnimatorWithOffset.startOffset = 200

        val avatarFrameSlideLeftDownAnimator =
            AnimationUtils.loadAnimation(requireContext() , R.anim.search_fragment_round_frame_anim)
        avatarFrameSlideLeftDownAnimator.startOffset = 500

        val avatarAnimatorSet = AnimationSet(true)
        avatarAnimatorSet.addAnimation(slideLeftAnimator)
        avatarAnimatorSet.addAnimation(avatarFrameSlideLeftDownAnimator)

        val animeFrameAnimatorSet = AnimationSet(true)
        animeFrameAnimatorSet.addAnimation(animeFrameAnimator)


        binding.apply {
            binding.searchBar.animation = slideLeftAnimatorWithOffset
            binding.leftLine.animation = slideLeftAnimator
            binding.upperline.animation = slideLeftAnimator
            binding.underline.animation = slideLeftAnimator
            binding.avatarCard.animation = slideLeftAnimator
            binding.roundFrame.animation = avatarAnimatorSet
            binding.resultTopline.animation = alphaInAnimator
            binding.animeFirstWord.animation = slideLeftAnimator
            binding.animeSecondWord.animation = slideLeftAnimatorWithOffset
            binding.animeImage.animation = slideLeftAnimator

        }
    }

    override fun onViewCreated(view: View , savedInstanceState: Bundle?) {
        super.onViewCreated(view , savedInstanceState)
        observer()
        binding.animeSecondWord.ellipsize = TextUtils.TruncateAt.MARQUEE
        binding.animeSecondWord.isSelected = true
        try {
            restore()
        } catch (e: Exception) {
            // There is no anime yet
        }



        binding.animeImage.setOnClickListener {
            if (validation()
                && viewModel.guessedAnim.value != null
                && viewModel.guessedAnim.value is UiState.Success
            ) {
                val anime = if (viewModel.guessedAnim.value is UiState.Failure) {
                    Anime()
                } else {
                    (viewModel.guessedAnim.value as UiState.Success<Anime>).data
                }

                Navigation.findNavController(requireView()).previousBackStackEntry?.savedStateHandle?.set(
                    "anime" ,
                    anime
                )
                Navigation.findNavController(requireView()).popBackStack()
            }
        }

        binding.searchBar.addTextChangedListener {
            Log.d(TAG , "triggered")
            if (binding.searchBar.isFocused) {
                if (validation()) {
                    viewModel.guessAnime(
                        binding.searchBar.text.toString()
                    )
                } else {
                    viewModel.cancelSearch()
                }
            }
        }


        fillRecyclerView(true)


    }

    private fun fillRecyclerView(flag: Boolean) {
        titleList = if (flag) arrayOf()
        else arrayOf("" , "" , "" , "" , "")
        binding.resultList.layoutManager = LinearLayoutManager(requireContext())
        binding.resultList.setHasFixedSize(true)
        titleGuessesArrayList = arrayListOf()
        for (i in titleList) {
            titleGuessesArrayList.add(SearchListItem(i))
        }
        binding.resultList.adapter =
            SearchListAdapter(requireContext() , titleGuessesArrayList , this)
    }


    /**
     * Get anime actual liveData
     * if it exists -> show at ui
     *
     * is necessary to reduce the number of requests to the site
     * */
    private fun restore() {
        context?.let {
            Glide.with(it)
                .load((viewModel.guessedAnim.value as UiState.Success).data.imageURI)
                .into(binding.animeImage)
        }
        val animeTitle = (viewModel.guessedAnim.value as UiState.Success).data.title
        binding.animeFirstWord.text = animeTitle.uppercase()
        binding.animeSecondWord.text = animeTitle.substring(animeTitle.indexOf(" ") + 1)
    }

    /**
     * Set up an observer for live data to get result of
     * parsing from viewModel
     *
     * If result is successful -> data contains Anime object
     * Anime = [title, description, studio, releaseDate, imageUti]
     * */
    private fun observer() {
        viewModel.guessedAnim.observe(viewLifecycleOwner) { state ->
            when (state) {
                is UiState.Loading -> {
                    binding.pb.show()
                    val alphaOutAnimation = AnimationUtils.loadAnimation(requireContext(),R.anim.alpha_out)
                    alphaOutAnimation.setAnimationListener(alphaOutAnimationListener())
                    binding.apply {
                        binding.animeImage.startAnimation(alphaOutAnimation)
                        binding.animeFirstWord.startAnimation(alphaOutAnimation)
                        binding.animeSecondWord.startAnimation(alphaOutAnimation)
                        binding.avatarCard.startAnimation(alphaOutAnimation)
                    }
                }
                is UiState.Failure -> { //TODO: start fix
                    binding.pb.hide()
                    toast(state.error)
                }
                is UiState.Success -> {
                    binding.pb.hide()
                    binding.animeFirstWord.text = state.data.title.uppercase()
                    binding.animeSecondWord.text =
                        state.data.title.substring(state.data.title.indexOf(" ") + 1)


                    context?.let {
                        Glide.with(it)
                            .load(state.data.imageURI)
                            .into(binding.animeImage)
                    }
                    val alphaInAnimation =
                        AnimationUtils.loadAnimation(requireContext() , R.anim.alpha_in)
                    alphaInAnimation.setAnimationListener(alphaInAnimationListener())
                    binding.animeImage.startAnimation(alphaInAnimation)
                    binding.animeFirstWord.startAnimation(alphaInAnimation)
                    binding.animeSecondWord.startAnimation(alphaInAnimation)
                    binding.avatarCard.startAnimation(alphaInAnimation)
                }
            }
        }
        viewModel.guessList.observe(viewLifecycleOwner) { arr ->
            titleGuessesArrayList.forEach {
                it.text = ""
            }
            fillRecyclerView(false)
            arr.forEachIndexed { index , s ->
                if (index < titleGuessesArrayList.size)
                    titleGuessesArrayList[index].text = s

            }
        }

    }

    /**
     * Validation block for input text
     * */
    private fun validation(): Boolean {
        var isValid = true

        if (binding.searchBar.text.isNullOrEmpty()) {
            isValid = false
        }

        return isValid
    }

    override fun OnClick(searchListItem: SearchListItem) {
        val animeTitle = searchListItem.text.toString()
        viewModel.getAnimeWithTitle(animeTitle)
        binding.animeFirstWord.text = animeTitle.uppercase()
        binding.animeSecondWord.text = animeTitle.substring(animeTitle.indexOf(" ") + 1)
    }

    inner class alphaOutAnimationListener : Animation.AnimationListener {
        override fun onAnimationStart(animation: Animation?) {

        }

        override fun onAnimationEnd(animation: Animation?) {
            binding.animeImage.isVisible = false
            binding.animeFirstWord.isVisible = false
            binding.animeSecondWord.isVisible = false
            binding.avatarCard.isVisible =false
        }

        override fun onAnimationRepeat(animation: Animation?) {

        }

    }

    inner class alphaInAnimationListener : Animation.AnimationListener {
        override fun onAnimationStart(animation: Animation?) {
            binding.animeImage.isVisible = true
            binding.animeFirstWord.isVisible = true
            binding.animeSecondWord.isVisible = true
            binding.avatarCard.isVisible = true
        }

        override fun onAnimationEnd(animation: Animation?) {

        }

        override fun onAnimationRepeat(animation: Animation?) {

        }

    }


}