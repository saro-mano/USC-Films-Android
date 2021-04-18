package com.example.hw9;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.smarteist.autoimageslider.SliderView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";



    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    private View rootView;


    public void onCreate(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }


    private void callSlideShow(ArrayList currently_playing_movies){
        // Slideshow
        ArrayList<SliderData> sliderDataArrayList = new ArrayList<>();

        // initializing the slider view.
        SliderView sliderView = rootView.findViewById(R.id.slider);


        // passing this array list inside our adapter class.
        SliderAdapter adapter = new SliderAdapter(getActivity(), currently_playing_movies);

        // below method is used to set auto cycle direction in left to
        // right direction you can change according to requirement.
        sliderView.setAutoCycleDirection(SliderView.LAYOUT_DIRECTION_LTR);


        sliderView.setSliderAdapter(adapter);

        // below method is use to set
        // scroll time in seconds.
        sliderView.setScrollTimeInSec(3);

        // to set it scrollable automatically
        // we use below method.
        sliderView.setAutoCycle(true);

        // to start autocycle below method is used.
        sliderView.startAutoCycle();

    }

    private void callTopRated(ArrayList top_rated_movies){
        //Top-Rated
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false);
        RecyclerView recyclerView = rootView.findViewById(R.id.top_rated);
        recyclerView.setLayoutManager(layoutManager);
        RecyclerViewAdapter adapter1 = new RecyclerViewAdapter(getContext(),top_rated_movies);
        recyclerView.setAdapter(adapter1);

    }

    private void callPopular(ArrayList popular_movies){

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false);
        RecyclerView recyclerView = rootView.findViewById(R.id.popular);
        recyclerView.setLayoutManager(layoutManager);
        RecyclerViewAdapter adapter1 = new RecyclerViewAdapter(getContext(),popular_movies);
        recyclerView.setAdapter(adapter1);
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //Inflate the layout for this fragment

        rootView =  inflater.inflate(R.layout.fragment_home, container,
                false);

        RequestQueue queue = Volley.newRequestQueue(getActivity());

        String url = "http://10.0.2.2:8080/homeContent";

        ArrayList<SliderData> currently_playing_movies = new ArrayList<>();
        ArrayList<String> top_rated_movies = new ArrayList<>();
        ArrayList<String> popular_movies = new ArrayList<>();

        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // Display the first 500 characters of the response string.
                        try {
                            JSONArray jsonArray = response.getJSONArray("currently_playing");
                            JSONArray json_top_rated_mov = response.getJSONArray("top_rated_movies");
                            JSONArray json_popular_mov = response.getJSONArray("popular_movies");
                            for (int i = 0 ; i < jsonArray.length() ; i++){
                                JSONObject movie = jsonArray.getJSONObject(i);

                                String url = movie.getString("poster_path");

                                currently_playing_movies.add(new SliderData(url));
                            }

                            callSlideShow(currently_playing_movies);
                            for(int i = 0; i < json_top_rated_mov.length() ; i++){
                                JSONObject movie = json_top_rated_mov.getJSONObject(i);
                                String url = movie.getString("poster_path");
                                top_rated_movies.add(url);
                            }
                            callTopRated(top_rated_movies);


                            for(int i = 0; i < json_popular_mov.length() ; i++){
                                JSONObject movie = json_popular_mov.getJSONObject(i);
                                String url = movie.getString("poster_path");
                                popular_movies.add(url);
                            }
                            callPopular(popular_movies);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println(error);
            }
        });

        // Add the request to the RequestQueue.
        queue.add(jsonRequest);


        return rootView;

    }
}