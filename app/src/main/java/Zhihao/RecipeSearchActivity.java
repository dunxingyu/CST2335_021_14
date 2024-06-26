package Zhihao;
// Import statements down below

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.college.converter.Dictionary;
import com.college.converter.MainActivity;
import com.college.converter.R;
import com.college.converter.Sunlookup;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import chaowu.DeezerActivity;

/**
 * This activity allows users to search for recipes using an external API. It displays
 * search results in a RecyclerView. Users can also navigate to the details of a specific
 * recipe, view their favorite recipes, and save their last search term for convenience.
 *
 * Features include:
 * - Searching for recipes by name.
 * - Displaying search results in a list.
 * - Saving the last search term in SharedPreferences.
 * - Navigating to recipe details.
 * - Viewing saved favorite recipes.
 *
 * @author Zhihao Zhang
 * @version 1.0
 * @since 2024-04-02
 */
public class RecipeSearchActivity extends AppCompatActivity {
    // Variable declarations, including UI components and data structures for managing recipes
    private static final String PREFS_NAME = "RecipePrefs";
    private static final String SEARCH_TERM_KEY = "recipeSearchTerm";

    private EditText searchEditText;
    private Button searchButton;
    private RecyclerView recipesRecyclerView;
    private List<Recipe> recipeList = new ArrayList<>();
    private RecipeAdapter adapter;
    private RequestQueue queue;
    private Toolbar toolbar;
    protected BottomNavigationView bottomNavigationView;

    private Button favoriteRecipes;

    /**
     * Initializes the activity, its views, and functionalities. It sets up the RecyclerView
     * for displaying search results and configures the search functionality, including saving
     * and retrieving the last search term from SharedPreferences.
     *
     * @param savedInstanceState If the activity is being re-initialized after
     *                           previously being shut down then this Bundle contains the data it most
     *                           recently supplied in onSaveInstanceState(Bundle). Otherwise, it is null.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_search);

        // Initialize the toolbar and set it as the app bar for the activity
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Initialize toolbar
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle(R.string.second);

        searchEditText = findViewById(R.id.searchEditText);
        searchButton = findViewById(R.id.search_button);
        recipesRecyclerView = findViewById(R.id.recipesRecyclerView);

        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        searchEditText.setText(prefs.getString(SEARCH_TERM_KEY, ""));

        recipesRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new RecipeAdapter(this, recipeList);
        recipesRecyclerView.setAdapter(adapter);

        queue = Volley.newRequestQueue(this);

        searchButton.setOnClickListener(view -> {
            String query = searchEditText.getText().toString().trim();
            if (!query.isEmpty()) {
                searchRecipes(query);
                prefs.edit().putString(SEARCH_TERM_KEY, query).apply();
            } else {
                Toast.makeText(RecipeSearchActivity.this, R.string.enter_search_term, Toast.LENGTH_SHORT).show();
            }
        });

        favoriteRecipes = findViewById(R.id.favorite_recipes);
        favoriteRecipes.setOnClickListener(view -> {
        });
        favoriteRecipes.setOnClickListener(view -> {
            startActivity(new Intent(getApplicationContext(), FavoriteActivity.class));
        });
        // Now setup the BottomNavigationView
        setupBottomNavigationView(); // Add this line


    }
//    class MyRowHolder extends RecyclerView.ViewHolder {
//        TextView rowitem;
//
//        public MyRowHolder(@NonNull View itemView, RecipeAdapter adapter) { // Pass the adapter as a parameter
//            super(itemView);
//            itemView.setOnClickListener(clk -> {
//                int position = getAbsoluteAdapterPosition();
//                Executor thread = Executors.newSingleThreadExecutor();
//                AlertDialog.Builder builder = new AlertDialog.Builder(itemView.getContext());
//                builder.setMessage("Do you want to add '" + rowitem.getText().toString() + "' to your Favorite List?")
//                        .setTitle("Question")
//                        .setNegativeButton("no", (dialog, cl) -> {})
//                        .setPositiveButton("yes", (dialog, cl) -> {
//                            thread.execute(() -> {
//                                // Assuming you have a method to add a recipe to favorites
//                                // This is a placeholder; replace with your actual method to insert into the database
//                                // Also ensure that RecipeDao and its method to insert a recipe are correctly implemented
//                                Recipe recipe = recipeList.get(position);
//                                // RecipeDao.insertRecipe(recipe); // This should be your method call to insert
//                                // Assuming you have access to update the UI after insertion
//                                itemView.post(() -> adapter.notifyItemInserted(recipeList.size() - 1));
//                            });
//                        }).create().show();
//            });
//            rowitem = itemView.findViewById(R.id.rowitem);
//        }
//    }


    /**
     * Sets up the bottom navigation view and its item selection behavior, allowing
     * navigation between different sections of the application.
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
                // Current activity, do nothing or handle accordingly
            } else if (itemId == R.id.third_id) {
                startActivity(new Intent(getApplicationContext(), Dictionary.class));
            } else if (itemId == R.id.forth_id) {
                startActivity(new Intent(getApplicationContext(), DeezerActivity.class));
            }
            return true; // Return true to display the item as the selected item
        });
    }

    /**
     * Inflates the menu options for the activity from a menu resource.
     *
     * @param menu The options menu in which you place your items.
     * @return You must return true for the menu to be displayed; if you return false it will not be shown.
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.my_menu, menu);
        return true;
    }

    /**
     * Handles action bar item clicks here. The action bar will automatically handle clicks
     * on the Home/Up button, so long as you specify a parent activity in AndroidManifest.xml.
     *
     * @param item The menu item that was selected.
     * @return boolean Return false to allow normal menu processing to proceed,
     * true to consume it here.
     */
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.help) {
            androidx.appcompat.app.AlertDialog.Builder builder1 = new androidx.appcompat.app.AlertDialog.Builder(RecipeSearchActivity.this);
            builder1.setMessage(getString(R.string.recipe_search_information));
            builder1.setTitle(getString(R.string.recipe_search_info_title));

            builder1.create().show();
        } else if (id == R.id.home) {
            Toast.makeText(this, getString(R.string.back), Toast.LENGTH_SHORT).show();
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }


    /**
     * Performs the recipe search using the specified query string. It sends a request to
     * the external API and processes the response to update the UI with the search results.
     *
     * @param query The search term used to query the recipe API.
     */
    private void searchRecipes(String query) {
        String apiKey = "7db24f50bd8c4927aff6c87ea850979b"; // Replace with your actual API key
        String url = "https://api.spoonacular.com/recipes/complexSearch?query=" + query + "&apiKey=" + apiKey;

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                response -> {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        JSONArray jsonArray = jsonObject.getJSONArray("results");
                        recipeList.clear();

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject recipeObject = jsonArray.getJSONObject(i);
                            String title = recipeObject.getString("title");
                            String imageUrl = recipeObject.getString("image");
                            int id = recipeObject.getInt("id");

                            Recipe recipe = new Recipe(id, title, imageUrl);
                            recipe.setId(id);
                            recipeList.add(recipe);
                        }
                        adapter.notifyDataSetChanged();
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(RecipeSearchActivity.this, "An error occurred during parsing.", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> Toast.makeText(RecipeSearchActivity.this, "Failed to fetch data.", Toast.LENGTH_SHORT).show());

        queue.add(stringRequest);
    }
}
