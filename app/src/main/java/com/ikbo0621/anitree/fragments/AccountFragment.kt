package com.ikbo0621.anitree.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.ikbo0621.anitree.databinding.FragmentAccountBinding
import com.ikbo0621.anitree.structure.TreeConverter
import com.ikbo0621.anitree.testUI.CheckTreeFragment
import com.ikbo0621.anitree.util.fillImageList
import com.ikbo0621.anitree.util.hide
import com.ikbo0621.anitree.viewModel.ParsingViewModel
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
    private var bundle: Bundle = Bundle()
    private var _binding: FragmentAccountBinding? = null
    private val binding get() = _binding!!
    val userViewModel: UserViewModel by viewModels()
    val treeViewModel: TreeViewModel by viewModels()


    val myTreeAdapter by lazy {
        TreeAdapter(requireContext() ,
            onItemClicked = { pos , item ->
                // TODO: to editor
                CoroutineScope(Dispatchers.Main).launch {
                    val converted = withContext(Dispatchers.IO) {
                        TreeConverter.convert(item)
                    }

                    Log.d(TAG , this.toString())

                    binding.pb.hide()
                    val fragment = CheckTreeFragment()

                    bundle.putParcelable("tree" , converted)
                    bundle.putString("id" , item.id)
                    fragment.arguments = bundle

                    // val action = AnimeFragmentDirections.actionAnimeFragmentToTreeViewerFragment()
                    // Navigation.findNavController(requireView()).navigate(action)


                }

            }
        )
    }

    val favTreeAdapter by lazy {
        TreeAdapter(requireContext() ,
            onItemClicked = { pos , item ->
                // TODO: to viewer
                CoroutineScope(Dispatchers.Main).launch {
                    val converted = withContext(Dispatchers.IO) {
                        TreeConverter.convert(item)
                    }

                    Log.d(TAG , this.toString())

                    binding.pb.hide()
                    val fragment = CheckTreeFragment()

                    bundle.putParcelable("tree" , converted)
                    bundle.putString("id" , item.id)
                    fragment.arguments = bundle

                    // val action = AnimeFragmentDirections.actionAnimeFragmentToTreeViewerFragment()
                    // Navigation.findNavController(requireView()).navigate(action)


                }

            }
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater , container: ViewGroup? ,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentAccountBinding.inflate(layoutInflater , container , false)
        return binding.root
    }

    override fun onViewCreated(view: View , savedInstanceState: Bundle?) {
        super.onViewCreated(view , savedInstanceState)
       // observer()

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
        val manager = LinearLayoutManager(context)
        manager.orientation = LinearLayoutManager.HORIZONTAL

      //  binding.myTreesRecyclerView.layoutManager = manager
       // binding.myFavouritesRecyclerView.layoutManager = manager

        binding.myTreesRecyclerView.adapter = myTreeAdapter
        binding.myFavouritesRecyclerView.adapter = favTreeAdapter

        // Call for trees
        userViewModel.getSession {
            if (it != null) {
                // treeViewModel.getTreesFor(it.id)
                // treeViewModel.getFavTreesFor(it.id)
            }
        }


        // set up user image
        userViewModel.getSession {
            if (it != null) {
                binding.firstName.text = it.name.substring(0 , 3).uppercase()
                binding.secondName.text = it.name.substring(0 , 3).uppercase()

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
        TODO("Not yet implemented")
    }


}