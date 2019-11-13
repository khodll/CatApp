package com.example.catapp;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

public class SearchResultViewHolder extends RecyclerView.ViewHolder {
    ImageView catPic;
    TextView catName;
    View view;
    String catID;
    View previewLayout;

    public SearchResultViewHolder(@NonNull View itemView) {
        super(itemView);
        view = itemView;
        catName= view.findViewById(R.id.catName);

        previewLayout = view.findViewById(R.id.previewLayout);
        previewLayout.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppCompatActivity activity = (AppCompatActivity)view.getContext();
                BreedDetailFragment breedDetailFragment = new BreedDetailFragment();
                Bundle bundle = new Bundle();
                bundle.putString("CAT_ID",catID);
                bundle.putString("CAT_NAME",catName.getText().toString());
                breedDetailFragment.setArguments(bundle);
                FragmentManager fragmentManager = activity.getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment_slot, breedDetailFragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        }));
    }

}
