package com.example.tmdbmoviesapp.ui.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.asalah.domain.usecase.GetMovieDetailsUseCase
import com.asalah.domain.util.onError
import com.asalah.domain.util.onSuccess
import com.example.tmdbmoviesapp.model.MovieDetailsUiModel
import com.example.tmdbmoviesapp.model.toUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class DetailsViewModel @Inject constructor(private val getMovieDetails: GetMovieDetailsUseCase) :
    ViewModel() {

    private val _state = MutableStateFlow<DetailsState>(DetailsState.Loading)
    val state: StateFlow<DetailsState> = _state

    fun processIntent(intent: MovieDetailsIntent) {
        viewModelScope.launch {
            when (intent) {
                is MovieDetailsIntent.FetchMovieDetails -> fetchMovieDetails(intent.movieId)
            }
        }
    }

    suspend fun fetchMovieDetails(id: Int) {
        getMovieDetails(id).onSuccess {
            _state.value = DetailsState.Success(
                MovieDetailsUiModel(
                    id = it.id,
                    title = it.title,
                    overview = it.overview,
                    genres = it.genres.map { genre -> genre.toUiModel() },
                    runtime = it.runtime,
                    imageUrl = it.image
                )
            )
        }.onError {
            _state.value = DetailsState.Error("Error: ${it.localizedMessage}")
        }
    }
}