package com.example.hikingapp;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.content.SharedPreferences;
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

public class HikeDetailActivity extends AppCompatActivity {

    private Button updatebutton;
    private Button deleteButton, updateButton;

    private ImageView goback_btn;

    private HikeModel hike;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hike_detail);

        goBack();
        dataPassing();
        deleteHike();
        updateHike();


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

                // Set the ImageView with the hike's image
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

    private int getID() {
        Intent i = getIntent();
        int id = i.getIntExtra("id", 0);
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




}
