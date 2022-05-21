package com.musiquitaapp.controllers;

import android.content.Intent;

import androidx.fragment.app.FragmentActivity;

import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.Timeline;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.musiquitaapp.models.Config;
import com.musiquitaapp.models.ItemType;
import com.musiquitaapp.models.MySession;
import com.musiquitaapp.models.PlayerDirections;
import com.musiquitaapp.models.YouTubeVideo;
import com.musiquitaapp.services.BackgroundAudioService;
import com.musiquitaapp.singleton.ComunicatorSingleton;

import java.util.ArrayList;
import java.util.List;

public class SessionController implements PlayerChangeController {

    private static boolean isOwner;

    private static boolean recentllyJoined;
    private static DocumentReference db;
    private YouTubeVideo videoItem;
    private Intent serviceIntent;
    private FirebaseAuth mAuth;
    private MySession mySession;
    private Thread sessionThread;
    //private FragmentPantall1Binding binding;
    private ComunicatorSingleton comunicator;
    private FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
    private FragmentActivity myActivity;
    private List<MySession> sessions;


    public interface SessionCallback {
        void onCallback(List<MySession> list);
    }

    public void setMyActivity(FragmentActivity myActivity){
        this.myActivity = myActivity;
    }

    /*public void setBinding(FragmentPantall1Binding binding) {
        this.binding = binding;
    }*/

    public void setComunicator(ComunicatorSingleton comunicatorSingleton){
        this.comunicator = comunicatorSingleton;
    }

    /*public void createAudioService(FragmentActivity activity){
        Song songTmp = mySession.currentSong;

        videoItem = new YouTubeVideo();

        videoItem.setId(songTmp.youtubeID);
        videoItem.setDuration(songTmp.duration);
        videoItem.setTitle(songTmp.title);
        videoItem.setViewCount(songTmp.viewCount); //2
        videoItem.setThumbnailURL(songTmp.thumbnailURL);

        //Esto puede que falle, probar bien si resulta en éxito o fracaso

        serviceIntent = new Intent(activity, comunicator.service.getClass());

        serviceIntent.setAction(BackgroundAudioService.ACTION_PLAY);
        serviceIntent.putExtra(Config.YOUTUBE_TYPE, ItemType.YOUTUBE_MEDIA_TYPE_VIDEO);
        serviceIntent.putExtra(Config.YOUTUBE_TYPE_VIDEO, videoItem);
    }*/

    public void createSession(String sessionName){
        mySession = new MySession(sessionName, currentUser.getEmail(), 0,
                0, 0, false, false, 0,
                null, null, 1);


        FirebaseFirestore.getInstance().collection("Minutes")
                .document(currentUser.getUid())
                .set(mySession)
                .addOnSuccessListener(task2 -> {

                });

        //We set the Owner Event Listener here
        FirebaseFirestore.getInstance().collection("Minutes")
                .document(currentUser.getUid())
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    db = documentSnapshot.getReference();
                    System.out.println(db.getPath());
                    db.addSnapshotListener((value, error) -> {
                        //We want only the Session Owner to send the minutes once they are requested
                        //By this way we avoid all the users overwriting and losing seconds cause of that
                        MySession sessionTmp = value.toObject(MySession.class);
                        if(sessionTmp.recentlyJoined){
                            publishSession();
                        } else{
                            mySession = sessionTmp;
                            //evaluateSession();
                        }
                    });
                });
    }

    public void publishSession(){
        db.set(mySession).addOnSuccessListener(task -> {
            /*binding.textUser.setText(mySession.ownerName);
            binding.textHour.setText(mySession.hour + "");
            binding.textMinutes.setText(mySession.minute + "");
            binding.textSeconds.setText(mySession.second + "");*/
        });

        /*FirebaseFirestore.getInstance().collection("Minutes")
                .document(currentUser.getUid())
                .set(mySession)
                .addOnSuccessListener(task2 -> {

                    binding.textUser.setText(mySession.ownerName);
                    binding.textHour.setText(mySession.hour + "");
                    binding.textMinutes.setText(mySession.minute + "");
                    binding.textSeconds.setText(mySession.second + "");
                });*/
    }

    public void getAllSessions(SessionCallback sessionCallback){
        FirebaseFirestore.getInstance().collection("Minutes")
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        sessions = queryDocumentSnapshots.toObjects(MySession.class);
                        sessionCallback.onCallback(sessions);
                    }
                });
    }

    public void joinSession(String name){
        /*FirebaseFirestore.getInstance().collection("Minutes")
                .whereEqualTo("ownerName", name)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    db = queryDocumentSnapshots.getDocuments().get(0).getReference();
                    db.get().addOnSuccessListener(
                            documentSnapshot -> mySession = documentSnapshot.toObject(MySession.class)
                    );

                    for (DocumentSnapshot doc: queryDocumentSnapshots.getDocuments()){
                        db = doc.getReference();
                    }
                });*/
        FirebaseFirestore.getInstance().collection("Minutes")
                .whereEqualTo("ownerName", name)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (DocumentSnapshot doc: queryDocumentSnapshots.getDocuments()){
                        db = doc.getReference();

                        db.get().addOnSuccessListener(
                                documentSnapshot -> mySession = documentSnapshot.toObject(MySession.class)
                        );

                        mySession.recentlyJoined = recentllyJoined;
                        mySession.connectedUsers++;

                        db.set(mySession);
                        db.addSnapshotListener((value, error) -> {
                            if (value != null && value.exists()) {

                                MySession sessionTmp = value.toObject(MySession.class);
                                if(!sessionTmp.recentlyJoined){
                                    mySession = value.toObject(MySession.class);
                                    /*binding.textUser.setText(mySession.ownerName);
                                    binding.textHour.setText(mySession.hour + "");
                                    binding.textMinutes.setText(mySession.minute + "");
                                    binding.textSeconds.setText(mySession.second + "");*/

                                }
                            }
                        });

                    }
                });
    }

    public void songChange(PlayerDirections direction){
        //Song songTmp = new Song();
        //MySession sessionTmp = new MySession();
        //Update the song to the server here
        if(direction == PlayerDirections.BACKWARDS){

            if(mySession.currentSongIndex>0){
                mySession.currentSongIndex--;
            }else{
                mySession.currentSongIndex = mySession.songsInQueue.size()-1;
            }
            mySession.currentSong = mySession.songsInQueue.get(mySession.currentSongIndex);
        } else if (direction == PlayerDirections.FORWARDS){
            if(mySession.currentSongIndex<mySession.songsInQueue.size()-1){
                mySession.currentSongIndex++;
            } else{
                mySession.currentSongIndex = 0;
            }
            mySession.currentSong = mySession.songsInQueue.get(mySession.currentSongIndex);
        }

        publishSession();

        /*Song songTmp = mySession.currentSong;

        videoItem = new YouTubeVideo();

        videoItem.setId(songTmp.youtubeID);
        videoItem.setDuration(songTmp.duration);
        videoItem.setTitle(songTmp.title);
        videoItem.setViewCount(songTmp.viewCount); //2
        videoItem.setThumbnailURL(songTmp.thumbnailURL);*/

        serviceIntent.setAction(BackgroundAudioService.ACTION_PLAY);
        serviceIntent.putExtra(Config.YOUTUBE_TYPE, ItemType.YOUTUBE_MEDIA_TYPE_VIDEO);
        serviceIntent.putExtra(Config.YOUTUBE_TYPE_VIDEO, videoItem);

    }

    public void play(){
        myActivity.startService(serviceIntent);
        mySession.isPlaying = true;
        publishSession();
    }

    /*public void play(){
        if (mySession == null) return;
        sessionThread = new Thread(() -> {
            try {
                mySession.isPlaying = true;

                //While song currentTime does not surpass song length
                while (mySession.isPlaying){
                    Thread.sleep(1000);
                    mySession.second++;
                    if(mySession.second > 60){
                        mySession.second = 0;
                        mySession.minute++;
                        if(mySession.minute > 60){
                            mySession.minute = 0;
                            mySession.hour++;
                        }
                    }
                    System.out.println(mySession.hour + " : " + mySession.minute + " : " + mySession.second);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        sessionThread.start();
    }*/

    public void pause(boolean serverPauseSignal){
        /*if(sessionThread != null){
            sessionThread.interrupt();
            mySession.isPlaying = false;
            if(!serverPauseSignal){
                publishSession();
            }
            //
        }*/
        mySession.isPlaying = false;
        myActivity.stopService(serviceIntent);
    }

    public void deleteSession(){
        pause(false);
        FirebaseFirestore.getInstance().collection("Minutes")
                .document(currentUser.getUid())
                .delete();
    }

    public void evaluateSession(){
        if(!mySession.isPlaying){
            pause(true);
        }
    }

    public void setSessionReady(){
        if(mySession.isUserReady == null){
            mySession.isUserReady = new ArrayList<>();
        }
        mySession.isUserReady.add(true);

        publishSession();
    }

    public void addSongToQueue(YouTubeVideo song){
        mySession.songsInQueue.add(song);
    }

    @Override
    public void OnPositionChange(Timeline timeline, int reason) {
        //mySession.timeline = timeline;
        if(reason== Player.TIMELINE_CHANGE_REASON_PLAYLIST_CHANGED){
            publishSession();
        }

    }

    @Override
    public void OnPlayerStateChange(Boolean isPlaying, long position) {
        if(isPlaying){
            play();
        } else{
            pause(true);
        }
    }

    @Override
    public void OnPlayerIsReady() {
        setSessionReady();
        play();
    }

}