package com.pinto.moviemonitor.presentation.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.pinto.moviemonitor.data.repository.MovieRepositoryImpl
import com.pinto.moviemonitor.data.source.local.models.MovieResult
import com.pinto.moviemonitor.databinding.FragmentMovieDetailBinding
import com.pinto.moviemonitor.utils.GenreIdConverter
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MovieDetailFragment : Fragment() {

    private var _binding: FragmentMovieDetailBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var movieRepository: MovieRepositoryImpl

    private val args: MovieDetailFragmentArgs by navArgs()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentMovieDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val movie: MovieResult = args.movie
        movie.run {
            binding.tvSynopsis.text = overview
            binding.tvReleaseDate.text = releaseDate
            binding.tvGenre.text = genreIds.let {
                GenreIdConverter.convertIdToGenre(it)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }


}