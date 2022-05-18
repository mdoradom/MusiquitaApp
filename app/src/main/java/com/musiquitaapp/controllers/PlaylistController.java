package com.musiquitaapp.controllers;

import androidx.annotation.Nullable;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.musiquitaapp.models.PlayListFirebase;
import com.musiquitaapp.models.YouTubeVideo;

import java.util.ArrayList;
import java.util.List;

public class PlaylistController {

    private PlayListFirebase myPlaylist;
    private FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();



    public void setCurrentUser(FirebaseUser currentUser) {
        this.currentUser = currentUser;
    }

    public void createPlaylist(String title, String description, String thumbnailUrl){
        List<YouTubeVideo> songs = new ArrayList<>();
        myPlaylist = new PlayListFirebase(currentUser.getEmail(), currentUser.getUid(),
                description, title, thumbnailUrl, songs);

        FirebaseFirestore.getInstance()
                .collection("Playlists")
                .document(myPlaylist.playlistID)
                .set(myPlaylist);
    }

    public void deletePlaylist(String playlistID){
        FirebaseFirestore.getInstance().collection("Playlists")
                .document(playlistID)
                .delete();
    }

    public List<PlayListFirebase> getPlaylistsByTitle(String playlistTitle){
        List<PlayListFirebase> result = new ArrayList<>();
        FirebaseFirestore.getInstance()
                .collection("Playlists")
                .whereEqualTo("playlistTitle", playlistTitle)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for(DocumentSnapshot doc: queryDocumentSnapshots.getDocuments()){
                        result.add(doc.toObject(PlayListFirebase.class));
                    }
                });
        return result;
    }

    public List<PlayListFirebase> getAllUserPlaylistsByUsername(String username){
        List<PlayListFirebase> result = new ArrayList<>();
        FirebaseFirestore.getInstance()
                .collection("Playlists")
                .whereEqualTo("authorName", username)
                .addSnapshotListener((value, error) -> {
                    for(DocumentSnapshot doc: value.getDocuments()){
                        result.add(doc.toObject(PlayListFirebase.class));
                    }
                });
        return result;
    }

    static List<PlayListFirebase> result = new ArrayList<>();

    public void getAllUserPlaylistsByUUID(String uuid, FirestoreCallback firestoreCallback){
        //List<PlayListFirebase> result = new ArrayList<>();

        FirebaseFirestore.getInstance()
                .collection("Playlists")
                .whereEqualTo("authorID", uuid)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isComplete()) {
                        for (DocumentSnapshot documentSnapshot: task.getResult().getDocuments()) {
                            PlayListFirebase playListFirebase = documentSnapshot.toObject(PlayListFirebase.class);
                            result.add(playListFirebase);
                            System.out.println("SIZE dentro: " + result.size());
                        }
                        firestoreCallback.onCallback(result);
                    }
                });
        System.out.println("SIZE fuera: " + result.size());
    }

    public interface FirestoreCallback {
        void onCallback(List<PlayListFirebase> list);
    }

    private List<PlayListFirebase> callbackGetAllUserPlaylistsByUUID (List<PlayListFirebase> result) {
        return result;
    }

    public DocumentReference getPlaylistReferenceByID(String playlistID){
        List<DocumentReference> result = new ArrayList<>();
        FirebaseFirestore.getInstance()
                .collection("Playlists")
                .whereEqualTo("playlistID", playlistID)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        result.add(value.getDocuments().get(0).getReference());
                    }
                });
        return result.get(0);
    }

    public void addSongToPlaylist(YouTubeVideo song, String playlistID){
        FirebaseFirestore.getInstance()
                .collection("Playlists")
                .whereEqualTo("playlistID", playlistID)
                .addSnapshotListener((value, error) -> {
                    value.getDocuments().get(0).getReference()
                            .get()
                            .addOnSuccessListener(documentSnapshot ->
                                    myPlaylist = documentSnapshot.toObject(PlayListFirebase.class));
                    myPlaylist.songs.add(song);
                    value.getDocuments().get(0).getReference()
                            .set(myPlaylist);
                });
    }

    public void removeSong(YouTubeVideo song, String playlistID){
        FirebaseFirestore.getInstance()
                .collection("Playlists")
                .whereEqualTo("playlistID", playlistID)
                .addSnapshotListener((value, error) -> {
                    value.getDocuments().get(0).getReference()
                            .get()
                            .addOnSuccessListener(documentSnapshot ->
                                    myPlaylist = documentSnapshot.toObject(PlayListFirebase.class));
                    myPlaylist.songs.remove(song);
                    value.getDocuments().get(0).getReference()
                            .set(myPlaylist);
                });
    }
}