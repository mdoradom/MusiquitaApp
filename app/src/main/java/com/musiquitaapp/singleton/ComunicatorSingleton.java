package com.musiquitaapp.singleton;

import com.musiquitaapp.controllers.SessionController;
import com.musiquitaapp.services.BackgroundAudioService;

public final class ComunicatorSingleton {

    public SessionController controller;
    public BackgroundAudioService service;
    private static ComunicatorSingleton instance;

    private ComunicatorSingleton() {
        if(controller==null && service==null){
            this.controller = new SessionController();
            this.service = new BackgroundAudioService();
        }
    }

    public static ComunicatorSingleton getInstance(){
        if(instance == null){
            instance = new ComunicatorSingleton();
        }
        return instance;
    }

}