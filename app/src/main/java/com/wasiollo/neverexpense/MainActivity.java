package com.wasiollo.neverexpense;

import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.wasiollo.neverexpense.balance.BalanceAdapter;
import com.wasiollo.neverexpense.receipt.domain.Receipt;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

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

        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        recyclerView = findViewById(R.id.balanceRecyclerView);

        List<Receipt> receipts= new ArrayList<>();
        Receipt receipt1 = new Receipt();
        receipt1.setCompany("Aa");
        receipt1.setCost(123.44);
        receipt1.setDateTime(new Date());
        receipt1.setId(1);
        receipt1.setUserId(1);
        receipt1.setBalanceId(1);

        Receipt receipt2 = new Receipt();
        receipt2.setCompany("Bb");
        receipt2.setCost(166.44);
        receipt2.setDateTime(new Date());
        receipt2.setId(1);
        receipt2.setUserId(1);
        receipt2.setBalanceId(1);

        Receipt receipt3 = new Receipt();
        receipt3.setCompany("CCC");
        receipt3.setCost(23.44);
        receipt3.setDateTime(new Date());
        receipt3.setId(1);
        receipt3.setUserId(1);
        receipt3.setBalanceId(1);

        Receipt receipt4 = new Receipt();
        receipt4.setCompany("DDD DDD DD");
        receipt4.setCost(13.44);
        receipt4.setDateTime(new Date());
        receipt4.setId(1);
        receipt4.setUserId(1);
        receipt4.setBalanceId(1);

        receipts.add(receipt1);
        receipts.add(receipt2);
        receipts.add(receipt3);
        receipts.add(receipt4);

        balanceAdapter = new BalanceAdapter(receipts);

        recyclerView.setAdapter(balanceAdapter);

        FloatingActionButton fab = findViewById(R.id.addReceiptButton);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
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

        return super.onOptionsItemSelected(item);
    }
}
