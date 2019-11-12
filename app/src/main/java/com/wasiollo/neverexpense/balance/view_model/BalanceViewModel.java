package com.wasiollo.neverexpense.balance.view_model;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.wasiollo.neverexpense.balance.domain.Balance;
import com.wasiollo.neverexpense.balance.domain.BalanceWithReceipts;
import com.wasiollo.neverexpense.balance.repository.BalanceRepository;
import com.wasiollo.neverexpense.receipt.domain.Receipt;
import com.wasiollo.neverexpense.receipt.repository.ReceiptRepository;

import java.util.List;

public class BalanceViewModel extends AndroidViewModel {

    private BalanceRepository balanceRepository;
    private ReceiptRepository receiptRepository;

    public BalanceViewModel(@NonNull Application application) {
        super(application);

        balanceRepository = new BalanceRepository(application);
        receiptRepository = new ReceiptRepository(application);
    }

    LiveData<Balance> getBalanceById(Integer balanceId) {
        return balanceRepository.getBalanceById(balanceId);
    }

    LiveData<List<Balance>> getBalancesByUserId(Integer userId){
        return balanceRepository.getBalancesByUserId(userId);
    }

    LiveData<List<Receipt>> getReceiptsByBalanceId(Integer balanceId){
        return receiptRepository.getReceiptsByBalanceId(balanceId);
    }

    public void insert(Balance balance){
        balanceRepository.insert(balance);
    }

    public void insert(BalanceWithReceipts balanceWithReceipts){
        balanceRepository.insert(balanceWithReceipts);
    }


}
