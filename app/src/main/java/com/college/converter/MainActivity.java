/*File name:MainActivity.java
 *Author:Dunxing YU,Qi Cheng,Chao Wu,Zhihao Zhang
 *Course:CST2335-021
 *Assignment:Project
 *Data:2024-3-26
 *Professor:Ouaaz, Samira
 */
package com.college.converter;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import Zhihao.RecipeSearchActivity;
import chaowu.DeezerActivity;

/**
 * This is a welcome page, the APP starts from MainActivity.
 * @author Dunxing YU,Qi Cheng,Chao Wu,Zhihao Zhang
 *
 */

public class MainActivity extends AppCompatActivity {

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
                startActivity(new Intent(getApplicationContext(), Sunlookup.class));
                return true;
            }
            else if (item_id == R.id.second_id) {
                startActivity(new Intent(getApplicationContext(), RecipeSearchActivity.class));
                return true;
            }
            else if (item_id == R.id.third_id) {
                startActivity(new Intent(getApplicationContext(), Dictionary.class));
                return true;
            }
            else if (item_id == R.id.forth_id) {
                startActivity(new Intent(getApplicationContext(), DeezerActivity.class));
                return true;
            }
            return false;
        });

    }
}
