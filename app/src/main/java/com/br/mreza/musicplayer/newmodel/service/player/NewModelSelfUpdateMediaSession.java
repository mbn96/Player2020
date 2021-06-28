package com.br.mreza.musicplayer.newmodel.service.player;

import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.SystemClock;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.RatingCompat;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;

import com.br.mreza.musicplayer.DataSong;
import com.br.mreza.musicplayer.StorageUtils;
import com.br.mreza.musicplayer.newmodel.service.NewModelPlayerService;
import com.br.mreza.musicplayer.newmodel.theme.ThemeEngine;
import com.br.mreza.musicplayer.p2020.visualizer.VisualizerManager;


public class NewModelSelfUpdateMediaSession extends MediaSessionCompat implements PlayerCommunicates.PlayerCallbacks {

    private ThemeEngine.DefaultThemeCallback themeCallback = new ThemeEngine.DefaultThemeCallback(true, true, false) {

        @Override
        public void onMainBitmap(Bitmap main) {
            original = main;

        }

        @Override
        public void onBlurBitmap(Bitmap blurIN) {
            blur = blurIN;

        }

        @Override
        public void onProcessFinished() {
            update();
        }
    };


    private Bitmap original, blur;
    private DataSong song;
    private int audioSessionID;
    private long pos;

    public NewModelSelfUpdateMediaSession(Context context, String tag, int audioSessionID) {
        super(context, tag);
        setAudioSessionID(audioSessionID);
        init(context);
    }

    public NewModelSelfUpdateMediaSession(Context context, String tag, ComponentName mbrComponent, PendingIntent mbrIntent) {
        super(context, tag, mbrComponent, mbrIntent);
        setRatingType(RatingCompat.RATING_HEART);
        init(context);
    }

    public void setAudioSessionID(int audioSessionID) {
        this.audioSessionID = audioSessionID;
    }

    private void init(Context context) {
//        BackImageChangeHandler.instance.addCallback(this);
//        BackImageChangeHandler.instance.force(context);
        ThemeEngine.getINSTANCE().registerCallback(themeCallback);
        ThemeEngine.getINSTANCE().getResult(themeCallback);
//        PlayerCommunicates.getINSTANCE().registerCallback(this);
//        VisualizerManager.INSTANCE.setSession(true, audioSessionID);
        Intent intent = new Intent(context, NewModelPlayerService.class);
        PendingIntent pendingIntent;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            pendingIntent = PendingIntent.getForegroundService(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        } else {
            pendingIntent = PendingIntent.getService(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        }
        setMediaButtonReceiver(pendingIntent);
    }

    @Override
    public void release() {
//        BackImageChangeHandler.instance.removeCallback(this);
        ThemeEngine.getINSTANCE().unRegisterCallback(themeCallback);
//        PlayerCommunicates.getINSTANCE().unRegisterCallback(this);
//        VisualizerManager.INSTANCE.setSession(false, audioSessionID);
        super.release();
    }

    public void setSong(DataSong song) {
        this.song = song;
        update();
    }

//    @Override
//    public void onChange(Bitmap original, Bitmap blur) {
//        this.original = original;
//        this.blur = blur;
//        update();
//    }

    void update() {
        update(pos);
    }

    void update(long pos) {
        this.pos = pos;
        if (song != null) {
            MediaMetadataCompat.Builder mBi = new MediaMetadataCompat.Builder();
            if (StorageUtils.isShowAlbumArt() && (original != null && blur != null)) {
                if (StorageUtils.isShowBlurAlbumArt())
                    mBi.putBitmap(MediaMetadataCompat.METADATA_KEY_ALBUM_ART, blur);
                else mBi.putBitmap(MediaMetadataCompat.METADATA_KEY_ALBUM_ART, original);
            }
            mBi.putString(MediaMetadataCompat.METADATA_KEY_TITLE, song.getTitle());
            mBi.putLong(MediaMetadataCompat.METADATA_KEY_DURATION, song.getDuration());
            setMetadata(mBi.build());

            PlaybackStateCompat.Builder builder = new PlaybackStateCompat.Builder();
            builder.setState(getState(), pos, 1, SystemClock.elapsedRealtime())
                    .setActions(PlaybackStateCompat.ACTION_PAUSE | PlaybackStateCompat.ACTION_PLAY |
                            PlaybackStateCompat.ACTION_SKIP_TO_NEXT | PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS |
                            PlaybackStateCompat.ACTION_SEEK_TO | PlaybackStateCompat.ACTION_FAST_FORWARD | PlaybackStateCompat.ACTION_REWIND);
            setPlaybackState(builder.build());


        }
    }

    private int getState() {
        return PlayerCommunicates.getINSTANCE().isPlaying() ? PlaybackStateCompat.STATE_PLAYING : PlaybackStateCompat.STATE_PAUSED;
    }

    @Override
    public void onCurrentPosChange(long pos) {
//        this.pos = pos;
//        update();
    }

    @Override
    public void onPlayerStateChange(boolean state) {

    }
}
