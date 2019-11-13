package com.wasiollo.neverexpense.balance.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.wasiollo.neverexpense.R;
import com.wasiollo.neverexpense.receipt.domain.Receipt;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class BalanceAdapter extends RecyclerView.Adapter<BalanceAdapter.BalanceRecyclerViewItemHolder> {
    private List<Receipt> receipts;

    public static class BalanceRecyclerViewItemHolder extends RecyclerView.ViewHolder {

        public TextView cost;
        public TextView firm;
        public TextView date;
        public TextView time;

        public BalanceRecyclerViewItemHolder(View v) {
            super(v);

            cost = v.findViewById(R.id.cost);
            firm = v.findViewById(R.id.firm);
            date = v.findViewById(R.id.date);
            time = v.findViewById(R.id.time);
        }
    }

    public BalanceAdapter() {}

    public void setReceipts(List<Receipt> receiptList) {
        receipts = receiptList;
    }

    @Override
    public BalanceRecyclerViewItemHolder onCreateViewHolder(ViewGroup parent,
                                                            int viewType) {

        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.balance_recycler_view_item, parent, false);

        return new BalanceRecyclerViewItemHolder(v);
    }

    @Override
    public void onBindViewHolder(BalanceRecyclerViewItemHolder holder, int position) {
        holder.cost.setText(receipts.get(position).getCost().toString());
        holder.firm.setText(receipts.get(position).getCompany().toString());
        Date reciptDate = receipts.get(position).getDateTime();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd");
        DateFormat timeFormat = new SimpleDateFormat("hh:mm");
        holder.date.setText(dateFormat.format(reciptDate));
        holder.time.setText(timeFormat.format(reciptDate));
    }

    @Override
    public int getItemCount() {
        return receipts.size();
    }
}