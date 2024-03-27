package chaowu;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Song {
    @ColumnInfo(name="title")
    protected String title;
    @ColumnInfo(name="duration")
    protected String duration;

    @ColumnInfo(name="albumName")
    protected String albumName;

    @ColumnInfo(name="cover")
    protected  String cover;

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name="id")
    public int id;
    public Song(){}
    Song (String t, String d, String a, String c){
        title = t;
        duration = d;
        albumName = a;
        cover = c;
    }

    public String getTitle(){
        return title;
    }
    public String getDuration(){
        return duration;
    }
    public String getName(){
        return albumName;
    }

    public String getCover(){
        return cover;
    }
}
