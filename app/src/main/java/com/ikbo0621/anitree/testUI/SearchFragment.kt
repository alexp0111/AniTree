package com.ikbo0621.anitree.testUI

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.ikbo0621.anitree.R
import com.ikbo0621.anitree.databinding.FragmentSearchBinding
import com.ikbo0621.anitree.util.*
import com.ikbo0621.anitree.viewModel.ParsingViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchFragment : Fragment() {

    private val TAG: String = "SEARCH_FRAGMENT"
    lateinit var binding: FragmentSearchBinding
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
        binding.btnFind.setOnClickListener {
            if (validation()) {
                val trimmedTitle = binding.etAnimeTitle.text.toString().trim().replace(" ", "-")
                viewModel.getAnimeWithTitle(trimmedTitle)
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
        viewModel.anim.observe(viewLifecycleOwner) { state ->
            when (state) {
                is UiState.Loading -> {
                    binding.btnFind.text = "Loading"
                    binding.btnFind.disable()
                    binding.pb.show()
                }
                is UiState.Failure -> {
                    binding.btnFind.text = "FIND"
                    binding.btnFind.enabled()
                    binding.pb.hide()
                    toast(state.error)
                }
                is UiState.Success -> {
                    binding.btnFind.text = "FIND"
                    binding.btnFind.enabled()
                    binding.pb.hide()
                    toast(state.data.toString())
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