package com.wasiollo.neverexpense.receipt.view_model;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.wasiollo.neverexpense.product.domain.Product;
import com.wasiollo.neverexpense.product.repository.ProductRepository;
import com.wasiollo.neverexpense.receipt.domain.Receipt;
import com.wasiollo.neverexpense.receipt.domain.ReceiptWithProducts;
import com.wasiollo.neverexpense.receipt.repository.ReceiptRepository;

import java.util.List;

public class ReceiptViewModel extends AndroidViewModel {

    private ReceiptRepository receiptRepository;
    private ProductRepository productRepository;

    public ReceiptViewModel(@NonNull Application application) {
        super(application);

        receiptRepository = new ReceiptRepository(application);
        productRepository = new ProductRepository(application);
    }

    LiveData<List<Receipt>> getReceiptsByBalanceId(Integer balanceId) {
        return receiptRepository.getReceiptsByBalanceId(balanceId);
    }

    LiveData<Receipt> getReceiptById(Integer receiptId) {
        return receiptRepository.getReceiptById(receiptId);
    }

    LiveData<List<Product>> getProductsByReceiptId(Integer receiptId) {
        return productRepository.getProductsByReceiptId(receiptId);
    }

    public void insert(Receipt receipt) {
        receiptRepository.insert(receipt);
    }

    public void insert(ReceiptWithProducts receiptWithProducts) {
        receiptRepository.insert(receiptWithProducts);
    }
}
