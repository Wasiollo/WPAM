package com.wasiollo.neverexpense.product.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.wasiollo.neverexpense.product.domain.Product;

import java.util.List;

@Dao
public interface ProductDao {

    @Insert
    void insert(Product product);

    @Query("SELECT * FROM product")
    List<Product> getAll();

}
