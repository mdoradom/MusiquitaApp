package com.musiquitaapp.screens.main;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.musiquitaapp.R;
import com.musiquitaapp.adapters.PlaylistAdapter;
import com.musiquitaapp.controllers.PlaylistController;
import com.musiquitaapp.databinding.FragmentLibraryArtistsBinding;
import com.musiquitaapp.databinding.FragmentLibraryPlaylistsBinding;
import com.musiquitaapp.models.PlayListFirebase;
import com.musiquitaapp.models.YouTubeVideo;

import java.util.List;

public class LibraryPlaylistsFragment extends Fragment {

    private FragmentLibraryPlaylistsBinding binding;
    private PlaylistAdapter playlistAdapter;
    private PlaylistController playlistController = new PlaylistController();

    public LibraryPlaylistsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentLibraryPlaylistsBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        playlistController.getAllUserPlaylistsByUUID(user.getUid(), list -> {
            List<PlayListFirebase> playListFirebases = list;
            playlistAdapter = new PlaylistAdapter(playListFirebases, getContext());
            binding.playlistRecycler.setAdapter(playlistAdapter);
            binding.playlistRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        });

        //binding.textView.setText(playlistController.getAllUserPlaylistsByUUID(user.getUid()).size());

        return view;
    }

}