package com.example.pruebatecnicaalternova.ui

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import com.example.pruebatecnicaalternova.ui.detail.DetailActivity
import com.example.pruebatecnicaalternova.ui.home.HomeActivity
import com.example.pruebatecnicaalternova.ui.login.LoginActivity
import com.example.pruebatecnicaalternova.ui.screenChange.*
import com.example.pruebatecnicaalternova.ui.splash.SplashActivity


enum class screenChange() {
    SPLAHS,
    LOGIN,
    HOME,
    DETAIL
}

class Router : AppCompatActivity() {

    /**
     * ir a pantallas posibles screen home, records,perfil
     */
    fun goToScreens(screen: screenChange, activity: Activity, finishActivity: Boolean) {

        when (screen) {
            SPLAHS -> {
                activity.startActivity(Intent(activity, SplashActivity::class.java))
            }
            LOGIN -> {
                activity.startActivity(Intent(activity, LoginActivity::class.java))
            }
            HOME -> {
                activity.startActivity(Intent(activity, HomeActivity::class.java))
            }
            DETAIL->{
                activity.startActivity(Intent(activity, DetailActivity::class.java))
            }


        }
        if (finishActivity) {
            activity.finish()
        }
    }

}