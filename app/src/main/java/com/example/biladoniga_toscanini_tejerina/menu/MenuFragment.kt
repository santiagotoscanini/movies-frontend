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
import com.example.biladoniga_toscanini_tejerina.instructions.HowToPlayActivity
import com.example.biladoniga_toscanini_tejerina.ranking.views.RankingActivity

class MenuFragment: Fragment() {

    private var startGameButton: Button? = null
    private var howToPlayButton: Button? = null
    private var rankingButton: Button? = null

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

        startGameButton?.setOnClickListener {
            navigateToStartGame()
        }
        howToPlayButton?.setOnClickListener {
            navigateToHowToPlay()
        }
        rankingButton?.setOnClickListener {
            navigateToRanking()
        }
    }

    private fun navigateToStartGame(){
        startActivity(Intent(context, GameLaunchActivity::class.java))
    }

    private fun navigateToHowToPlay() {
        startActivity(Intent(context, HowToPlayActivity::class.java))
    }

    private fun navigateToRanking() {
        startActivity(Intent(context, RankingActivity::class.java))
    }
}
