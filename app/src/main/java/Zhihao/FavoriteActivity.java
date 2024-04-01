package Zhihao;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.college.converter.R;
import com.college.converter.databinding.ActivityFavoriteBinding;
import com.college.converter.databinding.RecipeItemBinding;
import data.RecipeViewModel;

import java.util.List;

public class FavoriteActivity extends AppCompatActivity {

    private ActivityFavoriteBinding binding;
    private RecipeViewModel recipeModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityFavoriteBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.toolbar);

        recipeModel = new ViewModelProvider(this).get(RecipeViewModel.class);

        binding.recycle2.setLayoutManager(new LinearLayoutManager(this));
        recipeModel.getAllRecipes().observe(this, recipes -> {
            RecipeAdapter adapter = new RecipeAdapter(recipes);
            binding.recycle2.setAdapter(adapter);
        });
    }

    private class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder> {
        private final List<Recipe> recipes;

        RecipeAdapter(List<Recipe> recipes) {
            this.recipes = recipes;
        }

        @NonNull
        @Override
        public RecipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            RecipeItemBinding binding = RecipeItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
            return new RecipeViewHolder(binding);
        }

        @Override
        public void onBindViewHolder(@NonNull RecipeViewHolder holder, int position) {
            Recipe recipe = recipes.get(position);
            holder.binding.recipeTitle.setText(recipe.getTitle());
            // Set up click listeners or other event handlers
        }

        @Override
        public int getItemCount() {
            return recipes.size();
        }

        class RecipeViewHolder extends RecyclerView.ViewHolder {
            RecipeItemBinding binding;

            RecipeViewHolder(RecipeItemBinding binding) {
                super(binding.getRoot());
                this.binding = binding;
            }
        }
    }
}
