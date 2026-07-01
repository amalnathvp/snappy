package com.snappy.samsung.database

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface ScreenshotDao {
    @Query("SELECT * FROM screenshots ORDER BY dateTaken DESC")
    fun getAllScreenshots(): Flow<List<ScreenshotEntity>>

    @Query("SELECT * FROM screenshots WHERE appName = :appName ORDER BY dateTaken DESC")
    fun getScreenshotsByApp(appName: String): Flow<List<ScreenshotEntity>>

    @Query("SELECT * FROM screenshots WHERE favorite = 1 ORDER BY dateTaken DESC")
    fun getFavoriteScreenshots(): Flow<List<ScreenshotEntity>>

    @Query("SELECT * FROM screenshots WHERE filename LIKE '%' || :query || '%' OR appName LIKE '%' || :query || '%' ORDER BY dateTaken DESC")
    fun searchScreenshots(query: String): Flow<List<ScreenshotEntity>>

    @Query("SELECT * FROM screenshots WHERE id = :id LIMIT 1")
    suspend fun getScreenshotById(id: Long): ScreenshotEntity?

    @Query("SELECT * FROM screenshots WHERE id = :id LIMIT 1")
    fun getScreenshotByIdFlow(id: Long): Flow<ScreenshotEntity?>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertScreenshots(screenshots: List<ScreenshotEntity>): List<Long>

    @Query("UPDATE screenshots SET favorite = :favorite WHERE id = :id")
    suspend fun updateFavorite(id: Long, favorite: Boolean)

    @Query("DELETE FROM screenshots WHERE id = :id")
    suspend fun deleteScreenshot(id: Long)

    @Query("DELETE FROM screenshots WHERE id in (:ids)")
    suspend fun deleteScreenshots(ids: List<Long>)

    @Query("""
        SELECT appName, COUNT(*) as count, 
        (SELECT imageUri FROM screenshots s2 WHERE s2.appName = s1.appName ORDER BY s2.dateTaken DESC LIMIT 1) as latestUri,
        (SELECT MAX(dateTaken) FROM screenshots s3 WHERE s3.appName = s1.appName) as maxDate
        FROM screenshots s1 
        GROUP BY appName
    """)
    fun getCollectionsFlow(): Flow<List<CollectionInfoEntity>>

    @Query("SELECT COUNT(*) FROM screenshots")
    fun getTotalCountFlow(): Flow<Int>

    @Query("SELECT COUNT(*) FROM screenshots WHERE favorite = 1")
    fun getFavoritesCountFlow(): Flow<Int>

    @Query("SELECT SUM(fileSize) FROM screenshots")
    fun getTotalSizeFlow(): Flow<Long?>

    @Query("SELECT imageUri FROM screenshots WHERE favorite = 1 ORDER BY dateTaken DESC LIMIT 1")
    fun getLatestFavoriteUriFlow(): Flow<String?>

    @Query("SELECT imageUri FROM screenshots ORDER BY dateTaken DESC LIMIT 1")
    fun getLatestScreenshotUriFlow(): Flow<String?>
    
    @Query("DELETE FROM screenshots")
    suspend fun clearAll()
    
    @Query("SELECT id FROM screenshots")
    suspend fun getAllIds(): List<Long>

    @Query("SELECT name FROM custom_collections ORDER BY name ASC")
    suspend fun getCustomCollectionNames(): List<String>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertCustomCollection(collection: CustomCollectionEntity)

    @Query("DELETE FROM custom_collections WHERE name = :name")
    suspend fun deleteCustomCollection(name: String)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertScreenshotToCollection(ref: ScreenshotCustomCollectionCrossRef)

    @Query("DELETE FROM screenshot_custom_collection WHERE screenshotId = :screenshotId AND collectionName = :collectionName")
    suspend fun removeScreenshotFromCollection(screenshotId: Long, collectionName: String)

    @Query("SELECT collectionName FROM screenshot_custom_collection WHERE screenshotId = :screenshotId")
    suspend fun getCollectionsForScreenshot(screenshotId: Long): List<String>

    @Query("SELECT collectionName FROM screenshot_custom_collection WHERE screenshotId = :screenshotId")
    fun getCollectionsForScreenshotFlow(screenshotId: Long): Flow<List<String>>

    @Query("""
        SELECT collectionName as appName, COUNT(*) as count,
        (SELECT imageUri FROM screenshots s2 INNER JOIN screenshot_custom_collection j2 ON s2.id = j2.screenshotId WHERE j2.collectionName = j.collectionName ORDER BY s2.dateTaken DESC LIMIT 1) as latestUri,
        (SELECT MAX(s3.dateTaken) FROM screenshots s3 INNER JOIN screenshot_custom_collection j3 ON s3.id = j3.screenshotId WHERE j3.collectionName = j.collectionName) as maxDate
        FROM screenshot_custom_collection j
        GROUP BY collectionName
    """)
    fun getCustomCollectionsFlow(): Flow<List<CollectionInfoEntity>>

    @Query("""
        SELECT s.* FROM screenshots s
        INNER JOIN screenshot_custom_collection j ON s.id = j.screenshotId
        WHERE j.collectionName = :collectionName
        ORDER BY s.dateTaken DESC
    """)
    fun getScreenshotsByCustomCollection(collectionName: String): Flow<List<ScreenshotEntity>>
}

data class CollectionInfoEntity(
    val appName: String,
    val count: Int,
    val latestUri: String?,
    val maxDate: Long?
)
