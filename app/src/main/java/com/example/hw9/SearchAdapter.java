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
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.Target;

import java.util.ArrayList;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.ViewHolder> {

    private ArrayList<String> images = new ArrayList<>();
    private ArrayList<String> id = new ArrayList<>();
    private ArrayList<String> media_type = new ArrayList<>();
    private Context context;
    Toast toast;

    public SearchAdapter(Context context ,ArrayList<SearchData> data) {
        for(int i = 0; i < data.size() ; i++){
            images.add(data.get(i).getBackground());
//            id.add(data.get(i).getId());
//            media_type.add(data.get(i).getMedia_type());
        }
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_layout,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Glide.with(context)
                .asBitmap()
                .load(images.get(position))
                .into(holder.image);
    }

    @Override
    public int getItemCount() {
        return images.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView image;



        public ViewHolder(View itemView){
            super(itemView);
            image = itemView.findViewById(R.id.searchImg);
        }
    }
}
