package com.pinto.moviemonitor.presentation.movies

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.pinto.moviemonitor.data.repository.MovieRepositoryImpl
import com.pinto.moviemonitor.databinding.FragmentMovieListBinding
import com.pinto.moviemonitor.presentation.movies.adapter.MovieListAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import javax.inject.Inject

@AndroidEntryPoint
class MovieListFragment : Fragment() {
    private var _binding: FragmentMovieListBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var movieRepository: MovieRepositoryImpl

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentMovieListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val viewModel: MovieListViewModel by activityViewModels {
            MovieListViewModelProviderFactory(
                movieRepository
            )
        }

        val adapter = MovieListAdapter {
            val action =
                MovieListFragmentDirections.actionMovieListFragmentToMovieDetailFragment(it)
            view.findNavController().navigate(action)
        }
        binding.rvMovie.adapter = adapter
        binding.rvMovie.addItemDecoration(
            DividerItemDecoration(
                requireContext(), LinearLayoutManager.VERTICAL
            )
        )

        binding.btRetry.setOnClickListener{adapter.retry()}
        binding.swipeRefreshLayout.setOnRefreshListener {
            adapter.refresh()
            binding.swipeRefreshLayout.isRefreshing = false
        }

        viewLifecycleOwner.lifecycleScope.launchWhenCreated{
            viewModel.movieFlow.collect(){
                adapter.submitData(it)
            }
        }
        viewLifecycleOwner.lifecycleScope.launchWhenCreated{
            adapter.loadStateFlow.collect{
                val state = it.refresh
                binding.progresBar.isVisible = state is LoadState.Loading
                binding.tvErrorMessage.isVisible = state is LoadState.Error
                binding.tvErrorMessage.isVisible = state is LoadState.Error
                binding.btRetry.isVisible = state is LoadState.Error
            }
        }

    }


}