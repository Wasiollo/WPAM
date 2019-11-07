package com.wasiollo.neverexpense.user.domain;

import androidx.room.Embedded;
import androidx.room.Relation;

import com.wasiollo.neverexpense.balance.domain.Balance;

import java.util.List;

import lombok.Data;

@Data
public class UserWithBalances {
    @Embedded
    private User user;

    @Relation(parentColumn = "id", entityColumn = "balance_id", entity = UserBalanceJoin.class)
    private List<Balance> balances;
}
