package com.example.tmdbmoviesapp.ui.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.tmdbmoviesapp.R
import com.example.tmdbmoviesapp.databinding.ItemMovieBinding
import com.example.tmdbmoviesapp.model.MovieUiModel

interface IMoviesActionListener {
    fun onFavoriteClick(movie: MovieUiModel)
    fun onMovieClick(movie: MovieUiModel)
}

class MovieListAdapter(
    private val listener: IMoviesActionListener
) : PagingDataAdapter<MovieUiModel, MovieListAdapter.MovieViewHolder>(DiffCallback()) {

    private var isFavoriteEnabled: Boolean = true

    companion object {
        const val IMAGE_BASE_URL = "https://image.tmdb.org/t/p/w500"
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val binding = ItemMovieBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MovieViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        getItem(position)?.let { holder.bind(it) }
    }

    fun setFavoriteEnabled(b: Boolean) {
        isFavoriteEnabled = b
    }

    inner class MovieViewHolder(private val binding: ItemMovieBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(movie: MovieUiModel) {
            binding.textViewTitle.text = movie.title
            binding.textViewReleaseDate.text = movie.releaseDate

            Glide.with(binding.imageViewPoster.context)
                .load("${IMAGE_BASE_URL}${movie.imageUrl}")
                .into(binding.imageViewPoster)

            if (isFavoriteEnabled) {
                updateFavoriteIcon(movie)
                binding.favoriteIcon.setOnClickListener {
                    listener.onFavoriteClick(movie)
                    movie.isFavorite = !movie.isFavorite
                    updateFavoriteIcon(movie)
                }
            } else {
                binding.favoriteIcon.visibility = View.GONE
            }

            binding.root.setOnClickListener {
                listener.onMovieClick(movie)
            }
        }

        private fun updateFavoriteIcon(movie: MovieUiModel) {
            val iconRes =
                if (movie.isFavorite) R.drawable.ic_favorite_filled else R.drawable.ic_favorite_border

            binding.favoriteIcon.animate().cancel()
            binding.favoriteIcon.scaleX = 0.8f
            binding.favoriteIcon.scaleY = 0.8f
            binding.favoriteIcon.animate()
                .scaleX(1f)
                .scaleY(1f)
                .setDuration(150)
                .start()

            binding.favoriteIcon.setImageDrawable(
                ContextCompat.getDrawable(
                    binding.favoriteIcon.context,
                    iconRes
                )
            )
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<MovieUiModel>() {
        override fun areItemsTheSame(oldItem: MovieUiModel, newItem: MovieUiModel) =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: MovieUiModel, newItem: MovieUiModel) =
            oldItem == newItem
    }
}
