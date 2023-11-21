package com.example.hikingapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.example.hikingapp.Adapter.SearchHikeAdapter;
import com.example.hikingapp.Database.DatabaseHelper;
import com.example.hikingapp.Model.HikeModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

public class SearchHikeActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigation;

    SearchView searchView;

    RecyclerView recyclerView;

    ArrayList<HikeModel> hike;

    SearchHikeAdapter searchHikeAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_hike);

        searchView = findViewById(R.id.search_view);
        recyclerView = findViewById(R.id.search_recycler_view);
        fetchData();
        activityTransition();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searchHikeAdapter.getFilter().filter(newText);
                return false;
            }
        });
  }

    public void activityTransition() {
        bottomNavigation = findViewById(R.id.bottom_navigation_search);
        bottomNavigation.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.navigation_home) {
                activityTransition(MainActivity.class);
            } else if (item.getItemId() == R.id.navigation_add) {
                activityTransition(AddHikeActivity.class);
            }
            else if (item.getItemId() == R.id.navigation_search) {
                activityTransition(SearchHikeActivity.class);
            }
            return false;
        });
    }

    private void activityTransition(Class<?> targetActivity) {
        Intent intent = new Intent(this, targetActivity);
        startActivity(intent);
    }

    public void fetchData() {

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setMaxWidth(Integer.MAX_VALUE);

        DatabaseHelper db = new DatabaseHelper(this);
        hike = db.fetchHikeData();
        searchHikeAdapter = new SearchHikeAdapter(hike, this);
        recyclerView.setAdapter(searchHikeAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

    }
}