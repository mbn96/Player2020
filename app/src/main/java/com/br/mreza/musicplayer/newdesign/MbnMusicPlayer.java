package com.br.mreza.musicplayer.newdesign;

import android.Manifest;
import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.AudioAttributes;
import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;

import com.br.mreza.musicplayer.DataBaseHolder;
import com.br.mreza.musicplayer.MBN.bitmapUtils.MbnUtils;
import com.br.mreza.musicplayer.R;
import com.br.mreza.musicplayer.newmodel.artwork.ArtworkDownloader;
import com.br.mreza.musicplayer.newmodel.database.DataBaseManager;
import com.br.mreza.musicplayer.newmodel.database.DataBaseUtils;
import com.br.mreza.musicplayer.newmodel.service.player.PlayerCommunicates;
import com.br.mreza.musicplayer.newmodel.theme.ThemeEngine;

import mbn.libs.UI.ThemeCoordinator;
import mbn.libs.io.DownloadManager;


public class MbnMusicPlayer extends Application {

    public static String channel_id;

    public static final ThemeCoordinator.DefaultThemeCoordinator THEME_COORDINATOR =
            new ThemeCoordinator.DefaultThemeCoordinator(Color.WHITE, 0xff304050, Color.BLACK, Color.DKGRAY);

//    public static final ThemeCoordinator.DefaultThemeCoordinator THEME_COORDINATOR =
//            new ThemeCoordinator.DefaultThemeCoordinator(0xff304050, 0xff304050, Color.WHITE, Color.LTGRAY);

    @Override
    public void onCreate() {
        super.onCreate();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            makeNotifyChannel();
        }

//        MusicInfoHolder.setSeekPos(StorageUtils.getStartFromPos(getApplicationContext()));
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(getResources(), R.drawable.mbn_back_dr, options);
        options.inSampleSize = Math.max(options.outHeight, options.outWidth) / 500;
        options.inJustDecodeBounds = false;
        options.inMutable = true;
        DataBaseUtils.defaultArt = MbnUtils.roundedBitmap(MbnUtils.createSmallBit(BitmapFactory.decodeResource(getResources(), R.drawable.mbn_back_dr, options), 300));
        DataBaseUtils.defaultArt_blur = BitmapFactory.decodeResource(getResources(), R.drawable.blurred_1_10);


        DataBaseHolder.getInstance(getApplicationContext());
        DownloadManager.startManager(getApplicationContext());
        ArtworkDownloader.initArtworkDownloader(getApplicationContext());
        PlayerCommunicates.getINSTANCE().init(getApplicationContext());
        DataBaseManager.getManager().init(getApplicationContext());
        ThemeEngine.getINSTANCE().init(getApplicationContext());

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            Intent intent = new Intent(getApplicationContext(), PermissionActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            return;
        }

        while (!DataBaseManager.getManager().startDataBase()) {
            DataBaseManager.getManager().init(getApplicationContext());
        }

//        MediaSessionHolder.getMediaSession(getApplicationContext());

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private String makeNotifyChannel() {

        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        channel_id = getPackageName();
        CharSequence channelName = "MediaControl";
        int importance = NotificationManager.IMPORTANCE_LOW;


        NotificationChannel channel = new NotificationChannel(channel_id, channelName, importance);
        channel.enableVibration(false);
        if (manager != null) {
            manager.createNotificationChannel(channel);
        }

        return channel_id;
    }

}
