package com.wasiollo.neverexpense;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.wasiollo.neverexpense.balance.dao.BalanceDao;
import com.wasiollo.neverexpense.balance.domain.Balance;
import com.wasiollo.neverexpense.product.dao.ProductDao;
import com.wasiollo.neverexpense.product.domain.Product;
import com.wasiollo.neverexpense.receipt.dao.ReceiptDao;
import com.wasiollo.neverexpense.receipt.domain.Receipt;
import com.wasiollo.neverexpense.user.domain.User;
import com.wasiollo.neverexpense.user.domain.UserBalanceJoin;

import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {User.class, Balance.class, Receipt.class, Product.class, UserBalanceJoin.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {

    public abstract ReceiptDao receiptDao();

    public abstract ProductDao productDao();

    public abstract BalanceDao balanceDao();

    private static volatile AppDatabase INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;
    public static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public static AppDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            AppDatabase.class, "balance_db")
                            .allowMainThreadQueries()
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
