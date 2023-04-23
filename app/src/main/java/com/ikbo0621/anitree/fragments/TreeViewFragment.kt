package com.ikbo0621.anitree.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ikbo0621.anitree.R
import com.ikbo0621.anitree.databinding.FragmentSearchBinding
import com.ikbo0621.anitree.databinding.FragmentSettingsBinding
import com.ikbo0621.anitree.databinding.FragmentTreeViewBinding


class TreeViewFragment : Fragment() {

    private var _binding: FragmentTreeViewBinding? = null

    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentTreeViewBinding.inflate(layoutInflater, container, false)
        return binding.root
    }


}