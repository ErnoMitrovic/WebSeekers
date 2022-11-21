package com.example.realclient;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

import java.util.ArrayList;
import java.util.List;

public class DBHandler extends SQLiteOpenHelper {
    private static final int DB_VERSION = 1;
    public DBHandler(Context context, String DB_NAME){
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(Entry.CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL(Entry.DELETE_TABLE);
        onCreate(sqLiteDatabase);
    }

    public void insertUser(String user) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Entry.COL_NAME, user);
        if(db.insert(Entry.TABLE_NAME, null, values) < 0)
            System.err.println("Error inserting data");
    }

    public String getUser(){
        String userID = "";
        SQLiteDatabase db = this.getReadableDatabase();
        String [] columns = {Entry.COL_NAME};
        Cursor cursor = db.query(Entry.TABLE_NAME,
                columns,
                null,
                null,
                null,
                null,
                null);
        if(cursor.moveToFirst()) {
            do {
                userID = cursor.getString(
                        cursor.getColumnIndexOrThrow(Entry.COL_NAME)
                );
            }while (cursor.moveToNext());
        }
        cursor.close();
        return userID;
    }

    public static class Entry implements BaseColumns{
        public static final String TABLE_NAME = "users";
        public static final String COL_NAME = "user_id";
        private static final String CREATE_TABLE = "CREATE TABLE " +
                Entry.TABLE_NAME + " (" + Entry.COL_NAME + " TEXT PRIMARY KEY)";
        private static final String DELETE_TABLE = "DROP TABLE IF EXISTS " +
                Entry.TABLE_NAME;

    }
}
