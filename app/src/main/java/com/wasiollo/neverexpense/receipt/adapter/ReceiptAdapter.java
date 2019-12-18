package com.wasiollo.neverexpense.receipt.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.wasiollo.neverexpense.R;
import com.wasiollo.neverexpense.product.domain.Product;

import java.util.ArrayList;
import java.util.List;

public class ReceiptAdapter extends RecyclerView.Adapter<ReceiptAdapter.ReceiptRecyclerViewItemHolder> {
    private List<Product> products;

    public static class ReceiptRecyclerViewItemHolder extends RecyclerView.ViewHolder {

        public TextView cost;
        public TextView product;

        public ReceiptRecyclerViewItemHolder(View v) {
            super(v);

            cost = v.findViewById(R.id.cost);
            product = v.findViewById(R.id.product);
        }
    }

    public ReceiptAdapter() {
        products = new ArrayList<>();
    }

    public void setProducts(List<Product> products) {
        this.products = products;
        notifyDataSetChanged();
    }

    @Override
    public ReceiptRecyclerViewItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.receipt_recycler_view_item, parent, false);

        return new ReceiptRecyclerViewItemHolder(v);
    }

    @Override
    public void onBindViewHolder(ReceiptRecyclerViewItemHolder holder, int position) {
        holder.cost.setText(String.format("%.2f", products.get(position).getPrice()));
        holder.product.setText(products.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return products.size();
    }
}