package com.ikbo0621.anitree.fragments

import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import com.ikbo0621.anitree.R
import com.ikbo0621.anitree.databinding.FragmentTreeEditorBinding
import com.ikbo0621.anitree.structure.Anime
import com.ikbo0621.anitree.structure.TreeConverter
import com.ikbo0621.anitree.tree.builders.TreeEditor
import com.ikbo0621.anitree.tree.elements.Icon
import com.ikbo0621.anitree.tree.elements.VectorIcon
import com.ikbo0621.anitree.tree.elements.buttons.Button
import com.ikbo0621.anitree.tree.elements.buttons.CrossButton
import com.ikbo0621.anitree.tree.elements.buttons.SchemeButton
import com.ikbo0621.anitree.tree.structures.TreeData
import com.ikbo0621.anitree.util.UiState
import com.ikbo0621.anitree.util.toast
import com.ikbo0621.anitree.viewModel.TreeViewModel
import com.ikbo0621.anitree.viewModel.UserViewModel
import dagger.hilt.android.AndroidEntryPoint

private const val TREE_PARAM_KEY = "initialTree"

@AndroidEntryPoint
class TreeEditorFragment : Fragment() {
    private var _binding: FragmentTreeEditorBinding? = null
    private val binding get() = _binding!!
    private var initialTree: TreeData? = null
    private val listOfAnime: List<Anime?> = ArrayList()

    private var treeId: String = ""

    val userViewModel: UserViewModel by viewModels()
    val treeViewModel: TreeViewModel by viewModels()

    private val args: TreeEditorFragmentArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (Build.VERSION.SDK_INT == 33) {
            initialTree = args.bundle.getParcelable("tree", TreeData::class.java)
        } else {
            initialTree = args.bundle.getParcelable("tree")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTreeEditorBinding.inflate(inflater, container, false)

        observer()

        //
        Navigation.findNavController(requireView()).currentBackStackEntry?.savedStateHandle?.getLiveData<Anime>("anime")
            ?.observe(viewLifecycleOwner) {
                // TODO: set new anime
            }
        //

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
                    // TODO: make addSubElement return the index in [ root, 1, 2, 3, 1.1, 1.2, 1.3, 2.1, 2.2, 2.3, 3.1, 3.2, 3.3 ] way
                    // I get the anime and the picture... And add it to the listOfAnime
                }
                is CrossButton -> { // Delete anime
                    treeEditor.deleteElement(selectedElement.index)
                    // TODO: make deleteElement return name of deleted anime
                }
                is VectorIcon -> { // Save button
                    treeViewModel.updateTree(TreeConverter.convert(treeId, listOfAnime))
                }
                is Button -> treeEditor.toPreviousLayer()
                else -> treeEditor.showCrossButtons(false) // Empty space
            }
        }

        binding.editorTree.setOnLongClickListener {
            treeEditor.showCrossButtons(true)

            return@setOnLongClickListener true
        }


        return binding.root
    }

    private fun observer() {
        treeViewModel.tree.observe(viewLifecycleOwner){state ->
            when (state) {
                is UiState.Loading -> {}
                is UiState.Failure -> {
                    toast(state.error)
                }
                is UiState.Success -> {
                    treeId = state.data.id
                    toast("Tree is uploaded")
                }
            }
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(initialTree: TreeData) =
            TreeEditorFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(TREE_PARAM_KEY , initialTree)
                }
            }
    }
}