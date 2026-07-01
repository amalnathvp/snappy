package com.snappy.samsung.database

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index

@Entity(
    tableName = "screenshot_custom_collection",
    primaryKeys = ["screenshotId", "collectionName"],
    indices = [Index(value = ["collectionName"])]
)
data class ScreenshotCustomCollectionCrossRef(
    val screenshotId: Long,
    val collectionName: String
)
