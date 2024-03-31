package com.college.converter;

import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;



import com.college.converter.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    static private final Float CONVERSION_RATE = 0.80F;
    static final String TAG = "MainActivity";

    private ActivityMainBinding binding; // Declare a binding variable

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);


        // Set Home selected
        bottomNavigationView.setSelectedItemId(R.id.home_id);

        // Perform item selected listener
        bottomNavigationView.setOnItemSelectedListener(item -> {

            int item_id = item.getItemId();
            if (item_id == R.id.home_id) {
                return true;
            }
            else if (item_id == R.id.first_id) {
                startActivity(new Intent(getApplicationContext(), FirstActivity.class));
                return true;
            }
            else if (item_id == R.id.second_id) {
                startActivity(new Intent(getApplicationContext(), SecondActivity.class));
                return true;
            }
            else if (item_id == R.id.third_id) {
                startActivity(new Intent(getApplicationContext(), Dictionary.class));
                return true;
            }
            else if (item_id == R.id.forth_id) {
                startActivity(new Intent(getApplicationContext(), ForthActivity.class));
                return true;
            }
            return false;
        });

    }
}