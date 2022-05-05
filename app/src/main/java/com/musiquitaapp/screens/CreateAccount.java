package com.musiquitaapp.screens;

import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.musiquitaapp.R;
import com.musiquitaapp.databinding.FragmentCreateAccountBinding;
import com.musiquitaapp.databinding.FragmentRegisterSelectorBinding;

public class CreateAccount extends BaseFragment {

    private FragmentCreateAccountBinding binding;

    public CreateAccount() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentCreateAccountBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        binding.buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.dashboardActivity);
            }
        });

        return view;
    }
}