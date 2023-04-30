package com.ikbo0621.anitree.testUI

import android.graphics.Bitmap
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
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
    private var images: ArrayList<Bitmap>? = null // -> Matvey tree
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

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            tree = arguments?.getParcelable("tree", Tree::class.java)
            images = arguments?.getParcelableArrayList("image_list", Bitmap::class.java)
            Log.d(TAG, images?.size.toString())
        } else {
            @Suppress("DEPRECATION")
            tree = arguments?.getParcelable("tree")
            @Suppress("DEPRECATION")
            images = arguments?.getParcelableArrayList("image_list")
            Log.d(TAG, images?.size.toString())
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

        val ivs = arrayListOf(
            binding.ivTree1,
            binding.ivTree2,
            binding.ivTree3,
            binding.ivTree4,
            binding.ivTree5,
            binding.ivTree6,
            binding.ivTree7,
            binding.ivTree8,
            binding.ivTree9,
            binding.ivTree10,
            binding.ivTree11,
            binding.ivTree12,
            binding.ivTree13,
        )
        ivs.forEachIndexed { index, imageView ->
            try {
                imageView.setImageBitmap(images?.get(index))
            } catch (e: Exception) {
                Log.d(TAG, "null")
            }
        }

        binding.btnLike.setOnClickListener {
            userViewModel.getSession {
                if (it != null && tree != null) {
                    treeViewModel.like(
                        tree!!.children[0].toString(),
                        tree!!.id,
                        it.id,
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