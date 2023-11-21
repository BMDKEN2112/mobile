package com.example.hikingapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.hikingapp.Adapter.ObservationAdapter;
import com.example.hikingapp.Database.DatabaseHelper;
import com.example.hikingapp.Model.ObservationModel;

import java.util.ArrayList;

public class ObservationViewActivity extends AppCompatActivity {


    RecyclerView listViewObservations;

    ArrayList<ObservationModel> observations;

    ObservationAdapter adapter;

    ImageView goback_btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_observation_view);

        fetchObservationData();
        goBack();
        refreshPage();
    }

    private void refreshPage(){
        Button refresh = findViewById(R.id.refresh_button);

        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(ObservationViewActivity.this, "Refresh button clicked", Toast.LENGTH_SHORT).show();
                fetchObservationData();
            }
        });
    }

    private void fetchObservationData() {
        listViewObservations = findViewById(R.id.listViewObservations);
        DatabaseHelper db = new DatabaseHelper(this);
        observations = db.fetchObservationData(getHikeId());
        adapter = new ObservationAdapter(observations, this);
        listViewObservations.setAdapter(adapter);
        listViewObservations.setLayoutManager(new LinearLayoutManager(this));
    }

    private int getHikeId() {
        Intent i = getIntent();
        int id = i.getIntExtra("Hike_ID", 0);
        return id;
    };

    public void goBack() {
        goback_btn = findViewById(R.id.goback_btn);

        goback_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}