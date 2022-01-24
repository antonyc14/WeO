package com.example.ecommerce.Seller;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.ecommerce.R;

public class SellerPaymentConfirmationStatusActivity extends AppCompatActivity {

    private Button backToLoginBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_payment_confirmation_status);

        backToLoginBtn = findViewById(R.id.back_to_login_btn);

        backToLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SellerPaymentConfirmationStatusActivity.this, SellerLoginActivity.class);
                startActivity(intent);
            }
        });
    }
}