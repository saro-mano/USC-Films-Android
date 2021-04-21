package com.example.hw9;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private ArrayList<String> images = new ArrayList<>();
    private ArrayList<String> id = new ArrayList<>();
    private ArrayList<String> media_type = new ArrayList<>();
    private Context context;
    Toast toast;

    public RecyclerViewAdapter(Context context ,ArrayList<MovieData> data) {
        for(int i = 0; i < data.size() ; i++){
            images.add(data.get(i).getImgUrl());
            id.add(data.get(i).getId());
            media_type.add(data.get(i).getMedia_type());
        }
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.posters,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Glide.with(context)
                .asBitmap()
                .load(images.get(position))
                .into(holder.image);



        holder.popUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //creating a popup menu
                PopupMenu popup = new PopupMenu(context,holder.popUpButton);
                //inflating menu from xml resource
                popup.inflate(R.menu.poster_options);
                //adding click listener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.tmdb:
                                String url = "https://www.themoviedb.org/" + media_type.get(position) + "/" + id.get(position);
                                Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                i.setPackage("com.android.chrome");
                                try {
                                    context.startActivity(i);
                                } catch (ActivityNotFoundException e) {
                                    toast.makeText(context, "unable to open chrome", Toast.LENGTH_SHORT).show();
                                    i.setPackage(null);
                                    context.startActivity(i);
                                }
                                return true;
                            case R.id.fbShare:
                                url = "https://www.themoviedb.org/" + media_type.get(position) + "/" + id.get(position);
                                String fbURL = "https://www.facebook.com/sharer/sharer.php?u=" + url ;
                                i = new Intent(Intent.ACTION_VIEW, Uri.parse(fbURL));
                                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                i.setPackage("com.android.chrome");
                                try {
                                    context.startActivity(i);
                                } catch (ActivityNotFoundException e) {
                                    toast.makeText(context, "unable to open chrome", Toast.LENGTH_SHORT).show();
                                    i.setPackage(null);
                                    context.startActivity(i);
                                }
                                return true;

                                //handle menu2 click
                            case R.id.twitterShare:
                                url = "https://www.themoviedb.org/" + media_type.get(position) + "/" + id.get(position);
                                String tweetUrl = "https://twitter.com/intent/tweet?text=Check this out! " + url;
                                //handle menu3 click
                                i = new Intent(Intent.ACTION_VIEW, Uri.parse(tweetUrl));
                                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                i.setPackage("com.android.chrome");
                                try {
                                    context.startActivity(i);
                                } catch (ActivityNotFoundException e) {
                                    toast.makeText(context, "unable to open chrome", Toast.LENGTH_SHORT).show();
                                    i.setPackage(null);
                                    context.startActivity(i);
                                }
                                return true;
                            case R.id.watchlist:
                                return true;
                            default:
                                return false;
                        }
                    }
                });
                //displaying the popup
                popup.show();

            }
        });


        holder.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                try{
                    Intent intent = new Intent(context,Details.class);
                    bundle.putString("id", id.get(position));
                    bundle.putString("media_type",media_type.get(position));
                    intent.putExtras(bundle);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
//                    Toast.makeText(context,"Your answer is correct!" , Toast.LENGTH_LONG ).show();
                }
                catch(Exception e){
                    System.out.println(e);
                }

            }
        });



    }

    @Override
    public int getItemCount() {
        return images.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView image;
        ImageView popUpButton;


        public ViewHolder(View itemView){
            super(itemView);
            image = itemView.findViewById(R.id.poster);
            popUpButton = itemView.findViewById(R.id.verticalDots);
        }
    }
}
