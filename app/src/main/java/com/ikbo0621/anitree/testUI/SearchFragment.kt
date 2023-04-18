package com.ikbo0621.anitree.testUI

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.ikbo0621.anitree.R
import com.ikbo0621.anitree.databinding.FragmentSearchBinding
import com.ikbo0621.anitree.structure.Anime
import com.ikbo0621.anitree.util.UiState
import com.ikbo0621.anitree.util.hide
import com.ikbo0621.anitree.util.show
import com.ikbo0621.anitree.util.toast
import com.ikbo0621.anitree.viewModel.ParsingViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchFragment : Fragment() {

    private val TAG: String = "SEARCH_FRAGMENT"
    lateinit var binding: FragmentSearchBinding
    var trimmedTitle = ""
    val viewModel: ParsingViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSearchBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observer()
        binding.btnProfile.setOnClickListener {
            parentFragmentManager.beginTransaction().addToBackStack("search")
                .replace(R.id.fragment_container_view, ProfileFragment()).commit()
        }
        binding.iv.setOnClickListener {
            if (validation()
                && viewModel.guessedAnim.value != null
                && viewModel.guessedAnim.value is UiState.Success) {

                val fragment = AnimeFragment()
                val bundle = Bundle()
                bundle.putParcelable(
                    "anime",
                    (viewModel.guessedAnim.value as UiState.Success<Anime>).data
                )
                fragment.arguments = bundle

                parentFragmentManager.beginTransaction().addToBackStack(null)
                    .replace(R.id.fragment_container_view, fragment).commit()
            }
        }
        binding.etAnimeTitle.addTextChangedListener {
            if (validation()) {
                viewModel.guessAnime(
                    binding.etAnimeTitle.text.toString()
                        .trim()
                        .replace(" ", "%20")
                        .replace("\'", "")
                        .replace(":", "")
                        .lowercase()
                )
            }
        }
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
                }
                is UiState.Failure -> {
                    binding.pb.hide()

                    if (trimmedTitle.isNotEmpty()) {

                        //TODO: TEST

                        viewModel.getAnimeWithTitle(trimmedTitle.replace("%20", "-"))
                        Log.d(TAG, trimmedTitle)

                        //TODO: TEST
                    }
                }
                is UiState.Success -> {
                    binding.pb.hide()
                    binding.tvInfo.text = state.data.title

                    context?.let {
                        Glide.with(it)
                            .load(state.data.imageURI)
                            .into(binding.iv)
                    }
                }
            }
        }
    }

    /**
     * Validation block for input text
     * */
    fun validation(): Boolean {
        var isValid = true

        if (binding.etAnimeTitle.text.isNullOrEmpty()) {
            isValid = false
            toast("Enter something")
        }

        return isValid
    }
}