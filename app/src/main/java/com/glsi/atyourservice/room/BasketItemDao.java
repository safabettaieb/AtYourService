package com.glsi.atyourservice.room;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface BasketItemDao {
    @Query("SELECT * FROM basket_item")
    List<BasketItem> getAll();
    @Insert
    void insert(BasketItem basket);
    @Update
    void update(BasketItem basket);
    @Delete
    void delete(BasketItem basket);
    @Delete
    void deleteAll(List<BasketItem> baskets);
}
