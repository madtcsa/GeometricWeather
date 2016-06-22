package wangdaye.com.geometricweather.data.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import wangdaye.com.geometricweather.R;
import wangdaye.com.geometricweather.data.model.Location;

/**
 * Location table.
 * */

public class LocationTable {
    // data
    public static final String TABLE_LOCATION = "Location";
    public static final String COLUMN_LOCATION = "location";

    public static final String CREATE_TABLE_LOCATION = "CREATE TABLE Location ("
            + "location     text    PRIMARY KEY)";


    public static List<Location> readLocationList(Context context, MyDatabaseHelper databaseHelper) {
        List<Location> locationList = new ArrayList<>();

        SQLiteDatabase database = databaseHelper.getReadableDatabase();
        Cursor cursor = database.query(TABLE_LOCATION, null, null, null, null, null, null);
        if(cursor.moveToFirst()) {
            do {
                String location = cursor.getString(cursor.getColumnIndex(COLUMN_LOCATION));
                locationList.add(new Location(location));
            } while (cursor.moveToNext());
        }
        cursor.close();
        database.close();
        if (locationList.size() == 0) {
            writeLocation(databaseHelper, context.getString(R.string.local));
            locationList.add(new Location(context.getString(R.string.local)));
        }
        return locationList;
    }

    public static void writeLocation(MyDatabaseHelper databaseHelper, String location) {
        SQLiteDatabase database = databaseHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_LOCATION, location);
        database.insert(TABLE_LOCATION, null, values);
        values.clear();
        database.close();
    }

    public static void deleteLocation(MyDatabaseHelper databaseHelper, String location) {
        SQLiteDatabase database = databaseHelper.getWritableDatabase();
        String[] deleteLocation = new String[] {location};
        database.delete(TABLE_LOCATION,
                COLUMN_LOCATION + " = ?", deleteLocation);
        database.close();
    }
}
