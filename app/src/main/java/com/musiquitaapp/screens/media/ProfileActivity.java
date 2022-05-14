package com.musiquitaapp.screens.media;

import android.content.Intent;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.musiquitaapp.R;
import com.musiquitaapp.databinding.ActivityProfileBinding;
import com.musiquitaapp.models.Profile;
import com.musiquitaapp.screens.login.AuthenticationMethodSelector;

import jp.wasabeef.glide.transformations.BlurTransformation;

public class ProfileActivity extends AppCompatActivity {

    private ActivityProfileBinding binding;
    private NavController navController;

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

        binding.exitIcon.setOnClickListener(v -> onBackPressed());

        binding.editIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO editar perfil
                Intent intent = new Intent(ProfileActivity.this, EditProfile.class);
                startActivity(intent);
            }
        });

        binding.searchIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO buscar perfil
            }
        });

        binding.shareIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO compartir perfil
            }
        });
    }

    private void setProfileResources() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        // Load pfp image
        Glide.with(this).load(user.getPhotoUrl())
                .circleCrop()
                .into(binding.profileImage);

        // Load background image
        FirebaseFirestore.getInstance().collection("Backgrounds")
                .document(user.getUid())
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    Profile profile = documentSnapshot.toObject(Profile.class);

                    binding.description.setText(profile.description);

                    Glide.with(ProfileActivity.this).load(profile.backgroundImage)
                            .apply(RequestOptions.bitmapTransform(new BlurTransformation(5, 1)))
                            .listener(new RequestListener<Drawable>() {
                                @Override
                                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                    return false;
                                }

                                @Override
                                public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                    binding.generalContainer.bringChildToFront(binding.exitIcon);
                                    binding.generalContainer.bringChildToFront(binding.editIcon);
                                    binding.generalContainer.bringChildToFront(binding.searchIcon);
                                    binding.generalContainer.bringChildToFront(binding.shareIcon);
                                    return false;
                                }
                            })
                            .into(binding.backgroundImage);

                });

        binding.userNameText.setText(user.getDisplayName());
    }

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        Window window = getWindow();
        window.setFormat(PixelFormat.RGBA_8888);
    }
}