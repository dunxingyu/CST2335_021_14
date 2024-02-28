package com.college.converter;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.college.converter.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    static private final Float CONVERSION_RATE = 0.80F;
    static final String TAG = "MainActivity";

    private ActivityMainBinding binding; // Declare a binding variable

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Inflate the layout using ViewBinding
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot()); // Use the root view of the binding class

        // Set the onClickListener with ViewBinding
        binding.convertButton.setOnClickListener(view -> {
            convertCurrency();
            Log.i(TAG,"Enter onCreate()"); // Logging statement from 'main'
        });
    }

    public void convertCurrency() {
        String inputAmount = binding.entryId.getText().toString();

        if (!inputAmount.isEmpty()) {
            try {
                Float inputAmountDecimal = Float.parseFloat(inputAmount);
                Float resultFloat = inputAmountDecimal * CONVERSION_RATE;
                binding.resultId.setText(String.format("%s Euros", resultFloat));
            } catch (NumberFormatException e) {
                Toast.makeText(this, "Please enter a valid number.", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Input cannot be empty.", Toast.LENGTH_SHORT).show();
        }
        Log.i(TAG,"Enter convertCurrency()"); // Ensure logging is present
    }
}