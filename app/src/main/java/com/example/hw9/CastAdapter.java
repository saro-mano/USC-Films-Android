package com.example.hw9;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import android.widget.ArrayAdapter;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class CastAdapter extends ArrayAdapter {
    ArrayList<Cast> castList = new ArrayList<>();
    Context context;

    public CastAdapter(Context context, int textViewResourceId, ArrayList objects) {
        super(context, textViewResourceId, objects);
        castList = objects;
        this.context = context;
    }

    @Override
    public int getCount() {
        return super.getCount();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        v = inflater.inflate(R.layout.cast_cards, null);
        TextView textView = (TextView) v.findViewById(R.id.textView);
        de.hdodenhof.circleimageview.CircleImageView imageView = (CircleImageView) v.findViewById(R.id.imageView);
        textView.setText(castList.get(position).getName());
        Glide.with(context)
                .asBitmap()
                .load(castList.get(position).getImageUrl())
                .into(imageView);
//        imageView.setImageResource(birdList.get(position).getImageUrl());
        return v;

    }
}
