package com.example.expandable;

import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.expandable.Model.Products;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;


public class ProductDetailsFragment extends Fragment {
    public ProductDetailsFragment() {
        // Required empty public constructor
    }
    ImageView pimage;
    TextView pname,pprice,pdesc;
    EditText pcount;

    DatabaseReference db;
    String pid;

    @Override
    public void onCreate(Bundle savedInstanceState) { // is called before onCreateView()
        super.onCreate(savedInstanceState);
        Bundle bundle = this.getArguments();
        pid= bundle.getString("key");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) { // Passing the container is important in order for the system to apply layout parameters to the root view of the inflated layout, specified by the parent view in which it's going.

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_product_details, container, false);

        pimage=view.findViewById(R.id.pimage);
        pname=view.findViewById(R.id.pname);
        pprice=view.findViewById(R.id.pprice);
        pdesc=view.findViewById(R.id.pdesc);
        pcount=view.findViewById(R.id.pcount);
        db= FirebaseDatabase.getInstance().getReference();

        db.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.child("Products").child(pid).exists()) {

                        Products productsData = snapshot.child("Products").child(pid).getValue(Products.class);
                        Uri uri=Uri.parse(productsData.getImage());
                        Picasso.get().load(uri).into(pimage);
                        pname.setText(productsData.getPname());
                        pprice.setText(productsData.getPrice());
                        pdesc.setText(productsData.getDescription());
                    } else {
                        Log.d("product findd error", "Product not found in storaagee.");
                    }
                }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



        return view;
    }
}