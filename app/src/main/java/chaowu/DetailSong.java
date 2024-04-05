/**
 * The DetailSong class represents the activity where details of a selected song are displayed.
 * Users can view the title, duration, album, and cover image of the selected song.
 */
package chaowu;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.college.converter.Dictionary;
import com.college.converter.MainActivity;
import com.college.converter.R;
import com.college.converter.Sunlookup;
import com.college.converter.databinding.ActivityDetailSongBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.squareup.picasso.Picasso;

import Zhihao.RecipeSearchActivity;
/**
 * Represents the activity where details of a selected song are displayed.
 */
public class DetailSong extends AppCompatActivity {
    ActivityDetailSongBinding binding;
    /**
     * Initializes the activity when created.
     *
     * @param savedInstanceState The saved instance state bundle.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDetailSongBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        // Retrieve cover and duration from Intent extras
        String title = getIntent().getStringExtra("title");
        String duration = getIntent().getStringExtra("duration");
        String album = getIntent().getStringExtra("album");
        String cover = getIntent().getStringExtra("cover");

        binding.textView7.setText(title);
        binding.textView8.setText(duration);
        binding.textView9.setText(album);
        Picasso.get().load(cover).into(binding.imageView);
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.forth_id);

        // Perform item selected listener
        bottomNavigationView.setOnItemSelectedListener(item -> {

            int item_id = item.getItemId();
            if ( item_id == R.id.home_id ) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            }
            else if (item_id == R.id.first_id) {
                startActivity(new Intent(getApplicationContext(), Sunlookup.class));
                return true;
            }
            else if ( item_id == R.id.second_id ) {
                startActivity(new Intent(getApplicationContext(), RecipeSearchActivity.class));
                return true;
            }
            else if ( item_id == R.id.third_id ) {
                startActivity(new Intent(getApplicationContext(), Dictionary.class));

                return true;
            }
            else if ( item_id == R.id.forth_id ) {
                startActivity(new Intent(getApplicationContext(), DeezerActivity.class));

                return true;
            }
            return false;
        });
    }

    /**
     * Inflates the options menu.
     *
     * @param menu The menu to inflate.
     * @return true if the menu is inflated successfully, false otherwise.
     */
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.my_menu, menu);
        return true;
    }
    /**
     * Handles options menu item selection.
     *
     * @param item The selected menu item.
     * @return true if the menu item is handled successfully, false otherwise.
     */
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();
        if ( id ==  R.id.help) {
            androidx.appcompat.app.AlertDialog.Builder builder1 = new androidx.appcompat.app.AlertDialog.Builder(DetailSong.this);
            builder1.setMessage(getString(R.string.deezer_information));
            builder1.setTitle(getString(R.string.deezer_info_title));

            builder1.create().show();
        }
        else if (id ==  R.id.home) {
            Toast.makeText(this, getString(R.string.back), Toast.LENGTH_SHORT).show();
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }
}