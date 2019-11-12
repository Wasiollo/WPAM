package com.wasiollo.neverexpense.product.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.wasiollo.neverexpense.product.domain.Product;

import java.util.List;

@Dao
public interface ProductDao {

    @Insert
    void insert(Product product);

    @Query("DELETE FROM product")
    void deleteAll();

    @Query("SELECT * FROM product")
    LiveData<List<Product>> getAll();

    @Query("SELECT * FROM product p WHERE p.receipt_id = :receiptId")
    LiveData<List<Product>> getProductsByReceiptId(Integer receiptId);
}
