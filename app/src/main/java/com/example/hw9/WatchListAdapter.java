package com.example.hw9;

//public class WatchListAdapter {
//}

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Movie;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import org.apache.commons.lang3.ArrayUtils;
import org.w3c.dom.Text;

import java.util.ArrayList;


public class WatchListAdapter extends RecyclerView.Adapter<WatchListAdapter.MyViewHolder>{

    ArrayList<MovieData> moviedata;
    Context context;
    View watchView;

    public WatchListAdapter(Context context, ArrayList<MovieData> moviedata, View watchView) {
        this.context = context;
        this.moviedata = moviedata;
        this.watchView = watchView;
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // infalte the item Layout
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.watchlist_layout, parent, false);
        // set the view's size, margins, paddings and layout parameters
        MyViewHolder vh = new MyViewHolder(v); // pass the view to View Holder
        return vh;
    }



    @RequiresApi(api = Build.VERSION_CODES.O)
    private void resetWatchList(ArrayList<MovieData> watchList){
        SharedPreferences pref = context.getSharedPreferences("MyPref", 0); // 0 - for private mode
        SharedPreferences.Editor editor = pref.edit();
        String key = "watchlist";
        String arr_builder = "";
        for(int i = 0; i < watchList.size() ; i++){
            String val = watchList.get(i).getId() + "," + watchList.get(i).getMedia_type() + "," + watchList.get(i).getImgUrl() + "," + watchList.get(i).getTitle();
            arr_builder = arr_builder + val + "#";
        }
        editor.putString(key,arr_builder);
        editor.commit();
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        Glide.with(context)
                .asBitmap()
                .load(moviedata.get(position).getImgUrl())
                .centerCrop()
                .into(holder.image);

        holder.remove.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(),moviedata.get(position).getTitle() + " was removed to the watchlist",Toast.LENGTH_LONG).show();
                moviedata.remove(position);
                    notifyItemRemoved(position);
                    notifyItemRangeChanged(position, moviedata.size());
                    notifyDataSetChanged();
                    if(moviedata.size() == 0){
                       watchView.findViewById(R.id.nilWatchListText).setVisibility(View.VISIBLE);
                    }

                resetWatchList(moviedata);
            }
        });


        holder.watchCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                try{
                    Intent intent = new Intent(context,Details.class);
                    bundle.putString("id", moviedata.get(position).getId());
                    bundle.putString("media_type",moviedata.get(position).getMedia_type());
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

        String media_type = moviedata.get(position).getMedia_type();
        if (media_type.equals("tv")){
            media_type = "TV";
        }
        else{
            media_type = "Movie";
        }
        holder.mediaTypeWatchLst.setText(media_type);
    }
    @Override
    public int getItemCount() {
        return moviedata.size();
    }
    public class MyViewHolder extends RecyclerView.ViewHolder {
        // init the item view's
        ImageView image;
        ImageView remove;
        CardView watchCard;
        TextView mediaTypeWatchLst;


        public MyViewHolder(View itemView) {
            super(itemView);
            // get the reference of item view's
            image = itemView.findViewById(R.id.watchlistPoster);
            remove = itemView.findViewById(R.id.removeImg);
            watchCard = itemView.findViewById(R.id.watchCard);
            mediaTypeWatchLst = itemView.findViewById(R.id.mediaTypeWatchLst);
        }
    }
}