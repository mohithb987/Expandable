package com.example.expandable;

import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.expandable.Model.Products;
import com.example.expandable.Model.Reviews;
import com.example.expandable.Prevalent.Prevalent;
import com.example.expandable.ViewHolder.ReviewViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

import static java.lang.Boolean.valueOf;


public class ProductDetailsFragment extends Fragment {
    public ProductDetailsFragment() {
        // Required empty public constructor
    }
    ImageView pimage;
    TextView pname,pprice,pdesc;
    EditText pcount;

    DatabaseReference db;
    String pid,uname,count;

    EditText enter_review;
    Button add_review,add_to_cart;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    @Override
    public void onCreate(Bundle savedInstanceState) { // is called before onCreateView()
        super.onCreate(savedInstanceState);
        Bundle bundle = this.getArguments();
        pid= bundle.getString("key");
        uname= Prevalent.onlineUser.getName();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) { // Passing the container is important in order for the system to apply layout parameters to the root view of the inflated layout, specified by the parent view in which it's going.

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_product_details, container, false);

        recyclerView=view.findViewById(R.id.recycler_reviews);
        recyclerView.setHasFixedSize(true);
        layoutManager=new LinearLayoutManager(view.getContext());
        recyclerView.setLayoutManager(layoutManager);
        db= FirebaseDatabase.getInstance().getReference();
        FirebaseRecyclerOptions<Reviews> options=new FirebaseRecyclerOptions.Builder<Reviews>().setQuery(db.child("Reviews").orderByChild("pid").equalTo(pid),Reviews.class).build();
        FirebaseRecyclerAdapter<Reviews, ReviewViewHolder> adapter=new FirebaseRecyclerAdapter<Reviews, ReviewViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull ReviewViewHolder holder, int position, @NonNull Reviews model) {
            holder.review.setText(model.getUsername()+"\n"+model.getReview());
            }

            @NonNull
            @Override
            public ReviewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_review, parent, false);
                ReviewViewHolder holder = new ReviewViewHolder(view);
                return holder;
            }};
               recyclerView.setAdapter(adapter);
        adapter.startListening();
        pcount=view.findViewById(R.id.pcount);

        add_to_cart=view.findViewById(R.id.add_to_cart);
        add_to_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                count=pcount.getText().toString();
                if(TextUtils.isEmpty(count)){
                    pcount.setError("enter count value");
                }
                else {
                    int countval = Integer.parseInt(count);
                    if (countval <= 0 || countval > 9) {
                        pcount.setError("Enter a value between 0-9");
                    }
                   else {
                        addingToCart();
                    }
                }

            }
        });

        enter_review=view.findViewById(R.id.enter_review);
        add_review=view.findViewById(R.id.add_review);
        pimage=view.findViewById(R.id.pimage);
        pname=view.findViewById(R.id.pname);
        pprice=view.findViewById(R.id.pprice);
        pdesc=view.findViewById(R.id.pdesc);
        pcount=view.findViewById(R.id.pcount);

        db.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.child("Products").child(pid).exists()) {

                        Products productsData = snapshot.child("Products").child(pid).getValue(Products.class);
                        Uri uri=Uri.parse(productsData.getImage());
                        Picasso.get().load(uri).into(pimage);
                        pname.setText(productsData.getPname());
                        pprice.setText("Rs."+productsData.getPrice());
                        pdesc.setText(productsData.getDescription());
                    } else {
                        Log.d("product findd error", "Product not found in storaagee.");
                    }
                }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        add_review.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                HashMap<String,Object> reviewsMap = new HashMap<>();
                reviewsMap.put("review",enter_review.getText().toString());
                reviewsMap.put("username",uname);
                reviewsMap.put("pid",pid);
                String u_id=Prevalent.onlineUser.getPhone();
                db=FirebaseDatabase.getInstance().getReference();
                db.child("Reviews").child(u_id).updateChildren(reviewsMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            enter_review.clearFocus();
                            enter_review.setText("");
                            Toast.makeText(getActivity(),"Review Added",Toast.LENGTH_SHORT).show();
                        }
                        else{
                            Toast.makeText(getActivity(),"Review Not added",Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }
        });

        return view;
    }

    public void addingToCart() {
        String saveCurrentTime, saveCurrentDate;
        Calendar calForDate = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd,yyyy");
        saveCurrentDate = currentDate.format(calForDate.getTime());
        SimpleDateFormat currentTime = new SimpleDateFormat("MMM dd,yyyy");
        saveCurrentTime = currentTime.format(calForDate.getTime());

        final HashMap<String, Object> cartMap = new HashMap<>();
        cartMap.put("pid", pid);
        cartMap.put("pname", pname.getText().toString());
        cartMap.put("price", pprice.getText().toString());
        cartMap.put("date", saveCurrentDate);
        cartMap.put("time",saveCurrentTime);
        cartMap.put("quantity",count);
        db.child("Cart").child("User_View").child(Prevalent.onlineUser.getPhone()).child(pid).updateChildren(cartMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful())
                {
                    db.child("Cart").child("Admin View").child(Prevalent.onlineUser.getPhone()).child(pid).updateChildren(cartMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful())
                            {
                                Toast.makeText(getActivity(),"Product Added Successfully",Toast.LENGTH_SHORT).show();
                                getActivity().getSupportFragmentManager().popBackStack();
                            }
                        }
                    });
                }
            }
        });
    }



}