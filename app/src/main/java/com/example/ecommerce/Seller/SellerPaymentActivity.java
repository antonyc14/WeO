package com.example.ecommerce.Seller;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ecommerce.R;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;

public class SellerPaymentActivity extends AppCompatActivity {

    private Button checkStatusBtn;

    private RelativeLayout relativeLayout,relativeLayout4;
    private ImageView inputInvoiceImage;

    private TextView salin1,salin2;

    private String checker = "";
    private StorageReference storageInvoiceRef;

    private DatabaseReference sellersRef;
    private DatabaseReference invoicesRef;
    private static final int GalleryPick = 1;
    private ProgressDialog loadingBar;
    private Uri imageUri;
    private String invoiceRandomKey,downloadImageUrl;


//    private StorageTask uploadTask;
//    private String myUrl = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_payment);

        invoicesRef = FirebaseDatabase.getInstance("https://ecommerce-87169-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference().child("Invoices");
        sellersRef = FirebaseDatabase.getInstance("https://ecommerce-87169-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference().child("Sellers");
        storageInvoiceRef = FirebaseStorage.getInstance().getReference().child("Invoice pictures");
        
        relativeLayout = findViewById(R.id.relative_layout_payment_upload);
        relativeLayout4 = findViewById(R.id.relative_layout_payment4);
        inputInvoiceImage = findViewById(R.id.invoice_picture);
        loadingBar = new ProgressDialog(this);

        relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checker = "clicked";
                OpenGallery();
            }
        });

        checkStatusBtn = findViewById(R.id.check_status_btn);
        salin1 = findViewById(R.id.salin1);
        salin1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("Nomor Rekening", "4140359217");
                clipboard.setPrimaryClip(clip);
                Toast.makeText(SellerPaymentActivity.this, "Nomor Rekening Disimpan Ke Clipboard", Toast.LENGTH_SHORT).show();
            }
        });

        salin2 = findViewById(R.id.salin2);
        salin2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("Jumlah Yang harus Dibayar", "100000");
                clipboard.setPrimaryClip(clip);
                Toast.makeText(SellerPaymentActivity.this, "Jumlah Pembayaran Disimpan Ke Clipboard", Toast.LENGTH_SHORT).show();
            }
        });

        checkStatusBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (checker.equals("clicked"))
                {
                    uploadInvoice();
//                    Intent intent = new Intent(SellerPaymentActivity.this, SellerPaymentConfirmationStatusActivity.class);
//                    startActivity(intent);
                }
                else
                {
                    Toast.makeText(SellerPaymentActivity.this, "Harus Upload Bukti Pembayaran Terlebih Dahulu", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    private void uploadInvoice() {
        loadingBar.setTitle("Uploading Invoice");
        loadingBar.setMessage("Dear Seller, please wait while we are uploading your invoice.");
        loadingBar.setCanceledOnTouchOutside(false);
        loadingBar.show();
        Intent thisIntent = getIntent();

        String sellerName = thisIntent.getStringExtra("sellerName");
        String sellerID = thisIntent.getStringExtra("sellerID");
        
        invoiceRandomKey = sellerID + "-" +sellerName;

        final StorageReference filePath = storageInvoiceRef.child(imageUri.getLastPathSegment() + sellerName + "_Invoice" + ".jpg");
        final UploadTask uploadTask = filePath.putFile(imageUri);

        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                String message = e.toString();
                checker = "";
                Toast.makeText(SellerPaymentActivity.this, "Error: " + message, Toast.LENGTH_SHORT).show();
                loadingBar.dismiss();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(SellerPaymentActivity.this, "Invoice uploaded Successfully...", Toast.LENGTH_SHORT).show();

                Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        if (!task.isSuccessful()) {
                            throw task.getException();
                        }

                        downloadImageUrl = filePath.getDownloadUrl().toString();
                        return filePath.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if (task.isSuccessful())
                        {
                            downloadImageUrl = task.getResult().toString();

                            Toast.makeText(SellerPaymentActivity.this, "got the invoice Url Successfully...", Toast.LENGTH_SHORT).show();

                            SaveInvoiceInfoToDatabase();
                        }
                    }
                });
            }
        });
    }

    private void SaveInvoiceInfoToDatabase() {
        Intent thisIntent = getIntent();

        HashMap<String, Object> invoiceMap = new HashMap<>();

        invoiceMap.put("sid", thisIntent.getStringExtra("sellerID"));
        invoiceMap.put("sellerName", thisIntent.getStringExtra("sellerName"));
        invoiceMap.put("image", downloadImageUrl);
        
        invoicesRef.child(invoiceRandomKey).updateChildren(invoiceMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task)
                    {
                        if (task.isSuccessful())
                        {
                            Intent intent = new Intent(SellerPaymentActivity.this, SellerPaymentConfirmationStatusActivity.class);
                            startActivity(intent);

                            loadingBar.dismiss();
                            Toast.makeText(SellerPaymentActivity.this, "Invoice is added successfully..", Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            loadingBar.dismiss();
                            String message = task.getException().toString();
                            Toast.makeText(SellerPaymentActivity.this, "Error: " + message, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void OpenGallery() {
        Intent galleryIntent = new Intent();
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, GalleryPick);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GalleryPick && resultCode == RESULT_OK && data != null) {
            imageUri = data.getData();
            inputInvoiceImage.setVisibility(View.VISIBLE);
            relativeLayout4.setVisibility(View.INVISIBLE);
            inputInvoiceImage.setImageURI(imageUri);
        }
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setTitle("Really Exit?")
                .setMessage("Are you sure you want to exit?")
                .setNegativeButton(android.R.string.no, null)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                        SellerPaymentActivity.super.onBackPressed();
                    }
                }).create().show();
    }



}