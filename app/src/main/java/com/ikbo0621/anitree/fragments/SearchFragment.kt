package com.ikbo0621.anitree.fragments

import android.icu.text.CaseMap.Title
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ikbo0621.anitree.R
import com.ikbo0621.anitree.RecyclerViewD.RecyclerViewAdapter.SearchListAdapter
import com.ikbo0621.anitree.RecyclerViewD.RecyclerViewItems.SearchListItem
import com.ikbo0621.anitree.databinding.FragmentSearchBinding


class SearchFragment : Fragment() {


    private var _binding: FragmentSearchBinding ? = null

    private lateinit var newArrayList: ArrayList<SearchListItem>
    private lateinit var titleList: Array<String>

   private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSearchBinding.inflate(layoutInflater, container, false)
       return binding.root

    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        titleList = arrayOf("Here","Must","Be","Anime","List")
        binding.resultList.layoutManager = LinearLayoutManager(requireContext())
        binding.resultList.setHasFixedSize(true)
        newArrayList = arrayListOf<SearchListItem>()
        for(i in titleList){
            newArrayList.add(SearchListItem(i))
        }
        binding.resultList.adapter = SearchListAdapter(requireContext(),newArrayList)

        binding.avatar.setOnClickListener {
            val action = SearchFragmentDirections.actionSearchFragmentToAccountFragment()
            Navigation.findNavController(requireView()).navigate(action)
        }
        binding.animeImage.setOnClickListener {
            val action = SearchFragmentDirections.actionSearchFragmentToAnimeDescriptionFragment()
            Navigation.findNavController(requireView()).navigate(action)
        }

    }



}


