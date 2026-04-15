package com.example.locationtracking.models

data class AppUsers(
    val userId: String = "",
    val email: String = "",
    val username: String = "",
    val latitude: Double? = null,
    val longitude: Double? = null
) {
    constructor() : this("", "", "", null, null)
}