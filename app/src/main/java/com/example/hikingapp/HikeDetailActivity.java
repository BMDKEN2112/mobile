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
    private Button deleteButton;

    private ImageView goback_btn;

    private HikeModel hike;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hike_detail);

        goBack();
        dataPassing();
        deleteHike();


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
                parkingTextView.setText("Parking: "  + hike.getParking());
                if (hike.getParking()) {
                    parkingTextView.setText("Parking: Yes");
                } else {
                    parkingTextView.setText("Parking: No");
                }



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

    private void activityTransition(Class<?> targetActivity) {
        Intent intent = new Intent(this, targetActivity);
        intent.putExtra("id", getID());
        startActivity(intent);
    }

    private int getID() {
        Intent i = getIntent();
        int id = i.getIntExtra("id", 0);
        return id;
    };

    public void onUpdateButtonClick(View view) {
        // Call the activityTransition function to navigate to the UpdateHikeActivity

        // activityTransition(UpdateHikeActivity.class);
    }

    public void onAddObservationButtonClick(View view) {
        // Call the activityTransition function to navigate to the AddObservationActivity

        //activityTransition(AddObservationActivity.class);
    }

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
        Intent intent = getIntent();
        String hikeJson = intent.getStringExtra("Hike_JSON");
        Gson gson = new Gson();
        HikeModel hike = gson.fromJson(hikeJson, HikeModel.class);
        int id = hike.getId();
        deleteButton = findViewById(R.id.deleteButton);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Get the hike ID of the current hike

                try{

                    // Call the deleteHikeById method to delete the hike
                    DatabaseHelper dbHelper = new DatabaseHelper(HikeDetailActivity.this);
                    dbHelper.deleteHike(id);

                    // Show a toast message to indicate the deletion
                    Toast.makeText(HikeDetailActivity.this, "dd", Toast.LENGTH_SHORT).show();

                    // Finish the activity to return to the previous screen
                    finish();
                }catch(Exception e){
                    Toast.makeText(HikeDetailActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
                }
            }

        });

    }



}
