package com.cinntra.roomdb;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.cinntra.ledure.model.DataLedgerGroup;
import com.cinntra.ledure.model.DataZoneGroup;


@Database(entities = {DataZoneGroup.class},version = 1,exportSchema = false)
public abstract class LedgerZoneDatabase extends RoomDatabase {
    public abstract LedgerZoneDao myDataDao();
    private static volatile LedgerZoneDatabase ledgerZoneDatabase;


    //SIngelton Object of Database
     public static LedgerZoneDatabase getDatabase(final Context context)
         {
        if (ledgerZoneDatabase == null) {
            synchronized (LedgerZoneDatabase.class) {
                if (ledgerZoneDatabase == null) {
                    ledgerZoneDatabase = Room.databaseBuilder(context.getApplicationContext(),
                                    LedgerZoneDatabase.class, "my-db-ledger-zone")
                            .allowMainThreadQueries().build();
                }
            }
        }
        return ledgerZoneDatabase;
    }




}


