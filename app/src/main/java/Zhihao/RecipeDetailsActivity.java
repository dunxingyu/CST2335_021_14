package Zhihao;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.college.converter.Dictionary;
import com.college.converter.FirstActivity;
import com.college.converter.MainActivity;
import com.college.converter.R;
import com.college.converter.SecondActivity;
import com.college.converter.databinding.ActivityRecipeDetailsBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import chaowu.DeezerActivity;

public class RecipeDetailsActivity extends AppCompatActivity {
    ActivityRecipeDetailsBinding binding;
    private RequestQueue queue;

    public static final String SOURCE_LIST = "source_list";
    public static final String SOURCE_FAVORITE = "source_favorite";
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

    private void removeFavoirte(Recipe recipe) {
        Executors.newSingleThreadExecutor().execute(() -> {
            RecipeDao recipeDao = RecipeDatabase.getDbInstance(RecipeDetailsActivity.this).recipeDao();
            recipeDao.deleteRecipe(recipe);
        });
        Snackbar.make(binding.getRoot(), "Recipe remove success", Snackbar.LENGTH_SHORT).show();
    }

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.my_menu, menu);
        return true;
    }

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