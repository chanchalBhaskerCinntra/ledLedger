package com.cinntra.roomdb;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.cinntra.ledure.model.DataItemInSalesCard;


@Database(entities = {DataItemInSalesCard.class},version = 1,exportSchema = false)
public abstract class ItemsInSalesCardDatabase extends RoomDatabase {
    public abstract ItemInSalesCardDao myDataDao();
    private static volatile ItemsInSalesCardDatabase itemsInSalesDatabase;


    //SIngelton Object of Database
     public static ItemsInSalesCardDatabase getDatabase(final Context context)
         {
        if (itemsInSalesDatabase == null) {
            synchronized (ItemsInSalesCardDatabase.class) {
                if (itemsInSalesDatabase == null) {
                    itemsInSalesDatabase = Room.databaseBuilder(context.getApplicationContext(),
                                    ItemsInSalesCardDatabase.class, "my-db-item-in-sales")
                            .allowMainThreadQueries().build();
                }
            }
        }
        return itemsInSalesDatabase;
    }




}


