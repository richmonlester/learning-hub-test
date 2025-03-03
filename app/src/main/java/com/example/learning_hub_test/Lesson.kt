package com.example.learning_hub_test

data class Lesson(
    val id: String = "",
    val title: String = "",
    val content: String = "",
    val progress: Int = 0 // Added progress tracking
)
