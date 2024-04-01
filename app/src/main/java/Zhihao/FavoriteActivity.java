package Zhihao;
// Import statements down below
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
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
import com.college.converter.databinding.RecipeItemBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import chaowu.DeezerActivity;
import chaowu.Song;
import chaowu.SongDatabase;
import data.RecipeViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
/**
 * Represents an activity for displaying a list of favorite recipes.
 * This class manages the user interface for the user's favorite recipes,
 * handling the display and interaction with a RecyclerView of recipes.
 * It provides navigation to other activities through a BottomNavigationView.
 *
 * @author Zhihao Zhang
 * @version 1.0
 * @since 2024-04-02
 * @lab_section CST2335_021_14
 */
public class FavoriteActivity extends AppCompatActivity {

    private RecyclerView recipesRecyclerView;
    List<Recipe> recipes = new ArrayList<>();

    private Toolbar toolbar;

    private RecipeDao recipeDao;

    private RecipeFavoriteAdapter recipeAdapter;

    /**
     * Initializes the activity, setting up the toolbar, RecyclerView, and
     * BottomNavigationView. It also initializes the database connection and
     * loads the list of favorite recipes.
     *
     * @param savedInstanceState If the activity is being re-initialized after
     *                           previously being shut down then this Bundle
     *                           contains the data it most recently supplied in
     *                           onSaveInstanceState(Bundle). Otherwise it is null.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite_food);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle(R.string.app_name);

        recipesRecyclerView = findViewById(R.id.recycle_food);

        recipesRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        //database
        recipeDao = RecipeDatabase.getDbInstance(this).recipeDao();

        recipeAdapter = new RecipeFavoriteAdapter(this, recipes);

        refreshList();
        // Now setup the BottomNavigationView
        setupBottomNavigationView(); // Add this line
    }

    /**
     * Sets up the bottom navigation view and item selection handling, providing
     * navigation to other activities within the application.
     */
    protected void setupBottomNavigationView() {
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.second_id);

        // Perform item selected listener
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.home_id) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            } else if (itemId == R.id.first_id) {
                startActivity(new Intent(getApplicationContext(), Sunlookup.class));
            } else if (itemId == R.id.second_id) {
                startActivity(new Intent(getApplicationContext(), RecipeSearchActivity.class));
            } else if (itemId == R.id.third_id) {
                startActivity(new Intent(getApplicationContext(), Dictionary.class));
            } else if (itemId == R.id.forth_id) {
                startActivity(new Intent(getApplicationContext(), DeezerActivity.class));
            }
            return true; // Return true to display the item as the selected item
        });
    }

    /**
     * Refreshes the list of favorite recipes by retrieving the latest data
     * from the database and updating the adapter for the RecyclerView.
     */
    private void refreshList() {
        recipes.clear();
        Executors.newSingleThreadExecutor().execute(() -> {
            if (recipeDao.getAllRecipes() != null) {
                recipes.addAll( recipeDao.getAllRecipes() ); //Once you get the data from database
            }
            runOnUiThread( () ->  recipesRecyclerView.setAdapter( recipeAdapter )); //You can then load the RecyclerView
        });
    }

    /**
     * Called when the activity has been stopped, before it is started again.
     * It triggers a refresh of the recipe list to ensure the UI is up to date
     * with the latest changes.
     */
    @Override
    protected void onRestart() {
        super.onRestart();
        refreshList();
    }
}