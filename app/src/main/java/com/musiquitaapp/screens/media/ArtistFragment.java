package com.musiquitaapp.screens.media;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.musiquitaapp.R;
import com.musiquitaapp.databinding.FragmentArtistBinding;

import jp.wasabeef.glide.transformations.BlurTransformation;

public class ArtistFragment extends Fragment {

    private FragmentArtistBinding binding;

    public ArtistFragment() {
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
        View view = inflater.inflate(R.layout.fragment_artist, container, false);

        // Load background image
        Glide.with(this).load("https://cdna.artstation.com/p/assets/images/images/048/372/492/large/_z-ed_-da.jpg?1649869257")
                .apply(RequestOptions.bitmapTransform(new BlurTransformation(10, 1)))
                .into(binding.backgroundImage);


        return view;
    }
}