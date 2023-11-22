package com.example.hikingapp;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hikingapp.Database.DatabaseHelper;
import com.example.hikingapp.Model.HikeModel;
import com.google.gson.Gson;

import java.io.ByteArrayOutputStream;

public class HikeDetailActivity extends AppCompatActivity {

    private Button deleteButton, updateButton, addObservationButton, viewObservationButton;
    private ImageView goback_btn;
    DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hike_detail);

        goBack();
        dataPassing();
        deleteHike();
        updateHike();

        dbHelper = new DatabaseHelper(this);
        dbHelper.getReadableDatabase(); // Open the database

    }

    private void dataPassing(){
        // Retrieve the Hike ID passed from MainActivity
        Intent intent = getIntent();
        String hikeJson = intent.getStringExtra("Hike_JSON");
        if (hikeJson != null) {
            // Deserialize the JSON string back to a HikeModel object
            Gson gson = new Gson();
            HikeModel hike = gson.fromJson(hikeJson, HikeModel.class);
            if (hike != null) {
                TextView nameTextView = findViewById(R.id.detailNameTextView);
                nameTextView.setText("Name: " + hike.getName());
                TextView descriptionTextView = findViewById(R.id.detailDescriptionTextView);
                descriptionTextView.setText("Description: " + hike.getDescription());
                TextView dateTextView = findViewById(R.id.detailDateTextView);
                dateTextView.setText("Date: " + hike.getDate());
                TextView lengthTextView = findViewById(R.id.detailLengthTextView);
                lengthTextView.setText("Length: " + hike.getLength());
                TextView parkingTextView = findViewById(R.id.detailParkingTextView);
                int parkingValue = hike.getParking();
                parkingTextView.setText("Parking: " + (parkingValue == 1 ? "Yes" : "No"));
                TextView difficultyTextView = findViewById(R.id.detailDifficultyTextView);
                difficultyTextView.setText("Difficulty: " + hike.getDifficulty());
                TextView locationTextView = findViewById(R.id.detailLocationTextView);
                locationTextView.setText("Location: " + hike.getLocation());
                ImageView imageView = findViewById(R.id.detailImageView);
                imageView.setImageBitmap(hike.getHikeImage());
            } else {
                // Handle the case where deserialization failed
                Toast.makeText(this, "Failed to deserialize HikeModel", Toast.LENGTH_SHORT).show();
            }
        } else {
            // Handle the case where the JSON string is not found
            Toast.makeText(this, "Hike data not found", Toast.LENGTH_SHORT).show();
        }
    }

//    private void getData(){
//        Intent i = getIntent();
//        nameTextView = findViewById(R.id.detailNameTextView);
//        locationTextView = findViewById(R.id.detailLocationTextView);
//        lengthTextView = findViewById(R.id.detailLengthTextView);
//        descriptionTextView = findViewById(R.id.detailDescriptionTextView);
//        dateTextView = findViewById(R.id.detailDateTextView);
//        difficultyTextView = findViewById(R.id.detailDifficultyTextView);
//        parkingTextView = findViewById(R.id.detailParkingTextView);
//        imageView = findViewById(R.id.detailImageView);
//
//
//        nameTextView.setText(i.getStringExtra("Hike_Name"));
//        locationTextView.setText(i.getStringExtra("Hike_Location"));
//        lengthTextView.setText(i.getStringExtra("Hike_Length"));
//        dateTextView.setText(i.getStringExtra("Hike_Date"));
//        descriptionTextView.setText(i.getStringExtra("Hike_Description"));
//        difficultyTextView.setText(i.getStringExtra("Hike_Difficulty"));
//        int parkingValue = i.getIntExtra("Hike_Parking", 0);
//        parkingTextView.setText(String.valueOf(parkingValue));
//
//        Bitmap hikeImage = i.getParcelableExtra("Hike_Image");
//        if (hikeImage != null) {
//            ByteArrayOutputStream stream = new ByteArrayOutputStream();
//            hikeImage.compress(Bitmap.CompressFormat.PNG, 50, stream);
//            byte[] byteArray = stream.toByteArray();
//            hikeImage = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
//            imageView.setImageBitmap(hikeImage);
//        }else{
//            Log.e("HikeDetailActivity", "Hike image is null. Check why this is happening.");
//        }
//
//
//    }

    private int getID() {
        Intent i = getIntent();
        int id = i.getIntExtra("Hike_ID", 0);
        Log.d("HikeDetail", "ID: " + id);
        return id;
    };

    public void goBack() {
        goback_btn = findViewById(R.id.backButton);
        goback_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public void deleteHike() {
        int hikeId = getIntent().getIntExtra("Hike_ID", -1);
        if (hikeId != -1) {
            deleteButton = findViewById(R.id.deleteButton);
            deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        // Call the deleteHikeById method to delete the hike
                        DatabaseHelper dbHelper = new DatabaseHelper(HikeDetailActivity.this);
                        dbHelper.deleteHike(hikeId);
                        // Show a toast message to indicate the deletion
                        Toast.makeText(HikeDetailActivity.this, "Hike deleted", Toast.LENGTH_SHORT).show();
                        // Finish the activity to return to the previous screen
                        finish();
                    } catch (Exception e) {
                        Toast.makeText(HikeDetailActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } else {
            // Handle the case where the hike ID is not found
            Toast.makeText(this, "Hike ID not found", Toast.LENGTH_SHORT).show();
        }
    }

    public void updateHike() {
        int hikeId = getIntent().getIntExtra("Hike_ID", -1);
        if (hikeId != -1) {
            updateButton = findViewById(R.id.updateButton);
            updateButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        // Call the updateHike method to navigate to the UpdateHikeActivity
                        // Pass the Hike ID to the UpdateHikeActivity
                        Intent intent = new Intent(HikeDetailActivity.this, UpdateHikeActivity.class);
                        intent.putExtra("Hike_ID", hikeId);
                        startActivity(intent);
                        Toast.makeText(HikeDetailActivity.this, "Update button clicked", Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        Toast.makeText(HikeDetailActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } else {
            // Handle the case where the Hike ID is not found
            Toast.makeText(this, "Hike ID not found", Toast.LENGTH_SHORT).show();
        }
    }

    public void pageTransition(){
        addObservationButton = findViewById(R.id.addObservationButton);

        Intent intent = new Intent(this, AddObservationActivity.class);
        intent.putExtra("Hike_ID", getID());
        this.startActivity(intent);
        Toast.makeText(HikeDetailActivity.this, "Add observation button clicked", Toast.LENGTH_SHORT).show();
    }

    public void moveToAddObservation(View view){
        pageTransition();
    }

    public void pageTransition2(){
        viewObservationButton = findViewById(R.id.viewObservationButton);

        Intent intent = new Intent(this, ObservationViewActivity.class);
        intent.putExtra("Hike_ID", getID());
        this.startActivity(intent);

    }

    public void moveToViewObservation(View view){
        int hikeID = getID(); // Get the hike ID
        boolean hasObservations = dbHelper.hasObservations(hikeID);

        if (hasObservations) {
            pageTransition2(); // There are observations, proceed to the "View Observation" page
        } else {
            Toast.makeText(this, "No observation added yet", Toast.LENGTH_SHORT).show();
        }
    }



}
