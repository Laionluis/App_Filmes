package com.example.laion.myapplication;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class MovieApadpter extends RecyclerView.Adapter<MovieApadpter.MovieHolder> {

    private static ItemClickListener itemClickListener;

    public void setOnItemClickListener(ItemClickListener itemClickListener){
        this.itemClickListener = itemClickListener;
    }

    List<Result> results;

    public MovieApadpter(List<Result> results) {
        this.results = results;
    }

    @Override
    public MovieHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_movie, parent, false);
        return new MovieHolder(view);
    }

    @Override
    public void onBindViewHolder(MovieHolder holder, int position){
        Picasso.with(holder.itemView.getContext())
                .load("http://image.tmdb.org/t/p/w185/"+results.get(position).getPosterPath())
                .into(holder.Poster);
    }

    public void setData(List<Result> results){
        this.results = results;
    }

    @Override
    public int getItemCount(){
        return results.size();
    }

    public class MovieHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView Poster;
        public MovieHolder(View itemView){
            super(itemView);
            Poster = (ImageView)itemView.findViewById(R.id.poster);

            Poster.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if(itemClickListener != null) {
                itemClickListener.onItemClick(getAdapterPosition());
            }
        }
    }

    public interface ItemClickListener {

        void onItemClick(int position);
    }
}
