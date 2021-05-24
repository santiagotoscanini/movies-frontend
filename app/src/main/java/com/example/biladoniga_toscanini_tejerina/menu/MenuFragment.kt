package com.example.biladoniga_toscanini_tejerina.menu

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import com.example.biladoniga_toscanini_tejerina.R
import com.example.biladoniga_toscanini_tejerina.game_launch.views.GameLaunchActivity

class MenuFragment: Fragment() {

    private lateinit var startGameButton: Button
    private lateinit var howToPlayButton: Button
    private lateinit var rankingButton: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_menu, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews(view)
    }

    private fun initViews(view: View){
        startGameButton = view.findViewById(R.id.start_game_button)
        howToPlayButton = view.findViewById(R.id.how_to_play_button)
        rankingButton = view.findViewById(R.id.ranking_button)

        startGameButton.setOnClickListener {
            navigateToStartGame()
        }
    }

    private fun navigateToStartGame(){
        startActivity(Intent(context, GameLaunchActivity::class.java))
    }
}