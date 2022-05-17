package com.musiquitaapp.screens.main;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.musiquitaapp.R;
import com.musiquitaapp.databinding.FragmentLibraryArtistsBinding;
import com.musiquitaapp.databinding.FragmentLibraryPlaylistsBinding;

public class LibraryPlaylistsFragment extends Fragment {

    private FragmentLibraryPlaylistsBinding binding;

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

        return view;
    }

}