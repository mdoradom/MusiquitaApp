package com.musiquitaapp.screens;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import android.app.Activity;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.View;

import com.musiquitaapp.R;
import com.musiquitaapp.databinding.ActivityLoadingScreenBinding;
import com.musiquitaapp.databinding.ActivityPlayerBinding;

public class LoadingScreen extends AppCompatActivity {

    private ActivityLoadingScreenBinding binding;
    private AnimationDrawable animation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoadingScreenBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        /*animation = new AnimationDrawable();
        loadFrames();
        animation.setOneShot(false);
        binding.imageView2.setImageDrawable(animation);
        animation.start();*/
    }

    private void loadFrames() {
        String sImage;
        for (int i = 0; i < 300; i++) {
            sImage = "1_";
            animation.addFrame(
                    ResourcesCompat.getDrawable(
                            getResources(), getResources().getIdentifier(
                                    sImage + i , "drawable", getPackageName()),
                            null),
                    50);
        }
    }
}