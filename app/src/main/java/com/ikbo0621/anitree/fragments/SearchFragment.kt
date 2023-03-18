package com.ikbo0621.anitree.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.ikbo0621.anitree.MAIN
import com.ikbo0621.anitree.MainActivity
import com.ikbo0621.anitree.R
import com.ikbo0621.anitree.databinding.ActivityMainBinding
import com.ikbo0621.anitree.databinding.FragmentSearchBinding


class SearchFragment : Fragment() {

    lateinit var binding: FragmentSearchBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSearchBinding.inflate(layoutInflater, container, false)
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.searchButton.setOnClickListener {
            MAIN.navController.navigate(R.id.action_searchFragment_to_accountFragment)
        }
    }
}


