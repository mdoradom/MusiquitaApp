package com.musiquitaapp.screens.login;

import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.navigation.Navigation;

import com.musiquitaapp.R;
import com.musiquitaapp.databinding.FragmentLoginBinding;
import com.musiquitaapp.screens.BaseFragment;

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