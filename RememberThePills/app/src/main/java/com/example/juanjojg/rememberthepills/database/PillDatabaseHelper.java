package com.example.juanjojg.rememberthepills.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Juanjo on 07/08/2017.
 */

public class PillDatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "pills.db";
    private static final int DATABASE_VERSION = 1;

    public static final String TABLE_PILLS = "pills";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_DESCRIPTION = "description";
    public static final String COLUMN_PHOTO = "photo";

    private static final String TABLE_CREATE =
            "CREATE TABLE " + TABLE_PILLS + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_NAME + " TEXT, " +
                    COLUMN_DESCRIPTION + " TEXT, " +
                    COLUMN_PHOTO + " BLOB" +
                    ")";


    public PillDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_PILLS);
        onCreate(sqLiteDatabase);
    }
}
