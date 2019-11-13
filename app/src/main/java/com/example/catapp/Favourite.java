package com.example.catapp;

import java.util.ArrayList;

public class Favourite {
    private static ArrayList<Breeds> favourited=new ArrayList<>();

    public static void addToFavourites(Breeds selectedCat){
        favourited.add(selectedCat);
    }

    public static void removeFromFav(Breeds selectedCat){

        favourited.remove(selectedCat);

    }

    public static ArrayList<Breeds> getFavourited() {
        return favourited;
    }
}
