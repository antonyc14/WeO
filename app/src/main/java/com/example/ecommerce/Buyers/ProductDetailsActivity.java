package com.example.ecommerce.Buyers;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.example.ecommerce.Model.Products;
import com.example.ecommerce.Model.Sellers;
import com.example.ecommerce.Prevalent.Prevalent;
import com.example.ecommerce.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.spec.ECField;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class ProductDetailsActivity extends AppCompatActivity {

//    private FloatingActionButton addToCartBtn;
    private Button contactBtn;
    private ImageView productImage;
    private TextView productPrice, productDescription, productName, sellerName, sellerProject;
    private String saveCurrentDate,saveCurrentTime;
    private String productID = "", state = "Normal";

    private DatabaseReference productRef, ordersRef, sellersRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);


        productID = getIntent().getStringExtra("pid");

        contactBtn = findViewById(R.id.contact_btn);

//        numberButton = (ElegantNumberButton) findViewById(R.id.number_btn);
        sellerName = findViewById(R.id.seller_name_details);
        sellerProject = findViewById(R.id.detail_successful_project);
        productImage = (ImageView) findViewById(R.id.product_image_details);
        productPrice = (TextView) findViewById(R.id.product_price_details);
        productDescription = (TextView) findViewById(R.id.product_description_details);
        productName = (TextView) findViewById(R.id.product_name_details);
        productRef = FirebaseDatabase.getInstance("https://ecommerce-87169-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference().child("Products");
        ordersRef = FirebaseDatabase.getInstance("https://ecommerce-87169-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference().child("Orders");
        sellersRef = FirebaseDatabase.getInstance("https://ecommerce-87169-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference().child("Sellers");

        getProductDetail(productID);

        contactBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                redirectToWhatsapp(productID);
//                addToCart();
            }
        });
    }

    private void redirectToWhatsapp(String productID) {
        productRef.child(productID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    Calendar calendar = Calendar.getInstance();
                    SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy");
                    saveCurrentDate = currentDate.format(calendar.getTime());
                    SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
                    saveCurrentTime = currentTime.format(calendar.getTime());

                    Products products = snapshot.getValue(Products.class);

                    String message = "Hallo saya tertarik dengan produk ini, " + products.getPname() + " , tolong berikan saya penjelasan lebih untuk produk ini, Terima Kasih!";

                    String url = null;
                    try {
                        url = "https://api.whatsapp.com/send?phone=" + "+62" + products.getSellerPhone().substring(1)  + "&text=" + URLEncoder.encode(message, "UTF-8");
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }

                    HashMap<String, Object> orderMap = new HashMap<>();
                    orderMap.put("orderId", (Prevalent.currentOnlineUser.getName() + " " + saveCurrentDate + " " + saveCurrentTime));
                    orderMap.put("date", saveCurrentDate);
                    orderMap.put("time", saveCurrentTime);
                    orderMap.put("sellerPhone", products.getSellerPhone());
                    orderMap.put("userPhone", Prevalent.currentOnlineUser.getPhone());
                    orderMap.put("sid", products.getSid());
                    orderMap.put("orderStatus", "unknown"); //failed, unknown, success

                    ordersRef.child(Prevalent.currentOnlineUser.getName() +" "+ saveCurrentDate+""+saveCurrentTime).updateChildren(orderMap);

                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(url));
                    startActivity(i);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    private void addToCart() {
        String saveCurrentTime, saveCurrentDate;
        Calendar calForDate = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("MM dd, yyyy");
        saveCurrentDate = currentDate.format(calForDate.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime = currentTime.format(calForDate.getTime());



        final DatabaseReference cartListRef = FirebaseDatabase.getInstance("https://ecommerce-87169-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference().child("Cart List");

        final HashMap<String,Object> cartMap = new HashMap<>();
        cartMap.put("pid",productID);
        cartMap.put("pname",productName.getText().toString());
        cartMap.put("price",productPrice.getText().toString());
        cartMap.put("date",saveCurrentDate);
        cartMap.put("time",saveCurrentTime);
        cartMap.put("quantity",1);
        cartMap.put("discount","");

        cartListRef.child("User View").child(Prevalent.currentOnlineUser.getPhone())
                .child("Products").child(productID)
                .updateChildren(cartMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            cartListRef.child("User View").child(Prevalent.currentOnlineUser.getPhone())
                                    .child("Products").child(productID)
                                    .updateChildren(cartMap)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            Toast.makeText(ProductDetailsActivity.this, "Added to Cart", Toast.LENGTH_SHORT).show();
                                            Intent intent = new Intent(ProductDetailsActivity.this, HomeActivity.class);
                                            startActivity(intent);
                                        }
                                    });
                        }
                    }
                });
    }

    private void getProductDetail(String productID) {

        productRef.child(productID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    Products products = snapshot.getValue(Products.class);

                    String id = products.getSid();

                    productName.setText(products.getPname());
                    productPrice.setText("Rp." + products.getPrice());
                    productDescription.setText(products.getDescription());
                    Picasso.get().load(products.getImage()).into(productImage);
                    sellerName.setText(products.getSellerName());

                    try {
                        sellersRef.child(id).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if(snapshot.exists()){
                                    Sellers sellers = snapshot.getValue(Sellers.class);

                                    int trans = -1;
                                    try {
                                        trans = sellers.getSuccessfulTrans();
                                    }
                                    catch (Exception e){
                                        trans = 0;
                                    }
                                    if(sellers.getSuccessfulTrans() != 0){
                                        sellerProject.setText("Vendor Successful Transaction = " + sellers.getSuccessfulTrans());
                                    }
                                    else{
                                        sellerProject.setText("Vendor Successful Transaction = 0");
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }catch (Exception e){
                        sellerProject.setText("Vendor Successful Transaction = 0");
                    }


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });




    }
}