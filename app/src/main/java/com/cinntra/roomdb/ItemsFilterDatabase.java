package com.cinntra.roomdb;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.cinntra.ledure.model.DataItemDashBoard;
import com.cinntra.ledure.newapimodel.DataItemFilterDashBoard;


@Database(entities = {DataItemFilterDashBoard.class},version = 1,exportSchema = false)
public abstract class ItemsFilterDatabase extends RoomDatabase {
    public abstract ItemFilterDao myDataDao();
    private static volatile ItemsFilterDatabase itemsFilterDatabase;


    //SIngelton Object of Database
     public static ItemsFilterDatabase getDatabase(final Context context)
         {
        if (itemsFilterDatabase == null) {
            synchronized (ItemsFilterDatabase.class) {
                if (itemsFilterDatabase == null) {
                    itemsFilterDatabase = Room.databaseBuilder(context.getApplicationContext(),
                                    ItemsFilterDatabase.class, "my-db-item_filter")
                            .allowMainThreadQueries().build();
                }
            }
        }
        return itemsFilterDatabase;
    }




}


