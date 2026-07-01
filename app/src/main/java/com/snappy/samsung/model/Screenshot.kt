package com.snappy.samsung.model

data class Screenshot(
    val id: Long,
    val imageUri: String,
    val filename: String,
    val appName: String,
    val dateTaken: Long,
    val fileSize: Long,
    val favorite: Boolean
)
