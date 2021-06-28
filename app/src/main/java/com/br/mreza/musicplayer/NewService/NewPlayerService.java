//package com.br.mreza.musicplayer.NewService;
//
//import android.app.NotificationManager;
//import android.app.PendingIntent;
//import android.app.Service;
//import android.content.BroadcastReceiver;
//import android.content.Context;
//import android.content.Intent;
//import android.content.IntentFilter;
//import android.graphics.Bitmap;
//import android.media.AudioManager;
//import android.os.Handler;
//import android.os.IBinder;
//import android.os.Looper;
//import android.os.Message;
//import android.support.v4.app.TaskStackBuilder;
//import android.support.v4.media.session.MediaButtonReceiver;
//import android.support.v4.media.session.MediaSessionCompat;
//import android.widget.RemoteViews;
//
//import com.br.mreza.musicplayer.BackgroundForUI.BackImageChangeHandler;
//import com.br.mreza.musicplayer.MbnController;
//import com.br.mreza.musicplayer.MediaSessionHolder;
//import com.br.mreza.musicplayer.R;
//import com.br.mreza.musicplayer.StorageUtils;
//import com.br.mreza.musicplayer.newdesign.FirstNewDesignActivity;
//import com.br.mreza.musicplayer.newdesign.asyncloader.AsyncLoaderManager;
//
//import java.lang.ref.WeakReference;
//
//import static com.br.mreza.musicplayer.ListMaker.getCurrentTrack;
//import static com.br.mreza.musicplayer.MbnController.colorForText;
//import static com.br.mreza.musicplayer.MbnController.intentID;
//import static com.br.mreza.musicplayer.MbnController.playerServiceWeakReference;
//import static com.br.mreza.musicplayer.MbnController.playerState;
//import static com.br.mreza.musicplayer.newdesign.MbnMusicPlayer.channel_id;
//import static com.br.mreza.musicplayer.newdesign.TimerChooseDialog.ONE_MIN;
//
//public class NewPlayerService extends Service implements BackImageChangeHandler.ImageChangeCallback {
//
//    public static final String ACTION_PLAY = "com.br.mreza.musicplayer.ACTION_PLAY";
//    //    public static final String ACTION_PAUSE = "com.br.mreza.musicplayer.ACTION_PAUSE";
//    public static final String ACTION_PREVIOUS = "com.br.mreza.musicplayer.ACTION_PREVIOUS";
//    public static final String ACTION_NEXT = "com.br.mreza.musicplayer.ACTION_NEXT";
//    public static final String ACTION_STOP = "com.br.mreza.musicplayer.ACTION_STOP";
//
//
//    private BroadcastReceiver noisyReceiver = null;
//
//    public NewPlayerService() {
//    }
//
//    private static final int NOTIFICATION_ID = 96;
//
//    @Override
//    public IBinder onBind(Intent intent) {
//        // TODO: Return the communication channel to the service.
//        throw new UnsupportedOperationException("Not yet implemented");
//    }
//
//
//    private PlayerHandlerThread playerHandlerThread;
//
//    private MediaSessionCompat.Callback mediaSessionCallbacks = new MediaSessionCompat.Callback() {
//        @Override
//        public void onPlay() {
//            MbnController.play(getApplicationContext());
//        }
//
//        @Override
//        public void onPause() {
//            MbnController.pause(getApplicationContext());
//        }
//
//
//        @Override
//        public void onSkipToNext() {
//            MbnController.next(getApplicationContext());
//        }
//
//        @Override
//        public void onSkipToPrevious() {
//            MbnController.previous(getApplicationContext());
//        }
//
//        @Override
//        public void onRewind() {
//            MusicInfoHolder.setSeekPos(MusicInfoHolder.getCurrentPos() - 5000);
//            MbnController.seek(getApplicationContext());
//        }
//
//        @Override
//        public void onFastForward() {
//            MusicInfoHolder.setSeekPos(MusicInfoHolder.getCurrentPos() + 5000);
//            MbnController.seek(getApplicationContext());
//        }
//
//        @Override
//        public void onStop() {
//            MbnController.stop(getApplicationContext());
//        }
//
//        @Override
//        public void onSeekTo(long pos) {
//            MusicInfoHolder.setSeekPos((int) pos);
//            MbnController.seek(getApplicationContext());
//        }
//    };
//
//    @Override
//    public void onCreate() {
////        registerReceiver(new MediaButtonReceiver(), new IntentFilter(Intent.ACTION_MEDIA_BUTTON));
//        synchronized (AsyncLoaderManager.INSTANCE) {
//            if (!AsyncLoaderManager.INSTANCE.isHasStarted()) {
//                try {
//                    AsyncLoaderManager.INSTANCE.wait();
//                } catch (InterruptedException ignored) {
//                }
//            }
//        }
//        playerHandlerThread = new PlayerHandlerThread((AudioManager) getSystemService(AUDIO_SERVICE), getApplicationContext(), songFinishHandler);
//        MediaSessionHolder.reset(getApplicationContext());
//        MediaSessionHolder.getMediaSession(getApplicationContext()).setCallback(mediaSessionCallbacks);
//
//        super.onCreate();
//        MediaSessionHolder.getMediaSession(getApplicationContext()).setActive(true);
//
//        MbnController.isNewServiceAlive = true;
//        MbnController.playerServiceWeakReference = new WeakReference<>(this);
//        BackImageChangeHandler.instance.addCallback(this);
//        makeNotification(null, null, true);
//        BackImageChangeHandler.instance.force(getApplicationContext());
//
//        checkForTimer();
//        songFinishHandler.postDelayed(checkForTimerRunnable, 5000);
////        registerReceiver(everyMinReceiver, new IntentFilter(Intent.ACTION_TIME_TICK));
//    }
//
//    @Override
//    public int onStartCommand(Intent intent, int flags, int startId) {
//
//        MediaButtonReceiver.handleIntent(MediaSessionHolder.getMediaSession(getApplicationContext()), intent);
//
//        if (noisyReceiver == null) {
//            noisyReceiver = new BroadcastReceiver() {
//                @Override
//                public void onReceive(Context context, Intent intent) {
//                    MbnController.pause(getApplicationContext());
//
//                }
//            };
//            IntentFilter filter = new IntentFilter(AudioManager.ACTION_AUDIO_BECOMING_NOISY);
//            registerReceiver(noisyReceiver, filter);
//        }
//
//        if (intent != null) {
//            String intentAction = intent.getAction();
//
//            if (intentAction != null) {
//                switch (intentAction) {
//
//                    case intentID:
//                        play();
//                        break;
//                    default:
//                        handlingNotificationCallBacks(intent);
//                        break;
//
//                }
//            }
//        }
//
//
//        return super.onStartCommand(intent, flags, startId);
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
//                MediaSessionHolder.visyHelper.inAct();
//                MbnController.stop(this);
//                break;
//
//
//        }
//
//    }
//
//    @Override
//    public void onDestroy() {
//        playerServiceWeakReference.clear();
//        unregisterReceiver(noisyReceiver);
////        unregisterReceiver(everyMinReceiver);
//        songFinishHandler.removeCallbacks(checkForTimerRunnable);
//        BackImageChangeHandler.instance.removeCallback(this);
//        playerHandlerThread.release();
//        MbnController.isNewServiceAlive = false;
//        MediaSessionHolder.getMediaSession(getApplicationContext()).release();
//        super.onDestroy();
//    }
//
//    @Override
//    public void onChange(Bitmap original, Bitmap blur) {
//        // TODO update notification ...
//        makeNotification(original, blur, false);
//    }
//
//    private void makeNotification(Bitmap art, Bitmap blur, boolean fake) {
//        if (fake) {
//            android.support.v4.app.NotificationCompat.Builder builder = new android.support.v4.app.NotificationCompat.Builder(getApplicationContext(), channel_id);
//            startForeground(NOTIFICATION_ID, builder.build());
//            return;
//        }
//
//        Intent intent = new Intent(getApplicationContext(), FirstNewDesignActivity.class);
//        TaskStackBuilder stackBuilder = TaskStackBuilder.create(getApplicationContext());
//        stackBuilder.addParentStack(FirstNewDesignActivity.class);
//        stackBuilder.addNextIntent(intent);
////        stackBuilder.addNextIntentWithParentStack(intent);
////        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, 0);
//        PendingIntent pendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_CANCEL_CURRENT);
//
//        Intent intentPre = new Intent(getApplicationContext(), NewPlayerService.class);
//        intentPre.setAction(ACTION_PREVIOUS);
//        PendingIntent prePend = PendingIntent.getService(getApplicationContext(), 0, intentPre, 0);
//
//        Intent intentPlay = new Intent(getApplicationContext(), NewPlayerService.class);
//        intentPlay.setAction(ACTION_PLAY);
//        PendingIntent playPend = PendingIntent.getService(getApplicationContext(), 0, intentPlay, 0);
//
//        Intent intentNext = new Intent(getApplicationContext(), NewPlayerService.class);
//        intentNext.setAction(ACTION_NEXT);
//        PendingIntent nextPend = PendingIntent.getService(getApplicationContext(), 0, intentNext, 0);
//
//        Intent intentStop = new Intent(getApplicationContext(), NewPlayerService.class);
//        intentStop.setAction(ACTION_STOP);
//        PendingIntent stopPend = PendingIntent.getService(getApplicationContext(), 0, intentStop, 0);
//
//
//        RemoteViews content = new RemoteViews(getPackageName(), R.layout.notification_layout);
//        content.setImageViewBitmap(R.id.notification_cover_image, art);
//        content.setTextViewText(R.id.notification_titile, getCurrentTrack().getTitle());
//        content.setTextViewText(R.id.notification_album, getCurrentTrack().getAlbumTitle());
//        content.setTextViewText(R.id.notification_artist, getCurrentTrack().getArtistTitle());
//        content.setTextColor(R.id.notification_titile, colorForText);
//        content.setOnClickPendingIntent(R.id.notification_pre, prePend);
//        content.setOnClickPendingIntent(R.id.notification_play, playPend);
//        content.setOnClickPendingIntent(R.id.notification_next, nextPend);
//        content.setOnClickPendingIntent(R.id.notification_stop, stopPend);
//        content.setImageViewResource(R.id.notification_stop, R.drawable.ic_close_black_24dp);
//        content.setImageViewResource(R.id.notification_pre, R.drawable.ic_rewind_arrow);
//        content.setImageViewResource(R.id.notification_play, playButton());
//        content.setImageViewResource(R.id.notification_next, R.drawable.ic_fast_forward_arrow);
//        content.setImageViewBitmap(R.id.notification_background, blur);
////        content.setImageViewBitmap(R.id.notification_background, gradient());
//
//
//        RemoteViews contentCompact = new RemoteViews(getPackageName(), R.layout.notification_layout_compact);
//        contentCompact.setImageViewBitmap(R.id.notification_cover_image, art);
//        contentCompact.setTextViewText(R.id.notification_titile, getCurrentTrack().getTitle());
//        contentCompact.setTextColor(R.id.notification_titile, colorForText);
//        contentCompact.setOnClickPendingIntent(R.id.notification_pre, prePend);
//        contentCompact.setOnClickPendingIntent(R.id.notification_play, playPend);
//        contentCompact.setOnClickPendingIntent(R.id.notification_next, nextPend);
//        contentCompact.setOnClickPendingIntent(R.id.notification_stop, stopPend);
//        contentCompact.setImageViewResource(R.id.notification_stop, R.drawable.ic_close_black_24dp);
//        contentCompact.setImageViewResource(R.id.notification_pre, R.drawable.ic_rewind_arrow);
//        contentCompact.setImageViewResource(R.id.notification_play, playButton());
//        contentCompact.setImageViewResource(R.id.notification_next, R.drawable.ic_fast_forward_arrow);
//        contentCompact.setImageViewBitmap(R.id.notification_background, blur);
////        contentCompact.setImageViewBitmap(R.id.notification_background, gradient());
//
//
////        Icon nextIcon = Icon.createWithResource(getApplicationContext(), R.drawable.ic_skip);
////        Notification.Action action = new Notification.Action.Builder(nextIcon, "Next", nextPend).build();
//
//
//        android.support.v4.app.NotificationCompat.Builder trueBuilder = new android.support.v4.app.NotificationCompat.Builder(getApplicationContext(), channel_id)
//                .setContentIntent(pendingIntent)
//                .setSmallIcon(smallIcon())
//                .setCustomBigContentView(content)
//                .setCustomContentView(contentCompact);
//
//        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
//        assert manager != null;
//        if (MbnController.isNewServiceAlive) manager.notify(NOTIFICATION_ID, trueBuilder.build());
//
////        Notification notification = null;
////        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
////            notification = new Notification.Builder(getApplicationContext(), channel_id)
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
////
////            notification = new NotificationCompat.Builder(getApplicationContext()).setContentText(currentTrack.getTitle()).setTicker("MBN").setContentIntent(pendingIntent)
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
//    }
//
//    private int smallIcon() {
//        int smallIcon;
//        if (playerState) {
//            smallIcon = R.drawable.ic_play_button;
//        } else {
//            smallIcon = R.drawable.ic_pause;
//        }
//        return smallIcon;
//    }
//
//    private int playButton() {
//        int playIcon;
//        if (playerState) {
//            playIcon = R.drawable.ic_music_player_pause_lines;
//        } else {
//            playIcon = R.drawable.ic_music_player_play;
//        }
//        return playIcon;
//    }
//
//
//    public void play() {
//        playerHandlerThread.play(true);
//    }
//
//    public void pause() {
//        playerHandlerThread.pause();
//    }
//
//    public void next_previous_change() {
////        Log.i(TAG, "next_previous_change: yes is called");
//        MusicInfoHolder.resetA_B();
//        playerHandlerThread.play(false);
//    }
//
//    public void seek() {
//        playerHandlerThread.seek();
//    }
//
//    public void stop() {
//        stopForeground(true);
//        stopSelf();
//    }
//
//
//    private Handler songFinishHandler = new Handler(Looper.getMainLooper(), new Handler.Callback() {
//        @Override
//        public boolean handleMessage(Message msg) {
//            if (msg.arg1 == 2) {
//                MbnController.seek(getApplicationContext());
//            } else
//                MbnController.onComplete(getApplicationContext());
//            return true;
//        }
//    });
//
//    private Runnable checkForTimerRunnable = new Runnable() {
//        @Override
//        public void run() {
//            checkForTimer();
//            songFinishHandler.postDelayed(this, 5000);
//        }
//    };
//
//    private BroadcastReceiver everyMinReceiver = new BroadcastReceiver() {
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            checkForTimer();
//        }
//    };
//
//    private void checkForTimer() {
//        long diff = StorageUtils.getTimer(getApplicationContext()) - System.currentTimeMillis();
//        if (diff > 0) return;
//        diff = Math.abs(diff);
//        StorageUtils.setTimer(getApplicationContext(), 0);
//        if (diff / ONE_MIN < 2) {
//            MbnController.stop(getApplicationContext());
//        }
//    }
//
//}
