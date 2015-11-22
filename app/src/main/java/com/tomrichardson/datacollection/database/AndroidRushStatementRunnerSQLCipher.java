package com.tomrichardson.datacollection.database;

import android.content.Context;
import android.provider.Settings;

import net.sqlcipher.Cursor;
import net.sqlcipher.database.SQLiteDatabase;
import net.sqlcipher.database.SQLiteException;
import net.sqlcipher.database.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import co.uk.rushorm.core.RushConfig;
import co.uk.rushorm.core.RushQue;
import co.uk.rushorm.core.RushStatementRunner;
import co.uk.rushorm.core.exceptions.RushSqlException;

/**
 * Created by tom on 18/11/2015.
 */public class AndroidRushStatementRunnerSQLCipher extends SQLiteOpenHelper implements RushStatementRunner {

  private int lastRunVersion = -1;
  private RushConfig rushConfig;
  private final Context context;

  public AndroidRushStatementRunnerSQLCipher(Context context, String name, RushConfig rushConfig) {
    super(context, name, null, rushConfig.dbVersion());
    lastRunVersion = rushConfig.dbVersion();
    this.rushConfig = rushConfig;
    this.context = context;

    SQLiteDatabase.loadLibs(context);
  }

  @Override
  public void runRaw(String statement, RushQue que) {
    try {
      getWritableDatabase(getEncryptionKey()).execSQL(statement);
    } catch (SQLiteException exception) {
      if(rushConfig.inDebug()) {
        throw exception;
      } else {
        throw new RushSqlException();
      }
    }
  }

  @Override
  public ValuesCallback runGet(String sql, RushQue que) {
    final Cursor cursor;
    try {
      cursor = getWritableDatabase(getEncryptionKey()).rawQuery(sql, null);
    } catch (SQLiteException exception) {
      if(rushConfig.inDebug()) {
        throw exception;
      } else {
        throw new RushSqlException();
      }
    }
    cursor.moveToFirst();
    return new ValuesCallback() {
      @Override
      public boolean hasNext() {
        return !cursor.isAfterLast();
      }
      @Override
      public List<String> next() {

        List<String> row = new ArrayList<>();
        for(int i = 0; i < cursor.getColumnCount(); i++){
          row.add(cursor.getString(i));
        }
        cursor.moveToNext();
        return row;
      }
      @Override
      public void close() {
        cursor.close();
      }
    };
  }

  @Override
  public void startTransition(RushQue que) {
    getWritableDatabase(getEncryptionKey()).beginTransaction();
  }

  @Override
  public void endTransition(RushQue que) {
    getWritableDatabase(getEncryptionKey()).setTransactionSuccessful();
    getWritableDatabase(getEncryptionKey()).endTransaction();
  }

  @Override
  public boolean isFirstRun() {
    String[] databases = context.databaseList();
    for (String database : databases) {
      if(database.equals(rushConfig.dbName())) {
        return false;
      }
    }
    return true;
  }

  @Override
  public void initializeComplete(long version) {

  }

  @Override
  public boolean requiresUpgrade(long version, RushQue que) {
    return getLastRunVersion() != version;
  }

  @Override
  public void onCreate(SQLiteDatabase db) {}

  @Override
  public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    lastRunVersion = oldVersion;
  }

  private int getLastRunVersion(){
    getReadableDatabase(getEncryptionKey()).getVersion();
    return lastRunVersion;
  }

  private String getEncryptionKey() {
    return Settings.Secure.getString(context.getContentResolver(),
        Settings.Secure.ANDROID_ID);
  }
}
