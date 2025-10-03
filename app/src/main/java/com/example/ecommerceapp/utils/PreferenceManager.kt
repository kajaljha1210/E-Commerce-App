package com.example.ecommerceapp.utils

import android.content.Context
import android.content.SharedPreferences

class PreferenceManager(context: Context) {

    private val prefs: SharedPreferences =
        context.getSharedPreferences("login_prefs", Context.MODE_PRIVATE)

    companion object {
        private const val KEY_EMAIL = "key_email"
        private const val KEY_PASSWORD = "key_password"
        private const val KEY_USE_MOCK = "key_use_mock"
    }

    fun saveCredentials(email: String, password: String, useMock: Boolean) {
        prefs.edit().apply {
            putString(KEY_EMAIL, email)
            putString(KEY_PASSWORD, password)
            putBoolean(KEY_USE_MOCK, useMock)
            apply()
        }
    }

    fun getCredentials(): Triple<String?, String?, Boolean> {
        val email = prefs.getString(KEY_EMAIL, null)
        val password = prefs.getString(KEY_PASSWORD, null)
        val useMock = prefs.getBoolean(KEY_USE_MOCK, false)
        return Triple(email, password, useMock)
    }

    fun clearCredentials() {
        prefs.edit().clear().apply()
    }
}
