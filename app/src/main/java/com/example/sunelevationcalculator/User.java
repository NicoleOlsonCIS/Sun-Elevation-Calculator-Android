package com.example.sunelevationcalculator;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class User {
    @PrimaryKey
    public int uid;

    @ColumnInfo(name = "city")
    public String userCity;

    public void setUserCity(String city)
    {
        System.out.println("Setting User City to " + city);
        this.userCity = city;
    }
    public String getUserCity() { return userCity; }

    @ColumnInfo(name = "state")
    public String userState;

    public void setUserState(String state)
    {
        System.out.println("Setting User State to " + state);
        this.userState = state;
    }
    public String getUserState() { return userState; }

    @ColumnInfo(name = "country")
    public String userCountry;

    public void setUserCountry(String country)
    {
        System.out.println("Setting User Country to " + country);
        this.userCountry = country;
    }
    public String getUserCountry() { return userCountry; }

    @ColumnInfo(name = "latitude")
    public Double userLatitude;

    public void setUserLatitude(Double latitude)
    {
        System.out.println("Setting User latitude to " + latitude);
        this.userLatitude = latitude;
    }
    public Double getUserLatitude() { return userLatitude; }

    @ColumnInfo(name = "longitude")
    public Double userLongitude;

    public void setUserLongitude(Double longitude)
    {
        System.out.println("Setting User longitude to " + longitude);
        this.userLongitude = longitude;
    }
    public Double getUserLongitude() { return userLongitude; }

}


