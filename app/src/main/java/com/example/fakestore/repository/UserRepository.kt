package com.example.fakestore.repository

import com.example.fakestore.data.User
import com.example.fakestore.data.UserDao
import javax.inject.Inject

class UserRepository @Inject constructor(private val userDao: UserDao) {
    suspend fun insertUser(user: User) = userDao.setActiveUser(user)

    suspend fun getUser() = userDao.getActiveUser()

    suspend fun deleteUser() = userDao.removeActiveUser()
}