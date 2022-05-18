package com.musiquitaapp.youtube;

import android.app.Application;
import android.content.Context;
;import androidx.lifecycle.MutableLiveData;

import com.google.api.services.youtube.model.Activity;
import com.musiquitaapp.models.YouTubeVideo;

import java.util.ArrayList;

public class YTApplication extends Application {

    private static Context mContext;

    private static ArrayList<YouTubeVideo> mediaItems;

    public void onCreate() {
        super.onCreate();
        mediaItems = new ArrayList<>();
        mContext = getApplicationContext();
        pos.setValue(0);
    }

    private static YTApplication ourInstance = new YTApplication();

    private static MutableLiveData<Integer> pos = new MutableLiveData<>();

    public static MutableLiveData<Integer> getPos() { return pos; }

    public static void setPos(MutableLiveData<Integer> pos) { YTApplication.pos = pos; }

    public static YTApplication getOurInstance() { return ourInstance; }

    public static ArrayList<YouTubeVideo> getMediaItems(){ return mediaItems;}

    public static Context getAppContext() {
        return mContext;
    }

}