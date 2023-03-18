package com.ikbo0621.anitree.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ikbo0621.anitree.MAIN
import com.ikbo0621.anitree.R
import com.ikbo0621.anitree.databinding.ActivityMainBinding
import com.ikbo0621.anitree.databinding.FragmentAccountBinding


class AccountFragment : Fragment() {

    lateinit var binding: FragmentAccountBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAccountBinding.inflate(layoutInflater,container,false)
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.backButton.setOnClickListener{
            MAIN.navController.navigate(R.id.action_accountFragment_to_searchFragment)
        }
    }
}