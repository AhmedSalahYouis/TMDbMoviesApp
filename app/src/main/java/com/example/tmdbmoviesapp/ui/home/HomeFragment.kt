package com.example.tmdbmoviesapp.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tmdbmoviesapp.databinding.FragmentHomeBinding
import com.example.tmdbmoviesapp.model.MovieUiModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : Fragment(), IMoviesActionListener {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val viewModel: HomeViewModel by viewModels()
    private val adapter = MovieListAdapter(this)
    private var isGrid = false

    // LoadStateListener for Paging
    private val loadStateListener: (CombinedLoadStates) -> Unit = { loadState ->

        binding.swipeRefreshLayout.isEnabled = loadState.source.refresh !is LoadState.Loading

        val errorState = loadState.source.append as? LoadState.Error
            ?: loadState.source.prepend as? LoadState.Error
            ?: loadState.source.refresh as? LoadState.Error

        errorState?.let {
            Toast.makeText(
                requireContext(),
                "Error: ${it.error.localizedMessage}",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecycler()
        setupClickListeners()
        observeViewModel()
        setupMoviesAdapter()

    }

    private fun setupClickListeners() {
        // Toggle between grid and list
        binding.toggleViewButton.setOnClickListener {
            isGrid = !isGrid
            binding.recyclerViewMovies.layoutManager = if (isGrid) {
                GridLayoutManager(requireContext(), 2)
            } else {
                LinearLayoutManager(requireContext())
            }
        }

        // Setup Pull to Refresh
        binding.swipeRefreshLayout.setOnRefreshListener {
            adapter.refresh()
        }
    }

    private fun setupRecycler() {
        // Setup RecyclerView
        binding.recyclerViewMovies.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewMovies.adapter = adapter
    }

    private fun setupMoviesAdapter() {
        adapter.addLoadStateListener(loadStateListener)
        adapter.stateRestorationPolicy =
            RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY
    }

    private fun observeViewModel() {
        // Collect HomeState from ViewModel
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.state.collectLatest { state ->
                _binding?.swipeRefreshLayout?.isRefreshing = false
                when (state) {
                    is HomeState.Loading -> {
                        _binding?.progressBar?.isVisible = true
                    }

                    is HomeState.Success -> {
                        _binding?.progressBar?.isVisible = false
                        adapter.submitData(state.movies)
                    }

                    is HomeState.Error -> {
                        _binding?.progressBar?.isVisible = false
                        Toast.makeText(requireContext(), state.message, Toast.LENGTH_SHORT).show()
                    }

                    else -> {}
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        adapter.removeLoadStateListener(loadStateListener)
        _binding = null
    }

    override fun onFavoriteClick(movie: MovieUiModel) {
        viewModel.processIntent(HomeIntent.ToggleFavorite(movie))
    }

    override fun onMovieClick(movie: MovieUiModel) {
        view?.let {
            try {
                val navController = it.findNavController()
                val action = HomeFragmentDirections
                    .actionHomeFragmentToDetailsFragment(movie.id)
                navController.navigate(action)
            } catch (e:Exception){
                e.printStackTrace()
            }
        }
    }
}
