package com.ikbo0621.anitree.testUI

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.ikbo0621.anitree.databinding.FragmentAnimeBinding
import com.ikbo0621.anitree.structure.Anime
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AnimeFragment : Fragment() {

    private val TAG: String = "ANIME_FRAGMENT"
    private var anime: Anime? = null
    lateinit var binding: FragmentAnimeBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAnimeBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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
    }

}