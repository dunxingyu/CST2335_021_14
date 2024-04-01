/*File name:SunlookupDAO.java
 *Author:Dunxing Yu
 *Course:CST2335-021
 *Assignment:Project
 *Data:2024-3-26
 *Professor:Ouaaz, Samira
 */
package com.college.converter;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import com.college.converter.data.SunlookupData;
import com.college.converter.data.SunlooupModel;
import com.college.converter.ui.SunlookupDatabase;

import java.util.List;
/**This is a DAO interface
 * @author Dunxing Yu
 */
@Dao
public interface SunlookupDAO {

    @Insert
    public void insertData(SunlookupData s);
    @Query("Select * from SunlookupData")
    public List<SunlookupData> getAllData();
    @Delete
    public void deleteData(SunlookupData s);
}
