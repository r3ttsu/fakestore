package com.example.fakestore.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fakestore.data.User
import com.example.fakestore.repository.UserRepository
import kotlinx.coroutines.launch
import javax.inject.Inject

class SplashViewModel @Inject constructor(private val repository: UserRepository) : ViewModel() {
    private val _splashState = MutableLiveData(SplashState())
    val splashState: LiveData<SplashState> = _splashState

    init {
        checkLoggedInUser()
    }

    private fun checkLoggedInUser() {
        viewModelScope.launch {
            try {
                val user = repository.getUser()
                _splashState.value = _splashState.value?.copy(
                    data = user
                )
            } catch (e: Exception) {
                _splashState.value = _splashState.value?.copy(
                    error = "Terjadi kesalahan: ${e.message}"
                )
            }
        }
    }

    fun isLoggedIn(): Boolean {
        return _splashState.value?.data != null
    }

    data class SplashState(
        val data: User? = null,
        val error: String? = null,
    )
}