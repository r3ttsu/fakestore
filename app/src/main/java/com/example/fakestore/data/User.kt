package com.example.fakestore.data

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user")
data class User(
    @PrimaryKey val id: Int,
    val email: String,
    val username: String,
    val phone: String,

    @Embedded(prefix = "name_")
    val name: Name,

    @Embedded(prefix = "address_")
    val address: Address,
)

data class Address(
//    @Embedded(prefix = "geo_")
//    val geolocation: Geolocation,
    val city: String,
    val street: String,
    val number: Int,
    val zipcode: String,
)

//data class Geolocation(
//    val lat: String,
//    val long: String,
//)

data class Name(
    val firstname: String,
    val lastname: String,
)