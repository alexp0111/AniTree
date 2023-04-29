package com.ikbo0621.anitree.testUI

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.ikbo0621.anitree.R
import com.ikbo0621.anitree.databinding.FragmentAnimeBinding
import com.ikbo0621.anitree.databinding.FragmentCheckTreeBinding
import com.ikbo0621.anitree.structure.Anime
import com.ikbo0621.anitree.structure.Tree
import com.ikbo0621.anitree.viewModel.TreeViewModel
import com.ikbo0621.anitree.viewModel.UserViewModel

class CheckTreeFragment : Fragment() {

    private val TAG: String = "CHECK_TREE_FRAGMENT"
    private var tree: Tree? = null // -> Matvey tree
    lateinit var binding: FragmentCheckTreeBinding
    val treeViewModel: TreeViewModel by viewModels()
    val userViewModel: UserViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCheckTreeBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tree = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            arguments?.getParcelable("tree", Tree::class.java)
        } else {
            arguments?.getParcelable("tree")
        }

        binding.tvTree.text = tree.toString()

    }
}