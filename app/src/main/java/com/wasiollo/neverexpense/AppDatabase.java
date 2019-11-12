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

import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {User.class, Balance.class, Receipt.class, Product.class}, version = 1)
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
                            AppDatabase.class, "app_database")
                            .addCallback(sRoomDatabaseCallback).build();
                }
            }
        }
        return INSTANCE;
    }

    private static RoomDatabase.Callback sRoomDatabaseCallback = new RoomDatabase.Callback() {
        @Override
        public void onOpen(@NonNull SupportSQLiteDatabase db) {
            super.onOpen(db);

            // If you want to keep data through app restarts,
            // comment out the following block
            databaseWriteExecutor.execute(() -> {
                // Populate the database in the background.
                // If you want to start with more words, just add them.

                ProductDao productDao = INSTANCE.productDao();
                productDao.deleteAll();

                ReceiptDao receiptDao = INSTANCE.receiptDao();
                receiptDao.deleteAll();

                Receipt receipt1 = new Receipt();
                receipt1.setCompany("Aa");
                receipt1.setCost(123.44);
                receipt1.setDateTime(new Date());
                receipt1.setId(1);
                receipt1.setUserId(1);
                receipt1.setBalanceId(1);

                Receipt receipt2 = new Receipt();
                receipt2.setCompany("Bb");
                receipt2.setCost(166.44);
                receipt2.setDateTime(new Date());
                receipt2.setId(2);
                receipt2.setUserId(1);
                receipt2.setBalanceId(1);

                receiptDao.insert(receipt1);
                receiptDao.insert(receipt2);

                Product product1 = new Product();
                product1.setId(1);
                product1.setName("a");
                product1.setPrice(1.00);
                product1.setReceiptId(1);

                Product product2 = new Product();
                product2.setId(2);
                product2.setName("b");
                product2.setPrice(2.00);
                product2.setReceiptId(1);

                Product product3 = new Product();
                product3.setId(3);
                product3.setName("c");
                product3.setPrice(3.00);
                product3.setReceiptId(2);

                Product product4 = new Product();
                product4.setId(4);
                product4.setName("d");
                product4.setPrice(4.00);
                product4.setReceiptId(2);

                productDao.insert(product1);
                productDao.insert(product2);
                productDao.insert(product3);
                productDao.insert(product4);

            });
        }
    };

}
