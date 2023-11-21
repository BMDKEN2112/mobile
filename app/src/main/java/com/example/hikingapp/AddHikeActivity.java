package com.example.hikingapp;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.Manifest;
import android.content.pm.PackageManager;


import com.example.hikingapp.Database.DatabaseHelper;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.github.dhaval2404.imagepicker.constant.ImageProvider;

import java.io.IOException;
import java.util.Calendar;
import java.util.Locale;

public class AddHikeActivity extends AppCompatActivity {

    private EditText hikeNameEditText;
    private EditText locationEditText;
    private EditText hikeDateEditText;
    private EditText lengthEditText;
    private EditText difficultyEditText;
    private EditText descriptionEditText;
    private Button addHikeButton;
    private ImageView addedImageView;
    private Button selectImageButton;
    private ImageButton calendarButton;
    private ImageButton timeButton;
    private Bitmap imageGallery;
    private RadioGroup parkingRadioGroup;
    RadioButton btnYes, btnNo;
    private TextView parkingStatusTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_hike);

        addHike();
        selectImage();
        selectCalendar();
        selectTime();

        parkingStatusTextView = findViewById(R.id.parkingStatusTextView);

    }

    public void addHike(){
        addHikeButton = findViewById(R.id.addHikeButton);
        hikeNameEditText = findViewById(R.id.hikeNameEditText);
        locationEditText = findViewById(R.id.locationEditText);
        parkingRadioGroup = findViewById(R.id.parkingRadioGroup);
        difficultyEditText = findViewById(R.id.difficultyEditText);
        descriptionEditText = findViewById(R.id.descriptionEditText);
        lengthEditText = findViewById(R.id.lengthEditText);
        hikeDateEditText = findViewById(R.id.hikeDateEditText);
        btnYes = findViewById(R.id.yesRadioButton);
        btnNo = findViewById(R.id.noRadioButton);
        final int[] isParking = {btnYes.isChecked() ? 1 : 0};
        parkingRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.yesRadioButton) {
                    isParking[0] = 1; // Yes is selected
                } else if (checkedId == R.id.noRadioButton) {
                    isParking[0] = 0; // No is selected
                }
            }
        });
        addHikeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    DatabaseHelper db = new DatabaseHelper(AddHikeActivity.this);

                    db.addHike(
                            hikeNameEditText.getText().toString().trim(),
                            locationEditText.getText().toString().trim(),
                            hikeDateEditText.getText().toString().trim(),
                            isParking[0],
                            lengthEditText.getText().toString().trim(),
                            difficultyEditText.getText().toString().trim(),
                            descriptionEditText.getText().toString().trim(),
                            imageGallery);
                changeActivity();
                }catch(Exception e){
                    Toast.makeText(AddHikeActivity.this, e.toString(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public void selectImage() {
        addedImageView = findViewById(R.id.addedImageView);
        selectImageButton = findViewById(R.id.selectImageButton);
        selectImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImagePicker.with(AddHikeActivity.this)
                        .provider(ImageProvider.BOTH)
                        .crop()	    			//Crop image(Optional), Check Customization for more option
                        .compress(1024)			//Final image size will be less than 1 MB(Optional)
                        .maxResultSize(1080, 1080)	//Final image resolution will be less than 1080 x 1080(Optional)
                        .start(101);
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 101 && resultCode == Activity.RESULT_OK) {
            Uri uri = data.getData();
            addedImageView.setImageURI(uri);
            try {
                imageGallery = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            Toast.makeText(AddHikeActivity.this, "No Image", Toast.LENGTH_SHORT).show();
        }
    }

    public void selectCalendar(){
        hikeDateEditText = findViewById(R.id.hikeDateEditText);
        calendarButton = findViewById(R.id.calendarButton);
        calendarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePickerDialog();
            }
        });
    }
    private void showDatePickerDialog() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        String selectedDate = dayOfMonth + "/" + (month + 1) + "/" + year;
                        hikeDateEditText.setText(selectedDate);
                    }
                },
                year, month, day
        );
        datePickerDialog.show();
    }

    public void selectTime(){
        lengthEditText = findViewById(R.id.lengthEditText);
        timeButton = findViewById(R.id.timeButton);
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
                lengthEditText.setText(time);
            }
        }, 0, 0, true); // The last parameter, true, enables 24-hour format
        timePickerDialog.show();
    }

    private void changeActivity() {
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
    }


}
