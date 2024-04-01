package Zhihao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface RecipeDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    static void insertRecipe(Recipe recipe) {

    }

    @Delete
    void deleteRecipe(Recipe recipe);

    @Query("SELECT * FROM recipes") // Make sure "recipes" matches your @Entity tableName
    LiveData<List<Recipe>> getAllRecipes();

    @Query("SELECT * FROM recipes WHERE id = :recipeId")
    LiveData<Recipe> getRecipeById(int recipeId);
}