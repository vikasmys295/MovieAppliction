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
import com.example.movieapplication.model.TopMovie;
import com.squareup.picasso.Picasso;

import java.util.List;

public class TopMovieAdapter extends RecyclerView.Adapter<TopMovieAdapter.MyviewHolder> {
    Context context;
    List<TopMovie> movieList;

    public TopMovieAdapter( List<TopMovie> movieList,Context context) {
        this.context = context;
        this.movieList = movieList;
    }

    @NonNull
    @Override//inflate the content_list
    public TopMovieAdapter.MyviewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.content_lists,parent,false);
        return new TopMovieAdapter.MyviewHolder(view);
    }

    @Override//binding the views
    public void onBindViewHolder(@NonNull TopMovieAdapter.MyviewHolder holder, int position) {
        TopMovie results = movieList.get(position);
        holder.tvMovieName.setText(results.getName());
        Picasso.with(context).load("https://image.tmdb.org/t/p/w185"+results.getPoster_path()).resize(180,180)
                .into(holder.image);
    }

    @Override
    public int getItemCount() {
        return movieList.size();
    }

    //inner class for MyviewHolder
    public class MyviewHolder extends RecyclerView.ViewHolder {
        TextView tvMovieName;
        ImageView image;

        public MyviewHolder(View itemView) {
            super(itemView);
            tvMovieName = (TextView)itemView.findViewById(R.id.tv_name);
            image = (ImageView)itemView.findViewById(R.id.imageView2);
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
