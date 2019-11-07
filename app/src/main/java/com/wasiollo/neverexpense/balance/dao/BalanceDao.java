package com.wasiollo.neverexpense.balance.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Transaction;

import com.wasiollo.neverexpense.balance.domain.Balance;
import com.wasiollo.neverexpense.balance.domain.BalanceWithReceipts;
import com.wasiollo.neverexpense.receipt.domain.Receipt;

import java.util.List;

@Dao
public abstract class BalanceDao {

    @Insert
    protected abstract void insert(Balance balance);

    @Insert
    protected abstract void insert(List<Receipt> receipts);

    @Transaction
    public void insert(BalanceWithReceipts balanceWithReceipts) {
        insert(balanceWithReceipts.getBalance());
        insert(balanceWithReceipts.getReceipts());
    }

}
