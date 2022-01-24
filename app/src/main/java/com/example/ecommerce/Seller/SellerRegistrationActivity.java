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

import com.example.ecommerce.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class SellerRegistrationActivity extends AppCompatActivity {

    private Button sellerToLoginBtn,registerBtn;
    private EditText sName, sPhone, sEmail, sAddress, sPassword;
    private FirebaseAuth mAuth;
    private ProgressDialog loadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_registration);

        mAuth = FirebaseAuth.getInstance();

        sName = findViewById(R.id.seller_name);
        sPhone = findViewById(R.id.seller_phone);
        sEmail = findViewById(R.id.seller_email);
        sAddress = findViewById(R.id.seller_address);
        sPassword = findViewById(R.id.seller_password);
        loadingBar = new ProgressDialog(this);

        registerBtn = findViewById(R.id.seller_register_btn);

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerSeller();
            }
        });


        sellerToLoginBtn = findViewById(R.id.seller_to_login_btn);

        sellerToLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SellerRegistrationActivity.this, SellerLoginActivity.class);
                startActivity(intent);
            }
        });
    }

    private void registerSeller() {

        String name = sName.getText().toString();
        String phone = sPhone.getText().toString();
        String email = sEmail.getText().toString();
        String address = sAddress.getText().toString();
        String password = sPassword.getText().toString();

        if(!name.equals("") && !password.equals("") && !phone.equals("") && !address.equals("") && !email.equals("")){
            loadingBar.setTitle("Creating Seller Account");
            loadingBar.setMessage("Please wait, while we are checking the credentials");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();

            mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        final DatabaseReference reference;
                        reference = FirebaseDatabase.getInstance("https://ecommerce-87169-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference();

                        String sid = mAuth.getCurrentUser().getUid();

                        HashMap<String ,Object> sellerMap = new HashMap<>();
                        sellerMap.put("status","inactive");
                        sellerMap.put("sid",sid);
                        sellerMap.put("phone",phone);
                        sellerMap.put("email",email);
                        sellerMap.put("address",address);
                        sellerMap.put("name",name);

                        reference.child("Sellers").child(sid).updateChildren(sellerMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                loadingBar.dismiss();
                                Toast.makeText(SellerRegistrationActivity.this, "Registered Successfully", Toast.LENGTH_SHORT).show();

                                Intent intent = new Intent(SellerRegistrationActivity.this, SellerPaymentActivity.class);
                                intent.putExtra("sellerName", name);
                                intent.putExtra("sellerID", sid);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                                finish();
                            }
                        });

                    }
                }
            });
        }
        else{
            Toast.makeText(this, "Please Complete All Data Needed", Toast.LENGTH_SHORT).show();
        }
    }
}