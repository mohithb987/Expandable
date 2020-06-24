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
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {
    private EditText inputName;
    private  EditText inputPhone;
    private  EditText inputPassword;
    private  Button createAccountButton;
    private ProgressDialog loadingBar;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
         createAccountButton = findViewById(R.id.registerButton);
         inputName = findViewById(R.id.register_name);
         inputPhone = findViewById(R.id.register_phone_number);
         inputPassword = findViewById(R.id.register_password);
         loadingBar = new ProgressDialog(this);
        createAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateAccount();
            }
        });

    }
    private void CreateAccount(){
        String name = inputName.getText().toString();
        String phone = inputPhone.getText().toString();
        String Password = inputPassword.getText().toString();
        if(TextUtils.isEmpty(name)){
            inputName.setError("Name is required");
        }
        else if(TextUtils.isEmpty(Password)){
            inputPassword.setError("Name is required");
        }
        else if(TextUtils.isEmpty(phone)){
            inputPhone.setError("Phone number is required");
        }
        else{
            loadingBar.setTitle("Create Account");
            loadingBar.setMessage("Please wait, where are checking");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();
            ValidatephoneNumber(name,phone,Password);
        }
    }
    private void ValidatephoneNumber(final String name, final String phone, final String Password){
        final DatabaseReference rootRef;
        rootRef = FirebaseDatabase.getInstance().getReference();
        rootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(!(snapshot.child("users").child(phone).exists())){
                    Map<String,Object> userData = new HashMap<>();
                    userData.put("phone",phone);
                    userData.put("password",Password);
                    userData.put("name",name);
                    rootRef.child("users").child(phone).updateChildren(userData).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(RegisterActivity.this, "User created succesfully", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                                startActivity(intent);
                            } else {
                                loadingBar.dismiss();
                                Toast.makeText(RegisterActivity.this, "Error" + task.getException(), Toast.LENGTH_SHORT).show();

                            }
                        }
                    });

                    }
                else{

                    Toast.makeText(RegisterActivity.this,"This Phone has already been registered",Toast.LENGTH_SHORT).show();
                    loadingBar.dismiss();
                    Toast.makeText(RegisterActivity.this,"Please try again with new number",Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(RegisterActivity.this,MainActivity.class);
                    startActivity(intent);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

}