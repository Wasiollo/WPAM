package com.wasiollo.neverexpense.balance.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;

import com.wasiollo.neverexpense.balance.domain.Balance;
import com.wasiollo.neverexpense.balance.domain.BalanceWithReceipts;
import com.wasiollo.neverexpense.receipt.domain.Receipt;

import java.util.List;

@Dao
public abstract class BalanceDao {

    @Insert
    public abstract void insert(Balance balance);

    @Insert
    protected abstract void insert(List<Receipt> receipts);

    @Transaction
    public void insert(BalanceWithReceipts balanceWithReceipts) {
        insert(balanceWithReceipts.getBalance());
        insert(balanceWithReceipts.getReceipts());
    }

    @Query("SELECT * FROM balance b " +
            "JOIN user_balance_join uj " +
            "WHERE uj.user_id = :userId")
    public abstract LiveData<List<Balance>> findByUserId(Integer userId);

    @Query("SELECT * FROM balance b WHERE b.id = :balanceId")
    public abstract LiveData<Balance> findById(Integer balanceId);
}
