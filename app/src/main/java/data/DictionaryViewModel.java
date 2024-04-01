package data;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.college.converter.data.DictionaryRecord;

import java.util.ArrayList;
/**
 * This stores data when the mobile rotates.
 * Lab section: 021
 * Creation date: March 25, 2024
 * @author Qi Cheng
 * @version  1.0
 */
public class DictionaryViewModel extends ViewModel {
    public MutableLiveData<ArrayList<DictionaryRecord>> records = new MutableLiveData< >();

}
