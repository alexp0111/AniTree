package com.ikbo0621.anitree.testUI

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.ikbo0621.anitree.databinding.FragmentAnimeBinding
import com.ikbo0621.anitree.structure.Anime
import com.ikbo0621.anitree.structure.Tree
import com.ikbo0621.anitree.util.UiState
import com.ikbo0621.anitree.util.hide
import com.ikbo0621.anitree.util.show
import com.ikbo0621.anitree.util.toast
import com.ikbo0621.anitree.viewModel.TreeViewModel
import com.ikbo0621.anitree.viewModel.UserViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AnimeFragment : Fragment() {

    private val TAG: String = "ANIME_FRAGMENT"
    private var anime: Anime? = null
    lateinit var binding: FragmentAnimeBinding
    val treeViewModel: TreeViewModel by viewModels()
    val userViewModel: UserViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAnimeBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observer()


        anime = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            arguments?.getParcelable("anime", Anime::class.java)
        } else {
            arguments?.getParcelable("anime")
        }

        context?.let {
            Glide.with(it)
                .load(anime?.imageURI)
                .into(binding.ivAnime)
        }

        binding.tvAnimeTitle.text = anime?.title
        binding.tvAnimeStudio.text = anime?.studio
        binding.tvAnimeReleaseDate.text = anime?.releaseDate
        binding.tvAnimeDescription.text = anime?.description

        //

        treeViewModel.getTreesAccordingTo(anime?.title ?: "-1")


        //

        binding.btnUploadTree.setOnClickListener {
            userViewModel.getSession {
                if (it != null) {
                    val tree = getTestTree(it.id)
                    treeViewModel.updateTree(tree)
                }
            }
        }
    }

    private fun observer() {
        treeViewModel.tree.observe(viewLifecycleOwner) { state ->
            when (state) {
                is UiState.Loading -> binding.pb.show()
                is UiState.Failure -> {
                    binding.pb.hide()
                    toast(state.error)
                }
                is UiState.Success -> {
                    binding.pb.hide()
                    // temporary test solution
                    toast(state.data.id)
                }
            }
        }

        treeViewModel.trees.observe(viewLifecycleOwner) { state ->
            when (state) {
                is UiState.Loading -> binding.pb.show()
                is UiState.Failure -> {
                    binding.pb.hide()
                    toast(state.error)
                }
                is UiState.Success -> {
                    binding.pb.hide()
                    toast("Success")
                    // temporary test solution
                    binding.tvTrees.text = state.data.toString()
                }
            }
        }
    }

    private fun getTestTree(id: String): Tree {
        val children =
            arrayListOf(
                //root
                "Naruto Shippuden",

                // first layer
                "Black Clover",
                "My Hero Academia",
                null, // *

                // layer 1.1
                "Hunter x Hunter (2011)",
                "Dragon Ball",
                "One Piece",

                // layer 1.2
                "Bleach",
                null,
                null,

                // layer 1.2 (* == null -> 1.2 == null, null, null)
                null,
                null,
                null,
            )

        // we do not pass id & likers at the creation
        val tree = Tree(children = children, authorID = id)

        return tree
    }

}