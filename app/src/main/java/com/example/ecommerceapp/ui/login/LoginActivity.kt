package com.example.ecommerceapp.ui.login

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Switch
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.ecommerceapp.R
import com.example.ecommerceapp.ui.ProductListActivity
import com.example.ecommerceapp.utils.PreferenceManager
import dagger.hilt.android.AndroidEntryPoint
@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {

    private lateinit var etEmail: EditText
    private lateinit var etPassword: EditText
    private lateinit var btnLogin: Button
    private lateinit var progressBar: ProgressBar
    private lateinit var switchMock: Switch

    private val viewModel: LoginViewModel by viewModels()
    private var useMock = false
    private lateinit var prefManager: PreferenceManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        prefManager = PreferenceManager(this)
        initViews()
        loadSavedCredentials()
        setupObservers()
        setupListeners()
    }

    private fun initViews() {
        etEmail = findViewById(R.id.etEmail)
        etPassword = findViewById(R.id.etPassword)
        btnLogin = findViewById(R.id.btnLogin)
        progressBar = findViewById(R.id.progressBar)
        switchMock = findViewById(R.id.switchMock)
    }

    private fun loadSavedCredentials() {
        val (savedEmail, savedPassword, savedMock) = prefManager.getCredentials()
        etEmail.setText(savedEmail)
        etPassword.setText(savedPassword)
        switchMock.isChecked = savedMock
        useMock = savedMock
    }

    private fun setupObservers() {
        viewModel.loginResult.observe(this) { result ->
            progressBar.visibility = View.GONE
            btnLogin.isEnabled = true

            val (success, message) = result
            if (success) {
                // Save credentials
                prefManager.saveCredentials(
                    etEmail.text.toString().trim(),
                    etPassword.text.toString().trim(),
                    useMock
                )

                Toast.makeText(this, "Login Successful", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, ProductListActivity::class.java))
                finish()
            } else {
                Toast.makeText(this, message ?: "Login Failed", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setupListeners() {
        // Mock toggle
        switchMock.setOnCheckedChangeListener { _, isChecked ->
            useMock = isChecked
        }

        // Login button
        btnLogin.setOnClickListener {
            val email = etEmail.text.toString().trim()
            val password = etPassword.text.toString().trim()

            if (validateInput(email, password)) {
                progressBar.visibility = View.VISIBLE
                btnLogin.isEnabled = false
                viewModel.login(email, password, useMock)
            }
        }
    }

    private fun validateInput(email: String, password: String): Boolean {
        var valid = true

        // Email validation
        if (email.isEmpty()) {
            etEmail.error = "Email is required"
            valid = false
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etEmail.error = "Enter a valid email"
            valid = false
        } else {
            etEmail.error = null
        }

        // Password validation
        val passwordPattern = Regex("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#\$%^&+=]).{8,}\$")
        if (password.isEmpty()) {
            etPassword.error = "Password is required"
            valid = false
        } else if (!passwordPattern.matches(password)) {
            etPassword.error =
                "Password must be 8+ chars, include uppercase, lowercase, number & special char"
            valid = false
        } else {
            etPassword.error = null
        }

        return valid
    }
}
