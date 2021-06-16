package com.example.biladoniga_toscanini_tejerina.ranking.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.data.models.Movie
import com.example.biladoniga_toscanini_tejerina.R
import com.example.biladoniga_toscanini_tejerina.ranking.adapters.MovieAdapter
import com.example.biladoniga_toscanini_tejerina.ranking.viewmodel.RankingViewModel
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class RankingGuessedFragment : Fragment(), SwipeRefreshLayout.OnRefreshListener {

    private var movieList: RecyclerView? = null
    private var swipeLayout: SwipeRefreshLayout? = null
    private lateinit var adapter: MovieAdapter

    private val rankingViewModel by sharedViewModel<RankingViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_raking_guessed, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews(view)
        initRankingListener()
    }

    private fun initViews(view: View) {
        movieList = view.findViewById(R.id.guessed_list)
        swipeLayout = view.findViewById(R.id.swipe_layout)
        swipeLayout?.setOnRefreshListener(this)

        rankingViewModel.getRankingByGuessed()
    }

    private fun setList(movies: List<Movie>) {
        movieList?.layoutManager = LinearLayoutManager(context)
        movieList?.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        adapter = MovieAdapter(movies)
        movieList?.adapter = adapter

        swipeLayout?.isRefreshing = false
    }

    private fun initRankingListener() {
        rankingViewModel.rankingByGuessed.observe(
            viewLifecycleOwner,
            Observer { movies ->
                setList(movies)
            }
        )
    }

    override fun onRefresh() {
        rankingViewModel.getRankingByGuessed()
    }
}