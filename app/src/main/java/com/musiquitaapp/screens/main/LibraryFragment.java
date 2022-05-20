package com.musiquitaapp.screens.main;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.musiquitaapp.databinding.FragmentLibraryBinding;

public class LibraryFragment extends Fragment {

    private FragmentLibraryBinding binding;

    public LibraryFragment() {
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
        binding = FragmentLibraryBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();

        binding.libraryFrame.setAdapter(new FragmentStateAdapter(this) {
            @NonNull
            @Override
            public Fragment createFragment(int position) {
                switch (position) {
                    case 0: default:
                        return new LibraryPlaylistsFragment();
                    case 1:
                        return new LibraryArtistsFragment();
                    case 2:
                        return new LibraryFavoritesFragment();
                }
            }

            @Override
            public int getItemCount() {
                return 3;
            }
        });

        new TabLayoutMediator(binding.tabLayout, binding.libraryFrame, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                switch (position) {
                    case 0: default:
                        tab.setText("Playlists");
                        break;
                    case 1:
                        tab.setText("Artistas");
                        break;
                    case 2:
                        tab.setText("Favoritos");
                        break;
                }
            }
        }).attach();


        // TODO LibraryFragment cosas

        return view;
    }

}