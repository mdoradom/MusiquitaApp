package com.musiquitaapp.youtube;

import static com.musiquitaapp.youtube.Auth.SCOPES;

import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.ExponentialBackOff;
import com.google.api.services.youtube.YouTube;
import com.musiquitaapp.R;

import java.io.IOException;
import java.util.Arrays;

public class YoutubeSingleton {

    private static YouTube youTube;
    private static YouTube youTubeWithCredentials;
    private static GoogleAccountCredential credential;

    private static YoutubeSingleton ourInstance = new YoutubeSingleton();

    public static YoutubeSingleton getInstance() {
        return ourInstance;
    }

    private YoutubeSingleton() {

        credential = GoogleAccountCredential.usingOAuth2(
                YTApplication.getAppContext(), Arrays.asList(SCOPES))
                .setBackOff(new ExponentialBackOff());

        youTube = new YouTube.Builder(new NetHttpTransport(), new GsonFactory(), new HttpRequestInitializer() {
            @Override
            public void initialize(HttpRequest httpRequest) throws IOException {

            }
        }).setApplicationName(YTApplication.getAppContext().getString(R.string.app_name))
                .build();

        youTubeWithCredentials = new YouTube.Builder(new NetHttpTransport(), new GsonFactory(), credential)
                .setApplicationName(YTApplication.getAppContext().getString(R.string.app_name))
                .build();

    }

    public static YouTube getYouTube() {
        return youTube;
    }

    public static YouTube getYouTubeWithCredentials() {
        return youTubeWithCredentials;
    }

    public static GoogleAccountCredential getCredential() {
        return credential;
    }
}