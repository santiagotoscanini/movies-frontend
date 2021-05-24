package com.example.biladoniga_toscanini_tejerina.game_launch.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.biladoniga_toscanini_tejerina.R
import com.example.biladoniga_toscanini_tejerina.commons.TeamStatus
import com.example.biladoniga_toscanini_tejerina.commons.TeamStatus.*
import com.example.biladoniga_toscanini_tejerina.game_launch.viewmodel.LaunchViewModel
import com.example.biladoniga_toscanini_tejerina.utils.TeamButton
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class PrepareTeamsFragment : Fragment() {

    private lateinit var firstTeamButton: TeamButton
    private lateinit var secondTeamButton: TeamButton
    private lateinit var thirdTeamButton: TeamButton

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
    }

    private fun initViews(view: View) {
        firstTeamButton = view.findViewById(R.id.first_team_button)
        secondTeamButton = view.findViewById(R.id.second_team_button)
        thirdTeamButton = view.findViewById(R.id.third_team_button)

        launchViewModel.getTeamsStatus()
    }

    private fun initClickListeners() {
        firstTeamButton.setOnClickListener {
            this.findNavController()
                .navigate(R.id.action_prepareTeamsFragment_to_createTeamFragment)
        }
        secondTeamButton.setOnClickListener {
            this.findNavController()
                .navigate(R.id.action_prepareTeamsFragment_to_createTeamFragment)
        }
        thirdTeamButton.setOnClickListener {
            this.findNavController()
                .navigate(R.id.action_prepareTeamsFragment_to_createTeamFragment)
        }
    }

    private fun initListeners() {
        startFirstTeamListener()
        startSecondTeamListener()
        startThirdTeamListener()
        loaderListener()
    }

    private fun startFirstTeamListener() {
        launchViewModel.firstTeamStatus.observe(
            viewLifecycleOwner,
            Observer { status ->
                setButtonStatus(firstTeamButton, status)
            })
    }

    private fun startSecondTeamListener() {
        launchViewModel.secondTeamStatus.observe(
            viewLifecycleOwner,
            Observer { status ->
                setButtonStatus(secondTeamButton, status)
            })
    }

    private fun startThirdTeamListener() {
        launchViewModel.thirdTeamStatus.observe(
            viewLifecycleOwner,
            Observer { status ->
                setButtonStatus(thirdTeamButton, status)
            })
    }

    private fun loaderListener() {
        launchViewModel.showLoader.observe(
            viewLifecycleOwner,
            Observer { showLoader ->
                if (showLoader) {
                    (activity as GameLaunchActivity).showLoader()
                } else {
                    (activity as GameLaunchActivity).hideLoader()
                }
            })
    }

    private fun setButtonStatus(button: TeamButton, status: TeamStatus) {
        when (status) {
            READY -> button.setButtonReady()
            ALMOST_READY -> button.setButtonAlmostReady()
            NO_READY -> button.setButtonNoReady()
        }
    }
}
