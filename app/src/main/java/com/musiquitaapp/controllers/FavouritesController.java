package com.musiquitaapp.controllers;

import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.musiquitaapp.models.PlayListFirebase;
import com.musiquitaapp.models.YouTubeVideo;
import com.musiquitaapp.screens.media.PlayerActivity;

import java.util.ArrayList;
import java.util.List;

public class FavouritesController {
    private FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

    public interface FavouritesCallback {
        void onCallBack(PlayListFirebase list);
    }

    public void getFavouriteSongs(FavouritesCallback favouritesCallback){
        FirebaseFirestore.getInstance()
                .collection("Favourites")
                .document(currentUser.getUid())
                .get()
                .addOnCompleteListener(task -> {
                    favouritesCallback.onCallBack(task.getResult().toObject(PlayListFirebase.class));
                });
    }

    public boolean isSongFavourited(YouTubeVideo song){
        final boolean[] songIsFavorite = {false};
        getFavouriteSongs((FavouritesCallback) list -> {
            if(list.songs.contains(song)){
                songIsFavorite[0] = true;
            }
        });
        return songIsFavorite[0];
    }

    public void addFavouriteSongs(YouTubeVideo song, Context context){
        getFavouriteSongs(list -> {
            if (!list.songs.contains(song)) {
                list.songs.add(song);
                FirebaseFirestore.getInstance()
                        .collection("Favourites")
                        .document(currentUser.getUid())
                        .set(list)
                        .addOnSuccessListener(unused -> Toast.makeText(context, "Canción añadida a favoritos", Toast.LENGTH_SHORT).show())
                        .addOnFailureListener(e -> Toast.makeText(context, "Error al añadir a Favoritos: " + e.getLocalizedMessage(), Toast.LENGTH_SHORT).show());
            }
        });
    }

    public void deleteFavouriteSongs(YouTubeVideo song, Context context){
        getFavouriteSongs(list -> {
            list.songs.remove(song);
            FirebaseFirestore.getInstance()
                    .collection("Favourites")
                    .document(currentUser.getUid())
                    .set(list)
                    .addOnSuccessListener(unused -> Toast.makeText(context, "Canción eliminada de favoritos", Toast.LENGTH_SHORT).show())
                    .addOnFailureListener(e -> Toast.makeText(context, "Error al eliminar de favoritos: " + e.getLocalizedMessage(), Toast.LENGTH_SHORT).show());
        });
    }
}