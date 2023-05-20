package com.ikbo0621.anitree.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ikbo0621.anitree.R
import com.ikbo0621.anitree.databinding.FragmentAnimeDescriptionBinding
import com.ikbo0621.anitree.databinding.FragmentTreeRedactorBinding


class TreeRedactorFragment : Fragment() {

    private var _binding: FragmentTreeRedactorBinding? = null
    // This property is only valid between onCreateView and
// onDestroyView.
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentTreeRedactorBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }


}