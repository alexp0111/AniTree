package com.ikbo0621.anitree.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import com.ikbo0621.anitree.databinding.FragmentAnimeDescriptionBinding


class AnimeDescriptionFragment : Fragment() {

    private var _binding: FragmentAnimeDescriptionBinding? = null
    // This property is only valid between onCreateView and
// onDestroyView.
    private val binding get() = _binding!!

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater ,
        container: ViewGroup? ,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAnimeDescriptionBinding.inflate(inflater , container , false)
        val args: AnimeDescriptionFragmentArgs by navArgs()
        val text = args.animeTitle
        var text2 = args.customVar
        binding.animeTitle.text = text+text2
        return binding.root
    }

    override fun onViewCreated(view: View , savedInstanceState: Bundle?) {
        super.onViewCreated(view , savedInstanceState)
        binding.createTreeBtn.setOnClickListener {
            val action = AnimeDescriptionFragmentDirections.actionAnimeDescriptionFragmentToTreeRedactorFragment()
            Navigation.findNavController(requireView()).navigate(action)
        }
    }




}