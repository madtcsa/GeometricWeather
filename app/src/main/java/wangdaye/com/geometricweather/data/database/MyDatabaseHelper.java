package wangdaye.com.geometricweather.data.database;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Database helper.
 * */

public class MyDatabaseHelper extends SQLiteOpenHelper {
    // data
    public static final int VERSION_CODE = 6;

    public static final String DATABASE_NAME = "MyDatabase.db";

    public MyDatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public MyDatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version, DatabaseErrorHandler errorHandler) {
        super(context, name, factory, version, errorHandler);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(WeatherTable.CREATE_TABLE_WEATHER);
        db.execSQL(HistoryTable.CREATE_TABLE_HISTORY);
        db.execSQL(LocationTable.CREATE_TABLE_LOCATION);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists Info");
        db.execSQL("drop table if exists Weather");

        db.execSQL(WeatherTable.CREATE_TABLE_WEATHER);
        db.execSQL(HistoryTable.CREATE_TABLE_HISTORY);
    }

    public static MyDatabaseHelper getDatabaseHelper(Context context) {
        return new MyDatabaseHelper(context,
                MyDatabaseHelper.DATABASE_NAME,
                null,
                MyDatabaseHelper.VERSION_CODE);
    }
}
