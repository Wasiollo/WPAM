package com.wasiollo.neverexpense;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.wasiollo.neverexpense.balance.adapter.BalanceAdapter;
import com.wasiollo.neverexpense.balance.view_model.BalanceViewModel;
import com.wasiollo.neverexpense.receipt.domain.Receipt;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private BalanceViewModel balanceViewModel;
    private RecyclerView recyclerView;
    private BalanceAdapter balanceAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AppCompatDelegate.setDefaultNightMode(
                AppCompatDelegate.MODE_NIGHT_AUTO);

        setContentView(R.layout.balance_activity);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        recyclerView = findViewById(R.id.balanceRecyclerView);

        balanceAdapter = new BalanceAdapter();

        recyclerView.setAdapter(balanceAdapter);

        balanceViewModel = ViewModelProviders.of(this).get(BalanceViewModel.class);

        Integer balanceId = 1; //TODO get balance

        balanceViewModel.getReceiptsByBalanceId(balanceId).observe(this, receipts1 -> balanceAdapter.setReceipts(receipts1));

        FloatingActionButton fab = findViewById(R.id.addReceiptButton);
        fab.setOnClickListener(view -> {
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
            System.out.println("elo");
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
