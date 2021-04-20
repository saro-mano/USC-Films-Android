package com.example.hw9;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.GridView;
import android.widget.TextView;

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


    GridView simpleList;
    ArrayList<Cast> castList=new ArrayList<>();
    ArrayList<Review> reviewList = new ArrayList<>();
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

//                            reviewView = (RecyclerView) findViewById(R.id.reviewRecycle);
//                            ReviewAdapter rAdapter = new ReviewAdapter(getApplicationContext(),reviewList);
//                            LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.VERTICAL,false);
//                            reviewView.setLayoutManager(layoutManager);
//                            reviewView.setAdapter(rAdapter);
//                            reviewView.setNestedScrollingEnabled(false);


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
    }
}