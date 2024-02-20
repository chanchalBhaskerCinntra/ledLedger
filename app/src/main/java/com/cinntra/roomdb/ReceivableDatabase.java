package com.cinntra.roomdb;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.cinntra.ledure.model.BusinessPartnerData;
import com.cinntra.ledure.model.ReceivableCustomerData;


@Database(entities = {ReceivableCustomerData.class},version = 1,exportSchema = false)
public abstract class ReceivableDatabase extends RoomDatabase {
    public abstract ReceivableDao myDataDao();
    private static volatile ReceivableDatabase receivableDatabase;


    //SIngelton Object of Database
    public static ReceivableDatabase getDatabase(final Context context)
          {
        if (receivableDatabase == null) {
            synchronized (CountriesDatabase.class) {
                if (receivableDatabase == null) {
                    receivableDatabase = Room.databaseBuilder(context.getApplicationContext(),
                                    ReceivableDatabase.class, "my-db-receivables")
                            .allowMainThreadQueries().build();
                }
            }
        }
        return receivableDatabase;
        }








}


