package com.musiquitaapp.screens.main;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.musiquitaapp.adapters.SongAdapter;
import com.musiquitaapp.controllers.FavouritesController;
import com.musiquitaapp.controllers.PlaylistController;
import com.musiquitaapp.databinding.FragmentLibraryFavoritesBinding;
import com.musiquitaapp.models.PlayListFirebase;
import com.musiquitaapp.models.YouTubeVideo;

import java.util.ArrayList;
import java.util.List;

public class LibraryFavoritesFragment extends Fragment {

    private FragmentLibraryFavoritesBinding binding;
    private SongAdapter songAdapter = null;

    public LibraryFavoritesFragment () {
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
        binding = FragmentLibraryFavoritesBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        new FavouritesController().getFavouriteSongs(list -> {
            songAdapter = new SongAdapter(list.songs, getContext(), "LibraryFavoritesFragment");
            binding.songRecycler.setAdapter(songAdapter);
            binding.songRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        });


        return view;
    }
}