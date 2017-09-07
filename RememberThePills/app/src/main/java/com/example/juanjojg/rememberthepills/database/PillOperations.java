package com.example.juanjojg.rememberthepills.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.example.juanjojg.rememberthepills.models.Pill;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

public class PillOperations {
    public static final String LOGTAG = "PILL_MNGMNT_SYS";

    SQLiteOpenHelper dbhandler;
    SQLiteDatabase database;

    private static final String[] allColumns = {
            PillDatabaseHelper.COLUMN_ID,
            PillDatabaseHelper.COLUMN_NAME,
            PillDatabaseHelper.COLUMN_DESCRIPTION,
            PillDatabaseHelper.COLUMN_PHOTO
    };

    public PillOperations(Context context) {
        dbhandler = new PillDatabaseHelper(context);
    }

    public void open() {
        Log.i(LOGTAG, "Database Opened");
        database = dbhandler.getWritableDatabase();
    }

    public void close() {
        Log.i(LOGTAG, "Database Closed");
        dbhandler.close();
    }

    public Pill addPill(Pill pill) {
        ContentValues values = new ContentValues();

        // Convert the pill photo to a byte array
        Bitmap bitmap = pill.getPillPhoto();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, outputStream);
        byte[] pillPhotoArray = outputStream.toByteArray();

        values.put(PillDatabaseHelper.COLUMN_NAME, pill.getPillName());
        values.put(PillDatabaseHelper.COLUMN_DESCRIPTION, pill.getPillDescription());
        values.put(PillDatabaseHelper.COLUMN_PHOTO, pillPhotoArray);

        long insertID = database.insert(PillDatabaseHelper.TABLE_PILLS, null, values);
        pill.setPillID(insertID);

        return pill;
    }

    // Getting single Pill
    public Pill getPill(long id) {
        Cursor cursor = database.query(PillDatabaseHelper.TABLE_PILLS, allColumns, PillDatabaseHelper.COLUMN_ID + "=?", new String[]{String.valueOf(id)}, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        // When creating a new Pill object, it's necessary to convert the byte array to a Bitmap object
        Pill p = new Pill(Long.parseLong(cursor.getString(0)), cursor.getString(1), cursor.getString(2), BitmapFactory.decodeByteArray(cursor.getBlob(3), 0, cursor.getBlob(3).length));

        // Return Pill
        return p;
    }

    public ArrayList<Pill> getAllPills() {
        Cursor cursor = database.query(PillDatabaseHelper.TABLE_PILLS, allColumns, null, null, null, null, null);

        ArrayList<Pill> pillArray = new ArrayList<>();
        if (cursor != null && cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                Pill pill = new Pill();
                pill.setPillID(cursor.getLong(cursor.getColumnIndex(PillDatabaseHelper.COLUMN_ID)));
                pill.setPillName(cursor.getString(cursor.getColumnIndex(PillDatabaseHelper.COLUMN_NAME)));
                pill.setPillDescription(cursor.getString(cursor.getColumnIndex(PillDatabaseHelper.COLUMN_DESCRIPTION)));
                pill.setPillPhoto(BitmapFactory.decodeByteArray(cursor.getBlob(cursor.getColumnIndex(PillDatabaseHelper.COLUMN_PHOTO)), 0, cursor.getBlob(cursor.getColumnIndex(PillDatabaseHelper.COLUMN_PHOTO)).length));
                pillArray.add(pill);
            }
        }

        // Return all pills
        return pillArray;
    }

    // Updating Pill
    public int updatePill(Pill pill) {
        ContentValues values = new ContentValues();

        // Convert the pill photo to a byte array
        Bitmap bitmap = pill.getPillPhoto();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, outputStream);
        byte[] pillPhotoArray = outputStream.toByteArray();

        values.put(PillDatabaseHelper.COLUMN_NAME, pill.getPillName());
        values.put(PillDatabaseHelper.COLUMN_DESCRIPTION, pill.getPillDescription());
        values.put(PillDatabaseHelper.COLUMN_PHOTO, pillPhotoArray);

        // Updating row
        return database.update(PillDatabaseHelper.TABLE_PILLS, values,
                PillDatabaseHelper.COLUMN_ID + "=?", new String[]{String.valueOf(pill.getPillID())});
    }

    // Deleting Pill
    public void removePill(Pill pill) {
        database.delete(PillDatabaseHelper.TABLE_PILLS, PillDatabaseHelper.COLUMN_ID + "=" + pill.getPillID(), null);
    }
}
