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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class BalanceAdapter extends RecyclerView.Adapter<BalanceAdapter.BalanceRecyclerViewItemHolder> {

    public interface OnItemClickListener {
        void onItemClick(Receipt receipt);
    }

    private List<Receipt> receipts;
    private final OnItemClickListener listener;

    public static class BalanceRecyclerViewItemHolder extends RecyclerView.ViewHolder {

        public TextView cost;
        public TextView firm;
        public TextView date;
        public TextView time;

        public BalanceRecyclerViewItemHolder(View itemView) {
            super(itemView);

            cost = itemView.findViewById(R.id.cost);
            firm = itemView.findViewById(R.id.firm);
            date = itemView.findViewById(R.id.date);
            time = itemView.findViewById(R.id.time);
        }

        public void bind(final Receipt item, final OnItemClickListener listener){
            cost.setText(item.getCost().toString());
            firm.setText(item.getCompany());
            Date reciptDate = item.getDateTime();
            DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd");
            DateFormat timeFormat = new SimpleDateFormat("hh:mm");
            date.setText(dateFormat.format(reciptDate));
            time.setText(timeFormat.format(reciptDate));
            itemView.setOnClickListener(v -> listener.onItemClick(item));
        }
    }

    public BalanceAdapter(OnItemClickListener listener) {
        receipts = new ArrayList<>();
        this.listener = listener;
    }

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
        holder.bind(receipts.get(position), listener);

    }

    @Override
    public int getItemCount() {
        return receipts.size();
    }
}