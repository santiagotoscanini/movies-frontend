package com.example.biladoniga_toscanini_tejerina.game.views

import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.SystemClock
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Chronometer
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.example.biladoniga_toscanini_tejerina.R
import com.example.biladoniga_toscanini_tejerina.commons.STOP_TIME
import com.example.biladoniga_toscanini_tejerina.commons.TIMER_BASE
import com.example.biladoniga_toscanini_tejerina.utils.coloredStatusBarMode
import com.example.biladoniga_toscanini_tejerina.utils.gone
import com.example.biladoniga_toscanini_tejerina.utils.visible

class RepresentMovieFragment : Fragment(), Chronometer.OnChronometerTickListener {

    private var timerContainer: View? = null
    private var stopTimerTextView: TextView? = null
    private var timerChronometer: Chronometer? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        activity?.let {
            (it as GameActivity).hideToolbar()
            it.coloredStatusBarMode(it.getColor(R.color.mint_green))
        }

        return inflater.inflate(R.layout.fragment_represent_movie, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews(view)
    }

    private fun initViews(view: View) {
        timerContainer = view.findViewById(R.id.timer_container)
        stopTimerTextView = view.findViewById(R.id.timer_stop_message)
        timerChronometer = view.findViewById(R.id.timer_chronometer)

        timerChronometer?.onChronometerTickListener = this
        timerChronometer?.base = (SystemClock.elapsedRealtime()+ TIMER_BASE)
        timerChronometer?.start()
    }

    private fun setStopStyle() {
        activity?.let {
            it.coloredStatusBarMode(it.getColor(R.color.cancel_color))
            timerContainer?.setBackgroundColor(it.getColor(R.color.cancel_color))
        }
        timerChronometer?.gone()
        stopTimerTextView?.text = getString(R.string.stop)
        stopTimerTextView?.visible()
        playAlarm()
        navigateToTurnResult()
    }

    override fun onChronometerTick(chronometer: Chronometer?) {
        chronometer?.let {
            val elapsedMillis = SystemClock.elapsedRealtime() - chronometer.base
            if (elapsedMillis > STOP_TIME) {
                it.stop()
                setStopStyle()
            }
        }
    }

    private fun playAlarm() {
        val mediaPlayer: MediaPlayer? = MediaPlayer.create(context, R.raw.alarm)
        mediaPlayer?.start()
    }

    private fun navigateToTurnResult() {
        val h = Looper.myLooper()?.let { Handler(it) }
        h?.postDelayed({
            val navOption = NavOptions.Builder().setPopUpTo(
                R.id.representMovieFragment,
                true
            ).build()
            this.findNavController()
                .navigate(
                    R.id.action_representMovieFragment_to_turnResultFragment,
                    null,
                    navOption
                )
        }, 2000)
    }
}
