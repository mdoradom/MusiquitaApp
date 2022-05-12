package com.musiquitaapp.models;

import com.google.api.services.youtube.model.ChannelBrandingSettings;

public class Channels {
    String id;
    Snippet snippet;
    ContentDetails contentDetails;
    Statistics statistics;
    TopicDetails topicDetails;
    Status status;

    // Revisar el modelo este
    ChannelBrandingSettings brandingSettings;
}
