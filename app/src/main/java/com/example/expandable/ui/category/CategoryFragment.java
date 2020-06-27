package com.example.expandable.ui.category;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.activity.OnBackPressedDispatcher;
import androidx.activity.OnBackPressedDispatcherOwner;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;

import com.example.expandable.CategoryViewFragment;
import com.example.expandable.R;
import com.example.expandable.ui.home.HomeFragment;

public class CategoryFragment extends Fragment{

    ImageView officetables,officechairs,bookshelves;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_category, container, false);
        officetables=root.findViewById(R.id.officetables);
        officechairs=root.findViewById(R.id.officechairs);
        bookshelves=root.findViewById(R.id.bookshelves);
        officetables.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle=new Bundle();
                bundle.putString("key","officetables");
                CategoryViewFragment category=new CategoryViewFragment();
                category.setArguments(bundle);
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_category ,category).addToBackStack("tag1").commit();
            }
        });


        officechairs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle=new Bundle();
                bundle.putString("category","officechairs");
                CategoryViewFragment category=new CategoryViewFragment();
                category.setArguments(bundle);
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_category ,category).addToBackStack("tag1").commit();
            }
        });


        bookshelves.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle=new Bundle();
                bundle.putString("category","officetables");
                CategoryViewFragment category=new CategoryViewFragment();
                category.setArguments(bundle);
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_category ,category).addToBackStack("tag1").commit();
            }
        });

        return root;
    }
}