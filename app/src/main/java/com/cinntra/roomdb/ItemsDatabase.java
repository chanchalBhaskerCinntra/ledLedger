package com.cinntra.roomdb;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.cinntra.ledure.model.CountryData;
import com.cinntra.ledure.model.DataItemDashBoard;


@Database(entities = {DataItemDashBoard.class},version = 1,exportSchema = false)
public abstract class ItemsDatabase extends RoomDatabase {
    public abstract ItemDao myDataDao();
    private static volatile ItemsDatabase itemsDatabase;


    //SIngelton Object of Database
     public static ItemsDatabase getDatabase(final Context context)
         {
        if (itemsDatabase == null) {
            synchronized (ItemsDatabase.class) {
                if (itemsDatabase == null) {
                    itemsDatabase = Room.databaseBuilder(context.getApplicationContext(),
                                    ItemsDatabase.class, "my-db-item")
                            .allowMainThreadQueries().build();
                }
            }
        }
        return itemsDatabase;
    }




}


