package com.example.expandable.ViewHolder;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.expandable.Interface.ItemClickListener;
import com.example.expandable.R;

public class CartViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
    public TextView txtProductName,txtProductPrice,txtQuantity;
    public ItemClickListener listener;
    public Button remove_btn;
    public CartViewHolder(@NonNull View itemView) {
        super(itemView);
        remove_btn=itemView.findViewById(R.id.cart_remove_btn);
        txtProductName =  itemView.findViewById(R.id.pname);
        txtProductPrice =  itemView.findViewById(R.id.pprice);
        txtQuantity =  itemView.findViewById(R.id.count);
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
