package com.wasiollo.neverexpense;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.wasiollo.neverexpense.balance.adapter.BalanceAdapter;
import com.wasiollo.neverexpense.receipt.domain.Receipt;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private RecyclerView.Adapter balanceAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AppCompatDelegate.setDefaultNightMode(
                AppCompatDelegate.MODE_NIGHT_AUTO);

        setContentView(R.layout.balance_activity);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        recyclerView = findViewById(R.id.balanceRecyclerView);

        List<Receipt> receipts = getData();

        balanceAdapter = new BalanceAdapter(receipts);

        recyclerView.setAdapter(balanceAdapter);

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

    private List<Receipt> getData() {
        List<Receipt> receipts = new ArrayList<>();

        Receipt receipt1 = new Receipt();
        receipt1.setCompany("Biedronka");
        receipt1.setCost(123.44);
        receipt1.setDateTime(new Date());
        receipt1.setId(1);
        receipt1.setUserId(1);
        receipt1.setBalanceId(1);

        Receipt receipt2 = new Receipt();
        receipt2.setCompany("Stonka");
        receipt2.setCost(166.44);
        receipt2.setDateTime(new Date());
        receipt2.setId(2);
        receipt2.setUserId(1);
        receipt2.setBalanceId(1);

        receipts.add(receipt1);
        receipts.add(receipt2);
        return receipts;
    }
}
