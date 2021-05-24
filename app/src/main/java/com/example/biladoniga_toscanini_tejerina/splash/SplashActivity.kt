package com.example.biladoniga_toscanini_tejerina.splash

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.example.biladoniga_toscanini_tejerina.menu.MenuActivity
import com.example.biladoniga_toscanini_tejerina.R


class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        val h = Looper.myLooper()?.let { Handler(it) }
        h?.postDelayed({
            startActivity(Intent(this, MenuActivity::class.java))
        }, 5000)
    }
}