package com.snappy.samsung.database;

import androidx.annotation.NonNull;
import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.RoomDatabase;
import androidx.room.RoomOpenHelper;
import androidx.room.migration.AutoMigrationSpec;
import androidx.room.migration.Migration;
import androidx.room.util.DBUtil;
import androidx.room.util.TableInfo;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;
import java.lang.Class;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.processing.Generated;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class ScreenshotDatabase_Impl extends ScreenshotDatabase {
  private volatile ScreenshotDao _screenshotDao;

  @Override
  @NonNull
  protected SupportSQLiteOpenHelper createOpenHelper(@NonNull final DatabaseConfiguration config) {
    final SupportSQLiteOpenHelper.Callback _openCallback = new RoomOpenHelper(config, new RoomOpenHelper.Delegate(2) {
      @Override
      public void createAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS `screenshots` (`id` INTEGER NOT NULL, `imageUri` TEXT NOT NULL, `filename` TEXT NOT NULL, `appName` TEXT NOT NULL, `dateTaken` INTEGER NOT NULL, `fileSize` INTEGER NOT NULL, `favorite` INTEGER NOT NULL, PRIMARY KEY(`id`))");
        db.execSQL("CREATE TABLE IF NOT EXISTS `custom_collections` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `name` TEXT NOT NULL)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `screenshot_custom_collection` (`screenshotId` INTEGER NOT NULL, `collectionName` TEXT NOT NULL, PRIMARY KEY(`screenshotId`, `collectionName`))");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_screenshot_custom_collection_collectionName` ON `screenshot_custom_collection` (`collectionName`)");
        db.execSQL("CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)");
        db.execSQL("INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, 'e0e8496d7b38a780441adeb08cc9bff2')");
      }

      @Override
      public void dropAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS `screenshots`");
        db.execSQL("DROP TABLE IF EXISTS `custom_collections`");
        db.execSQL("DROP TABLE IF EXISTS `screenshot_custom_collection`");
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onDestructiveMigration(db);
          }
        }
      }

      @Override
      public void onCreate(@NonNull final SupportSQLiteDatabase db) {
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onCreate(db);
          }
        }
      }

      @Override
      public void onOpen(@NonNull final SupportSQLiteDatabase db) {
        mDatabase = db;
        internalInitInvalidationTracker(db);
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onOpen(db);
          }
        }
      }

      @Override
      public void onPreMigrate(@NonNull final SupportSQLiteDatabase db) {
        DBUtil.dropFtsSyncTriggers(db);
      }

      @Override
      public void onPostMigrate(@NonNull final SupportSQLiteDatabase db) {
      }

      @Override
      @NonNull
      public RoomOpenHelper.ValidationResult onValidateSchema(
          @NonNull final SupportSQLiteDatabase db) {
        final HashMap<String, TableInfo.Column> _columnsScreenshots = new HashMap<String, TableInfo.Column>(7);
        _columnsScreenshots.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsScreenshots.put("imageUri", new TableInfo.Column("imageUri", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsScreenshots.put("filename", new TableInfo.Column("filename", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsScreenshots.put("appName", new TableInfo.Column("appName", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsScreenshots.put("dateTaken", new TableInfo.Column("dateTaken", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsScreenshots.put("fileSize", new TableInfo.Column("fileSize", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsScreenshots.put("favorite", new TableInfo.Column("favorite", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysScreenshots = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesScreenshots = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoScreenshots = new TableInfo("screenshots", _columnsScreenshots, _foreignKeysScreenshots, _indicesScreenshots);
        final TableInfo _existingScreenshots = TableInfo.read(db, "screenshots");
        if (!_infoScreenshots.equals(_existingScreenshots)) {
          return new RoomOpenHelper.ValidationResult(false, "screenshots(com.snappy.samsung.database.ScreenshotEntity).\n"
                  + " Expected:\n" + _infoScreenshots + "\n"
                  + " Found:\n" + _existingScreenshots);
        }
        final HashMap<String, TableInfo.Column> _columnsCustomCollections = new HashMap<String, TableInfo.Column>(2);
        _columnsCustomCollections.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCustomCollections.put("name", new TableInfo.Column("name", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysCustomCollections = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesCustomCollections = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoCustomCollections = new TableInfo("custom_collections", _columnsCustomCollections, _foreignKeysCustomCollections, _indicesCustomCollections);
        final TableInfo _existingCustomCollections = TableInfo.read(db, "custom_collections");
        if (!_infoCustomCollections.equals(_existingCustomCollections)) {
          return new RoomOpenHelper.ValidationResult(false, "custom_collections(com.snappy.samsung.database.CustomCollectionEntity).\n"
                  + " Expected:\n" + _infoCustomCollections + "\n"
                  + " Found:\n" + _existingCustomCollections);
        }
        final HashMap<String, TableInfo.Column> _columnsScreenshotCustomCollection = new HashMap<String, TableInfo.Column>(2);
        _columnsScreenshotCustomCollection.put("screenshotId", new TableInfo.Column("screenshotId", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsScreenshotCustomCollection.put("collectionName", new TableInfo.Column("collectionName", "TEXT", true, 2, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysScreenshotCustomCollection = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesScreenshotCustomCollection = new HashSet<TableInfo.Index>(1);
        _indicesScreenshotCustomCollection.add(new TableInfo.Index("index_screenshot_custom_collection_collectionName", false, Arrays.asList("collectionName"), Arrays.asList("ASC")));
        final TableInfo _infoScreenshotCustomCollection = new TableInfo("screenshot_custom_collection", _columnsScreenshotCustomCollection, _foreignKeysScreenshotCustomCollection, _indicesScreenshotCustomCollection);
        final TableInfo _existingScreenshotCustomCollection = TableInfo.read(db, "screenshot_custom_collection");
        if (!_infoScreenshotCustomCollection.equals(_existingScreenshotCustomCollection)) {
          return new RoomOpenHelper.ValidationResult(false, "screenshot_custom_collection(com.snappy.samsung.database.ScreenshotCustomCollectionCrossRef).\n"
                  + " Expected:\n" + _infoScreenshotCustomCollection + "\n"
                  + " Found:\n" + _existingScreenshotCustomCollection);
        }
        return new RoomOpenHelper.ValidationResult(true, null);
      }
    }, "e0e8496d7b38a780441adeb08cc9bff2", "37461157d6ed2cfadc2ba11951d21627");
    final SupportSQLiteOpenHelper.Configuration _sqliteConfig = SupportSQLiteOpenHelper.Configuration.builder(config.context).name(config.name).callback(_openCallback).build();
    final SupportSQLiteOpenHelper _helper = config.sqliteOpenHelperFactory.create(_sqliteConfig);
    return _helper;
  }

  @Override
  @NonNull
  protected InvalidationTracker createInvalidationTracker() {
    final HashMap<String, String> _shadowTablesMap = new HashMap<String, String>(0);
    final HashMap<String, Set<String>> _viewTables = new HashMap<String, Set<String>>(0);
    return new InvalidationTracker(this, _shadowTablesMap, _viewTables, "screenshots","custom_collections","screenshot_custom_collection");
  }

  @Override
  public void clearAllTables() {
    super.assertNotMainThread();
    final SupportSQLiteDatabase _db = super.getOpenHelper().getWritableDatabase();
    try {
      super.beginTransaction();
      _db.execSQL("DELETE FROM `screenshots`");
      _db.execSQL("DELETE FROM `custom_collections`");
      _db.execSQL("DELETE FROM `screenshot_custom_collection`");
      super.setTransactionSuccessful();
    } finally {
      super.endTransaction();
      _db.query("PRAGMA wal_checkpoint(FULL)").close();
      if (!_db.inTransaction()) {
        _db.execSQL("VACUUM");
      }
    }
  }

  @Override
  @NonNull
  protected Map<Class<?>, List<Class<?>>> getRequiredTypeConverters() {
    final HashMap<Class<?>, List<Class<?>>> _typeConvertersMap = new HashMap<Class<?>, List<Class<?>>>();
    _typeConvertersMap.put(ScreenshotDao.class, ScreenshotDao_Impl.getRequiredConverters());
    return _typeConvertersMap;
  }

  @Override
  @NonNull
  public Set<Class<? extends AutoMigrationSpec>> getRequiredAutoMigrationSpecs() {
    final HashSet<Class<? extends AutoMigrationSpec>> _autoMigrationSpecsSet = new HashSet<Class<? extends AutoMigrationSpec>>();
    return _autoMigrationSpecsSet;
  }

  @Override
  @NonNull
  public List<Migration> getAutoMigrations(
      @NonNull final Map<Class<? extends AutoMigrationSpec>, AutoMigrationSpec> autoMigrationSpecs) {
    final List<Migration> _autoMigrations = new ArrayList<Migration>();
    return _autoMigrations;
  }

  @Override
  public ScreenshotDao screenshotDao() {
    if (_screenshotDao != null) {
      return _screenshotDao;
    } else {
      synchronized(this) {
        if(_screenshotDao == null) {
          _screenshotDao = new ScreenshotDao_Impl(this);
        }
        return _screenshotDao;
      }
    }
  }
}
