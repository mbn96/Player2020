//package com.br.mreza.musicplayer;
//
//
//import android.annotation.SuppressLint;
//import android.content.Context;
//import android.support.v4.media.MediaMetadataCompat;
//import android.support.v4.media.session.MediaControllerCompat;
//import android.support.v4.media.session.MediaControllerCompat.TransportControls;
//import android.support.v4.media.session.MediaSessionCompat;
//import android.support.v4.media.session.PlaybackStateCompat;
//
//import com.br.mreza.musicplayer.NewService.SelfUpdateMediaSession;
//
//public class MediaSessionHolder {
//
//
//    public static VisyHelper visyHelper = new VisyHelper();
//
//    private static SelfUpdateMediaSession mediaSessionCompat;
//    private static MediaControllerCompat mediaController;
//    private static TransportControls transportControls;
//
//
//    private MediaSessionHolder() {
//    }
//
//
//    public synchronized static MediaSessionCompat getMediaSession(Context context) {
//
//        if (mediaSessionCompat == null) {
//
//            mediaSessionCompat = new SelfUpdateMediaSession(context, "mbnP");
//            mediaSessionCompat.setFlags(MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS | MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS);
//            mediaController = mediaSessionCompat.getController();
//            transportControls = mediaController.getTransportControls();
//
////            PlaybackStateCompat.Builder builder = new PlaybackStateCompat.Builder();
////            builder.setState(PlaybackStateCompat.STATE_PLAYING, 5000, 1);
////            mediaSessionCompat.setPlaybackState(builder.build());
////            MediaMetadataCompat.Builder mBi = new MediaMetadataCompat.Builder();
////            mBi.putBitmap(MediaMetadataCompat.METADATA_KEY_ALBUM_ART, MbnController.getCover(context, ListMaker.currentTrack.getPath(), 1));
////            mBi.putString(MediaMetadataCompat.METADATA_KEY_TITLE, ListMaker.currentTrack.getTitle());
////            mediaSessionCompat.setMetadata(mBi.build());
//
//        }
//
//        return mediaSessionCompat;
//    }
//
//    public synchronized static void reset(Context context) {
//        if (mediaSessionCompat != null) {
//            try {
//                mediaSessionCompat.release();
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//        mediaSessionCompat = null;
//        getMediaSession(context);
//    }
//
//    public synchronized static TransportControls getControls(Context context) {
//
//        if (transportControls == null) {
//
//            try {
//                mediaController = new MediaControllerCompat(context, getMediaSession(context).getSessionToken());
//                transportControls = mediaController.getTransportControls();
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//
//
//        }
//
//        return transportControls;
//    }
//
//    public synchronized static MediaControllerCompat getMediaController() {
//        return mediaController;
//    }
//}
