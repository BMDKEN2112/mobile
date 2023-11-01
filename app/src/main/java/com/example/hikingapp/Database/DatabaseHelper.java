package com.example.hikingapp.Database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import com.example.hikingapp.Model.HikeModel;
import android.graphics.BitmapFactory;

public class DatabaseHelper extends SQLiteOpenHelper {
    private Context context;
    public static final String DATABASE_NAME = "HikeManagement.db";
    public static final int DATABASE_VERSION = 1;
    public static final String TABLE_HIKE = "Hikes";

    public static final String COLUMN_HIKE_ID = "Hike_ID";
    public static final String COLUMN_HIKE_NAME  = "Hike_Name";
    public static final String COLUMN_HIKE_LOCATION  = "Hike_Location";
    public static final String COLUMN_HIKE_DATE  = "Hike_Date";
    public static final String COLUMN_HIKE_PARKING = "Hike_Parking";
    public static final String COLUMN_HIKE_LENGTH = "Hike_Length";
    public static final String COLUMN_HIKE_DIFFICULTY = "Hike_Difficulty";
    public static final String COLUMN_HIKE_DESCRIPTION = "Hike_Description";
    public static final String COLUMN_HIKE_IMAGE = "Hike_Image";

    private ByteArrayOutputStream byteArrayOutputStream;

    private byte[] imageInByte;


    public DatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    private String Hike = "CREATE TABLE " + TABLE_HIKE + " (" +
            COLUMN_HIKE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COLUMN_HIKE_NAME + " nvarchar," +
            COLUMN_HIKE_LOCATION + " nvarchar, " +
            COLUMN_HIKE_DATE + " Long, " +
            COLUMN_HIKE_PARKING + " nvarchar, " +
            COLUMN_HIKE_LENGTH + " nvarchar, " +
            COLUMN_HIKE_DIFFICULTY + " nvarchar, " +
            COLUMN_HIKE_DESCRIPTION + " nvarchar, " +
            COLUMN_HIKE_IMAGE + " BLOB );";

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        db.execSQL(Hike);
        getAllHikes();
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public void addHike(String name, String location, String date, boolean Parking, String length, String level, String description, Bitmap Hike_Image) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        Bitmap imageToStorageBitmap = Hike_Image;

        byteArrayOutputStream = new ByteArrayOutputStream();
        imageToStorageBitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        imageInByte = byteArrayOutputStream.toByteArray();

        contentValues.put(COLUMN_HIKE_NAME, name);
        contentValues.put(COLUMN_HIKE_LOCATION, location);
        contentValues.put(COLUMN_HIKE_DATE, date);
        contentValues.put(COLUMN_HIKE_PARKING, Parking ? "Yes" : "No");
        contentValues.put(COLUMN_HIKE_LENGTH, length);
        contentValues.put(COLUMN_HIKE_DIFFICULTY, level);
        contentValues.put(COLUMN_HIKE_DESCRIPTION, description);
        contentValues.put("Hike_Image", imageInByte);

        long result = db.insert(TABLE_HIKE, null, contentValues);
        if(result == -1) {
            int newlyAssignedHikeID = (int) result;
            Toast.makeText(context, "Failed", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(context, "Success", Toast.LENGTH_LONG).show();
        }
    }

    public List<HikeModel> getAllHikes() {
        List<HikeModel> hikeList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_HIKE, null);

        if (cursor.moveToFirst()) {
            do {
                HikeModel hike = new HikeModel();
                int hikeNameColumnIndex = cursor.getColumnIndex("Hike_Name");
                hike.setName(cursor.getString(hikeNameColumnIndex));
                int hikeLocationColumnIndex = cursor.getColumnIndex("Hike_Location");
                hike.setLocation(cursor.getString(hikeLocationColumnIndex));
                int hikeDifficultyColumnIndex = cursor.getColumnIndex("Hike_Difficulty");
                hike.setDifficulty(cursor.getString(hikeDifficultyColumnIndex));
                int hikeLengthColumnIndex = cursor.getColumnIndex("Hike_Length");
                hike.setLength(cursor.getString(hikeLengthColumnIndex));

                int hikeParkingColumnIndex = cursor.getColumnIndex("Hike_Parking");
                String parkingValue = cursor.getString(hikeParkingColumnIndex);
                boolean isParking = "Yes".equalsIgnoreCase(parkingValue);
                hike.setParking(isParking);

                int hikeDateColumnIndex = cursor.getColumnIndex("Hike_Date");
                hike.setDate(cursor.getString(hikeDateColumnIndex));

                int hikeDescriptionColumnIndex = cursor.getColumnIndex("Hike_Description");
                hike.setDescription(cursor.getString(hikeDescriptionColumnIndex));

                int hikeImageColumnIndex = cursor.getColumnIndex("Hike_Image");
                byte[] imageBytes = cursor.getBlob(hikeImageColumnIndex);

                // Convert the byte array to a Bitmap
                Bitmap image = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
                hike.setHikeImage(image);

                hikeList.add(hike);
                Log.d("DatabaseHelper", "Retrieved Hike Name: " + hike.getName());

            } while (cursor.moveToNext());
        }

        cursor.close();
        return hikeList;
    }

    public HikeModel getHikeById(int hikeId) {
        SQLiteDatabase db = this.getReadableDatabase();
        HikeModel hike = null;

        Cursor cursor = db.query(
                TABLE_HIKE,  // Table name
                null,         // All columns
                "Hike_ID" + "=?",  // WHERE clause
                new String[]{String.valueOf(hikeId)},  // Arguments for WHERE clause
                null,
                null,
                null
        );

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                hike = new HikeModel();
//                int hikeIdColumnIndex = cursor.getColumnIndex("Hike_ID");
//                hike.setId(cursor.getInt(hikeIdColumnIndex));

                int hikeNameColumnIndex = cursor.getColumnIndex("Hike_Name");
                hike.setName(cursor.getString(hikeNameColumnIndex));

                // Add log statements here to check the values
//                Log.d("DatabaseHelper", "Retrieved Hike ID: " + hike.getId());
//                Log.d("DatabaseHelper", "Hike Name: " + hike.getName());

                int hikeDateColumnIndex = cursor.getColumnIndex("Hike_Date");
                hike.setDate(cursor.getString(hikeDateColumnIndex));

                int hikeDescriptionColumnIndex = cursor.getColumnIndex("Hike_Description");
                hike.setDescription(cursor.getString(hikeDescriptionColumnIndex));

                int hikeLengthColumnIndex = cursor.getColumnIndex("Hike_Length");
                hike.setLength(cursor.getString(hikeLengthColumnIndex));

                int hikeParkingColumnIndex = cursor.getColumnIndex("Hike_Parking");
                int parkingValue = cursor.getInt(hikeParkingColumnIndex);
                boolean isParking = (parkingValue == 1);
                hike.setParking(isParking);


                int hikeLocationColumnIndex = cursor.getColumnIndex("Hike_Location");
                hike.setLocation(cursor.getString(hikeLocationColumnIndex));

                int hikeDifficultyColumnIndex = cursor.getColumnIndex("Hike_Difficulty");
                hike.setDifficulty(cursor.getString(hikeDifficultyColumnIndex));

                int hikeImageColumnIndex = cursor.getColumnIndex("Hike_Image");
                byte[] imageBytes = cursor.getBlob(hikeImageColumnIndex);

                // Convert the byte array to a Bitmap
                Bitmap image = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
                hike.setHikeImage(image);
            }
            else {
                Log.e("DatabaseHelper", "Cursor is empty");
            }
            cursor.close();
        }
        else {
            Log.e("DatabaseHelper", "Cursor is null");
        }

        return hike;
    }

//    public void deleteHike(int hikeId) {
//        SQLiteDatabase db = this.getWritableDatabase();
//
//        try {
//            // Check if the hike with the specified ID exists
//            HikeModel hike = getHikeById(hikeId);
//
//            if (hike != null) {
//                // Define the WHERE clause to delete the hike with the specified ID
//                String whereClause = "Hike_ID = ?";
//                String[] whereArgs = {String.valueOf(hikeId)};
//
//                int deletedRows = db.delete("Hikes", whereClause, whereArgs);
//
//                if (deletedRows > 0) {
//                    Log.d("DatabaseHelper", "Hike with ID " + hikeId + " deleted successfully.");
//                } else {
//                    Log.d("DatabaseHelper", "No hike deleted. Rows affected: " + deletedRows);
//                }
//            } else {
//                Log.d("DatabaseHelper", "Hike with ID " + hikeId + " not found.");
//            }
//        } catch (Exception e) {
//            Log.e("DatabaseHelper", "Error deleting hike: " + e.getMessage());
//        } finally {
//            if (db != null && db.isOpen()) {
//                db.close();
//            }
//        }
//    }

    public void deleteHike(int hikeId) {
        SQLiteDatabase db = getReadableDatabase();
        db.delete(TABLE_HIKE, "Hike_ID = " + hikeId, null );
        db.close();
    }







}
