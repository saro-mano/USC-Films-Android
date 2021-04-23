package com.example.hw9;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Movie;
import android.net.Uri;
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
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Details extends AppCompatActivity {


    private void setYoutube(String trailer){
        YouTubePlayerView youTubePlayerView = findViewById(R.id.youTubePlayerView);
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
        RecyclerViewAdapter adapter1 = new RecyclerViewAdapter(getApplicationContext(),top_rated_movies);
        recyclerView.setAdapter(adapter1);

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


        //Volley Request:

        RequestQueue queue = Volley.newRequestQueue(this);

        String url = "http://10.0.2.2:8080/getDetails?id="+id+"&media_type="+media_type;

        System.out.println(url);

        ArrayList<SliderData> currently_playing_movies = new ArrayList<>();
        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
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
                            JSONArray cast_arr = response.getJSONArray("movie_cast");
                            JSONArray rev_arr = response.getJSONArray("movie_rev");
                            JSONArray recommended_arr = response.getJSONArray("recommended_mov");

                            //Setting Youtube
                            setYoutube(trailer);

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
                                String media_type = "movie";
                                recommendedList.add(new MovieData(url,id,media_type));
                            }

                            callRecommeded(recommendedList);



                        } catch (Exception e) {
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
    }

}