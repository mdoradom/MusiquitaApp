package com.musiquitaapp.screens.media;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.bumptech.glide.Glide;
import com.musiquitaapp.R;
import com.musiquitaapp.adapters.SearchAdapter;
import com.musiquitaapp.databinding.ActivityPlayerBinding;
import com.musiquitaapp.models.Config;
import com.musiquitaapp.models.ItemType;
import com.musiquitaapp.models.YouTubeVideo;
import com.musiquitaapp.services.BackgroundAudioService;

public class PlayerActivity extends AppCompatActivity {

    private ActivityPlayerBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPlayerBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        YouTubeVideo song = startService();

        binding.playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playSong(song);
            }
        });

    }

    private YouTubeVideo startService() {
        Bundle bundle = getIntent().getExtras();

        YouTubeVideo youTubeVideo = (YouTubeVideo) bundle.getSerializable("song");

        Glide.with(this).load(youTubeVideo.getThumbnailURL())
                .into(binding.songImage);

        return youTubeVideo;
    }

    private void playSong(YouTubeVideo song) {
        Intent serviceIntent = new Intent(getApplicationContext(), BackgroundAudioService.class);

        serviceIntent.setAction(BackgroundAudioService.ACTION_PLAY);
        serviceIntent.putExtra(Config.YOUTUBE_TYPE, ItemType.YOUTUBE_MEDIA_TYPE_VIDEO);
        serviceIntent.putExtra(Config.YOUTUBE_TYPE_VIDEO, song);

        getApplicationContext().startService(serviceIntent);
    }
}