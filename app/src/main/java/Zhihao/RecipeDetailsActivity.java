package Zhihao;
// Import statements down below

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.college.converter.R;
import com.college.converter.databinding.ActivityRecipeDetailsBinding;
import com.google.android.material.snackbar.Snackbar;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.Executors;

/**
 * Activity for displaying the details of a recipe.
 * This activity is responsible for fetching recipe information from an API or database,
 * displaying it, and providing options to add or remove a recipe from the user's favorites.
 *
 * The class also handles user interactions with the UI, such as button clicks and menu selections.
 * It uses the Volley library to make network requests and Picasso to load images.
 *
 * @author Zhihao Zhang
 * @version 1.0
 * @since 2024-04-02
 * @lab_section CST2335_021_14
 */
public class RecipeDetailsActivity extends AppCompatActivity {
    ActivityRecipeDetailsBinding binding;
    private RequestQueue queue;

    // Constant keys for identifying the source of the data
    public static final String SOURCE_LIST = "source_list";
    public static final String SOURCE_FAVORITE = "source_favorite";

    /**
     * Initializes the activity, sets up the view binding, and processes the intent data to
     * display the correct recipe information. If the recipe comes from the favorites list,
     * it displays the stored data. Otherwise, it fetches data from an external API.
     *
     * @param savedInstanceState Bundle object containing the activity's previously saved state.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRecipeDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        String source = getIntent().getStringExtra("source");
        int recipeId = getIntent().getIntExtra("recipeId", -1);
        if (SOURCE_FAVORITE.equals(source)) {
            Recipe recipe = (Recipe) getIntent().getSerializableExtra("recipe");
            assert recipe != null;
            binding.progressBar.setVisibility(View.GONE);
            binding.recipeTitle.setText(recipe.getTitle());
            binding.recipeSummary.setText(recipe.getSummary());
            binding.recipeSourceUrl.setText(recipe.getSpoonacularSourceUrl());
            Picasso.get().load(recipe.getImageUrl()).into(binding.recipeImage);
            binding.addFavroite.setText(R.string.remove_favorite);
            binding.addFavroite.setOnClickListener(clk -> removeFavoirte(recipe));
            return;
        }
        queue = Volley.newRequestQueue(this);
        binding.progressBar.setVisibility(View.VISIBLE);
        searchRecipe(recipeId);
    }

    /**
     * Removes a recipe from the favorites list using a background executor.
     * Displays a Snackbar message upon completion.
     *
     * @param recipe The recipe object to be removed from favorites.
     */
    private void removeFavoirte(Recipe recipe) {
        Executors.newSingleThreadExecutor().execute(() -> {
            RecipeDao recipeDao = RecipeDatabase.getDbInstance(RecipeDetailsActivity.this).recipeDao();
            recipeDao.deleteRecipe(recipe);
        });
        Snackbar.make(binding.getRoot(), "Recipe remove success", Snackbar.LENGTH_SHORT).show();
    }

    /**
     * Adds a recipe to the favorites list. Presents a confirmation dialog before adding.
     * Uses a background executor to insert the recipe into the database and displays
     * a Snackbar message upon completion.
     *
     * @param recipe The recipe object to be added to favorites.
     */
    private void addFavorite(Recipe recipe) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Do you want to add '" + recipe.getTitle() + "' to your Favorite List?")
                .setTitle("Question")
                .setNegativeButton("no", (dialog, cl) -> {})
                .setPositiveButton("yes", (dialog, cl) -> {
                    Executors.newSingleThreadExecutor().execute(() -> {
                        RecipeDao recipeDao = RecipeDatabase.getDbInstance(RecipeDetailsActivity.this).recipeDao();
                        recipeDao.insertRecipe(recipe);
                    });
                    Snackbar.make(binding.getRoot(), "Add success", Snackbar.LENGTH_SHORT).show();
                    binding.addFavroite.setText(R.string.favorite_has_added);
                }).create().show();
    }

    /**
     * Fetches the detailed information of a recipe based on its ID using a GET request.
     * Updates the UI elements with the fetched data upon successful response.
     *
     * @param query The unique ID of the recipe to fetch details for.
     */
    private void searchRecipe(int query) {
        if (query == -1) {
            Snackbar.make(binding.getRoot(), "Recipe not found", Snackbar.LENGTH_SHORT).show();
            return;
        }
        String apiKey = "7db24f50bd8c4927aff6c87ea850979b"; // Replace with your actual API key
        String url = "https://api.spoonacular.com/recipes/" + query + "/information?includeNutrition=false&apiKey=" + apiKey;

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                response -> {
                    try {
                        Recipe recipe = getRecipe(response);
                        binding.recipeTitle.setText(recipe.getTitle());
                        binding.recipeSummary.setText(recipe.getSummary());
                        binding.recipeSourceUrl.setText(recipe.getSpoonacularSourceUrl());
                        Picasso.get().load(recipe.getImageUrl()).into(binding.recipeImage);
                        binding.progressBar.setVisibility(View.GONE);
                        binding.addFavroite.setOnClickListener(clk -> addFavorite(recipe));
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(RecipeDetailsActivity.this, "An error occurred during parsing.", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> Toast.makeText(RecipeDetailsActivity.this, "Failed to fetch data.", Toast.LENGTH_SHORT).show());

        queue.add(stringRequest);
    }

    /**
     * Parses the JSON response from the API and returns a Recipe object.
     *
     * @param response The string representation of the JSON response.
     * @return A Recipe object with data extracted from the JSON response.
     * @throws JSONException If parsing the JSON data fails.
     */
    @NonNull
    private static Recipe getRecipe(String response) throws JSONException {
        JSONObject jsonObject = new JSONObject(response);
        Recipe recipe = new Recipe();
        recipe.setId(jsonObject.getInt("id"));
        recipe.setTitle(jsonObject.getString("title"));
        recipe.setImageUrl(jsonObject.getString("image"));
        recipe.setSummary(jsonObject.getString("summary"));
        recipe.setSourceUrl(jsonObject.getString("sourceUrl"));
        recipe.setSpoonacularSourceUrl(jsonObject.getString("spoonacularSourceUrl"));
        return recipe;
    }

    /**
     * Initializes the contents of the Activity's standard options menu.
     * This is only called once, the first time the options menu is displayed.
     *
     * @param menu The options menu in which items are placed.
     * @return true for the menu to be displayed; false it will not be shown.
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.my_menu, menu);
        return true;
    }

    /**
     * Handles action bar item clicks. The action bar will automatically handle clicks on the
     * Home/Up button, so long as a parent activity in AndroidManifest.xml is specified.
     *
     * @param item The menu item that was selected.
     * @return boolean Return false to allow normal menu processing to proceed,
     *         true to consume it here.
     */
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.help) {
            // Show help information
            Toast.makeText(this, getString(R.string.help_info), Toast.LENGTH_LONG).show();
            return true;
        } else if (item.getItemId() == R.id.home) {
            // Navigate to the home activity
            startActivity(new Intent(getApplicationContext(), RecipeSearchActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}