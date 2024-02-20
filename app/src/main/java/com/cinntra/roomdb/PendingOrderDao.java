package com.cinntra.roomdb;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.cinntra.ledure.model.DataItemDashBoard;
import com.cinntra.ledure.model.PartyWisePendingOrder;

import java.util.List;

@Dao
public interface PendingOrderDao {

   // Getting ALL Data
    @Query("SELECT * FROM pending_order_table")
    List<PartyWisePendingOrder> getAll();
    @Query("DELETE  FROM pending_order_table")
    void deleteAll();

  //Inserting All Data At once
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<PartyWisePendingOrder> data);


}
