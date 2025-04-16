package com.example.fakestore.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface UserDao {
    @Query("SELECT * FROM user")
    suspend fun getActiveUser(): User

    @Insert
    suspend fun setActiveUser(user: User)

    @Query("DELETE FROM user")
    suspend fun removeActiveUser()
}