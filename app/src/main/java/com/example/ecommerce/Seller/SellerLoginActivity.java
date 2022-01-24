package com.example.ecommerce.Seller;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.ecommerce.Buyers.MainActivity;
import com.example.ecommerce.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SellerLoginActivity extends AppCompatActivity {
    private Button sellerLoginBtn;
    private EditText sEmail, sPassword;

    private FirebaseAuth mAuth;
    private ProgressDialog loadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_login);

        mAuth = FirebaseAuth.getInstance();
        sEmail = findViewById(R.id.seller_login_email);
        sPassword = findViewById(R.id.seller_login_password);
        sellerLoginBtn = findViewById(R.id.seller_login_btn);
        loadingBar = new ProgressDialog(this);

        sellerLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginSeller();
            }
        });
    }

    private void loginSeller() {
        String email = sEmail.getText().toString();
        String password = sPassword.getText().toString();

        if(!email.equals("") && !password.equals("")) {
            loadingBar.setTitle("Wedding Organizer Account Login");
            loadingBar.setMessage("Please wait, while we are checking the credentials");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();

            mAuth.signInWithEmailAndPassword(email,password)
                    .addOnCompleteListener(task ->{
                        if (task.isSuccessful()) {
                            Intent intent = new Intent(SellerLoginActivity.this, SellerHomeActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                            finish();
                        }
                        else{
                            Toast.makeText(this, "Email/Password is not recognized", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(SellerLoginActivity.this, SellerLoginActivity.class);
                            startActivity(intent);
                        }
                    });
        }
        else {
            Toast.makeText(this, "Email/Password Cannot Be Empty", Toast.LENGTH_SHORT).show();
        }
    }
}