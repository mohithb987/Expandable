package com.example.expandable.ViewHolder;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.expandable.Interface.ItemClickListener;
import com.example.expandable.R;

import static com.example.expandable.R.id.product_image;

public class ProductViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    public TextView txtProductName, txtProductDescription, txtProductPrice;
    public ImageView imageView;
    public ItemClickListener listener;
    public ProductViewHolder(@NonNull View itemView) {
        super(itemView);
        imageView =  itemView.findViewById(product_image);
        txtProductName =  itemView.findViewById(R.id.product_name);
        txtProductDescription =  itemView.findViewById(R.id.product_description);
        txtProductPrice =  itemView.findViewById(R.id.product_price);
    }
    public void setItemClickListener(ItemClickListener listener)
    {
        this.listener = listener;
    }

    @Override
    public void onClick(View v) {
        listener.onClick(v, getAdapterPosition(), false);
    }
}
