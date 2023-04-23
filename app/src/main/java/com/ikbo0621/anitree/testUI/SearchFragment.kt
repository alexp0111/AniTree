package com.ikbo0621.anitree.testUI

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
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
    private var textGuesses = arrayListOf<TextView>()
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
                && viewModel.guessedAnim.value is UiState.Success
            ) {
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
                )
            }
        }

        textGuesses = arrayListOf(
            binding.tvRec1,
            binding.tvRec2,
            binding.tvRec3,
            binding.tvRec4,
            binding.tvRec5
        )

        textGuesses.forEach { textView ->
            textView.setOnClickListener {
                val animeTitle = textView.text.toString()
                viewModel.getAnimeWithTitle(animeTitle)
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
                is UiState.Loading -> binding.pb.show()
                is UiState.Failure -> {
                    binding.pb.hide()
                    toast(state.error)
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
        viewModel.guessList.observe(viewLifecycleOwner) { arr ->
            textGuesses.forEach {
                it.text = ""
            }
            arr.forEachIndexed { index, s ->
                if (index < textGuesses.size)
                    textGuesses[index].text = s
            }
        }
    }

    /**
     * Validation block for input text
     * */
    private fun validation(): Boolean {
        var isValid = true

        if (binding.etAnimeTitle.text.isNullOrEmpty()) {
            isValid = false
            toast("Enter something")
        }

        return isValid
    }
}