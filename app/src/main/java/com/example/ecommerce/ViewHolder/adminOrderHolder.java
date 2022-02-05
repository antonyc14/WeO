package com.example.ecommerce.ViewHolder;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ecommerce.Interface.ItemClickListener;
import com.example.ecommerce.R;

public class adminOrderHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    public TextView txtSellerPhone, txtOrderId, txtOrderStatus, txtUserPhone;
    public ItemClickListener listener;

    public adminOrderHolder(@NonNull View itemView) {
        super(itemView);

        txtOrderId = (TextView) itemView.findViewById(R.id.admin_order_id);
        txtSellerPhone = (TextView) itemView.findViewById(R.id.admin_seller_phone);
        txtUserPhone = (TextView) itemView.findViewById(R.id.admin_user_phone);
        txtOrderStatus = itemView.findViewById(R.id.admin_order_status);
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

