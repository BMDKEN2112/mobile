package com.example.hikingapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
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

import com.example.hikingapp.Database.DatabaseHelper;
import com.example.hikingapp.Model.HikeModel;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.github.dhaval2404.imagepicker.constant.ImageProvider;

import org.w3c.dom.Text;

import java.io.IOException;
import java.util.Calendar;
import java.util.Locale;

public class UpdateHikeActivity extends AppCompatActivity {

    EditText nameEditText, locationEditText, descriptionEditText, difficultyEditText;
    TextView parkingTextView, lengthTextView, dateTextView;
    RadioButton yesBTN, noBTN;
    RadioGroup parkingRadioGroup;
    ImageView hikeImageView, back_btn;
    Button updateHike;
    DatabaseHelper dbHelper;
    ImageButton calendarButton, timeButton;
    Bitmap existingImage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_hike);

        selectImage();
        selectCalendar();
        selectTime();
        getData();
        goBack();

    }

    private void getData(){
            dbHelper = new DatabaseHelper(this);
            Intent i = getIntent();
            int hikeID = i.getIntExtra("Hike_ID", -1);
            HikeModel hike = dbHelper.getHikeById(hikeID);
            nameEditText = findViewById(R.id.nameEditText);
            locationEditText = findViewById(R.id.locationEditText);
            descriptionEditText = findViewById(R.id.descriptionEditText);
            difficultyEditText = findViewById(R.id.difficultyEditText);
            dateTextView = findViewById(R.id.dateTextView);
            lengthTextView = findViewById(R.id.lengthTextView);
            parkingTextView = findViewById(R.id.parkingTextView);
            parkingRadioGroup = findViewById(R.id.parkingRadioGroup);
            yesBTN = findViewById(R.id.yesRadioButton);
            noBTN = findViewById(R.id.noRadioButton);
            hikeImageView = findViewById(R.id.hikeImageView);
            updateHike = findViewById(R.id.updateButton);
            nameEditText.setText(hike.getName());
            locationEditText.setText(hike.getLocation());
            descriptionEditText.setText(hike.getDescription());
            difficultyEditText.setText(hike.getDifficulty());
            dateTextView.setText(hike.getDate());
            lengthTextView.setText(hike.getLength());
            if(i.getIntExtra("isParking", 0) == 1) {
                yesBTN.setChecked(true);
            } else {
                noBTN.setChecked(false);
            }
            final int[] Parking = {yesBTN.isChecked() ? 1 : 0};

            parkingRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId) {
                    if (checkedId == R.id.yesRadioButton) {
                        Parking[0] = 1; // Yes is selected
                    } else if (checkedId == R.id.noRadioButton) {
                        Parking[0] = 0; // No is selected
                    }
                }
            });
            existingImage = dbHelper.getExistingHikeImage(hikeID);

            if (existingImage != null) {
                hikeImageView.setImageBitmap(existingImage);
            }
            updateHike.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        dbHelper = new DatabaseHelper(UpdateHikeActivity.this);
                        dbHelper.updateHike(
                                hikeID,
                                nameEditText.getText().toString().trim(),
                                locationEditText.getText().toString().trim(),
                                dateTextView.getText().toString().trim(),
                                Parking[0],
                                lengthTextView.getText().toString().trim(),
                                difficultyEditText.getText().toString().trim(),
                                descriptionEditText.getText().toString().trim(),
                                existingImage
                        );
                        BacktoMainActivity();
                    }catch  (Exception e) {
                    }
                }
            });
    }

    public void selectCalendar(){
        dateTextView = findViewById(R.id.dateTextView);
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
                        dateTextView.setText(selectedDate);
                    }
                },
                year, month, day
        );
        datePickerDialog.show();
    }

    public void selectTime(){
        lengthTextView = findViewById(R.id.lengthEditText);
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
                lengthTextView.setText(time);
            }
        }, 0, 0, true); // The last parameter, true, enables 24-hour format
        timePickerDialog.show();
    }

    private void selectImage() {
        ImageView hikeImageView = findViewById(R.id.hikeImageView);
        hikeImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImagePicker.with(UpdateHikeActivity.this)
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
            hikeImageView.setImageURI(uri);
            try {
                existingImage = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            Toast.makeText(UpdateHikeActivity.this, "No Image", Toast.LENGTH_SHORT).show();
        }
    }

    private void BacktoMainActivity() {
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
    }

    private void goBack() {
        back_btn = findViewById(R.id.backButton);

        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}