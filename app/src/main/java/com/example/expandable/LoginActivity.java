package com.example.expandable;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.expandable.Model.Users;
import com.example.expandable.Prevalent.Prevalent;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rey.material.widget.CheckBox;

import io.paperdb.Paper;

public class LoginActivity extends AppCompatActivity {
     EditText loginPhone,loginPassword;
     Button loginAccountButton;
     ProgressDialog loadingBar;
     String parentDbname = "users";
     CheckBox checkBox ;
     TextView userTextView;
     TextView adminTextView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        loginAccountButton = findViewById(R.id.loginButton);
        loginPhone = findViewById(R.id.login_phone_number);
        loginPassword = findViewById(R.id.login_password);
        loadingBar = new ProgressDialog(this);
        checkBox = findViewById(R.id.remember_me_checkbox);
        userTextView = findViewById(R.id.userTextview);
        adminTextView = findViewById(R.id.adminTextview);

        Paper.init(this);
        loginAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginUser();
            }
        });
        adminTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginAccountButton.setText("Login as Admin");
                adminTextView.setVisibility(View.INVISIBLE);
                userTextView.setVisibility(View.VISIBLE);
                parentDbname="Admins";
            }
        });
        userTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginAccountButton.setText("LOGIN");
                adminTextView.setVisibility(View.VISIBLE);
                userTextView.setVisibility(View.INVISIBLE);
                parentDbname="Users";
            }
        });

    }
    public void LoginUser(){
        String phone = loginPhone.getText().toString();
        String Password =loginPassword.getText().toString();

         if(TextUtils.isEmpty(Password)){
            loginPassword.setError("Name is required");
        }
        else if(TextUtils.isEmpty(phone)){
            loginPhone.setError("Phone number is required");
        }
        else{
            loadingBar.setTitle("Logging in");
            loadingBar.setMessage("Please wait, where are checking credentials");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();
            AllowAccess(phone,Password);
        }
    }
    public void AllowAccess(final String phone, final String Password){
        if(checkBox.isChecked()){
            Paper.book().write(Prevalent.UserPhoneKey,phone);
            Paper.book().write(Prevalent.UserPasswordKey,Password);


        }
        final DatabaseReference  rootRef;
        rootRef = FirebaseDatabase.getInstance().getReference();
        rootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if((snapshot.child(parentDbname).child(phone).exists())){
                    Users userData = snapshot.child(parentDbname).child(phone).getValue(Users.class); //REMEMBER ("Users" => class)
                    if(userData.getPhone().equals(phone)){
                        if(userData.getPassword().equals(Password)){
                            if(parentDbname.equals("Admins")){
                                Toast.makeText(LoginActivity.this ,"logged in Succesfully",Toast.LENGTH_SHORT).show();
                                loadingBar.dismiss();
                                Intent intent = new Intent(LoginActivity.this,AdminCategoryActivity.class);
                                startActivity(intent);
                                finish();
                            }
                            else{
                                Toast.makeText(LoginActivity.this,"logged in Succesfully",Toast.LENGTH_SHORT).show();
                                loadingBar.dismiss();
                                Intent intent = new Intent(LoginActivity.this,HomeActivity.class);
                                startActivity(intent);
                                finish();

                            }

                        }
                        else{
                            Toast.makeText(LoginActivity.this,"Password is Incorrect",Toast.LENGTH_SHORT).show();

                        }
                    }
                }
                else{
                    Toast.makeText(LoginActivity.this,"Account with this Id doesn't exist",Toast.LENGTH_SHORT).show();
                    loadingBar.dismiss();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}