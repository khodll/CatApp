package com.example.catapp;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.github.mikephil.charting.charts.RadarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.RadarData;
import com.github.mikephil.charting.data.RadarDataSet;
import com.github.mikephil.charting.data.RadarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.gson.Gson;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link BreedDetailFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link BreedDetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BreedDetailFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String catID;
    private String catName;

    private ArrayList<CatImport> catImports;

    private OnFragmentInteractionListener mListener;

    public BreedDetailFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment BreedDetailFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static BreedDetailFragment newInstance(String param1, String param2) {
        BreedDetailFragment fragment = new BreedDetailFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            catID = getArguments().getString("CAT_ID");
            catName = getArguments().getString("CAT_NAME");
        }
        final RequestQueue requestQueue= Volley.newRequestQueue(getContext());
        final Response.Listener<String> responseListener = new Response.Listener<String>(){
            @Override
            public void onResponse(String response){
                CatImport[] importedCatList = new Gson().fromJson(response,CatImport[].class);
                catImports= new ArrayList<CatImport>(Arrays.asList(importedCatList));
                if (catImports.size()==0){
                    Toast.makeText(getActivity(),"Sorry, this cat's data is not available",Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(getContext(),MainActivity.class);
                    startActivity(intent);
                }else{
                    setFragment(catImports);
                }
            }
        };
        Response.ErrorListener errorListener=new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error){
                System.out.println("Request failed");
            }

        };
        String url = "https://api.thecatapi.com/v1/images/search?breed_id="+catID;
        StringRequest stringRequest = new StringRequest(Request.Method.GET,url,responseListener,errorListener);
        requestQueue.add(stringRequest);


    }

    private void setFragment(ArrayList<CatImport> catsImports) {
        final Breeds selectedCat = catsImports.get(0).getBreeds().get(0);
        TextView catName = getView().findViewById(R.id.catName);
        catName.setText(catImports.get(0).getBreeds().get(0).getName());
        TextView description = getView().findViewById(R.id.description);
        description.setText(catImports.get(0).getBreeds().get(0).getDescription());
        ImageView catPic = getView().findViewById(R.id.catPic);
        Glide.with(getContext()).load(catImports.get(0).getUrl()).into(catPic);



        final ImageView likeButton = getView().findViewById(R.id.likeButton);
        if(Favourite.getFavourited().size()>0){
            for(int i=0;i<Favourite.getFavourited().size();i++) {
                System.out.println("selected cat: "+selectedCat.getName()+" | checking favCat: "+Favourite.getFavourited().get(i).getName());
                if (selectedCat.getName().equals(Favourite.getFavourited().get(i).getName())) {
                    System.out.println("match found");
                    likeButton.setImageResource(R.drawable.liked);
                }
            }
        }

        likeButton.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("clicked");
                AppCompatActivity activity = (AppCompatActivity)getView().getContext();
                if(likeButton.getDrawable().getConstantState()==getResources().getDrawable(R.drawable.like).getConstantState()){
                    likeButton.setImageResource(R.drawable.liked);
                    Favourite.addToFavourites(selectedCat);
                }
                else{
                    likeButton.setImageResource(R.drawable.like);
                    for(int i=0;i<Favourite.getFavourited().size();i++){
                        if(selectedCat.getName().equals(Favourite.getFavourited().get(i).getName())){
                            Favourite.removeFromFav(Favourite.getFavourited().get(i));
                            System.out.println("removed from favourites list");
                        }
                    }

                }
            }
        }));
        setChart(selectedCat);

        TextView temperament = getView().findViewById(R.id.temperament);
        TextView origin = getView().findViewById(R.id.origin);
        TextView weight = getView().findViewById(R.id.weight);
        TextView lifespan = getView().findViewById(R.id.lifespan);
        TextView wiki = getView().findViewById(R.id.labelWiki);


        ArrayList<Method> attributeMethods = new ArrayList<>();
        HashMap<Method, ConstraintLayout> methodRowMap = new HashMap<>();
        HashMap<Method,TextView> methodTextMap = new HashMap<>();

        try {
            attributeMethods.add(Breeds.class.getDeclaredMethod("getTemperament", null));
            attributeMethods.add(Breeds.class.getDeclaredMethod("getOrigin", null));
            attributeMethods.add(Breeds.class.getDeclaredMethod("getWeight", null));
            attributeMethods.add(Breeds.class.getDeclaredMethod("getLife_span", null));
            attributeMethods.add(Breeds.class.getDeclaredMethod("getWikipedia_url", null));
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        try {
            methodRowMap.put(Breeds.class.getDeclaredMethod("getTemperament", null),(ConstraintLayout)getView().findViewById(R.id.rowTemp));
            methodRowMap.put(Breeds.class.getDeclaredMethod("getOrigin", null),(ConstraintLayout) getView().findViewById(R.id.rowTemp));
            methodRowMap.put(Breeds.class.getDeclaredMethod("getWeight", null),(ConstraintLayout) getView().findViewById(R.id.rowWeight));
            methodRowMap.put(Breeds.class.getDeclaredMethod("getLife_span", null),(ConstraintLayout) getView().findViewById(R.id.rowLife));
            methodRowMap.put(Breeds.class.getDeclaredMethod("getWikipedia_url",null),(ConstraintLayout) getView().findViewById(R.id.rowWiki));
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        try {
            methodTextMap.put(Breeds.class.getDeclaredMethod("getTemperament", null),(TextView)getView().findViewById(R.id.temperament));
            methodTextMap.put(Breeds.class.getDeclaredMethod("getOrigin", null),(TextView)getView().findViewById(R.id.origin));
            methodTextMap.put(Breeds.class.getDeclaredMethod("getWeight", null),(TextView)getView().findViewById(R.id.weight));
            methodTextMap.put(Breeds.class.getDeclaredMethod("getLife_span", null),(TextView)getView().findViewById(R.id.lifespan));
            methodTextMap.put(Breeds.class.getDeclaredMethod("getWikipedia_url",null),(TextView)getView().findViewById(R.id.wiki));
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        for (int i=0;i<attributeMethods.size();i++){
            if(checkNullGetters(selectedCat,attributeMethods.get(i))==true){
                methodRowMap.get(attributeMethods.get(i)).setVisibility(View.GONE);
            }
            else{
                try {
                    if(attributeMethods.get(i).equals(Breeds.class.getDeclaredMethod("getWeight", null))) {
                        methodTextMap.get(attributeMethods.get(i)).setText(selectedCat.getWeight().getMetric() + " kg");
                    }
                    else if(attributeMethods.get(i).equals(Breeds.class.getDeclaredMethod("getLife_span",null))) {
                        methodTextMap.get(attributeMethods.get(i)).setText(selectedCat.getLife_span() + " years");
                    }
                    else {
                        methodTextMap.get(attributeMethods.get(i)).setText(attributeMethods.get(i).invoke(selectedCat).toString());
                    }
                    } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }

                }
            }




    }

    private void setChart(Breeds selectedCat){
        RadarChart radarChart = getView().findViewById(R.id.RadarChart);
        ArrayList<RadarEntry> radarEntries = new ArrayList<>();
        ArrayList<String> labels= new ArrayList<>();

        RadarChart friendlyChart = getView().findViewById(R.id.friendlyChart);
        ArrayList<RadarEntry> friendEntries = new ArrayList<>();
        ArrayList<String> friendLabels = new ArrayList<>();
        Method[] methods = Breeds.class.getDeclaredMethods();

        for(int i=0;i<methods.length;i++){
            if(methods[i].getReturnType().equals(Integer.TYPE)){
                if(!checkNullGetters(selectedCat, methods[i])){
                    try {
                        int result=(int)methods[i].invoke(selectedCat);
                        if (result!=0){
                            if(methods[i].getName().contains("friendly")){
                                friendEntries.add(new RadarEntry(result,result));
                                friendLabels.add(methods[i].getName().substring(3));
                            }
                            else{
                                radarEntries.add(new RadarEntry(result, result));
                                labels.add(methods[i].getName().substring(3));
                            }
                        }

                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                    }
                }

            }
        }
        //formatting chart
        RadarDataSet radarDataSet = new RadarDataSet(radarEntries,"");
        RadarData radarData = new RadarData(radarDataSet);
        XAxis xAxis = radarChart.getXAxis();
        YAxis yAxis = radarChart.getYAxis();
        xAxis.setTextSize(14f);
        yAxis.setTextSize(10f);
        yAxis.setTextColor(Color.GRAY);
        yAxis.setAxisMinimum(0f);
        yAxis.setAxisMaximum(5f);
        yAxis.setGranularity(1f);
        yAxis.setSpaceBottom(0);
        yAxis.setSpaceTop(0);
        xAxis.setValueFormatter(new IndexAxisValueFormatter(labels));
        radarChart.setData(radarData);
        radarChart.invalidate();
        radarDataSet.setDrawFilled(true);
        radarDataSet.setFillColor(Color.MAGENTA);
        radarDataSet.setColors(ColorTemplate.PASTEL_COLORS);
        radarDataSet.setValueTextColor(Color.BLUE);
        radarDataSet.setValueTextSize(10f);
        radarChart.getDescription().setEnabled(false);
        radarChart.getLegend().setEnabled(false);

        //formatting second chart on friendliness of cat
        RadarDataSet friendDataSet = new RadarDataSet(friendEntries, "");
        RadarData friendData = new RadarData(friendDataSet);
        XAxis friendXAxis = friendlyChart.getXAxis();
        YAxis friendYAxis = friendlyChart.getYAxis();
        friendXAxis.setTextSize(14f);
        friendYAxis.setTextSize(10f);
        friendYAxis.setTextColor(Color.GRAY);
        friendYAxis.setAxisMinimum(0f);
        friendYAxis.setAxisMaximum(5f);
        friendYAxis.setGranularity(1f);
        friendYAxis.setSpaceBottom(0);
        friendYAxis.setSpaceTop(0);
        friendXAxis.setValueFormatter(new IndexAxisValueFormatter(friendLabels));
        friendlyChart.setData(friendData);
        friendlyChart.invalidate();
        friendDataSet.setDrawFilled(true);
        friendDataSet.setFillColor(Color.MAGENTA);
        friendDataSet.setColors(ColorTemplate.PASTEL_COLORS);
        friendDataSet.setValueTextColor(Color.BLUE);
        friendDataSet.setValueTextSize(10f);
        friendlyChart.getDescription().setEnabled(false);
        friendlyChart.getLegend().setEnabled(false);
    }

    private boolean checkNullGetters(Breeds selectedCat, Method method) {
        Object obj=null;
        System.out.println(selectedCat.getName() +" at checkNullGetters");
        try {
            obj= method.invoke(selectedCat);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        if (obj==null){
            return true;
        }
        else{
            return false;
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_breed_detail, container, false);
    }
    @Override
    public void onViewCreated(final View view, Bundle savedInstanceState){

    }
    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
