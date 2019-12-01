package com.wasiollo.neverexpense.receipt;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.material.snackbar.Snackbar;
import com.wasiollo.neverexpense.R;
import com.wasiollo.neverexpense.product.domain.Product;
import com.wasiollo.neverexpense.receipt.domain.Receipt;
import com.wasiollo.neverexpense.receipt.domain.ReceiptWithProducts;
import com.wasiollo.neverexpense.receipt.view_model.ReceiptViewModel;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AddingReceiptActivity extends AppCompatActivity {

    private LinearLayout parentLinearLayout;
    private ReceiptViewModel receiptViewModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
        setContentView(R.layout.adding_activity);

        //        TODO get data from camera

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        receiptViewModel = ViewModelProviders.of(this).get(ReceiptViewModel.class);

        parentLinearLayout = findViewById(R.id.parent_linear_layout);

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
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
    }

    public void onSave(View view) {
        final int childCount = parentLinearLayout.getChildCount();
        List<Product> products = new ArrayList<>();
        for (int i = 0; i < childCount - 1; ++i) { //last one is add field button
            LinearLayout v = (LinearLayout) parentLinearLayout.getChildAt(i);
            String itemName = ((EditText) v.getChildAt(0)).getText().toString();
            String itemPrize = ((EditText) v.getChildAt(1)).getText().toString();
            Product newProduct = new Product();
            newProduct.setName(itemName);
            newProduct.setPrice(Double.valueOf(itemPrize));
            products.add(newProduct);
        }
        System.out.println(products.size());

        EditText totalCostField = findViewById(R.id.sum);
        Double totalCost = Double.valueOf(totalCostField.getText().toString());
        Receipt receipt = new Receipt();
        receipt.setCost(totalCost);
        receipt.setCompany("TEST");
        receipt.setDateTime(new Date());
        receipt.setUserId(1);   //todo
        receipt.setBalanceId(1);    //todo


        ReceiptWithProducts receiptWithProducts = new ReceiptWithProducts();
        receiptWithProducts.setProducts(products);
        receiptWithProducts.setReceipt(receipt);

        receiptViewModel.insert(receiptWithProducts);

        Snackbar.make((View) view.getParent(), "Save clicked", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();

    }
}