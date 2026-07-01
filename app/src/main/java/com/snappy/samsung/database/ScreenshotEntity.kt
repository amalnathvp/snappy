package com.snappy.samsung.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.snappy.samsung.model.Screenshot

@Entity(tableName = "screenshots")
data class ScreenshotEntity(
    @PrimaryKey val id: Long, // MediaStore ID used as Primary Key
    val imageUri: String,
    val filename: String,
    val appName: String,
    val dateTaken: Long,
    val fileSize: Long,
    val favorite: Boolean
) {
    fun toDomain(): Screenshot = Screenshot(
        id = id,
        imageUri = imageUri,
        filename = filename,
        appName = appName,
        dateTaken = dateTaken,
        fileSize = fileSize,
        favorite = favorite
    )

    companion object {
        fun fromDomain(screenshot: Screenshot): ScreenshotEntity = ScreenshotEntity(
            id = screenshot.id,
            imageUri = screenshot.imageUri,
            filename = screenshot.filename,
            appName = screenshot.appName,
            dateTaken = screenshot.dateTaken,
            fileSize = screenshot.fileSize,
            favorite = screenshot.favorite
        )
    }
}
