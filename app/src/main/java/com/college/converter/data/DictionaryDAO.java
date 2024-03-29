package com.college.converter.data;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import java.util.List;

/**
 * This interface provides a way to access the data stored within a SQLite database. It contains
 * the SQL statements.
 * Lab section: 021
 * Creation date: March 25, 2024
 * @author Qi Cheng
 * @version  1.0
 */
@Dao
public interface DictionaryDAO {
    /**
     * This method is for inserting one record into database.
     * @param dr The object of DictionaryRecord.
     */
    @Insert
    public void insertRecord(DictionaryRecord dr);
    /**
     * This method is for querying one record from database.
     * @return Returns a list which contains all of the records.
     */
    @Query("Select * from DictionaryRecord")
    public List<DictionaryRecord> getAllRecords();
    /**
     * This method is for deleting one record from database.
     * @param dr The object of DictionaryRecord.
     */
    @Delete
    public void deleteRecord(DictionaryRecord dr);

}
