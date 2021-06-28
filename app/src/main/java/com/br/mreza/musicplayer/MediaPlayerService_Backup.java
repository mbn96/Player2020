//package com.br.mreza.musicplayer;
//
//import android.app.Notification;
//import android.app.NotificationChannel;
//import android.app.NotificationManager;
//import android.app.PendingIntent;
//import android.app.Service;
//import android.content.BroadcastReceiver;
//import android.content.Context;
//import android.content.Intent;
//import android.content.IntentFilter;
//import android.media.AudioManager;
//import android.media.MediaPlayer;
//import android.media.session.MediaSessionManager;
//import android.os.Binder;
//import android.os.Build;
//import android.os.IBinder;
//import android.support.annotation.RequiresApi;
//import android.support.v4.app.TaskStackBuilder;
//import android.support.v4.media.session.MediaControllerCompat;
//import android.support.v4.media.session.MediaSessionCompat;
//import android.support.v7.app.NotificationCompat;
//import android.telephony.PhoneStateListener;
//import android.telephony.TelephonyManager;
//import android.widget.Toast;
//
//import java.io.IOException;
//
//import static com.br.mreza.musicplayer.ListMaker.currentTrack;
//
//public class MediaPlayerService_Backup extends Service implements MediaPlayer.OnCompletionListener,
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
//    private boolean state;
//    private int seekPosition = 0;
//
//
//    public static final String ACTION_PLAY = "com.br.mreza.musicplayer.ACTION_PLAY";
//    public static final String ACTION_PAUSE = "com.br.mreza.musicplayer.ACTION_PAUSE";
//    public static final String ACTION_PREVIOUS = "com.br.mreza.musicplayer.ACTION_PREVIOUS";
//    public static final String ACTION_NEXT = "com.br.mreza.musicplayer.ACTION_NEXT";
//    public static final String ACTION_STOP = "com.br.mreza.musicplayer.ACTION_STOP";
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
//                            MbnController.pause(MediaPlayerService_Backup.this);
//                            ongoingCall = true;
//                        }
//                        break;
//                    case TelephonyManager.CALL_STATE_IDLE:
//                        // Phone idle. Start playing.
//                        if (mediaPlayer != null) {
//                            if (ongoingCall) {
//                                ongoingCall = false;
//                                MbnController.play(MediaPlayerService_Backup.this);
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
//    @Override
//    public void onBufferingUpdate(MediaPlayer mp, int percent) {
//        //Invoked indicating buffering status of
//        //a media resource being streamed over the network.
//    }
//
//    @Override
//    public void onCompletion(MediaPlayer mp) {
//        //Invoked when playback of a media source has completed.
//        MbnController.next(this);
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
//        mediaPlayer.start();
////        mediaPlayer.seekTo(seekPosition);
//    }
//
//    @Override
//    public void onSeekComplete(MediaPlayer mp) {
//        //Invoked indicating the completion of a seek operation.
////        mediaPlayer.start();
//    }
//
//    @Override
//    public void onAudioFocusChange(int focusState) {
//        //Invoked when the audio focus of the system is updated.
//        switch (focusState) {
//            case AudioManager.AUDIOFOCUS_GAIN:
//                // resume playback
//
//                break;
//            case AudioManager.AUDIOFOCUS_LOSS:
//                // Lost focus for an unbounded amount of time: stop playback and release media player
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
////        registerReceiver();
//
//        System.out.println("startCommand here --------------------------->");
//
//        System.out.println(intent.getAction());
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
//
//            noisyReceiver = new BroadcastReceiver() {
//                @Override
//                public void onReceive(Context context, Intent intent) {
//
//                    MbnController.pause(MediaPlayerService_Backup.this);
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
////        callStateListener();
//
//
//        handlingNotificationCallBacks(intent);
//
//        updateNotification();
//
//
//
//        return super.onStartCommand(intent, flags, startId);
//    }
//
//    private boolean requestAudioFocus() {
//        audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
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
//                break;
//            case ACTION_NEXT:
//                MbnController.next(this);
//                break;
//            case ACTION_PLAY:
//                if (state) {
//                    MbnController.pause(this);
//                } else {
//                    MbnController.play(this);
//                }
//                break;
//            case ACTION_STOP:
//                MbnController.stop(this);
//                break;
//
//
//        }
//
//    }
//
//    private void updateNotification() {
//
//        Intent intent = new Intent(this, AlbumActivity.class);
//        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
//        stackBuilder.addParentStack(AlbumActivity.class);
//        stackBuilder.addNextIntent(intent);
//        PendingIntent pendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
//
//
//        Intent intentPre = new Intent(this, this.getClass());
//        intentPre.setAction(ACTION_PREVIOUS);
//        PendingIntent prePend = PendingIntent.getService(this, 0, intentPre, 0);
//
//        Intent intentPlay = new Intent(this, MediaPlayerService_Backup.class);
//        intentPlay.setAction(ACTION_PLAY);
//        PendingIntent playPend = PendingIntent.getService(this, 0, intentPlay, 0);
//
//        Intent intentNext = new Intent(this, MediaPlayerService_Backup.class);
//        intentNext.setAction(ACTION_NEXT);
//        PendingIntent nextPend = PendingIntent.getService(this, 0, intentNext, 0);
//
//        Intent intentStop = new Intent(this, this.getClass());
//        intentStop.setAction(ACTION_STOP);
//        PendingIntent stopPend = PendingIntent.getService(this, 0, intentStop, 0);
//
//        Notification notification = null;
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            notification = new Notification.Builder(this, channel_id).setContentTitle(currentTrack.getAlbumTitle())
//                    .setContentText(currentTrack.getTitle()).setTicker("MBN").setContentIntent(pendingIntent)
//                    .setSmallIcon(R.drawable.ic_play_button).setStyle(new Notification.MediaStyle().setShowActionsInCompactView(0, 1, 2, 3))
//                    .addAction(R.drawable.ic_play_button, "Pre", prePend)
//                    .addAction(R.drawable.ic_play_button, "Play", playPend)
//                    .addAction(R.drawable.ic_play_button, "Next", nextPend)
//                    .addAction(R.drawable.ic_play_circle, "Stop", stopPend)
//                    .build();
//        } else {
//
//            notification = new NotificationCompat.Builder(this).setContentText(currentTrack.getTitle()).setTicker("MBN").setContentIntent(pendingIntent)
//                    .setSmallIcon(R.drawable.ic_play_button).setContentTitle(currentTrack.getAlbumTitle()).setStyle(new NotificationCompat.MediaStyle().setShowActionsInCompactView(0, 1, 2, 3))
//                    .addAction(R.drawable.ic_play_button, "Pre", prePend)
//                    .addAction(R.drawable.ic_play_button, "Play", playPend)
//                    .addAction(R.drawable.ic_play_button, "Next", nextPend)
//                    .addAction(R.drawable.ic_play_button, "Stop", stopPend)
//                    .build();
//        }
//
//
//        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
//
//        manager.notify(NOTIFICATION_ID, notification);
//
////        startForeground(NOTIFICATION_ID, notification);
//
//
//    }
//
//
//    @Override
//    public void onDestroy() {
//        super.onDestroy();
//        removeAudioFocus();
//        unregisterReceiver(noisyReceiver);
//        unregisterReceiver(actionReceiver);
//        stopForeground(true);
//    }
//
//    @Override
//    public void onCreate() {
//
//        registerReceiver();
//        super.onCreate();
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            makeNotifyChannel();
//        }
//        makeNotification();
//
//
//        try {
//            playerInitiate();
//            playerStart();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//    }
//
//    private void playerInitiate() {
//
//
//        if (mediaPlayer != null) {
//            mediaPlayer.reset();
//            mediaPlayer.release();
//        }
//        mediaPlayer = null;
//        mediaPlayer = new MediaPlayer();
//        mediaPlayer.setOnPreparedListener(this);
//        mediaPlayer.setOnCompletionListener(this);
//        mediaPlayer.setOnSeekCompleteListener(this);
//        try {
//            mediaPlayer.setDataSource(currentTrack.getPath());
//        } catch (IOException e) {
//            e.printStackTrace();
//            Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show();
//        }
//
//
//    }
//
//    private void playerStart() {
//        try {
//            mediaPlayer.prepare();
//            mediaPlayer.seekTo(seekPosition);
//            state = true;
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//
//    BroadcastReceiver actionReceiver = new BroadcastReceiver() {
//        @Override
//        public void onReceive(Context context, Intent intent) {
//
//            String action = intent.getStringExtra("MBN");
//
//            System.out.println("service received------------------->");
//
//            switch (action) {
//
//                case MbnController.ACTION_CHANGE:
//                    seekPosition = 0;
//                    playerInitiate();
//                    playerStart();
//                    break;
//                case MbnController.ACTION_NEXT:
//                    seekPosition = 0;
//                    playerInitiate();
//                    if (state) {
//                        playerStart();
//                    }
//
//                    break;
//                case MbnController.ACTION_PREVIOUS:
//                    seekPosition = 0;
//                    playerInitiate();
//                    if (state) {
//                        playerStart();
//                    }
//                    break;
//                case MbnController.ACTION_PAUSE:
//                    seekPosition = mediaPlayer.getCurrentPosition();
//                    state = false;
//
//                    mediaPlayer.pause();
//
//                    break;
//                case MbnController.ACTION_PLAY:
//
//                    playerInitiate();
//                    playerStart();
//
//                    break;
//
//                case MbnController.ACTION_STOP:
//
//                    System.out.println("-----------------------------Action STOP----------------- Here");
//                    mediaPlayer.reset();
//                    mediaPlayer.release();
//                    stopForeground(true);
//                    stopSelf();
//
//                    break;
//
//            }
//
//        }
//    };
//
//
//    private void registerReceiver() {
//
//        IntentFilter filter = new IntentFilter(MbnController.intentID);
//
//        registerReceiver(actionReceiver, filter);
//
//    }
//
//
//    private class LocalBinder extends Binder {
//        public MediaPlayerService_Backup getService() {
//            return MediaPlayerService_Backup.this;
//        }
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
//        NotificationChannel channel = new NotificationChannel(channel_id, channelName, importance);
//        manager.createNotificationChannel(channel);
//
//        return channel_id;
//    }
//
//    public void makeNotification() {
//
//
//        Intent intent = new Intent(this, AlbumActivity.class);
//        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
//        stackBuilder.addParentStack(AlbumActivity.class);
//        stackBuilder.addNextIntent(intent);
//        PendingIntent pendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
//
//
//        Intent intentPre = new Intent(this, this.getClass());
//        intent.setAction(ACTION_PREVIOUS);
//        PendingIntent prePend = PendingIntent.getService(this, 0, intentPre, 0);
//
//        Intent intentPlay = new Intent(this, MediaPlayerService_Backup.class);
//        intent.setAction(ACTION_PLAY);
//        PendingIntent playPend = PendingIntent.getService(this, 0, intentPlay, 0);
//
//        Intent intentNext = new Intent(this, MediaPlayerService_Backup.class);
//        intent.setAction(ACTION_NEXT);
//        PendingIntent nextPend = PendingIntent.getService(this, 0, intentNext, 0);
//
//        Intent intentStop = new Intent(this, this.getClass());
//        intent.setAction(ACTION_STOP);
//        PendingIntent stopPend = PendingIntent.getService(this, 0, intentStop, 0);
//
//        Notification notification = null;
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            notification = new Notification.Builder(this, channel_id).setContentTitle(currentTrack.getAlbumTitle())
//                    .setContentText(currentTrack.getTitle()).setTicker("MBN").setContentIntent(pendingIntent)
//                    .setSmallIcon(R.drawable.ic_play_button).setStyle(new Notification.MediaStyle().setShowActionsInCompactView(0, 1, 2, 3))
//                    .addAction(R.drawable.ic_play_button, "Pre", prePend)
//                    .addAction(R.drawable.ic_play_button, "Play", playPend)
//                    .addAction(R.drawable.ic_play_button, "Next", nextPend)
//                    .addAction(R.drawable.ic_play_circle, "Stop", stopPend)
//                    .build();
//        } else {
//
//            notification = new NotificationCompat.Builder(this).setContentText(currentTrack.getTitle()).setTicker("MBN").setContentIntent(pendingIntent)
//                    .setSmallIcon(R.drawable.ic_play_button).setContentTitle(currentTrack.getAlbumTitle()).setStyle(new NotificationCompat.MediaStyle().setShowActionsInCompactView(0, 1, 2, 3))
//                    .addAction(R.drawable.ic_play_button, "Pre", prePend)
//                    .addAction(R.drawable.ic_play_button, "Play", playPend)
//                    .addAction(R.drawable.ic_play_button, "Next", nextPend)
//                    .addAction(R.drawable.ic_play_button, "Stop", stopPend)
//                    .build();
//        }
//
//
//        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
//
////        manager.notify(NOTIFICATION_ID, notification);
//
//        startForeground(NOTIFICATION_ID, notification);
//
//
//    }
//
//}
//
//
