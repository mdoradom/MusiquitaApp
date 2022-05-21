package com.musiquitaapp.screens.media;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.view.View;

import com.musiquitaapp.R;
import com.musiquitaapp.adapters.SessionAdapter;
import com.musiquitaapp.controllers.SessionController;
import com.musiquitaapp.databinding.ActivityConnectBinding;
import com.musiquitaapp.databinding.ActivityProfileBinding;
import com.musiquitaapp.models.MySession;

import java.util.List;

public class ConnectActivity extends AppCompatActivity {

    private ActivityConnectBinding binding;
    private SessionAdapter sessionAdapter;
    private SessionController sessionController = new SessionController();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityConnectBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();

        sessionController.getAllSessions(list -> {
            sessionAdapter = new SessionAdapter(getApplicationContext(), list);
            binding.sessionsRecycler.setAdapter(sessionAdapter);
            binding.sessionsRecycler.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        });

        binding.createSessionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v) {
                sessionController.createSession("prueba");
            }
        });

        setContentView(view);
    }
}