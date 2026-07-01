package com.snappy.samsung.repository

import android.content.Context
import com.snappy.samsung.database.ScreenshotDao
import com.snappy.samsung.database.ScreenshotEntity
import com.snappy.samsung.model.Screenshot
import com.snappy.samsung.utils.MediaStoreScanner
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class ScreenshotRepository(
    private val context: Context,
    private val screenshotDao: ScreenshotDao
) {
    fun getAllScreenshots(): Flow<List<Screenshot>> =
        screenshotDao.getAllScreenshots().map { entities ->
            entities.map { it.toDomain() }
        }

    fun getScreenshotsByApp(appName: String): Flow<List<Screenshot>> =
        screenshotDao.getScreenshotsByApp(appName).map { entities ->
            entities.map { it.toDomain() }
        }

    fun getFavoriteScreenshots(): Flow<List<Screenshot>> =
        screenshotDao.getFavoriteScreenshots().map { entities ->
            entities.map { it.toDomain() }
        }

    fun getCollections(): Flow<List<CollectionInfo>> =
        screenshotDao.getCollectionsFlow().map { list ->
            list.map { CollectionInfo(it.appName, it.count, it.latestUri, it.maxDate ?: 0L) }
        }

    suspend fun getScreenshotById(id: Long): Screenshot? = withContext(Dispatchers.IO) {
        screenshotDao.getScreenshotById(id)?.toDomain()
    }
    
    fun getScreenshotByIdFlow(id: Long): Flow<Screenshot?> =
        screenshotDao.getScreenshotByIdFlow(id).map { it?.toDomain() }

    suspend fun toggleFavorite(id: Long, favorite: Boolean) = withContext(Dispatchers.IO) {
        screenshotDao.updateFavorite(id, favorite)
    }

    suspend fun deleteFromDatabase(id: Long) = withContext(Dispatchers.IO) {
        screenshotDao.deleteScreenshot(id)
    }

    fun getTotalCountFlow(): Flow<Int> = screenshotDao.getTotalCountFlow()
    fun getFavoritesCountFlow(): Flow<Int> = screenshotDao.getFavoritesCountFlow()
    fun getTotalSizeFlow(): Flow<Long> = screenshotDao.getTotalSizeFlow().map { it ?: 0L }
    fun getLatestFavoriteUriFlow(): Flow<String?> = screenshotDao.getLatestFavoriteUriFlow()
    fun getLatestScreenshotUriFlow(): Flow<String?> = screenshotDao.getLatestScreenshotUriFlow()

    suspend fun syncScreenshots() = withContext(Dispatchers.IO) {
        val mediaStoreScreens = MediaStoreScanner.scanScreenshots(context)
        val mediaStoreIds = mediaStoreScreens.map { it.id }.toSet()

        val dbIds = screenshotDao.getAllIds().toSet()

        val newScreens = mediaStoreScreens.filter { it.id !in dbIds }
        if (newScreens.isNotEmpty()) {
            val entitiesToInsert = newScreens.map { ScreenshotEntity.fromDomain(it) }
            screenshotDao.insertScreenshots(entitiesToInsert)
        }

        val deletedIds = dbIds.filter { it !in mediaStoreIds }
        if (deletedIds.isNotEmpty()) {
            screenshotDao.deleteScreenshots(deletedIds)
        }
    }

    suspend fun refreshDatabase() = withContext(Dispatchers.IO) {
        screenshotDao.clearAll()
        val mediaStoreScreens = MediaStoreScanner.scanScreenshots(context)
        val entitiesToInsert = mediaStoreScreens.map { ScreenshotEntity.fromDomain(it) }
        screenshotDao.insertScreenshots(entitiesToInsert)
    }

    fun getCustomCollections(): Flow<List<CollectionInfo>> =
        screenshotDao.getCustomCollectionsFlow().map { list ->
            list.map { CollectionInfo(it.appName, it.count, it.latestUri, it.maxDate ?: 0L) }
        }

    fun getScreenshotsByCustomCollection(collectionName: String): Flow<List<Screenshot>> =
        screenshotDao.getScreenshotsByCustomCollection(collectionName).map { entities ->
            entities.map { it.toDomain() }
        }

    suspend fun getCustomCollectionNames(): List<String> = withContext(Dispatchers.IO) {
        screenshotDao.getCustomCollectionNames()
    }

    suspend fun createCustomCollection(name: String) = withContext(Dispatchers.IO) {
        screenshotDao.insertCustomCollection(com.snappy.samsung.database.CustomCollectionEntity(name = name))
    }

    suspend fun deleteCustomCollection(name: String) = withContext(Dispatchers.IO) {
        screenshotDao.deleteCustomCollection(name)
    }

    suspend fun addScreenshotToCustomCollection(screenshotId: Long, collectionName: String) = withContext(Dispatchers.IO) {
        screenshotDao.insertScreenshotToCollection(
            com.snappy.samsung.database.ScreenshotCustomCollectionCrossRef(screenshotId, collectionName)
        )
    }

    suspend fun removeScreenshotFromCustomCollection(screenshotId: Long, collectionName: String) = withContext(Dispatchers.IO) {
        screenshotDao.removeScreenshotFromCollection(screenshotId, collectionName)
    }

    suspend fun getCollectionsForScreenshot(screenshotId: Long): List<String> = withContext(Dispatchers.IO) {
        screenshotDao.getCollectionsForScreenshot(screenshotId)
    }

    fun getCollectionsForScreenshotFlow(screenshotId: Long): Flow<List<String>> =
        screenshotDao.getCollectionsForScreenshotFlow(screenshotId)
}

data class CollectionInfo(
    val appName: String,
    val count: Int,
    val latestUri: String?,
    val latestDate: Long = 0L
)
