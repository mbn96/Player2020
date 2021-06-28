//package com.br.mreza.musicplayer.NewService;
//
//import android.app.PendingIntent;
//import android.content.ComponentName;
//import android.content.Context;
//import android.content.Intent;
//import android.graphics.Bitmap;
//import android.support.v4.media.MediaMetadataCompat;
//import android.support.v4.media.RatingCompat;
//import android.support.v4.media.session.MediaSessionCompat;
//import android.support.v4.media.session.PlaybackStateCompat;
//
//import com.br.mreza.musicplayer.BackgroundForUI.BackImageChangeHandler;
//import com.br.mreza.musicplayer.ListMaker;
//import com.br.mreza.musicplayer.MbnController;
//import com.br.mreza.musicplayer.StorageUtils;
//
//
//public class SelfUpdateMediaSession extends MediaSessionCompat implements BackImageChangeHandler.ImageChangeCallback {
//
//    public SelfUpdateMediaSession(Context context, String tag) {
//        super(context, tag);
//        init(context);
//    }
//
//    public SelfUpdateMediaSession(Context context, String tag, ComponentName mbrComponent, PendingIntent mbrIntent) {
//        super(context, tag, mbrComponent, mbrIntent);
//        setRatingType(RatingCompat.RATING_HEART);
//        init(context);
//    }
//
//    private void init(Context context) {
//        BackImageChangeHandler.instance.addCallback(this);
//        BackImageChangeHandler.instance.force(context);
//        MbnController.mToken = getSessionToken();
//        Intent intent = new Intent(context, NewPlayerService.class);
//        PendingIntent pendingIntent;
//        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
//            pendingIntent = PendingIntent.getForegroundService(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
//        } else {
//            pendingIntent = PendingIntent.getService(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
//        }
//        setMediaButtonReceiver(pendingIntent);
//    }
//
//    @Override
//    public void release() {
//        BackImageChangeHandler.instance.removeCallback(this);
//        super.release();
//    }
//
//
//    @Override
//    public void onChange(Bitmap original, Bitmap blur) {
//        update(original, blur);
//    }
//
//    private void update(Bitmap original, Bitmap blur) {
//        MediaMetadataCompat.Builder mBi = new MediaMetadataCompat.Builder();
//        if (StorageUtils.isShowAlbumArt()) {
//            if (StorageUtils.isShowBlurAlbumArt())
//                mBi.putBitmap(MediaMetadataCompat.METADATA_KEY_ALBUM_ART, blur);
//            else mBi.putBitmap(MediaMetadataCompat.METADATA_KEY_ALBUM_ART, original);
//        }
//        mBi.putString(MediaMetadataCompat.METADATA_KEY_TITLE, ListMaker.getCurrentTrack().getTitle());
//        setMetadata(mBi.build());
//
//        PlaybackStateCompat.Builder builder = new PlaybackStateCompat.Builder();
//        builder.setState(getState(), MusicInfoHolder.getCurrentPos(), 1)
//                .setActions(PlaybackStateCompat.ACTION_PAUSE | PlaybackStateCompat.ACTION_PLAY |
//                        PlaybackStateCompat.ACTION_SKIP_TO_NEXT | PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS);
//        setPlaybackState(builder.build());
//
//
//    }
//
//    private int getState() {
//        return MbnController.playerState ? PlaybackStateCompat.STATE_PLAYING : PlaybackStateCompat.STATE_PAUSED;
//    }
//
//}
