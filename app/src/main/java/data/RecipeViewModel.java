package data;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;

import Zhihao.Recipe;

public class RecipeViewModel extends ViewModel {
    public MutableLiveData<ArrayList<Recipe>> recipes = new MutableLiveData<>();

    public RecipeViewModel() {
        recipes.setValue(new ArrayList<>()); // Initialize with an empty list
    }

    // Add methods to manipulate recipes as needed, for example:

    // Add a recipe
    public void addRecipe(Recipe recipe) {
        ArrayList<Recipe> currentRecipes = recipes.getValue();
        if (currentRecipes != null) {
            currentRecipes.add(recipe);
            recipes.setValue(currentRecipes); // Trigger observers
        }
    }

    // Remove a recipe
    public void removeRecipe(Recipe recipe) {
        ArrayList<Recipe> currentRecipes = recipes.getValue();
        if (currentRecipes != null && currentRecipes.remove(recipe)) {
            recipes.setValue(currentRecipes); // Trigger observers
        }
    }

    // Get all recipes
    public MutableLiveData<ArrayList<Recipe>> getAllRecipes() {
        return recipes;
    }
}
