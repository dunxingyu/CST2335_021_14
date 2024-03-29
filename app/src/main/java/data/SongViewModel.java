package data;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;

import chaowu.Song;

public class SongViewModel extends ViewModel {
    public MutableLiveData<ArrayList<Song>> songs = new MutableLiveData< >();

}
