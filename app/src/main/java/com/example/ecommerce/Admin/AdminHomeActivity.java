package com.example.ecommerce.Admin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.ecommerce.Users.MainActivity;
import com.example.ecommerce.R;

public class AdminHomeActivity extends AppCompatActivity {

    private Button approveVendorBtn, logoutBtn, checkBookingBtn, approveProductBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home);

        approveProductBtn = findViewById(R.id.approve_product_btn);
        approveProductBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminHomeActivity.this, AdminCheckNewProductsActivity.class);
                startActivity(intent);
            }
        });

        // ---------------1--------------
        approveVendorBtn = findViewById(R.id.approve_vendor_btn);
        approveVendorBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminHomeActivity.this, AdminApproveVendorActivity.class);
                intent.putExtra("Admin","Admin");
                startActivity(intent);
            }
        });
        // ----------------1---------------


        // oke
        logoutBtn = findViewById(R.id.admin_logout_btn);

        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminHomeActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

                startActivity(intent);
                finish();
            }
        });


        // ----------------2-----------------
        checkBookingBtn = findViewById(R.id.check_booking_btn);
        checkBookingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminHomeActivity.this, AdminNewOrdersActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

                startActivity(intent);
            }
        });
        // -----------------2---------------------
    }
}