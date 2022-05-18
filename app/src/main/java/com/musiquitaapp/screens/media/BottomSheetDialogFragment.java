package com.musiquitaapp.screens.media;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.musiquitaapp.adapters.SongAdapter;
import com.musiquitaapp.databinding.FragmentBottomSheetDialogBinding;
import com.musiquitaapp.youtube.YTApplication;

import org.w3c.dom.Text;

public class BottomSheetDialogFragment extends Fragment {

    private FragmentBottomSheetDialogBinding binding;
    private int oldTextColor;
    private static int alpha = 200;
    private SongAdapter songAdapter;

    public BottomSheetDialogFragment() {
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
        binding = FragmentBottomSheetDialogBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();

        Log.d("getMediaItems content", String.valueOf(YTApplication.getMediaItems().isEmpty()));

        songAdapter = new SongAdapter(YTApplication.getMediaItems(), BottomSheetDialogFragment.this.getContext());
        binding.recyclerQueue.setAdapter(songAdapter);
        binding.recyclerQueue.setLayoutManager(new LinearLayoutManager(BottomSheetDialogFragment.this.getContext()));

        return view;
    }
}