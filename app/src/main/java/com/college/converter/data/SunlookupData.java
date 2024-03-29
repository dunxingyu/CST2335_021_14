/*File name:SunlookupData.java
 *Author:Dunxing Yu
 *Course:CST2335-021
 *Assignment:Project
 *Data:2024-3-26
 *Professor:Ouaaz, Samira
 */
package com.college.converter.data;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**This is a Room to set the SunlookupData.
 * @author Dunxing Yu
 */
@Entity
public class SunlookupData {

    @ColumnInfo(name="lat")
    protected String lat;
    @ColumnInfo(name="longitude")
    protected String longitude;
    @ColumnInfo(name="isSentButton")
    protected boolean isSentButton;
    @PrimaryKey (autoGenerate = true)
    @ColumnInfo(name="id")
    public int id;

    public SunlookupData(String lat, String longitude, boolean isSentButton) {
        this.lat = lat;
        this.longitude =longitude;
        this.isSentButton=isSentButton;
    }

    public String getLat()
    {
        return this.lat;
    }

    public String getLongitude()
    {
        return this.longitude;
    }

    public boolean getIsSentButton()
    {
        return this.isSentButton;
    }

}

