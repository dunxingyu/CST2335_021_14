package com.college.converter.data;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * This class represents an entity (DictionaryRecord table in the database)
 * Lab section: 021
 * Creation date: March 25, 2024
 * @author Qi Cheng
 * @version  1.0
 */
@Entity
public class DictionaryRecord {
    /** The id is declared as the primary key.*/
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name="id")
    public int id;
    /** The word is declared as a column name of word in the database .*/
    @ColumnInfo(name="word")
    protected String word;
    /** The meaning is declared as a column name of meaning in the database .*/
    @ColumnInfo(name="meaning")
    protected String meaning;

    /**
     * This constructor with two parameters.
     * @param w It represents word.
     * @param m It represents meaning.
     */
    public DictionaryRecord(String w, String m){
        word=w;
        meaning=m;
    }
    /**
     * The default constructor.
     */
    public DictionaryRecord(){

    }
    /**
     * This method gets the word.
     * @return Returns word.
     */
    public String getWord() {
        return word;
    }
    /**
     * This method gets the meaning.
     * @return Returns meaning.
     */
    public String getMeaning() {
        return meaning;
    }
}
