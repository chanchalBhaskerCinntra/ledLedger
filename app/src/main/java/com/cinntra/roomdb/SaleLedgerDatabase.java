package com.cinntra.roomdb;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import com.cinntra.ledure.model.BusinessPartnerData;



@Database(entities = {BusinessPartnerData.class},version = 1,exportSchema = false)
public abstract class SaleLedgerDatabase extends RoomDatabase {
    public abstract SaleLedgerDao myDataDao();
    private static volatile SaleLedgerDatabase saleLedgerDatabase;


    //SIngelton Object of Database
    public static SaleLedgerDatabase getDatabase(final Context context)
          {
        if (saleLedgerDatabase == null) {
            synchronized (CountriesDatabase.class) {
                if (saleLedgerDatabase == null) {
                    saleLedgerDatabase = Room.databaseBuilder(context.getApplicationContext(),
                                    SaleLedgerDatabase.class, "my-db-sales")
                            .allowMainThreadQueries().build();
                }
            }
        }
        return saleLedgerDatabase;
        }








}


