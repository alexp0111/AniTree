package com.ikbo0621.anitree.testUI

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.ikbo0621.anitree.R
import com.ikbo0621.anitree.databinding.FragmentAnimeBinding
import com.ikbo0621.anitree.structure.Anime
import com.ikbo0621.anitree.structure.Tree
import com.ikbo0621.anitree.util.UiState
import com.ikbo0621.anitree.util.hide
import com.ikbo0621.anitree.util.show
import com.ikbo0621.anitree.util.toast
import com.ikbo0621.anitree.viewModel.ParsingViewModel
import com.ikbo0621.anitree.viewModel.TreeViewModel
import com.ikbo0621.anitree.viewModel.UserViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AnimeFragment : Fragment() {

    private val TAG: String = "ANIME_FRAGMENT"
    private var anime: Anime? = null
    private var bundle: Bundle = Bundle()
    lateinit var binding: FragmentAnimeBinding
    val treeViewModel: TreeViewModel by viewModels()
    val userViewModel: UserViewModel by viewModels()
    val parsingViewModel: ParsingViewModel by viewModels()

    val adapter by lazy {
        TreeAdapter(
            onItemClicked = { pos, item ->
                bundle.putParcelable("tree", item)
                parsingViewModel.getBitmapListForTree(item, requireContext())
            }
        )
    }

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

        val manager = LinearLayoutManager(context)
        manager.orientation = LinearLayoutManager.HORIZONTAL
        binding.rvTrees.layoutManager = manager
        binding.rvTrees.adapter = adapter


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
                    // setting up an recyclerView
                    adapter.updateList(state.data)
                    // binding.tvTrees.text = state.data.toString()
                }
            }
        }

        parsingViewModel.bitmapList.observe(viewLifecycleOwner) { state ->
            when (state) {
                is UiState.Loading -> binding.pb.show()
                is UiState.Failure -> {
                    binding.pb.hide()
                    toast(state.error)
                }
                is UiState.Success -> {
                    binding.pb.hide()
                    val fragment = CheckTreeFragment()

                    bundle.putParcelableArrayList("image_list", state.data)
                    fragment.arguments = bundle

                    parsingViewModel.bitmapList.value = null

                    parentFragmentManager.beginTransaction().addToBackStack(null)
                        .replace(R.id.fragment_container_view, fragment).commit()
                }
                else -> {}
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

        val urls = arrayListOf(
            //root
            "https://cdn.anime-planet.com/anime/primary/naruto-shippuden-1-190x285.jpg?t=1625885757",

            // first layer
            "https://cdn.anime-planet.com/anime/primary/black-clover-1-285x399.jpg?t=1630356468",
            "https://cdn.anime-planet.com/anime/primary/my-hero-academia-1-190x285.jpg?t=1625897284",
            null, // *

            // layer 1.1
            "https://cdn.anime-planet.com/anime/primary/hunter-x-hunter-2011-1-190x285.jpg?t=1625896160",
            "https://cdn.anime-planet.com/anime/primary/dragon-ball-1-190x255.jpg?t=1625885364",
            "https://cdn.anime-planet.com/anime/primary/one-piece-1-190x260.jpg?t=1625885349",

            // layer 1.2
            "https://cdn.anime-planet.com/anime/primary/bleach-1-190x291.jpg?t=1625885618",
            null,
            null,

            // layer 1.2 (* == null -> 1.2 == null, null, null)
            null,
            null,
            null,
        )

        // we do not pass id & likers at the creation
        val tree = Tree(children = children, urls = urls, authorID = id)

        return tree
    }

}