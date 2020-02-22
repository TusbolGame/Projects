package com.liveitandroid.liveit.model.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabaseLockedException;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

import com.liveitandroid.liveit.model.FavouriteDBModel;
import com.liveitandroid.liveit.model.FavouriteDBModel;
import java.util.ArrayList;

public class DatabaseHandler extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "liveit_fav.db";
    private static final int DATABASE_VERSION = 2;
    private static final String KEY_CATEGORY_ID = "categoryID";
    private static final String KEY_ID = "id";
    private static final String KEY_STREAM_ID = "streamID";
    private static final String KEY_TYPE = "type";
    private static final String TABLE_IPTV_FAVOURITES = "iptv_favourit_v1";
    String CREATE_PRODUCTS_TABLE = "CREATE TABLE IF NOT EXISTS iptv_favourit_v1(id INTEGER PRIMARY KEY,type TEXT,streamID TEXT,categoryID TEXT)";
    Context context;
    SQLiteDatabase db;

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL(this.CREATE_PRODUCTS_TABLE);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS iptv_favourit_v1");
        onCreate(db);
    }

    public void addToFavourite(FavouriteDBModel favourite, String type) {
        this.db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("type", type);
        values.put(KEY_STREAM_ID, Integer.valueOf(favourite.getStreamID()));
        values.put(KEY_CATEGORY_ID, favourite.getCategoryID());
        this.db.insert(TABLE_IPTV_FAVOURITES, null, values);
        this.db.close();
    }

    public void deleteFavourite(int streamID, String categoryID, String type) {
        this.db = getWritableDatabase();
        this.db.delete(TABLE_IPTV_FAVOURITES, "streamID='" + streamID + "' AND " + KEY_CATEGORY_ID + "='" + categoryID + "' AND " + "type" + "='" + type + "'", null);
        this.db.close();
    }

    public void deleteAndRecreateAllTables() {
        this.db = getWritableDatabase();
        onUpgrade(this.db, 0, 0);
        this.db.close();
    }

    public ArrayList<FavouriteDBModel> getAllFavourites(String type) {
        ArrayList<FavouriteDBModel> favouriteList = new ArrayList();
        String selectQuery = "SELECT  * FROM iptv_favourit_v1 WHERE type='" + type + "'";
        this.db = getReadableDatabase();
        Cursor cursor = this.db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                FavouriteDBModel favourite = new FavouriteDBModel();
                favourite.setId(Integer.parseInt(cursor.getString(0)));
                favourite.setType(cursor.getString(1));
                favourite.setStreamID(Integer.parseInt(cursor.getString(2)));
                favourite.setCategoryID(cursor.getString(3));
                favouriteList.add(favourite);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return favouriteList;
    }

    public int getFavouriteCount(String type) {
        try {
            Cursor cursor = getReadableDatabase().rawQuery("SELECT  COUNT(*) FROM iptv_favourit_v1 WHERE type='" + type + "'", null);
            cursor.moveToFirst();
            int count = cursor.getInt(0);
            cursor.close();
            return count;
        } catch (SQLiteDatabaseLockedException e) {
            return 0;
        } catch (SQLiteException e2) {
            return 0;
        }
    }

    public ArrayList<FavouriteDBModel> checkFavourite(int streamID, String categoryID, String type) {
        ArrayList<FavouriteDBModel> favouriteList = new ArrayList();
        String selectQuery = "SELECT  * FROM iptv_favourit_v1 WHERE streamID='" + streamID + "' AND " + KEY_CATEGORY_ID + "='" + categoryID + "' AND " + "type" + "='" + type + "'";
        this.db = getReadableDatabase();
        Cursor cursor = this.db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                FavouriteDBModel favourite = new FavouriteDBModel();
                favourite.setId(Integer.parseInt(cursor.getString(0)));
                favourite.setType(cursor.getString(1));
                favourite.setStreamID(Integer.parseInt(cursor.getString(2)));
                favourite.setCategoryID(cursor.getString(3));
                favouriteList.add(favourite);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return favouriteList;
    }
}
