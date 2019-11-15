package com.wasiollo.neverexpense.receipt;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;

import com.wasiollo.neverexpense.MainActivity;
import com.wasiollo.neverexpense.R;
import com.wasiollo.neverexpense.receipt.adapter.ReceiptAdapter;
import com.wasiollo.neverexpense.receipt.view_model.ReceiptViewModel;

public class ReceiptDetailsActivity extends AppCompatActivity {
    private ReceiptViewModel receiptViewModel;
    private RecyclerView recyclerView;
    private ReceiptAdapter receiptAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.receipt_activity);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        Intent intent = getIntent();
        Integer receiptId = intent.getIntExtra("receiptId", 0);

        recyclerView = findViewById(R.id.receiptRecyclerView);
        receiptAdapter = new ReceiptAdapter();
        recyclerView.setAdapter(receiptAdapter);

        receiptViewModel = ViewModelProviders.of(this).get(ReceiptViewModel.class);

        receiptViewModel.getReceiptById(receiptId).observe(this, receipt -> setTotalCostOnView(receipt.getCost()));
        receiptViewModel.getProductsByReceiptId(receiptId).observe(this, products -> receiptAdapter.setProducts(products));
    }

    private void setTotalCostOnView(Double totalCost) {
        ConstraintLayout header = findViewById(R.id.receiptHeader);
        TextView cost = header.findViewById(R.id.cost);
        if (cost != null) {
            cost.setText(String.valueOf(totalCost));
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        Intent myIntent = new Intent(getApplicationContext(), MainActivity.class);
        startActivityForResult(myIntent, 0);
        return true;
    }
}
