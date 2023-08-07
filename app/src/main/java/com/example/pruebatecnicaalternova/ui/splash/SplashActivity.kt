package com.example.pruebatecnicaalternova.ui.splash

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.pruebatecnicaalternova.ui.Router
import com.example.pruebatecnicaalternova.ui.screenChange
import com.google.firebase.auth.FirebaseAuth

class SplashActivity : AppCompatActivity() {

    // region declaracion de varibles
    private lateinit var auth: FirebaseAuth
    // endregion

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        init()
    }

    // region funcion para inicarle al sistema a que pantalla dirigirse
    private fun init() {
        if (FirebaseAuth.getInstance().currentUser?.email != null) {
            goToHome()
        } else {
            goToLogin()
        }
    }
    // endregion

    // region funciones que indican  al archivo Router a que vista hacer el intent

    private fun goToLogin() {
        Router().goToScreens(screenChange.LOGIN, this, true)
    }

    private fun goToHome() {
        Router().goToScreens(screenChange.HOME, this, true)
    }

    // endregion

}