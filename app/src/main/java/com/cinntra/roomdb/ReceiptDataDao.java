package com.cinntra.roomdb;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.cinntra.ledure.model.BusinessPartnerData;
import com.cinntra.ledure.model.ReceiptBusinessPartnerData;

import java.util.List;

@Dao
public interface ReceiptDataDao {

    //Getting ALL Data
    @Query("SELECT * FROM receiptData")
    List<ReceiptBusinessPartnerData> getAll();

    @Query("DELETE  FROM receiptData")
   void deleteAll();

  //Inserting All Data At once
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<ReceiptBusinessPartnerData> data);


}
