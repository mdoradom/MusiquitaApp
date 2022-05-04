package com.musiquitaapp;

import android.app.AlertDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.musiquitaapp.databinding.FragmentLoginBinding;
import com.musiquitaapp.databinding.FragmentRegisterSelectorBinding;

public class Login extends BaseFragment {

    private FragmentLoginBinding binding;

    public Login() {
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
        binding = FragmentLoginBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        binding.buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.dashboardActivity);
            }
        });

        binding.iForgorPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO cambiar a pantalla recuperar contraseña
                Toast.makeText(getContext(), "TODO pantalla cambiar contraseña", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }
}