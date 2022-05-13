package com.musiquitaapp.models;

public final class Config {
    public static final boolean DEBUG = false;

    public static final String SUGGESTIONS_URL = "http://suggestqueries.google.com/complete/search?client=youtube&ds=yt&q=";
    public static final String YOUTUBE_BASE_URL = "http://youtube.com/watch?v=";
    public static final String SHARE_VIDEO_URL = "http://youtube.com/watch?v=";
    public static final String SHARE_PLAYLIST_URL = "https://www.youtube.com/playlist?list=";
    public static final String YOUTUBE_TYPE = "YT_MEDIA_TYPE";
    public static final String YOUTUBE_TYPE_VIDEO = "YT_VIDEO";
    public static final String YOUTUBE_TYPE_PLAYLIST= "YT_PLAYLIST";
    public static final String YOUTUBE_TYPE_PLAYLIST_VIDEO_POS = "YT_PLAYLIST_VIDEO_POS";
    public static final String YOUTUBE_QUERY_VIDEO = "https://www.googleapis.com/youtube/v3/search?part=snippet&&maxResults=25&q=";
    public static final String YOUTUBE_QUERY_VIDEO_2 = "&type=all&key=";

    public static final String YOUTUBE_QUERY_PLAYLISTS = "https://www.googleapis.com/youtube/v3/search?part=snippet&&maxResults=25&q=";
    public static final String YOUTUBE_QUERY_PLAYLISTS_2 = "&type=playlist&key=";

    //public static final String YOUTUBE_API_KEY = "AIzaSyA4dKwi4otNjyMKgiQGv3gMEUFDBxRTcJ4";
    public static final String YOUTUBE_API_KEY = "AIzaSyDc7nfmLCmBYXiVf_uu3rwSJk3_izJQekc";

    public static final long NUMBER_OF_VIDEOS_RETURNED = 30; //due to YouTube API rules - MAX 50

}
