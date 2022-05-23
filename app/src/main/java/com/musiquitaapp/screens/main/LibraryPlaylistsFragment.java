package com.musiquitaapp.screens.main;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.musiquitaapp.R;
import com.musiquitaapp.adapters.PlaylistAdapter;
import com.musiquitaapp.controllers.PlaylistController;
import com.musiquitaapp.databinding.FragmentLibraryPlaylistsBinding;
import com.musiquitaapp.models.PlayListFirebase;

import java.util.List;
import java.util.UUID;

public class LibraryPlaylistsFragment extends Fragment {

    private FragmentLibraryPlaylistsBinding binding;
    private PlaylistAdapter playlistAdapter = null;
    private PlaylistController playlistController = new PlaylistController();
    private Uri uriImagen;

    public LibraryPlaylistsFragment() {
        // Required empty public constructor
    }

    public MutableLiveData<Uri> uriImagenSeleccionada = new MutableLiveData<>();

    public void setUriImagenSeleccionada(Uri uri) {
        uriImagenSeleccionada.setValue(uri);
    }

    private final ActivityResultLauncher<String> galeria = registerForActivityResult(new ActivityResultContracts.GetContent(), uri -> {
        setUriImagenSeleccionada(uri);
    });

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentLibraryPlaylistsBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        playlistController.getAllUserPlaylistsByUUID(user.getUid(), list -> {
            List<PlayListFirebase> playListFirebases = list;
            playlistAdapter = new PlaylistAdapter(playListFirebases, getContext());
            binding.playlistRecycler.setAdapter(playlistAdapter);
            binding.playlistRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        });

        binding.createPlaylist.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            final View customLayout = getLayoutInflater().inflate(R.layout.create_playlists_layout, null);
            builder.setView(customLayout);
            AlertDialog alertDialog = builder.create();

            TextView playlistName = customLayout.findViewById(R.id.playlistNameMenu);
            TextView playlistDescription = customLayout.findViewById(R.id.playlistDescriptionMenu);
            ImageView playlistImage = customLayout.findViewById(R.id.playlistImageSelector);

            galeria.launch("image/*");
            uriImagenSeleccionada.observe(getViewLifecycleOwner(), uri -> {
                uriImagen = uri;
            });

            Glide.with(getContext()).load(uriImagen).into(playlistImage);

            builder.setPositiveButton("Crear playlist", (dialog, id) -> {
                FirebaseStorage.getInstance()
                        .getReference("/playlists/" + UUID.randomUUID() + ".jpg")
                        .putFile(uriImagen)
                        .continueWithTask(task2 -> task2.getResult().getStorage().getDownloadUrl())
                        .addOnSuccessListener(uri -> new PlaylistController().createPlaylist(playlistName.getText().toString(), playlistDescription.getText().toString(), uri.toString()));
            });

            builder.setNegativeButton("Cancelar", (dialog, id) -> dialog.cancel());

            builder.show();
        });

        return view;
    }
}