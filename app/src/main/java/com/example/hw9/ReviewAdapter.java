package com.example.hw9;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.MyViewHolder> {

    ArrayList<Review> reviewList;
    Context context;


    public ReviewAdapter(Context context, ArrayList<Review> reviewList){
        this.reviewList = reviewList;
        this.context = context;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView reviewBy,rating,content;
        CardView reviewCard;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            reviewBy = itemView.findViewById(R.id.reviewBy);
            rating = itemView.findViewById(R.id.rating);
            content = itemView.findViewById(R.id.revContent);
            reviewCard = itemView.findViewById(R.id.reviewCard);
        }
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.review_layout,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewAdapter.MyViewHolder holder, int position) {
        holder.reviewBy.setText(reviewList.get(position).getReviewName());
        holder.rating.setText(reviewList.get(position).getRating());
        holder.content.setText(reviewList.get(position).getContent());

        holder.reviewCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                try{
                    Intent intent = new Intent(context,ReviewFullScreen.class);
                    bundle.putString("reviewBy", reviewList.get(position).getReviewName());
                    bundle.putString("rating",reviewList.get(position).getRating());
                    bundle.putString("content",reviewList.get(position).getContent());
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
        return reviewList.size();
    }


}
