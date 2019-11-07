package com.wasiollo.neverexpense.receipt.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;

import com.wasiollo.neverexpense.product.domain.Product;
import com.wasiollo.neverexpense.receipt.domain.Receipt;
import com.wasiollo.neverexpense.receipt.domain.ReceiptWithProducts;

import java.util.List;

@Dao
public abstract class ReceiptDao {

    @Insert
    protected abstract void insert(Receipt receipt);

    @Insert
    protected abstract void insert(List<Product> products);

    @Transaction
    public void insert(ReceiptWithProducts receiptWithProducts) {
        insert(receiptWithProducts.getReceipt());
        insert(receiptWithProducts.getProducts());
    }

    @Query("SELECT * FROM receipt")
    public abstract List<ReceiptWithProducts> getAll();
}
