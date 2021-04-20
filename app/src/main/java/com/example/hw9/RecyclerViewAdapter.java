package com.example.hw9;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
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
//                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
//                    @Override
//                    public boolean onMenuItemClick(MenuItem item) {
//                        switch (item.getItemId()) {
//                            case R.id.menu1:
//                                //handle menu1 click
//                                return true;
//                            case R.id.menu2:
//                                //handle menu2 click
//                                return true;
//                            case R.id.menu3:
//                                //handle menu3 click
//                                return true;
//                            default:
//                                return false;
//                        }
//                    }
//                });
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
