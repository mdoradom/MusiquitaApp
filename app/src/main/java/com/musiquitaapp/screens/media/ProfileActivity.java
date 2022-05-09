package com.musiquitaapp.screens.media;

import android.graphics.PixelFormat;
import android.os.Bundle;
import android.view.View;
import android.view.Window;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.musiquitaapp.databinding.ActivityProfileBinding;

import jp.wasabeef.glide.transformations.BlurTransformation;

public class ProfileActivity extends AppCompatActivity {

    private ActivityProfileBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProfileBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        // Load background image
        Glide.with(this).load("https://cdna.artstation.com/p/assets/images/images/048/372/492/large/_z-ed_-da.jpg?1649869257")
                .apply(RequestOptions.bitmapTransform(new BlurTransformation(10, 1)))
                .into(binding.backgroundImage);

        // Load pfp image
        Glide.with(this).load("https://i.pinimg.com/originals/19/cf/78/19cf789a8e216dc898043489c16cec00.jpg")
                .circleCrop()
                .into(binding.profileImage);
    }

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        Window window = getWindow();
        window.setFormat(PixelFormat.RGBA_8888);
    }
}