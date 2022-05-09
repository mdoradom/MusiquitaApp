package com.musiquitaapp.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.musiquitaapp.R;
import com.musiquitaapp.models.Config;
import com.musiquitaapp.models.ItemType;
import com.musiquitaapp.models.Items;
import com.musiquitaapp.models.YouTubeVideo;
import com.musiquitaapp.screens.media.PlayerActivity;
import com.musiquitaapp.services.BackgroundAudioService;

import java.util.List;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.MyViewHolder>{
    private Context mContext;
    private List<Items> mItems;
    private YouTubeVideo videoItem;

    public SearchAdapter(List<Items> results, Context applicationContext) {
        this.mContext = applicationContext;
        this.mItems = results;
    }

    @NonNull
    @Override
    public SearchAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        View view = layoutInflater.inflate(R.layout.search_layout, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchAdapter.MyViewHolder holder, int position) {
        holder.titleRow.setText(mItems.get(position).getSnippet().getTitle());
        holder.channelRow.setText(mItems.get(position).getSnippet().getChannelTitle());
        Glide.with(mContext).load(mItems.get(position).getSnippet().getThumbnails().getMedium().getUrl()).fitCenter().into(holder.imageRow);
        holder.relativeLayout.setOnClickListener(v -> {
            videoItem = new YouTubeVideo();
            videoItem.setId(mItems.get(position).getId().videoId);
            //videoItem.setDuration();
            videoItem.setTitle(mItems.get(position).getSnippet().getTitle());
            videoItem.setThumbnailURL(mItems.get(position).getSnippet().getThumbnails().getHigh().getUrl());

            /*Intent intent = new Intent(mContext, PlayerActivity.class);

            intent.putExtra("song", videoItem);

            mContext.startActivity(intent);*/

            Bundle bundle = new Bundle();
            bundle.putSerializable("song", videoItem);
            Navigation.createNavigateOnClickListener(R.id.action_searchFragment_to_playerActivity, bundle).onClick(v);
        });
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView titleRow;
        TextView channelRow;
        ImageView imageRow;
        RelativeLayout relativeLayout;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            titleRow = itemView.findViewById(R.id.TituloRow);
            channelRow = itemView.findViewById(R.id.ChannelRow);
            imageRow = itemView.findViewById(R.id.imageRow);
            relativeLayout = itemView.findViewById(R.id.vistarelativa);
        }
    }
}
