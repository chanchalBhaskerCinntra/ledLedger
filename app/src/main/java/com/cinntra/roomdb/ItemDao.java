package com.cinntra.roomdb;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.cinntra.ledure.model.DataItemDashBoard;

import java.util.List;

@Dao
public interface ItemDao {

   // Getting ALL Data
    @Query("SELECT * FROM item_list")
    List<DataItemDashBoard> getAll();
    @Query("DELETE  FROM item_list")
    void deleteAll();

  //Inserting All Data At once
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<DataItemDashBoard> data);


}
