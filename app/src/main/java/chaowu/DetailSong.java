package chaowu;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.college.converter.databinding.ActivityDetailSongBinding;
import com.college.converter.databinding.ActivityFavoriteBinding;
import com.squareup.picasso.Picasso;

public class DetailSong extends AppCompatActivity {
    ActivityDetailSongBinding binding;

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

    }
}