package com.wasiollo.neverexpense.receipt.dao;

import androidx.lifecycle.LiveData;
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
    public abstract void insert(Receipt receipt);

    @Insert
    protected abstract void insert(List<Product> products);

    @Transaction
    public void insert(ReceiptWithProducts receiptWithProducts) {
        insert(receiptWithProducts.getReceipt());
        insert(receiptWithProducts.getProducts());
    }

    @Query("DELETE FROM receipt")
    public abstract void deleteAll();

    @Query("SELECT * FROM receipt")
    public abstract LiveData<List<Receipt>> getAllReceipts();

    @Query("SELECT * FROM receipt r WHERE r.balance_id = :balanceId")
    public abstract LiveData<List<Receipt>> getReceiptsByBalanceId(Integer balanceId);

    @Query("SELECT * FROM receipt r WHERE r.id = :receiptId")
    public abstract LiveData<Receipt> getReceiptById(Integer receiptId);
}
