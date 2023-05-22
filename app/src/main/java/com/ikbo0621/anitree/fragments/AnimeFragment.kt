package com.ikbo0621.anitree.fragments

import android.graphics.BitmapFactory
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.ikbo0621.anitree.databinding.FragmentAnimeBinding
import com.ikbo0621.anitree.structure.Anime
import com.ikbo0621.anitree.structure.Tree
import com.ikbo0621.anitree.structure.TreeConverter
import com.ikbo0621.anitree.util.*
import com.ikbo0621.anitree.viewModel.ParsingViewModel
import com.ikbo0621.anitree.viewModel.TreeViewModel
import com.ikbo0621.anitree.viewModel.UserViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@AndroidEntryPoint
class AnimeFragment : Fragment() {

    private val TAG: String = "ANIME_FRAGMENT"
    private var anime: Anime? = null
    private var _binding: FragmentAnimeBinding? = null
    private val binding get() = _binding!!
    val treeViewModel: TreeViewModel by viewModels()
    val userViewModel: UserViewModel by viewModels()
    val parsingViewModel: ParsingViewModel by viewModels()

    val adapter by lazy {
        TreeAdapter(requireContext(),
            onItemClicked = { pos, item ->

                CoroutineScope(Dispatchers.Main).launch {
                    val converted = withContext(Dispatchers.IO) {
                        TreeConverter.convert(item)
                    }

                    val bundle = Bundle()

                    userViewModel.getUserById(item.authorID) {
                        if (it != null) {
                            bundle.putString("author_name", it.name)

                            val arrayList = arrayListOf<Int>()
                            fillImageList(arrayList)

                            val x = arrayList[it.iconId.toInt()]
                            val icon = BitmapFactory.decodeResource(resources, x)
                            bundle.putParcelable("author_image", icon)
                        } else {
                            bundle.putString("author_name", "none")
                        }
                    }

                    bundle.putString("tree_id", item.id)

                    Log.d(TAG, this.toString())

                    binding.pb.hide()
                    val fragment = TreeViewerFragment()

                    bundle.putParcelable("tree", converted)
                    bundle.putString("id", item.id)
                    fragment.arguments = bundle

                    val action =
                        AnimeFragmentDirections.actionAnimeFragmentToTreeViewerFragment(bundle = bundle)
                    Navigation.findNavController(requireView()).navigate(action)
                }

            }
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAnimeBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observer()


        val args: AnimeFragmentArgs by navArgs()
        anime = args.anime
        binding.animeTitle.text = anime?.title
        binding.titleBackground.text = anime?.title?.uppercase()
        binding.animeTitle.isSelected = true
        binding.animeTitle.ellipsize = TextUtils.TruncateAt.MARQUEE
        binding.backButton.setOnClickListener {
            Navigation.findNavController(requireView()).popBackStack()
        }
        context?.let {
            Glide.with(it)
                .load(anime?.imageURI)
                .into(binding.animeImage)
        }
        val animeDescription =
            anime?.title + "\n" + anime?.studio + "\n" + anime?.releaseDate + "\n" + anime?.description
        binding.animeDescription.text = animeDescription


        //

        treeViewModel.getTreesAccordingTo(anime?.title ?: "-1")

        val manager = LinearLayoutManager(context)
        manager.orientation = LinearLayoutManager.HORIZONTAL
        binding.rvTrees.layoutManager = manager
        binding.rvTrees.adapter = adapter


        //

        binding.createTreeBtn.setOnClickListener {
            CoroutineScope(Dispatchers.Main).launch {
                var tree = Tree()
                userViewModel.getSession {
                    if (it != null && anime != null) {
                        tree = Tree(
                            authorID = it.id,
                            children = listOf(anime!!.title),
                            studios = listOf(anime!!.studio),
                            urls = listOf(anime!!.imageURI.toString()),
                        )
                    }
                }

                val converted = withContext(Dispatchers.IO) {
                    TreeConverter.convert(tree)
                }
                val bundle = Bundle()
                val fragment = TreeViewerFragment()

                bundle.putParcelable("tree", converted)
                fragment.arguments = bundle

                val action =
                    AnimeFragmentDirections.actionAnimeFragmentToTreeEditorFragment(bundle = bundle)
                Navigation.findNavController(requireView()).navigate(action)
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

        //parsingViewModel.bitmapList.observe(viewLifecycleOwner) { state ->
        //    when (state) {
        //        is UiState.Loading -> binding.pb.show()
        //        is UiState.Failure -> {
        //            binding.pb.hide()
        //            toast(state.error)
        //        }
        //        is UiState.Success -> {
        //            binding.pb.hide()
        //            val fragment = CheckTreeFragment()
//
        //            bundle.putParcelableArrayList("image_list", state.data)
        //            fragment.arguments = bundle
//
        //            parsingViewModel.bitmapList.value = null
//
        //            //parentFragmentManager.beginTransaction().addToBackStack(null)
        //            //    .replace(R.id.fragment_container_view, fragment).commit()
        //        }
        //        else -> {}
        //    }
        //}
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

        val studios = arrayListOf(
            //root
            "Pierrot",

            // first layer
            "Pierrot",
            "Bones",
            null, // *

            // layer 1.1
            "MADHOUSE",
            "Toei Animation",
            "Toei Animation",

            // layer 1.2
            "Pierrot",
            null,
            null,

            // layer 1.2 (* == null -> 1.2 == null, null, null)
            null,
            null,
            null,
        )

        // we do not pass id & likers at the creation
        val tree = Tree(children = children, urls = urls, studios = studios, authorID = id)

        return tree
    }

}