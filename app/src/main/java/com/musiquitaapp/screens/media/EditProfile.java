package com.musiquitaapp.screens.media;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.MutableLiveData;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.auth.User;
import com.google.firebase.storage.FirebaseStorage;
import com.musiquitaapp.R;
import com.musiquitaapp.databinding.ActivityEditProfileBinding;
import com.musiquitaapp.databinding.ActivityProfileBinding;
import com.musiquitaapp.models.Profile;

import java.util.UUID;

public class EditProfile extends AppCompatActivity {

    private ActivityEditProfileBinding binding;
    private Uri uriImagen;
    private Uri uriImagen2;

    public MutableLiveData<Uri> uriImagenSeleccionada = new MutableLiveData<>();

    public void setUriImagenSeleccionada(Uri uri) {
        uriImagenSeleccionada.setValue(uri);
    }

    private final ActivityResultLauncher<String> galeria = registerForActivityResult(new ActivityResultContracts.GetContent(), uri -> {
        setUriImagenSeleccionada(uri);
    });

    public MutableLiveData<Uri> uriImagenSeleccionada2 = new MutableLiveData<>();

    public void setUriImagenSeleccionada2(Uri uri2) {
        uriImagenSeleccionada2.setValue(uri2);
    }

    private final ActivityResultLauncher<String> galeria2 = registerForActivityResult(new ActivityResultContracts.GetContent(), uri2 -> {
        setUriImagenSeleccionada2(uri2);
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEditProfileBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        binding.imageSelector1.setOnClickListener(v -> galeria.launch("image/*"));

        binding.imageSelector2.setOnClickListener(v -> galeria2.launch("image/*"));

        uriImagenSeleccionada.observe(this, uri -> {
            Glide.with(this).load(uri).into(binding.imageSelector1);
            uriImagen = uri;
        });

        uriImagenSeleccionada2.observe(this, uri2 -> {
            Glide.with(this).load(uri2).into(binding.imageSelector2);
            uriImagen2 = uri2;
        });

        binding.buttonEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                        .setDisplayName(binding.tietUser.getText().toString())
                        .setPhotoUri(uriImagen)
                        .build();

                user.updateProfile(profileUpdates)
                        .addOnCompleteListener(task -> {
                           if (task.isSuccessful()) {
                                user.updatePassword(binding.tietPasswd.getText().toString())
                                        .addOnCompleteListener(task1 -> FirebaseStorage.getInstance().getReference("/backgrounds/" + UUID.randomUUID() + ".jpg")
                                                .putFile(uriImagen2)
                                                .continueWithTask(task2 -> task2
                                                        .getResult()
                                                        .getStorage()
                                                        .getDownloadUrl())
                                                .addOnSuccessListener(uri -> FirebaseFirestore
                                                        .getInstance()
                                                        .collection("Backgrounds")
                                                        .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                                        .set(new Profile(uri.toString(), binding.tietDesc.getText().toString()))
                                                        .addOnSuccessListener(unused -> {
                                                            Intent intent = new Intent(EditProfile.this, ProfileActivity.class);
                                                            startActivity(intent);
                                                        })));
                           } else {
                               Toast.makeText(EditProfile.this, task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                           }
                        });
            }
        });
    }
}