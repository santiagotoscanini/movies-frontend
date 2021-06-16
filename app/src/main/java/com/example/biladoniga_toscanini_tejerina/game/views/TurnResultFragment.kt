package com.example.biladoniga_toscanini_tejerina.game.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.example.biladoniga_toscanini_tejerina.R
import com.example.biladoniga_toscanini_tejerina.game.viewmodel.GameViewModel
import com.example.biladoniga_toscanini_tejerina.utils.coloredStatusBarMode
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class TurnResultFragment : Fragment() {

    private var yesButton: Button? = null
    private var noButton: Button? = null

    private val gameViewModel by sharedViewModel<GameViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        activity?.let {
            it.coloredStatusBarMode(it.getColor(R.color.white))
            (it as GameActivity).setToolbarTitle(getString(R.string.did_team_guess))
            it.showToolbar()
        }

        return inflater.inflate(R.layout.fragment_turn_result, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews(view)
    }

    private fun initViews(view: View) {
        yesButton = view.findViewById(R.id.yes_button)
        noButton = view.findViewById(R.id.no_button)

        yesButton?.setOnClickListener {
            updateTeamPosition()
            navigateToPrincipalScreen()
        }
        noButton?.setOnClickListener {
            navigateToAlternative()
        }
    }

    private fun updateTeamPosition() {
        gameViewModel.increaseTurnTeamPosition()
    }

    private fun navigateToPrincipalScreen() {
        val navOption = NavOptions.Builder().setPopUpTo(R.id.teamResultFragment, true).build()
        this.findNavController()
            .navigate(
                R.id.action_teamResultFragment_to_gamePrincipalScreenFragment,
                null,
                navOption
            )
    }

    private fun navigateToAlternative() {
        val navOption = NavOptions.Builder().setPopUpTo(R.id.teamResultFragment, true).build()
        this.findNavController()
            .navigate(
                R.id.action_teamResultFragment_to_turnResultAlternativeFragment,
                null,
                navOption
            )
    }
}
