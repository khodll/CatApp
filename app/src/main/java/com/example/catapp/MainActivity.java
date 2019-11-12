package com.example.catapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements SearchResultsFragment.OnFragmentInteractionListener,
        BreedDetailFragment.OnFragmentInteractionListener{
    public static ArrayList<Breeds> favourites=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView navBar = findViewById(R.id.bottomNav);
        FragmentManager fragmentManager = getSupportFragmentManager();
        final FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        final FavouritesFragment favouritesFragment = new FavouritesFragment();
        navBar.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch(menuItem.getItemId()){
                    case R.id.browseButton:
                        Toast.makeText(MainActivity.this,"Browse",Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                        break;
                    case R.id.favButton:
                        Toast.makeText(MainActivity.this,"Favourites",Toast.LENGTH_SHORT).show();
                        fragmentTransaction.replace(R.id.fragment_slot,favouritesFragment);
                        fragmentTransaction.commit(); break;

                }
                return true;
            }
        });

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