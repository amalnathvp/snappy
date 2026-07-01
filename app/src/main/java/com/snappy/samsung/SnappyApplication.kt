package com.snappy.samsung

import android.app.Application
import com.snappy.samsung.database.ScreenshotDatabase
import com.snappy.samsung.repository.ScreenshotRepository

class SnappyApplication : Application() {
    val database by lazy { ScreenshotDatabase.getDatabase(this) }
    val repository by lazy { ScreenshotRepository(this, database.screenshotDao()) }
}
