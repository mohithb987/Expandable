package com.example.expandable;

import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.expandable.Model.Products;
import com.example.expandable.ViewHolder.ProductViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;


public class CategoryViewFragment extends Fragment {


    public CategoryViewFragment() { }

    DatabaseReference ProductsRef = FirebaseDatabase.getInstance().getReference().child("Products");
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root= inflater.inflate(R.layout.fragment_category_view, container, false);
        recyclerView = root.findViewById(R.id.recycler_menu1);
        recyclerView.setHasFixedSize(true); //COMPULSORY
        layoutManager = new LinearLayoutManager(root.getContext()); //"root.getContext()" instad of "this"
        recyclerView.setLayoutManager(layoutManager);

        FirebaseRecyclerOptions<Products> options = new FirebaseRecyclerOptions.Builder<Products>().setQuery(ProductsRef.orderByChild("category").equalTo("sportsTshirts"),Products.class).build();
        FirebaseRecyclerAdapter<Products, ProductViewHolder> adapter = new FirebaseRecyclerAdapter<Products, ProductViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull ProductViewHolder holder, int position, @NonNull Products model) {
                //Picasso.get().load(model.getImage()).into(holder.imageView);
                holder.txtProductName.setText(model.getPname());
                holder.txtProductDescription.setText(model.getDescription());
                holder.txtProductPrice.setText("Price = " + model.getPrice() + "$");
                //holder.imageView.setImageURI(Uri.parse("content://com.android.providers.media.documents/document/image%3A25"));
                Uri uri=Uri.parse(model.getImage());

                Picasso.get().load(uri).into(holder.imageView);

            }

            @NonNull
            @Override
            public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_show, parent, false);
                ProductViewHolder holder = new ProductViewHolder(view);
                return holder;
            }


        };

        recyclerView.setAdapter(adapter);
        adapter.startListening();

        return root;
    }
}