package com.ikbo0621.anitree.testUI

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.ikbo0621.anitree.databinding.FragmentCheckTreeBinding
import com.ikbo0621.anitree.structure.Tree
import com.ikbo0621.anitree.util.UiState
import com.ikbo0621.anitree.util.hide
import com.ikbo0621.anitree.util.show
import com.ikbo0621.anitree.util.toast
import com.ikbo0621.anitree.viewModel.TreeViewModel
import com.ikbo0621.anitree.viewModel.UserViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CheckTreeFragment : Fragment() {

    private val TAG: String = "CHECK_TREE_FRAGMENT"
    private var BUTTON_STATE = false

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
        observer()

        tree = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            arguments?.getParcelable("tree", Tree::class.java)
        } else {
            arguments?.getParcelable("tree")
        }

        binding.tvTree.text = tree.toString()

        userViewModel.getSession {
            if (it != null) {
                treeViewModel.checkIfCurrentUserIsLiker(
                    tree!!.children[0].toString(),
                    tree!!.id,
                    it.id
                )
            }
        }

        binding.btnLike.setOnClickListener {
            // TODO: check fpr state to pass
            userViewModel.getSession {
                if (it != null && tree != null) {
                    treeViewModel.like(
                        tree!!.children[0].toString(),
                        tree!!.id,
                        it.id,
                        !BUTTON_STATE
                    )
                }
            }
        }
    }

    private fun observer() {
        treeViewModel.likeState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is UiState.Loading -> binding.pb.show()
                is UiState.Failure -> {
                    binding.pb.hide()
                    toast(state.error)
                }
                is UiState.Success -> {
                    binding.pb.hide()

                    if (state.data) {
                        binding.btnLike.text = "Liked!"
                        binding.btnLike.setBackgroundColor(Color.RED)
                        BUTTON_STATE = true
                    } else {
                        binding.btnLike.text = "Like"
                        binding.btnLike.setBackgroundColor(Color.WHITE)
                        BUTTON_STATE = false
                    }

                }
            }
        }
    }
}