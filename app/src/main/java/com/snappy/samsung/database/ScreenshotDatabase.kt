package com.snappy.samsung.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [
        ScreenshotEntity::class,
        CustomCollectionEntity::class,
        ScreenshotCustomCollectionCrossRef::class
    ],
    version = 2,
    exportSchema = false
)
abstract class ScreenshotDatabase : RoomDatabase() {
    abstract fun screenshotDao(): ScreenshotDao

    companion object {
        @Volatile
        private var INSTANCE: ScreenshotDatabase? = null

        fun getDatabase(context: Context): ScreenshotDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ScreenshotDatabase::class.java,
                    "screenshot_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
