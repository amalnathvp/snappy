package com.snappy.samsung.database;

import android.database.Cursor;
import android.os.CancellationSignal;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.room.util.StringUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import java.lang.Class;
import java.lang.Exception;
import java.lang.Integer;
import java.lang.Long;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.StringBuilder;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import javax.annotation.processing.Generated;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlinx.coroutines.flow.Flow;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class ScreenshotDao_Impl implements ScreenshotDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<ScreenshotEntity> __insertionAdapterOfScreenshotEntity;

  private final EntityInsertionAdapter<CustomCollectionEntity> __insertionAdapterOfCustomCollectionEntity;

  private final EntityInsertionAdapter<ScreenshotCustomCollectionCrossRef> __insertionAdapterOfScreenshotCustomCollectionCrossRef;

  private final SharedSQLiteStatement __preparedStmtOfUpdateFavorite;

  private final SharedSQLiteStatement __preparedStmtOfDeleteScreenshot;

  private final SharedSQLiteStatement __preparedStmtOfClearAll;

  private final SharedSQLiteStatement __preparedStmtOfDeleteCustomCollection;

  private final SharedSQLiteStatement __preparedStmtOfRemoveScreenshotFromCollection;

  public ScreenshotDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfScreenshotEntity = new EntityInsertionAdapter<ScreenshotEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR IGNORE INTO `screenshots` (`id`,`imageUri`,`filename`,`appName`,`dateTaken`,`fileSize`,`favorite`) VALUES (?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final ScreenshotEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindString(2, entity.getImageUri());
        statement.bindString(3, entity.getFilename());
        statement.bindString(4, entity.getAppName());
        statement.bindLong(5, entity.getDateTaken());
        statement.bindLong(6, entity.getFileSize());
        final int _tmp = entity.getFavorite() ? 1 : 0;
        statement.bindLong(7, _tmp);
      }
    };
    this.__insertionAdapterOfCustomCollectionEntity = new EntityInsertionAdapter<CustomCollectionEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR IGNORE INTO `custom_collections` (`id`,`name`) VALUES (nullif(?, 0),?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final CustomCollectionEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindString(2, entity.getName());
      }
    };
    this.__insertionAdapterOfScreenshotCustomCollectionCrossRef = new EntityInsertionAdapter<ScreenshotCustomCollectionCrossRef>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `screenshot_custom_collection` (`screenshotId`,`collectionName`) VALUES (?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final ScreenshotCustomCollectionCrossRef entity) {
        statement.bindLong(1, entity.getScreenshotId());
        statement.bindString(2, entity.getCollectionName());
      }
    };
    this.__preparedStmtOfUpdateFavorite = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE screenshots SET favorite = ? WHERE id = ?";
        return _query;
      }
    };
    this.__preparedStmtOfDeleteScreenshot = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM screenshots WHERE id = ?";
        return _query;
      }
    };
    this.__preparedStmtOfClearAll = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM screenshots";
        return _query;
      }
    };
    this.__preparedStmtOfDeleteCustomCollection = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM custom_collections WHERE name = ?";
        return _query;
      }
    };
    this.__preparedStmtOfRemoveScreenshotFromCollection = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM screenshot_custom_collection WHERE screenshotId = ? AND collectionName = ?";
        return _query;
      }
    };
  }

  @Override
  public Object insertScreenshots(final List<ScreenshotEntity> screenshots,
      final Continuation<? super List<Long>> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<List<Long>>() {
      @Override
      @NonNull
      public List<Long> call() throws Exception {
        __db.beginTransaction();
        try {
          final List<Long> _result = __insertionAdapterOfScreenshotEntity.insertAndReturnIdsList(screenshots);
          __db.setTransactionSuccessful();
          return _result;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object insertCustomCollection(final CustomCollectionEntity collection,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfCustomCollectionEntity.insert(collection);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object insertScreenshotToCollection(final ScreenshotCustomCollectionCrossRef ref,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfScreenshotCustomCollectionCrossRef.insert(ref);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object updateFavorite(final long id, final boolean favorite,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfUpdateFavorite.acquire();
        int _argIndex = 1;
        final int _tmp = favorite ? 1 : 0;
        _stmt.bindLong(_argIndex, _tmp);
        _argIndex = 2;
        _stmt.bindLong(_argIndex, id);
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return Unit.INSTANCE;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfUpdateFavorite.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteScreenshot(final long id, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteScreenshot.acquire();
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, id);
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return Unit.INSTANCE;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfDeleteScreenshot.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object clearAll(final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfClearAll.acquire();
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return Unit.INSTANCE;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfClearAll.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteCustomCollection(final String name,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteCustomCollection.acquire();
        int _argIndex = 1;
        _stmt.bindString(_argIndex, name);
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return Unit.INSTANCE;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfDeleteCustomCollection.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object removeScreenshotFromCollection(final long screenshotId, final String collectionName,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfRemoveScreenshotFromCollection.acquire();
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, screenshotId);
        _argIndex = 2;
        _stmt.bindString(_argIndex, collectionName);
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return Unit.INSTANCE;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfRemoveScreenshotFromCollection.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<ScreenshotEntity>> getAllScreenshots() {
    final String _sql = "SELECT * FROM screenshots ORDER BY dateTaken DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"screenshots"}, new Callable<List<ScreenshotEntity>>() {
      @Override
      @NonNull
      public List<ScreenshotEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfImageUri = CursorUtil.getColumnIndexOrThrow(_cursor, "imageUri");
          final int _cursorIndexOfFilename = CursorUtil.getColumnIndexOrThrow(_cursor, "filename");
          final int _cursorIndexOfAppName = CursorUtil.getColumnIndexOrThrow(_cursor, "appName");
          final int _cursorIndexOfDateTaken = CursorUtil.getColumnIndexOrThrow(_cursor, "dateTaken");
          final int _cursorIndexOfFileSize = CursorUtil.getColumnIndexOrThrow(_cursor, "fileSize");
          final int _cursorIndexOfFavorite = CursorUtil.getColumnIndexOrThrow(_cursor, "favorite");
          final List<ScreenshotEntity> _result = new ArrayList<ScreenshotEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final ScreenshotEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpImageUri;
            _tmpImageUri = _cursor.getString(_cursorIndexOfImageUri);
            final String _tmpFilename;
            _tmpFilename = _cursor.getString(_cursorIndexOfFilename);
            final String _tmpAppName;
            _tmpAppName = _cursor.getString(_cursorIndexOfAppName);
            final long _tmpDateTaken;
            _tmpDateTaken = _cursor.getLong(_cursorIndexOfDateTaken);
            final long _tmpFileSize;
            _tmpFileSize = _cursor.getLong(_cursorIndexOfFileSize);
            final boolean _tmpFavorite;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfFavorite);
            _tmpFavorite = _tmp != 0;
            _item = new ScreenshotEntity(_tmpId,_tmpImageUri,_tmpFilename,_tmpAppName,_tmpDateTaken,_tmpFileSize,_tmpFavorite);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Flow<List<ScreenshotEntity>> getScreenshotsByApp(final String appName) {
    final String _sql = "SELECT * FROM screenshots WHERE appName = ? ORDER BY dateTaken DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindString(_argIndex, appName);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"screenshots"}, new Callable<List<ScreenshotEntity>>() {
      @Override
      @NonNull
      public List<ScreenshotEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfImageUri = CursorUtil.getColumnIndexOrThrow(_cursor, "imageUri");
          final int _cursorIndexOfFilename = CursorUtil.getColumnIndexOrThrow(_cursor, "filename");
          final int _cursorIndexOfAppName = CursorUtil.getColumnIndexOrThrow(_cursor, "appName");
          final int _cursorIndexOfDateTaken = CursorUtil.getColumnIndexOrThrow(_cursor, "dateTaken");
          final int _cursorIndexOfFileSize = CursorUtil.getColumnIndexOrThrow(_cursor, "fileSize");
          final int _cursorIndexOfFavorite = CursorUtil.getColumnIndexOrThrow(_cursor, "favorite");
          final List<ScreenshotEntity> _result = new ArrayList<ScreenshotEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final ScreenshotEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpImageUri;
            _tmpImageUri = _cursor.getString(_cursorIndexOfImageUri);
            final String _tmpFilename;
            _tmpFilename = _cursor.getString(_cursorIndexOfFilename);
            final String _tmpAppName;
            _tmpAppName = _cursor.getString(_cursorIndexOfAppName);
            final long _tmpDateTaken;
            _tmpDateTaken = _cursor.getLong(_cursorIndexOfDateTaken);
            final long _tmpFileSize;
            _tmpFileSize = _cursor.getLong(_cursorIndexOfFileSize);
            final boolean _tmpFavorite;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfFavorite);
            _tmpFavorite = _tmp != 0;
            _item = new ScreenshotEntity(_tmpId,_tmpImageUri,_tmpFilename,_tmpAppName,_tmpDateTaken,_tmpFileSize,_tmpFavorite);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Flow<List<ScreenshotEntity>> getFavoriteScreenshots() {
    final String _sql = "SELECT * FROM screenshots WHERE favorite = 1 ORDER BY dateTaken DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"screenshots"}, new Callable<List<ScreenshotEntity>>() {
      @Override
      @NonNull
      public List<ScreenshotEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfImageUri = CursorUtil.getColumnIndexOrThrow(_cursor, "imageUri");
          final int _cursorIndexOfFilename = CursorUtil.getColumnIndexOrThrow(_cursor, "filename");
          final int _cursorIndexOfAppName = CursorUtil.getColumnIndexOrThrow(_cursor, "appName");
          final int _cursorIndexOfDateTaken = CursorUtil.getColumnIndexOrThrow(_cursor, "dateTaken");
          final int _cursorIndexOfFileSize = CursorUtil.getColumnIndexOrThrow(_cursor, "fileSize");
          final int _cursorIndexOfFavorite = CursorUtil.getColumnIndexOrThrow(_cursor, "favorite");
          final List<ScreenshotEntity> _result = new ArrayList<ScreenshotEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final ScreenshotEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpImageUri;
            _tmpImageUri = _cursor.getString(_cursorIndexOfImageUri);
            final String _tmpFilename;
            _tmpFilename = _cursor.getString(_cursorIndexOfFilename);
            final String _tmpAppName;
            _tmpAppName = _cursor.getString(_cursorIndexOfAppName);
            final long _tmpDateTaken;
            _tmpDateTaken = _cursor.getLong(_cursorIndexOfDateTaken);
            final long _tmpFileSize;
            _tmpFileSize = _cursor.getLong(_cursorIndexOfFileSize);
            final boolean _tmpFavorite;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfFavorite);
            _tmpFavorite = _tmp != 0;
            _item = new ScreenshotEntity(_tmpId,_tmpImageUri,_tmpFilename,_tmpAppName,_tmpDateTaken,_tmpFileSize,_tmpFavorite);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Flow<List<ScreenshotEntity>> searchScreenshots(final String query) {
    final String _sql = "SELECT * FROM screenshots WHERE filename LIKE '%' || ? || '%' OR appName LIKE '%' || ? || '%' ORDER BY dateTaken DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 2);
    int _argIndex = 1;
    _statement.bindString(_argIndex, query);
    _argIndex = 2;
    _statement.bindString(_argIndex, query);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"screenshots"}, new Callable<List<ScreenshotEntity>>() {
      @Override
      @NonNull
      public List<ScreenshotEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfImageUri = CursorUtil.getColumnIndexOrThrow(_cursor, "imageUri");
          final int _cursorIndexOfFilename = CursorUtil.getColumnIndexOrThrow(_cursor, "filename");
          final int _cursorIndexOfAppName = CursorUtil.getColumnIndexOrThrow(_cursor, "appName");
          final int _cursorIndexOfDateTaken = CursorUtil.getColumnIndexOrThrow(_cursor, "dateTaken");
          final int _cursorIndexOfFileSize = CursorUtil.getColumnIndexOrThrow(_cursor, "fileSize");
          final int _cursorIndexOfFavorite = CursorUtil.getColumnIndexOrThrow(_cursor, "favorite");
          final List<ScreenshotEntity> _result = new ArrayList<ScreenshotEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final ScreenshotEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpImageUri;
            _tmpImageUri = _cursor.getString(_cursorIndexOfImageUri);
            final String _tmpFilename;
            _tmpFilename = _cursor.getString(_cursorIndexOfFilename);
            final String _tmpAppName;
            _tmpAppName = _cursor.getString(_cursorIndexOfAppName);
            final long _tmpDateTaken;
            _tmpDateTaken = _cursor.getLong(_cursorIndexOfDateTaken);
            final long _tmpFileSize;
            _tmpFileSize = _cursor.getLong(_cursorIndexOfFileSize);
            final boolean _tmpFavorite;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfFavorite);
            _tmpFavorite = _tmp != 0;
            _item = new ScreenshotEntity(_tmpId,_tmpImageUri,_tmpFilename,_tmpAppName,_tmpDateTaken,_tmpFileSize,_tmpFavorite);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Object getScreenshotById(final long id,
      final Continuation<? super ScreenshotEntity> $completion) {
    final String _sql = "SELECT * FROM screenshots WHERE id = ? LIMIT 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, id);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<ScreenshotEntity>() {
      @Override
      @Nullable
      public ScreenshotEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfImageUri = CursorUtil.getColumnIndexOrThrow(_cursor, "imageUri");
          final int _cursorIndexOfFilename = CursorUtil.getColumnIndexOrThrow(_cursor, "filename");
          final int _cursorIndexOfAppName = CursorUtil.getColumnIndexOrThrow(_cursor, "appName");
          final int _cursorIndexOfDateTaken = CursorUtil.getColumnIndexOrThrow(_cursor, "dateTaken");
          final int _cursorIndexOfFileSize = CursorUtil.getColumnIndexOrThrow(_cursor, "fileSize");
          final int _cursorIndexOfFavorite = CursorUtil.getColumnIndexOrThrow(_cursor, "favorite");
          final ScreenshotEntity _result;
          if (_cursor.moveToFirst()) {
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpImageUri;
            _tmpImageUri = _cursor.getString(_cursorIndexOfImageUri);
            final String _tmpFilename;
            _tmpFilename = _cursor.getString(_cursorIndexOfFilename);
            final String _tmpAppName;
            _tmpAppName = _cursor.getString(_cursorIndexOfAppName);
            final long _tmpDateTaken;
            _tmpDateTaken = _cursor.getLong(_cursorIndexOfDateTaken);
            final long _tmpFileSize;
            _tmpFileSize = _cursor.getLong(_cursorIndexOfFileSize);
            final boolean _tmpFavorite;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfFavorite);
            _tmpFavorite = _tmp != 0;
            _result = new ScreenshotEntity(_tmpId,_tmpImageUri,_tmpFilename,_tmpAppName,_tmpDateTaken,_tmpFileSize,_tmpFavorite);
          } else {
            _result = null;
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Flow<ScreenshotEntity> getScreenshotByIdFlow(final long id) {
    final String _sql = "SELECT * FROM screenshots WHERE id = ? LIMIT 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, id);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"screenshots"}, new Callable<ScreenshotEntity>() {
      @Override
      @Nullable
      public ScreenshotEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfImageUri = CursorUtil.getColumnIndexOrThrow(_cursor, "imageUri");
          final int _cursorIndexOfFilename = CursorUtil.getColumnIndexOrThrow(_cursor, "filename");
          final int _cursorIndexOfAppName = CursorUtil.getColumnIndexOrThrow(_cursor, "appName");
          final int _cursorIndexOfDateTaken = CursorUtil.getColumnIndexOrThrow(_cursor, "dateTaken");
          final int _cursorIndexOfFileSize = CursorUtil.getColumnIndexOrThrow(_cursor, "fileSize");
          final int _cursorIndexOfFavorite = CursorUtil.getColumnIndexOrThrow(_cursor, "favorite");
          final ScreenshotEntity _result;
          if (_cursor.moveToFirst()) {
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpImageUri;
            _tmpImageUri = _cursor.getString(_cursorIndexOfImageUri);
            final String _tmpFilename;
            _tmpFilename = _cursor.getString(_cursorIndexOfFilename);
            final String _tmpAppName;
            _tmpAppName = _cursor.getString(_cursorIndexOfAppName);
            final long _tmpDateTaken;
            _tmpDateTaken = _cursor.getLong(_cursorIndexOfDateTaken);
            final long _tmpFileSize;
            _tmpFileSize = _cursor.getLong(_cursorIndexOfFileSize);
            final boolean _tmpFavorite;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfFavorite);
            _tmpFavorite = _tmp != 0;
            _result = new ScreenshotEntity(_tmpId,_tmpImageUri,_tmpFilename,_tmpAppName,_tmpDateTaken,_tmpFileSize,_tmpFavorite);
          } else {
            _result = null;
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Flow<List<CollectionInfoEntity>> getCollectionsFlow() {
    final String _sql = "\n"
            + "        SELECT appName, COUNT(*) as count, \n"
            + "        (SELECT imageUri FROM screenshots s2 WHERE s2.appName = s1.appName ORDER BY s2.dateTaken DESC LIMIT 1) as latestUri\n"
            + "        FROM screenshots s1 \n"
            + "        GROUP BY appName \n"
            + "        ORDER BY count DESC, appName ASC\n"
            + "    ";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"screenshots"}, new Callable<List<CollectionInfoEntity>>() {
      @Override
      @NonNull
      public List<CollectionInfoEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfAppName = 0;
          final int _cursorIndexOfCount = 1;
          final int _cursorIndexOfLatestUri = 2;
          final List<CollectionInfoEntity> _result = new ArrayList<CollectionInfoEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final CollectionInfoEntity _item;
            final String _tmpAppName;
            _tmpAppName = _cursor.getString(_cursorIndexOfAppName);
            final int _tmpCount;
            _tmpCount = _cursor.getInt(_cursorIndexOfCount);
            final String _tmpLatestUri;
            if (_cursor.isNull(_cursorIndexOfLatestUri)) {
              _tmpLatestUri = null;
            } else {
              _tmpLatestUri = _cursor.getString(_cursorIndexOfLatestUri);
            }
            _item = new CollectionInfoEntity(_tmpAppName,_tmpCount,_tmpLatestUri);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Flow<Integer> getTotalCountFlow() {
    final String _sql = "SELECT COUNT(*) FROM screenshots";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"screenshots"}, new Callable<Integer>() {
      @Override
      @NonNull
      public Integer call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final Integer _result;
          if (_cursor.moveToFirst()) {
            final int _tmp;
            _tmp = _cursor.getInt(0);
            _result = _tmp;
          } else {
            _result = 0;
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Flow<Integer> getFavoritesCountFlow() {
    final String _sql = "SELECT COUNT(*) FROM screenshots WHERE favorite = 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"screenshots"}, new Callable<Integer>() {
      @Override
      @NonNull
      public Integer call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final Integer _result;
          if (_cursor.moveToFirst()) {
            final int _tmp;
            _tmp = _cursor.getInt(0);
            _result = _tmp;
          } else {
            _result = 0;
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Flow<Long> getTotalSizeFlow() {
    final String _sql = "SELECT SUM(fileSize) FROM screenshots";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"screenshots"}, new Callable<Long>() {
      @Override
      @Nullable
      public Long call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final Long _result;
          if (_cursor.moveToFirst()) {
            final Long _tmp;
            if (_cursor.isNull(0)) {
              _tmp = null;
            } else {
              _tmp = _cursor.getLong(0);
            }
            _result = _tmp;
          } else {
            _result = null;
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Flow<String> getLatestFavoriteUriFlow() {
    final String _sql = "SELECT imageUri FROM screenshots WHERE favorite = 1 ORDER BY dateTaken DESC LIMIT 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"screenshots"}, new Callable<String>() {
      @Override
      @Nullable
      public String call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final String _result;
          if (_cursor.moveToFirst()) {
            if (_cursor.isNull(0)) {
              _result = null;
            } else {
              _result = _cursor.getString(0);
            }
          } else {
            _result = null;
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Object getAllIds(final Continuation<? super List<Long>> $completion) {
    final String _sql = "SELECT id FROM screenshots";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<Long>>() {
      @Override
      @NonNull
      public List<Long> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final List<Long> _result = new ArrayList<Long>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final Long _item;
            _item = _cursor.getLong(0);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Object getCustomCollectionNames(final Continuation<? super List<String>> $completion) {
    final String _sql = "SELECT name FROM custom_collections ORDER BY name ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<String>>() {
      @Override
      @NonNull
      public List<String> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final List<String> _result = new ArrayList<String>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final String _item;
            _item = _cursor.getString(0);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Object getCollectionsForScreenshot(final long screenshotId,
      final Continuation<? super List<String>> $completion) {
    final String _sql = "SELECT collectionName FROM screenshot_custom_collection WHERE screenshotId = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, screenshotId);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<String>>() {
      @Override
      @NonNull
      public List<String> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final List<String> _result = new ArrayList<String>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final String _item;
            _item = _cursor.getString(0);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<String>> getCollectionsForScreenshotFlow(final long screenshotId) {
    final String _sql = "SELECT collectionName FROM screenshot_custom_collection WHERE screenshotId = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, screenshotId);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"screenshot_custom_collection"}, new Callable<List<String>>() {
      @Override
      @NonNull
      public List<String> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final List<String> _result = new ArrayList<String>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final String _item;
            _item = _cursor.getString(0);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Flow<List<CollectionInfoEntity>> getCustomCollectionsFlow() {
    final String _sql = "\n"
            + "        SELECT collectionName as appName, COUNT(*) as count,\n"
            + "        (SELECT imageUri FROM screenshots s2 INNER JOIN screenshot_custom_collection j2 ON s2.id = j2.screenshotId WHERE j2.collectionName = j.collectionName ORDER BY s2.dateTaken DESC LIMIT 1) as latestUri\n"
            + "        FROM screenshot_custom_collection j\n"
            + "        GROUP BY collectionName\n"
            + "        ORDER BY count DESC, collectionName ASC\n"
            + "    ";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"screenshots",
        "screenshot_custom_collection"}, new Callable<List<CollectionInfoEntity>>() {
      @Override
      @NonNull
      public List<CollectionInfoEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfAppName = 0;
          final int _cursorIndexOfCount = 1;
          final int _cursorIndexOfLatestUri = 2;
          final List<CollectionInfoEntity> _result = new ArrayList<CollectionInfoEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final CollectionInfoEntity _item;
            final String _tmpAppName;
            _tmpAppName = _cursor.getString(_cursorIndexOfAppName);
            final int _tmpCount;
            _tmpCount = _cursor.getInt(_cursorIndexOfCount);
            final String _tmpLatestUri;
            if (_cursor.isNull(_cursorIndexOfLatestUri)) {
              _tmpLatestUri = null;
            } else {
              _tmpLatestUri = _cursor.getString(_cursorIndexOfLatestUri);
            }
            _item = new CollectionInfoEntity(_tmpAppName,_tmpCount,_tmpLatestUri);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Flow<List<ScreenshotEntity>> getScreenshotsByCustomCollection(
      final String collectionName) {
    final String _sql = "\n"
            + "        SELECT s.* FROM screenshots s\n"
            + "        INNER JOIN screenshot_custom_collection j ON s.id = j.screenshotId\n"
            + "        WHERE j.collectionName = ?\n"
            + "        ORDER BY s.dateTaken DESC\n"
            + "    ";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindString(_argIndex, collectionName);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"screenshots",
        "screenshot_custom_collection"}, new Callable<List<ScreenshotEntity>>() {
      @Override
      @NonNull
      public List<ScreenshotEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfImageUri = CursorUtil.getColumnIndexOrThrow(_cursor, "imageUri");
          final int _cursorIndexOfFilename = CursorUtil.getColumnIndexOrThrow(_cursor, "filename");
          final int _cursorIndexOfAppName = CursorUtil.getColumnIndexOrThrow(_cursor, "appName");
          final int _cursorIndexOfDateTaken = CursorUtil.getColumnIndexOrThrow(_cursor, "dateTaken");
          final int _cursorIndexOfFileSize = CursorUtil.getColumnIndexOrThrow(_cursor, "fileSize");
          final int _cursorIndexOfFavorite = CursorUtil.getColumnIndexOrThrow(_cursor, "favorite");
          final List<ScreenshotEntity> _result = new ArrayList<ScreenshotEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final ScreenshotEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpImageUri;
            _tmpImageUri = _cursor.getString(_cursorIndexOfImageUri);
            final String _tmpFilename;
            _tmpFilename = _cursor.getString(_cursorIndexOfFilename);
            final String _tmpAppName;
            _tmpAppName = _cursor.getString(_cursorIndexOfAppName);
            final long _tmpDateTaken;
            _tmpDateTaken = _cursor.getLong(_cursorIndexOfDateTaken);
            final long _tmpFileSize;
            _tmpFileSize = _cursor.getLong(_cursorIndexOfFileSize);
            final boolean _tmpFavorite;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfFavorite);
            _tmpFavorite = _tmp != 0;
            _item = new ScreenshotEntity(_tmpId,_tmpImageUri,_tmpFilename,_tmpAppName,_tmpDateTaken,_tmpFileSize,_tmpFavorite);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Object deleteScreenshots(final List<Long> ids,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final StringBuilder _stringBuilder = StringUtil.newStringBuilder();
        _stringBuilder.append("DELETE FROM screenshots WHERE id in (");
        final int _inputSize = ids.size();
        StringUtil.appendPlaceholders(_stringBuilder, _inputSize);
        _stringBuilder.append(")");
        final String _sql = _stringBuilder.toString();
        final SupportSQLiteStatement _stmt = __db.compileStatement(_sql);
        int _argIndex = 1;
        for (long _item : ids) {
          _stmt.bindLong(_argIndex, _item);
          _argIndex++;
        }
        __db.beginTransaction();
        try {
          _stmt.executeUpdateDelete();
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
