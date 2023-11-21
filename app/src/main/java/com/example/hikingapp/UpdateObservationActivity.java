package com.example.hikingapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.hikingapp.Database.DatabaseHelper;
import com.example.hikingapp.Model.ObservationModel;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.github.dhaval2404.imagepicker.constant.ImageProvider;

import java.io.IOException;
import java.util.Locale;

public class UpdateObservationActivity extends AppCompatActivity {

    ImageView goback, observation_image_view;

    EditText edit_observation_name, edit_observation_comment, edit_observation_time;

    ImageButton timeButton;

    Button updateObservation;

    Bitmap observationImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_observation);

        getData();
        selectTime();
        selectImage();

    }

    private void getData(){
        edit_observation_name = findViewById(R.id.observationNameEditText);
        edit_observation_comment = findViewById(R.id.observationCommentEditText);
        edit_observation_time = findViewById(R.id.observationTimeEditText);
        observation_image_view = findViewById(R.id.observationImageView);
        updateObservation = findViewById(R.id.updateObservationButton);

        Intent i = getIntent();
        int observationID = i.getIntExtra("Observation_ID", -1);
        DatabaseHelper dpHelper = new DatabaseHelper(this);
        ObservationModel observationModel = dpHelper.getObservationById(observationID);

        edit_observation_name.setText(observationModel.getObservationName());
        edit_observation_comment.setText(observationModel.getObservationComment());
        edit_observation_time.setText(observationModel.getObservationTime());

        // Retrieve the byte array from the intent
        observationImage = dpHelper.getExistingObservationImage(observationID);
        if (observationImage != null) {
            observation_image_view.setImageBitmap(observationImage);
        }

        updateObservation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                    DatabaseHelper dbHelper = new DatabaseHelper(UpdateObservationActivity.this);
                    dbHelper.updateObservation(
                        observationID,
                        edit_observation_name.getText().toString().trim(),
                        edit_observation_comment.getText().toString().trim(),
                        edit_observation_time.getText().toString().trim(),
                        observationImage
                    );
                    backToObservationList();
                }catch (Exception e){
                    Toast.makeText(UpdateObservationActivity.this, "Please fill all the required fields", Toast.LENGTH_LONG).show();
                }
            }
        });

    }


    public void selectTime(){
        timeButton = findViewById(R.id.timeButton);

        timeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showTimePickerDialog();
            }
        });
    }

    private void showTimePickerDialog() {
        edit_observation_time =findViewById(R.id.observationTimeEditText);
        TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                // Format the selected time (hour and minute) as a string
                String time = String.format(Locale.getDefault(), "%02d:%02d", hourOfDay, minute);

                // Set the selected time in the EditText
                edit_observation_time.setText(time);
            }
        }, 0, 0, true); // The last parameter, true, enables 24-hour format

        timePickerDialog.show();
    }

    private void selectImage() {
        observation_image_view = findViewById(R.id.observationImageView);
        observation_image_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImagePicker.with(UpdateObservationActivity.this)
                        .provider(ImageProvider.BOTH)
                        .crop()
                        .maxResultSize(1080, 1080)
                        .start(101);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 101 && resultCode == Activity.RESULT_OK) {
            Uri uri = data.getData();
            observation_image_view.setImageURI(uri);

            // Database purpose

            try {
                 observationImage = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        } else {
            Toast.makeText(UpdateObservationActivity.this, "No Image", Toast.LENGTH_SHORT).show();
        }
    }

    private void backToObservationList() {
        Intent i = new Intent(this, ObservationViewActivity.class);
        startActivity(i);
    }


}