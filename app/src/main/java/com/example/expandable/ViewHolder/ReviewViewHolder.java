package com.example.expandable.ViewHolder;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.expandable.Interface.ItemClickListener;
import com.example.expandable.R;

import java.text.BreakIterator;

public class ReviewViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    public TextView review;
    public ItemClickListener listener;
    public ReviewViewHolder(View view) {
        super(view);
        review=view.findViewById(R.id.review);
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
