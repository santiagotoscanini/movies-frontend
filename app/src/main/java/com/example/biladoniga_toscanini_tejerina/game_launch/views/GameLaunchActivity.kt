package com.example.biladoniga_toscanini_tejerina.game_launch.views

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.example.biladoniga_toscanini_tejerina.R
import com.example.biladoniga_toscanini_tejerina.game_launch.viewmodel.LaunchViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class GameLaunchActivity() : AppCompatActivity() {

    private lateinit var loader: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game_launch)

        setSupportActionBar(findViewById(R.id.game_launch_toolbar))
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        loader = this.findViewById(R.id.loader)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    fun showLoader() {
        loader.visibility = View.VISIBLE
    }

    fun hideLoader() {
        loader.visibility = View.GONE
    }
}