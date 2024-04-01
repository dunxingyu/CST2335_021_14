package Zhihao;

// Import statements down below

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

/**
 * Data Access Object (DAO) for the recipes table. Defines methods for standard database
 * operations such as Insert, Delete, and Query, utilizing the Room Persistence Library
 * for SQL abstraction.
 *
 * This DAO provides an API for accessing Recipe entities within the database, including
 * operations to insert a new recipe, delete an existing recipe, retrieve all recipes,
 * and fetch a single recipe by its ID.
 *
 * Methods within this interface are utilized by the RecipeRepository and the ViewModel
 * which interact with the UI components. This abstraction facilitates testing and
 * maintenance by segregating the data access code.
 *
 * @author Zhihao Zhang
 * @version 1.0
 * @since 2024-04-02
 * @lab_section CST2335_021_14
 */
@Dao
public interface RecipeDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertRecipe(Recipe recipe);

    @Delete
    void deleteRecipe(Recipe recipe);

    @Query("SELECT * FROM recipes") // Make sure "recipes" matches your @Entity tableName
    List<Recipe> getAllRecipes();

    @Query("SELECT * FROM recipes WHERE id = :recipeId")
    Recipe getRecipeById(int recipeId);
}