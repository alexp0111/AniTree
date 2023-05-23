package com.ikbo0621.anitree.fragments

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

private const val TREE_PARAM_KEY = "initialTree"

@AndroidEntryPoint
class TreeEditorFragment : Fragment() {
    private var _binding: FragmentTreeEditorBinding? = null
    private val binding get() = _binding!!
    private var initialTree: TreeData? = null
    private var initialAnime: Anime? = null
    private var listOfAnime: ArrayList<Anime?> = ArrayList()

    private var treeId: String = ""

    val userViewModel: UserViewModel by viewModels()
    val treeViewModel: TreeViewModel by viewModels()

    private val args: TreeEditorFragmentArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (Build.VERSION.SDK_INT == 33) {
            initialTree = args.bundle.getParcelable("tree", TreeData::class.java)
            initialAnime = args.bundle.getParcelable("anime", Anime::class.java)
        } else {
            initialTree = args.bundle.getParcelable("tree")
            initialAnime = args.bundle.getParcelable("anime")
        }
        listOfAnime.add(initialAnime)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTreeEditorBinding.inflate(inflater, container, false)

        observer()

        val treeEditor = TreeEditor(binding.editorTree, requireActivity(), initialTree!!)
        treeEditor.setBottomIcon(
            ContextCompat.getDrawable(requireActivity(), R.drawable.save)!!,
            ContextCompat.getDrawable(requireActivity(), R.drawable.confirmation)!!
        )
        treeEditor.invalidate()

        //
        findNavController()
            .currentBackStackEntry
            ?.savedStateHandle
            ?.getLiveData<Anime>("anime")
            ?.observe(viewLifecycleOwner) {
                CoroutineScope(Dispatchers.Main).launch {
                    val converted = withContext(Dispatchers.IO) {
                        TreeConverter.decodeBitmapFromURL(it.imageURI.toString())
                    }

                    listOfAnime.add(it)

                    if (converted != null) {
                        treeEditor.addSubElement(it.title, it.studio, converted)
                    }
                }
            }
        //

        binding.editorTree.setOnClickListener {
            val selectedElement = binding.editorTree.selectedElement
            if (selectedElement == null) {
                treeEditor.showCrossButtons(false)
                return@setOnClickListener
            }

            when (selectedElement) {
                is Icon -> treeEditor.toNextLayer(selectedElement)
                is SchemeButton -> { // Add new anime to tree
                    val action =
                        TreeEditorFragmentDirections.actionTreeEditorFragmentToSearchTreeFragment()
                    Navigation.findNavController(requireView()).navigate(action)
                    // treeEditor.getTree() returns tree in format of List<String>
                }
                is CrossButton -> { // Delete anime
                    val deletedTitle = treeEditor.deleteElement(selectedElement.index)

                    Log.d("EDITOR", deletedTitle.toString())

                    if (deletedTitle != null) {
                        for (anime in listOfAnime) {
                            if (anime != null && anime.title == deletedTitle) {
                                listOfAnime.remove(anime)
                                break
                            }
                        }
                    }
                }
                is VectorIcon -> { // Save button
                    treeViewModel.updateTree(
                        TreeConverter.convert(
                            treeId,
                            refactorListWithHierarchy(listOfAnime, treeEditor)
                        )
                    )
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

    private fun refactorListWithHierarchy(
        listOfAnime: java.util.ArrayList<Anime?>,
        treeEditor: TreeEditor
    ): List<Anime?> {
        var newList = mutableListOf<Anime?>()
        treeEditor.getTree().forEach {
            if (it == null) {
                newList.add(null)
            } else {
                newList.add(findByName(listOfAnime, it))
            }
        }
        return newList
    }

    private fun findByName(listOfAnime: java.util.ArrayList<Anime?>, title: String): Anime? {
        listOfAnime.forEach { anime ->
            if (anime != null && anime.title == title) return anime
        }
        return null
    }

    private fun observer() {
        treeViewModel.tree.observe(viewLifecycleOwner) { state ->
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
                    putParcelable(TREE_PARAM_KEY, initialTree)
                }
            }
    }
}