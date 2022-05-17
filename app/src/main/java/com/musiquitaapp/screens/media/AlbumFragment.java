package com.musiquitaapp.screens.media;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.musiquitaapp.R;
import com.musiquitaapp.databinding.FragmentAlbumBinding;
import com.musiquitaapp.databinding.FragmentLibraryPlaylistsBinding;

public class AlbumFragment extends Fragment {

    private FragmentAlbumBinding binding;

    public AlbumFragment() {
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
        binding = FragmentAlbumBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        return view;
    }
}