/*File name:SunlooupModel.java
 *Author:Dunxing Yu
 *Course:CST2335-021
 *Assignment:Project
 *Data:2024-3-26
 *Professor:Ouaaz, Samira
 */
package com.college.converter.data;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
/**This is a ViewModel to hold the messages values.
 * @author Dunxing Yu
 */
public class SunlooupModel extends ViewModel {
    public MutableLiveData<ArrayList<SunlookupData>> messages = new MutableLiveData<>();
}
