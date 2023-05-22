package com.ikbo0621.anitree.fragments

import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.ikbo0621.anitree.R
import com.ikbo0621.anitree.databinding.FragmentTreeViewerBinding
import com.ikbo0621.anitree.tree.builders.TreeViewer
import com.ikbo0621.anitree.tree.elements.Icon
import com.ikbo0621.anitree.tree.elements.VectorIcon
import com.ikbo0621.anitree.tree.elements.buttons.Button
import com.ikbo0621.anitree.tree.structures.TreeData
import com.ikbo0621.anitree.util.UiState
import com.ikbo0621.anitree.util.toast
import com.ikbo0621.anitree.viewModel.TreeViewModel
import com.ikbo0621.anitree.viewModel.UserViewModel
import dagger.hilt.android.AndroidEntryPoint

private const val TREE_PARAM_KEY = "initialTree"

@AndroidEntryPoint
class TreeViewerFragment : Fragment() {
    private var _binding: FragmentTreeViewerBinding? = null
    private val binding get() = _binding!!
    private var initialTree: TreeData? = null

    private val args: TreeViewerFragmentArgs by navArgs()
    private var bitmap: Bitmap? = null
    private var authorName: String? = null
    private var treeID: String? = null

    val userViewModel: UserViewModel by viewModels()
    val treeViewModel: TreeViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (Build.VERSION.SDK_INT == 33) {
            initialTree = args.bundle.getParcelable("tree", TreeData::class.java)
            bitmap = args.bundle.getParcelable("author_image", Bitmap::class.java)
            authorName = args.bundle.getString("author_name")
            treeID = args.bundle.getString("tree_id")
        } else {
            initialTree = args.bundle.getParcelable("tree")
            bitmap = args.bundle.getParcelable("author_image")
            authorName = args.bundle.getString("author_name")
            treeID = args.bundle.getString("tree_id")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTreeViewerBinding.inflate(inflater, container, false)

        val treeViewer = TreeViewer(binding.viewerTree, requireActivity(), initialTree!!)
        observer(treeViewer)
        treeViewer.setAuthor(bitmap!!, authorName!!)
        treeViewer.setBottomIcon(
            ContextCompat.getDrawable(requireActivity(), R.drawable.like)!!,
            ContextCompat.getDrawable(requireActivity(), R.drawable.like_activated)!!
        )
        treeViewer.invalidate()

        userViewModel.getSession {
            if (it != null) {
                treeViewModel.checkIfCurrentUserIsLiker(initialTree!!.name, treeID!!, it.id)
            } else {
                // error
            }
        }

        binding.viewerTree.setOnClickListener {
            val selectedElement = binding.viewerTree.selectedElement ?: return@setOnClickListener

            when (selectedElement) {
                is VectorIcon -> {
                    userViewModel.getSession {
                        if (it != null) {
                            treeViewModel.like(initialTree!!.name, treeID!!, it.id)
                        } else {
                            // error
                        }
                    }
                }
                is Icon -> treeViewer.toNextLayer(selectedElement) // Anime icon
                is Button -> treeViewer.toPreviousLayer() // Back button
            }
        }

        return binding.root
    }

    private fun observer(treeViewer: TreeViewer) {
        // изначально не активирован
        treeViewModel.likeState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is UiState.Loading -> {}
                is UiState.Failure -> {
                    toast(state.error)
                }
                is UiState.Success -> {
                    if (state.data) {
                        if (!treeViewer.isBottomButtonActivated) treeViewer.switchBottomButton()
                    } else {
                        if (treeViewer.isBottomButtonActivated) treeViewer.switchBottomButton()
                    }
                }
            }
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(initialTree: TreeData) =
            TreeEditorFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(TREE_PARAM_KEY, initialTree)
                }
            }
    }
}