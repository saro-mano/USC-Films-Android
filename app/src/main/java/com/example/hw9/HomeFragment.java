package com.example.hw9;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

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

    private View rootView,rootLinear;
    private ConstraintLayout spinner;



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

        spinner = (ConstraintLayout) rootView.findViewById(R.id.loadingLayout);

        RequestQueue queue = Volley.newRequestQueue(getActivity());

        String url = "http://10.0.2.2:8080/homeContent";

        ArrayList<SliderData> currently_playing_movies = new ArrayList<>();
        ArrayList<MovieData> top_rated_movies = new ArrayList<>();
        ArrayList<MovieData> popular_movies = new ArrayList<>();
        ArrayList<MovieData> popular_tv = new ArrayList<>();
        ArrayList<MovieData> top_rated_tv = new ArrayList<>();
        ArrayList<SliderData> trending_tv = new ArrayList<>();

        spinner.setVisibility(View.VISIBLE);
        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // Display the first 500 characters of the response string.
                        try {
                            JSONArray jsonArray = response.getJSONArray("currently_playing");
                            JSONArray json_top_rated_mov = response.getJSONArray("top_rated_movies");
                            JSONArray json_popular_mov = response.getJSONArray("popular_movies");
                            JSONArray json_popular_tv = response.getJSONArray("popular_tv");
                            JSONArray json_top_rated_tv = response.getJSONArray("top_rated_tv");
                            JSONArray json_trending_tv = response.getJSONArray("trending_tv");
                            for (int i = 0 ; i < jsonArray.length() ; i++){
                                JSONObject movie = jsonArray.getJSONObject(i);

                                String url = movie.getString("poster_path");
                                currently_playing_movies.add(new SliderData(url));
                            }

                            callSlideShow(currently_playing_movies);


                            for(int i = 0; i < json_top_rated_mov.length() ; i++){
                                JSONObject movie = json_top_rated_mov.getJSONObject(i);
                                String url = movie.getString("poster_path");
                                String id = movie.getString("id");
                                String media_type = "movie";

                                //to_rate_moviesurl.add("url")
                                //top_id.add("id")
                                //top_media_tyoe("movue")

                                top_rated_movies.add(new MovieData(url,id,media_type));
                            }
                            callTopRated(top_rated_movies);


                            for(int i = 0; i < json_popular_mov.length() ; i++){
                                JSONObject movie = json_popular_mov.getJSONObject(i);
                                String url = movie.getString("poster_path");
                                String id = movie.getString("id");
                                String media_type = "movie";
                                popular_movies.add(new MovieData(url,id,media_type));
                            }
                            callPopular(popular_movies);

                            for (int i = 0 ; i < 6 ; i++){
                                JSONObject movie = json_trending_tv.getJSONObject(i);

                                String url = movie.getString("poster_path");
                                trending_tv.add(new SliderData(url));
                            }

                            for(int i = 0; i < json_popular_tv.length() ; i++){
                                JSONObject movie = json_popular_tv.getJSONObject(i);
                                String url = movie.getString("poster_path");
                                String id = movie.getString("id");
                                String media_type = "tv";
                                popular_tv.add(new MovieData(url,id,media_type));
                            }

                            for(int i = 0; i < json_top_rated_tv.length() ; i++){
                                JSONObject movie = json_top_rated_tv.getJSONObject(i);
                                String url = movie.getString("poster_path");
                                String id = movie.getString("id");
                                String media_type = "tv";
                                top_rated_tv.add(new MovieData(url,id,media_type));
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        spinner.setVisibility(View.GONE);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println(error);
            }
        });

        // Add the request to the RequestQueue.
        queue.add(jsonRequest);

        TextView poweredBy = rootView.findViewById(R.id.poweredBy);
        poweredBy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "https://www.themoviedb.org/";
                Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                i.setPackage("com.android.chrome");
                try {
                    getContext().startActivity(i);
                } catch (ActivityNotFoundException e) {
                    Toast.makeText(getContext(), "unable to open chrome", Toast.LENGTH_SHORT).show();
                    i.setPackage(null);
                    getContext().startActivity(i);
                }
            }
        });


        TextView tvBtn = rootView.findViewById(R.id.tv_btn);
        TextView movieBtn = rootView.findViewById(R.id.movie_btn);
        tvBtn.setTextColor(getResources().getColor(R.color.colorPrimary));
        tvBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvBtn.setTextColor(getResources().getColor(R.color.white));
                movieBtn.setTextColor(getResources().getColor(R.color.colorPrimary));
                callPopular(popular_tv);
                callTopRated(top_rated_tv);
                callSlideShow(trending_tv);
            }
        });


        movieBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                movieBtn.setTextColor(getResources().getColor(R.color.white));
                tvBtn.setTextColor(getResources().getColor(R.color.colorPrimary));
                callPopular(popular_movies);
                callTopRated(top_rated_movies);
                callSlideShow(currently_playing_movies);
            }
        });


        return rootView;

    }
}