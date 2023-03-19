package com.ikbo0621.anitree.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.google.android.material.button.MaterialButton

import com.ikbo0621.anitree.MainActivity
import com.ikbo0621.anitree.R
import com.ikbo0621.anitree.databinding.ActivityMainBinding
import com.ikbo0621.anitree.databinding.FragmentSearchBinding


class SearchFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_search, container, false)
        val navButton = view.findViewById<MaterialButton>(R.id.search_button)
        navButton.setOnClickListener {
            findNavController().navigate(R.id.action_searchFragment_to_accountFragment)
        }
        return view
    }


}


