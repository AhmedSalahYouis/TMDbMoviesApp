package com.example.tmdbmoviesapp.ui.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.asLiveData
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.tmdbmoviesapp.databinding.FragmentDetailsBinding
import com.example.tmdbmoviesapp.model.MovieDetailsUiModel
import com.example.tmdbmoviesapp.ui.home.MovieListAdapter.Companion.IMAGE_BASE_URL
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailsFragment : Fragment() {

    private var _binding: FragmentDetailsBinding? = null
    private val binding get() = _binding!!
    private val viewModel: DetailsViewModel by viewModels()
    private val args: DetailsFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.processIntent(MovieDetailsIntent.FetchMovieDetails(args.movieId))

        viewModel.state.asLiveData().observe(viewLifecycleOwner) { state ->

            when (state) {
                is DetailsState.Loading -> {
                    binding.textViewError.visibility = View.GONE
                    binding.skeletonLoader.visibility = View.VISIBLE
                }

                is DetailsState.Success -> {
                    binding.textViewError.visibility = View.GONE
                    showDataState(state.details)
                }

                is DetailsState.Error -> {
                    binding.textViewError.text = state.message
                    binding.textViewError.visibility = View.VISIBLE
                    binding.skeletonLoader.visibility = View.GONE
                }
            }
        }
    }

    private fun showDataState(movieData: MovieDetailsUiModel) {
        binding.skeletonLoader.visibility = View.GONE
        movieData.let { movie ->
            binding.textViewTitle.text = movie.title
            binding.textViewOverview.text = movie.overview
            binding.textViewGenres.text = movie.genres.joinToString { it.name }
            "${movie.runtime} min".also { binding.textViewRuntime.text = it }

            Glide.with(requireContext())
                .load("${IMAGE_BASE_URL}${movie.imageUrl}")
                .into(binding.imageViewPoster)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}