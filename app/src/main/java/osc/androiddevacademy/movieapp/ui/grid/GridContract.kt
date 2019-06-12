package osc.androiddevacademy.movieapp.ui.grid

import osc.androiddevacademy.movieapp.model.Movie

interface GridContract {
    interface View {

        fun onSuccess(movies:ArrayList<Movie>)

        fun onFailure()
    }

    interface Presenter {
        fun setView(view: View)

        fun favoriteMovie(movie: Movie)

        fun getPopularMovies()

        fun getFavoriteMovies():ArrayList<Movie>
    }
}