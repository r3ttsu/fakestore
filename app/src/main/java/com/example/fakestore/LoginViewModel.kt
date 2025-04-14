package com.example.fakestore

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fakestore.model.Login
import com.example.fakestore.repository.LoginRepository
import com.google.gson.Gson
import kotlinx.coroutines.launch

class LoginViewModel(private val repository: LoginRepository) : ViewModel() {
    private val _userState = MutableLiveData(LoginState())
    val userState: LiveData<LoginState> = _userState

    init {
        fetchUsers()
    }

    private fun fetchUsers() {
        viewModelScope.launch {
            try {
                val response = repository.getAllUsers()
                _userState.value = _userState.value?.copy(
                    isLoading = false,
                    data = response,
                    error = null
                )
            } catch (e: Exception) {
                _userState.value = _userState.value?.copy(
                    isLoading = false,
                    data = emptyList(),
                    error = "Error fetch users: ${e.message}"
                )
            }
        }
    }

    fun validateUser(username: String, password: String): Boolean {
        val users = _userState.value?.data ?: return false
        return repository.validateLogin(users, username, password)
    }

    data class LoginState(
        val isLoading: Boolean = true,
        val data: List<Login> = emptyList(),
        val error: String? = null,
    )
}