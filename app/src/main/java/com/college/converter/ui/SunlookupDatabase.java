/*File name:SunlooupModel.java
 *Author:Dunxing Yu
 *Course:CST2335-021
 *Assignment:Project
 *Data:2024-3-26
 *Professor:Ouaaz, Samira
 */
package com.college.converter.ui;
import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.college.converter.SunlookupDAO;
import com.college.converter.data.SunlookupData;
/**This is a RoomDatabase to operate SunlookupDatabase
 * @author Dunxing Yu
 */
@Database(entities = {SunlookupData.class}, version=1)
public abstract class SunlookupDatabase extends RoomDatabase {
    public abstract SunlookupDAO slDAO();
}