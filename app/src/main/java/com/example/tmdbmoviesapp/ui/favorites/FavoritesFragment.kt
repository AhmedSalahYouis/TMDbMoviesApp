package com.example.tmdbmoviesapp.ui.favorites

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
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tmdbmoviesapp.databinding.FragmentFavoritesBinding
import com.example.tmdbmoviesapp.model.MovieUiModel
import com.example.tmdbmoviesapp.ui.home.IMoviesActionListener
import com.example.tmdbmoviesapp.ui.home.MovieListAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FavoritesFragment : Fragment(), IMoviesActionListener {

    private var _binding: FragmentFavoritesBinding? = null
    private val binding get() = _binding!!
    private val viewModel: FavoritesViewModel by viewModels()

    private lateinit var adapter: MovieListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoritesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = MovieListAdapter(this)
        binding.recyclerViewFavorites.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewFavorites.adapter = adapter

        // Observe PagingData from ViewModel and submit to adapter
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.favorites
                .collectLatest { adapter.submitData(it) }
        }

        adapter.setFavoriteEnabled(false)

        // Handle load state listener
        adapter.addLoadStateListener { loadState ->
            val isListEmpty = loadState.source.refresh is LoadState.NotLoading &&
                    adapter.itemCount == 0
            binding.textViewEmpty.isVisible = isListEmpty

            // Optional: Show errors
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
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onFavoriteClick(movie: MovieUiModel) {
        viewModel.processIntent(FavoritesIntent.ToggleFavorite(movie))
    }

    override fun onMovieClick(movie: MovieUiModel) {
        view?.let {
            val navController = it.findNavController()
            val action = FavoritesFragmentDirections
                .actionFavoriteFragmentToDetailsFragment(movie.id)
            navController.navigate(action)
        }
    }
}
