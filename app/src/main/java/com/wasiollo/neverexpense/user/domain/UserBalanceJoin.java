package com.wasiollo.neverexpense.user.domain;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;

import com.wasiollo.neverexpense.balance.domain.Balance;

import lombok.Data;

@Data
@Entity(tableName = "user_balance_join",
        primaryKeys = {"user_id", "balance_id"},
        foreignKeys = {
                @ForeignKey(entity = User.class,
                        parentColumns = "id",
                        childColumns = "user_id"),
                @ForeignKey(entity = Balance.class,
                        parentColumns = "id",
                        childColumns = "balance_id")
        })
public class UserBalanceJoin {
    @ColumnInfo(name = "user_id")
    @NonNull
    private Integer userId;

    @NonNull
    @ColumnInfo(name = "balance_id")
    private Integer balanceId;
}
