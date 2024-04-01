package Zhihao;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.college.converter.R;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import chaowu.SongDatabase;

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder> {
    private Context context;
    private List<Recipe> recipeList;

    private RecipeDao recipeDao;

    public RecipeAdapter(Context context, List<Recipe> recipeList) {
        this.context = context;
        this.recipeList = recipeList;
        recipeDao = RecipeDatabase.getDbInstance(context).recipeDao();
    }

    @NonNull
    @Override
    public RecipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.recipe_item, parent, false);
        return new RecipeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeViewHolder holder, int position) {
        Recipe recipe = recipeList.get(position);
        holder.titleTextView.setText(recipe.getTitle());
        holder.itemView.setOnClickListener(clk -> {
            Intent intent = new Intent(context, RecipeDetailsActivity.class);
            intent.putExtra("source", RecipeDetailsActivity.SOURCE_LIST);
            intent.putExtra("recipeId", recipe.recipeId);
            intent.putExtra("title", recipe.title);
            intent.putExtra("image_url", recipe.imageUrl);
            intent.putExtra("summary", recipe.summary);
            intent.putExtra("source_url", recipe.sourceUrl);
            context.startActivity(intent);
        });
        Picasso.get().load(recipe.getImageUrl()).into(holder.imageView);
        // Set other fields accordingly
    }

    @Override
    public int getItemCount() {
        return recipeList.size();
    }

    public static class RecipeViewHolder extends RecyclerView.ViewHolder {
        TextView titleTextView;
        ImageView imageView;

        public RecipeViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.recipeTitle);
            imageView = itemView.findViewById(R.id.recipeImage);
            // Initialize other views
        }
    }
}