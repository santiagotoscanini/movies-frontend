package com.example.biladoniga_toscanini_tejerina.utils

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.core.content.res.use
import com.example.biladoniga_toscanini_tejerina.R
import androidx.constraintlayout.widget.ConstraintLayout as ConstraintLayout

open class TeamButton @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private var label: TextView
    private var iconStart: View
    private var iconEnd: View
    private var isTeamReady = false
    private var isTeamEmpty = true

    init {
        LayoutInflater.from(context).inflate(R.layout.team_button, this, true)

        label = findViewById(R.id.label)
        iconStart = findViewById(R.id.icon_start)
        iconEnd = findViewById(R.id.icon_end)

        context.theme.obtainStyledAttributes(attrs, R.styleable.TeamButton, 0, 0).use {
            isTeamEmpty = it.getBoolean(R.styleable.TeamButton_isTeamEmpty, true)
            isTeamReady = it.getBoolean(R.styleable.TeamButton_isTeamReady, false)
            label.text = it.getText(R.styleable.TeamButton_android_text)
        }

        when {
            isTeamEmpty -> {
                setButtonNoReady()
            }
            isTeamReady -> {
                setButtonReady()
            }
            else -> {
                setButtonLittleReady()
            }
        }


        isClickable = true
        isFocusable = true
    }

    fun setButtonNoReady() {
        label.setTextColor(context.getColor(R.color.gray_color))
        iconStart.visibility = View.INVISIBLE
        iconEnd.visibility = View.INVISIBLE
    }

    fun setButtonLittleReady() {
        label.setTextColor(context.getColor(R.color.gray_color))
        iconStart.visibility = View.VISIBLE
        iconEnd.visibility = View.INVISIBLE
    }

    fun setButtonAlmostReady(){
        label.setTextColor(context.getColor(R.color.gray_color))
        iconStart.visibility = View.VISIBLE
        iconEnd.visibility = View.VISIBLE
    }

    fun setButtonReady() {
        label.setTextColor(context.getColor(R.color.black))
        iconStart.visibility = View.VISIBLE
        iconEnd.visibility = View.VISIBLE
    }

}
