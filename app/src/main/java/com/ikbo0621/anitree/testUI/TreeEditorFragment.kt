package com.ikbo0621.anitree.testUI

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.ikbo0621.anitree.R
import com.ikbo0621.anitree.databinding.FragmentTreeEditorBinding
import com.ikbo0621.anitree.structure.Anime
import com.ikbo0621.anitree.tree.builders.TreeEditor
import com.ikbo0621.anitree.tree.elements.Icon
import com.ikbo0621.anitree.tree.elements.VectorIcon
import com.ikbo0621.anitree.tree.elements.buttons.Button
import com.ikbo0621.anitree.tree.elements.buttons.CrossButton
import com.ikbo0621.anitree.tree.elements.buttons.SchemeButton
import com.ikbo0621.anitree.tree.structures.TreeData

private const val TREE_PARAM_KEY = "initialTree"
class TreeEditorFragment : Fragment() {
    private var _binding: FragmentTreeEditorBinding? = null
    private val binding get() = _binding!!
    private var initialTree: TreeData? = null
    private val listOfAnime: List<Anime?> = ArrayList()

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
        _binding = FragmentTreeEditorBinding.inflate(inflater, container, false)

        val treeEditor = TreeEditor(binding.editorTree, requireActivity(), initialTree!!)
        treeEditor.setBottomIcon(
            ContextCompat.getDrawable(requireActivity(), R.drawable.save)!!,
            ContextCompat.getDrawable(requireActivity(), R.drawable.confirmation)!!
        )
        treeEditor.invalidate()

        binding.editorTree.setOnClickListener {
            val selectedElement = binding.editorTree.selectedElement
            if (selectedElement == null) {
                treeEditor.showCrossButtons(false)
                return@setOnClickListener
            }

            when(selectedElement) {
                is Icon -> treeEditor.toNextLayer(selectedElement)
                is SchemeButton -> { // Add new anime to tree
                    // treeEditor.addSubElement(name, studio, bitmap)
                    // I get the anime and the picture... And add it to the listOfAnime
                }
                is CrossButton -> { // Delete anime
                    treeEditor.deleteElement(selectedElement.index)
                }
                is VectorIcon -> { // Save button
                    // Save some stuff
                }
                is Button -> {
                    if (selectedElement.index != null) // Author button
                        // Go to author's page
                    else // Back button
                        treeEditor.toPreviousLayer()
                }
                else -> treeEditor.showCrossButtons(false) // Empty space
            }
        }

        binding.editorTree.setOnLongClickListener {
            treeEditor.showCrossButtons(true)

            return@setOnLongClickListener true
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