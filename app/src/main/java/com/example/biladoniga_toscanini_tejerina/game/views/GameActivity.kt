package com.example.biladoniga_toscanini_tejerina.game.views

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.data.ErrorType
import com.data.Response
import com.example.biladoniga_toscanini_tejerina.R
import com.example.biladoniga_toscanini_tejerina.game.viewmodel.GameViewModel
import com.example.biladoniga_toscanini_tejerina.game_launch.views.GameLaunchActivity
import com.example.biladoniga_toscanini_tejerina.utils.coloredStatusBarMode
import com.example.biladoniga_toscanini_tejerina.utils.gone
import com.example.biladoniga_toscanini_tejerina.utils.visible
import org.koin.androidx.viewmodel.ext.android.viewModel

class GameActivity : AppCompatActivity() {

    private var loader: View? = null
    private var loaderText: TextView? = null
    private var toolbar: Toolbar? = null

    private val gameViewModel by viewModel<GameViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_game)
        setSupportActionBar(findViewById(R.id.game_screen_toolbar))
        supportActionBar?.setDisplayShowTitleEnabled(false)

        initViews()
        initListeners()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        coloredStatusBarMode(getColor(R.color.white))
    }

    private fun initViews(){
        loader = this.findViewById(R.id.loader)
        loaderText = this.findViewById(R.id.loader_text)
        toolbar = this.findViewById(R.id.game_screen_toolbar)
    }

    private fun initListeners(){
        initLoaderListener()
        initErrorListener()
    }

    fun showLoader(loaderString: String? = null) {
        if (loaderString != null) {
            loaderText?.text = loaderString
            loaderText?.visible()
        } else {
            loaderText?.gone()
        }
        loader?.visibility = View.VISIBLE
    }

    private fun hideLoader() {
        loader?.visibility = View.GONE
    }

    private fun initLoaderListener() {
        gameViewModel.showLoader.observe(
            this,
            Observer { showLoader ->
                if (showLoader) {
                    showLoader()
                } else {
                    hideLoader()
                }
            }
        )
    }

    private fun sendErrorAndNavigateToLaunch(errorMessage: String?) {
        val intent = Intent()
        intent.putExtra(GameLaunchActivity.ERROR_MESSAGE_KEY, errorMessage)
        setResult(Activity.RESULT_OK, intent)
        finish()
    }

    fun setToolbarTitle(title: String) {
        findViewById<TextView>(R.id.game_toolbar_text).text = title
    }

    fun showToolbar(){
        toolbar?.visible()
    }

    fun hideToolbar(){
        toolbar?.gone()
    }

     private fun handlerError(errorType: ErrorType){
        var errorMessage: String? = null
        when (errorType) {
            ErrorType.ErrorGameNotFound -> errorMessage =
                getString(R.string.no_game_error_message)
            ErrorType.ErrorTeamNotFound -> errorMessage =
                getString(R.string.no_team_error_message)
            ErrorType.ErrorNoTeammatesFound -> errorMessage =
                getString(R.string.no_teammate_error_message)
            ErrorType.ErrorMovieServerProblem -> errorMessage =
                getString(R.string.error_in_server_message)
            ErrorType.ErrorWithTheAPIRequest -> errorMessage =
                getString(R.string.error_in_request_message)
        }
        sendErrorAndNavigateToLaunch(errorMessage)
    }

    private fun initErrorListener() {
        gameViewModel.showError.observe(
            this,
            Observer { response ->
                if (response is Response.Error) {
                    handlerError(response.error)
                }
            }
        )
    }

}
