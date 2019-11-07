package com.wasiollo.neverexpense.balance.domain;

import androidx.room.Embedded;
import androidx.room.Relation;

import com.wasiollo.neverexpense.receipt.domain.Receipt;
import com.wasiollo.neverexpense.receipt.domain.ReceiptWithProducts;

import java.util.List;

import lombok.Data;

@Data
public class BalanceWithReceipts {

    @Embedded
    private Balance balance;

    @Relation(parentColumn = "id", entityColumn = "balance_id", entity = Receipt.class)
    private List<Receipt> receipts;

}
