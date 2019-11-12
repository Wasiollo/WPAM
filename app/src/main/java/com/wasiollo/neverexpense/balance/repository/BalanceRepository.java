package com.wasiollo.neverexpense.balance.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.wasiollo.neverexpense.AppDatabase;
import com.wasiollo.neverexpense.balance.dao.BalanceDao;
import com.wasiollo.neverexpense.balance.domain.Balance;
import com.wasiollo.neverexpense.balance.domain.BalanceWithReceipts;

import java.util.List;

public class BalanceRepository {
    private BalanceDao balanceDao;

    public BalanceRepository(Application application){
        AppDatabase db = AppDatabase.getDatabase(application);
        balanceDao = db.balanceDao();
    }

    public LiveData<List<Balance>> getBalancesByUserId(Integer userId){
        return balanceDao.findByUserId(userId);

        //TODO reconsider http-request
    }

    public LiveData<Balance> getBalanceById(Integer balanceId) {
        return balanceDao.findById(balanceId);

        //TODO reconsider http-request
    }

    public void insert(Balance balance){
        AppDatabase.databaseWriteExecutor.execute(() -> {
            balanceDao.insert(balance);
        });

        //TODO http request
    }

    public void insert(BalanceWithReceipts balanceWithReceipts){
        AppDatabase.databaseWriteExecutor.execute(() -> {
            balanceDao.insert(balanceWithReceipts);
        });

        //TODO http request
    }
}
