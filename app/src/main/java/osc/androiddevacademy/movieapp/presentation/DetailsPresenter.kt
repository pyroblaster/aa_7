package osc.androiddevacademy.movieapp.presentation

import osc.androiddevacademy.movieapp.model.Movie
import osc.androiddevacademy.movieapp.model.ReviewsResponse
import osc.androiddevacademy.movieapp.networking.interactors.MovieInteractor
import osc.androiddevacademy.movieapp.ui.details.DetailsContract
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailsPresenter(private val apiInteractor: MovieInteractor) : DetailsContract.Presenter{
    private lateinit var view: DetailsContract.View

    override fun setView(view: DetailsContract.View) {
        this.view = view
    }

    override fun returnMovie(movie: Movie) {
       apiInteractor.getReviewsForMovie(movie.id, reviewsCallback())
    }

    private fun reviewsCallback(): Callback<ReviewsResponse> = object : Callback <ReviewsResponse> {
        override fun onFailure(call: Call<ReviewsResponse>, t: Throwable) {
            view.onFailure()
        }

        override fun onResponse(call: Call<ReviewsResponse>, response: Response<ReviewsResponse>) {
            if(response.isSuccessful){
                response.body()?.reviews?.run{ view.onSuccess(this) }
            }
        }

    }

}