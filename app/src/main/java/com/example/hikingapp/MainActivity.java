package com.example.hikingapp;

import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hikingapp.Database.DatabaseHelper;
import com.example.hikingapp.Model.HikeModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.ByteArrayOutputStream;
import java.util.List;
import com.google.gson.Gson;

public class MainActivity extends AppCompatActivity {

    private ImageView imageView;
    private Button prevButton, nextButton;
    private int currentImageIndex = 0;
    Bitmap hikeImage;
    BottomNavigationView bottomNavigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        imageView = findViewById(R.id.imageView);
        prevButton = findViewById(R.id.prevButton);
        nextButton = findViewById(R.id.nextButton);
        activityTransition();
        updateHikeList();


        prevButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (currentImageIndex > 0) {
                    currentImageIndex--; // Move to the previous hike
                    Toast.makeText(MainActivity.this, "The previous button clicked", Toast.LENGTH_SHORT).show();
                    displayHike(currentImageIndex);
                }
                else {
                    Toast.makeText(MainActivity.this, "No more hike to show", Toast.LENGTH_SHORT).show();
                }
            }
        });
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseHelper db = new DatabaseHelper(MainActivity.this);
                List<HikeModel> hikeList = db.fetchHikeData();
                if (currentImageIndex < hikeList.size() - 1) {
                    currentImageIndex++; // Move to the next hike
                    Toast.makeText(MainActivity.this, "The next button clicked", Toast.LENGTH_SHORT).show();
                    displayHike(currentImageIndex);
                }
                else {
                    Toast.makeText(MainActivity.this, "No more hike to show", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void resetDatabase() {
        DatabaseHelper db = new DatabaseHelper(MainActivity.this);
        db.resetDatabase();
        Toast.makeText(MainActivity.this, "Database reset successfully", Toast.LENGTH_SHORT).show();
        updateHikeList(); // Update the UI to reflect the changes
    }

    public void activityTransition() {
        bottomNavigation = findViewById(R.id.bottom_navigation);
        bottomNavigation.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.navigation_home) {
                activityTransition(MainActivity.class);
            } else if (item.getItemId() == R.id.navigation_add) {
                activityTransition(AddHikeActivity.class);
            }
            else if (item.getItemId() == R.id.navigation_search) {
                activityTransition(SearchHikeActivity.class);
            } else if (item.getItemId() == R.id.navigation_reset) {
                resetDatabase();
                activityTransition(MainActivity.class);
            }
            return false;
        });
    }

    private void activityTransition(Class<?> targetActivity) {
        Intent intent = new Intent(this, targetActivity);
        startActivity(intent);
    }


    private void updateHikeList() {
        displayHike(0);
    }

    private void displayHike(int index) {
        DatabaseHelper db = new DatabaseHelper(MainActivity.this);
        List<HikeModel> hikeList = db.fetchHikeData();
        if (index >= 0 && index < hikeList.size()) {
            HikeModel hike = hikeList.get(index);
            TextView nameTextView = findViewById(R.id.nameTextView);
            nameTextView.setText("Name: " + hike.getName());
            TextView locationTextView = findViewById(R.id.locationTextView);
            locationTextView.setText("Location: " + hike.getLocation());
            TextView lengthTextView = findViewById(R.id.lengthTextView);
            lengthTextView.setText("Length: " + hike.getLength());
            TextView parkingTextView = findViewById(R.id.parkingTextView);
            int parkingValue = hike.getParking();
            parkingTextView.setText("Parking: " + (parkingValue == 1 ? "Yes" : "No"));
            TextView difficultyTextView = findViewById(R.id.difficultyTextView);
            difficultyTextView.setText("Difficulty: " + hike.getDifficulty());
            TextView dateTextView = findViewById(R.id.dateTextView);
            dateTextView.setText("Date: " + hike.getDate());
            TextView descriptionTextView = findViewById(R.id.descriptionTextView);
            descriptionTextView.setText("Description: " + hike.getDescription());
            imageView.setImageBitmap(hike.getHikeImage());
            currentImageIndex = index;
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    moveToDetail(hike); // Pass the hike
                }
            });
        }
    }

    private void moveToDetail(HikeModel hikeModel) {
        Log.d("MainActivity", "Moving to HikeDetailActivity");
        HikeModel hikeDetail = new HikeModel();
        hikeDetail.setId(hikeModel.getId());
        hikeDetail.setName(hikeModel.getName());
        hikeDetail.setLocation(hikeModel.getLocation());
        hikeDetail.setDate(hikeModel.getDate()); // Include the Date field
        hikeDetail.setLength(hikeModel.getLength()); // Include the Length field
        hikeDetail.setDescription(hikeModel.getDescription());
        hikeDetail.setParking(hikeModel.getParking());
        hikeDetail.setDifficulty(hikeModel.getDifficulty());
        hikeDetail.setHikeImage(hikeModel.getHikeImage());
        Intent intent = new Intent(MainActivity.this, HikeDetailActivity.class);
        intent.putExtra("Hike_ID", hikeModel.getId());
        Gson gson = new Gson();
        String hikeJson = gson.toJson(hikeModel);
        intent.putExtra("Hike_JSON", hikeJson);
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateHikeList();
    }

}