//package com.br.mreza.musicplayer;
//
//import android.annotation.SuppressLint;
//import android.app.Notification;
//import android.app.NotificationChannel;
//import android.app.NotificationManager;
//import android.app.PendingIntent;
//import android.content.BroadcastReceiver;
//import android.content.Context;
//import android.content.Intent;
//import android.content.IntentFilter;
//import android.graphics.Bitmap;
//import android.graphics.BitmapFactory;
//import android.graphics.Canvas;
//import android.graphics.Color;
//import android.graphics.PixelFormat;
//import android.graphics.Point;
//import android.graphics.drawable.GradientDrawable;
//import android.graphics.drawable.Icon;
//import android.media.AudioManager;
//import android.media.MediaPlayer;
//import android.media.audiofx.AudioEffect;
//import android.media.session.MediaSessionManager;
//import android.os.AsyncTask;
//import android.os.Binder;
//import android.os.Build;
//import android.os.Bundle;
//import android.os.CountDownTimer;
//import android.os.IBinder;
//import android.os.PowerManager;
//import android.os.ResultReceiver;
//import android.support.annotation.NonNull;
//import android.support.annotation.Nullable;
//import android.support.annotation.RequiresApi;
//import android.support.v4.app.TaskStackBuilder;
//import android.support.v4.content.LocalBroadcastManager;
//import android.support.v4.media.MediaBrowserCompat;
//import android.support.v4.media.MediaBrowserServiceCompat;
//import android.support.v4.media.session.MediaButtonReceiver;
//import android.support.v4.media.session.MediaControllerCompat;
//import android.support.v4.media.session.MediaSessionCompat;
//import android.support.v7.app.NotificationCompat;
//import android.telephony.PhoneStateListener;
//import android.telephony.TelephonyManager;
//import android.util.Log;
//import android.view.KeyEvent;
//import android.view.LayoutInflater;
//import android.view.MotionEvent;
//import android.view.View;
//import android.view.ViewGroup;
//import android.view.WindowManager;
//import android.widget.ImageView;
//import android.widget.RemoteViews;
//import android.widget.Toast;
//
//import java.io.IOException;
//import java.util.List;
//
//import static com.br.mreza.musicplayer.ListMaker.currentTrack;
//import static com.br.mreza.musicplayer.MbnController.audioID;
//import static com.br.mreza.musicplayer.MbnController.colorForText;
//import static com.br.mreza.musicplayer.MbnController.colorToBe;
//import static com.br.mreza.musicplayer.MbnController.intentID;
//import static com.br.mreza.musicplayer.MbnController.mToken;
//import static com.br.mreza.musicplayer.MbnController.playerState;
//import static com.br.mreza.musicplayer.MbnController.seekPosition;
//import static com.br.mreza.musicplayer.MbnController.serviceWorking;
//import static com.br.mreza.musicplayer.MbnController.shouldStart;
//import static com.br.mreza.musicplayer.TimerFragment.TIMER_ACTION;
//import static com.br.mreza.musicplayer.TimerFragment.TIMER_ACTION_COMMAND;
//import static com.br.mreza.musicplayer.TimerFragment.TIMER_ACTION_TIME;
//
////import android.support.v4.app.NotificationCompat;
////import android.support.v4.app.NotificationCompat;
//
//public class MediaPlayerService extends MediaBrowserServiceCompat implements MediaPlayer.OnCompletionListener,
//        MediaPlayer.OnPreparedListener, MediaPlayer.OnErrorListener, MediaPlayer.OnSeekCompleteListener,
//        MediaPlayer.OnInfoListener, MediaPlayer.OnBufferingUpdateListener,
//        AudioManager.OnAudioFocusChangeListener {
//
//
//    private MediaPlayer mediaPlayer;
//
//    private AudioManager audioManager;
//
//    //Handle incoming phone calls
//    private boolean ongoingCall = false;
//
////    private AdvancedMbnBlur mbnBlur = new AdvancedMbnBlur();
//
//    private class PlayerAsyncSentBag {
//
//        private int action;
//
//
//        private ResultReceiver resultReceiver;
//
//
//        public PlayerAsyncSentBag(int action, ResultReceiver resultReceiver) {
//            this.action = action;
//            this.resultReceiver = resultReceiver;
//        }
//
//        public int getAction() {
//            return action;
//        }
//
//        public ResultReceiver getResultReceiver() {
//            return resultReceiver;
//        }
//    }
//
//    @SuppressLint("StaticFieldLeak")
//    private class PlayerAsyncManager extends AsyncTask<PlayerAsyncSentBag, Void, Boolean> {
//
//        private int millis = 5000;
//
//        @Override
//        protected Boolean doInBackground(PlayerAsyncSentBag... bags) {
//
//            switch (bags[0].getAction()) {
//                case ASYNC_PLAY:
//                    playerInitiate();
////                    updateNotification();
//                    startNotification();
//                    break;
//                case ASYNC_PAUSE:
//                    if (mediaPlayer != null && mediaPlayer.isPlaying()) {
//                        seekPosition = getSeekPosition();
//                        Intent intent2 = new Intent(AudioEffect.ACTION_CLOSE_AUDIO_EFFECT_CONTROL_SESSION);
//                        intent2.putExtra(AudioEffect.EXTRA_PACKAGE_NAME, getPackageName());
//                        intent2.putExtra(AudioEffect.EXTRA_AUDIO_SESSION, mediaPlayer.getAudioSessionId());
//                        sendBroadcast(intent2);
//                        mediaPlayer.pause();
//                        mediaPlayer.release();
//                        mediaPlayer = null;
//                        playerState = false;
//                    }
////                    updateNotification();
//                    startNotification();
//                    break;
//                case ASYNC_SEEK:
//                    if (mediaPlayer != null && mediaPlayer.isPlaying()) {
////                mediaPlayer.pause();
//                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                            mediaPlayer.seekTo(seekPosition, MediaPlayer.SEEK_CLOSEST_SYNC);
//                        } else {
//                            mediaPlayer.seekTo(seekPosition);
//                        }
////                mediaPlayer.start();
//                    }
//                    break;
//                case ASYNC_FAST_FORWARD:
//                    if (mediaPlayer != null && mediaPlayer.isPlaying()) {
////                mediaPlayer.pause();
//                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                            mediaPlayer.seekTo(getSeekPosition() + millis, MediaPlayer.SEEK_CLOSEST_SYNC);
//                        } else {
//                            mediaPlayer.seekTo(getSeekPosition() + millis);
//                        }
////                mediaPlayer.start();
//                    }
//                    break;
//                case ASYNC_REWIND:
//                    if (mediaPlayer != null && mediaPlayer.isPlaying()) {
////                mediaPlayer.pause();
//                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                            mediaPlayer.seekTo(getSeekPosition() - millis, MediaPlayer.SEEK_CLOSEST_SYNC);
//                        } else {
//                            mediaPlayer.seekTo(getSeekPosition() - millis);
//                        }
////                mediaPlayer.start();
//                    }
//                    break;
//                case ASYNC_NEXT:
//                    if ((mediaPlayer != null && mediaPlayer.isPlaying()) || shouldStart) {
//                        playerInitiate();
//                        shouldStart = false;
//                    }
////                    updateNotification();
//                    startNotification();
//                    break;
//                case ASYNC_PREVIOUS:
//                    if (mediaPlayer != null && mediaPlayer.isPlaying()) {
//                        playerInitiate();
//                    }
////                    updateNotification();
//                    startNotification();
//                    break;
//                case ASYNC_GET_SEEK:
//                    if (mediaPlayer != null && mediaPlayer.isPlaying()) {
//
//                        seekPosition = getSeekPosition();
//                        bags[0].getResultReceiver().send(1996, null);
//
//                    }
//
//
//                    break;
//                case ASYNC_STOP:
//                    if (mediaPlayer != null) {
//                        Intent intent2 = new Intent(AudioEffect.ACTION_CLOSE_AUDIO_EFFECT_CONTROL_SESSION);
//                        intent2.putExtra(AudioEffect.EXTRA_PACKAGE_NAME, getPackageName());
//                        intent2.putExtra(AudioEffect.EXTRA_AUDIO_SESSION, mediaPlayer.getAudioSessionId());
//                        sendBroadcast(intent2);
//                        mediaPlayer.reset();
//                        mediaPlayer.release();
//                        mediaPlayer = null;
//                    }
//
//                    return true;
//
//
//            }
//
//
//            return false;
//        }
//
//
//        @Override
//        protected void onPostExecute(Boolean aBoolean) {
//            super.onPostExecute(aBoolean);
//
//
//            if (aBoolean) {
//
//                stopForeground(true);
//                stopSelf();
//
//            }
//
//
//        }
//    }
//
//
//    MediaSessionCompat.Callback mSessionCallback = new MediaSessionCompat.Callback() {
//        @Override
//        public void onCommand(String command, Bundle extras, ResultReceiver cb) {
//            super.onCommand(command, extras, cb);
//
////            Bundle bundle = new Bundle();
//
//            new PlayerAsyncManager().execute(new PlayerAsyncSentBag(ASYNC_GET_SEEK, cb));
//
////            if (mediaPlayer != null && mediaPlayer.isPlaying()) {
////
////                seekPosition = getSeekPosition();
////                cb.send(1996, null);
////
////
////            }
//
////            bundle.putInt("seekPosition", seekPosition);
//
//
////            System.out.println("herrrr iam");
////            System.out.println(Thread.currentThread().getName());
//
//
//        }
//
//        @Override
//        public void onPlay() {
//            super.onPlay();
//
//            new PlayerAsyncManager().execute(new PlayerAsyncSentBag(ASYNC_PLAY, null));
//
//
////            playerInitiate();
////
////            updateNotification();
//
////            System.out.println("mmmmmmmmmmmmmmm in play transport");
//
//        }
//
//        @Override
//        public void onPause() {
//            super.onPause();
//
//
//            MediaSessionHolder.visyHelper.inAct();
//
//            new PlayerAsyncManager().execute(new PlayerAsyncSentBag(ASYNC_PAUSE, null));
//
//
////            if (mediaPlayer != null && mediaPlayer.isPlaying()) {
////                seekPosition = getSeekPosition();
////                mediaPlayer.pause();
////                mediaPlayer.release();
////                mediaPlayer = null;
////                playerState = false;
////            }
////
////            updateNotification();
//        }
//
//        @Override
//        public void onSkipToNext() {
//            super.onSkipToNext();
//
//            new PlayerAsyncManager().execute(new PlayerAsyncSentBag(ASYNC_NEXT, null));
//
//
////            if (mediaPlayer != null && mediaPlayer.isPlaying()) {
////                playerInitiate();
////            }
////            updateNotification();
//        }
//
//
//        @Override
//        public void onSkipToPrevious() {
//            super.onSkipToPrevious();
//            new PlayerAsyncManager().execute(new PlayerAsyncSentBag(ASYNC_PREVIOUS, null));
////
////            if (mediaPlayer != null && mediaPlayer.isPlaying()) {
////                playerInitiate();
////            }
////            updateNotification();
//        }
//
//        @Override
//        public void onStop() {
//            super.onStop();
//        }
//
//        @Override
//        public void onSeekTo(long pos) {
//            super.onSeekTo(pos);
//
//            new PlayerAsyncManager().execute(new PlayerAsyncSentBag(ASYNC_SEEK, null));
//
////
////            if (mediaPlayer != null && mediaPlayer.isPlaying()) {
//////                mediaPlayer.pause();
////                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
////                    mediaPlayer.seekTo(seekPosition, MediaPlayer.SEEK_CLOSEST_SYNC);
////                } else {
////                    mediaPlayer.seekTo(seekPosition);
////                }
//////                mediaPlayer.start();
////            }
//
//        }
//
//        @Override
//        public void onFastForward() {
//            super.onFastForward();
//            new PlayerAsyncManager().execute(new PlayerAsyncSentBag(ASYNC_FAST_FORWARD, null));
//        }
//
//        @Override
//        public void onRewind() {
//            super.onRewind();
//            new PlayerAsyncManager().execute(new PlayerAsyncSentBag(ASYNC_REWIND, null));
//        }
//
//        @Override
//        public void onCustomAction(String action, Bundle extras) {
//            super.onCustomAction(action, extras);
//        }
//    };
//
//    // Binder given to clients
//    private final IBinder iBinder = new LocalBinder();
//    private BroadcastReceiver noisyReceiver;
//
//    private String channel_id;
//
//    private static final int NOTIFICATION_ID = 96;
//
//    //MediaSession
//    private MediaSessionManager mediaSessionManager;
//    private MediaSessionCompat mediaSession;
//    private MediaControllerCompat.TransportControls transportControls;
//
////    private boolean state = false;
//
//
//    private static final int ASYNC_PLAY = 1;
//    private static final int ASYNC_PAUSE = 2;
//    private static final int ASYNC_SEEK = 3;
//    private static final int ASYNC_FAST_FORWARD = 8;
//    private static final int ASYNC_REWIND = 9;
//    private static final int ASYNC_STOP = 4;
//    private static final int ASYNC_NEXT = 5;
//    private static final int ASYNC_PREVIOUS = 6;
//    private static final int ASYNC_GET_SEEK = 7;
//
//    public static final String ACTION_PLAY = "com.br.mreza.musicplayer.ACTION_PLAY";
//    public static final String ACTION_PAUSE = "com.br.mreza.musicplayer.ACTION_PAUSE";
//    public static final String ACTION_PREVIOUS = "com.br.mreza.musicplayer.ACTION_PREVIOUS";
//    public static final String ACTION_NEXT = "com.br.mreza.musicplayer.ACTION_NEXT";
//    public static final String ACTION_STOP = "com.br.mreza.musicplayer.ACTION_STOP";
//
//    private View overlayView;
//    private WindowManager.LayoutParams params;
//    private WindowManager windowManager;
//    //    private Timer timer;
//
//
//    //Handle incoming phone calls
//    private void callStateListener() {
//        // Get the telephony manager
//        TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
////        Starting listening for PhoneState changes
//        PhoneStateListener phoneStateListener = new PhoneStateListener() {
//            @Override
//            public void onCallStateChanged(int state, String incomingNumber) {
//                switch (state) {
//                    //if at least one call exists or the phone is ringing
//                    //pause the MediaPlayer
//                    case TelephonyManager.CALL_STATE_OFFHOOK:
//                    case TelephonyManager.CALL_STATE_RINGING:
//                        if (mediaPlayer != null) {
//                            MbnController.pause(MediaPlayerService.this);
//                            ongoingCall = true;
//                        }
//                        break;
//                    case TelephonyManager.CALL_STATE_IDLE:
//                        // Phone idle. Start playing.
//                        if (mediaPlayer != null) {
//                            if (ongoingCall) {
//                                ongoingCall = false;
//                                MbnController.play(MediaPlayerService.this);
//                            }
//                        }
//                        break;
//                }
//            }
//        };
//        // Register the listener with the telephony manager
//        // Listen for changes to the device call state.
//        telephonyManager.listen(phoneStateListener,
//                PhoneStateListener.LISTEN_CALL_STATE);
//    }
//
//    @Override
//    public IBinder onBind(Intent intent) {
//        return iBinder;
//    }
//
//    @Nullable
//    @Override
//    public BrowserRoot onGetRoot(@NonNull String clientPackageName, int clientUid, @Nullable Bundle rootHints) {
//        return null;
//    }
//
//    @Override
//    public void onLoadChildren(@NonNull String parentId, @NonNull Result<List<MediaBrowserCompat.MediaItem>> result) {
//
//    }
//
//    @Override
//    public void onBufferingUpdate(MediaPlayer mp, int percent) {
//        //Invoked indicating buffering status of
//        //a media resource being streamed over the network.
//    }
//
//    @Override
//    public void onCompletion(MediaPlayer mp) {
//        //Invoked when playback of a media source has completed.
//        MbnController.onComplete(this);
//    }
//
//    //Handle errors
//    @Override
//    public boolean onError(MediaPlayer mp, int what, int extra) {
//        //Invoked when there has been an error during an asynchronous operation.
//        return false;
//    }
//
//    @Override
//    public boolean onInfo(MediaPlayer mp, int what, int extra) {
//        //Invoked to communicate some info.
//        return false;
//    }
//
//    @Override
//    public void onPrepared(MediaPlayer mp) {
//        //Invoked when the media source is ready for playback.
//
////        Equalizer equalizer = new Equalizer(0, mp.getAudioSessionId());
////
////        equalizer.setEnabled(true);
//
//
//        Intent intent = new Intent(AudioEffect.ACTION_OPEN_AUDIO_EFFECT_CONTROL_SESSION);
//        intent.putExtra(AudioEffect.EXTRA_PACKAGE_NAME, getPackageName());
//        intent.putExtra(AudioEffect.EXTRA_AUDIO_SESSION, mp.getAudioSessionId());
//        audioID = mp.getAudioSessionId();
////            intent.putExtra(AudioEffect.EXTRA_AUDIO_SESSION, mediaPlayer.getAudioSessionId());
//
////        sendBroadcast(intent);
//
//
//        MediaSessionHolder.visyHelper.reset(mp.getAudioSessionId());
//
////        if (mp.getDuration() == currentTrack.getDuration()) {
//        mp.seekTo(seekPosition);
//        mp.start();
////        }
////        mediaPlayer.seekTo(seekPosition);
//    }
//
//    @Override
//    public void onSeekComplete(MediaPlayer mp) {
//        //Invoked indicating the completion of a seek operation.
////        mediaPlayer.start();
//    }
//
//
//    @Override
//    public void onAudioFocusChange(int focusState) {
//        //Invoked when the audio focus of the system is updated.
//        switch (focusState) {
//            case AudioManager.AUDIOFOCUS_GAIN:
//                // resume playback
//                MbnController.play(this);
//
//                break;
//            case AudioManager.AUDIOFOCUS_LOSS:
//                // Lost focus for an unbounded amount of time: stop playback and release media player
//                MbnController.pause(this);
//
//                break;
//            case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT:
//                // Lost focus for a short time, but we have to stop
//                // playback. We don't release the media player because playback
//                // is likely to resume
//
//                break;
//            case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK:
//                // Lost focus for a short time, but it's ok to keep playing
//                // at an attenuated level
//                break;
//        }
//    }
//
//
//    @Override
//    public int onStartCommand(Intent intent, int flags, int startId) {
//
//
////        Log.i("don", intent.getAction());
////        MediaButtonReceiver.handleIntent(MediaSessionHolder.getMediaSession(getApplicationContext()), intent);
//
//        if (!requestAudioFocus()) {
//
////            Check for stop ForeGround Notification.
//
//            stopForeground(true);
//
//        }
//
//        if (noisyReceiver == null) {
//
//            noisyReceiver = new BroadcastReceiver() {
//                @Override
//                public void onReceive(Context context, Intent intent) {
//
//                    MbnController.pause(MediaPlayerService.this);
//
//                }
//            };
//
//            IntentFilter filter = new IntentFilter(AudioManager.ACTION_AUDIO_BECOMING_NOISY);
//
//            registerReceiver(noisyReceiver, filter);
//
//        }
//
//        try {
//            callStateListener();
//        } catch (Exception e) {
////            e.printStackTrace();
//        }
//
//
//        String intentAction = intent.getAction();
//
//        System.out.println(intentAction);
//
//
//        if (intentAction != null) {
//            switch (intentAction) {
//
//                case intentID:
//                    handlingNonNotificationIntents(intent);
//                    break;
//                default:
//                    handlingNotificationCallBacks(intent);
//                    break;
//
//            }
//        }
////
////        if (intent.getAction().equals(intentID)) {
////            handlingNonNotificationIntents(intent);
////        } else {
////            handlingNotificationCallBacks(intent);
////        }
//
//
//        return super.onStartCommand(intent, flags, startId);
//    }
//
//    private boolean requestAudioFocus() {
//        audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
//        assert audioManager != null;
//        int result = audioManager.requestAudioFocus(this, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN);
//        if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
//            //Focus gained
//            return true;
//        }
//        //Could not gain focus
//        return false;
//    }
//
//    private boolean removeAudioFocus() {
//        return AudioManager.AUDIOFOCUS_REQUEST_GRANTED ==
//                audioManager.abandonAudioFocus(this);
//    }
//
//    private void handlingNotificationCallBacks(Intent intent) {
//
//
////        System.out.println("---------------------------heretoo");
//        String action = intent.getAction();
//
//        if (action == null) {
//            return;
//        }
//
//        switch (action) {
//
//            case ACTION_PREVIOUS:
//                MbnController.previous(this);
////                playerInitiate();
////                updateNotification();
//                break;
//            case ACTION_NEXT:
//                MbnController.next(this);
////                playerInitiate();
////                updateNotification();
//                break;
//            case ACTION_PLAY:
//                if (playerState) {
//                    MbnController.pause(this);
////                    mediaPlayer.pause();
//                } else {
//                    MbnController.play(this);
////                    playerInitiate();
//                }
////                updateNotification();
//                break;
//            case ACTION_STOP:
//                MbnController.stop(this);
//
//                MediaSessionHolder.visyHelper.inAct();
//
//                new PlayerAsyncManager().execute(new PlayerAsyncSentBag(ASYNC_STOP, null));
//
//
//                break;
//
//
//        }
//
//    }
//
//    private int playButton() {
//        int playIcon;
//
//        if (playerState) {
//
//            playIcon = R.drawable.ic_music_player_pause_lines;
//        } else {
//
//            playIcon = R.drawable.ic_music_player_play;
//        }
//
//        return playIcon;
//    }
//
//    private int smallIcon() {
//
//        int smallIcon;
//
//        if (playerState) {
//            smallIcon = R.drawable.ic_play_button;
//
//        } else {
//            smallIcon = R.drawable.ic_pause;
//
//        }
//        return smallIcon;
//    }
//
//    private void updateNotification(Bitmap blurry, Bitmap image) {
//
//
//        Intent intent = new Intent(getApplicationContext(), FirstActivity.class);
//        TaskStackBuilder stackBuilder = TaskStackBuilder.create(getApplicationContext());
//        stackBuilder.addParentStack(FirstActivity.class);
//        stackBuilder.addNextIntent(intent);
//        PendingIntent pendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
//
//
//        Intent intentPre = new Intent(getApplicationContext(), MediaPlayerService.class);
//        intentPre.setAction(ACTION_PREVIOUS);
//        PendingIntent prePend = PendingIntent.getService(getApplicationContext(), 0, intentPre, 0);
//
//        Intent intentPlay = new Intent(getApplicationContext(), MediaPlayerService.class);
//        intentPlay.setAction(ACTION_PLAY);
//        PendingIntent playPend = PendingIntent.getService(getApplicationContext(), 0, intentPlay, 0);
//
//        Intent intentNext = new Intent(getApplicationContext(), MediaPlayerService.class);
//        intentNext.setAction(ACTION_NEXT);
//        PendingIntent nextPend = PendingIntent.getService(getApplicationContext(), 0, intentNext, 0);
//
//        Intent intentStop = new Intent(getApplicationContext(), MediaPlayerService.class);
//        intentStop.setAction(ACTION_STOP);
//        PendingIntent stopPend = PendingIntent.getService(getApplicationContext(), 0, intentStop, 0);
//
//
//        RemoteViews content = new RemoteViews(getPackageName(), R.layout.notification_layout);
//        content.setImageViewBitmap(R.id.notification_cover_image, image);
//        content.setTextViewText(R.id.notification_titile, currentTrack.getTitle());
//        content.setTextViewText(R.id.notification_album, currentTrack.getAlbumTitle());
//        content.setTextViewText(R.id.notification_artist, currentTrack.getArtistTitle());
//        content.setTextColor(R.id.notification_titile, colorForText);
//        content.setOnClickPendingIntent(R.id.notification_pre, prePend);
//        content.setOnClickPendingIntent(R.id.notification_play, playPend);
//        content.setOnClickPendingIntent(R.id.notification_next, nextPend);
//        content.setOnClickPendingIntent(R.id.notification_stop, stopPend);
//        content.setImageViewResource(R.id.notification_stop, R.drawable.ic_close_black_24dp);
//        content.setImageViewResource(R.id.notification_pre, R.drawable.ic_rewind_arrow);
//        content.setImageViewResource(R.id.notification_play, playButton());
//        content.setImageViewResource(R.id.notification_next, R.drawable.ic_fast_forward_arrow);
//        content.setImageViewBitmap(R.id.notification_background, blurry);
////        content.setImageViewBitmap(R.id.notification_background, gradient());
//
//
//        RemoteViews contentCompact = new RemoteViews(getPackageName(), R.layout.notification_layout_compact);
//        contentCompact.setImageViewBitmap(R.id.notification_cover_image, image);
//        contentCompact.setTextViewText(R.id.notification_titile, currentTrack.getTitle());
//        contentCompact.setTextColor(R.id.notification_titile, colorForText);
//        contentCompact.setOnClickPendingIntent(R.id.notification_pre, prePend);
//        contentCompact.setOnClickPendingIntent(R.id.notification_play, playPend);
//        contentCompact.setOnClickPendingIntent(R.id.notification_next, nextPend);
//        contentCompact.setOnClickPendingIntent(R.id.notification_stop, stopPend);
//        contentCompact.setImageViewResource(R.id.notification_stop, R.drawable.ic_close_black_24dp);
//        contentCompact.setImageViewResource(R.id.notification_pre, R.drawable.ic_rewind_arrow);
//        contentCompact.setImageViewResource(R.id.notification_play, playButton());
//        contentCompact.setImageViewResource(R.id.notification_next, R.drawable.ic_fast_forward_arrow);
//        contentCompact.setImageViewBitmap(R.id.notification_background, blurry);
////        contentCompact.setImageViewBitmap(R.id.notification_background, gradient());
//
//
////        Icon nextIcon = Icon.createWithResource(getApplicationContext(), R.drawable.ic_skip);
////        Notification.Action action = new Notification.Action.Builder(nextIcon, "Next", nextPend).build();
//
//        Notification notification = null;
//        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
//            notification = new Notification.Builder(getApplicationContext(), channel_id)
////                    .setContentTitle(currentTrack.getAlbumTitle())
////                    .setContentText(currentTrack.getTitle()).setTicker("MBN").setContentIntent(pendingIntent)
//                    .setSmallIcon(smallIcon())
//                    .setCustomBigContentView(content)
//                    .setCustomContentView(contentCompact)
//
////                    .addAction(actionMaker(R.drawable.ic_previous, "Pre", prePend))
////                    .addAction(actionMaker(playIcon, "Play", playPend))
////                    .addAction(actionMaker(R.drawable.ic_skip, "Next", nextPend))
////                    .addAction(actionMaker(R.drawable.ic_cancel, "Stop", stopPend))
////                    .setLargeIcon(currentTrack.getCover()).setColor(colorToBe)
////                    .setColorized(true)
//                    .build();
//        } else {
//
//
//            notification = new NotificationCompat.Builder(getApplicationContext()).setContentText(currentTrack.getTitle()).setTicker("MBN").setContentIntent(pendingIntent)
////                    .setSmallIcon(smallIcon()).setContentTitle(currentTrack.getAlbumTitle()).setStyle(new NotificationCompat.MediaStyle().setShowActionsInCompactView(0, 1, 2, 3))
////                    .addAction(R.drawable.ic_previous, "Pre", prePend)
////                    .addAction(playButton(), "Play", playPend)
////                    .addAction(R.drawable.ic_skip, "Next", nextPend)
////                    .addAction(R.drawable.ic_cancel, "Stop", stopPend)
////                    .setLargeIcon(currentTrack.getCover()).setColor(colorToBe)
//
//                    .setSmallIcon(smallIcon())
//                    .setCustomBigContentView(content)
//                    .setCustomContentView(contentCompact)
//
//                    .build();
//        }
//
//
//        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
//
//        assert manager != null;
//        manager.notify(NOTIFICATION_ID, notification);
//
//
////
//
////        Intent intent = new Intent(this, FirstActivity.class);
////        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
////        stackBuilder.addParentStack(FirstActivity.class);
////        stackBuilder.addNextIntent(intent);
////        PendingIntent pendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
////
////
////        Intent intentPre = new Intent(this, this.getClass());
////        intentPre.setAction(ACTION_PREVIOUS);
////        PendingIntent prePend = PendingIntent.getService(this, 0, intentPre, 0);
////
////        Intent intentPlay = new Intent(this, MediaPlayerService.class);
////        intentPlay.setAction(ACTION_PLAY);
////        PendingIntent playPend = PendingIntent.getService(this, 0, intentPlay, 0);
////
////        Intent intentNext = new Intent(this, MediaPlayerService.class);
////        intentNext.setAction(ACTION_NEXT);
////        PendingIntent nextPend = PendingIntent.getService(this, 0, intentNext, 0);
////
////        Intent intentStop = new Intent(this, this.getClass());
////        intentStop.setAction(ACTION_STOP);
////        PendingIntent stopPend = PendingIntent.getService(this, 0, intentStop, 0);
////
////
////        RemoteViews content = new RemoteViews(getPackageName(), R.layout.notification_layout);
////        content.setImageViewBitmap(R.id.notification_cover_image, MbnController.getCover(getApplicationContext(), currentTrack.getPath(), 1));
////        content.setTextViewText(R.id.notification_titile, currentTrack.getTitle());
////        content.setTextViewText(R.id.notification_album, currentTrack.getAlbumTitle());
////        content.setTextViewText(R.id.notification_artist, currentTrack.getArtistTitle());
////        content.setTextColor(R.id.notification_titile, colorForText);
////        content.setOnClickPendingIntent(R.id.notification_pre, prePend);
////        content.setOnClickPendingIntent(R.id.notification_play, playPend);
////        content.setOnClickPendingIntent(R.id.notification_next, nextPend);
////        content.setOnClickPendingIntent(R.id.notification_stop, stopPend);
////        content.setImageViewResource(R.id.notification_stop, R.drawable.ic_cancel);
////        content.setImageViewResource(R.id.notification_pre, R.drawable.ic_previous);
////        content.setImageViewResource(R.id.notification_play, playButton());
////        content.setImageViewResource(R.id.notification_next, R.drawable.ic_skip);
////        content.setImageViewBitmap(R.id.notification_background, gradient());
////
////
////        RemoteViews contentCompact = new RemoteViews(getPackageName(), R.layout.notification_layout_compact);
////        contentCompact.setImageViewBitmap(R.id.notification_cover_image, MbnController.getCover(getApplicationContext(), currentTrack.getPath(), 2));
////        contentCompact.setTextViewText(R.id.notification_titile, currentTrack.getTitle());
////        contentCompact.setTextColor(R.id.notification_titile, colorForText);
////        contentCompact.setOnClickPendingIntent(R.id.notification_pre, prePend);
////        contentCompact.setOnClickPendingIntent(R.id.notification_play, playPend);
////        contentCompact.setOnClickPendingIntent(R.id.notification_next, nextPend);
////        contentCompact.setOnClickPendingIntent(R.id.notification_stop, stopPend);
////        contentCompact.setImageViewResource(R.id.notification_stop, R.drawable.ic_cancel);
////        contentCompact.setImageViewResource(R.id.notification_pre, R.drawable.ic_previous);
////        contentCompact.setImageViewResource(R.id.notification_play, playButton());
////        contentCompact.setImageViewResource(R.id.notification_next, R.drawable.ic_skip);
////        contentCompact.setImageViewBitmap(R.id.notification_background, gradient());
////
////
//////        Icon nextIcon = Icon.createWithResource(getApplicationContext(), R.drawable.ic_skip);
//////        Notification.Action action = new Notification.Action.Builder(nextIcon, "Next", nextPend).build();
////
////        Notification notification = null;
////        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
////            notification = new Notification.Builder(this, channel_id)
//////                    .setContentTitle(currentTrack.getAlbumTitle())
//////                    .setContentText(currentTrack.getTitle()).setTicker("MBN").setContentIntent(pendingIntent)
////                    .setSmallIcon(smallIcon())
////                    .setCustomBigContentView(content)
////                    .setCustomContentView(contentCompact)
////
//////                    .addAction(actionMaker(R.drawable.ic_previous, "Pre", prePend))
//////                    .addAction(actionMaker(playIcon, "Play", playPend))
//////                    .addAction(actionMaker(R.drawable.ic_skip, "Next", nextPend))
//////                    .addAction(actionMaker(R.drawable.ic_cancel, "Stop", stopPend))
//////                    .setLargeIcon(currentTrack.getCover()).setColor(colorToBe)
//////                    .setColorized(true)
////                    .build();
////        } else {
////
////            notification = new NotificationCompat.Builder(this).setContentText(currentTrack.getTitle()).setTicker("MBN").setContentIntent(pendingIntent)
//////                    .setSmallIcon(smallIcon()).setContentTitle(currentTrack.getAlbumTitle()).setStyle(new NotificationCompat.MediaStyle().setShowActionsInCompactView(0, 1, 2, 3))
//////                    .addAction(R.drawable.ic_previous, "Pre", prePend)
//////                    .addAction(playButton(), "Play", playPend)
//////                    .addAction(R.drawable.ic_skip, "Next", nextPend)
//////                    .addAction(R.drawable.ic_cancel, "Stop", stopPend)
//////                    .setLargeIcon(currentTrack.getCover()).setColor(colorToBe)
////
////                    .setSmallIcon(smallIcon())
////                    .setCustomBigContentView(content)
////                    .setCustomContentView(contentCompact)
////
////                    .build();
////        }
////
////
////        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
////
////        manager.notify(NOTIFICATION_ID, notification);
//
////        startForeground(NOTIFICATION_ID, notification);
//
//
//    }
//
//    private Bitmap gradient() {
//
//        Bitmap bitmap = Bitmap.createBitmap(150, 150, Bitmap.Config.ARGB_8888);
//        Canvas canvas = new Canvas(bitmap);
//        int[] col = new int[]{colorToBe, Color.WHITE};
//        GradientDrawable gradientDrawable = new GradientDrawable(GradientDrawable.Orientation.RIGHT_LEFT, col);
////        gradientDrawable.setGradientCenter(bitmap.getWidth()/2,bitmap.getHeight()/2);
////        gradientDrawable.setGradientType(GradientDrawable.LINEAR_GRADIENT);
////        gradientDrawable.setCornerRadius(0f);
////        gradientDrawable.setChangingConfigurations(ActivityInfo.CONFIG_ORIENTATION);
////        gradientDrawable.setShape(GradientDrawable.RECTANGLE);
////        gradientDrawable.setSize(bitmap.getWidth(), bitmap.getHeight());
//        gradientDrawable.setBounds(0, 0, bitmap.getWidth(), bitmap.getHeight());
//        gradientDrawable.draw(canvas);
////        canvas.drawColor(colorToBe);
//        return bitmap;
//
//    }
//
//
//    @RequiresApi(api = Build.VERSION_CODES.M)
//    private Notification.Action actionMaker(int icon, String s, PendingIntent intent) {
//
//
//        Icon realIcon = Icon.createWithResource(getApplicationContext(), icon);
//
//
//        return new Notification.Action.Builder(realIcon, s, intent).build();
//    }
//
//    @Override
//    public void onDestroy() {
//        super.onDestroy();
//        removeAudioFocus();
//        unregisterReceiver(noisyReceiver);
//        unReg();
//        LocalBroadcastManager.getInstance(getApplicationContext()).unregisterReceiver(sleepTimerReceiver);
//        stopTimer();
////        timer.cancel();
////        unregisterReceiver(actionReceiver);
//        stopForeground(true);
//
//        removeOverLay();
//
//        working = false;
//
//        serviceWorking = false;
//
//
//        MediaSessionHolder.getMediaSession(getApplicationContext()).setCallback(null);
//
//    }
//
//    private void startNotification() {
//        new AlbumArtLoaderForChangeHandler(getApplicationContext(), currentTrack.getPath(), 0) {
//            @Override
//            public void onFinish(Bitmap main, Bitmap blur) {
//                updateNotification(blur, main);
//            }
//        };
//    }
//
////    AdvancedMbnBlur.MbnAdvancedBlurHelper helper = new AdvancedMbnBlur.MbnAdvancedBlurHelper() {
////        @Override
////        public void onFinish(Bitmap blurred) {
////
////            updateNotification(blurred);
////
////        }
////    };
//
//    @Override
//    public void onCreate() {
//
////        mbnBlur.setHelper(helper);
//
//        super.onCreate();
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            makeNotifyChannel();
//        }
//        try {
//            makeNotification();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
////        timer();
//        registerReceiverFunc();
//
//        sleepTimerReceiverRegister();
//
//        try {
////            makeOverlay();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//
//        setMediaSession();
//
//        serviceWorking = true;
//
//    }
//
//
//    private void setMediaSession() {
//
////        mediaSession = new MediaSessionCompat(getApplicationContext(), "media_session");
////        MediaSessionHolder.reset(getApplicationContext());
//        MediaSessionHolder.getMediaSession(getApplicationContext()).setCallback(mSessionCallback);
//
//
//        Intent mediaButt = new Intent(getApplicationContext(), MediaPlayerService.class);
////        mediaButt.setAction(ACTION_PLAY);
//        PendingIntent pendingIntent = PendingIntent.getService(getApplicationContext(), 0, mediaButt, 0);
//
////        MediaSessionHolder.getMediaSession(getApplicationContext()).setMediaButtonReceiver(pendingIntent);
//
//
//        mToken = MediaSessionHolder.getMediaSession(getApplicationContext()).getSessionToken();
//
//        setSessionToken(mToken);
//
//
//    }
//
//    @SuppressLint("ClickableViewAccessibility")
//    private void makeOverlay() {
//
//        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
//
//        int type;
//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
//        } else {
//
//            type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
//        }
//
//        params = new WindowManager.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
//                ViewGroup.LayoutParams.WRAP_CONTENT,
//                type,
//                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL | WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH,
//                PixelFormat.TRANSLUCENT);
//
////        params.gravity = Gravity.START | Gravity.CENTER;
//        params.setTitle("Load Average");
//
//        LayoutInflater inflater = (LayoutInflater) getBaseContext().getSystemService(LAYOUT_INFLATER_SERVICE);
//
//        overlayView = inflater.inflate(R.layout.overlay_layout, null);
//
//        ImageView moveBar = overlayView.findViewById(R.id.overlay_move_bar);
//        moveBar.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_action_move_remote_layout));
//        moveBar.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View view, MotionEvent motionEvent) {
//                if (motionEvent.getAction() == MotionEvent.ACTION_MOVE) {
//                    Point point = new Point();
//                    windowManager.getDefaultDisplay().getSize(point);
//                    params.x = (int) motionEvent.getRawX() - point.x / 2;
//                    params.y = ((int) motionEvent.getRawY() - point.y / 2) + ((overlayView.getHeight() / 2) - view.getHeight() / 2);
//                    windowManager.updateViewLayout(overlayView, params);
//                }
//                return true;
//            }
//        });
//
//        ImageView next = overlayView.findViewById(R.id.overlay_next);
//        next.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.skip_png));
//        ImageView previous = overlayView.findViewById(R.id.overlay_previous);
//        previous.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.previous_png));
//        final ImageView playPause = overlayView.findViewById(R.id.overlay_play_pause);
//        playPause.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.play_pause_png));
//
//        View.OnClickListener clickListener = new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                int id = view.getId();
//
//                switch (id) {
//
//                    case R.id.overlay_next:
//                        MbnController.next(getApplicationContext());
//                        break;
//                    case R.id.overlay_previous:
//                        MbnController.previous(getApplicationContext());
//                        break;
//                    case R.id.overlay_play_pause:
//
//                        if (playerState) {
//                            MbnController.pause(getApplicationContext());
//                        } else {
//                            MbnController.play(getApplicationContext());
//                        }
//
//                        break;
//
//
//                }
//
//
//            }
//        };
//
//        next.setOnClickListener(clickListener);
//        playPause.setOnClickListener(clickListener);
//        previous.setOnClickListener(clickListener);
//
//        windowManager.addView(overlayView, params);
//
//
//    }
//
//    private void removeOverLay() {
//
//        try {
//            windowManager.removeView(overlayView);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//
//    }
//
//    private String songCodeForPlayer = "";
//
//    private boolean isSame() {
//
//        if (songCodeForPlayer.equals(currentTrack.getPath())) {
//
//            return true;
//        }
//
//        songCodeForPlayer = "" + currentTrack.getPath();
//
//
//        return false;
//    }
//
//    private void forSeek() {
//
//        try {
//            mediaPlayer.seekTo(seekPosition);
//            if (!mediaPlayer.isPlaying()) {
//                mediaPlayer.start();
//            }
//            playerState = true;
//        } catch (Exception e) {
//            e.printStackTrace();
//
////            System.out.println("mmmmmmm seek");
//            mediaPlayer = null;
//            mediaPlayer = new MediaPlayer();
//            mediaPlayer.setOnPreparedListener(this);
//            mediaPlayer.setOnCompletionListener(this);
//            mediaPlayer.setOnSeekCompleteListener(this);
//            mediaPlayer.setWakeMode(this, PowerManager.PARTIAL_WAKE_LOCK);
//            try {
//                mediaPlayer.setDataSource(currentTrack.getPath());
//                mediaPlayer.prepare();
//
////                System.out.println("mmmmmm " + seekPosition);
////                mediaPlayer.seekTo(seekPosition);
////                mediaPlayer.start();
//                playerState = true;
//            } catch (Exception i) {
//            }
//
//        }
//    }
//
//
//    private void playerInitiate() {
//
//
//        if (isSame()) {
//            forSeek();
//        } else {
//            if (mediaPlayer != null) {
//                Intent intent = new Intent(AudioEffect.ACTION_CLOSE_AUDIO_EFFECT_CONTROL_SESSION);
//                intent.putExtra(AudioEffect.EXTRA_PACKAGE_NAME, getPackageName());
//                intent.putExtra(AudioEffect.EXTRA_AUDIO_SESSION, mediaPlayer.getAudioSessionId());
//                sendBroadcast(intent);
//                mediaPlayer.release();
//            }
//            mediaPlayer = null;
//            mediaPlayer = new MediaPlayer();
//            mediaPlayer.setOnPreparedListener(MediaPlayerService.this);
//            mediaPlayer.setOnCompletionListener(MediaPlayerService.this);
//            mediaPlayer.setOnSeekCompleteListener(MediaPlayerService.this);
//            mediaPlayer.setWakeMode(MediaPlayerService.this, PowerManager.PARTIAL_WAKE_LOCK);
//            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
//            try {
//                mediaPlayer.setAudioSessionId(audioManager.generateAudioSessionId());
//                mediaPlayer.setDataSource(currentTrack.getPath());
////            Equalizer equalizer = new Equalizer(0, mediaPlayer.getAudioSessionId());
////
////            System.out.println(equalizer.getCurrentPreset());
//
//
////            equalizer.setEnabled(true);
//
////            mediaPlayer.ses
//
////            Intent intent = new Intent(AudioEffect.ACTION_OPEN_AUDIO_EFFECT_CONTROL_SESSION);
////            intent.putExtra(AudioEffect.EXTRA_PACKAGE_NAME, getPackageName());
////            intent.putExtra(AudioEffect.EXTRA_AUDIO_SESSION, mediaPlayer.getAudioSessionId());
//////            intent.putExtra(AudioEffect.EXTRA_AUDIO_SESSION, mediaPlayer.getAudioSessionId());
////
////            sendBroadcast(intent);
////                System.out.println("yes it is");
////                if (playerState) {
//                playerStart();
////                }
////            if (isBound) {
////                listener.onTrackChange();
////            }
//            } catch (IOException e) {
//                e.printStackTrace();
//                Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show();
//            }
//        }
////        System.out.println("hhhhhhhhh"
//    }
//
//    private void playerStart() {
//        try {
//            seekPosition = 0;
//            mediaPlayer.prepare();
//            playerState = true;
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    synchronized private int getSeekPosition() {
//
//
//        return mediaPlayer.getCurrentPosition();
//    }
//
//    private void handlingNonNotificationIntents(Intent intent) {
//
//        String action = intent.getStringExtra("MBN");
//
//
//        switch (action) {
//
//            case MbnController.ACTION_PAUSE:
//                seekPosition = getSeekPosition();
//
//                mediaPlayer.pause();
////                updateNotification();
////                stopForeground(false);
//                break;
//            case MbnController.ACTION_PLAY:
//
//
//                new PlayerAsyncManager().execute(new PlayerAsyncSentBag(ASYNC_PLAY, null));
////                playerInitiate();
////                if (isForeground) {
////                    makeNotification();
////                } else {
////                updateNotification();
////                }
//                break;
//
//            case MbnController.ACTION_STOP:
//
//                Intent intent2 = new Intent(AudioEffect.ACTION_CLOSE_AUDIO_EFFECT_CONTROL_SESSION);
//                intent2.putExtra(AudioEffect.EXTRA_PACKAGE_NAME, getPackageName());
//                intent2.putExtra(AudioEffect.EXTRA_AUDIO_SESSION, mediaPlayer.getAudioSessionId());
//                sendBroadcast(intent2);
//                mediaPlayer.reset();
//                mediaPlayer.release();
//                mediaPlayer = null;
////                updateNotification();
////                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
////                    stopForeground(STOP_FOREGROUND_REMOVE);
////                } else {
////
////                    stopForeground(true);
////
////                }
//                stopSelf();
//
//                break;
//
//        }
//    }
//
////    private boolean isForeground = false;
//
//    BroadcastReceiver actionReceiver = new BroadcastReceiver() {
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            handlingNonNotificationIntents(intent);
//        }
//    };
//
//    private void unReg() {
//
//        LocalBroadcastManager.getInstance(getApplicationContext()).unregisterReceiver(actionReceiver);
////        unregisterReceiver(headsetButtonListener);
//    }
//
//    private void registerReceiverFunc() {
//
//        IntentFilter filter = new IntentFilter(intentID);
//
//        LocalBroadcastManager.getInstance(getApplicationContext()).registerReceiver(actionReceiver, filter);
////        registerReceiver(headsetButtonListener, new IntentFilter(Intent.ACTION_MEDIA_BUTTON));
////        registerReceiver(new MediaButtonReciever(), new IntentFilter(Intent.ACTION_MEDIA_BUTTON));
//
//    }
//
//
//    class LocalBinder extends Binder {
//        public MediaPlayerService getService() {
//            return MediaPlayerService.this;
//        }
//
//        public int getDuration() {
//
//            return getDurationSync();
//        }
//    }
//
//    synchronized private int getDurationSync() {
//
//        return mediaPlayer.getDuration();
//    }
//
//
//    @RequiresApi(api = Build.VERSION_CODES.O)
//    private String makeNotifyChannel() {
//
//        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
//
//        channel_id = getPackageName();
//        CharSequence channelName = "MediaControl";
//        int importance = NotificationManager.IMPORTANCE_LOW;
//
//
//        NotificationChannel channel = new NotificationChannel(channel_id, channelName, importance);
//        manager.createNotificationChannel(channel);
//
//        return channel_id;
//    }
//
//    public void makeNotification() {
//
////
////        Intent intent = new Intent(this, AlbumActivity.class);
////        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
////        stackBuilder.addParentStack(AlbumActivity.class);
////        stackBuilder.addNextIntent(intent);
////        PendingIntent pendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
////
////
////        Intent intentPre = new Intent(this, this.getClass());
////        intent.setAction(ACTION_PREVIOUS);
////        PendingIntent prePend = PendingIntent.getService(this, 0, intentPre, 0);
////
////        Intent intentPlay = new Intent(this, MediaPlayerService.class);
////        intent.setAction(ACTION_PLAY);
////        PendingIntent playPend = PendingIntent.getService(this, 0, intentPlay, 0);
////
////        Intent intentNext = new Intent(this, MediaPlayerService.class);
////        intent.setAction(ACTION_NEXT);
////        PendingIntent nextPend = PendingIntent.getService(this, 0, intentNext, 0);
////
////        Intent intentStop = new Intent(this, this.getClass());
////        intent.setAction(ACTION_STOP);
////        PendingIntent stopPend = PendingIntent.getService(this, 0, intentStop, 0);
////
////
////        RemoteViews content = new RemoteViews(getPackageName(), R.layout.notification_layout);
////        content.setImageViewBitmap(R.id.notification_cover_image, currentTrack.getCover(this));
////        content.setTextViewText(R.id.notification_titile, currentTrack.getTitle());
////        content.setTextViewText(R.id.notification_album, currentTrack.getAlbumTitle());
////        content.setTextViewText(R.id.notification_artist, currentTrack.getArtistTitle());
////        content.setTextColor(R.id.notification_titile, colorToBe);
////        content.setOnClickPendingIntent(R.id.notification_pre, prePend);
////        content.setOnClickPendingIntent(R.id.notification_play, playPend);
////        content.setOnClickPendingIntent(R.id.notification_next, nextPend);
////        content.setImageViewResource(R.id.notification_pre, R.drawable.ic_previous);
////        content.setImageViewResource(R.id.notification_play, playButton());
////        content.setImageViewResource(R.id.notification_next, R.drawable.ic_skip);
////        content.setOnClickPendingIntent(R.id.notification_stop, stopPend);
////        content.setImageViewResource(R.id.notification_stop, R.drawable.ic_cancel);
////
////
////        RemoteViews contentCompat = new RemoteViews(getPackageName(), R.layout.notification_layout_compact);
////        contentCompat.setImageViewBitmap(R.id.notification_cover_image, currentTrack.getCover(this));
////        contentCompat.setTextViewText(R.id.notification_titile, currentTrack.getTitle());
////        contentCompat.setTextColor(R.id.notification_titile, colorToBe);
////        contentCompat.setOnClickPendingIntent(R.id.notification_pre, prePend);
////        contentCompat.setOnClickPendingIntent(R.id.notification_play, playPend);
////        contentCompat.setOnClickPendingIntent(R.id.notification_next, nextPend);
////        contentCompat.setImageViewResource(R.id.notification_pre, R.drawable.ic_previous);
////        contentCompat.setImageViewResource(R.id.notification_play, playButton());
////        contentCompat.setImageViewResource(R.id.notification_next, R.drawable.ic_skip);
////        contentCompat.setOnClickPendingIntent(R.id.notification_stop, stopPend);
////        contentCompat.setImageViewResource(R.id.notification_stop, R.drawable.ic_cancel);
////
////
////        Notification notification = null;
////        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
////            notification = new Notification.Builder(this, channel_id).setContentTitle(currentTrack.getAlbumTitle())
//////                    .setContentText(currentTrack.getTitle()).setTicker("MBN").setContentIntent(pendingIntent)
////                    .setSmallIcon(smallIcon())
////                    .setCustomBigContentView(content)
////                    .setCustomContentView(contentCompat)
//////                    .addAction(actionMaker(R.drawable.ic_previous, "Pre", prePend))
//////                    .addAction(actionMaker(R.drawable.ic_play_button, "Play", playPend))
//////                    .addAction(actionMaker(R.drawable.ic_skip, "Next", nextPend))
//////                    .addAction(actionMaker(R.drawable.ic_cancel, "Stop", stopPend))
//////                    .setColor(colorToBe).setColorized(true)
////                    .build();
////        } else {
////
////            notification = new NotificationCompat.Builder(this).setContentText(currentTrack.getTitle()).setTicker("MBN").setContentIntent(pendingIntent)
//////                    .setSmallIcon(smallIcon()).setContentTitle(currentTrack.getAlbumTitle()).setStyle(new NotificationCompat.MediaStyle().setShowActionsInCompactView(0, 1, 2, 3))
//////                    .addAction(R.drawable.ic_previous, "Pre", prePend)
//////                    .addAction(playButton(), "Play", playPend)
//////                    .addAction(R.drawable.ic_skip, "Next", nextPend)
//////                    .addAction(R.drawable.ic_cancel, "Stop", stopPend)
//////                    .setColor(colorToBe)
////
////                    .setSmallIcon(smallIcon())
////                    .setCustomBigContentView(content)
////                    .setCustomContentView(contentCompat)
////
////                    .build();
////        }
//
//        Intent intent = new Intent(this, FirstActivity.class);
//        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
//        stackBuilder.addParentStack(FirstActivity.class);
//        stackBuilder.addNextIntent(intent);
//        PendingIntent pendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
//
//
//        Intent intentPre = new Intent(this, this.getClass());
//        intentPre.setAction(ACTION_PREVIOUS);
//        PendingIntent prePend = PendingIntent.getService(this, 0, intentPre, 0);
//
//        Intent intentPlay = new Intent(this, MediaPlayerService.class);
//        intentPlay.setAction(ACTION_PLAY);
//        PendingIntent playPend = PendingIntent.getService(this, 0, intentPlay, 0);
//
//        Intent intentNext = new Intent(this, MediaPlayerService.class);
//        intentNext.setAction(ACTION_NEXT);
//        PendingIntent nextPend = PendingIntent.getService(this, 0, intentNext, 0);
//
//        Intent intentStop = new Intent(this, this.getClass());
//        intentStop.setAction(ACTION_STOP);
//        PendingIntent stopPend = PendingIntent.getService(this, 0, intentStop, 0);
//
//
//        RemoteViews content = new RemoteViews(getPackageName(), R.layout.notification_layout);
//        content.setImageViewBitmap(R.id.notification_cover_image, MbnController.getCover(getApplicationContext(), currentTrack.getPath(), 1));
//        content.setTextViewText(R.id.notification_titile, currentTrack.getTitle());
//        content.setTextViewText(R.id.notification_album, currentTrack.getAlbumTitle());
//        content.setTextViewText(R.id.notification_artist, currentTrack.getArtistTitle());
//        content.setTextColor(R.id.notification_titile, colorForText);
//        content.setOnClickPendingIntent(R.id.notification_pre, prePend);
//        content.setOnClickPendingIntent(R.id.notification_play, playPend);
//        content.setOnClickPendingIntent(R.id.notification_next, nextPend);
//        content.setOnClickPendingIntent(R.id.notification_stop, stopPend);
//        content.setImageViewResource(R.id.notification_stop, R.drawable.ic_cancel);
//        content.setImageViewResource(R.id.notification_pre, R.drawable.ic_previous);
//        content.setImageViewResource(R.id.notification_play, playButton());
//        content.setImageViewResource(R.id.notification_next, R.drawable.ic_skip);
//        content.setImageViewBitmap(R.id.notification_background, gradient());
//
//
//        RemoteViews contentCompact = new RemoteViews(getPackageName(), R.layout.notification_layout_compact);
//        contentCompact.setImageViewBitmap(R.id.notification_cover_image, MbnController.getCover(getApplicationContext(), currentTrack.getPath(), 2));
//        contentCompact.setTextViewText(R.id.notification_titile, currentTrack.getTitle());
//        contentCompact.setTextColor(R.id.notification_titile, colorForText);
//        contentCompact.setOnClickPendingIntent(R.id.notification_pre, prePend);
//        contentCompact.setOnClickPendingIntent(R.id.notification_play, playPend);
//        contentCompact.setOnClickPendingIntent(R.id.notification_next, nextPend);
//        contentCompact.setOnClickPendingIntent(R.id.notification_stop, stopPend);
//        contentCompact.setImageViewResource(R.id.notification_stop, R.drawable.ic_cancel);
//        contentCompact.setImageViewResource(R.id.notification_pre, R.drawable.ic_previous);
//        contentCompact.setImageViewResource(R.id.notification_play, playButton());
//        contentCompact.setImageViewResource(R.id.notification_next, R.drawable.ic_skip);
//        contentCompact.setImageViewBitmap(R.id.notification_background, gradient());
//
//
////        Icon nextIcon = Icon.createWithResource(getApplicationContext(), R.drawable.ic_skip);
////        Notification.Action action = new Notification.Action.Builder(nextIcon, "Next", nextPend).build();
//
//        Notification notification = null;
//        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
//            notification = new Notification.Builder(this, channel_id)
////                    .setContentTitle(currentTrack.getAlbumTitle())
////                    .setContentText(currentTrack.getTitle()).setTicker("MBN").setContentIntent(pendingIntent)
//                    .setSmallIcon(smallIcon())
//                    .setCustomBigContentView(content)
//                    .setCustomContentView(contentCompact)
//
////                    .addAction(actionMaker(R.drawable.ic_previous, "Pre", prePend))
////                    .addAction(actionMaker(playIcon, "Play", playPend))
////                    .addAction(actionMaker(R.drawable.ic_skip, "Next", nextPend))
////                    .addAction(actionMaker(R.drawable.ic_cancel, "Stop", stopPend))
////                    .setLargeIcon(currentTrack.getCover()).setColor(colorToBe)
////                    .setColorized(true)
//                    .build();
//        } else {
//
//            notification = new NotificationCompat.Builder(this).setContentText(currentTrack.getTitle()).setTicker("MBN").setContentIntent(pendingIntent)
////                    .setSmallIcon(smallIcon()).setContentTitle(currentTrack.getAlbumTitle()).setStyle(new NotificationCompat.MediaStyle().setShowActionsInCompactView(0, 1, 2, 3))
////                    .addAction(R.drawable.ic_previous, "Pre", prePend)
////                    .addAction(playButton(), "Play", playPend)
////                    .addAction(R.drawable.ic_skip, "Next", nextPend)
////                    .addAction(R.drawable.ic_cancel, "Stop", stopPend)
////                    .setLargeIcon(currentTrack.getCover()).setColor(colorToBe)
//
//                    .setSmallIcon(smallIcon())
//                    .setCustomBigContentView(content)
//                    .setCustomContentView(contentCompact)
//
//                    .build();
//        }
//
////        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
//
////        manager.notify(NOTIFICATION_ID, notification);
//
//        startForeground(NOTIFICATION_ID, notification);
////        isForeground = true;
//
//
//    }
//
//    private boolean working = true;
//
//
////    private class AsyncTimerTask extends AsyncTask<Void, Void, Void> {
////
////
////        @Override
////        protected Void doInBackground(Void... voids) {
////
////
////            publishProgress();
////
////
////            return null;
////
////        }
////
////
////        @Override
////        protected void onProgressUpdate(Void... values) {
////            super.onProgressUpdate(values);
////
////            if (playerState) {
////
////
////                try {
////                    Intent intent = new Intent(ON_TICK);
////                    intent.putExtra("seek", getSeekPosition());
////                    intent.putExtra("duration", getDurationSync());
////                    sendBroadcast(intent);
////                } catch (Exception e) {
////                }
////            }
////
////        }
////
////
////    }
//
//
////    private void timer() {
////
////        final int delay = 1000;
////
////        final Handler handler = new Handler();
////
////        Runnable runnable = new Runnable() {
////            @Override
////            public void run() {
////
////                try {
////                    if (working) {
////                        if (playerState) {
////
////                            seekPosition = getSeekPosition();
////
////                            if (playerFragmentIsOn) {
////
////                                try {
////                                    Intent intent = new Intent(ON_TICK);
////                                    intent.putExtra("seek", seekPosition);
////                                    intent.putExtra("duration", getDurationSync());
////                                    sendBroadcast(intent);
////                                } catch (Exception ignored) {
////                                }
////                            }
////
////                        }
////                        handler.postDelayed(this, delay);
////
////                    } else {
////                        handler.removeCallbacks(this, delay);
////                    }
////                } catch (Exception e) {
////                    e.printStackTrace();
////                }
////
////
////            }
////        };
////
////        handler.postDelayed(runnable, delay);
////
//////        Timer timer = new Timer();
//////
//////        timer.scheduleAtFixedRate(new TimerTask() {
//////            @Override
//////            public void run() {
//////
//////                if (working) {
//////                    if (playerFragmentIsOn) {
//////
//////
//////                        new AsyncTimerTask().execute();
//////                    }
//////                } else {
//////                    cancel();
//////                }
//////
//////            }
//////        }, 500, 1000);
////
////
////    }
//
//
//    boolean isBound = false;
//
//    private MbnTimerHelper listener;
//
//    public void setBound(boolean bound) {
//        isBound = bound;
//    }
//
//    public void setListener(MbnTimerHelper listener) {
//        this.listener = listener;
//    }
//
//    interface MbnTimerHelper {
//
//        void onTick(int currentPosition, int duration);
//
//        void onTrackChange();
//
//    }
//
//    private void sleepTimerReceiverRegister() {
//
//        IntentFilter filter = new IntentFilter(TIMER_ACTION_COMMAND);
//
//        LocalBroadcastManager.getInstance(getApplicationContext()).registerReceiver(sleepTimerReceiver, filter);
//
//    }
//
//    BroadcastReceiver sleepTimerReceiver = new BroadcastReceiver() {
//        @Override
//        public void onReceive(Context context, Intent intent) {
//
//
//            int action = intent.getIntExtra(TIMER_ACTION, 0);
//
//            switch (action) {
//
//                case 0:
//                    stopTimer();
//                    break;
//                case 1:
//                    long time = intent.getLongExtra(TIMER_ACTION_TIME, 0);
//                    startTimer(time);
//                    break;
//            }
//
//        }
//    };
//
//
//    private CountDownTimer countDownTimer;
//
//    private void startTimer(long time) {
//
//
//        stopTimer();
//
//        countDownTimer = new CountDownTimer(time, 60 * 1000) {
//            @Override
//            public void onTick(long l) {
//
//            }
//
//            @Override
//            public void onFinish() {
//
//                MbnController.stop(getApplicationContext());
//
//            }
//        }.start();
//
//
//    }
//
//    private void stopTimer() {
//
//        if (countDownTimer != null) {
//
//            countDownTimer.cancel();
//
//            countDownTimer = null;
//
//        }
//
//
//    }
//
//
//    private BroadcastReceiver headsetButtonListener = new BroadcastReceiver() {
//        @Override
//        public void onReceive(Context context, Intent intent) {
//
//            Log.i("event_b", intent.getAction());
//
//            if (intent.hasExtra(Intent.EXTRA_KEY_EVENT)) {
//                KeyEvent keyEvent = intent.getParcelableExtra(Intent.EXTRA_KEY_EVENT);
//                switch (keyEvent.getKeyCode()) {
//                    case KeyEvent.KEYCODE_HEADSETHOOK:
//                    case KeyEvent.KEYCODE_MEDIA_PLAY:
//                    case KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE:
//                    case KeyEvent.KEYCODE_MEDIA_PAUSE:
//                        if (mediaPlayer != null && mediaPlayer.isPlaying())
//                            MbnController.pause(getApplicationContext());
//                        else MbnController.play(getApplication());
//                        break;
//
//                    case KeyEvent.KEYCODE_MEDIA_STOP:
//                        MbnController.stop(getApplicationContext());
//                        break;
//
//                    case KeyEvent.KEYCODE_MEDIA_NEXT:
//                        MbnController.next(getApplicationContext());
//                        break;
//
//                    case KeyEvent.KEYCODE_MEDIA_PREVIOUS:
//                        MbnController.previous(getApplicationContext());
//                        break;
//
//
//                }
//
//            }
//
//        }
//    };
//
//
//}
//
//
