package com.wasiollo.neverexpense;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
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
                balanceViewModel.deleteReceipt(receipt.getId());
                balanceAdapter.notifyDataSetChanged();
            };
            Snackbar.make(this.findViewById(android.R.id.content), "Deleting receipt", Snackbar.LENGTH_LONG)
                    .setAction("Delete", onClickListener)
                    .setActionTextColor(Color.RED)
                    .show();
            return true;
        });

        recyclerView.setAdapter(balanceAdapter);

        balanceViewModel = ViewModelProviders.of(this).get(BalanceViewModel.class);

        balanceViewModel.getReceiptsByBalanceId(balanceId).observe(this, receipts -> balanceAdapter.setReceipts(receipts));

        FloatingActionButton fab = findViewById(R.id.addReceiptButton);
        fab.setOnClickListener(view -> {
            /*Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
            balanceViewModel.getReceiptsByBalanceId(balanceId).observe(this, receipts -> balanceAdapter.setReceipts(receipts));*/
            Intent addingReceiptIntent = new Intent(MainActivity.this, AddingReceiptActivity.class);
            Bundle addingReceiptExtras = new Bundle();
            addingReceiptExtras.putInt("balanceId", balanceId);
            addingReceiptIntent.putExtras(addingReceiptExtras);
            startActivity(addingReceiptIntent);
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        if (id == R.id.action_logout) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
