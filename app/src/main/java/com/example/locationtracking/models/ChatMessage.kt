package com.example.locationtracking.models

data class ChatMessage(
    val senderId: String = "",
    val receiverId: String = "",
    val text: String = "",
    val timestamp: Long = 0L
)