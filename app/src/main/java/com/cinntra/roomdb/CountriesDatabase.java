package com.cinntra.roomdb;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.cinntra.ledure.model.CountryData;


@Database(entities = {CountryData.class},version = 1,exportSchema = false)
public abstract class CountriesDatabase extends RoomDatabase {
    public abstract CountryDao myDataDao();
    private static volatile CountriesDatabase countriesDatabase;


    //SIngelton Object of Database
     public static CountriesDatabase getDatabase(final Context context)
         {
        if (countriesDatabase == null) {
            synchronized (CountriesDatabase.class) {
                if (countriesDatabase == null) {
                    countriesDatabase = Room.databaseBuilder(context.getApplicationContext(),
                                    CountriesDatabase.class, "my-db-country")
                            .allowMainThreadQueries().build();
                }
            }
        }
        return countriesDatabase;
    }




}


