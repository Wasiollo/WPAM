package com.wasiollo.neverexpense.receipt;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.material.snackbar.Snackbar;
import com.wasiollo.neverexpense.MainActivity;
import com.wasiollo.neverexpense.R;
import com.wasiollo.neverexpense.receipt.view_model.ReceiptViewModel;

public class AddingReceiptActivity extends AppCompatActivity {

    private LinearLayout parentLinearLayout;
    private ReceiptViewModel receiptViewModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.receipt_activity);

        //        TODO get data from camera

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        receiptViewModel = ViewModelProviders.of(this).get(ReceiptViewModel.class);

        parentLinearLayout = findViewById(R.id.parent_linear_layout);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        Intent myIntent = new Intent(getApplicationContext(), MainActivity.class);
        startActivityForResult(myIntent, 0);
        return true;
    }

    public void onAddField(View v) {
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View rowView = inflater.inflate(R.layout.adding_field, null);
        // Add the new row before the add field button.
        parentLinearLayout.addView(rowView, parentLinearLayout.getChildCount() - 1);
    }

    public void onDelete(View v) {
        parentLinearLayout.removeView((View) v.getParent());
        Snackbar.make((View) ((View) v.getParent()).getParent(), "Removed line", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
    }

    public void onSave(View view) {
        Snackbar.make((View) view.getParent(), "Save clicked", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
    }
}