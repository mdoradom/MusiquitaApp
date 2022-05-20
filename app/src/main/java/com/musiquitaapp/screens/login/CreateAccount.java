package com.musiquitaapp.screens.login;

import static android.content.ContentValues.TAG;

import android.net.Uri;
import android.os.Bundle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.google.firebase.storage.internal.StorageReferenceUri;
import com.musiquitaapp.R;
import com.musiquitaapp.controllers.FavouritesController;
import com.musiquitaapp.databinding.FragmentCreateAccountBinding;
import com.musiquitaapp.models.PlayListFirebase;
import com.musiquitaapp.models.Profile;
import com.musiquitaapp.models.YouTubeVideo;
import com.musiquitaapp.screens.BaseFragment;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CreateAccount extends BaseFragment {

    private FragmentCreateAccountBinding binding;
    private Uri uriImagen;
    private Uri uriImagen2;

    public CreateAccount() {
        // Required empty public constructor
    }

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
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentCreateAccountBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        binding.imageSelector1.setOnClickListener(v -> galeria.launch("image/*"));

        binding.imageSelector2.setOnClickListener(v -> galeria2.launch("image/*"));

        uriImagenSeleccionada.observe(getViewLifecycleOwner(), uri -> {
            Glide.with(this).load(uri).into(binding.imageSelector1);
            uriImagen = uri;
        });

        uriImagenSeleccionada2.observe(getViewLifecycleOwner(), uri2 -> {
            Glide.with(this).load(uri2).into(binding.imageSelector2);
            uriImagen2 = uri2;
        });

        binding.buttonRegister.setOnClickListener(v -> {
            if (binding.tietPasswd.getText().toString().isEmpty()) {
                binding.tietPasswd.setError("Required");
                return;
            }

            FirebaseAuth.getInstance()
                    .createUserWithEmailAndPassword(
                            binding.tietMail.getText().toString(),
                            binding.tietPasswd.getText().toString()
                    ).addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            if (binding.tietUser.getText().toString().isEmpty()) {
                                binding.tietUser.setError("Required");
                            } else {
                                // subo la foto a firebase y obtengo la uri de descarga
                                FirebaseStorage.getInstance()
                                        .getReference("/images/"+ UUID.randomUUID()+".jpg")
                                        .putFile(uriImagen)
                                        .continueWithTask(task2 -> task2.getResult().getStorage().getDownloadUrl())
                                        .addOnSuccessListener(uri -> {
                                            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                                            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                                    .setDisplayName(binding.tietUser.getText().toString())
                                                    .setPhotoUri(uri)
                                                    .build();

                                            user.updateProfile(profileUpdates)
                                                    .addOnCompleteListener(task1 -> {
                                                        if (task1.isSuccessful()) {
                                                            Log.d(TAG, "User profile updated.");
                                                        }
                                                    });
                                        });

                                // subir la foto de background a firebase y añadirla a la colección con el UUID del usuario asociado
                                FirebaseStorage.getInstance().getReference("/backgrounds/" + UUID.randomUUID() + ".jpg")
                                        .putFile(uriImagen2)
                                        .continueWithTask(task2 -> task2
                                                .getResult()
                                                .getStorage()
                                                .getDownloadUrl())
                                        .addOnSuccessListener(uri -> FirebaseFirestore
                                                .getInstance()
                                                .collection("Backgrounds")
                                                .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                                .set(new Profile(uri.toString()))
                                                .addOnSuccessListener(unused ->
                                                        navController.navigate(R.id.dashboardActivity)
                                                )
                                        );

                                new FavouritesController().getFavouriteSongs(list -> {
                                    if (list == null) {
                                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                                        List<YouTubeVideo> videos = new ArrayList<>();

                                        FirebaseFirestore.getInstance()
                                                .collection("Favourites")
                                                .document(user.getUid())
                                                .set(new PlayListFirebase(user.getEmail(), user.getUid(), "", "Liked Songs", "", videos));
                                    }
                                });
                            }
                        } else {
                            Toast.makeText(requireContext(), task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        });

        return view;
    }
}