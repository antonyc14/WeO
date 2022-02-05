package com.example.ecommerce.Admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.ecommerce.Model.Products;
import com.example.ecommerce.Model.Sellers;
import com.example.ecommerce.R;
import com.example.ecommerce.ViewHolder.ItemViewHolder;
import com.example.ecommerce.ViewHolder.ProductViewHolder;
import com.example.ecommerce.ViewHolder.SellerViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class AdminApproveVendorActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    private DatabaseReference unverifiedSellersRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_approve_vendor);

        unverifiedSellersRef = FirebaseDatabase.getInstance("https://ecommerce-87169-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference().child("Sellers");

        recyclerView = findViewById(R.id.admin_vendor_checklist);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<Sellers> options =
                new FirebaseRecyclerOptions.Builder<Sellers>()
                        .setQuery(unverifiedSellersRef.orderByChild("status").equalTo("inactive"), Sellers.class).build();

        FirebaseRecyclerAdapter<Sellers, SellerViewHolder> adapter =
                new FirebaseRecyclerAdapter<Sellers, SellerViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull SellerViewHolder holder, int position, @NonNull Sellers model) {
                        holder.txtSellerId.setText(model.getSid());
                        holder.txtSellerName.setText(model.getName());
                        holder.txtSellerStatus.setText("Status = " + model.getStatus());

                        holder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                final String sID = model.getSid();

                                CharSequence options[] = new CharSequence[]
                                        {
                                                "Yes",
                                                "No"
                                        };
                                AlertDialog.Builder builder = new AlertDialog.Builder(AdminApproveVendorActivity.this);
                                builder.setTitle("Do you want to Approve this Vendor. Are You Sure?");
                                builder.setItems(options, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        if(which == 0){
                                            changeVendorState(sID);
                                        }
                                        if(which == 1){

                                        }
                                    }
                                });
                                builder.show();
                            }
                        });
                    }

                    @NonNull
                    @Override
                    public SellerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.seller_layout, parent, false);
                        SellerViewHolder holder = new SellerViewHolder(view);
                        return holder;
                    }
                };
        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }

    private void changeVendorState(String sID) {
        unverifiedSellersRef.child(sID).child("successfulTrans").setValue(0);
        unverifiedSellersRef.child(sID).child("status").setValue("active")
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(AdminApproveVendorActivity.this, "Vendor is Approved", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}