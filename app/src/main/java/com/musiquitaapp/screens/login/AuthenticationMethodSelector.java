package com.musiquitaapp.screens.login;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.musiquitaapp.R;
import com.musiquitaapp.databinding.FragmentRegisterSelectorBinding;

public class AuthenticationMethodSelector extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authentication_method_selector);
    }
}