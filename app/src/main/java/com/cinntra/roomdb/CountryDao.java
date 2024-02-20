package com.cinntra.roomdb;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import com.cinntra.ledure.model.Countries;
import java.util.List;

@Dao
public interface CountryDao {

    //Getting ALL Data
//    @Query("SELECT * FROM country")
//    List<Countries> getAll();
//
//  //Inserting All Data At once
//    @Insert(onConflict = OnConflictStrategy.REPLACE)
//    void insertAll(List<Countries> data);


}
