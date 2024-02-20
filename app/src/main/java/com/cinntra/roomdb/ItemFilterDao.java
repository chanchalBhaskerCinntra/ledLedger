package com.cinntra.roomdb;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.cinntra.ledure.model.DataItemDashBoard;
import com.cinntra.ledure.model.DataItemInSalesCard;
import com.cinntra.ledure.newapimodel.DataItemFilterDashBoard;

import java.util.List;

@Dao
public interface ItemFilterDao {

   // Getting ALL Data
    @Query("SELECT * FROM item_filter_table")
    List<DataItemFilterDashBoard> getAll();
    @Query("DELETE  FROM item_filter_table")
    void deleteAll();

  //Inserting All Data At once
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<DataItemFilterDashBoard> data);


}
