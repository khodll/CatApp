package com.example.catapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity implements SearchResultsFragment.OnFragmentInteractionListener,
        BreedDetailFragment.OnFragmentInteractionListener, FavouritesFragment.OnFragmentInteractionListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView navBar = findViewById(R.id.bottomNav);

        navBar.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch(menuItem.getItemId()){
                    case R.id.browseButton:
                        Toast.makeText(MainActivity.this,"Browse",Toast.LENGTH_SHORT).show();
                        SearchResultsFragment searchResultsFragment= new SearchResultsFragment();
                        FragmentManager fragmentManager = getSupportFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.fragment_slot, searchResultsFragment);
                        fragmentTransaction.commit();
                        break;
                    case R.id.favButton:
                        Toast.makeText(MainActivity.this,"Favourites",Toast.LENGTH_SHORT).show();
                        FragmentManager fragmentManager2 = getSupportFragmentManager();
                        FragmentTransaction fragmentTransaction2 = fragmentManager2.beginTransaction();
                        FavouritesFragment favouritesFragment = new FavouritesFragment();
                        fragmentTransaction2.replace(R.id.fragment_slot,favouritesFragment);
                        fragmentTransaction2.commit(); break;

                }
                return true;
            }
        });
        SearchResultsFragment searchResultsFragment= new SearchResultsFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_slot, searchResultsFragment);
        fragmentTransaction.commit();
    }

    public void onClickSearch(View view){
        EditText searchBar = findViewById(R.id.searchBar);
        String searchText = searchBar.getText().toString();
        SearchResultsFragment searchResultsFragment= new SearchResultsFragment();
        Bundle bundle = new Bundle();
        bundle.putString("SEARCH_TEXT",searchText);
        searchResultsFragment.setArguments(bundle);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_slot, searchResultsFragment);
        fragmentTransaction.commit();
    }
    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}