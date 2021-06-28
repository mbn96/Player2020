package com.br.mreza.musicplayer.newmodel.service;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.graphics.Shader;
import android.media.AudioManager;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;

import androidx.core.app.NotificationCompat;
import androidx.core.app.TaskStackBuilder;
import androidx.media.session.MediaButtonReceiver;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RemoteViews;

import com.br.mreza.musicplayer.DataSong;
import com.br.mreza.musicplayer.MBN.bitmapUtils.MbnUtils;
import com.br.mreza.musicplayer.R;
import com.br.mreza.musicplayer.StorageUtils;
import com.br.mreza.musicplayer.newdesign.FirstNewDesignActivity;
import com.br.mreza.musicplayer.newdesign.asyncloader.AsyncLoaderManager;
import com.br.mreza.musicplayer.newmodel.database.DataBaseManager;
import com.br.mreza.musicplayer.newmodel.service.player.PlayerCommunicates;
import com.br.mreza.musicplayer.newmodel.service.player.PlayerManager;
import com.br.mreza.musicplayer.newmodel.theme.ThemeEngine;
import com.br.mreza.musicplayer.p2020.design.MainActivity2020;

import mbn.libs.backgroundtask.BaseTaskHolder;
import mbn.libs.backgroundtask.ThreadManager;
import mbn.libs.imagelibs.imageworks.Effects;

import static com.br.mreza.musicplayer.MbnController.intentID;
import static com.br.mreza.musicplayer.newdesign.MbnMusicPlayer.channel_id;
import static com.br.mreza.musicplayer.newdesign.TimerChooseDialog.ONE_MIN;

public class NewModelPlayerService extends Service implements PlayerCommunicates.PlayerCallbacks {


    public static final String ACTION_PLAY = "com.br.mreza.musicplayer.ACTION_PLAY";
    //    public static final String ACTION_PAUSE = "com.br.mreza.musicplayer.ACTION_PAUSE";
    public static final String ACTION_PREVIOUS = "com.br.mreza.musicplayer.ACTION_PREVIOUS";
    public static final String ACTION_NEXT = "com.br.mreza.musicplayer.ACTION_NEXT";
    public static final String ACTION_STOP = "com.br.mreza.musicplayer.ACTION_STOP";

    private BroadcastReceiver noisyReceiver = null;
    private PlayerManager playerManager;
    private DataSong currentTrack;
    private boolean isWorking = true;

    private Bitmap art, blur;
    private int accentColor;
    private static final int TRANSPARENT_WHITE_COLOR = Color.argb(0, 255, 255, 255);
    private Paint gradientPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private long lastUpdate;


    private static final int NOTIFICATION_ID = 96;

    private ThemeEngine.DefaultThemeCallback themeCallback = new ThemeEngine.DefaultThemeCallback(true, true, true) {

        @Override
        public void onAccentColor(int color) {
            accentColor = color;
        }

        @Override
        public void onMainBitmap(Bitmap main) {
            art = main;

        }

        @Override
        public void onBlurBitmap(Bitmap blurIN) {
            blur = blurIN;

        }

        @Override
        public void onProcessFinished() {
            makeNotification(false);
        }
    };
    private WindowManager windowManager;
    private View overlayView;
    private NotificationManager notificationManager;


    public NewModelPlayerService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }


    @Override
    public void onCreate() {
//        registerReceiver(new MediaButtonReceiver(), new IntentFilter(Intent.ACTION_MEDIA_BUTTON));
        synchronized (AsyncLoaderManager.INSTANCE) {
            if (!AsyncLoaderManager.INSTANCE.isHasStarted()) {
                try {
                    AsyncLoaderManager.INSTANCE.wait();
                } catch (InterruptedException ignored) {
                }
            }
        }
        playerManager = new PlayerManager(getApplicationContext(), (AudioManager) getSystemService(AUDIO_SERVICE)) {
            @Override
            public void changeNotification(DataSong song) {
                currentTrack = song;
                makeNotification(false);
            }
        };
//        MediaSessionHolder.reset(getApplicationContext());
//        MediaSessionHolder.getMediaSession(getApplicationContext()).setCallback(mediaSessionCallbacks);

        super.onCreate();
//        MediaSessionHolder.getMediaSession(getApplicationContext()).setActive(true);

//        MbnController.isNewServiceAlive = true;
//        MbnController.playerServiceWeakReference = new WeakReference<>(this);
//        BackImageChangeHandler.instance.addCallback(this);
        ThemeEngine.getINSTANCE().registerCallback(themeCallback);
        makeNotification(true);
//        BackImageChangeHandler.instance.force(getApplicationContext());
        ThemeEngine.getINSTANCE().getResult(themeCallback);

        checkForTimer();
        songFinishHandler.postDelayed(checkForTimerRunnable, 5000);
//        registerReceiver(everyMinReceiver, new IntentFilter(Intent.ACTION_TIME_TICK));
//        makeOverlay();

        gradientPaint.setStyle(Paint.Style.FILL);

//        PlayerCommunicates.getINSTANCE().registerCallback(this);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        MediaButtonReceiver.handleIntent(playerManager.getMediaSession(), intent);

        if (noisyReceiver == null) {
            noisyReceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    PlayerCommunicates.getINSTANCE().pause();

                }
            };
            IntentFilter filter = new IntentFilter(AudioManager.ACTION_AUDIO_BECOMING_NOISY);
            registerReceiver(noisyReceiver, filter);
        }

        if (intent != null) {
            String intentAction = intent.getAction();

            if (intentAction != null) {
                switch (intentAction) {

                    case intentID:
//                        play();
                        break;
                    default:
                        handlingNotificationCallBacks(intent);
                        break;

                }
            }
        }


        return super.onStartCommand(intent, flags, startId);
    }


    private void handlingNotificationCallBacks(Intent intent) {


//        System.out.println("---------------------------heretoo");
        String action = intent.getAction();

        if (action == null) {
            return;
        }

        switch (action) {

            case ACTION_PREVIOUS:
                PlayerCommunicates.getINSTANCE().checkForPrevious();
//                playerInitiate();
//                updateNotification();
                break;
            case ACTION_NEXT:
                DataBaseManager.getManager().performNext();
//                playerInitiate();
//                updateNotification();
                break;
            case ACTION_PLAY:
                if (PlayerCommunicates.getINSTANCE().isPlaying()) {
                    PlayerCommunicates.getINSTANCE().pause();
//                    mediaPlayer.pause();
                } else {
                    PlayerCommunicates.getINSTANCE().play();
//                    playerInitiate();
                }
//                updateNotification();
                break;
            case ACTION_STOP:
//                MediaSessionHolder.visyHelper.inAct();
                stop();
                break;


        }

    }

    @Override
    public void onDestroy() {
//        removeOverLay();
//        PlayerCommunicates.getINSTANCE().unRegisterCallback(this);
        unregisterReceiver(noisyReceiver);
//        unregisterReceiver(everyMinReceiver);
        songFinishHandler.removeCallbacks(checkForTimerRunnable);
        playerManager.release();
//        MbnController.isNewServiceAlive = false;
//        MediaSessionHolder.getMediaSession(getApplicationContext()).release();
        super.onDestroy();
    }


    private void makeNotification(boolean fake) {
        if (isWorking) {
            if (fake) {
                androidx.core.app.NotificationCompat.Builder builder = new androidx.core.app.NotificationCompat.Builder(getApplicationContext(), channel_id);
                startForeground(NOTIFICATION_ID, builder.build());
                return;
            }

            ThreadManager.getAppGlobalTask().StartTask(new BaseTaskHolder.BaseTask() {
                @Override
                public Object onRun() {
                    int textColor = Effects.getMonochromePercentage(accentColor) >= 0.5 ? Color.DKGRAY : Color.WHITE;
                    Intent intent = new Intent(getApplicationContext(), MainActivity2020.class);
//            TaskStackBuilder stackBuilder = TaskStackBuilder.create(getApplicationContext());
//            stackBuilder.addParentStack(MainActivity2020.class);
//            stackBuilder.addNextIntent(intent);
//        stackBuilder.addNextIntentWithParentStack(intent);
//        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, 0);
                    PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 96, intent, PendingIntent.FLAG_UPDATE_CURRENT);

                    Intent intentPre = new Intent(getApplicationContext(), NewModelPlayerService.class);
                    intentPre.setAction(ACTION_PREVIOUS);
                    PendingIntent prePend = PendingIntent.getService(getApplicationContext(), 0, intentPre, 0);

                    Intent intentPlay = new Intent(getApplicationContext(), NewModelPlayerService.class);
                    intentPlay.setAction(ACTION_PLAY);
                    PendingIntent playPend = PendingIntent.getService(getApplicationContext(), 0, intentPlay, 0);

                    Intent intentNext = new Intent(getApplicationContext(), NewModelPlayerService.class);
                    intentNext.setAction(ACTION_NEXT);
                    PendingIntent nextPend = PendingIntent.getService(getApplicationContext(), 0, intentNext, 0);

                    Intent intentStop = new Intent(getApplicationContext(), NewModelPlayerService.class);
                    intentStop.setAction(ACTION_STOP);
                    PendingIntent stopPend = PendingIntent.getService(getApplicationContext(), 0, intentStop, 0);


//            RemoteViews content = new RemoteViews(getPackageName(), R.layout.notification_layout_2020);
//////            if (art != null) {
//////                content.setImageViewBitmap(R.id.notification_cover_image, art);
//////            }
//////            if (currentTrack != null) {
//////                content.setTextViewText(R.id.notification_titile, getString(R.string.app_name_2020) + " | " + currentTrack.getTitle());
//////                content.setTextViewText(R.id.notification_album, currentTrack.getAlbumTitle());
//////                content.setTextViewText(R.id.notification_artist, currentTrack.getArtistTitle());
//////            }
//////            content.setTextColor(R.id.notification_titile, textColor);
//////            content.setTextColor(R.id.notification_album, textColor);
//////            content.setTextColor(R.id.notification_artist, textColor);
//////
//////
////////            ImageButton view;
////////            view.tint
//////            content.setInt(R.id.notification_main_layout, "setBackgroundColor", accentColor);
//////            content.setTextColor(R.id.notification_stop, accentColor);
//////            content.setImageViewBitmap(R.id.notification_cover_image_tint, gradientBitmap());
////////            content.setTextColor(R.id.notification_titile, accentColor);
//////            content.setOnClickPendingIntent(R.id.notification_pre, prePend);
//////            content.setOnClickPendingIntent(R.id.notification_play, playPend);
//////            content.setOnClickPendingIntent(R.id.notification_next, nextPend);
//////            content.setOnClickPendingIntent(R.id.notification_stop, stopPend);
////////            content.setImageViewResource(R.id.notification_stop, R.drawable.ic_close_black_24dp);
//////            content.setImageViewResource(R.id.notification_pre, R.drawable.ic_rewind_arrow);
//////            content.setImageViewResource(R.id.notification_play, playButton());
////////            content.setTextViewText(R.id.notification_play, playButton_2020());
//////            content.setImageViewResource(R.id.notification_next, R.drawable.ic_fast_forward_arrow);
//////            if (blur != null && false) {
//////                content.setImageViewBitmap(R.id.notification_background, blur);
//////            }
////////        content.setImageViewBitmap(R.id.notification_background, gradient());
//////
//////
//////            RemoteViews contentCompact = new RemoteViews(getPackageName(), R.layout.notification_layout_compact_2020);
//////            if (art != null) {
//////                contentCompact.setImageViewBitmap(R.id.notification_cover_image, art);
//////            }
//////            if (currentTrack != null) {
//////                contentCompact.setTextViewText(R.id.notification_titile, currentTrack.getTitle());
//////            }
//////
//////            contentCompact.setTextColor(R.id.notification_titile, textColor);
////////            content.setTextColor(R.id.notification_album, textColor);
////////            content.setTextColor(R.id.notification_artist, textColor);
//////
//////            contentCompact.setInt(R.id.notification_main_layout, "setBackgroundColor", accentColor);
//////            contentCompact.setTextColor(R.id.notification_stop, accentColor);
////////            contentCompact.setTextColor(R.id.notification_titile, accentColor);
//////            contentCompact.setOnClickPendingIntent(R.id.notification_pre, prePend);
//////            contentCompact.setOnClickPendingIntent(R.id.notification_play, playPend);
//////            contentCompact.setOnClickPendingIntent(R.id.notification_next, nextPend);
//////            contentCompact.setOnClickPendingIntent(R.id.notification_stop, stopPend);
////////            contentCompact.setImageViewResource(R.id.notification_stop, R.drawable.ic_close_black_24dp);
//////            contentCompact.setImageViewResource(R.id.notification_pre, R.drawable.ic_rewind_arrow);
//////            contentCompact.setImageViewResource(R.id.notification_play, playButton());
//////            contentCompact.setImageViewResource(R.id.notification_next, R.drawable.ic_fast_forward_arrow);
//////            if (blur != null && false) {
//////                contentCompact.setImageViewBitmap(R.id.notification_background, blur);
//////            }


//        contentCompact.setImageViewBitmap(R.id.notification_background, gradient());


//        Icon nextIcon = Icon.createWithResource(getApplicationContext(), R.drawable.ic_skip);
//        Notification.Action action = new Notification.Action.Builder(nextIcon, "Next", nextPend).build();


                    androidx.media.app.NotificationCompat.MediaStyle style = new androidx.media.app.NotificationCompat.MediaStyle();
                    style.setShowActionsInCompactView(0, 1, 2);
                    style.setMediaSession(playerManager.getMediaSession().getSessionToken()).setCancelButtonIntent(stopPend).setShowCancelButton(true);
                    androidx.core.app.NotificationCompat.Builder trueBuilder = new androidx.core.app.NotificationCompat.Builder(getApplicationContext(), channel_id)
                            .addAction(R.drawable.ic_round_fast_rewind_new_24, "Previous", prePend)
                            .addAction(playButton(), playButton_2020(), playPend)
                            .addAction(R.drawable.ic_round_fast_forward_new_24, "Next", nextPend)
                            .addAction(R.drawable.ic_round_close_24, "Stop", stopPend);
                    if (currentTrack != null) {
                        trueBuilder.setContentTitle(currentTrack.getTitle())
                                .setContentText(currentTrack.getArtistTitle());
                    }
                    trueBuilder.setColor(accentColor)
                            .setColorized(true)
                            .setLargeIcon(art)
                            .setStyle(style)
                            .setContentIntent(pendingIntent)
                            .setAllowSystemGeneratedContextualActions(true)
                            .setSmallIcon(smallIcon());
//                    .setCustomBigContentView(content)
//                    .setCustomContentView(contentCompact);


//            androidx.core.app.NotificationCompat.Builder trueBuilder = new androidx.core.app.NotificationCompat.Builder(getApplicationContext(), channel_id)
//                    .setContentIntent(pendingIntent)
//                    .setSmallIcon(smallIcon())
//                    .setCustomBigContentView(content)
//                    .setCustomContentView(contentCompact);
//                    .setStyle(new NotificationCompat.DecoratedCustomViewStyle());


                    if (notificationManager == null)
                        notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                    assert notificationManager != null;
                    notificationManager.notify(NOTIFICATION_ID, trueBuilder.build());

                    lastUpdate = System.currentTimeMillis();
                    return null;
                }

                @Override
                public Object getInfo() {
                    return null;
                }
            },null);

            /*
            int textColor = Effects.getMonochromePercentage(accentColor) >= 0.5 ? Color.DKGRAY : Color.WHITE;
            Intent intent = new Intent(getApplicationContext(), MainActivity2020.class);
//            TaskStackBuilder stackBuilder = TaskStackBuilder.create(getApplicationContext());
//            stackBuilder.addParentStack(MainActivity2020.class);
//            stackBuilder.addNextIntent(intent);
//        stackBuilder.addNextIntentWithParentStack(intent);
//        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, 0);
            PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 96, intent, PendingIntent.FLAG_UPDATE_CURRENT);

            Intent intentPre = new Intent(getApplicationContext(), NewModelPlayerService.class);
            intentPre.setAction(ACTION_PREVIOUS);
            PendingIntent prePend = PendingIntent.getService(getApplicationContext(), 0, intentPre, 0);

            Intent intentPlay = new Intent(getApplicationContext(), NewModelPlayerService.class);
            intentPlay.setAction(ACTION_PLAY);
            PendingIntent playPend = PendingIntent.getService(getApplicationContext(), 0, intentPlay, 0);

            Intent intentNext = new Intent(getApplicationContext(), NewModelPlayerService.class);
            intentNext.setAction(ACTION_NEXT);
            PendingIntent nextPend = PendingIntent.getService(getApplicationContext(), 0, intentNext, 0);

            Intent intentStop = new Intent(getApplicationContext(), NewModelPlayerService.class);
            intentStop.setAction(ACTION_STOP);
            PendingIntent stopPend = PendingIntent.getService(getApplicationContext(), 0, intentStop, 0);


//            RemoteViews content = new RemoteViews(getPackageName(), R.layout.notification_layout_2020);
//////            if (art != null) {
//////                content.setImageViewBitmap(R.id.notification_cover_image, art);
//////            }
//////            if (currentTrack != null) {
//////                content.setTextViewText(R.id.notification_titile, getString(R.string.app_name_2020) + " | " + currentTrack.getTitle());
//////                content.setTextViewText(R.id.notification_album, currentTrack.getAlbumTitle());
//////                content.setTextViewText(R.id.notification_artist, currentTrack.getArtistTitle());
//////            }
//////            content.setTextColor(R.id.notification_titile, textColor);
//////            content.setTextColor(R.id.notification_album, textColor);
//////            content.setTextColor(R.id.notification_artist, textColor);
//////
//////
////////            ImageButton view;
////////            view.tint
//////            content.setInt(R.id.notification_main_layout, "setBackgroundColor", accentColor);
//////            content.setTextColor(R.id.notification_stop, accentColor);
//////            content.setImageViewBitmap(R.id.notification_cover_image_tint, gradientBitmap());
////////            content.setTextColor(R.id.notification_titile, accentColor);
//////            content.setOnClickPendingIntent(R.id.notification_pre, prePend);
//////            content.setOnClickPendingIntent(R.id.notification_play, playPend);
//////            content.setOnClickPendingIntent(R.id.notification_next, nextPend);
//////            content.setOnClickPendingIntent(R.id.notification_stop, stopPend);
////////            content.setImageViewResource(R.id.notification_stop, R.drawable.ic_close_black_24dp);
//////            content.setImageViewResource(R.id.notification_pre, R.drawable.ic_rewind_arrow);
//////            content.setImageViewResource(R.id.notification_play, playButton());
////////            content.setTextViewText(R.id.notification_play, playButton_2020());
//////            content.setImageViewResource(R.id.notification_next, R.drawable.ic_fast_forward_arrow);
//////            if (blur != null && false) {
//////                content.setImageViewBitmap(R.id.notification_background, blur);
//////            }
////////        content.setImageViewBitmap(R.id.notification_background, gradient());
//////
//////
//////            RemoteViews contentCompact = new RemoteViews(getPackageName(), R.layout.notification_layout_compact_2020);
//////            if (art != null) {
//////                contentCompact.setImageViewBitmap(R.id.notification_cover_image, art);
//////            }
//////            if (currentTrack != null) {
//////                contentCompact.setTextViewText(R.id.notification_titile, currentTrack.getTitle());
//////            }
//////
//////            contentCompact.setTextColor(R.id.notification_titile, textColor);
////////            content.setTextColor(R.id.notification_album, textColor);
////////            content.setTextColor(R.id.notification_artist, textColor);
//////
//////            contentCompact.setInt(R.id.notification_main_layout, "setBackgroundColor", accentColor);
//////            contentCompact.setTextColor(R.id.notification_stop, accentColor);
////////            contentCompact.setTextColor(R.id.notification_titile, accentColor);
//////            contentCompact.setOnClickPendingIntent(R.id.notification_pre, prePend);
//////            contentCompact.setOnClickPendingIntent(R.id.notification_play, playPend);
//////            contentCompact.setOnClickPendingIntent(R.id.notification_next, nextPend);
//////            contentCompact.setOnClickPendingIntent(R.id.notification_stop, stopPend);
////////            contentCompact.setImageViewResource(R.id.notification_stop, R.drawable.ic_close_black_24dp);
//////            contentCompact.setImageViewResource(R.id.notification_pre, R.drawable.ic_rewind_arrow);
//////            contentCompact.setImageViewResource(R.id.notification_play, playButton());
//////            contentCompact.setImageViewResource(R.id.notification_next, R.drawable.ic_fast_forward_arrow);
//////            if (blur != null && false) {
//////                contentCompact.setImageViewBitmap(R.id.notification_background, blur);
//////            }


//        contentCompact.setImageViewBitmap(R.id.notification_background, gradient());


//        Icon nextIcon = Icon.createWithResource(getApplicationContext(), R.drawable.ic_skip);
//        Notification.Action action = new Notification.Action.Builder(nextIcon, "Next", nextPend).build();


            androidx.media.app.NotificationCompat.MediaStyle style = new androidx.media.app.NotificationCompat.MediaStyle();
            style.setShowActionsInCompactView(0, 1, 2);
            style.setMediaSession(playerManager.getMediaSession().getSessionToken()).setCancelButtonIntent(stopPend).setShowCancelButton(true);
            androidx.core.app.NotificationCompat.Builder trueBuilder = new androidx.core.app.NotificationCompat.Builder(getApplicationContext(), channel_id)
                    .addAction(R.drawable.ic_round_fast_rewind_new_24, "Previous", prePend)
                    .addAction(playButton(), playButton_2020(), playPend)
                    .addAction(R.drawable.ic_round_fast_forward_new_24, "Next", nextPend)
                    .addAction(R.drawable.ic_round_close_24, "Stop", stopPend);
            if (currentTrack != null) {
                trueBuilder.setContentTitle(currentTrack.getTitle())
                        .setContentText(currentTrack.getArtistTitle());
            }
            trueBuilder.setColor(accentColor)
                    .setColorized(true)
                    .setLargeIcon(art)
                    .setStyle(style)
                    .setContentIntent(pendingIntent)
                    .setAllowSystemGeneratedContextualActions(true)
                    .setSmallIcon(smallIcon());
//                    .setCustomBigContentView(content)
//                    .setCustomContentView(contentCompact);


//            androidx.core.app.NotificationCompat.Builder trueBuilder = new androidx.core.app.NotificationCompat.Builder(getApplicationContext(), channel_id)
//                    .setContentIntent(pendingIntent)
//                    .setSmallIcon(smallIcon())
//                    .setCustomBigContentView(content)
//                    .setCustomContentView(contentCompact);
//                    .setStyle(new NotificationCompat.DecoratedCustomViewStyle());


            if (notificationManager == null)
                notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            assert notificationManager != null;
            notificationManager.notify(NOTIFICATION_ID, trueBuilder.build());

            lastUpdate = System.currentTimeMillis();
            */

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
        }
    }

    private int smallIcon() {
        int smallIcon;
        if (PlayerCommunicates.getINSTANCE().isPlaying()) {
            smallIcon = R.drawable.ic_play_circle_filled_black_24dp;
//            smallIcon = R.drawable.ic_play_button;
        } else {
            smallIcon = R.drawable.ic_pause_circle_filled_black_24dp;
//            smallIcon = R.drawable.ic_pause;
        }
        return smallIcon;
    }

    private Bitmap gradientBitmap() {
        Bitmap bitmap = Bitmap.createBitmap(100, 100, Bitmap.Config.ARGB_8888);
        gradientPaint.setShader(new LinearGradient(0, 0, 100, 0, accentColor, TRANSPARENT_WHITE_COLOR, Shader.TileMode.CLAMP));
        Canvas canvas = new Canvas(bitmap);
        canvas.drawPaint(gradientPaint);
        return bitmap;
    }

    private String playButton_2020() {
        String playIcon;
        if (PlayerCommunicates.getINSTANCE().isPlaying()) {
            playIcon = "Pause";
        } else {
            playIcon = "Play";
        }
        return playIcon;
    }

    private int playButton() {
        int playIcon;
        if (PlayerCommunicates.getINSTANCE().isPlaying()) {
            playIcon = R.drawable.ic_round_pause_24;
        } else {
            playIcon = R.drawable.ic_round_play_arrow_24;
        }
        return playIcon;
    }


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

    public void stop() {
//        BackImageChangeHandler.instance.removeCallback(this);
        isWorking = false;
        ThemeEngine.getINSTANCE().unRegisterCallback(themeCallback);
        stopForeground(true);
        stopSelf();
    }


    private Handler songFinishHandler = new Handler(Looper.getMainLooper(), new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
//            if (msg.arg1 == 2) {
//                MbnController.seek(getApplicationContext());
//            } else
//                MbnController.onComplete(getApplicationContext());
            return false;
        }
    });

    private Runnable checkForTimerRunnable = new Runnable() {
        @Override
        public void run() {
            checkForTimer();
            songFinishHandler.postDelayed(this, 5000);
        }
    };

    private BroadcastReceiver everyMinReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            checkForTimer();
        }
    };

    private void checkForTimer() {
        long diff = StorageUtils.getTimer(getApplicationContext()) - System.currentTimeMillis();
        if (diff > 0) return;
        diff = Math.abs(diff);
        StorageUtils.setTimer(getApplicationContext(), 0);
        if (diff / ONE_MIN < 2) {
            stop();
        }
    }


    @SuppressLint("ClickableViewAccessibility")
    private void makeOverlay() {

        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);

        int type;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        } else {

            type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
        }

        WindowManager.LayoutParams params = new WindowManager.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                type,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL | WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH,
                PixelFormat.TRANSLUCENT);

        params.gravity = Gravity.RIGHT | Gravity.CENTER;
        params.setTitle("Load Average");

        LayoutInflater inflater = (LayoutInflater) getBaseContext().getSystemService(LAYOUT_INFLATER_SERVICE);

        overlayView = inflater.inflate(R.layout.overlay_layout, null);

        ImageView next = overlayView.findViewById(R.id.overlay_next);
        next.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.skip_png));
        ImageView previous = overlayView.findViewById(R.id.overlay_previous);
        previous.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.previous_png));
        ImageView playPause = overlayView.findViewById(R.id.overlay_play_pause);
        playPause.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.play_pause_png));


        ImageView moveBar = overlayView.findViewById(R.id.overlay_move_bar);
        moveBar.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_action_move_remote_layout));
        moveBar.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_MOVE) {
                    Point point = new Point();
                    windowManager.getDefaultDisplay().getSize(point);
//                    params.x = (int) motionEvent.getRawX() - point.x / 2;
                    params.y = ((int) motionEvent.getRawY() - point.y / 2) + ((overlayView.getHeight() / 2) - view.getHeight() / 2);
                    windowManager.updateViewLayout(overlayView, params);
                }
                return true;
            }
        });


        View.OnClickListener clickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int id = view.getId();
                switch (id) {
                    case R.id.overlay_next:
                        DataBaseManager.getManager().performNext();
                        break;
                    case R.id.overlay_previous:
                        PlayerCommunicates.getINSTANCE().checkForPrevious();
                        break;
                    case R.id.overlay_play_pause:
                        if (PlayerCommunicates.getINSTANCE().isPlaying()) {
                            PlayerCommunicates.getINSTANCE().pause();
                        } else {
                            PlayerCommunicates.getINSTANCE().play();
                        }
                    default:
//                        overlayView.setTranslationX(0);
                        break;
                }


            }
        };

        next.setOnClickListener(clickListener);
        playPause.setOnClickListener(clickListener);
        previous.setOnClickListener(clickListener);
        moveBar.setOnClickListener(clickListener);
        windowManager.addView(overlayView, params);
//        float slideSize;
//        slideSize = getResources().getDisplayMetrics().density * 150;
//        params.x
//        windowManager.updateViewLayout(overlayView,params);
    }

    private void removeOverLay() {

        try {
            windowManager.removeView(overlayView);
        } catch (Exception e) {
            e.printStackTrace();
        }


    }


    @Override
    public void onCurrentPosChange(long pos) {
        if ((System.currentTimeMillis() - lastUpdate) >= 1000)
            makeNotification(false);
    }

    @Override
    public void onPlayerStateChange(boolean state) {

    }
}
