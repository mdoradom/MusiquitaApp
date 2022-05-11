package com.musiquitaapp.screens.login;

import static android.app.Activity.RESULT_OK;
import static com.musiquitaapp.youtube.YoutubeSingleton.getCredential;

import android.Manifest;
import android.accounts.AccountManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.result.contract.ActivityResultContracts;

import com.musiquitaapp.R;
import com.musiquitaapp.databinding.FragmentRegisterSelectorBinding;
import com.musiquitaapp.screens.BaseFragment;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

public class RegisterSelector extends BaseFragment {

    private FragmentRegisterSelectorBinding binding;

    private static final int PERMISSIONS = 1;
    static final int REQUEST_ACCOUNT_PICKER = 1000;
    static final int REQUEST_PERMISSION_GET_ACCOUNTS = 1003;
    public static final String PREF_ACCOUNT_NAME = "accountName";

    public RegisterSelector() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentRegisterSelectorBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        binding.buttonEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.action_registerSelector_to_createAccount);
            }
        });

        binding.buttonGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO login con google
                requestPermissions();
                //navController.navigate(R.id.action_registerSelector_to_dashboardActivity);
            }
        });

        binding.buttonLogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.action_registerSelector_to_login);
            }
        });

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @AfterPermissionGranted(PERMISSIONS)
    private void requestPermissions() {
        if (EasyPermissions.hasPermissions(this.getContext(), Manifest.permission.GET_ACCOUNTS, Manifest.permission.READ_PHONE_STATE)) {
            // Already have permission, do the thing

            if (EasyPermissions.hasPermissions(this.getContext(), Manifest.permission.GET_ACCOUNTS)) {
                String accountName =  getActivity().getPreferences(Context.MODE_PRIVATE).getString(PREF_ACCOUNT_NAME, null);
                if (accountName != null) {
                    getCredential().setSelectedAccountName(accountName);
                    navController.navigate(R.id.action_registerSelector_to_dashboardActivity);
                } else {
                    // Start a dialog from which the user can choose an account

                    startActivityForResult(getCredential().newChooseAccountIntent(), REQUEST_ACCOUNT_PICKER);

                }
            } else {
                // Request the GET_ACCOUNTS permission via a user dialog
                EasyPermissions.requestPermissions(
                        this,
                        "This app needs to access your Google account (via Contacts).",
                        REQUEST_PERMISSION_GET_ACCOUNTS,
                        Manifest.permission.GET_ACCOUNTS);
            }
        } else {
            // Do not have permissions, request them now
            EasyPermissions.requestPermissions(this, getString(R.string.all_permissions_request),
                    PERMISSIONS, Manifest.permission.GET_ACCOUNTS, Manifest.permission.READ_PHONE_STATE);
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_ACCOUNT_PICKER) {
            if (resultCode == RESULT_OK && data != null && data.getExtras() != null) {
                String accountName = data.getStringExtra(AccountManager.KEY_ACCOUNT_NAME);
                if (accountName != null) {
                    SharedPreferences settings = getActivity().getPreferences(Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = settings.edit();
                    editor.putString(PREF_ACCOUNT_NAME, accountName);
                    editor.apply();
                    navController.navigate(R.id.action_registerSelector_to_dashboardActivity);

                }
            }
        }
    }
}