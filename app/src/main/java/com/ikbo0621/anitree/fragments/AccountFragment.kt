package com.ikbo0621.anitree.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.google.android.material.button.MaterialButton

import com.ikbo0621.anitree.R
import com.ikbo0621.anitree.databinding.ActivityMainBinding
import com.ikbo0621.anitree.databinding.FragmentAccountBinding


class AccountFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_account, container, false)
        val backButton = view.findViewById<MaterialButton>(R.id.back_button)
        backButton.setOnClickListener {
            findNavController().navigate(R.id.action_accountFragment_to_searchFragment)
        }
        return view
    }


}