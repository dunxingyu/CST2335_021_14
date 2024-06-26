/**
 * The ActivityFavorite class represents the activity where users can view their favorite songs.
 * Users can see a list of their favorite songs, and they can also delete songs from the list.
 */
package chaowu;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.android.volley.RequestQueue;
import com.college.converter.Dictionary;
import com.college.converter.MainActivity;
import com.college.converter.R;
import com.college.converter.Sunlookup;
import com.college.converter.databinding.ActivityFavoriteBinding;
import com.college.converter.databinding.ViewDatabaseBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import Zhihao.RecipeSearchActivity;
import data.SongViewModel;
/**
 * Represents the activity where users can view their favorite songs and perform actions on them.
 */
public class ActivityFavorite extends AppCompatActivity {

    ActivityFavoriteBinding binding;
    SongViewModel songModel;

    ArrayList<Song> songs;

    ArrayList<String> songtest;
    private RecyclerView.Adapter myAdapter;
    SongDAO sDAO;
    protected RequestQueue queue;

    /**
     * Initializes the activity when created.
     *
     * @param savedInstanceState The saved instance state bundle.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityFavoriteBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.toolbar.setTitle(getString(R.string.forth));
        setSupportActionBar(binding.toolbar);

        songModel = new ViewModelProvider(this).get(SongViewModel.class);


        //database
        SongDatabase db = Room.databaseBuilder(getApplicationContext(), SongDatabase.class, "database-name").build();
        sDAO = db.songDAO();

        songs = songModel.songs.getValue();

        /////
        if(songs == null){
            songModel.songs.postValue( songs = new ArrayList<Song>());
            Executor thread = Executors.newSingleThreadExecutor();
            thread.execute(() ->
            {
                songs.addAll( sDAO.getAllSongs() ); //Once you get the data from database

                runOnUiThread( () ->  binding.recycle2.setAdapter( myAdapter )); //You can then load the RecyclerView
            });
        }


        binding.recycle2.setLayoutManager(new LinearLayoutManager(this));
        binding.recycle2.setAdapter(myAdapter = new RecyclerView.Adapter<ActivityFavorite.MyRowHolder>() {
            @NonNull
            @Override
            public ActivityFavorite.MyRowHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                ViewDatabaseBinding binding = ViewDatabaseBinding.inflate(getLayoutInflater(),parent,false);
                return new ActivityFavorite.MyRowHolder(binding.getRoot());
            }

            @Override
            public void onBindViewHolder(@NonNull ActivityFavorite.MyRowHolder holder, int position) {
                String obj = songs.get(position).getTitle();

                holder.rowitem2.setText(obj);


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
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.forth_id);

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
                startActivity(new Intent(getApplicationContext(), Dictionary.class));

                return true;
            }
            else if ( item_id == R.id.forth_id ) {
                startActivity(new Intent(getApplicationContext(), DeezerActivity.class));
                return true;
            }
            return false;
        });
    }

    /**
     * Inflates the options menu.
     *
     * @param menu The menu to inflate.
     * @return true if the menu is inflated successfully, false otherwise.
     */
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.my_menu, menu);
        return true;
    }
    /**
     * Handles options menu item selection.
     *
     * @param item The selected menu item.
     * @return true if the menu item is handled successfully, false otherwise.
     */
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();
        if ( id ==  R.id.help) {
            androidx.appcompat.app.AlertDialog.Builder builder1 = new androidx.appcompat.app.AlertDialog.Builder(ActivityFavorite.this);
            builder1.setMessage(getString(R.string.deezer_information));
            builder1.setTitle(getString(R.string.deezer_info_title));

            builder1.create().show();
        }
        else if (id ==  R.id.home) {
            Toast.makeText(this, getString(R.string.back), Toast.LENGTH_SHORT).show();
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }
    /**
     * Represents a row holder for the RecyclerView.
     */
    class MyRowHolder extends RecyclerView.ViewHolder{
        TextView rowitem2;
        Button button;


        /**
         * Constructs a new MyRowHolder with the given itemView.
         *
         * @param itemView The view for the row.
         */
        public MyRowHolder(@NonNull View itemView){
            super(itemView);
            itemView.setOnClickListener(clk ->{
                int position = getAbsoluteAdapterPosition();
                //Executor thread = Executors.newSingleThreadExecutor();
                Song selectedSong = songs.get(position);
                String title = selectedSong.getTitle();
                String duration = selectedSong.getDuration();
                String album = selectedSong.getName();
                String cover = selectedSong.getCover();

                Intent intent = new Intent(itemView.getContext(), DetailSong.class);
                intent.putExtra("title",title);
                intent.putExtra("duration", duration);
                intent.putExtra("album",album);
                intent.putExtra("cover", cover);
                itemView.getContext().startActivity(intent);


            });
            rowitem2 = itemView.findViewById(R.id.textView);
            button = itemView.findViewById(R.id.button3);
            button.setOnClickListener(v -> {
                int position = getAbsoluteAdapterPosition();
                Executor thread = Executors.newSingleThreadExecutor();
                Song deletedSong = songs.get(position); // Get the song to be deleted
                thread.execute(() -> {
                    sDAO.deleteSong(deletedSong); // Delete the song from the database
                });
                songs.remove(position); // Remove the song from the list
                myAdapter.notifyItemRemoved(position); // Notify adapter about the removal
                Snackbar.make(rowitem2,getString(R.string.chao_delete)+deletedSong.getTitle(),Snackbar.LENGTH_LONG).
                        setAction(R.string.undo, click -> {
                            songs.add(position,deletedSong);
                            Executor thread2 = Executors.newSingleThreadExecutor();
                            thread2.execute(() ->
                            {
                                sDAO.insertSong(deletedSong);

                            });
                            myAdapter.notifyItemInserted(position);
                        }).show();
            });

        }
    }

}
