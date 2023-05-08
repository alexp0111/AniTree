package com.ikbo0621.anitree.testUI

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.ikbo0621.anitree.R
import com.ikbo0621.anitree.databinding.FragmentTreeViewerBinding
import com.ikbo0621.anitree.tree.builders.TreeViewer
import com.ikbo0621.anitree.tree.elements.Icon
import com.ikbo0621.anitree.tree.elements.VectorIcon
import com.ikbo0621.anitree.tree.elements.buttons.Button
import com.ikbo0621.anitree.tree.structures.TreeData

private const val TREE_PARAM_KEY = "initialTree"
class TreeViewerFragment : Fragment() {
    private var _binding: FragmentTreeViewerBinding? = null
    private val binding get() = _binding!!
    private var initialTree: TreeData? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            initialTree = it.getParcelable(TREE_PARAM_KEY)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTreeViewerBinding.inflate(inflater, container, false)

        val treeViewer = TreeViewer(binding.viewerTree, requireActivity(), initialTree!!)
        // treeViewer.setAuthor(bitmap, "nickname")
        treeViewer.setBottomIcon(
            ContextCompat.getDrawable(requireActivity(), R.drawable.like)!!,
            ContextCompat.getDrawable(requireActivity(), R.drawable.like_activated)!!
        )
        treeViewer.invalidate()

        binding.viewerTree.setOnClickListener {
            val selectedElement = binding.viewerTree.selectedElement ?: return@setOnClickListener

            when(selectedElement) {
                is VectorIcon -> { // Like
                    // Sending a request to like or delete it
                }
                is Icon -> treeViewer.toNextLayer(selectedElement) // Anime icon
                is Button -> treeViewer.toPreviousLayer() // Back button
            }
        }

        return binding.root
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