package com.musiquitaapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.musiquitaapp.R;
import com.musiquitaapp.models.YouTubeVideo;

import java.util.ArrayList;

public class SongAdapter extends RecyclerView.Adapter<SongAdapter.MyViewHolder> {
    private ArrayList<YouTubeVideo> videos;
    private Context context;

    public SongAdapter(ArrayList<YouTubeVideo> videos, Context context) {
        this.videos = videos;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.song_layout, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.songName.setText(videos.get(position).getTitle());
        holder.songAuthor.setText(videos.get(position).getAuthor());
        holder.songLenght.setText(videos.get(position).getDuration());

        Glide.with(context).load(videos.get(position).getThumbnailURL())
                .fitCenter()
                .into(holder.songImage);
    }

    @Override
    public int getItemCount() {
        return videos.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView songName;
        TextView songAuthor;
        TextView songLenght;
        RelativeLayout relativeLayout;
        ImageView songImage;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            songName = itemView.findViewById(R.id.songName);
            songAuthor = itemView.findViewById(R.id.songAuthor);
            songLenght = itemView.findViewById(R.id.songLenght);

            relativeLayout = itemView.findViewById(R.id.mainContainer);
            songImage = itemView.findViewById(R.id.songImage);

        }
    }
}
