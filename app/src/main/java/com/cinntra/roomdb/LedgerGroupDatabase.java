package com.cinntra.roomdb;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.cinntra.ledure.model.DataLedgerGroup;


@Database(entities = {DataLedgerGroup.class},version = 1,exportSchema = false)
public abstract class LedgerGroupDatabase extends RoomDatabase {
    public abstract LedgerGroupDao myDataDao();
    private static volatile LedgerGroupDatabase ledgerGroupDatabase;


    //SIngelton Object of Database
     public static LedgerGroupDatabase getDatabase(final Context context)
         {
        if (ledgerGroupDatabase == null) {
            synchronized (LedgerGroupDatabase.class) {
                if (ledgerGroupDatabase == null) {
                    ledgerGroupDatabase = Room.databaseBuilder(context.getApplicationContext(),
                                    LedgerGroupDatabase.class, "my-db-ledger-group")
                            .allowMainThreadQueries().build();
                }
            }
        }
        return ledgerGroupDatabase;
    }




}


