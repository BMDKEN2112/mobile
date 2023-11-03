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
            COLUMN_HIKE_PARKING + " INTEGER, " +
            COLUMN_HIKE_LENGTH + " nvarchar, " +
            COLUMN_HIKE_DIFFICULTY + " nvarchar, " +
            COLUMN_HIKE_DESCRIPTION + " nvarchar, " +
            COLUMN_HIKE_IMAGE + " BLOB );";

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        db.execSQL(Hike);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public void addHike(String name, String location, String date, int Parking, String length, String level, String description, Bitmap Hike_Image) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        Bitmap imageToStorageBitmap = Hike_Image;

        byteArrayOutputStream = new ByteArrayOutputStream();
        imageToStorageBitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        imageInByte = byteArrayOutputStream.toByteArray();

        contentValues.put(COLUMN_HIKE_NAME, name);
        contentValues.put(COLUMN_HIKE_LOCATION, location);
        contentValues.put(COLUMN_HIKE_DATE, date);
        contentValues.put(COLUMN_HIKE_PARKING, Parking);
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

//    public List<HikeModel> getAllHikes() {
//        List<HikeModel> hikeList = new ArrayList<>();
//        SQLiteDatabase db = this.getReadableDatabase();
//        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_HIKE, null);
//
//        if (cursor.moveToFirst()) {
//            do {
//                HikeModel hike = new HikeModel();
//                int hikeNameColumnIndex = cursor.getColumnIndex("Hike_Name");
//                hike.setName(cursor.getString(hikeNameColumnIndex));
//                int hikeLocationColumnIndex = cursor.getColumnIndex("Hike_Location");
//                hike.setLocation(cursor.getString(hikeLocationColumnIndex));
//                int hikeDifficultyColumnIndex = cursor.getColumnIndex("Hike_Difficulty");
//                hike.setDifficulty(cursor.getString(hikeDifficultyColumnIndex));
//                int hikeLengthColumnIndex = cursor.getColumnIndex("Hike_Length");
//                hike.setLength(cursor.getString(hikeLengthColumnIndex));
//
//                int hikeParkingColumnIndex = cursor.getColumnIndex("Hike_Parking");
//                int parkingValue = cursor.getInt(hikeParkingColumnIndex);
//                hike.setParking(parkingValue);
//
//                int hikeDateColumnIndex = cursor.getColumnIndex("Hike_Date");
//                hike.setDate(cursor.getString(hikeDateColumnIndex));
//
//                int hikeDescriptionColumnIndex = cursor.getColumnIndex("Hike_Description");
//                hike.setDescription(cursor.getString(hikeDescriptionColumnIndex));
//
//                int hikeImageColumnIndex = cursor.getColumnIndex("Hike_Image");
//                byte[] imageBytes = cursor.getBlob(hikeImageColumnIndex);
//
//                // Convert the byte array to a Bitmap
//                Bitmap image = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
//                hike.setHikeImage(image);
//
//                hikeList.add(hike);
//                Log.d("DatabaseHelper", "Retrieved Hike Name: " + hike.getName());
//
//            } while (cursor.moveToNext());
//        }
//
//        cursor.close();
//        return hikeList;
//    }

    public ArrayList<HikeModel> fetchHikeData() {
        ArrayList<HikeModel> hikeList = new ArrayList<>();

        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_HIKE + " ORDER BY " + COLUMN_HIKE_ID + " ASC", null);

        if (cursor.moveToFirst()){
            do{
                int ID = cursor.getInt(0);
                String Name = cursor.getString(1);
                String Location = cursor.getString(2);
                String Date = cursor.getString(3);
                int Parking = cursor.getInt(4);
                String Length = cursor.getString(5);
                String Difficulty = cursor.getString(6);
                String Description = cursor.getString(7);
                byte[] image = cursor.getBlob(8);

                Bitmap BitmapImage = BitmapFactory.decodeByteArray(image, 0, image.length);

                HikeModel hike = new HikeModel(ID, Name, Location, Date, Parking, Length, Difficulty, Description, BitmapImage);
                hikeList.add(hike);

            }while (cursor.moveToNext());
        }
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

                int hikeNameColumnIndex = cursor.getColumnIndex("Hike_Name");
                hike.setName(cursor.getString(hikeNameColumnIndex));

                int hikeDateColumnIndex = cursor.getColumnIndex("Hike_Date");
                hike.setDate(cursor.getString(hikeDateColumnIndex));

                int hikeDescriptionColumnIndex = cursor.getColumnIndex("Hike_Description");
                hike.setDescription(cursor.getString(hikeDescriptionColumnIndex));

                int hikeLengthColumnIndex = cursor.getColumnIndex("Hike_Length");
                hike.setLength(cursor.getString(hikeLengthColumnIndex));

                int hikeParkingColumnIndex = cursor.getColumnIndex("Hike_Parking");
                int parkingValue = cursor.getInt(hikeParkingColumnIndex);
                hike.setParking(parkingValue);


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


    public void deleteHike(int hikeId) {
        SQLiteDatabase db = getReadableDatabase();
        db.delete(TABLE_HIKE, "Hike_ID = " + hikeId, null );
        db.close();
    }

    public void updateHike(int id, String name, String location, String date, int Parking, String length, String difficulty, String description, Bitmap bitmapImage)
    {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_HIKE_NAME, name);
        contentValues.put(COLUMN_HIKE_LOCATION, location);
        contentValues.put(COLUMN_HIKE_DATE, date);
        contentValues.put(COLUMN_HIKE_PARKING, Parking);
        contentValues.put(COLUMN_HIKE_LENGTH, length);
        contentValues.put(COLUMN_HIKE_DIFFICULTY, difficulty);
        contentValues.put(COLUMN_HIKE_DESCRIPTION, description);
        byte[] hikeImage = convertBitmapToByteArray(bitmapImage);
        contentValues.put(COLUMN_HIKE_IMAGE, hikeImage);
        db.update(TABLE_HIKE, contentValues, "Hike_ID = " + id, null);
    }

    private byte[] convertBitmapToByteArray(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        return stream.toByteArray();
    }

    public Bitmap getExistingImage(int hikeId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Bitmap existingImage = null;

        String[] columns = {COLUMN_HIKE_IMAGE};
        String selection = COLUMN_HIKE_ID + " = ?";
        String[] selectionArgs = {String.valueOf(hikeId)};

        Cursor cursor = db.query(
                TABLE_HIKE,
                columns,
                selection,
                selectionArgs,
                null,
                null,
                null
        );

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                int imageColumnIndex = cursor.getColumnIndex(COLUMN_HIKE_IMAGE);
                byte[] imageBytes = cursor.getBlob(imageColumnIndex);

                // Convert the byte array to a Bitmap
                existingImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
            }
            cursor.close();
        }

        return existingImage;
    }











}
