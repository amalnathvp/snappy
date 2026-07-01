package com.snappy.samsung.utils

import android.content.ContentUris
import android.content.Context
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import com.snappy.samsung.model.Screenshot

object MediaStoreScanner {
    private const val TAG = "MediaStoreScanner"

    fun scanScreenshots(context: Context): List<Screenshot> {
        val screenshots = mutableListOf<Screenshot>()
        
        val collection = MediaStore.Images.Media.EXTERNAL_CONTENT_URI

        val projection = arrayOf(
            MediaStore.Images.Media._ID,
            MediaStore.Images.Media.DISPLAY_NAME,
            MediaStore.Images.Media.DATE_TAKEN,
            MediaStore.Images.Media.DATE_ADDED,
            MediaStore.Images.Media.SIZE
        )

        // Samsung screenshots can be in DCIM/Screenshots or Pictures/Screenshots depending on OS version
        val selection = "${MediaStore.Images.Media.RELATIVE_PATH} LIKE ? OR ${MediaStore.Images.Media.RELATIVE_PATH} LIKE ?"
        val selectionArgs = arrayOf("DCIM/Screenshots%", "Pictures/Screenshots%")

        val sortOrder = "${MediaStore.Images.Media.DATE_TAKEN} DESC"

        try {
            context.contentResolver.query(
                collection,
                projection,
                selection,
                selectionArgs,
                sortOrder
            )?.use { cursor ->
                val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID)
                val displayNameColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME)
                val dateTakenColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATE_TAKEN)
                val dateAddedColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATE_ADDED)
                val sizeColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.SIZE)

                while (cursor.moveToNext()) {
                    val id = cursor.getLong(idColumn)
                    val filename = cursor.getString(displayNameColumn) ?: "Unknown"
                    
                    var dateTaken = cursor.getLong(dateTakenColumn)
                    if (dateTaken == 0L) {
                        val dateAdded = cursor.getLong(dateAddedColumn)
                        dateTaken = dateAdded * 1000
                    }
                    
                    val fileSize = cursor.getLong(sizeColumn)
                    val imageUri = ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id).toString()
                    
                    val appName = FilenameParser.parseAppName(filename)

                    screenshots.add(
                        Screenshot(
                            id = id,
                            imageUri = imageUri,
                            filename = filename,
                            appName = appName,
                            dateTaken = dateTaken,
                            fileSize = fileSize,
                            favorite = false
                        )
                    )
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error scanning MediaStore", e)
        }

        return screenshots
    }
}
