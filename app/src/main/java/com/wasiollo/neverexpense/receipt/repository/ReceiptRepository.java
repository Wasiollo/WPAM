package com.wasiollo.neverexpense.receipt.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.wasiollo.neverexpense.AppDatabase;
import com.wasiollo.neverexpense.receipt.dao.ReceiptDao;
import com.wasiollo.neverexpense.receipt.domain.Receipt;
import com.wasiollo.neverexpense.receipt.domain.ReceiptWithProducts;

import java.util.List;

public class ReceiptRepository {
    private ReceiptDao receiptDao;

    public ReceiptRepository(Application application) {
        AppDatabase db = AppDatabase.getDatabase(application);
        receiptDao = db.receiptDao();
    }

    public LiveData<List<Receipt>> getReceiptsByBalanceId(Integer balanceId){
        return receiptDao.getReceiptsByBalanceId(balanceId);

        //TODO reconsider http-request
    }

    public LiveData<Receipt> getReceiptById(Integer receiptId){
        return receiptDao.getReceiptById(receiptId);

        //TODO reconsider http-request
    }

    public void insert(ReceiptWithProducts receiptWithProducts){
        AppDatabase.databaseWriteExecutor.execute(() -> {
            receiptDao.insert(receiptWithProducts);
        });

        //TODO http-request to backend instance (receiptWithProducts)
    }

    public void insert(Receipt receipt){
        AppDatabase.databaseWriteExecutor.execute(() -> {
            receiptDao.insert(receipt);
        });

        //TODO http-request to backend instance (only Receipt)
    }
}
