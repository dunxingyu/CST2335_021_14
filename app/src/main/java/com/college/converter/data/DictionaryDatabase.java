package com.college.converter.data;

import androidx.room.Database;
import androidx.room.RoomDatabase;

/**
 * This class is responsible for creating a new room database instance and for providing
 * access to the DAO instances associated with the database.
 * Lab section: 021
 * Creation date: March 25, 2024
 * @author Qi Cheng
 * @version  1.0
 */
@Database(entities = {DictionaryRecord.class}, version = 1)
public abstract class DictionaryDatabase extends RoomDatabase {
    /**
     * This method is for accessing the DAO.
     * @return Returns an instance of DictionaryDAO.
     */
    public abstract DictionaryDAO dicDAO();

}
