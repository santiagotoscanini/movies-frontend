package com.example.biladoniga_toscanini_tejerina.instructions

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.example.biladoniga_toscanini_tejerina.R
import com.example.biladoniga_toscanini_tejerina.game_launch.views.GameLaunchActivity

class HowToPlayActivity: AppCompatActivity() {

    private var playButton: Button? = null
    private var toolbar: Toolbar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_how_to_play)

        setSupportActionBar(findViewById(R.id.instructions_screen_toolbar))
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        initViews()
    }

    override fun onSupportNavigateUp(): Boolean {
        super.onBackPressed()
        return true
    }

    private fun initViews() {
        toolbar = this.findViewById(R.id.instructions_screen_toolbar)
        findViewById<TextView>(R.id.instructions_toolbar_text).text = getString(R.string.how_to_play)

        playButton = findViewById(R.id.play_button)
        playButton?.setOnClickListener{ navigateToStartGame() }
    }

    private fun navigateToStartGame(){
        startActivity(Intent(this, GameLaunchActivity::class.java))
    }
}
