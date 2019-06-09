package osc.androiddevacademy.movieapp.ui.pager

import osc.androiddevacademy.movieapp.model.Movie

interface PagerContract {
    interface View{

    }
    interface Presenter{
        fun setView(view:View)

        fun returnMovie(movie: Movie)
    }
}