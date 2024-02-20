package com.cinntra.roomdb;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.cinntra.ledure.model.DataItemDashBoard;
import com.cinntra.ledure.model.DataLedgerGroup;

import java.util.List;

@Dao
public interface LedgerGroupDao {

   // Getting ALL Data
    @Query("SELECT * FROM ledger_group")
    List<DataLedgerGroup> getAll();
    @Query("DELETE  FROM ledger_group")
    void deleteAll();

  //Inserting All Data At once
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<DataLedgerGroup> data);


}
