package com.asalah.data.module

import com.asalah.data.api.MovieApi
import com.asalah.data.db.favoritemovies.FavoriteMovieDao
import com.asalah.data.db.movies.MovieDao
import com.asalah.data.db.movies.MovieRemoteKeyDao
import com.asalah.data.repository.movie.IMovieDataSource
import com.asalah.data.repository.movie.MovieLocalDataSource
import com.asalah.data.repository.movie.MovieRemoteDataSource
import com.asalah.data.repository.movie.MovieRemoteMediator
import com.asalah.data.repository.movie.MovieRepositoryImpl
import com.asalah.data.repository.favorite.FavoriteMoviesDataSource
import com.asalah.data.repository.favorite.FavoriteMoviesLocalDataSource
import com.asalah.data.util.DiskExecutor
import com.asalah.domain.repository.IMovieRepository
import com.asalah.domain.usecase.AddMovieToFavoriteUseCase
import com.asalah.domain.usecase.IsMovieFavoriteUseCase
import com.asalah.domain.usecase.GetFavoriteMoviesUseCase
import com.asalah.domain.usecase.GetMovieDetailsUseCase
import com.asalah.domain.usecase.RemoveMovieFromFavoriteUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DataModule {

    @Provides
    @Singleton
    fun provideMovieRepository(
        movieRemote: IMovieDataSource.Remote,
        movieLocal: IMovieDataSource.Local,
        movieRemoteMediator: MovieRemoteMediator,
        favoriteLocal: FavoriteMoviesDataSource.Local,
    ): IMovieRepository {
        return MovieRepositoryImpl(movieRemote, movieLocal, movieRemoteMediator, favoriteLocal)
    }

    @Provides
    @Singleton
    fun provideMovieLocalDataSource(
        executor: DiskExecutor,
        movieDao: MovieDao,
        movieRemoteKeyDao: MovieRemoteKeyDao,
    ): IMovieDataSource.Local {
        return MovieLocalDataSource(executor, movieDao, movieRemoteKeyDao)
    }

    @Provides
    @Singleton
    fun provideMovieMediator(
        movieLocalDataSource: IMovieDataSource.Local,
        movieRemoteDataSource: IMovieDataSource.Remote,
    ): MovieRemoteMediator {
        return MovieRemoteMediator(movieLocalDataSource, movieRemoteDataSource)
    }

    @Provides
    @Singleton
    fun provideFavoriteMovieLocalDataSource(
        executor: DiskExecutor,
        favoriteMovieDao: FavoriteMovieDao
    ): FavoriteMoviesDataSource.Local {
        return FavoriteMoviesLocalDataSource(executor, favoriteMovieDao)
    }

    @Provides
    @Singleton
    fun provideMovieRemoveDataSource(movieApi: MovieApi): IMovieDataSource.Remote {
        return MovieRemoteDataSource(movieApi)
    }

    @Provides
    fun provideGetMovieDetailsUseCase(movieRepository: IMovieRepository): GetMovieDetailsUseCase {
        return GetMovieDetailsUseCase(movieRepository)
    }

    @Provides
    fun provideGetFavoriteMoviesUseCase(movieRepository: IMovieRepository): GetFavoriteMoviesUseCase {
        return GetFavoriteMoviesUseCase(movieRepository)
    }

    @Provides
    fun provideCheckFavoriteStatusUseCase(movieRepository: IMovieRepository): IsMovieFavoriteUseCase {
        return IsMovieFavoriteUseCase(movieRepository)
    }

    @Provides
    fun provideAddMovieToFavoriteUseCase(movieRepository: IMovieRepository): AddMovieToFavoriteUseCase {
        return AddMovieToFavoriteUseCase(movieRepository)
    }

    @Provides
    fun provideRemoveMovieFromFavoriteUseCase(movieRepository: IMovieRepository): RemoveMovieFromFavoriteUseCase {
        return RemoveMovieFromFavoriteUseCase(movieRepository)
    }
}