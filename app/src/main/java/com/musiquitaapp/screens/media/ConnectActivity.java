package com.musiquitaapp.screens.media;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.musiquitaapp.R;
import com.musiquitaapp.databinding.ActivityConnectBinding;
import com.musiquitaapp.databinding.ActivityProfileBinding;

public class ConnectActivity extends AppCompatActivity {

    private ActivityConnectBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityConnectBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
    }
}