package chaowu;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface SongDAO {
    @Insert
    public long insertSong(Song s);

    @Query("Select * from Song")
    List<Song> getAllSongs();

    @Delete
    void deleteSong(Song s);
}
