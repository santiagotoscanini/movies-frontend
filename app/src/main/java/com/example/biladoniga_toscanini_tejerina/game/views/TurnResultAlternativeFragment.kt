package com.example.biladoniga_toscanini_tejerina.game.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.data.models.Team
import com.example.biladoniga_toscanini_tejerina.R
import com.example.biladoniga_toscanini_tejerina.game.viewmodel.GameViewModel
import com.example.biladoniga_toscanini_tejerina.utils.gone
import com.example.biladoniga_toscanini_tejerina.utils.visible
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class TurnResultAlternativeFragment : Fragment() {

    private var noOneButton: Button? = null
    private var firstTeamButton: Button? = null
    private var secondTeamButton: Button? = null
    private var thirdTeamButton: Button? = null

    private val gameViewModel by sharedViewModel<GameViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        activity?.let {
            (it as GameActivity).setToolbarTitle(getString(R.string.which_team_guess_it))
            it.showToolbar()
        }

        return inflater.inflate(R.layout.fragment_turn_result_alternative, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews(view)
        initClickListeners()
        initListeners()
    }

    private fun initViews(view: View) {
        noOneButton = view.findViewById(R.id.no_one_button)
        firstTeamButton = view.findViewById(R.id.team_button_1)
        firstTeamButton?.gone()
        secondTeamButton = view.findViewById(R.id.team_button_2)
        secondTeamButton?.gone()
        thirdTeamButton = view.findViewById(R.id.team_button_3)
        thirdTeamButton?.gone()

        gameViewModel.getTeams()
        gameViewModel.getNextTeamTurn()
    }

    private fun initClickListeners() {
        firstTeamButton?.setOnClickListener { updateTeamPosition(Team.firstTeam) }
        secondTeamButton?.setOnClickListener { updateTeamPosition(Team.secondTeam) }
        thirdTeamButton?.setOnClickListener { updateTeamPosition(Team.thirdTeam) }
        noOneButton?.setOnClickListener { updateTeamPosition(null) }
    }

    private fun updateTeamPosition(teamId: String?) {
        gameViewModel.increaseTeamPosition(teamId)
        navigateToPrincipalScreen()
    }

    private fun navigateToPrincipalScreen() {
        val navOption =
            NavOptions.Builder().setPopUpTo(R.id.turnResultAlternativeFragment, true).build()
        this.findNavController()
            .navigate(
                R.id.action_turnResultAlternativeFragment_to_gamePrincipalScreenFragment,
                null,
                navOption
            )
    }

    private fun initListeners() {
        initTeamTurnListener()
    }

    private fun initTeamTurnListener() {
        gameViewModel.nexTeamTurn.observe(
            viewLifecycleOwner,
            Observer { teamId ->
                when (teamId) {
                    Team.firstTeam -> {
                        firstTeamButton?.gone()
                        initSecondTeamListener()
                        initThirdTeamListener()
                    }
                    Team.secondTeam -> {
                        secondTeamButton?.gone()
                        initFirstTeamListener()
                        initThirdTeamListener()
                    }
                    Team.thirdTeam -> {
                        thirdTeamButton?.gone()
                        initFirstTeamListener()
                        initSecondTeamListener()
                    }
                }
            }
        )
    }

    private fun initFirstTeamListener() {
        gameViewModel.firstTeam.observe(
            viewLifecycleOwner,
            Observer { team ->
                team.image?.let {
                    firstTeamButton?.visible()
                }
            }
        )
    }

    private fun initSecondTeamListener() {
        gameViewModel.secondTeam.observe(
            viewLifecycleOwner,
            Observer { team ->
                team.image?.let {
                    secondTeamButton?.visible()
                }
            }
        )
    }

    private fun initThirdTeamListener() {
        gameViewModel.thirdTeam.observe(
            viewLifecycleOwner,
            Observer { team ->
                team.image?.let {
                    thirdTeamButton?.visible()
                }
            }
        )
    }
}
