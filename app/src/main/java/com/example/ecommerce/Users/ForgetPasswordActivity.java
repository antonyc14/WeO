package com.example.ecommerce.Users;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ecommerce.Prevalent.Prevalent;
import com.example.ecommerce.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class ForgetPasswordActivity extends AppCompatActivity {

    private String check = "";
    private TextView pageTitle, questionTitle;
    private EditText phoneNumber, question1, question2;
    private Button verifyBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);
        check = getIntent().getStringExtra("check");

        pageTitle = findViewById(R.id.forget_password_title);
        phoneNumber = findViewById(R.id.check_phone);
        question1 = findViewById(R.id.question_1);
        question2 = findViewById(R.id.question_2);
        questionTitle = findViewById(R.id.security_title);
        verifyBtn = findViewById(R.id.security_verify_btn);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(check.equals("settings")){

            pageTitle.setText("Set Question");
            questionTitle.setText("Please Answer the Following Question");

            displayPreviousAnswer();

            verifyBtn.setText("Set Question");
            verifyBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setAnswer();

                }
            });

        }
        else if(check.equals("login")){
            phoneNumber.setVisibility(View.VISIBLE);

            verifyBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    verifyUser();
                }
            });
        }
    }

    private void verifyUser() {
        String phone = phoneNumber.getText().toString();
        String answer1 = question1.getText().toString();
        String answer2 = question2.getText().toString();

        if(!phone.equals("") && !answer1.equals("") && !answer2.equals("")){
            DatabaseReference ref = FirebaseDatabase.getInstance("https://ecommerce-87169-default-rtdb.asia-southeast1.firebasedatabase.app/")
                    .getReference()
                    .child("Users")
                    .child(phone);

            ref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.exists()){
                        String mPhone = snapshot.child("phone").getValue().toString();

                        if(snapshot.hasChild("Security Questions")){
                            String ans1 = snapshot.child("Security Questions").child("answer1").getValue().toString();
                            String ans2 = snapshot.child("Security Questions").child("answer2").getValue().toString();

                            if (!ans1.equals(answer1)){
                                Toast.makeText(ForgetPasswordActivity.this, "Wrong answer", Toast.LENGTH_SHORT).show();
                            }
                            else if(!ans2.equals(answer2)){
                                Toast.makeText(ForgetPasswordActivity.this, "Wrong answer", Toast.LENGTH_SHORT).show();
                            }
                            else{
                                AlertDialog.Builder builder = new AlertDialog.Builder(ForgetPasswordActivity.this);
                                builder.setTitle("New Password");

                                final EditText newPassword = new EditText(ForgetPasswordActivity.this);
                                newPassword.setHint("Type Your Password Here");
                                builder.setView(newPassword);

                                builder.setPositiveButton("Change", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                        if(!newPassword.getText().toString().equals("")){
                                            ref.child("password")
                                                    .setValue(newPassword.getText().toString())
                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            if(task.isSuccessful()) {
                                                                Toast.makeText(ForgetPasswordActivity.this, "Your Password has changed", Toast.LENGTH_SHORT).show();
                                                                Intent intent = new Intent(ForgetPasswordActivity.this, LoginActivity.class);
                                                                startActivity(intent);
                                                            }
                                                        }
                                                    });
                                        }
                                    }
                                });
                                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();
                                    }
                                });
                                builder.show();
                            }
                        }
                        else {
                            Toast.makeText(ForgetPasswordActivity.this, "Please Answer All the Questions!", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else {
                        Toast.makeText(ForgetPasswordActivity.this, "Phone Number is not exist", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }


    }

    private void setAnswer() {
        String answer1 = question1.getText().toString().toLowerCase();
        String answer2 = question2.getText().toString().toLowerCase();

        if(question1.equals("") && question2.equals("")){
            Toast.makeText(ForgetPasswordActivity.this, "Please Answer Both Question", Toast.LENGTH_SHORT).show();

        }
        else
        {
            DatabaseReference ref = FirebaseDatabase.getInstance("https://ecommerce-87169-default-rtdb.asia-southeast1.firebasedatabase.app/")
                    .getReference()
                    .child("Users")
                    .child(Prevalent.currentOnlineUser.getPhone());

            HashMap<String,Object> userdataMap = new HashMap<>();
            userdataMap.put("answer1",answer1);
            userdataMap.put("answer2",answer2);

            ref.child("Security Questions").updateChildren(userdataMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){
                        Toast.makeText(ForgetPasswordActivity.this, "You have set Security Questions", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(ForgetPasswordActivity.this, HomeActivity.class);
                        startActivity(intent);
                    }
                    else{
                        Toast.makeText(ForgetPasswordActivity.this, "Error", Toast.LENGTH_SHORT).show();
                    }
                }
            });

        }
    }

    public void displayPreviousAnswer(){
        DatabaseReference ref = FirebaseDatabase.getInstance("https://ecommerce-87169-default-rtdb.asia-southeast1.firebasedatabase.app/")
                .getReference()
                .child("Users")
                .child(Prevalent.currentOnlineUser.getPhone());

        ref.child("Security Questions").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if(snapshot.exists()){
                    String ans1 = snapshot.child("answer1").getValue().toString();
                    String ans2 = snapshot.child("answer2").getValue().toString();

                    question1.setText(ans1);
                    question2.setText(ans2);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}