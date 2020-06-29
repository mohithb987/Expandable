package com.example.expandable;

import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
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

    RecyclerView recyclerView1;
    RecyclerView.LayoutManager layoutManager1;
    String productID,category;

    public CategoryViewFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_category_view, container, false);
        Bundle bundle = this.getArguments();
        category = bundle.getString("key");
        recyclerView1 = root.findViewById(R.id.recycler_menu1);
        DatabaseReference ProductsRef1 = FirebaseDatabase.getInstance().getReference().child("Products");

        recyclerView1.setHasFixedSize(true);
        layoutManager1 = new LinearLayoutManager(root.getContext());
        recyclerView1.setLayoutManager(layoutManager1);

        FirebaseRecyclerOptions<Products> options = new FirebaseRecyclerOptions.Builder<Products>().setQuery(ProductsRef1.orderByChild("category").equalTo(category),Products.class).build();
        FirebaseRecyclerAdapter<Products, ProductViewHolder> adapter = new FirebaseRecyclerAdapter<Products, ProductViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull ProductViewHolder holder, int position, @NonNull final Products model) {
                //Picasso.get().load(model.getImage()).into(holder.imageView);
                holder.txtProductName.setText(model.getPname());
                holder.txtProductDescription.setText(model.getDescription());
                holder.txtProductPrice.setText("Price = " + model.getPrice() + "$");
                //holder.imageView.setImageURI(Uri.parse("content://com.android.providers.media.documents/document/image%3A25"));
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        productID=model.getPid();
                        Bundle bundle=new Bundle();
                        bundle.putString("key",productID);
                        ProductDetailsFragment productDetailsFragment=new ProductDetailsFragment();
                        productDetailsFragment.setArguments(bundle);
                        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_category_view,productDetailsFragment).addToBackStack("tag").commit();
                    }
                });
                Uri uri=Uri.parse(model.getImage());
                Picasso.get().load(uri).into(holder.imageView);

            }

            @NonNull
            @Override
            public ProductViewHolder onCreateViewHolder(@NonNull final ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_show, parent, false);
                ProductViewHolder holder = new ProductViewHolder(view);
                return holder;
            }


        };

        recyclerView1.setAdapter(adapter);
        adapter.startListening();

        return root;

    }
}