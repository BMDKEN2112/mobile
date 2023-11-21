package com.example.hikingapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
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
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.github.dhaval2404.imagepicker.constant.ImageProvider;

import java.io.IOException;
import java.util.Locale;

public class AddObservationActivity extends AppCompatActivity {

    private ImageView goback_btn, observationImageView;

    private EditText observationTimeEditText, observationNameEditText, observationCommentEditText;

    private ImageButton timeButton;

    private Button selectImageButton, addObservationButton;

    private Bitmap imageGallery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_observation);

        goBack();
        selectTime();
        selectImage();
        addObservation();

    }

    public void addObservation(){
        addObservationButton = findViewById(R.id.addObservationButton);
        observationNameEditText = findViewById(R.id.observationNameEditText);
        observationCommentEditText = findViewById(R.id.observationCommentEditText);

        addObservationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                    DatabaseHelper dbHelper = new DatabaseHelper(AddObservationActivity.this);
                    dbHelper.addObservation(
                            observationNameEditText.getText().toString().trim(),
                            observationCommentEditText.getText().toString().trim(),
                            observationTimeEditText.getText().toString().trim(),
                            getHikeID(),
                            imageGallery
                    );
                    //changeActivity();
                }catch (Exception e){
                    Toast.makeText(AddObservationActivity.this, e.toString(), Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    public int getHikeID() {
        Intent i = getIntent();
        return i.getIntExtra("Hike_ID", 14);
    }
    public void selectTime(){
        observationTimeEditText = findViewById(R.id.observationTimeEditText);
        timeButton = findViewById(R.id.timePickerButton);

        timeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showTimePickerDialog();
            }
        });
    }

    private void showTimePickerDialog() {
        TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                // Format the selected time (hour and minute) as a string
                String time = String.format(Locale.getDefault(), "%02d:%02d", hourOfDay, minute);

                // Set the selected time in the EditText
                observationTimeEditText.setText(time);
            }
        }, 0, 0, true); // The last parameter, true, enables 24-hour format

        timePickerDialog.show();
    }

    public void selectImage() {
        // Image
        observationImageView = findViewById(R.id.observationImageView);
        selectImageButton = findViewById(R.id.addImageButton);

        selectImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImagePicker.with(AddObservationActivity.this)
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
            observationImageView.setImageURI(uri);

            // Database purpose

            try {
                imageGallery = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        } else {
            Toast.makeText(AddObservationActivity.this, "No Image", Toast.LENGTH_SHORT).show();
        }
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

//    private void changeActivity() {
//        Intent i = new Intent(this, HikeDetailActivity.class);
//        startActivity(i);
//    }
}