package Zhihao;

// Import statements down below
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.college.converter.R;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Adapter for the RecyclerView to display a list of favorite recipes.
 * This adapter handles the binding of recipe data to the views within the RecyclerView.
 * It is responsible for creating ViewHolder instances for each item and binding the recipe data to these views.
 *
 * @author Zhihao Zhang
 * @version 1.0
 * @since 2024-04-02
 * @lab_section CST2335_021_14
 */
public class RecipeFavoriteAdapter extends RecyclerView.Adapter<RecipeFavoriteAdapter.RecipeViewHolder> {
    private Context context;
    private List<Recipe> recipeList;

    /**
     * Constructs the adapter with a given context and list of recipes.
     *
     * @param context The current context.
     * @param recipeList A list of recipes to be displayed.
     */
    public RecipeFavoriteAdapter(Context context, List<Recipe> recipeList) {
        this.context = context;
        this.recipeList = recipeList;
    }

    @NonNull
    @Override
    public RecipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.recipe_item, parent, false);
        return new RecipeViewHolder(view);
    }

    /**
     * Binds the data from the recipe list to the views in the ViewHolder.
     * This method sets up the title of the recipe, the image using Picasso for asynchronous loading,
     * and the click listener which opens the recipe details activity when a recipe item is clicked.
     *
     * @param holder   The ViewHolder which should be updated to represent the
     *                 contents of the item at the given position in the data set.
     * @param position The position of the item within the adapter's data set.
     */

    @Override
    public void onBindViewHolder(@NonNull RecipeViewHolder holder, int position) {
        Recipe recipe = recipeList.get(position);
        holder.titleTextView.setText(recipe.getTitle());
        holder.itemView.setOnClickListener(clk -> {
            Intent intent = new Intent(context, RecipeDetailsActivity.class);
            intent.putExtra("recipe", recipe);
            intent.putExtra("source", RecipeDetailsActivity.SOURCE_FAVORITE);
            context.startActivity(intent);
        });
        Picasso.get().load(recipe.getImageUrl()).into(holder.imageView);
        // Set other fields accordingly
    }

    /**
     * Returns the total number of items in the data set held by the adapter.
     *
     * @return The total number of items in this adapter.
     */
    @Override
    public int getItemCount() {
        return recipeList.size();
    }

    /**
     * Provides a reference to the type of views that the adapter uses.
     * It holds all the view components for the recipe item layout, allowing for each one to be manipulated programmatically.
     */
    public static class RecipeViewHolder extends RecyclerView.ViewHolder {
        TextView titleTextView;
        ImageView imageView;

        /**
         * Constructs the ViewHolder instance and binds the view components.
         *
         * @param itemView The view that the ViewHolder will manage.
         */
        public RecipeViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.recipeTitle);
            imageView = itemView.findViewById(R.id.recipeImage);
            // Initialize other views
        }
    }
}