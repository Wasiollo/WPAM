package com.wasiollo.neverexpense;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.wasiollo.neverexpense.balance.domain.Balance;
import com.wasiollo.neverexpense.product.dao.ProductDao;
import com.wasiollo.neverexpense.product.domain.Product;
import com.wasiollo.neverexpense.receipt.dao.ReceiptDao;
import com.wasiollo.neverexpense.receipt.domain.Receipt;
import com.wasiollo.neverexpense.user.domain.User;

@Database(entities = {User.class, Balance.class, Receipt.class, Product.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public ReceiptDao receiptDao;
    public ProductDao productDao;

}
