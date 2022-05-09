package com.musiquitaapp.services;

import static android.content.ContentValues.TAG;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.os.PowerManager;
import android.support.v4.media.RatingCompat;
import android.support.v4.media.session.MediaControllerCompat;
import android.support.v4.media.session.MediaSessionCompat;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.util.SparseArray;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;
import com.facebook.network.connectionclass.ConnectionClassManager;
import com.facebook.network.connectionclass.ConnectionQuality;
import com.facebook.network.connectionclass.DeviceBandwidthSampler;
import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.audio.AudioAttributes;
import com.musiquitaapp.R;
import com.musiquitaapp.models.Config;
import com.musiquitaapp.models.ItemType;
import com.musiquitaapp.models.YouTubeVideo;
import com.musiquitaapp.screens.media.PlayerActivity;

import at.huber.youtubeExtractor.VideoMeta;
import at.huber.youtubeExtractor.YouTubeExtractor;
import at.huber.youtubeExtractor.YtFile;

//import android.support.v4.media.app.NotificationCompat.MediaStyle;

public class BackgroundAudioService extends Service implements MediaPlayer.OnCompletionListener,
        MediaPlayer.OnPreparedListener{
    private ExoPlayer player;
    private MediaPlayer mMediaPlayer;
    private YouTubeVideo videoItem;
    private DeviceBandwidthSampler deviceBandwidthSampler;
    private static YouTubeExtractor youTubeExtractor;
    private ItemType mediaType = ItemType.YOUTUBE_MEDIA_NONE;
    private ConnectionQuality connectionQuality = ConnectionQuality.EXCELLENT;
    private MediaSessionCompat mSession;
    private MediaControllerCompat mController;
    public static final String ACTION_PLAY = "action_play";
    public static final String ACTION_PAUSE = "action_pause";
    public static final String ACTION_NEXT = "action_next";
    public static final String ACTION_PREVIOUS = "action_previous";
    public static final String ACTION_STOP = "action_stop";
    private NotificationCompat.Builder builder = null;
    private boolean isStarting = false;


    @Override
    public void onCreate(){
        super.onCreate();
        player = new ExoPlayer.Builder(getApplicationContext()).build();
        videoItem = new YouTubeVideo();
        mMediaPlayer = new MediaPlayer();
        mMediaPlayer.setOnCompletionListener(this);
        mMediaPlayer.setOnPreparedListener(this);
        initMediaSessions();
        initPhoneCallListener();
        deviceBandwidthSampler = DeviceBandwidthSampler.getInstance();
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        handleIntent(intent);
        return super.onStartCommand(intent, flags, startId);
    }
    /**
     * Handles intent (player options play/pause/stop...)
     *
     * @param intent
     */
    private void handleIntent(Intent intent) {
        if (intent == null || intent.getAction() == null)
            return;
        String action = intent.getAction();
        if (action.equalsIgnoreCase(ACTION_PLAY)) {
            handleMedia(intent);
            mController.getTransportControls().play();
        } else if (action.equalsIgnoreCase(ACTION_PAUSE)) {
            mController.getTransportControls().pause();
        } else if (action.equalsIgnoreCase(ACTION_PREVIOUS)) {
            mController.getTransportControls().skipToPrevious();
        } else if (action.equalsIgnoreCase(ACTION_NEXT)) {
            mController.getTransportControls().skipToNext();
        } else if (action.equalsIgnoreCase(ACTION_STOP)) {
            mController.getTransportControls().stop();
        }
    }
    private void playVideo(){
        isStarting = true;
        extractUrlAndPlay();

    }
    private void handleMedia(Intent intent) {
        ItemType intentMediaType = ItemType.YOUTUBE_MEDIA_NONE;
        if (intent.getSerializableExtra(Config.YOUTUBE_TYPE) != null) {
            intentMediaType = (ItemType) intent.getSerializableExtra(Config.YOUTUBE_TYPE);
        }
        Log.d("INTENTMEDIATYPE",intentMediaType.toString());
        switch (intentMediaType) {
            case YOUTUBE_MEDIA_NONE: //video is paused,so no new playback requests should be processed
                mMediaPlayer.start();
                break;
            case YOUTUBE_MEDIA_TYPE_VIDEO:
                mediaType = ItemType.YOUTUBE_MEDIA_TYPE_VIDEO;
                videoItem = (YouTubeVideo) intent.getSerializableExtra(Config.YOUTUBE_TYPE_VIDEO);
                if (videoItem.getId() != null) {
                    playVideo();
                }
                break;
            case YOUTUBE_MEDIA_TYPE_PLAYLIST: //new playlist playback request
                /*mediaType = ItemType.YOUTUBE_MEDIA_TYPE_PLAYLIST;
                youTubeVideos = (ArrayList<YouTubeVideo>) intent.getSerializableExtra(Config.YOUTUBE_TYPE_PLAYLIST);
                int startPosition = intent.getIntExtra(Config.YOUTUBE_TYPE_PLAYLIST_VIDEO_POS, 0);
                videoItem = youTubeVideos.get(startPosition);
                currentSongIndex = startPosition;
                playVideo();*/
                break;
            default:
                Log.d(TAG, "Unknown command");
                break;
        }
    }
    private YtFile getBestStream(SparseArray<YtFile> ytFiles) {

        connectionQuality = ConnectionClassManager.getInstance().getCurrentBandwidthQuality();
        int[] itags = new int[]{251, 141, 140, 17};

        if (connectionQuality != null && connectionQuality != ConnectionQuality.UNKNOWN) {
            switch (connectionQuality) {
                case POOR:
                    itags = new int[]{17, 140, 251, 141};
                    break;
                case MODERATE:
                    itags = new int[]{251, 141, 140, 17};
                    break;
                case GOOD:
                case EXCELLENT:
                    itags = new int[]{141, 251, 140, 17};
                    break;
            }
        }

        if (ytFiles.get(itags[0]) != null) {
            return ytFiles.get(itags[0]);
        } else if (ytFiles.get(itags[1]) != null) {
            return ytFiles.get(itags[1]);
        } else if (ytFiles.get(itags[2]) != null) {
            return ytFiles.get(itags[2]);
        }
        return ytFiles.get(itags[3]);
    }

    private void extractUrlAndPlay() {
        player = new ExoPlayer.Builder(getApplicationContext()).build();
        mMediaPlayer = new MediaPlayer();
        mMediaPlayer.setOnCompletionListener(this);
        mMediaPlayer.setOnPreparedListener(this);
        initMediaSessions();
        initPhoneCallListener();
        deviceBandwidthSampler = DeviceBandwidthSampler.getInstance();
        String youtubeLink = Config.YOUTUBE_BASE_URL + videoItem.getId();
        deviceBandwidthSampler.startSampling();

        youTubeExtractor = new YouTubeExtractor(this) {
            @Override
            protected void onExtractionComplete(SparseArray<YtFile> ytFiles, VideoMeta videoMeta) {
                if (ytFiles == null) {
                    // Something went wrong we got no urls. Always check this.
                    Toast.makeText(getApplicationContext(), "R.string.failed_playback",
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                deviceBandwidthSampler.stopSampling();
                YtFile ytFile = getBestStream(ytFiles);
                if (player != null) {

                    player.setMediaItem(MediaItem.fromUri(ytFile.getUrl()));
                    player.setAudioAttributes(new AudioAttributes.Builder()
                            .setUsage(C.USAGE_MEDIA)
                            .setContentType(C.CONTENT_TYPE_MUSIC)
                            .build(),true);
                    player.prepare();
                    //Listener para saber cuando esto está en buffer o no
                    /*
                    player.addListener(new Player.Listener(){
                        @Override
                        public void onPlaybackStateChanged(@Player.State int state){
                            if(state == Player.STATE_READY){

                            }else{

                            }
                        }
                    });*/

                    player.play();
                    /*mMediaPlayer.setDataSource(ytFile.getUrl());
                    mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                    mMediaPlayer.prepare();
                    mMediaPlayer.start();*/
                    Log.d("AAAAAAAA",String.valueOf(player.isPlaying()));
                    Toast.makeText(getApplicationContext(), "videoItem.getTitle()", Toast.LENGTH_SHORT).show();
                }
            }
        };

        youTubeExtractor.execute(youtubeLink);
        Log.d("AAAAAAAA", "o");
    }
    private void initMediaSessions() {
        // Make sure the media player will acquire a wake-lock while playing. If we don't do
        // that, the CPU might go to sleep while the song is playing, causing playback to stop.
        //
        // Remember that to use this, we have to declare the android.permission.WAKE_LOCK
        // permission in AndroidManifest.xml.
        mMediaPlayer.setWakeMode(getApplicationContext(), PowerManager.PARTIAL_WAKE_LOCK);

        PendingIntent buttonReceiverIntent = PendingIntent.getBroadcast(
                getApplicationContext(),
                0,
                new Intent(Intent.ACTION_MEDIA_BUTTON),
                PendingIntent.FLAG_UPDATE_CURRENT
        );

        mSession = new MediaSessionCompat(getApplicationContext(), "simple player session",
                null, buttonReceiverIntent);

        mController = new MediaControllerCompat(getApplicationContext(), mSession.getSessionToken());

        mSession.setCallback(
                new MediaSessionCompat.Callback() {
                    @Override
                    public void onPlay() {
                        super.onPlay();
                        buildNotification(generateAction(android.R.drawable.ic_media_pause, "Pause", ACTION_PAUSE));
                    }

                    @Override
                    public void onPause() {

                        super.onPause();
                        pauseVideo();
                        buildNotification(generateAction(android.R.drawable.ic_media_play, "Play", ACTION_PLAY));
                    }

                    @Override
                    public void onSkipToNext() {
                        super.onSkipToNext();
                        if (!isStarting) {
                            //playNext();
                        }
                        buildNotification(generateAction(android.R.drawable.ic_media_pause, "Pause", ACTION_PAUSE));
                    }

                    @Override
                    public void onSkipToPrevious() {
                        super.onSkipToPrevious();
                        if (!isStarting) {
                            //playPrevious();
                        }
                        buildNotification(generateAction(android.R.drawable.ic_media_pause, "Pause", ACTION_PAUSE));
                    }

                    @Override
                    public void onStop() {
                        super.onStop();
                        //stopPlayer();
                        //remove notification and stop service
                        NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
                        notificationManager.cancel(1);
                        Intent intent = new Intent(getApplicationContext(), BackgroundAudioService.class);
                        stopService(intent);
                    }

                    @Override
                    public void onSetRating(RatingCompat rating) {
                        super.onSetRating(rating);
                    }
                }
        );
    }
    private void initPhoneCallListener() {
        PhoneStateListener phoneStateListener = new PhoneStateListener() {
            @Override
            public void onCallStateChanged(int state, String incomingNumber) {
                if (state == TelephonyManager.CALL_STATE_RINGING) {
                    //Incoming call: Pause music
                    pauseVideo();
                } else if (state == TelephonyManager.CALL_STATE_IDLE) {
                    //Not in call: Play music
                    Log.d(TAG, "onCallStateChanged: ");
                    resumeVideo();
                } else if (state == TelephonyManager.CALL_STATE_OFFHOOK) {
                    //A call is dialing, active or on hold
                }
                super.onCallStateChanged(state, incomingNumber);
            }
        };

        TelephonyManager mgr = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        if (mgr != null) {
            mgr.listen(phoneStateListener, PhoneStateListener.LISTEN_CALL_STATE);
        }
    }
    private void pauseVideo() {
        if (mMediaPlayer.isPlaying()) {
            mMediaPlayer.pause();
        }
    }

    private void buildNotification(NotificationCompat.Action action) {

        final androidx.media.app.NotificationCompat.MediaStyle style = new androidx.media.app.NotificationCompat.MediaStyle();

        Intent intent = new Intent(getApplicationContext(), BackgroundAudioService.class);
        intent.setAction(ACTION_STOP);
        PendingIntent stopPendingIntent = PendingIntent.getService(getApplicationContext(), 1, intent, 0);

        Intent clickIntent = new Intent(this, PlayerActivity.class);
        clickIntent.setAction(Intent.ACTION_MAIN);
        clickIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        PendingIntent clickPendingIntent = PendingIntent.getActivity(this, 0, clickIntent, 0);

        builder = new NotificationCompat.Builder(this, "1");
        builder.setSmallIcon(R.mipmap.ic_launcher);
        builder.setContentTitle(videoItem.getTitle());
        builder.setContentInfo(videoItem.getDuration());
        builder.setShowWhen(false);
        builder.setContentIntent(clickPendingIntent);
        builder.setDeleteIntent(stopPendingIntent);
        builder.setOngoing(false);
        builder.setSubText(videoItem.getViewCount());
        builder.setStyle(style);

        //load bitmap for largeScreen
        if (videoItem.getThumbnailURL() != null && !videoItem.getThumbnailURL().isEmpty()) {
            Glide.with(getApplicationContext()).asBitmap().load(videoItem.getThumbnailURL()).into(target);
        }

        builder.addAction(generateAction(android.R.drawable.ic_media_previous, "Previous", ACTION_PREVIOUS));
        builder.addAction(action);
        builder.addAction(generateAction(android.R.drawable.ic_media_next, "Next", ACTION_NEXT));
        style.setShowActionsInCompactView(0, 1, 2);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(1, builder.build());

    }
    private NotificationCompat.Action generateAction(int icon, String title, String intentAction) {
        Intent intent = new Intent(getApplicationContext(), BackgroundAudioService.class);
        intent.setAction(intentAction);
        PendingIntent pendingIntent = PendingIntent.getService(getApplicationContext(), 1, intent, 0);
        return new NotificationCompat.Action.Builder(icon, title, pendingIntent).build();
    }

    /**
     * Field which handles image loading
     */
    private Target<Bitmap> target = new SimpleTarget<Bitmap>() {
        @Override
        public void onResourceReady(@NonNull Bitmap bitmap, @Nullable Transition transition) {
            updateNotificationLargeIcon(bitmap);
        }
    };
    /**
     * Updates only large icon in notification panel when bitmap is decoded
     *
     * @param bitmap
     */
    private void updateNotificationLargeIcon(Bitmap bitmap) {
        builder.setLargeIcon(bitmap);
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(1, builder.build());
    }
    /**
     * Resumes video
     */
    private void resumeVideo() {
        if (mMediaPlayer != null && mMediaPlayer.isPlaying()) {
            mMediaPlayer.start();
        }
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
    }
    @Override
    public void onCompletion(MediaPlayer mp) {

    }

    @Override
    public void onPrepared(MediaPlayer mp) {

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
