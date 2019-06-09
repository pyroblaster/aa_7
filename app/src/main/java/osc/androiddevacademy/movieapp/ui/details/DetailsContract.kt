package osc.androiddevacademy.movieapp.ui.details

import osc.androiddevacademy.movieapp.model.Movie
import osc.androiddevacademy.movieapp.model.Review

interface DetailsContract{
    interface View{
        fun onSuccess(reviews:List<Review>)

        fun onFailure()


    }
    interface Presenter{
        fun setView(view:View)

        fun returnMovie(movie:Movie)
    }
}