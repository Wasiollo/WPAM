package com.wasiollo.neverexpense.product.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.wasiollo.neverexpense.AppDatabase;
import com.wasiollo.neverexpense.product.dao.ProductDao;
import com.wasiollo.neverexpense.product.domain.Product;

import java.util.List;

public class ProductRepository {
    private ProductDao productDao;

    public ProductRepository(Application application){
        AppDatabase db = AppDatabase.getDatabase(application);
        productDao = db.productDao();
    }

    public LiveData<List<Product>> getProductsByReceiptId(Integer receiptId){
        return productDao.getProductsByReceiptId(receiptId);

        //TODO reconsider http-requests
    }

    public void insert(Product product) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            productDao.insert(product);
        });
    }

    public void insert(List<Product> products) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            productDao.insert(products);
        });
    }

    public LiveData<List<Product>> getAll() {
        return productDao.getAll();
    }
}
