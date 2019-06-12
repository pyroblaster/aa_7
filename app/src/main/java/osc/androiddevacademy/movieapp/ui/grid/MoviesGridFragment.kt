package osc.androiddevacademy.movieapp.ui.grid

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import kotlinx.android.synthetic.main.fragemnt_movie_grid.*
import osc.androiddevacademy.movieapp.App
import osc.androiddevacademy.movieapp.R
import osc.androiddevacademy.movieapp.common.showFragment
import osc.androiddevacademy.movieapp.database.MoviesDatabase
import osc.androiddevacademy.movieapp.model.Movie
import osc.androiddevacademy.movieapp.model.MoviesResponse
import osc.androiddevacademy.movieapp.networking.BackendFactory
import osc.androiddevacademy.movieapp.networking.interactors.MovieInteractor
import osc.androiddevacademy.movieapp.presentation.GridPresenter
import osc.androiddevacademy.movieapp.ui.pager.MoviesPagerFragment
import osc.androiddevacademy.movieapp.ui.adapters.MoviesGridAdapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MoviesGridFragment : Fragment(), GridContract.View {
    override fun onSuccess(movies: ArrayList<Movie>) {
        movies.filter { (id) ->
            gridPresenter.getFavoriteMovies().any { it.id == id }
        }.forEach { it.isFavorite = true }
        movieList.clear()
        movieList.addAll(movies)
        gridAdapter.setMovies(movieList)
    }

    override fun onFailure() {
    }


    private val SPAN_COUNT = 2

    private val gridAdapter by lazy {
        MoviesGridAdapter(
            { onMovieClicked(it) },
            { onFavoriteClicked(it) })
    }
    private val apiInteractor: MovieInteractor by lazy { BackendFactory.getMovieInteractor() }
    private val appDatabase by lazy { MoviesDatabase.getInstance(App.getAppContext()) }
    private val gridPresenter : GridContract.Presenter by lazy { GridPresenter(BackendFactory.getMovieInteractor(), MoviesDatabase.getInstance(App.getAppContext()))}

    private val movieList = arrayListOf<Movie>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragemnt_movie_grid, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        moviesGrid.apply {
            adapter = gridAdapter
            layoutManager = GridLayoutManager(context, SPAN_COUNT)
        }
        gridPresenter.setView(this)
        favoriteButton.setOnClickListener{onFavoriteButtonClicked()}
        popularButton.setOnClickListener{onPopularButtonClicked()}
        requestPopularMovies()
    }

    override fun onResume() {
        super.onResume()
        requestPopularMovies()
    }

    fun onFavoriteButtonClicked(){
        gridPresenter.getFavoriteMovies()
    }

    fun onPopularButtonClicked(){
        gridPresenter.getPopularMovies()
    }

    private fun requestPopularMovies() {
        apiInteractor.getPopularMovies(popularMoviesCallback())
    }

    private fun popularMoviesCallback(): Callback<MoviesResponse> =
        object : Callback<MoviesResponse> {
            override fun onFailure(call: Call<MoviesResponse>, t: Throwable) {

            }

            override fun onResponse(
                call: Call<MoviesResponse>,
                response: Response<MoviesResponse>
            ) {
                if (response.isSuccessful) {
                    response.body()?.movies?.run {
                        movieList.clear()
                        movieList.addAll(this)
                        gridAdapter.setMovies(movieList)
                    }
                }
            }
        }

    private fun onMovieClicked(movie: Movie) {
        activity?.showFragment(
            R.id.mainFragmentHolder,
            MoviesPagerFragment.getInstance(
                movieList,
                movie
            ),
            true
        )
    }

    private fun onFavoriteClicked(movie: Movie) {
        movie.isFavorite = !movie.isFavorite
        gridAdapter.notifyDataSetChanged()
        gridPresenter.favoriteMovie(movie)

    }

}