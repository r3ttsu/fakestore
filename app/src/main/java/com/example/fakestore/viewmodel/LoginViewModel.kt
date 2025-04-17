package com.example.fakestore.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fakestore.data.Address
import com.example.fakestore.data.Name
import com.example.fakestore.data.User
import com.example.fakestore.model.Login
import com.example.fakestore.repository.LoginRepository
import com.example.fakestore.repository.UserRepository
import kotlinx.coroutines.launch
import javax.inject.Inject

class LoginViewModel @Inject constructor(
    private val repository: LoginRepository,
    private val userRepository: UserRepository
) : ViewModel() {
    private val _userState = MutableLiveData(LoginState())
    val userState: LiveData<LoginState> = _userState

    init {
        fetchUsers()
        checkLoggedInUser()
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

    private fun checkLoggedInUser() {
        viewModelScope.launch {
            val user = userRepository.getUser()
            _userState.value = _userState.value?.copy(
                user = user
            )
        }
    }

    fun isLoggedIn(): Boolean {
        return _userState.value?.user != null
    }

    fun validateUser(username: String, password: String): Boolean {
        val users = _userState.value?.data ?: return false
        return repository.validateLogin(users, username, password)
    }

    fun setActiveUser(username: String, password: String) {
        viewModelScope.launch {
            val users = _userState.value?.data
            val user = users?.let { repository.getUser(it, username, password) }
            if (user != null) {
                val newUser = User(
                    id = user.id,
                    email = user.email,
                    username = user.username,
                    phone = user.phone,
                    name = Name(user.name.firstname, user.name.lastname),
                    address = Address(
                        user.address.city,
                        user.address.street,
                        user.address.number,
                        user.address.zipcode
                    )
                )
                userRepository.insertUser(newUser)
            }
        }
    }

    data class LoginState(
        val isLoading: Boolean = true,
        val data: List<Login> = emptyList(),
        val user: User? = null,
        val error: String? = null,
    )
}