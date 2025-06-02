import androidx.paging.PagingData
import com.example.tmdbmoviesapp.model.MovieUiModel

sealed class HomeState {
    object Loading : HomeState()
    data class Success(val movies: PagingData<MovieUiModel>) : HomeState()
    data class Error(val message: String) : HomeState()
}
