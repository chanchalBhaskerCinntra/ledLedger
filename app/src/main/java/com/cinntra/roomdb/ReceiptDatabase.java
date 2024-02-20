package com.cinntra.roomdb;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.cinntra.ledure.model.BusinessPartnerData;
import com.cinntra.ledure.model.ReceiptBusinessPartnerData;


@Database(entities = {ReceiptBusinessPartnerData.class},version = 1,exportSchema = false)
public abstract class ReceiptDatabase extends RoomDatabase {
    public abstract ReceiptDataDao myDataDao();
    private static volatile ReceiptDatabase receiptDatabase;


    //SIngelton Object of Database
    public static ReceiptDatabase getDatabase(final Context context)
          {
        if (receiptDatabase == null) {
            synchronized (CountriesDatabase.class) {
                if (receiptDatabase == null) {
                    receiptDatabase = Room.databaseBuilder(context.getApplicationContext(),
                                    ReceiptDatabase.class, "my-db-receipt")
                            .allowMainThreadQueries().build();
                }
            }
        }
        return receiptDatabase;
        }








}


