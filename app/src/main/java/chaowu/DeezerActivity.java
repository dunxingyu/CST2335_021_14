package chaowu;

import static android.content.ContentValues.TAG;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;


import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.college.converter.MainActivity;
import com.college.converter.R;
import com.college.converter.databinding.ActivityDeezerBinding;
import com.college.converter.databinding.ViewRowBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import data.SongViewModel;

public class DeezerActivity extends AppCompatActivity {

    ActivityDeezerBinding binding;
    SongViewModel songModel;

    ArrayList<Song> songs;

    ArrayList<String> songtest;
    private RecyclerView.Adapter myAdapter;
    SongDAO sDAO;
    protected RequestQueue queue;
    private static final String PREFS_NAME = "MyPrefs";
    private static final String SEARCH_TERM_KEY = "searchTerm";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDeezerBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        if (binding.editTextText != null) {
            SharedPreferences prefs = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
            String saveSearch = prefs.getString(SEARCH_TERM_KEY, null);
            binding.editTextText.setText(saveSearch);
        }
        songModel = new ViewModelProvider(this).get(SongViewModel.class);

        //go to favorite song page
        binding.button2.setOnClickListener(view -> {
            Intent nextPage = new Intent(DeezerActivity.this, ActivityFavorite.class);
            startActivity(nextPage);
        });

        //database
        SongDatabase db = Room.databaseBuilder(getApplicationContext(), SongDatabase.class, "database-name").build();
        sDAO = db.songDAO();

        songs = songModel.songs.getValue();
        if(songtest == null){

            fetchSong("q");
        }


        if(songs == null){
            songModel.songs.postValue( songs = new ArrayList<Song>());
            Executor thread = Executors.newSingleThreadExecutor();
            thread.execute(() ->
            {//Once you get the data from database
                songs.addAll( sDAO.getAllSongs() );

                runOnUiThread( () ->  binding.recycleView.setAdapter( myAdapter )); //You can then load the RecyclerView
            });
        }

        binding.button.setOnClickListener(click -> {
            String input = binding.editTextText.getText().toString();
            fetchSong(input);
            SharedPreferences.Editor editor = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE).edit();
            editor.putString(SEARCH_TERM_KEY, binding.editTextText.getText().toString());
            editor.apply();

        });

        binding.recycleView.setLayoutManager(new LinearLayoutManager(this));
        binding.recycleView.setAdapter(myAdapter = new RecyclerView.Adapter<MyRowHolder>() {
            @NonNull
            @Override
            public MyRowHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                ViewRowBinding binding = ViewRowBinding.inflate(getLayoutInflater(),parent,false);
                return new MyRowHolder(binding.getRoot());
            }

            @Override
            public void onBindViewHolder(@NonNull MyRowHolder holder, int position) {
               String obj = songs.get(position).getTitle();
                //String testname = songtest.get(position).toString();
                //holder.timeText.setText(messages.get(position).getTimeSpent());
                holder.rowitem.setText(obj);
                //holder.rowitem.setText(testname);

            }

            @Override
            public int getItemViewType(int position){
                return 0;
            }

            @Override
            public int getItemCount() {
                return songs.size();
            }
        });
    }
    class MyRowHolder extends RecyclerView.ViewHolder{
        TextView rowitem;



        public MyRowHolder(@NonNull View itemView){
            super(itemView);
            itemView.setOnClickListener(clk ->{
                int position = getAbsoluteAdapterPosition();
                Executor thread = Executors.newSingleThreadExecutor();
                AlertDialog.Builder builder = new AlertDialog.Builder( DeezerActivity.this );
                builder.setMessage("Do you want to add '"+ rowitem.getText().toString()+"' to your Favorite List?")
                        .setTitle("Question")
                        .setNegativeButton("no",(dialog, cl)->{}).
                        setPositiveButton("yes",(dialog, cl)->{
                            thread.execute(() ->
                            {
                                sDAO.insertSong(songs.get(position));
                                //song.id=(int)id;
                            });
                            myAdapter.notifyItemInserted(songs.size()-1);

                        }).create().show();
            });
            rowitem = itemView.findViewById(R.id.rowitem);

        }
    }

    public void fetchSong(String key){
        songtest = new ArrayList<>();
        songs = new ArrayList<>();
        queue = Volley.newRequestQueue(this);
        String url = "https://api.deezer.com/search/artist/?q="+key;
        // Request for searching artists
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Log.d(TAG, "Response received from Deezer API. Tracklist URL: ");
                            JSONArray data = response.getJSONArray("data");
                            if (data.length() > 0) {
                                // Get the first artist
                                JSONObject artist = data.getJSONObject(0);

                                // Get the artist's tracklist URL
                                String tracklistUrl = artist.getString("tracklist");

                                // Fetch the tracklist
                                fetchTracklist(tracklistUrl);
                            } else {
                                Toast.makeText(DeezerActivity.this, "No artist found!", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, "Error: " + error.getMessage());
                    }
                });

        // Add the request to the RequestQueue.
        queue.add(jsonObjectRequest);

    }
    private void fetchTracklist(String tracklistUrl) {
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);

        // Request for fetching the tracklist
        JsonObjectRequest tracklistRequest = new JsonObjectRequest
                (Request.Method.GET, tracklistUrl, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray tracks = response.getJSONArray("data");
                            for(int i=0;i<tracks.length();i++){
                                JSONObject songItem = tracks.getJSONObject(i);
                                // Process the tracklist here
                                //get title and duration
                                String title2 = songItem.getString("title");
                                String duration = songItem.getString("duration");
                                //get album object
                                JSONObject albumObject = songItem.getJSONObject("album");
                                //get title in album object
                                String albumTitle = albumObject.getString("title");
                                //get cover
                                String cover = albumObject.getString("cover");
                                Song song = new Song(title2, duration, albumTitle,cover);
                                songtest.add(title2);
                                songs.add(song);
                                myAdapter.notifyDataSetChanged();
                            }

                            Log.d(TAG, "Tracklist: " + tracks.toString());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, "Error: " + error.getMessage());
                    }
                });

        // Add the request to the RequestQueue.
        queue.add(tracklistRequest);
    }


}