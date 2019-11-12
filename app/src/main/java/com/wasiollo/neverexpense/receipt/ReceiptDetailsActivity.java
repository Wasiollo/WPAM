package com.wasiollo.neverexpense.receipt;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.wasiollo.neverexpense.R;
import com.wasiollo.neverexpense.product.domain.Product;
import com.wasiollo.neverexpense.receipt.adapter.ReceiptAdapter;
import com.wasiollo.neverexpense.receipt.domain.Receipt;

import java.util.ArrayList;
import java.util.List;

public class ReceiptDetailsActivity extends Activity {
    private RecyclerView recyclerView;
    private RecyclerView.Adapter receiptAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.receipt_activity);

//        TODO get data
        Receipt receipt = new Receipt();
        List<Product> products = new ArrayList<>();

        buildHeader(receipt.getCost());
        attachAdapterToRecyclerView(products);
    }

    private void buildHeader(Double price) {
        ConstraintLayout header = findViewById(R.id.receiptHeader);
        TextView cost = header.findViewById(R.id.cost);
        cost.setText(price.toString());
    }

    private void attachAdapterToRecyclerView(List<Product> products) {
        recyclerView = findViewById(R.id.receiptRecyclerView);
        receiptAdapter = new ReceiptAdapter(products);
        recyclerView.setAdapter(receiptAdapter);
    }
}
