package com.example.realclient;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

import java.util.ArrayList;
import java.util.List;

/**
 * <h1>Database Handler</h1>
 * <p>Class that acts as a helper to save an unique user ID locally to a sqlite database</p>
 * @author ErnoMitrovic <a>https://github.com/ErnoMitrovic</a>
 * @version 1.0
 * @since 21/11/2022
 * */
public class DBHandler extends SQLiteOpenHelper {
    private static final int DB_VERSION = 1;
    /**
     * Constructor for database handler
     * @param context the context from which it is being called
     * @param DB_NAME name of the database
     * @see SQLiteOpenHelper
     * */
    public DBHandler(Context context, String DB_NAME){
        super(context, DB_NAME, null, DB_VERSION);
    }

    /**
     * On create method callback
     * @param sqLiteDatabase database to execute a query.
     * */
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(Entry.CREATE_TABLE);
    }
    /**
     * Upgrade the database, just if the app is online.
     * */
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL(Entry.DELETE_TABLE);
        onCreate(sqLiteDatabase);
    }
    /**
     * Method to insert a user
     * @param user user to insert
     * */
    public void insertUser(String user) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Entry.COL_NAME, user);
        if(db.insert(Entry.TABLE_NAME, null, values) < 0)
            System.err.println("Error inserting data");
    }

    /**
     * Get the user if it exists in database.
     * @return the string representation of the user id
     * @throws IllegalArgumentException if column does not exist
     * @throws android.database.CursorIndexOutOfBoundsException if cursor is called with an unbounded index
     * */
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

    /**
     * Inner class to establish the table's schema
     * @author ErnoMitrovic <a>https://github.com/ErnoMitrovic</a>
     * @version 1.0
     * @since 21/11/2022
     * */
    public static class Entry{
        public static final String TABLE_NAME = "users";
        public static final String COL_NAME = "user_id";
        private static final String CREATE_TABLE = "CREATE TABLE " +
                Entry.TABLE_NAME + " (" + Entry.COL_NAME + " TEXT PRIMARY KEY)";
        private static final String DELETE_TABLE = "DROP TABLE IF EXISTS " +
                Entry.TABLE_NAME;

    }
}
