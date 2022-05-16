package com.musiquitaapp.screens.media;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.core.graphics.ColorUtils;
import androidx.fragment.app.Fragment;
import androidx.palette.graphics.Palette;

import com.musiquitaapp.databinding.FragmentBottomSheetDialogBinding;

public class BottomSheetDialogFragment extends Fragment {

    private FragmentBottomSheetDialogBinding binding;
    private int oldTextColor;
    private static int alpha = 200;

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

        return view;
    }
}