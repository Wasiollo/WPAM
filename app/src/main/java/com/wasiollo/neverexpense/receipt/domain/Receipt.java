package com.wasiollo.neverexpense.receipt.domain;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.time.LocalDateTime;

import lombok.Data;

@Data
@Entity(tableName = "receipt")
public class Receipt {

    @PrimaryKey(autoGenerate = true)
    private Integer id;

    @ColumnInfo(name = "cost")
    private Double cost;

    @ColumnInfo(name = "company")
    private Double company;

    @ColumnInfo(name = "date_time")
    private LocalDateTime dateTime;

    @ColumnInfo(name = "user_id")
    private Integer userId;

    @ColumnInfo(name = "balance_id")
    private Integer balanceId;

}
