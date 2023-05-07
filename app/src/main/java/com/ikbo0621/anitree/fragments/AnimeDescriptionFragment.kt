package com.ikbo0621.anitree.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.ikbo0621.anitree.R
import com.ikbo0621.anitree.databinding.FragmentAnimeDescriptionBinding
import com.ikbo0621.anitree.databinding.FragmentSearchBinding


class AnimeDescriptionFragment : Fragment() {

    private var _binding: FragmentAnimeDescriptionBinding? = null
    // This property is only valid between onCreateView and
// onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAnimeDescriptionBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View , savedInstanceState: Bundle?) {
        super.onViewCreated(view , savedInstanceState)
        binding.createTreeBtn.setOnClickListener {
            val action = AnimeDescriptionFragmentDirections.actionAnimeDescriptionFragmentToTreeRedactorFragment()
            Navigation.findNavController(requireView()).navigate(action)
        }
    }




}