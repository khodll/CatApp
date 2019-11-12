package com.example.catapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class SearchResultAdapter extends RecyclerView.Adapter<SearchResultViewHolder> {
    ArrayList<Breeds> breeds;

    public SearchResultAdapter(ArrayList<Breeds> breeds){
        this.breeds=breeds;
    }

    @NonNull
    @Override
    public SearchResultViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_result_chunk, parent, false);

        SearchResultViewHolder searchResultViewHolder = new SearchResultViewHolder(view);
        return searchResultViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull SearchResultViewHolder holder, int position) {
        holder.catName.setText(breeds.get(position).getName());
        holder.catID=breeds.get(position).getId();
    }

    @Override
    public int getItemCount() {
        return breeds.size();
    }
}
