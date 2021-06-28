package mbn.libs.media;

import android.app.PendingIntent;
import android.content.Context;
import android.media.AudioAttributes;
import android.media.MediaMetadata;
import android.media.VolumeProvider;
import android.media.session.MediaController;
import android.media.session.MediaSession;
import android.media.session.MediaSessionManager;
import android.media.session.PlaybackState;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import java.util.List;

public class ExtendableMediaSession {
    private MediaSession mediaSession;

    public ExtendableMediaSession(Context context, String tag) {
        mediaSession = new MediaSession(context, tag);
    }

    public void setCallback(@Nullable MediaSession.Callback callback) {
        mediaSession.setCallback(callback);
    }

    public void setCallback(@Nullable MediaSession.Callback callback, @Nullable Handler handler) {
        mediaSession.setCallback(callback, handler);
    }

    public void setSessionActivity(@Nullable PendingIntent pi) {
        mediaSession.setSessionActivity(pi);
    }

    public void setMediaButtonReceiver(@Nullable PendingIntent mbr) {
        mediaSession.setMediaButtonReceiver(mbr);
    }

    public void setFlags(int flags) {
        mediaSession.setFlags(flags);
    }

    public void setPlaybackToLocal(AudioAttributes attributes) {
        mediaSession.setPlaybackToLocal(attributes);
    }

    public void setPlaybackToRemote(@NonNull VolumeProvider volumeProvider) {
        mediaSession.setPlaybackToRemote(volumeProvider);
    }

    public void setActive(boolean active) {
        mediaSession.setActive(active);
    }

    public boolean isActive() {
        return mediaSession.isActive();
    }

    public void sendSessionEvent(@NonNull String event, @Nullable Bundle extras) {
        mediaSession.sendSessionEvent(event, extras);
    }

    public void release() {
        mediaSession.release();
    }

    public MediaSession.Token getSessionToken() {
        return mediaSession.getSessionToken();
    }

    public MediaController getController() {
        return mediaSession.getController();
    }

    public void setPlaybackState(@Nullable PlaybackState state) {
        mediaSession.setPlaybackState(state);
    }

    public void setMetadata(@Nullable MediaMetadata metadata) {
        mediaSession.setMetadata(metadata);
    }

    public void setQueue(@Nullable List<MediaSession.QueueItem> queue) {
        mediaSession.setQueue(queue);
    }

    public void setQueueTitle(@Nullable CharSequence title) {
        mediaSession.setQueueTitle(title);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP_MR1)
    public void setRatingType(int type) {
        mediaSession.setRatingType(type);
    }

    public void setExtras(@Nullable Bundle extras) {
        mediaSession.setExtras(extras);
    }


    @RequiresApi(api = Build.VERSION_CODES.P)
    public MediaSessionManager.RemoteUserInfo getCurrentControllerInfo() {
        return mediaSession.getCurrentControllerInfo();
    }
}
