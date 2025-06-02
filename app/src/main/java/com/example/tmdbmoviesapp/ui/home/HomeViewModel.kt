package com.example.tmdbmoviesapp.ui.home

import HomeState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.asalah.domain.usecase.AddMovieToFavoriteUseCase
import com.asalah.domain.usecase.GetMoviesUseCase
import com.asalah.domain.usecase.IsMovieFavoriteUseCase
import com.asalah.domain.usecase.RemoveMovieFromFavoriteUseCase
import com.asalah.domain.util.getResult
import com.asalah.domain.util.onSuccess
import com.example.tmdbmoviesapp.model.MovieUiModel
import com.example.tmdbmoviesapp.model.toUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getMoviesUseCase: GetMoviesUseCase,
    private val isMovieFavorite: IsMovieFavoriteUseCase,
    private val addMovieToFavorite: AddMovieToFavoriteUseCase,
    private val removeMovieFromFavorite: RemoveMovieFromFavoriteUseCase,
) : ViewModel() {

    private val _state = MutableStateFlow<HomeState>(HomeState.Loading)
    val state: StateFlow<HomeState> = _state

    private var currentPagingFlow: Flow<PagingData<MovieUiModel>>? = null

    init {
        // Initial fetch
        processIntent(HomeIntent.FetchMovies)
    }

    fun processIntent(intent: HomeIntent) {
        viewModelScope.launch {
            when (intent) {
                is HomeIntent.FetchMovies -> fetchMovies()
                is HomeIntent.RefreshMovies -> refreshMovies()
                is HomeIntent.ToggleFavorite -> handleFavoriteMovie(intent.movie)
            }
        }
    }

    private fun fetchMovies() {
        viewModelScope.launch {
            currentPagingFlow = getMoviesUseCase.movies(20)
                .map { pagingData ->
                    pagingData.map {
                        val isFavorite = async {
                            isMovieFavorite.invoke(it.id)
                                .getResult({ result -> result.data }, { false })
                        }
                        it.toUiModel(isFavorite.await())
                    }
                }
                .cachedIn(viewModelScope)
                .onStart { _state.value = HomeState.Loading }
                .catch { e ->
                    _state.value = HomeState.Error("Failed to load movies: ${e.localizedMessage}")
                }
            currentPagingFlow?.collect { pagingData ->
                _state.value = HomeState.Success(pagingData)
            }
        }
    }

    private suspend fun refreshMovies() {
        // Simply re-collect from the currentPagingFlow to refresh data
        currentPagingFlow?.let { flow ->
            _state.value = HomeState.Loading
            flow.collect { pagingData ->
                _state.value = HomeState.Success(pagingData)
            }
        } ?: fetchMovies()
    }

    private suspend fun handleFavoriteMovie(movie: MovieUiModel) {
            isMovieFavorite.invoke(movie.id).onSuccess {isFav ->
                if (isFav) {
                    removeMovieFromFavorite(movie.id)
                } else {
                    addMovieToFavorite(movie.id)
                }
            }
    }
}
