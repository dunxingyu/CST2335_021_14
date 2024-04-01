/*File name:Sunlookup.java
 *Author:Dunxing Yu
 *Course:CST2335-021
 *Assignment:Project
 *Data:2024-3-26
 *Professor:Ouaaz, Samira
 */

package com.college.converter;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.college.converter.data.SunlookupData;
import com.college.converter.data.SunlooupModel;
import com.college.converter.databinding.ActivitySunlookupBinding;
import com.college.converter.databinding.SentMessageBinding;
import com.college.converter.ui.SunlookupDatabase;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import Zhihao.RecipeSearchActivity;
import chaowu.DeezerActivity;


/**This APP search the sunrise and sunset time base on user enter the lat and longitude.
 * @author Dunxing Yu
 * @see SunlookupData
 * @see SunlooupModel
 * @see SunlookupDatabase
 * @see SunlookupDAO
 */
public class Sunlookup extends AppCompatActivity {
    /**Binding for activity layout*/
    ActivitySunlookupBinding binding;
    /** List to store search history messages */
    ArrayList<SunlookupData> messages = new ArrayList<>();
    /** Adapter for RecyclerView */
    private RecyclerView.Adapter<MyRowHolder> myAdapter;
    /** ViewModel for managing app data */
    SunlooupModel sunModel;

    /** DAO for interacting with the database */
    SunlookupDAO sDAO;
    /** Request queue for network requests */
    protected RequestQueue queue;
    /** Latitude */
    String lat;
    /**longitude strings */
    String lng;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySunlookupBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        queue = Volley.newRequestQueue(this);
        SunlookupDatabase db1 = Room.databaseBuilder(getApplicationContext(), SunlookupDatabase.class, getString(R.string.sundb)).build();
        sDAO = db1.slDAO();
        sunModel = new ViewModelProvider(this).get(SunlooupModel.class);
        messages = sunModel.messages.getValue();
        if(messages == null)
        {
            sunModel.messages.postValue(messages = new ArrayList<SunlookupData>());
            Executor thread = Executors.newSingleThreadExecutor();
            thread.execute(() ->
            {
                messages.addAll( sDAO.getAllData() ); //Once you get the data from database

                runOnUiThread( () ->  binding.recyclerView.setAdapter( myAdapter )); //You can then load the RecyclerView
            });
        }
        SharedPreferences spSearch = getSharedPreferences(getString(R.string.searchRecord),MODE_PRIVATE);
        SharedPreferences.Editor edit = spSearch.edit();
        binding.editTextLat.setText(spSearch.getString(getString(R.string.latSearch),"0"));
        binding.editTextLongitut.setText(spSearch.getString(getString(R.string.lngSearch),"0"));
        binding.buttonSearch.setOnClickListener(clk -> {
            lat = binding.editTextLat.getText().toString();
            lng = binding.editTextLongitut.getText().toString();
            edit.putString(getString(R.string.latSearch),lat);
            edit.putString(getString(R.string.lngSearch),lng);
            edit.apply();
            search(lat,lng);

          });
        Toolbar toolbar = binding.toolbar;
        //toolbar.setLogo(R.drawable.sun);
        toolbar.setTitle(getString(R.string.Sunlookup)); // set the Name
        setSupportActionBar(toolbar);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        bottomNavigationView.setSelectedItemId(R.id.first_id);

        // Perform item selected listener
        bottomNavigationView.setOnItemSelectedListener(item ->
                {
                    int item_id = item.getItemId();
                    if ( item_id == R.id.home_id ) {
                        startActivity(new Intent(getApplicationContext(),MainActivity.class));
                    }
                    else if ( item_id == R.id.first_id ) {
                        return true;
                    }
                    else if ( item_id == R.id.second_id ) {
                        startActivity(new Intent(getApplicationContext(), RecipeSearchActivity.class));
                        return true;
                    }
                    else if ( item_id == R.id.third_id ) {
                        startActivity(new Intent(getApplicationContext(), Dictionary.class));
                        return true;
                    }
                    else if ( item_id == R.id.forth_id ) {
                        startActivity(new Intent(getApplicationContext(), DeezerActivity.class));
                        return true;
                    }
                    return false;
                }
        );

        binding.buttonSave.setOnClickListener(click ->{
            SunlookupData data = new SunlookupData(binding.editTextLat.getText().toString(),
                    binding.editTextLongitut.getText().toString(),true);
            messages.add(data);
            new Thread(() -> {sDAO.insertData(data);}).start();
            myAdapter.notifyItemInserted(messages.size()-1);
        });

        binding.recyclerView.setAdapter(myAdapter = new RecyclerView.Adapter<MyRowHolder>() {
            @NonNull
            @Override
            public MyRowHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                    SentMessageBinding binding = SentMessageBinding.inflate(getLayoutInflater());
                    return new MyRowHolder(binding.getRoot());
            }

            @Override
            public void onBindViewHolder(@NonNull MyRowHolder holder, int position) {
                String obj = messages.get(position).getLat();
                holder.latText.setText(obj);
                holder.lngText.setText(messages.get(position).getLongitude());
            }

            @Override
            public int getItemCount() {
                return messages.size();
            }
        });
        binding.buttonRead.setOnClickListener(clk ->{
            binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        });
    }
    @Override
    /**
     * Create a menu
     * @return true
     */
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.my_menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();
        if ( id ==  R.id.help) {
            AlertDialog.Builder builder1 = new AlertDialog.Builder(Sunlookup.this);
            builder1.setMessage(getString(R.string.sunlookup_information)+getString(R.string.sunlookup_information1)+getString(R.string.sunlookup_information2)+getString(R.string.sunlookup_information3));
            builder1.setTitle(getString(R.string.Sunlookup));

            builder1.create().show();
        }
        else if (id ==  R.id.home) {
            Toast.makeText(this, getString(R.string.back), Toast.LENGTH_SHORT).show();
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Inner class to create Row holder object
     */
    class MyRowHolder extends RecyclerView.ViewHolder {
        TextView latText;
        TextView lngText;

        /**
         * Row holder
         * @param itemView holds user select item view
         */
        public MyRowHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(clk ->{
                int position = getAbsoluteAdapterPosition();
                    SunlookupData s = messages.get(position);
                    lat = s.getLat();
                    lng = s.getLongitude();
                    search(lat,lng);
            });
            itemView.setOnLongClickListener(v -> {
                int position = getAbsoluteAdapterPosition();
                AlertDialog.Builder builder = new AlertDialog.Builder( Sunlookup.this );
                builder.setMessage(getString(R.string.want_to_delete));
                builder.setTitle(getString(R.string.question));
                builder.setPositiveButton(getString(R.string.no),(dialog,cl)->{});
                builder.setNegativeButton(getString(R.string.delete),(dialog,cl)->{
                    SunlookupData s = messages.get(position);
                    new Thread(() -> {sDAO.deleteData(s);}).start();
                    messages.remove(position);
                    myAdapter.notifyItemRemoved(position);
                    Snackbar.make(latText,getString(R.string.deleted_message)+position, Snackbar.LENGTH_LONG)
                            .setAction(getString(R.string.undo),click ->{
                                new Thread(() -> {sDAO.insertData(s);}).start();
                                messages.add(position,s);
                                myAdapter.notifyItemInserted(position); }).show();
                });
                builder.create().show();
                return true;
            });
            latText = itemView.findViewById(R.id.lat);
            lngText =itemView.findViewById(R.id.lng);
        }
    }

    /**
     * Search sunrise and sunset time
     * @param la lat
     * @param ln longitude
     */
    public void search(String la,String ln){
       try {
          double lat  = Double.parseDouble(la)%90;
          double lng  = Double.parseDouble(ln)%180;
          int z = (int)lng/15;
          String url = "https://api.sunrisesunset.io/json?lat=" +lat + "&lng="+lng+"&timezone=UTC"+z+"&date=today";

          JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                  (response) -> {
                      try {
                          JSONObject results = response.optJSONObject("results");
                          String sunrise = getString(R.string.sunrise)+ results.optString("sunrise");
                          String sunset= getString(R.string.sunset)+ results.optString("sunset");
                          binding.textViewRise.setText(sunrise);
                          binding.textViewSet.setText(sunset);
                          binding.editTextLat.setText(Double.toString(lat));
                          binding.editTextLongitut.setText(Double.toString(lng));
                      } catch (Exception e) {
                          e.printStackTrace();
                      }
                  },
                  (error) -> {
                      Log.e("error", "Error:" + error.getMessage());
                  });
          queue.add(request);

       }
       catch (Exception e) {
          Log.e("ex", "Error encoding city name");
       }


    }

}