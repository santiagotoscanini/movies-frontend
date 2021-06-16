package com.example.biladoniga_toscanini_tejerina.game.views

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import androidx.core.net.toUri
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.example.biladoniga_toscanini_tejerina.R
import com.example.biladoniga_toscanini_tejerina.commons.MAX_LEVELS
import com.example.biladoniga_toscanini_tejerina.game.viewmodel.GameViewModel
import com.example.biladoniga_toscanini_tejerina.menu.MenuActivity
import com.example.biladoniga_toscanini_tejerina.utils.gone
import com.example.biladoniga_toscanini_tejerina.utils.setMarginExtensionFunction
import com.example.biladoniga_toscanini_tejerina.utils.visible
import com.google.android.material.imageview.ShapeableImageView
import org.koin.androidx.viewmodel.ext.android.sharedViewModel


class GamePrincipalScreenFragment : Fragment() {

    private var startTurnButton: Button? = null
    private var firstTeam: ShapeableImageView? = null
    private var secondTeam: ShapeableImageView? = null
    private var thirdTeam: ShapeableImageView? = null

    private val gameViewModel by sharedViewModel<GameViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        activity?.let {
            (it as GameActivity).showLoader()
        }
        return inflater.inflate(R.layout.fragment_game_principal_screen, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        activity?.let {
            (it as GameActivity).hideToolbar()
        }

        initViews(view)
        initClickListeners()
        initListeners()
    }

    private fun initViews(view: View) {
        startTurnButton = view.findViewById(R.id.start_turn_button)
        firstTeam = view.findViewById(R.id.first_team)
        firstTeam?.gone()
        secondTeam = view.findViewById(R.id.second_team)
        secondTeam?.gone()
        thirdTeam = view.findViewById(R.id.third_team)
        thirdTeam?.gone()

        gameViewModel.getTeamsPositions()
        gameViewModel.getTeams()
    }

    private fun initClickListeners() {
        startTurnButton?.setOnClickListener {
            startTurn()
        }
    }

    private fun initListeners() {
        initFirstTeamPositionListener()
        initSecondTeamPositionListener()
        initThirdTeamPositionListener()
        initFirstTeamListener()
        initSecondTeamListener()
        initThirdTeamListener()
        initGameFinishedListener()
    }

    private fun initGameFinishedListener() {
        gameViewModel.gameFinished.observe(
            viewLifecycleOwner,
            Observer { isFinished ->
                if (isFinished) {
                    goToMainMenu()
                }
            }
        )
    }

    private fun goToMainMenu() {
        val intent = Intent(context, MenuActivity::class.java)
        intent.flags =
            Intent.FLAG_ACTIVITY_NEW_TASK or
                    Intent.FLAG_ACTIVITY_CLEAR_TASK or
                    Intent.FLAG_ACTIVITY_CLEAR_TOP
        startActivity(intent)
        activity?.finish()
    }

    private fun initFirstTeamPositionListener() {
        gameViewModel.firstTeamPosition.observe(
            viewLifecycleOwner,
            Observer { position ->
                setPosition(firstTeam, position)
            }
        )
    }

    private fun initSecondTeamPositionListener() {
        gameViewModel.secondTeamPosition.observe(
            viewLifecycleOwner,
            Observer { position ->
                setPosition(secondTeam, position)
            }
        )
    }

    private fun initThirdTeamPositionListener() {
        gameViewModel.thirdTeamPosition.observe(
            viewLifecycleOwner,
            Observer { position ->
                setPosition(thirdTeam, position)
            }
        )
    }

    private fun setPosition(team: ImageView?, position: Int) {
        if (position < MAX_LEVELS) {
            team?.let {
                team.setMarginExtensionFunction(bottom = getMargin(position))
                team.visible()
            }
        }
    }

    private fun getMargin(position: Int): Int {
        return context?.resources?.let {
            val teamPieceSize = it.getDimensionPixelSize(R.dimen.team_piece_size)
            val levelSize = it.getDimensionPixelSize(R.dimen.level_divider_height)
            (position * teamPieceSize) + ((position + 1) * levelSize)
        } ?: 0
    }

    private fun initFirstTeamListener() {
        gameViewModel.firstTeam.observe(
            viewLifecycleOwner,
            Observer { team ->
                team.image?.let {
                    firstTeam?.setImageURI(it.toUri())
                }
            }
        )
    }

    private fun initSecondTeamListener() {
        gameViewModel.secondTeam.observe(
            viewLifecycleOwner,
            Observer { team ->
                team.image?.let {
                    secondTeam?.setImageURI(it.toUri())
                }
            }
        )
    }

    private fun initThirdTeamListener() {
        gameViewModel.thirdTeam.observe(
            viewLifecycleOwner,
            Observer { team ->
                team.image?.let {
                    thirdTeam?.setImageURI(it.toUri())
                }
            }
        )
    }

    private fun startTurn() {
        gameViewModel.getNextTeamTurn()
        gameViewModel.nexTeamTurn.observe(
            viewLifecycleOwner,
            Observer { teamId ->
                navigateToPickMovie(teamId)
            }
        )
    }

    private fun navigateToPickMovie(nextTeamTurn: String) {
        val navOption = NavOptions.Builder().setPopUpTo(
            R.id.gamePrincipalScreenFragment, true
        ).build()
        val bundle =
            bundleOf(PickMovieFragment.nextTeamTurn to nextTeamTurn)
        this.findNavController()
            .navigate(
                R.id.action_gamePrincipalScreenFragment_to_pickMovieFragment,
                bundle,
                navOption
            )
    }
}
