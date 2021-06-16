package com.example.biladoniga_toscanini_tejerina.game.views

import android.animation.AnimatorInflater
import android.animation.AnimatorSet
import android.hardware.Sensor
import android.hardware.SensorManager
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.data.models.Movie
import com.data.models.Team
import com.example.biladoniga_toscanini_tejerina.R
import com.example.biladoniga_toscanini_tejerina.game.viewmodel.GameViewModel
import com.example.biladoniga_toscanini_tejerina.utils.invisible
import com.example.biladoniga_toscanini_tejerina.utils.shake.OnShakeListener
import com.example.biladoniga_toscanini_tejerina.utils.shake.ShakeDetector
import com.example.biladoniga_toscanini_tejerina.utils.visible
import com.squareup.picasso.Picasso
import org.koin.androidx.viewmodel.ext.android.sharedViewModel


class PickMovieFragment : Fragment() {

    private lateinit var currentTeamTurnId: String
    private var isShowingTheMovie = false

    private var startShowButton: Button? = null
    private var changeMovieButton: Button? = null
    private var cardHide: View? = null
    private var cardShow: View? = null
    private var movieTitle: TextView? = null
    private var movieImage: ImageView? = null
    private var cardInstructions: TextView? = null

    // The following are used for the flip animation
    private lateinit var frontAnimation: AnimatorSet
    private lateinit var backAnimation: AnimatorSet

    // The following are used for the shake detection
    private var sensorManager: SensorManager? = null
    private var accelerometer: Sensor? = null
    private var shakeDetector: ShakeDetector? = null

    private val gameViewModel by sharedViewModel<GameViewModel>()

    companion object {
        const val nextTeamTurn = "nextTeamTurn"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_pick_movie, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.let {
            (it as GameActivity).showLoader()
        }

        currentTeamTurnId = arguments?.getString(nextTeamTurn) ?: Team.firstTeam
        gameViewModel.getNextTeammateTurn(currentTeamTurnId)

        showTeamTurn()
        initViews(view)
        initListeners()
        hideOptions()
        initFlip()
        initSensor()
        val h = Looper.myLooper()?.let { Handler(it) }
        h?.postDelayed({
            getMovie()
        }, 2000)
    }

    private fun showTeamTurn() {
        activity?.let {
            when (currentTeamTurnId) {
                Team.firstTeam -> (it as GameActivity).showLoader(
                    getString(R.string.team_1_turn)
                )
                Team.secondTeam -> (it as GameActivity).showLoader(
                    getString(R.string.team_2_turn)
                )
                Team.thirdTeam -> (it as GameActivity).showLoader(
                    getString(R.string.team_3_turn)
                )
            }
        }
    }

    private fun initViews(view: View) {
        startShowButton = view.findViewById(R.id.start_show_button)
        changeMovieButton = view.findViewById(R.id.change_movie_button)
        cardHide = view.findViewById(R.id.card_hide)
        cardShow = view.findViewById(R.id.card_show)
        movieTitle = view.findViewById(R.id.movie_title)
        movieImage = view.findViewById(R.id.movie_image)
        cardInstructions = view.findViewById(R.id.card_instructions)

        startShowButton?.setOnClickListener { startShow() }
        changeMovieButton?.setOnClickListener { changeMovie() }
        cardHide?.setOnClickListener {
            showMovie()
        }
    }

    private fun hideOptions() {
        cardHide?.isClickable = true
        startShowButton?.invisible()
        startShowButton?.isClickable = false
        changeMovieButton?.invisible()
        changeMovieButton?.isClickable = false
        cardInstructions?.text = getString(R.string.card_hide_instructions)
    }

    private fun showOptions() {
        cardHide?.isClickable = false
        startShowButton?.visible()
        startShowButton?.isClickable = true
        changeMovieButton?.visible()
        changeMovieButton?.isClickable = true
        cardInstructions?.text = getString(R.string.card_show_instructions)
    }

    private fun showMovie() {
        getMovie()
        flipCardShowMovie()
        showOptions()
        isShowingTheMovie = true
        registerShakeListener()
    }

    private fun changeMovie() {
        flipCardHideMovie()
        hideOptions()
        isShowingTheMovie = false
        unregisterShakeListener()
    }

    private fun startShow() {
        val navOption = NavOptions.Builder().setPopUpTo(
            R.id.pickMovieFragment, true).build()
        this.findNavController().navigate(
            R.id.action_pickMovieFragment_to_representMovieFragment,
            null,
            navOption)
    }

    private fun initFlip() {
        modifyCameraScale()
        setFrontAnimation()
    }

    private fun modifyCameraScale() {
        activity?.applicationContext?.let {
            val scale = it.resources.displayMetrics.density
            cardHide?.cameraDistance = 8000 * scale
            cardShow?.cameraDistance = 8000 * scale
        }
    }

    private fun setFrontAnimation() {
        frontAnimation = AnimatorInflater.loadAnimator(
            activity?.applicationContext,
            R.animator.front_animator
        ) as AnimatorSet
        backAnimation = AnimatorInflater.loadAnimator(
            activity?.applicationContext,
            R.animator.back_animator
        ) as AnimatorSet
    }

    private fun flipCardShowMovie() {
        frontAnimation.setTarget(cardHide)
        backAnimation.setTarget(cardShow)
        frontAnimation.start()
        backAnimation.start()
    }

    private fun flipCardHideMovie() {
        frontAnimation.setTarget(cardShow)
        backAnimation.setTarget(cardHide)
        frontAnimation.start()
        backAnimation.start()
    }

    private fun initSensor() {
        sensorManager =
            activity?.getSystemService(AppCompatActivity.SENSOR_SERVICE) as SensorManager
        accelerometer = sensorManager?.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        shakeDetector = ShakeDetector()
        shakeDetector?.setOnShakeListener(object : OnShakeListener {
            override fun onShake(count: Int) {
                changeMovie()
            }
        })
    }

    override fun onResume() {
        super.onResume()
        // to register the Session Manager Listener onResume
        if (isShowingTheMovie) {
            registerShakeListener()
        }
    }

    override fun onPause() { // to unregister the Sensor Manager onPause
        unregisterShakeListener()
        super.onPause()
    }

    private fun registerShakeListener() {
        sensorManager?.registerListener(
            shakeDetector,
            accelerometer,
            SensorManager.SENSOR_DELAY_UI
        )
    }

    private fun unregisterShakeListener() {
        sensorManager?.unregisterListener(shakeDetector)
    }

    private fun initListeners() {
        initTeammateTurnNameListener()
        initMovieListener()
    }

    private fun initTeammateTurnNameListener() {
        gameViewModel.nextTeammateTurn.observe(
            viewLifecycleOwner,
            Observer { teammateName ->
                setNextTeammateTurnName(teammateName)
            }
        )
    }

    private fun setNextTeammateTurnName(teammateName: String) {
        activity?.let {
            (it as GameActivity).apply {
                this.showToolbar()
                this.setToolbarTitle("${teammateName}${getString(R.string.acts)}")
            }
        }
    }

    private fun initMovieListener() {
        gameViewModel.movie.observe(
            viewLifecycleOwner,
            Observer { movie ->
                setMovieData(movie)
            }
        )
    }

    private fun setMovieData(movie: Movie) {
        Picasso.get().load(movie.imageURL).into(movieImage)
        movieTitle?.text = movie.movieName
    }

    private fun getMovie() {
        gameViewModel.getMovie()
    }
}
