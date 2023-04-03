package com.ikbo0621.anitree.testUI

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.ikbo0621.anitree.R
import com.ikbo0621.anitree.databinding.FragmentProfileBinding
import com.ikbo0621.anitree.viewModel.UserViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileFragment : Fragment() {

    private val TAG: String = "PROFILE_FRAGMENT"
    lateinit var binding: FragmentProfileBinding
    val viewModel: UserViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProfileBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Temporary solution
        // TODO: Replace with get request to firestore with observer logic
        viewModel.getSession {
            if (it != null) {
                binding.tvProfileName.text = it.name + "\n" + it.id
            }
        }

        binding.btnLogOut.setOnClickListener {
            viewModel.logout {
                parentFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container_view, LogInFragment()).commit()
            }
        }
    }

}