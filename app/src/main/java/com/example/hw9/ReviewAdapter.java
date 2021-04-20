package com.example.hw9;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.MyViewHolder> {

    ArrayList<Review> reviewList;
    Context context;


    public ReviewAdapter(Context context, ArrayList reviewList){
        this.reviewList = reviewList;
        this.context = context;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView reviewBy,rating,content;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            reviewBy = itemView.findViewById(R.id.reviewBy);
            rating = itemView.findViewById(R.id.rating);
            content = itemView.findViewById(R.id.revContent);
        }
    }

    @NonNull
    @Override
    public ReviewAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.review_layout,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewAdapter.MyViewHolder holder, int position) {
        holder.reviewBy.setText(reviewList.get(position).getReviewName());
        holder.rating.setText(reviewList.get(position).getRating());
//        holder.content.setText(reviewList.get(position).getContent());

    }

    @Override
    public int getItemCount() {
        return reviewList.size();
    }


}
