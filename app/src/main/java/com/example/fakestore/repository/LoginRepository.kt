package com.example.fakestore.repository

import com.example.fakestore.ApiService
import com.example.fakestore.model.Login

class LoginRepository(private val service: ApiService) {
    suspend fun getAllUsers(): List<Login> {
        return service.getAllUsers()
    }

    fun validateLogin(users: List<Login>, username: String, password: String): Boolean {
        return users.any { it.username == username && it.password == password }
    }
}