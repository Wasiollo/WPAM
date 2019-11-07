package com.wasiollo.neverexpense.user.domain;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import com.wasiollo.neverexpense.balance.domain.Balance;

import lombok.Data;

@Data
@Entity(tableName = "user_balance_join",
        primaryKeys = { "user_id", "balance_id" },
        foreignKeys = {
                @ForeignKey(entity = User.class,
                        parentColumns = "id",
                        childColumns = "user_id"),
                @ForeignKey(entity = Balance.class,
                        parentColumns = "id",
                        childColumns = "balance_id")
        })
public class UserBalanceJoin {
    @PrimaryKey(autoGenerate = true)
    private Integer id;

    @ColumnInfo(name = "user_id")
    private Integer userId;

    @ColumnInfo(name = "balnce_id")
    private Integer balanceId;
}
