package com.billaway.lyfepoints.transaction;


import android.databinding.DataBindingUtil;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.billaway.lyfepoints.R;
import com.billaway.lyfepoints.data.models.BrandTransaction;
import com.billaway.lyfepoints.databinding.ListItemBrandTransactionBinding;

import java.util.List;

public class TransactionsAdapter extends RecyclerView.Adapter<TransactionsAdapter.TransactionsHolder> {
    public final List<BrandTransaction> transactions;
    public LayoutInflater inflater;

    public TransactionsAdapter(List<BrandTransaction> transactions) {
        this.transactions = transactions;
    }

    @Override
    public TransactionsHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (inflater == null)
            inflater = LayoutInflater.from(parent.getContext());
        return new TransactionsHolder(DataBindingUtil.inflate(inflater, R.layout.list_item_brand_transaction, parent, false));
    }

    @Override
    public void onBindViewHolder(TransactionsHolder holder, int position) {
        if (position == 0) {
            holder.binding.tvDate.setText(R.string.date);
            holder.binding.tvAmount.setText(R.string.amount);
            holder.binding.tvDetails.setText(R.string.details);
            holder.binding.tvDate.setTypeface(Typeface.DEFAULT_BOLD);
            holder.binding.tvAmount.setTypeface(Typeface.DEFAULT_BOLD);
            holder.binding.tvDetails.setTypeface(Typeface.DEFAULT_BOLD);
        } else {
            holder.binding.tvDate.setText("34m ago");
            holder.binding.tvAmount.setText("Used data for new Task");
            holder.binding.tvDetails.setText("-50MB");
            holder.binding.tvDate.setTypeface(Typeface.DEFAULT);
            holder.binding.tvAmount.setTypeface(Typeface.DEFAULT);
            holder.binding.tvDetails.setTypeface(Typeface.DEFAULT);
        }
    }

    @Override
    public int getItemCount() {
        return transactions.size() + 1;
    }

    static class TransactionsHolder extends RecyclerView.ViewHolder {

        private ListItemBrandTransactionBinding binding;

        public TransactionsHolder(ListItemBrandTransactionBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
