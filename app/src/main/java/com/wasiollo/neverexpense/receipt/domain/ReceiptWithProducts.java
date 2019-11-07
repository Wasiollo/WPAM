package com.wasiollo.neverexpense.receipt.domain;

import androidx.room.Embedded;
import androidx.room.Relation;

import com.wasiollo.neverexpense.product.domain.Product;

import java.util.List;

import lombok.Data;

@Data
public class ReceiptWithProducts {

    @Embedded
    private Receipt receipt;

    @Relation(parentColumn = "id", entityColumn = "receipt_id", entity = Product.class)
    private List<Product> products;
}
