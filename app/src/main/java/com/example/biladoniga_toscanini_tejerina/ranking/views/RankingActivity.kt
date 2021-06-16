package com.example.biladoniga_toscanini_tejerina.ranking.views

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.Observer
import com.data.ErrorType
import com.example.biladoniga_toscanini_tejerina.R
import com.example.biladoniga_toscanini_tejerina.game_launch.viewmodel.LaunchViewModel
import com.example.biladoniga_toscanini_tejerina.ranking.viewmodel.RankingViewModel
import com.google.android.material.snackbar.Snackbar
import org.koin.androidx.viewmodel.ext.android.viewModel

class RankingActivity : AppCompatActivity() {

    private var toolbar: Toolbar? = null
    private var loader: View? = null

    private val rankingViewModel by viewModel<RankingViewModel>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ranking)

        setSupportActionBar(findViewById(R.id.ranking_toolbar))
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        loader = this.findViewById(R.id.loader)
        initViews()
        initListeners()
    }

    private fun initViews() {
        toolbar = this.findViewById(R.id.ranking_toolbar)
        findViewById<TextView>(R.id.toolbar_text).text = getString(R.string.ranking)
    }

    private fun initListeners() {
        initErrorListener()
        initLoaderListener()
    }

    private fun initErrorListener() {
        rankingViewModel.showError.observe(this, Observer { error ->
            when(error){
                is ErrorType.ErrorMovieServerProblem ->
                    showError(getString(R.string.error_in_server_message))
                is ErrorType.ErrorBadRequestGettingRanking ->
                    showError(getString(R.string.error_in_ranking_request_message))
            }
        })
    }

    private fun initLoaderListener() {
        rankingViewModel.showLoader.observe(this, Observer { showLoader ->
            if (showLoader) {
                showLoader()
            } else {
                hideLoader()
            }
        })
    }

    private fun showLoader() {
        loader?.visibility = View.VISIBLE
    }

    private fun hideLoader() {
        loader?.visibility = View.GONE
    }

    override fun onSupportNavigateUp(): Boolean {
        super.onBackPressed()
        return true
    }

    private fun showError(message: String) {
        val contextView = findViewById<View>(android.R.id.content)
        Snackbar.make(contextView, message, Snackbar.LENGTH_LONG).show()
    }
}
