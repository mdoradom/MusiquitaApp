package com.musiquitaapp.adapters;

import android.content.Context;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.musiquitaapp.R;
import com.musiquitaapp.models.Config;
import com.musiquitaapp.models.Items;
import com.musiquitaapp.models.YouTubeVideo;
import com.musiquitaapp.youtube.YTApplication;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.MyViewHolder>{
    private Context mContext;
    private List<Items> mItems;
    private YouTubeVideo videoItem;
    private int position;
    private String time;

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

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

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                setPosition(holder.getPosition());
                return false;
            }
        });

        holder.relativeLayout.setOnClickListener(v -> {
            YTApplication.getMediaItems().clear();
            RequestQueue queue = Volley.newRequestQueue(mContext);
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                    Request.Method.GET,
                    Config.YOUTUBE_GET_TIME + mItems.get(position).getId().videoId + Config.YOUTUBE_GET_TIME_2 + Config.YOUTUBE_API_KEY,
                    null,
                    response -> {
                        //TODO el response.optString no coje la duración bien porque
                        // k;fjvertsdjkgnbsretkhnsjkrethn está como muy dentro del json (creo)
                        time = response.optString("duration");

                        System.out.println(time);
                        Pattern pattern = Pattern.compile("P(\\d+D)?T(\\d+H)?(\\d+M)?(\\d+S)?", Pattern.CASE_INSENSITIVE);
                        Matcher m = pattern.matcher(time);

                        if (m.find( )) {
                            System.out.println("Found value D: " + m.group(0) );
                            System.out.println("Found value H: " + m.group(1) );
                            System.out.println("Found value M: " + m.group(2) );
                            System.out.println("Found value S: " + m.group(3) );
                        } else {
                            System.out.println("NO MATCH");
                        }
                    },
                    error -> Log.d("tag", "onErrorResponse: " + error.getMessage())
            );

            queue.add(jsonObjectRequest);
            videoItem = new YouTubeVideo();
            //TODO hay que convertir el tiempo que nos da yt (PT3M22S) a segundos
            videoItem.setDuration(time);
            videoItem.setId(mItems.get(position).getId().videoId);
            videoItem.setTitle(mItems.get(position).getSnippet().getTitle());
            videoItem.setThumbnailURL(mItems.get(position).getSnippet().getThumbnails().getHigh().getUrl());
            videoItem.setAuthor(mItems.get(position).getSnippet().getChannelTitle());
            YTApplication.getMediaItems().add(videoItem);
            YTApplication.getPos().setValue(0);
            Navigation.createNavigateOnClickListener(R.id.action_searchFragment_to_playerActivity).onClick(v);
        });
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {
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

            itemView.setOnCreateContextMenuListener(this);
        }



        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            menu.setHeaderTitle("Canción");
            menu.add(0, R.id.addQueue, 0, "Añadir a Cola");
            menu.add(0, R.id.addPlaylist, 0, "Añadir a Playlist");
        }
    }

}
