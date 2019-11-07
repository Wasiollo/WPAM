package com.wasiollo.neverexpense.balance.domain;

import androidx.room.Embedded;
import androidx.room.Relation;

import com.wasiollo.neverexpense.user.domain.User;
import com.wasiollo.neverexpense.user.domain.UserBalanceJoin;

import java.util.List;

import lombok.Data;

@Data
public class BalanceWithUsers {
    @Embedded
    private Balance balance;

    @Relation(parentColumn = "id", entityColumn = "user_id", entity = UserBalanceJoin.class)
    private List<User> users;
}
