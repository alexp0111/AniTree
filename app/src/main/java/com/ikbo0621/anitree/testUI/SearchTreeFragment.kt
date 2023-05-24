package com.ikbo0621.anitree.testUI

import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
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
class SearchTreeFragment : Fragment(), SearchListAdapter.Listener {

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
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSearchTreeBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
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
                val anime = if (viewModel.guessedAnim.value is UiState.Failure){
                    Anime()
                } else {
                    (viewModel.guessedAnim.value as UiState.Success<Anime>).data
                }

                Navigation.findNavController(requireView()).previousBackStackEntry?.savedStateHandle?.set("anime", anime)
                Navigation.findNavController(requireView()).popBackStack()
            }
        }

        binding.searchBar.addTextChangedListener {
            Log.d(TAG, "triggered")
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
        titleList = if(flag) arrayOf()
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
        binding.animeSecondWord.text = animeTitle.substring(animeTitle.indexOf(" ")+1)
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
                is UiState.Failure -> { //TODO: start fix
                    binding.pb.hide()
                    toast(state.error)
                }
                is UiState.Success -> {
                    binding.pb.hide()
                    binding.animeFirstWord.text = state.data.title.uppercase()
                    binding.animeSecondWord.text = state.data.title.substring(state.data.title.indexOf(" ")+1)


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
            arr.forEachIndexed { index, s ->
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
        binding.animeSecondWord.text = animeTitle.substring(animeTitle.indexOf(" ")+1)
    }


}