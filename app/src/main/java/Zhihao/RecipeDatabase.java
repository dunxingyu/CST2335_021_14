package Zhihao;

// Import statements down below
import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

/**
 * Main database class for the application using the Room persistence library.
 * It includes definitions for the database's version, schema information, and
 * entities associated with it, specifically the {@link Recipe} class.
 *
 * This abstract class provides the application with instances of the Dao
 * interfaces that access the database.
 *
 * The database uses a singleton pattern to prevent having multiple instances
 * of the database opened at the same time.
 *
 * @author Zhihao Zhang
 * @version 1.0
 * @since 2024-04-02
 * @lab_section CST2335_021_14
 */
@Database(entities = {Recipe.class}, version = 1, exportSchema = false)
public abstract class RecipeDatabase extends RoomDatabase {

    /**
     * Gets the DAO (Data Access Object) for the database.
     * The DAO contains methods to perform read/write operations on the database.
     *
     * @return The DAO for accessing recipes.
     */
    public abstract RecipeDao recipeDao();

    // Single instance of the RecipeDatabase
    private static volatile RecipeDatabase INSTANCE;

    /**
     * Retrieves the single instance of the database for the application's context.
     * If the instance does not exist, it synchronously creates the database.
     *
     * @param context The context of the application used to construct the database.
     * @return The single instance of the RecipeDatabase.
     */
    public static RecipeDatabase getDbInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (RecipeDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    RecipeDatabase.class, "recipes_database")
                            .fallbackToDestructiveMigration() // You might want to handle migrations properly instead
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
