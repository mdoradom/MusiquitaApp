package com.musiquitaapp.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.musiquitaapp.R;
import com.musiquitaapp.models.YouTubeVideo;
import com.musiquitaapp.screens.main.LibraryFavoritesFragment;
import com.musiquitaapp.youtube.YTApplication;

import java.util.ArrayList;
import java.util.List;

public class SongAdapter extends RecyclerView.Adapter<SongAdapter.MyViewHolder> {
    private List<YouTubeVideo> videos;
    private Context context;
    private String className;

    public SongAdapter(List<YouTubeVideo> videos, Context context, String className) {
        this.videos = videos;
        this.context = context;
        this.className = className;
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
        holder.songLenght.setText(String.valueOf(videos.get(position).getDuration()));

        Glide.with(context).load(videos.get(position).getThumbnailURL())
                .fitCenter()
                .into(holder.songImage);


        // si viene de la libreria te manda directamente al reproductor,
        // si no es que viene de la queue del reproductor y simplemente te cambia la posicion de la lista a esa canción

        if (className.equals("LibraryFavoritesFragment")) {
            holder.relativeLayout.setOnClickListener(v -> {
                YTApplication.getMediaItems().clear();
                YTApplication.getMediaItems().addAll(videos);
                YTApplication.getPos().setValue(position);
                YTApplication.getIsPlaying().setValue(false);
                YTApplication.getIsPaused().setValue(false);
                Navigation.createNavigateOnClickListener(R.id.playerActivity).onClick(v);
            });
        } else {
            holder.relativeLayout.setOnClickListener(view -> {
                if (!videos.get(position).getId().equals(YTApplication.getMediaItems().get(YTApplication.getPos().getValue()).getId())){
                    YTApplication.getPos().setValue(position);
                    YTApplication.getIsPlaying().setValue(false);
                    YTApplication.getIsPaused().setValue(false);
                }
            });
        }
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
