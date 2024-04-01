package com.college.converter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.college.converter.R.layout;
import com.college.converter.R.string;
import com.college.converter.data.DictionaryDAO;
import com.college.converter.data.DictionaryDatabase;
import com.college.converter.data.DictionaryRecord;
import com.college.converter.data.DictionaryViewModel;
import com.college.converter.databinding.ActivityDictionaryBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import Zhihao.RecipeSearchActivity;
import chaowu.DeezerActivity;

/**
 * This application has an EditText for entering a word to look up the definition via internet.
 * User can see the definitions and save the search term and the definitions for viewing later.
 * Lab section: 021
 * Creation date: March 25, 2024
 * @author Qi Cheng
 * @version  1.0
 */
public class Dictionary extends AppCompatActivity {
    /**
     * This object is used to access views and elements binding in the activity.
     */
    private ActivityDictionaryBinding binding;
    /**
     * This stores ArrayList of DictionaryRecord object.
     */
    private ArrayList<DictionaryRecord>  records;
    /**
     * This stores String type ArrayList of each definition.
     */
    private ArrayList<String> resultRecord=new ArrayList<>();
    /**
     * An instance of DictionaryDAO.
     */
    private DictionaryDAO dictionaryDAO;
    /**
     * This adapter for the Search History RecyclerView in the activity.
     * The adapter is responsible for managing the data and views in the RecyclerView.
     */
    private RecyclerView.Adapter  myAdapter;
    /**
     * This adapter for the Definitions RecyclerView in the activity.
     * The adapter is responsible for managing the data and views in the RecyclerView.
     */
    private RecyclerView.Adapter  myAdapter2;
    /**
     * The SharedPreferences object stores and retrieves key-value pairs for the previous searching.
     */
    private SharedPreferences prefs;
    /**
     * This holds each definition of one word.
     */
    private String result;
    /**
     * This holds all of definitions of one word.
     */
    private String totalResult;
    /**
     * This represents the simple name of the class.
     */
    private final String TAG = getClass().getSimpleName();
    /**
     * This holds the prefix url for requesting.
     */
    private final  String URL_REQUEST = "https://api.dictionaryapi.dev/api/v2/entries/en/";
    /**
     * This is for formatting the result of StringRequest.
     */
    private final String front= "{\"words\":";
    /**
     * This is for formatting the result of StringRequest.
     */
    private final String back= "}";
    /**
     * This holds the content of editText .
     */
    private String myWord;
    /**
     * The request queue for listing and managing network requests.
     */
    private RequestQueue queue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        DictionaryViewModel dictionaryModel = new ViewModelProvider(this).get(DictionaryViewModel.class);

        binding = ActivityDictionaryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        DictionaryDatabase db= Room.databaseBuilder(getApplicationContext(), DictionaryDatabase.class, getString(string.dic_database_name))
                .build();
        dictionaryDAO = db.dicDAO();

        binding.toolbar.setTitle(getString(string.third));
        setSupportActionBar(binding.toolbar);

        records=dictionaryModel.records.getValue();

        queue= Volley.newRequestQueue(this);

        if (records == null) {
            dictionaryModel.records.postValue(records=new ArrayList<DictionaryRecord>());
            Executor thread = Executors.newSingleThreadExecutor();
            thread.execute(() ->
            {
                records.addAll( dictionaryDAO.getAllRecords() ); //get the data from database
                runOnUiThread( () ->  binding.recycleView.setAdapter( myAdapter )); //load the RecyclerView
            });
        }

        prefs = getSharedPreferences(getString(string.dic_sharedPreferences_name), Context.MODE_PRIVATE);
        String previous = prefs.getString(getString(string.word), "");
        binding.editTextWord.setText(previous);

        binding.recycleView.setAdapter(myAdapter=new RecyclerView.Adapter<MyRowHolder>() {
            @NonNull
            @Override
            public MyRowHolder onCreateViewHolder(ViewGroup parent, int viewType) {

                    LayoutInflater newRow = LayoutInflater.from(parent.getContext());
                    View thisRow = newRow.inflate(R.layout.word,parent,false);
                    return new MyRowHolder(thisRow);
            }

            @Override
            public void onBindViewHolder(@NonNull MyRowHolder holder, int position) {
                DictionaryRecord obj = records.get(position);
                holder.wordText.setText(obj.getWord());
            }

            @Override
            public int getItemCount() {
                return records.size();
            }
        });

        binding.recycleViewDef.setAdapter(myAdapter2=new RecyclerView.Adapter<MyRowHolder2>() {
            @NonNull
            @Override
            public MyRowHolder2 onCreateViewHolder(ViewGroup parent, int viewType) {

                LayoutInflater newRow = LayoutInflater.from(parent.getContext());
                View thisRow = newRow.inflate(layout.definition,parent,false);
                return new MyRowHolder2(thisRow);
            }

            @Override
            public void onBindViewHolder(@NonNull MyRowHolder2 holder, int position) {
                String obj = resultRecord.get(position);
                holder.resultText.setText(obj);
            }

            @Override
            public int getItemCount() {
                return resultRecord.size();
            }
        });

        binding.recycleViewDef.setLayoutManager(new LinearLayoutManager(this));

        binding.buttonSearch.setOnClickListener(click -> {
            myWord=binding.editTextWord.getText().toString();
            searchTerm(myWord);
        });

        binding.buttonSave.setOnClickListener(click -> {
            DictionaryRecord DR=new DictionaryRecord(myWord, totalResult);
            records.add(DR);
            new Thread(() -> {dictionaryDAO.insertRecord(DR);}).start();
            myAdapter.notifyItemInserted(records.size() - 1);
        });

        binding.buttonRead.setOnClickListener(click -> {
            binding.recycleView.setLayoutManager(new LinearLayoutManager(this));
        });

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.third_id);

        // Perform item selected listener
        bottomNavigationView.setOnItemSelectedListener(item -> {

            int item_id = item.getItemId();
            if ( item_id == R.id.home_id ) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            }
            else if (item_id == R.id.first_id) {
                startActivity(new Intent(getApplicationContext(), Sunlookup.class));
                return true;
            }
            else if ( item_id == R.id.second_id ) {
                startActivity(new Intent(getApplicationContext(), RecipeSearchActivity.class));
                return true;
            }
            else if ( item_id == R.id.third_id ) {
                return true;
            }
            else if ( item_id == R.id.forth_id ) {
                startActivity(new Intent(getApplicationContext(), DeezerActivity.class));
                return true;
            }
            return false;
        });
    }

    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.my_menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();
        if ( id ==  R.id.help) {
            AlertDialog.Builder builder1 = new AlertDialog.Builder(Dictionary.this);
            builder1.setMessage(getString(string.dictionary_information));
            builder1.setTitle(getString(string.dictionary_info_title));

            builder1.create().show();
        }
        else if (id ==  R.id.home) {
            Toast.makeText(this, getString(string.back), Toast.LENGTH_SHORT).show();
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * This class holds TextViews(for history of searched word) on a row:
     */
    class MyRowHolder extends RecyclerView.ViewHolder {
        /**
         * This is a textview holds the a searched word on a row:
         */
        TextView wordText;
        public MyRowHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(click -> {
                int position = getAbsoluteAdapterPosition();
                binding.editTextWord.setText(records.get(position).getWord());
                DictionaryRecord DRecord = records.get(position);
                showDefinitions(DRecord.getMeaning());
            });
            itemView.setOnLongClickListener(click -> {
                int position = getAbsoluteAdapterPosition();
                AlertDialog.Builder builder = new AlertDialog.Builder(Dictionary.this);
                builder.setMessage(getString(string.want_to_delete) + wordText.getText());
                builder.setTitle(getString(string.question));
                builder.setPositiveButton(getString(string.yes), (dialog, cl) -> {
                    DictionaryRecord removedRecord = records.get(position);
                    new Thread(() -> {dictionaryDAO.deleteRecord(removedRecord);}).start();
                    records.remove(position);
                    myAdapter.notifyItemRemoved(position);

                    String editText= binding.editTextWord.getText().toString();
                    String historyWord= wordText.getText().toString();
                    if(editText.equals(historyWord)){
                        binding.editTextWord.setText("");
                        resultRecord.clear();
                        myAdapter2.notifyDataSetChanged();
                    }

                    Snackbar.make(wordText,getString(string.deleted_message)+position, Snackbar.LENGTH_LONG)
                            .setAction(getString(string.undo), clk->{
                                new Thread(() -> {dictionaryDAO.insertRecord(removedRecord);}).start();
                                records.add(position,removedRecord);
                                myAdapter.notifyItemInserted(position);
                            }).show();
                    });
                builder.setNegativeButton(getString(string.no), (dialog, cl) -> {
                });
                builder.create().show();
                return false;
            });
            wordText = itemView.findViewById(R.id.record);
        }
   }
    /**
     * This class holds TextViews(for definition) on a row:
     */
    class MyRowHolder2 extends RecyclerView.ViewHolder {
        /**
         * This is a textview holds one definition on a row:
         */
        TextView resultText;
        public MyRowHolder2(@NonNull View itemView) {
            super(itemView);
            resultText = itemView.findViewById(R.id.record);
        }
    }
    /**
     * This method is for looking up the definitions based on the entered word by user via internet.
     * @param w The search term.
     */
    private void searchTerm(String w){

        try{
            if(!myWord.isEmpty()){
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString(getString(string.word), myWord);
                editor.apply();

                String url= URL_REQUEST + URLEncoder.encode(myWord,"UTF-8");

                StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                        (response) -> {
                            String jsonString = front + response + back;
                            StringBuilder resultBuilder = new StringBuilder();

                            try {
                                JSONObject jsonObject = new JSONObject(jsonString);
                                JSONArray wordsArray = jsonObject.getJSONArray("words");
                                for (int i = 0; i < wordsArray.length(); i++) {
                                    JSONObject wordsItem = wordsArray.getJSONObject(i);

                                    JSONArray meaningsArray = wordsItem.getJSONArray("meanings");

                                    for (int j = 0; j < meaningsArray.length(); j++) {
                                        JSONObject meaningsItem = meaningsArray.getJSONObject(j);

                                        JSONArray definitionsArray = meaningsItem.getJSONArray("definitions");
                                        for (int k = 0; k < definitionsArray.length(); k++) {
                                            JSONObject definitionsItem = definitionsArray.getJSONObject(k);
                                            result =  definitionsItem.getString("definition");
                                            resultBuilder.append(result); // Append current definition to StringBuilder
                                            resultBuilder.append("\n");
                                        }
                                    }
                                }
                                totalResult = resultBuilder.toString();
                                showDefinitions(totalResult);
                            }
                            catch (Exception e) {
                                e.printStackTrace();
                            }
                        },
                        (error) -> {
                            Log.e(TAG, "Error: " + error.getMessage());
                        });
                queue.add(stringRequest);
            }
            else{
                Toast.makeText(this, getString(string.error_msg), Toast.LENGTH_SHORT).show();
            }
        }
        catch (Exception e) {
            Log.e(TAG, "Error encoding word");
        }
    }

    /**
     * This method is for showing definitions of a word in recycleViewDef
     * @param meaning The definitions of the word
     */
    private void showDefinitions(String meaning) {
        //clear all item in recycleViewDef
        resultRecord.clear();
        myAdapter2.notifyDataSetChanged();
        //update recycleViewDef with current meaning
        String[] definitionsArray = meaning.split("\n");
        for (String stringResult : definitionsArray) {
            resultRecord.add(stringResult);
        }
        myAdapter2.notifyDataSetChanged();
    }
}
