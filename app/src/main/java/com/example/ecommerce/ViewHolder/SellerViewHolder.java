package com.example.ecommerce.ViewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ecommerce.Interface.ItemClickListener;
import com.example.ecommerce.R;

public class SellerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView txtSellerId, txtSellerName, txtSellerStatus;
    public ItemClickListener listener;

    public SellerViewHolder(@NonNull View itemView) {
        super(itemView);

        txtSellerId = (TextView) itemView.findViewById(R.id.seller_id);
        txtSellerName = (TextView) itemView.findViewById(R.id.seller_name);
        txtSellerStatus = itemView.findViewById(R.id.seller_status);
    }

    public void setItemClickListener(ItemClickListener listener)
    {
        this.listener = listener;
    }

    @Override
    public void onClick(View v) {
        listener.onClick(v, getAdapterPosition(),false);
    }
}
