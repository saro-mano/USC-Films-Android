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
    private ArrayList<SearchData> data;
    private Context context;
    Toast toast;

    public SearchAdapter(Context context ,ArrayList<SearchData> data) {
        this.data = data;
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
                .load(data.get(position).getBackground())
                .into(holder.image);


        holder.title.setText(data.get(position).getTitle());
        holder.rating.setText(data.get(position).getRating());

        String movieAndYearStr = data.get(position).getMedia_type().toUpperCase() + " (" + data.get(position).getYear() + ")";
        holder.mediaAndYear.setText(movieAndYearStr);

        holder.card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                try{
                    Intent intent = new Intent(context,Details.class);
                    bundle.putString("id", data.get(position).getId());
                    bundle.putString("media_type",data.get(position).getMedia_type());
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
        TextView rating, mediaAndYear, title;
        CardView card;



        public ViewHolder(View itemView){
            super(itemView);
            card = itemView.findViewById(R.id.searchCard);
            rating = itemView.findViewById(R.id.ratingNumber);
            mediaAndYear = itemView.findViewById(R.id.mediaAndYear);
            title = itemView.findViewById(R.id.posterTitle);
            image = itemView.findViewById(R.id.searchImg);
        }
    }
}
