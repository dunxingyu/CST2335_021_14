package Zhihao;

// Import statements down below
import android.content.Intent;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.college.converter.Dictionary;
import com.college.converter.MainActivity;
import com.college.converter.R;
import com.college.converter.Sunlookup;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.Serializable;

import chaowu.DeezerActivity;

/**
 * Entity representing a recipe. This class holds information about a recipe
 * including its ID, title, image URL, summary, and source URLs. It is used
 * with Room to store and retrieve recipe data from the database.
 *
 * @author Zhihao Zhang
 * @version 1.0
 * @since 2024-04-02
 * @lab_section CST2335_021_14
 */
@Entity(tableName = "recipes")
public class Recipe implements Serializable {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    protected int id;

    @ColumnInfo(name = "recipeId")
    protected int recipeId;

    @ColumnInfo(name = "title")
    protected String title;

    @ColumnInfo(name = "image_url")
    protected String imageUrl;

    @ColumnInfo(name = "summary")
    protected String summary;

    @ColumnInfo(name = "source_url")
    protected String sourceUrl;

    @ColumnInfo(name = "spoonacular_source_url")
    protected String spoonacularSourceUrl;

    /**
     * Default constructor used by Room to create Recipe instances.
     */
    public Recipe() {}

    /**
     * Constructor used for creating a Recipe instance when fetching from an API.
     *
     * @param recipeId The unique identifier for the recipe.
     * @param title    The title of the recipe.
     * @param imageUrl The URL to the image of the recipe.
     */
    public Recipe(int recipeId, String title, String imageUrl) {
        this.recipeId = recipeId;
        this.title = title;
        this.imageUrl = imageUrl;
    }

    /**
     * Full constructor used by Room to create instances with all fields.
     *
     * @param id                   The unique identifier for the recipe.
     * @param title                The title of the recipe.
     * @param imageUrl             The URL to the image of the recipe.
     * @param summary              A summary description of the recipe.
     * @param sourceUrl            The URL to the source of the recipe.
     * @param spoonacularSourceUrl The URL to the Spoonacular source of the recipe.
     */
    @Ignore
    public Recipe(int id, String title, String imageUrl, String summary, String sourceUrl, String spoonacularSourceUrl) {
        this.id = id;
        this.title = title;
        this.imageUrl = imageUrl;
        this.summary = summary;
        this.sourceUrl = sourceUrl;
        this.spoonacularSourceUrl = spoonacularSourceUrl;
    }


    // Getters and setters...

    /**
     * Gets the unique identifier for the recipe.
     * @return the unique identifier.
     */
    public int getId() { return id; }
    /**
     * Sets the unique identifier for the recipe.
     * @param id the unique identifier.
     */
    public void setId(int id) { this.id = id; }

    /**
     * Retrieves the recipe ID from an external API or source.
     *
     * @return The external recipe ID.
     */
    public int getRecipeId() {
        return recipeId;
    }

    /**
     * Sets the recipe ID from an external API or source.
     *
     * @param recipeId The new external recipe ID.
     */
    public void setRecipeId(int recipeId) {
        this.recipeId = recipeId;
    }

    /**
     * Retrieves the title of the recipe.
     *
     * @return The title of the recipe.
     */
    public String getTitle() { return title; }

    /**
     * Sets the title of the recipe.
     *
     * @param title The new title of the recipe.
     */
    public void setTitle(String title) { this.title = title; }

    /**
     * Retrieves the URL to the image of the recipe.
     *
     * @return The URL to the image of the recipe.
     */
    public String getImageUrl() { return imageUrl; }

    /**
     * Sets the URL to the image of the recipe.
     *
     * @param imageUrl The new URL to the image of the recipe.
     */
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    /**
     * Retrieves the summary description of the recipe.
     *
     * @return The summary description of the recipe.
     */
    public String getSummary() { return summary; }

    /**
     * Sets the summary description of the recipe.
     *
     * @param summary The new summary description of the recipe.
     */
    public void setSummary(String summary) { this.summary = summary; }

    /**
     * Retrieves the URL to the original source of the recipe.
     *
     * @return The URL to the original source of the recipe.
     */
    public String getSourceUrl() { return sourceUrl; }

    /**
     * Sets the URL to the original source of the recipe.
     *
     * @param sourceUrl The new URL to the original source of the recipe.
     */
    public void setSourceUrl(String sourceUrl) { this.sourceUrl = sourceUrl; }


    /**
     * Sets the Spoonacular source URL of the recipe.
     *
     * @param spoonacularSourceUrl The new Spoonacular source URL of the recipe.
     */
    public void setSpoonacularSourceUrl(String spoonacularSourceUrl) {
        this.spoonacularSourceUrl = spoonacularSourceUrl;
    }

    /**
     * Retrieves the Spoonacular source URL of the recipe.
     *
     * @return The Spoonacular source URL of the recipe.
     */
    public String getSpoonacularSourceUrl() {
        return spoonacularSourceUrl;
    }
}