package com.example.ecommerceapp

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.example.ecommerceapp.ui.login.LoginActivity
import com.example.ecommerceapp.ui.ProductListActivity
import com.example.ecommerceapp.utils.PreferenceManager
import com.google.firebase.auth.FirebaseAuth
class MainActivity : AppCompatActivity() {

    private lateinit var prefManager: PreferenceManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        prefManager = PreferenceManager(this)

        Handler(Looper.getMainLooper()).postDelayed({
            val (savedEmail, savedPassword, useMock) = prefManager.getCredentials()

            val nextActivity = if (useMock && !savedEmail.isNullOrEmpty() && !savedPassword.isNullOrEmpty()) {
                // Mock credentials saved → auto-login
                ProductListActivity::class.java
            } else if (!useMock && FirebaseAuth.getInstance().currentUser != null) {
                // Firebase user already logged in → auto-login
                ProductListActivity::class.java
            } else {
                // No saved credentials → show login
                LoginActivity::class.java
            }

            startActivity(Intent(this, nextActivity))
            finish()
        }, 2000)
    }
}
