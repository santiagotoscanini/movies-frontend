package com.example.biladoniga_toscanini_tejerina.game_launch.views

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.data.models.Team
import com.example.biladoniga_toscanini_tejerina.R
import com.example.biladoniga_toscanini_tejerina.commons.TeamStatus
import com.example.biladoniga_toscanini_tejerina.commons.TeamStatus.*
import com.example.biladoniga_toscanini_tejerina.game.views.GameActivity
import com.example.biladoniga_toscanini_tejerina.game_launch.viewmodel.LaunchViewModel
import com.example.biladoniga_toscanini_tejerina.game_launch.views.GameLaunchActivity.Companion.ERROR_MESSAGE_CODE
import com.example.biladoniga_toscanini_tejerina.utils.TeamButton
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class PrepareTeamsFragment : Fragment() {

    private var firstTeamButton: TeamButton? = null
    private var secondTeamButton: TeamButton? = null
    private var thirdTeamButton: TeamButton? = null
    private var startGameButton: Button? = null

    private val launchViewModel by sharedViewModel<LaunchViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_prepare_teams, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews(view)
        initClickListeners()
        initListeners()
        setToolbarTitle()
    }

    private fun initViews(view: View) {
        firstTeamButton = view.findViewById(R.id.first_team_button)
        secondTeamButton = view.findViewById(R.id.second_team_button)
        thirdTeamButton = view.findViewById(R.id.third_team_button)
        startGameButton = view.findViewById(R.id.start_game_button)

        launchViewModel.getTeamsStatus()
    }

    private fun initClickListeners() {
        firstTeamButton?.setOnClickListener {
            navigateToCreateTeam(Team.firstTeam)
        }
        secondTeamButton?.setOnClickListener {
            navigateToCreateTeam(Team.secondTeam)
        }
        thirdTeamButton?.setOnClickListener {
            navigateToCreateTeam(Team.thirdTeam)
        }
        startGameButton?.setOnClickListener {
            startGame()
        }
    }

    private fun initListeners() {
        startFirstTeamListener()
        startSecondTeamListener()
        startThirdTeamListener()
        readyToStartGameListener()
    }

    private fun startFirstTeamListener() {
        launchViewModel.firstTeamStatus.observe(
            viewLifecycleOwner,
            Observer { status ->
                firstTeamButton?.let {
                    setButtonStatus(it, status)
                }
            })
    }

    private fun startSecondTeamListener() {
        launchViewModel.secondTeamStatus.observe(
            viewLifecycleOwner,
            Observer { status ->
                secondTeamButton?.let {
                    setButtonStatus(it, status)
                }
            }
        )
    }

    private fun startThirdTeamListener() {
        launchViewModel.thirdTeamStatus.observe(
            viewLifecycleOwner,
            Observer { status ->
                thirdTeamButton?.let {
                    setButtonStatus(it, status)
                }
            })
    }

    private fun readyToStartGameListener() {
        launchViewModel.readyToStartGame.observe(
            viewLifecycleOwner,
            Observer { isReady ->
                startGameButton?.isEnabled = isReady
            }
        )
    }

    private fun setButtonStatus(button: TeamButton, status: TeamStatus) {
        when (status) {
            READY -> button.setButtonReady()
            LITLLE_READY -> button.setButtonLittleReady()
            ALMOST_READY -> button.setButtonAlmostReady()
            NO_READY -> button.setButtonNoReady()
        }
    }

    private fun navigateToCreateTeam(teamId: String) {
        val bundle = bundleOf(CreateTeamFragment.teamId to teamId)
        this.findNavController()
            .navigate(R.id.action_prepareTeamsFragment_to_createTeamFragment, bundle)
    }

    private fun setToolbarTitle() {
        activity?.let {
            (it as GameLaunchActivity).apply {
                this.setToolbarTitle(getString(R.string.prepare_teams))
            }
        }
    }

    private fun startGame() {
        launchViewModel.createGame()
        startActivityForResult(
            Intent(activity, GameActivity::class.java),
            ERROR_MESSAGE_CODE
        )
    }
}
