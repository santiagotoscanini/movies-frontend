package com.example.biladoniga_toscanini_tejerina.game_launch.views

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.example.biladoniga_toscanini_tejerina.R
import com.example.biladoniga_toscanini_tejerina.game_launch.viewmodel.LaunchViewModel
import com.google.android.material.snackbar.Snackbar
import org.koin.androidx.viewmodel.ext.android.viewModel

class GameLaunchActivity : AppCompatActivity() {

    private var loader: View? = null

    private val launchViewModel by viewModel<LaunchViewModel>()

    companion object {
        const val ERROR_MESSAGE_CODE = 0
        const val ERROR_MESSAGE_KEY = "errorMessage"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game_launch)

        setSupportActionBar(findViewById(R.id.game_launch_toolbar))
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        loader = this.findViewById(R.id.loader)
        initLoaderListener()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    fun showLoader() {
        loader?.visibility = View.VISIBLE
    }

    private fun hideLoader() {
        loader?.visibility = View.GONE
    }

    private fun initLoaderListener() {
        launchViewModel.showLoader.observe(
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

    fun setToolbarTitle(title: String) {
        findViewById<TextView>(R.id.toolbar_text).text = title
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            data?.getStringExtra(ERROR_MESSAGE_KEY)?.let {
                showError(it)
            }
        }
    }

    fun showError(message: String) {
        val contextView = findViewById<View>(android.R.id.content)
        Snackbar.make(contextView, message, Snackbar.LENGTH_LONG).show()
    }
}
