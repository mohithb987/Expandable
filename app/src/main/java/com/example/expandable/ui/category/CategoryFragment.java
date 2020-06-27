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

import com.example.expandable.R;
import com.example.expandable.ui.home.HomeFragment;

public class CategoryFragment extends Fragment implements OnBackPressedDispatcherOwner {

    private CategoryViewModel homeViewModel;
    ImageView img;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(CategoryViewModel.class);
        View root = inflater.inflate(R.layout.fragment_category, container, false);
        img=root.findViewById(R.id.imageView2);
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getChildFragmentManager().beginTransaction().replace(R.id.fragment_category ,new HomeFragment()).addToBackStack("tag").commit();
            }
        });
        return root;
    }


    @NonNull
    @Override
    public OnBackPressedDispatcher getOnBackPressedDispatcher() {
        getChildFragmentManager().beginTransaction().replace(R.id.fragment_home,new CategoryFragment()).commit();
        return null;
    }
}