package com.example.hw9;

import android.content.ActivityNotFoundException;
import java.lang.Object;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import org.apache.commons.lang3.ArrayUtils;

import java.util.ArrayList;

public class RecViewAdapter extends RecyclerView.Adapter<RecViewAdapter.ViewHolder> {

    private ArrayList<String> images = new ArrayList<>();
    private ArrayList<String> id = new ArrayList<>();
    private ArrayList<String> media_type = new ArrayList<>();
    private Context context;
    Toast toast;


    public RecViewAdapter(Context context ,ArrayList<MovieData> data) {
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

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recommended_picks,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {




        Glide.with(context)
                .asBitmap()
                .load(images.get(position))
                .centerCrop()
                .into(holder.image);

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

        public ViewHolder(View itemView){
            super(itemView);
            image = itemView.findViewById(R.id.recPoster);
        }
    }

}
