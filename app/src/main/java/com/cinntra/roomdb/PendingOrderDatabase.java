package com.cinntra.roomdb;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.cinntra.ledure.model.DataItemDashBoard;
import com.cinntra.ledure.model.PartyWisePendingOrder;


@Database(entities = {PartyWisePendingOrder.class},version = 1,exportSchema = false)
public abstract class PendingOrderDatabase extends RoomDatabase {
    public abstract PendingOrderDao myDataDao();
    private static volatile PendingOrderDatabase pendingOrderDatabase;


    //SIngelton Object of Database
     public static PendingOrderDatabase getDatabase(final Context context)
         {
        if (pendingOrderDatabase == null) {
            synchronized (PendingOrderDatabase.class) {
                if (pendingOrderDatabase == null) {
                    pendingOrderDatabase = Room.databaseBuilder(context.getApplicationContext(),
                                    PendingOrderDatabase.class, "my-db-pending-order")
                            .allowMainThreadQueries().build();
                }
            }
        }
        return pendingOrderDatabase;
    }




}


