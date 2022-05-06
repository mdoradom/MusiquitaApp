package com.musiquitaapp.screens.main;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.musiquitaapp.R;
import com.musiquitaapp.databinding.ActivityDashboardBinding;

public class Dashboard extends AppCompatActivity {

    NavController navController;
    NavHostFragment navHostFragment;

    private ActivityDashboardBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDashboardBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.main_frame);
        navController  = navHostFragment.getNavController();

        binding.mainNav.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.nav_home:
                    navController.navigate(R.id.homeFragment);
                    return true;
                case R.id.nav_search:
                    navController.navigate(R.id.searchFragment);
                    return true;
                case R.id.nav_library:
                    navController.navigate(R.id.libraryFragment);
                    return true;
                default:
                    return false;
            }
        });

        binding.profileIcon.setOnClickListener(v -> navController.navigate(R.id.profileActivity));

        binding.connectIcon.setOnClickListener(v -> navController.navigate(R.id.connectActivity));
    }

}