package com.musiquitaapp.services;

import static android.content.ContentValues.TAG;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.os.PowerManager;
import android.support.v4.media.RatingCompat;
import android.support.v4.media.session.MediaControllerCompat;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyCallback;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.util.SparseArray;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
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
import com.google.android.exoplayer2.MediaMetadata;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.audio.AudioAttributes;
import com.google.android.exoplayer2.ui.DownloadNotificationHelper;
import com.google.android.exoplayer2.ui.PlayerNotificationManager;
import com.google.android.exoplayer2.util.NotificationUtil;
import com.musiquitaapp.R;
import com.musiquitaapp.models.Config;
import com.musiquitaapp.models.ItemType;
import com.musiquitaapp.models.YouTubeVideo;
import com.musiquitaapp.screens.media.PlayerActivity;
import com.musiquitaapp.youtube.YTApplication;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import at.huber.youtubeExtractor.VideoMeta;
import at.huber.youtubeExtractor.YouTubeExtractor;
import at.huber.youtubeExtractor.YtFile;

//import android.support.v4.media.app.NotificationCompat.MediaStyle;

public class BackgroundAudioService extends Service implements MediaPlayer.OnCompletionListener,
        MediaPlayer.OnPreparedListener{
    //private ExoPlayer player;
    private YouTubeVideo videoItem;
    private DeviceBandwidthSampler deviceBandwidthSampler;
    private static YouTubeExtractor youTubeExtractor;
    private ItemType mediaType = ItemType.YOUTUBE_MEDIA_NONE;
    private ConnectionQuality connectionQuality = ConnectionQuality.EXCELLENT;
    private MediaSessionCompat mSession;
    private MediaControllerCompat mController;
    public static final String ACTION_PLAY = "action_play";
    public static final String ACTION_RESUME = "action_resume";
    public static final String ACTION_PAUSE = "action_pause";
    public static final String ACTION_NEXT = "action_next";
    public static final String ACTION_PREVIOUS = "action_previous";
    public static final String ACTION_STOP = "action_stop";
    public static final String ACTION_REPEAT_ONE = "action_repeat_one";
    public static final String ACTION_REPEAT_NONE = "action_repeat_none";
    private NotificationCompat.Builder builder = null;
    private boolean isStarting = false;
    private String log = "music";



    @Override
    public void onCreate(){
        super.onCreate();


        videoItem = new YouTubeVideo();
        YTApplication.getExoPlayer().addListener(new Player.Listener() {

            @Override
            public void onPlaybackStateChanged(int playbackState) {
                Player.Listener.super.onPlaybackStateChanged(playbackState);
                if (playbackState == ExoPlayer.STATE_ENDED){
                    if (YTApplication.getExoPlayer().getRepeatMode() == ExoPlayer.REPEAT_MODE_ALL){
                        playNext();
                        buildNotification(generateAction(android.R.drawable.ic_media_pause, "Pause", ACTION_PAUSE));
                    }else if (YTApplication.getExoPlayer().getRepeatMode() == ExoPlayer.REPEAT_MODE_OFF&&YTApplication.getPos().getValue()<YTApplication.getMediaItems().size()){
                        playNext();
                        buildNotification(generateAction(android.R.drawable.ic_media_pause, "Pause", ACTION_PAUSE));
                    }
                }
            }
        });
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
        Log.d(log, action);
        if (action.equalsIgnoreCase(ACTION_PLAY)) {
            handleMedia(intent);
            mController.getTransportControls().play();
        }else if (action.equalsIgnoreCase(ACTION_RESUME)){
            mController.getTransportControls().play();
        }else if (action.equalsIgnoreCase(ACTION_PAUSE)) {
            mController.getTransportControls().pause();
        } else if (action.equalsIgnoreCase(ACTION_PREVIOUS)) {
            mController.getTransportControls().skipToPrevious();
        } else if (action.equalsIgnoreCase(ACTION_NEXT)) {
            mController.getTransportControls().skipToNext();
        } else if (action.equalsIgnoreCase(ACTION_STOP)) {
            mController.getTransportControls().stop();
        } else if(action.equalsIgnoreCase(ACTION_REPEAT_ONE)){
            mController.getTransportControls().setRepeatMode(PlaybackStateCompat.REPEAT_MODE_ONE);
        } else if(action.equalsIgnoreCase(ACTION_REPEAT_NONE)){
            mController.getTransportControls().setRepeatMode(PlaybackStateCompat.REPEAT_MODE_NONE);
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

                break;
            case YOUTUBE_MEDIA_TYPE_VIDEO:
                mediaType = ItemType.YOUTUBE_MEDIA_TYPE_VIDEO;
                videoItem = (YouTubeVideo) intent.getSerializableExtra(Config.YOUTUBE_TYPE_VIDEO);
                if (videoItem.getId() != null) {
                    playVideo();
                }
                break;
            case YOUTUBE_MEDIA_TYPE_PLAYLIST: //new playlist playback request
                mediaType = ItemType.YOUTUBE_MEDIA_TYPE_PLAYLIST;
                videoItem = YTApplication.getMediaItems().get(YTApplication.getPos().getValue());
                playVideo();
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
        initMediaSessions();
        initPhoneCallListener();
        deviceBandwidthSampler = DeviceBandwidthSampler.getInstance();
        String youtubeLink = Config.YOUTUBE_BASE_URL + videoItem.getId();
        deviceBandwidthSampler.startSampling();

        youTubeExtractor = new YouTubeExtractor(this) {
            @Override
            protected void onExtractionComplete(SparseArray<YtFile> ytFiles, VideoMeta videoMeta) {
                if (ytFiles == null) {
                }
                deviceBandwidthSampler.stopSampling();
                YtFile ytFile = getBestStream(ytFiles);
                if (YTApplication.getExoPlayer() != null) {
                    YTApplication.getExoPlayer().setMediaItem(MediaItem.fromUri(ytFile.getUrl()));
                    YTApplication.getExoPlayer().setAudioAttributes(new AudioAttributes.Builder()
                            .setUsage(C.USAGE_MEDIA)
                            .setContentType(C.CONTENT_TYPE_MUSIC)
                            .build(),true);
                    YTApplication.getExoPlayer().prepare();
                    YTApplication.getExoPlayer().play();
                }
            }
        };
        youTubeExtractor.execute(youtubeLink);
        buildNotification(generateAction(android.R.drawable.ic_media_play, "Play", ACTION_PLAY));
    }
    private void initMediaSessions() {
        // Make sure the media player will acquire a wake-lock while playing. If we don't do
        // that, the CPU might go to sleep while the song is playing, causing playback to stop.
        //
        // Remember that to use this, we have to declare the android.permission.WAKE_LOCK
        // permission in AndroidManifest.xml.
        YTApplication.getExoPlayer().setWakeMode(PowerManager.PARTIAL_WAKE_LOCK);

        PendingIntent buttonReceiverIntent = PendingIntent.getBroadcast(
                getApplicationContext(),
                0,
                new Intent(Intent.ACTION_MEDIA_BUTTON),
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        mSession = new MediaSessionCompat(getApplicationContext(), "simple player session",
                null, buttonReceiverIntent);

        mController = new MediaControllerCompat(getApplicationContext(), mSession.getSessionToken());

        mSession.setCallback(
                new MediaSessionCompat.Callback() {
                    @Override
                    public void onSetRepeatMode(int repeatMode){
                        super.onSetRepeatMode(repeatMode);
                        YTApplication.getExoPlayer().setRepeatMode(repeatMode);
                    }
                    @Override
                    public void onPlay() {
                        super.onPlay();
                        Log.d(log, YTApplication.getExoPlayer().isCurrentMediaItemLive()+"\n"+YTApplication.getExoPlayer().isPlaying());
                        resumeVideo();
                        buildNotification(generateAction(android.R.drawable.ic_media_pause, "Pause", ACTION_PAUSE));
                    }

                    @Override
                    public void onPause() {
                        Log.d(log, "onPause: ");
                        super.onPause();
                        pauseVideo();
                        buildNotification(generateAction(android.R.drawable.ic_media_play, "Play", ACTION_PLAY));
                    }

                    @Override
                    public void onSkipToNext() {
                        super.onSkipToNext();
                        playNext();
                        buildNotification(generateAction(android.R.drawable.ic_media_pause, "Pause", ACTION_PAUSE));

                    }

                    @Override
                    public void onSkipToPrevious() {
                        super.onSkipToPrevious();

                        playPrevious();
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
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                mgr.registerTelephonyCallback(getMainExecutor(),new TelephonyCallback());
            }else{
                mgr.listen(phoneStateListener, PhoneStateListener.LISTEN_CALL_STATE);
            }
        }
    }

    private void pauseVideo() {
        if (YTApplication.getExoPlayer().isPlaying()) {
            YTApplication.getExoPlayer().pause();

        }
    }
    private void playPrevious() {
        //if media type is video not playlist, just loop it
        if (mediaType == ItemType.YOUTUBE_MEDIA_TYPE_VIDEO) {
            seekVideo(0);
            playVideo();
            return;
        }
        if (YTApplication.getPos().getValue() - 1 >= 0) {
            YTApplication.getPos().setValue(YTApplication.getPos().getValue()-1);
        } else { //play last song
            YTApplication.getPos().setValue(YTApplication.getMediaItems().size()-1);
        }
        videoItem = YTApplication.getMediaItems().get(YTApplication.getPos().getValue());
        extractUrlAndPlay();
    }
    private void playNext() {
        //if media type is video not playlist, just loop it
        if (mediaType == ItemType.YOUTUBE_MEDIA_TYPE_VIDEO) {
            seekVideo(0);
            playVideo();
            return;
        }
        if (YTApplication.getPos().getValue() +1 >= YTApplication.getMediaItems().size() ) {//play 1st song
            YTApplication.getPos().setValue(0);
        } else {
            YTApplication.getPos().setValue(YTApplication.getPos().getValue() +1);
        }

        videoItem = YTApplication.getMediaItems().get(YTApplication.getPos().getValue());
        extractUrlAndPlay();
    }
    private void seekVideo(int seekTo) {
        YTApplication.getExoPlayer().seekTo(seekTo);
    }
    private void buildNotification(NotificationCompat.Action action) {

        final androidx.media.app.NotificationCompat.MediaStyle style = new androidx.media.app.NotificationCompat.MediaStyle();

        Intent intent = new Intent(getApplicationContext(), BackgroundAudioService.class);
        intent.setAction(ACTION_STOP);
        PendingIntent stopPendingIntent = PendingIntent.getService(getApplicationContext(), 1, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        Intent clickIntent = new Intent(this, PlayerActivity.class);
        clickIntent.setAction(Intent.ACTION_MAIN);
        clickIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        PendingIntent clickPendingIntent = PendingIntent.getActivity(this, 0, clickIntent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        builder = new NotificationCompat.Builder(this, "1");
        builder.setSmallIcon(R.drawable.logo);
        builder.setContentTitle(videoItem.getTitle());
        builder.setContentInfo(String.valueOf(videoItem.getDuration()));
        builder.setPriority(NotificationCompat.PRIORITY_HIGH);
        builder.setShowWhen(true);
        builder.setContentIntent(clickPendingIntent);
        builder.setDeleteIntent(stopPendingIntent);
        builder.setOngoing(true);
        builder.setSubText(videoItem.getAuthor());
        builder.setStyle(style);

        //load bitmap for largeScreen
        if (videoItem.getThumbnailURL() != null && !videoItem.getThumbnailURL().isEmpty()) {
            Glide.with(getApplicationContext()).asBitmap().load(videoItem.getThumbnailURL()).into(target);
        }

        builder.addAction(generateAction(android.R.drawable.ic_media_previous, "Previous", ACTION_PREVIOUS));
        /*builder.addAction(generateAction(android.R.drawable.ic_media_play, "Play", ACTION_PLAY));
        builder.addAction(generateAction(android.R.drawable.ic_media_pause, "Pause", ACTION_PAUSE));*/
        builder.addAction(action);
        builder.addAction(generateAction(android.R.drawable.ic_media_next, "Next", ACTION_NEXT));
        style.setShowActionsInCompactView(0, 1, 2);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(1, builder.build());
        NotificationChannel notificationChannel = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationChannel = new NotificationChannel("1", "MusiquitaApp", NotificationManager.IMPORTANCE_DEFAULT);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            notificationManager = getSystemService(NotificationManager.class);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationManager.createNotificationChannel(notificationChannel);
        }

    }
    private NotificationCompat.Action generateAction(int icon, String title, String intentAction) {
        Intent intent = new Intent(getApplicationContext(), BackgroundAudioService.class);
        intent.setAction(intentAction);
        PendingIntent pendingIntent = PendingIntent.getService(
                getApplicationContext(),
                1,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
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
        YTApplication.getExoPlayer().play();
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
    public class LocalBinder extends Binder {
        BackgroundAudioService getService() {
            return BackgroundAudioService.this;
        }
    }
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }
    private final IBinder mBinder = new LocalBinder();


}
