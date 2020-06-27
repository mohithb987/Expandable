package com.example.expandable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

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

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class AdminAddNewProductActivity extends AppCompatActivity {
    public String categoryName,description,price,productName;
    String saveCurrentDate,saveCurrentTime,downloadImageUrl;
    String productRandomKey;
    public Button addNewButton;
    public ImageView inputProductImage;

    Spinner spinner;
    String finalCategory;

    static final int GalleryPick = 1;
    private StorageReference productImagesRef;
    Uri imageUri;
    ProgressDialog loadingBar;

    EditText inputProductName,inputProductDescription,inputProductPrice;
    private DatabaseReference ProductsRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_add_new_product);
        loadingBar = new ProgressDialog(this);
        categoryName = getIntent().getExtras().get("category").toString();
        addNewButton = findViewById(R.id.add_new_product);
        inputProductImage = findViewById(R.id.select_product_image);
        inputProductName = findViewById(R.id.product_name);
        inputProductDescription = findViewById(R.id.product_description);
        inputProductPrice = findViewById(R.id.product_price);

        spinner=findViewById(R.id.spinner);
        setSpinner();

        productImagesRef = FirebaseStorage.getInstance().getReference().child("Product Images");
        ProductsRef  = FirebaseDatabase.getInstance().getReference().child("Products");
        inputProductImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OpenGallery();
            }
        });

        addNewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateProductData();
            }
        });

        Toast.makeText(this,categoryName, Toast.LENGTH_SHORT).show();
    }
    public void setSpinner(){
        if(categoryName.equals("officetables")){
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.officetables_array, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(adapter);
        }
        else if(categoryName.equals("officechairs")){
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.officechairs_array, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(adapter);
        }
        else if(categoryName.equals("bookshelves")){
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.bookshelves_array, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(adapter);
        }
        else{
            Toast.makeText(AdminAddNewProductActivity.this,"Spinner error",Toast.LENGTH_LONG).show();
        }
         finalCategory = spinner.getSelectedItem().toString();

    }

    public void OpenGallery() {
        Intent galleryIntent = new Intent();
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, GalleryPick);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == GalleryPick && resultCode == RESULT_OK && data!=null){
            imageUri = data.getData();
            inputProductImage.setImageURI(imageUri);
        }
    }


    public void validateProductData(){
        productName = inputProductName.getText().toString();
        price = inputProductPrice.getText().toString();
        description = inputProductDescription.getText().toString();

        if(imageUri == null){
            Toast.makeText(this,"Select an image for sure",Toast.LENGTH_SHORT).show();

        }
        else if(TextUtils.isEmpty(description)){
            Toast.makeText(this,"Please write description for product",Toast.LENGTH_SHORT).show();

        }
        else if(TextUtils.isEmpty(price)){
            Toast.makeText(this,"Please specify price for product",Toast.LENGTH_SHORT).show();

        }
        else if(TextUtils.isEmpty(productName)){
            Toast.makeText(this,"Please specify name for product",Toast.LENGTH_SHORT).show();

        }
        else{
            storeProductInformation();
        }


    }

    private void storeProductInformation() {
        loadingBar.setTitle("Adding Product");
        loadingBar.setMessage("Please wait, where are adding product");
        loadingBar.setCanceledOnTouchOutside(false);
        loadingBar.show();

        Calendar calender = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd,yyyy");
        saveCurrentDate = currentDate.format(calender.getTime());
        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime = currentTime.format(calender.getTime());
        productRandomKey = saveCurrentDate + saveCurrentTime;

        final StorageReference filepath = productImagesRef.child(imageUri.getLastPathSegment()+productRandomKey+".jpg");
        final UploadTask uploadTask = filepath.putFile(imageUri);

        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                String message = e.toString();
                Toast.makeText(AdminAddNewProductActivity.this,"error:"+message,Toast.LENGTH_SHORT).show();
                loadingBar.dismiss();

            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(AdminAddNewProductActivity.this,"Image uploaded succesfully",Toast.LENGTH_SHORT).show();
                Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        if(!task.isSuccessful()){
                            throw task.getException();
                        }
                        downloadImageUrl = filepath.getDownloadUrl().toString();
                        return filepath.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if(task.isSuccessful()) {
                            loadingBar.dismiss();
                            downloadImageUrl = task.getResult().toString();
                            Toast.makeText(AdminAddNewProductActivity.this, "Getting product image url  succesfully", Toast.LENGTH_SHORT).show();
                            SaveProductInformation();
                        }
                    }

                });
            }
        });




    }
    public void SaveProductInformation(){
        HashMap<String,Object> productMap = new HashMap<>();
        productMap.put("pid",productRandomKey);
        productMap.put("date",saveCurrentDate);
        productMap.put("time",saveCurrentTime);
        productMap.put("description",description);
        productMap.put("image",downloadImageUrl);
        productMap.put("category",categoryName);
        productMap.put("subcategory",finalCategory);
        productMap.put("price",price);
        productMap.put("pname",productName);

        ProductsRef.child(productRandomKey).updateChildren(productMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Intent intent = new Intent(AdminAddNewProductActivity.this,AdminCategoryActivity.class);
                    startActivity(intent);
                    loadingBar.dismiss();
                    Toast.makeText(AdminAddNewProductActivity.this, "Product is added succesfully", Toast.LENGTH_SHORT).show();

                }
                else {
                    loadingBar.dismiss();
                    String message = task.getException().toString();
                    Toast.makeText(AdminAddNewProductActivity.this, "ERROR:"+message, Toast.LENGTH_SHORT).show();

                }
            }
        });



    }



}