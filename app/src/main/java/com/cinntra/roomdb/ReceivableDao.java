package com.cinntra.roomdb;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.cinntra.ledure.model.BusinessPartnerData;
import com.cinntra.ledure.model.ReceivableCustomerData;

import java.util.List;

@Dao
public interface ReceivableDao {

    //Getting ALL Data
    @Query("SELECT * FROM receivable_data")
    List<ReceivableCustomerData> getAll();

    @Query("DELETE  FROM receivable_data")
   void deleteAll();

  //Inserting All Data At once
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<ReceivableCustomerData> data);


}
