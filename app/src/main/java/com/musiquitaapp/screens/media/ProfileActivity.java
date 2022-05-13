package com.musiquitaapp.screens.media;

import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.view.View;
import android.view.Window;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.auth.User;
import com.google.firebase.storage.FirebaseStorage;
import com.musiquitaapp.R;
import com.musiquitaapp.databinding.ActivityProfileBinding;
import com.musiquitaapp.screens.login.AuthenticationMethodSelector;

import jp.wasabeef.glide.transformations.BlurTransformation;

public class ProfileActivity extends AppCompatActivity {

    private ActivityProfileBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProfileBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();

        setContentView(view);

        setProfileResources();

        binding.profileImage.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            GoogleSignInClient googleSignInClient = GoogleSignIn.getClient(getApplicationContext(), new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build());

            googleSignInClient.signOut();

            Intent intent = new Intent(ProfileActivity.this, AuthenticationMethodSelector.class);
            startActivity(intent);
        });
    }

    private void setProfileResources() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        // Load pfp image
        Glide.with(this).load(user.getPhotoUrl())
                .circleCrop()
                .into(binding.profileImage);

        // Load background image
        Glide.with(this).load(""/*TODO añadir url descarga imagen background*/)
                .apply(RequestOptions.bitmapTransform(new BlurTransformation(10, 1)))
                .into(binding.backgroundImage);

        binding.userNameText.setText(user.getDisplayName());
    }

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        Window window = getWindow();
        window.setFormat(PixelFormat.RGBA_8888);
    }
}