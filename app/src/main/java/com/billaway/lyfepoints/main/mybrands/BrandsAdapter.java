package com.billaway.lyfepoints.main.mybrands;


import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.billaway.lyfepoints.databinding.ListItemMyBrandsBinding;
import com.billaway.lyfepoints.transaction.TransactionActivity;

import java.util.List;

public class BrandsAdapter extends RecyclerView.Adapter<BrandsAdapter.BrandsHolder> {
    private LayoutInflater inflater;
    private final List<BrandListItemViewModel> brandsItems;

    public BrandsAdapter(@NonNull List<BrandListItemViewModel> brandsItems) {
        this.brandsItems = brandsItems;
    }


    @Override
    public BrandsHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (inflater == null)
            inflater = LayoutInflater.from(parent.getContext());
        return new BrandsHolder(ListItemMyBrandsBinding.inflate(inflater, parent, false));
    }

    @Override
    public void onBindViewHolder(BrandsHolder holder, int position) {
        holder.binding.setItem(brandsItems.get(position));
        holder.itemView.setOnClickListener(view -> view.getContext().startActivity(new Intent(view.getContext(), TransactionActivity.class)));
    }

    @Override
    public int getItemCount() {
        return brandsItems.size();
    }

    static class BrandsHolder extends RecyclerView.ViewHolder {

        private ListItemMyBrandsBinding binding;

        public BrandsHolder(ListItemMyBrandsBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
