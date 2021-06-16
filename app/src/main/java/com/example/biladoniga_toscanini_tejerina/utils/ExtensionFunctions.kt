package com.example.biladoniga_toscanini_tejerina.utils

import android.app.Activity
import android.graphics.Color
import android.view.View
import android.view.ViewGroup
import androidx.annotation.ColorInt
import androidx.core.graphics.ColorUtils

fun View.setMarginExtensionFunction(start: Int = 0, top: Int = 0, end: Int = 0, bottom: Int) {
    val params = layoutParams as ViewGroup.MarginLayoutParams
    params.setMargins(start, top, end, bottom)
    layoutParams = params
}

fun View.gone() {
    visibility = View.GONE
}

fun View.visible() {
    visibility = View.VISIBLE
}

fun View.invisible() {
    visibility = View.INVISIBLE
}

fun Activity.coloredStatusBarMode(@ColorInt color: Int = Color.WHITE, lightSystemUI: Boolean? = null) {
    var flags: Int = window.decorView.systemUiVisibility // get current flags
    var systemLightUIFlag = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
    var setSystemUILight = lightSystemUI

    if (setSystemUILight == null) {
        // Automatically check if the desired status bar is dark or light
        setSystemUILight = ColorUtils.calculateLuminance(color) < 0.5
    }

    flags = if (setSystemUILight) {
        // Set System UI Light (Battery Status Icon, Clock, etc)
        removeFlag(flags, systemLightUIFlag)
    } else {
        // Set System UI Dark (Battery Status Icon, Clock, etc)
        addFlag(flags, systemLightUIFlag)
    }

    window.decorView.systemUiVisibility = flags
    window.statusBarColor = color
}

private fun containsFlag(flags: Int, flagToCheck: Int) = (flags and flagToCheck) != 0

private fun addFlag(flags: Int, flagToAdd: Int): Int {
    return if (!containsFlag(flags, flagToAdd)) {
        flags or flagToAdd
    } else {
        flags
    }
}

private fun removeFlag(flags: Int, flagToRemove: Int): Int {
    return if (containsFlag(flags, flagToRemove)) {
        flags and flagToRemove.inv()
    } else {
        flags
    }
}
