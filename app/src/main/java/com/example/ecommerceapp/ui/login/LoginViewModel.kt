package com.example.ecommerceapp.ui.login


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ecommerceapp.data.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    private val _loginResult = MutableLiveData<Pair<Boolean, String?>>()
    val loginResult: LiveData<Pair<Boolean, String?>> = _loginResult

    fun login(email: String, password: String, useMock: Boolean = false) {
        viewModelScope.launch {
            val result = userRepository.login(email, password, useMock)
            if (result.isSuccess) {
                _loginResult.value = true to null
            } else {
                _loginResult.value = false to result.exceptionOrNull()?.message
            }
        }
    }
}
