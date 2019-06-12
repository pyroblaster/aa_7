package osc.androiddevacademy.movieapp.presentation

import osc.androiddevacademy.movieapp.App
import osc.androiddevacademy.movieapp.database.MoviesDatabase
import osc.androiddevacademy.movieapp.model.Movie
import osc.androiddevacademy.movieapp.model.MoviesResponse
import osc.androiddevacademy.movieapp.networking.interactors.MovieInteractor
import osc.androiddevacademy.movieapp.ui.grid.GridContract
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class GridPresenter(private val apiInteractor: MovieInteractor, private val database: MoviesDatabase) : GridContract.Presenter{

    private lateinit var view: GridContract.View


    override fun setView(view: GridContract.View) {
        this.view = view
    }

    override fun favoriteMovie(movie: Movie) {
       if(movie.isFavorite == true){
           database.moviesDao().addFavoriteMovie(movie)
       }
        else{
           database.moviesDao().deleteFavoriteMovie(movie)
       }
    }

    override fun getPopularMovies() {
        apiInteractor.getPopularMovies(popularMoviesCallback())
    }

    private fun popularMoviesCallback(): Callback<MoviesResponse> = object  : Callback<MoviesResponse> {
        override fun onFailure(call: Call<MoviesResponse>, t: Throwable) {
            view.onFailure()
        }

        override fun onResponse(call: Call<MoviesResponse>, response: Response<MoviesResponse>) {
            if(response.isSuccessful){
                response.body()?.movies?.run { view.onSuccess(this) }
            }
        }
    }

    override fun getFavoriteMovies(): ArrayList<Movie> {
        return database.moviesDao().getFavoriteMovies() as ArrayList<Movie>
    }

}