package com.wasiollo.neverexpense;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.wasiollo.neverexpense.balance.adapter.BalanceAdapter;
import com.wasiollo.neverexpense.balance.view_model.BalanceViewModel;
import com.wasiollo.neverexpense.receipt.AddingReceiptActivity;
import com.wasiollo.neverexpense.receipt.ReceiptDetailsActivity;

public class MainActivity extends AppCompatActivity {
    private BalanceViewModel balanceViewModel;
    private RecyclerView recyclerView;
    private BalanceAdapter balanceAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.balance_activity);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Integer balanceId = 1; //TODO get balance

        recyclerView = findViewById(R.id.balanceRecyclerView);

        balanceAdapter = new BalanceAdapter(receipt -> {
            Intent receiptDetailsIntent = new Intent(MainActivity.this, ReceiptDetailsActivity.class);
            Bundle receiptDetailsExtras = new Bundle();
            receiptDetailsExtras.putInt("receiptId", receipt.getId());
            receiptDetailsIntent.putExtras(receiptDetailsExtras);
            startActivity(receiptDetailsIntent);
        }, receipt -> {
            View.OnClickListener onClickListener = v -> {
                Integer receiptId = receipt.getId();
                balanceViewModel.deleteReceipt(receiptId);
                balanceAdapter.removeReceipt(receiptId);
            };
            Snackbar.make(this.findViewById(android.R.id.content), "Aby usunąć naciśnij", Snackbar.LENGTH_LONG)
                    .setAction("Usuń", onClickListener)
                    .setActionTextColor(Color.RED)
                    .show();
            return true;
        });

        recyclerView.setAdapter(balanceAdapter);

        balanceViewModel = ViewModelProviders.of(this).get(BalanceViewModel.class);

        getReceipts(balanceId);

        FloatingActionButton addReceiptButton = findViewById(R.id.addReceiptButton);
        addReceiptButton.setOnClickListener(view -> {
            Intent addingReceiptIntent = new Intent(MainActivity.this, AddingReceiptActivity.class);
            Bundle addingReceiptExtras = new Bundle();
            addingReceiptExtras.putInt("balanceId", balanceId);
            addingReceiptIntent.putExtras(addingReceiptExtras);
            startActivity(addingReceiptIntent);
        });
    }

    private void getReceipts(Integer balanceId) {
        balanceViewModel.getReceiptsByBalanceId(balanceId).observe(this, receipts -> balanceAdapter.setReceipts(receipts));
    }

}
