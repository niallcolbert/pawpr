package com.neddoesdev.pawpr.activities

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.common.SignInButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.neddoesdev.pawpr.R
import com.neddoesdev.pawpr.main.MainApp
import kotlinx.android.synthetic.main.activity_auth.*
import org.jetbrains.anko.startActivity

class AuthActivity : AppCompatActivity(), View.OnClickListener {

    lateinit var app: MainApp

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)
        app = application as MainApp
        // Buttons
        emailSignInButton.setOnClickListener(this)
        emailCreateAccountButton.setOnClickListener(this)

        app.auth = FirebaseAuth.getInstance()

        sign_in_button.setSize(SignInButton.SIZE_WIDE)
        sign_in_button.setColorScheme(0)
        sign_in_button.setOnClickListener(this)

    }

    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = app.auth.currentUser
    }

    private fun createAccount(email: String, password: String) {
        app.auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    startActivity<MainActivity>()
                } else {
                    Toast.makeText(baseContext, "Authentication failed.",
                        Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun signIn(email: String, password: String) {
        app.auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    startActivity<MainActivity>()
                } else {
                    Toast.makeText(baseContext, "Authentication failed.",
                        Toast.LENGTH_SHORT).show()
                }
                if (!task.isSuccessful) {
                    Toast.makeText(baseContext, "Authentication failed.",
                        Toast.LENGTH_SHORT).show()
                }
           }
    }

    override fun onClick(v: View) {
        val i = v.id
        when (i) {
            R.id.emailCreateAccountButton -> createAccount(fieldSignupEmail.text.toString(), fieldSignupPassword.text.toString())
            R.id.emailSignInButton -> signIn(fieldLoginEmail.text.toString(), fieldLoginPassword.text.toString())
        }
    }
}
