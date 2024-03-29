package com.college.converter;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;

/*
    TODOs:
    In groups of 4, complete the following tasks, 1 for each team member:
    1. Extract all the strings into the strings.xml file and use them in the layout and the activity
    2. Change the theme of the app to a NoActionBar theme and modify the primary colors
    3. Add Log messages at the entry/exit of onCreate() and convertCurrency methods. Level should be Info
    4. Add ViewBinding to the project

    ** Each task must be done in a separate branch and merged to the main branch
    after completion using a Pull Request.
    ** Each task must be done by a different team member.

*/

public class MainActivity extends AppCompatActivity {
    static private final Float CONVERSION_RATE = 0.80F;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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