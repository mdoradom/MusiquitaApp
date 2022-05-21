package com.musiquitaapp.controllers;

import com.google.android.exoplayer2.Timeline;

public interface PlayerChangeController {
    void OnPositionChange(Timeline timeline, int reason);
    void OnPlayerStateChange(Boolean isPlaying, long position);
    void OnPlayerIsReady();
}