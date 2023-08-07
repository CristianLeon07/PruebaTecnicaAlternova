package com.example.pruebatecnicaalternova.ui.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.pruebatecnicaalternova.R
import com.example.pruebatecnicaalternova.databinding.ActivityLoginBinding
import com.example.pruebatecnicaalternova.ui.home.HomeActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider

class LoginActivity : AppCompatActivity() {

    // region declaracion de variables

    private lateinit var binding: ActivityLoginBinding
    private lateinit var client: GoogleSignInClient
    private lateinit var auth: FirebaseAuth

    companion object {
        private const val RC_SIGN_IN = 10001
    }
    // endregion

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        init()
        iniEvent()
    }

    // region funcion inicializar el modulo de auntenticacion de firebase

    private fun init() {
        auth = FirebaseAuth.getInstance()
        val options = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            // dont worry about this error
            .requestEmail()
            .build()
        client = GoogleSignIn.getClient(this, options)

    }
    // endregion

    // region funcion evento de clic para levantar el dialogo e ingresar una cuenta

    private fun iniEvent() {
        binding.signInWithGoogle.setOnClickListener {
            clientSigOut()
        }
    }

    private fun clientSigOut() {
        // Cerrar sesión y eliminar credenciales almacenadas
        client.signOut().addOnCompleteListener(this) {

            val intent = client.signInIntent
            startActivityForResult(intent, 10001)
        }
    }
    // endregion

    // region  funcion sobre escrita para atrapar el resultado de la cuenta ingresada y hacer el intent a la otra vista

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 10001) {
            try {
                val task = GoogleSignIn.getSignedInAccountFromIntent(data)
                val account = task.getResult(ApiException::class.java)
                val credential = GoogleAuthProvider.getCredential(account.idToken, null)
                FirebaseAuth.getInstance().signInWithCredential(credential)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {

                            startActivity(Intent(this, HomeActivity::class.java))

                        } else {
                            Toast.makeText(this, task.exception?.message, Toast.LENGTH_SHORT)
                                .show()
                        }

                    }
            } catch (e: Exception) {
                Log.i("errorInicioSesion", e.message.toString())
            }
        }
    }
    // endregion

}
