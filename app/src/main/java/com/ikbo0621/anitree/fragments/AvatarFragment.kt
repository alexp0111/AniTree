package com.ikbo0621.anitree.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ikbo0621.anitree.R
import com.ikbo0621.anitree.databinding.FragmentAvatarBinding
import com.ikbo0621.anitree.databinding.FragmentSearchBinding

class AvatarFragment : Fragment() {
    private var _binding: FragmentAvatarBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentAvatarBinding.inflate(layoutInflater,container,false)
        return binding.root
    }



}