package com.example.ecommerce.Admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.ecommerce.Model.AdminOrders;
import com.example.ecommerce.Model.Sellers;
import com.example.ecommerce.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AdminNewOrdersActivity extends AppCompatActivity {

    private RecyclerView orderList;
    private DatabaseReference ordersRef,sellersRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_new_orders);

        ordersRef = FirebaseDatabase.getInstance("https://ecommerce-87169-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference().child("Orders");
        sellersRef = FirebaseDatabase.getInstance("https://ecommerce-87169-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference().child("Sellers");

        orderList = findViewById(R.id.order_list);
        orderList.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<AdminOrders> options =
                new FirebaseRecyclerOptions.Builder<AdminOrders>()
                .setQuery(ordersRef.orderByChild("orderStatus").equalTo("unknown"), AdminOrders.class)
                .build();

        FirebaseRecyclerAdapter<AdminOrders,AdminOrdersViewHolder> adapter =
                new FirebaseRecyclerAdapter<AdminOrders, AdminOrdersViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull AdminOrdersViewHolder holder, int position, @NonNull AdminOrders model) {
                        holder.txtOrderId.setText(model.getOrderId());
                        holder.txtUserPhone.setText("Customer Number: " + model.getUserPhone());
                        holder.txtSellerPhone.setText("Seller Number: " + model.getSellerPhone());
                        holder.txtOrderStatus.setText("status: " + model.getOrderStatus());


                        holder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                CharSequence options[]= new CharSequence[]{
                                        "Accept",
                                        "Deny"
                                };

                                AlertDialog.Builder builder = new AlertDialog.Builder(AdminNewOrdersActivity.this);
                                builder.setTitle("Accept this Order?");

                                builder.setItems(options, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        String oId = getRef(position).getKey();
                                        if(which == 0){
                                            successOrder(oId, model.getSid());
                                        }
                                        else if(which == 1) {
                                            failOrder(oId);
                                        }
                                        else{
                                            finish();
                                        }
                                    }
                                });
                                builder.show();
                            }
                        });
                    }

                    @NonNull
                    @Override
                    public AdminOrdersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.admin_order_layout,parent,false);
                        return new AdminOrdersViewHolder(view);
                    }
                };
                orderList.setAdapter(adapter);
                adapter.startListening();
    }

    private void successOrder(String uId, String sid) {
        ordersRef.child(uId).child("orderStatus").setValue("success");

        sellersRef.child(sid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Sellers sellers = snapshot.getValue(Sellers.class);

                int value = sellers.getSuccessfulTrans();
                value++;
                sellersRef.child(sid).child("successfulTrans").setValue((value));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    private void failOrder(String uId) {
        ordersRef.child(uId).child("status").setValue("fail");
    }

    public static class AdminOrdersViewHolder extends RecyclerView.ViewHolder{
        public TextView txtOrderStatus, txtUserPhone, txtSellerPhone, txtOrderId;

        public AdminOrdersViewHolder(@NonNull View itemView) {
            super(itemView);
            txtOrderId = (TextView) itemView.findViewById(R.id.admin_order_id);
            txtSellerPhone = (TextView) itemView.findViewById(R.id.admin_seller_phone);
            txtUserPhone = (TextView) itemView.findViewById(R.id.admin_user_phone);
            txtOrderStatus = itemView.findViewById(R.id.admin_order_status);
        }
    }
}