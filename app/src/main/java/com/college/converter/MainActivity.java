package com.college.converter;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.college.converter.databinding.ActivityMainBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import Zhihao.RecipeSearchActivity;
import chaowu.DeezerActivity;

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

    static final String TAG = "MainActivity";

    private ActivityMainBinding binding; // Declare a binding variable

    @Override
    /**protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button buttonConvert = findViewById(R.id.convertButton);
        buttonConvert.setOnClickListener( view ->  {
            convertCurrency(view);
            Log.i(TAG,"Enter onCreate()");
        } );
    }**/
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        // Set the onClickListener with ViewBinding
        //binding.convertButton.setOnClickListener(view -> {
            //convertCurrency();
        //    Intent nextPage = new Intent(MainActivity.this, DeezerActivity.class);
         //   startActivity(nextPage);
       // });

    //}

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


//        EditText inputView = findViewById(R.id.entryId);

//        String inputAmount = inputView.getText().toString();

//        TextView resultView = findViewById(R.id.resultId);

//        if (!inputAmount.isEmpty()) {
//            Float inputAmountDecimal = Float.valueOf(inputAmount);

//            Float resultFloat = inputAmountDecimal * CONVERSION_RATE;

            //resultView.setText( resultFloat + " Euros" );
//            binding.resultId.setText(resultFloat + " Euros");
//        }
//        Log.i(TAG,"Enter convertCurrency()");


    }
}
