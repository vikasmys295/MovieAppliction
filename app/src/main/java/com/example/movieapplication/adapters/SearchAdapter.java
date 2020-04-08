package com.example.movieapplication.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.movieapplication.R;
import com.example.movieapplication.activities.PlayerActivity;
import com.example.movieapplication.model.Results;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.MyviewHolder>  {

    Context context;
    List<Results> movieList;

    public SearchAdapter(List<Results> movieList,Context context) {
        this.context = context;
        this.movieList = movieList;
    }

    @NonNull
    @Override//inflate the content_list
    public SearchAdapter.MyviewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.content_lists,parent,false);
        return new MyviewHolder(view);
    }

    @Override//binding the views
    public void onBindViewHolder(@NonNull SearchAdapter.MyviewHolder holder, int position) {
        Results results = movieList.get(position);
        holder.tvMovieNames.setText(results.getTitle());
        Picasso.with(context).load("https://image.tmdb.org/t/p/w185"+results.getPoster_path()).resize(180,180)
                .into(holder.images);
    }

    @Override
    public int getItemCount() {
        return movieList.size();
    }

    //updating the text after filterable
    public void updateList(List<Results> newlist){
        movieList = new ArrayList<>();
        movieList.addAll(newlist);
        notifyDataSetChanged();
    }
    //inner class for MyviewHolder
    public class MyviewHolder extends RecyclerView.ViewHolder {
        TextView tvMovieNames;
        ImageView images;

        public MyviewHolder(View itemView) {
            super(itemView);
            tvMovieNames = (TextView)itemView.findViewById(R.id.tv_name);
            images = (ImageView)itemView.findViewById(R.id.imageView2);
            context = itemView.getContext();
            //Adding clicklistener for each item in the list
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, PlayerActivity.class);
                    context.startActivity(intent);
                }
            });
        }
    }
}
