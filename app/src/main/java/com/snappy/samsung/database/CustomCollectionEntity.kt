package com.snappy.samsung.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "custom_collections")
data class CustomCollectionEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String
)
