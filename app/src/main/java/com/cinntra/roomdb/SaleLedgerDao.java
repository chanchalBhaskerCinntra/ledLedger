package com.cinntra.roomdb;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import com.cinntra.ledure.model.BusinessPartnerData;
import java.util.List;

@Dao
public interface SaleLedgerDao {

    //Getting ALL Data
    @Query("SELECT * FROM salesData")
    List<BusinessPartnerData> getAll();

    @Query("DELETE  FROM salesData")
   void deleteAll();

  //Inserting All Data At once
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<BusinessPartnerData> data);


}
