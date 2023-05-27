package com.ikbo0621.anitree.fragments

import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationSet
import android.view.animation.AnimationUtils
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
import com.ikbo0621.anitree.structure.Anime
import com.ikbo0621.anitree.util.*
import com.ikbo0621.anitree.viewModel.ParsingViewModel
import com.ikbo0621.anitree.viewModel.UserViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchFragment : Fragment() , SearchListAdapter.Listener {

    private val TAG: String = "SEARCH_FRAGMENT"

    private lateinit var titleGuessesArrayList: ArrayList<SearchListItem>
    private lateinit var titleList: Array<String>


    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

    val viewModel: ParsingViewModel by viewModels()
    val userViewModel: UserViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater , container: ViewGroup? ,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSearchBinding.inflate(layoutInflater , container , false)

        //Animators init
        val slideLeftAnimator: Animation =
            AnimationUtils.loadAnimation(requireContext() , R.anim.slide_out_left)
        val alphaInAnimator = AnimationUtils.loadAnimation(requireContext() , R.anim.alpha_in)

        val animeFrameAnimator = AnimationUtils.loadAnimation(requireContext(), R.anim.search_fragment_anime_frame_anim)

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
            binding.animeImage.animation = slideLeftAnimatorWithOffset
            binding.animeFrame.animation = animeFrameAnimatorSet

        }


        return binding.root
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
        setUpAvatar()

        binding.avatar.setOnClickListener {
            val action = SearchFragmentDirections.actionSearchFragmentToAccountFragment()
            Navigation.findNavController(requireView()).navigate(action)
        }

        binding.animeImage.setOnClickListener {
            if (validation()
                && viewModel.guessedAnim.value != null
                && viewModel.guessedAnim.value is UiState.Success
            ) {
                val anime = (viewModel.guessedAnim.value as UiState.Success<Anime>).data
                val action =
                    SearchFragmentDirections.actionSearchFragmentToAnimeFragment(anime = anime)
                Navigation.findNavController(requireView()).navigate(action)
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

    private fun setUpAvatar() {
        userViewModel.getSession {
            if (it != null) {
                var avatarList = ArrayList<Int>()
                fillImageList(avatarList)

                binding.avatar.setImageResource(avatarList[it.iconId.toInt()])
            }
        }
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
                is UiState.Loading -> binding.pb.show()
                is UiState.Failure -> {
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


}