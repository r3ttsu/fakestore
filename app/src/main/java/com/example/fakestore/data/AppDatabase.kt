package com.example.fakestore.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [User::class, Cart::class], version = 2)
abstract class AppDatabase: RoomDatabase(){
    abstract fun userDao(): UserDao
    abstract fun cartDao(): CartDao
}