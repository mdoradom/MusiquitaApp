package com.musiquitaapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.graphics.ColorUtils;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.musiquitaapp.R;
import com.musiquitaapp.controllers.PlaylistController;
import com.musiquitaapp.models.PlayListFirebase;
import com.musiquitaapp.models.YouTubeVideo;
import com.musiquitaapp.screens.main.SearchFragment;

import java.util.List;

public class PlaylistAdapter extends RecyclerView.Adapter<PlaylistAdapter.MyViewHolder>{
    private List<PlayListFirebase> videos;
    private Context context;
    private YouTubeVideo video;

    public PlaylistAdapter(List<PlayListFirebase> videos, Context context) {
        this.videos = videos;
        this.context = context;
    }

    public PlaylistAdapter (List<PlayListFirebase> videos, Context context, YouTubeVideo video) {
        this.videos = videos;
        this.context = context;
        this.video = video;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.playlist_layout, parent, false);
        return new PlaylistAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.playlistName.setText(videos.get(position).playlistTitle);
        holder.playlistAuthor.setText(videos.get(position).authorName);
        holder.playlistLenght.setText(String.valueOf(videos.get(position).songs.size()));

        Glide.with(context)
                .load(videos.get(position).thumbnail)
                .into(holder.playlistImage);

        holder.relativeLayout.setOnClickListener(v -> new PlaylistController().addSongToPlaylist(video, videos.get(position).playlistID));
    }

    @Override
    public int getItemCount() {
        return videos.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView playlistName;
        TextView playlistAuthor;
        TextView playlistLenght;
        RelativeLayout relativeLayout;
        ImageView playlistImage;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            playlistName = itemView.findViewById(R.id.playlistName);
            playlistAuthor = itemView.findViewById(R.id.playlistAuthor);
            playlistLenght = itemView.findViewById(R.id.playlistLenght);

            relativeLayout = itemView.findViewById(R.id.mainContainer);
            playlistImage = itemView.findViewById(R.id.playlistImage);
        }
    }
}
