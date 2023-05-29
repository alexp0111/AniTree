package com.ikbo0621.anitree.fragments

import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.ikbo0621.anitree.R
import com.ikbo0621.anitree.databinding.FragmentAccountBinding
import com.ikbo0621.anitree.structure.TreeConverter
import com.ikbo0621.anitree.util.*
import com.ikbo0621.anitree.viewModel.TreeViewModel
import com.ikbo0621.anitree.viewModel.UserViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@AndroidEntryPoint
class AccountFragment : Fragment() {

    private val TAG: String = "ACCOUNT_FRAGMENT"
    private var _binding: FragmentAccountBinding? = null
    private val binding get() = _binding!!
    val userViewModel: UserViewModel by viewModels()
    val treeViewModel: TreeViewModel by viewModels()


    val myTreeAdapter by lazy {
        TreeAdapter(requireContext(),
            onItemClicked = { pos, item ->
                CoroutineScope(Dispatchers.Main).launch {
                    val converted = withContext(Dispatchers.IO) {
                        TreeConverter.convert(item)
                    }

                    userViewModel.getUserById(item.authorID) {
                        if (it != null) {
                            val arrayList = arrayListOf<Int>()
                            fillImageList(arrayList)

                            val icon = BitmapFactory.decodeResource(
                                resources,
                                arrayList[it.iconId.toInt()]
                            )

                            val fragment = TreeViewerFragment()
                            val bundle = Bundle()
                            bundle.putString("author_name", it.name)
                            bundle.putParcelable("author_image", icon)
                            bundle.putString("tree_id", item.id)
                            bundle.putParcelable("tree", converted)
                            fragment.arguments = bundle

                            binding.pb.hide()

                            val action =
                                AccountFragmentDirections.actionAccountFragmentToTreeViewerFragment(
                                    bundle = bundle
                                )
                            Navigation.findNavController(requireView()).navigate(action)
                        }
                    }
                }
            }, true
        )
    }

    val favTreeAdapter by lazy {
        TreeAdapter(requireContext(),
            onItemClicked = { pos, item ->
                CoroutineScope(Dispatchers.Main).launch {
                    val converted = withContext(Dispatchers.IO) {
                        TreeConverter.convert(item)
                    }

                    userViewModel.getUserById(item.authorID) {
                        if (it != null) {
                            val arrayList = arrayListOf<Int>()
                            fillImageList(arrayList)

                            val icon = BitmapFactory.decodeResource(
                                resources,
                                arrayList[it.iconId.toInt()]
                            )

                            val fragment = TreeViewerFragment()
                            val bundle = Bundle()
                            bundle.putString("author_name", it.name)
                            bundle.putParcelable("author_image", icon)
                            bundle.putString("tree_id", item.id)
                            bundle.putParcelable("tree", converted)
                            fragment.arguments = bundle

                            binding.pb.hide()

                            val action =
                                AccountFragmentDirections.actionAccountFragmentToTreeViewerFragment(
                                    bundle = bundle
                                )
                            Navigation.findNavController(requireView()).navigate(action)
                        }
                    }
                }
            }, true
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAccountBinding.inflate(layoutInflater, container, false)
        animationsInit()
        return binding.root
    }

    private fun animationsInit() {
        val avatarAnimator = AnimationUtils.loadAnimation(requireContext(), R.anim.account_fragment_avatar_anim)
        val slideRightAnimator = AnimationUtils.loadAnimation(requireContext(), R.anim.slide_out_right)
        val slideDownAnimator = AnimationUtils.loadAnimation(requireContext(), R.anim.slide_down)
        val slideLeftAnimator = AnimationUtils.loadAnimation(requireContext(), R.anim.slide_out_left)

        binding.apply {
            binding.avatar.animation = avatarAnimator
            binding.cardView.animation = avatarAnimator
            binding.firstName.animation = slideDownAnimator
            binding.secondName.animation = slideRightAnimator
            binding.backButton.animation = slideRightAnimator
            binding.logOutBtn.animation = slideLeftAnimator
            binding.yourTreesTitle.animation = slideLeftAnimator
            binding.myTreesRecyclerView.animation = slideLeftAnimator
            binding.yourFavouritesTitle.animation = slideLeftAnimator
            binding.myFavouritesRecyclerView.animation = slideLeftAnimator
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observer()

        // Button back
        binding.backButton.setOnClickListener {
            Navigation.findNavController(requireView()).popBackStack()
        }

        // Button for profile image
        binding.avatar.setOnClickListener {
            val action = AccountFragmentDirections.actionAccountFragmentToAvatarFragment()
            Navigation.findNavController(requireView()).navigate(action)
        }

        // Adapters
        val favManager = LinearLayoutManager(context)
        favManager.orientation = LinearLayoutManager.HORIZONTAL
        binding.myFavouritesRecyclerView.layoutManager = favManager
        binding.myFavouritesRecyclerView.adapter = favTreeAdapter

        val myManager = LinearLayoutManager(context)
        myManager.orientation = LinearLayoutManager.HORIZONTAL
        binding.myTreesRecyclerView.layoutManager = myManager
        binding.myTreesRecyclerView.adapter = myTreeAdapter


        // Call for trees
        userViewModel.getSession {
            if (it != null) {
                // FIXME:
                treeViewModel.getTreesFor(it.createdTrees)
                treeViewModel.getFavTreesFor(it.favoriteTrees)
            }
        }


        // set up user image
        userViewModel.getSession {
            if (it != null) {
                binding.firstName.text = it.name.substring(0, 3).uppercase()
                binding.secondName.text = it.name.substring(0, 3).uppercase()

                var avatarList = ArrayList<Int>()
                fillImageList(avatarList)

                binding.avatar.setImageResource(avatarList[it.iconId.toInt()])
            }
        }

        binding.logOutBtn.setOnClickListener {
            userViewModel.logout {
                val action = AccountFragmentDirections.actionAccountFragmentToLogInFragment()
                Navigation.findNavController(requireView()).navigate(action)
            }
        }

    }

    private fun observer() {
        treeViewModel.myTrees.observe(viewLifecycleOwner) { state ->
            when (state) {
                is UiState.Loading -> binding.pb.show()
                is UiState.Failure -> {
                    binding.pb.hide()
                    toast(state.error)
                }
                is UiState.Success -> {
                    binding.pb.hide()
                    myTreeAdapter.updateList(state.data)
                }
            }
        }

        treeViewModel.favTrees.observe(viewLifecycleOwner) { state ->
            when (state) {
                is UiState.Loading -> binding.pb.show()
                is UiState.Failure -> {
                    binding.pb.hide()
                    toast(state.error)
                }
                is UiState.Success -> {
                    binding.pb.hide()
                    favTreeAdapter.updateList(state.data)
                }
            }
        }
    }


}