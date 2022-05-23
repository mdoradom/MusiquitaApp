package com.musiquitaapp.youtube;

import android.app.Application;
import android.content.Context;

import androidx.lifecycle.MutableLiveData;

import com.google.android.exoplayer2.ExoPlayer;
import com.musiquitaapp.controllers.SessionController;
import com.musiquitaapp.models.YouTubeVideo;

import java.util.ArrayList;
public class YTApplication extends Application {

    private static Context mContext;
    private static ArrayList<YouTubeVideo> mediaItems;
    private static YTApplication ourInstance = new YTApplication();
    private static MutableLiveData<Integer> pos = new MutableLiveData<>();
    private static MutableLiveData<Boolean> isPlaying = new MutableLiveData<>();
    private static MutableLiveData<Boolean> isPaused = new MutableLiveData<>();
    private static ExoPlayer exoPlayer;
    private static SessionController sessionController;

    public void onCreate() {
        super.onCreate();
        mediaItems = new ArrayList<>();
        mContext = getApplicationContext();
        pos.setValue(0);
        isPlaying.setValue(false);
        isPaused.setValue(false);
        exoPlayer = new ExoPlayer.Builder(mContext).build();
        sessionController = new SessionController();
    }

    public static ExoPlayer getExoPlayer() {
        return exoPlayer;
    }

    public static void setExoPlayer(ExoPlayer exoPlayer) {
        YTApplication.exoPlayer = exoPlayer;
    }

    public static MutableLiveData<Boolean> getIsPaused() {
        return isPaused;
    }

    public static void setIsPaused(MutableLiveData<Boolean> isPaused) {
        YTApplication.isPaused = isPaused;
    }

    public static MutableLiveData<Boolean> getIsPlaying() {
        return isPlaying;
    }

    public static void setIsPlaying(MutableLiveData<Boolean> isPlaying) {
        YTApplication.isPlaying = isPlaying;
    }

    public static MutableLiveData<Integer> getPos() {
        return pos;
    }

    public static void setPos(MutableLiveData<Integer> pos) {
        YTApplication.pos = pos;
    }

    public static YTApplication getOurInstance() {
        return ourInstance;
    }

    public static ArrayList<YouTubeVideo> getMediaItems(){
        return mediaItems;
    }

    public static Context getAppContext() {
        return mContext;
    }

    public static SessionController getSessionController() { return sessionController; }
}