package com.example.hw9;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Movie;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;

import org.apache.commons.lang3.ArrayUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Details extends AppCompatActivity {


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void setYoutube(String trailer){
        YouTubePlayerView youTubePlayerView = findViewById(R.id.youTubePlayerView);
        ImageView backgroundImg = findViewById(R.id.backgroundImg);

        backgroundImg.setVisibility(View.GONE);
        youTubePlayerView.setVisibility(View.VISIBLE);
//        backgroundImg.setTranslationZ(Float.parseFloat("0dp"));
//        youTubePlayerView.setTranslationZ(Float.parseFloat("1dp"));
        getLifecycle().addObserver(youTubePlayerView);

        youTubePlayerView.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
            @Override
            public void onReady(@NonNull YouTubePlayer youTubePlayer) {
                String videoId = trailer;
                youTubePlayer.cueVideo(videoId, 0);
            }
        });
    }

    private void callRecommeded(ArrayList top_rated_movies){
        //Top-Rated
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.HORIZONTAL,false);
        RecyclerView recyclerView = findViewById(R.id.recommendedPicks);
        recyclerView.setLayoutManager(layoutManager);
        RecViewAdapter adapter1 = new RecViewAdapter(getApplicationContext(),top_rated_movies);
        recyclerView.setAdapter(adapter1);

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void checkWatchList(SharedPreferences pref, String id, String media_type, String profile_path){
        SharedPreferences.Editor editor = pref.edit();
        ImageView watchlistBtn = findViewById(R.id.watchlistBtn);
        String key = "watchlist";

        Boolean flag = true;
        String main_arr_str = pref.getString(key,null);
        System.out.println("In Detail: " + main_arr_str);
        if(main_arr_str != null && main_arr_str.length() != 0){
            String[] arr = main_arr_str.split("#");
            for(int j = 0 ; j < arr.length ; j++) {
                String[] temp = arr[j].split(",");
                if (temp[0].equals(id) && temp[1].equals(media_type)) {
                    //removing the list item
                    watchlistBtn.setImageResource(R.drawable.ic_baseline_remove_circle_outline_24);;
                    flag = false;
                }
            }
            if(flag){
                watchlistBtn.setImageResource(R.drawable.ic_baseline_add_circle_outline_24);
            }
        }

        watchlistBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Boolean flag = true;
                String key = "watchlist";
                String value = id + "," + media_type+ "," + profile_path;
                String main_arr_str = pref.getString(key,null);
                System.out.println("Before: " + main_arr_str);
                if(main_arr_str != null && main_arr_str.length() != 0){
                    String[] arr = main_arr_str.split("#");
                    for(int j = 0 ; j < arr.length ; j++) {
                        String[] temp = arr[j].split(",");
                        if (temp[0].equals(id) && temp[1].equals(media_type)) {
                            //removing the list item
                            arr = ArrayUtils.remove(arr, j);
                            main_arr_str = String.join("#", arr);
                            main_arr_str += "#";
                            System.out.println("Delimited"+ main_arr_str);
                            flag = false;
                            watchlistBtn.setImageResource(R.drawable.ic_baseline_add_circle_outline_24);
                        }
                    }
                    if(flag){
                        main_arr_str = main_arr_str + value + "#";
                        watchlistBtn.setImageResource(R.drawable.ic_baseline_remove_circle_outline_24);
                    }
                }
                else{
                    main_arr_str = value + "#";
                    watchlistBtn.setImageResource(R.drawable.ic_baseline_remove_circle_outline_24);
                }
                editor.putString(key,main_arr_str);
                editor.commit();

//                if(temp == null){
//                    editor.putString(key,profile_path);
//                    watchlistBtn.setImageResource(R.drawable.ic_baseline_remove_circle_outline_24);;
//                }
//                else{
//                    editor.remove(key);
//                    watchlistBtn.setImageResource(R.drawable.ic_baseline_add_circle_outline_24);
//                }
            }
        });
    }


    GridView simpleList;
    ArrayList<Cast> castList=new ArrayList<>();
    ArrayList<Review> reviewList = new ArrayList<>();
    ArrayList<MovieData> recommendedList = new ArrayList<>();
    RecyclerView reviewView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try
        {
            this.getSupportActionBar().hide();
        }
        catch (NullPointerException e){}

        setContentView(R.layout.activity_details);

        //Getting the Data from the Previous Intent
        Bundle bundle = getIntent().getExtras();
        String id = bundle.getString("id");
        String media_type = bundle.getString("media_type");

        SharedPreferences pref = this.getSharedPreferences("MyPref", 0); // 0 - for private mode


        ConstraintLayout loadingScreen = (ConstraintLayout) findViewById(R.id.loadingDetail);


        //Volley Request:

        RequestQueue queue = Volley.newRequestQueue(this);

        String url = "http://10.0.2.2:8080/getDetails?id="+id+"&media_type="+media_type;

        System.out.println(url);

        ArrayList<SliderData> currently_playing_movies = new ArrayList<>();
        loadingScreen.setVisibility(View.VISIBLE);
        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @RequiresApi(api = Build.VERSION_CODES.O)
                    @Override
                    public void onResponse(JSONObject response) {
                        // Display the first 500 characters of the response string.
                        try {
//                            System.out.println(response);
                            String title = response.getString("title");
                            String trailer = response.getString("trailer");
                            String overview = response.getString("overview");
                            String genre = response.getString("genres");
                            String year = response.getString("year");
                            String background_path = response.getString("backdrop_path");
                            String profile_path = response.getString("poster_path");
                            JSONArray cast_arr = response.getJSONArray("movie_cast");
                            JSONArray rev_arr = response.getJSONArray("movie_rev");
                            JSONArray recommended_arr = response.getJSONArray("recommended_mov");


                            //Setting Youtube
                            if(trailer.equals("None")){
                                ImageView backgroundImg = findViewById(R.id.backgroundImg);
                                YouTubePlayerView youTubePlayerView = findViewById(R.id.youTubePlayerView);

                                backgroundImg.setVisibility(View.VISIBLE);
                                youTubePlayerView.setVisibility(View.GONE);

//                                backgroundImg.setTranslationZ(Float.parseFloat("1dp"));
//                                youTubePlayerView.setTranslationZ(Float.parseFloat("0dp"));
                                Glide.with(getApplicationContext())
                                        .asBitmap()
                                        .load(background_path)
                                        .into(backgroundImg);
                            }

                            else{
                                setYoutube(trailer);
                            }

                            //Setting Title
                            TextView setTitle = findViewById(R.id.textView3);
                            setTitle.setText(title);

                            //Setting Overview
                            com.borjabravo.readmoretextview.ReadMoreTextView setOverview = findViewById(R.id.overviewContent);
                            setOverview.setText(overview);

                            //Setting Genre
                            TextView setGenre = findViewById(R.id.genreContent);
                            setGenre.setText(genre);

                            //Setting Year
                            TextView setYear = findViewById(R.id.yearContent);
                            setYear.setText(year);

                            //Calling WatchList Check

                            checkWatchList(pref,id,media_type,profile_path);

                            //Setting Cast
                            for(int i = 0 ; i < cast_arr.length() ; i++){
                                JSONObject castDet = cast_arr.getJSONObject(i);
                                String castURL = castDet.getString("profile_pic");
                                String castName = castDet.getString("name");
                                castList.add(new Cast(castURL,castName));
                            }
                            simpleList = (GridView) findViewById(R.id.simpleGridView);
                            CastAdapter myAdapter=new CastAdapter(getApplicationContext(),R.layout.cast_cards,castList);
                            simpleList.setAdapter(myAdapter);

                            //Setting Review
                            for(int i = 0 ; i < rev_arr.length() ; i++){
                                JSONObject revDet = rev_arr.getJSONObject(i);
                                String reviewBy = "by " + revDet.getString("author") + " on " + revDet.getString("created_at");
                                String rating = revDet.getString("rating");
                                String content = revDet.getString("content");
                                reviewList.add(new Review(reviewBy,rating,content));
                                System.out.println(content);
                            }

                            reviewView = (RecyclerView) findViewById(R.id.reviewRecycle);
                            ReviewAdapter rAdapter = new ReviewAdapter(getApplicationContext(),reviewList);
                            LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.VERTICAL,false);
                            reviewView.setLayoutManager(layoutManager);
                            reviewView.setAdapter(rAdapter);
                            reviewView.setNestedScrollingEnabled(false);


                            //Setting Recommended Picks
                            for(int i = 0; i < recommended_arr.length() ; i++){
                                JSONObject movie = recommended_arr.getJSONObject(i);
                                String url = movie.getString("poster_path");
                                String id = movie.getString("id");
                                recommendedList.add(new MovieData(url,id,media_type));
                            }

                            callRecommeded(recommendedList);


                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        loadingScreen.setVisibility(View.GONE);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println(error);
            }
        });

        // Add the request to the RequestQueue.
        queue.add(jsonRequest);
        ImageView fbShareBtn = findViewById(R.id.fbShareBtn);
        ImageView twitterShareBtn = findViewById(R.id.tweetShareBtn);

        fbShareBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "https://www.themoviedb.org/" + media_type + "/" + id;
                String fbURL = "https://www.facebook.com/sharer/sharer.php?u=" + url ;
                Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(fbURL));
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                i.setPackage("com.android.chrome");
                startActivity(i);
            }
        });

        twitterShareBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "https://www.themoviedb.org/" + media_type + "/" + id;
                String tweetUrl = "https://twitter.com/intent/tweet?text=Check this out! " + url;
                Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(tweetUrl));
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                i.setPackage("com.android.chrome");
                startActivity(i);
            }
        });


        //checking for watchlist
    }

}