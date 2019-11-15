package com.wasiollo.neverexpense.receipt;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;

import com.wasiollo.neverexpense.R;
import com.wasiollo.neverexpense.receipt.adapter.ReceiptAdapter;
import com.wasiollo.neverexpense.receipt.domain.Receipt;
import com.wasiollo.neverexpense.receipt.view_model.ReceiptViewModel;

public class ReceiptDetailsActivity extends AppCompatActivity {
    private ReceiptViewModel receiptViewModel;
    private RecyclerView recyclerView;
    private ReceiptAdapter receiptAdapter;
    private Receipt receipt;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.receipt_activity);

        Intent intent = getIntent();
        Integer receiptId = intent.getIntExtra("receiptId", 0);

        recyclerView = findViewById(R.id.receiptRecyclerView);
        receiptAdapter = new ReceiptAdapter();
        recyclerView.setAdapter(receiptAdapter);

        receiptViewModel = ViewModelProviders.of(this).get(ReceiptViewModel.class);

        receiptViewModel.getReceiptById(receiptId).observe(this, receipt -> this.receipt = receipt);
        receiptViewModel.getProductsByReceiptId(receiptId).observe(this, products -> receiptAdapter.setProducts(products));

//        buildHeader();
    }

    private void buildHeader() {
        ConstraintLayout header = findViewById(R.id.receiptHeader);
        TextView cost = header.findViewById(R.id.cost);

        cost.setText(String.format("%d", receipt.getCost()));
    }
}
