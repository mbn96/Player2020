package com.br.mreza.musicplayer.newmodel.service.player;


import android.content.Context;
import android.media.AudioAttributes;
import android.media.AudioDeviceInfo;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.audiofx.BassBoost;
import android.media.audiofx.Equalizer;
import android.media.audiofx.PresetReverb;
import android.os.Handler;
import android.os.Looper;
import android.os.PowerManager;
import android.support.v4.media.session.MediaSessionCompat;
import android.util.Log;

import com.br.mreza.musicplayer.DataSong;
import com.br.mreza.musicplayer.StorageUtils;
import com.br.mreza.musicplayer.newmodel.database.A_B_Manager;
import com.br.mreza.musicplayer.newmodel.database.DataBaseManager;
import com.br.mreza.musicplayer.p2020.visualizer.VisualizerManager;

import mbn.libs.backgroundtask.BaseTaskHolder;

public abstract class PlayerManager implements MediaPlayer.OnCompletionListener, AudioManager.OnAudioFocusChangeListener, MediaPlayer.OnPreparedListener, A_B_Manager.A_B_Interface {

    private MediaPlayer mediaPlayer;
    private boolean shouldStart = true;
    private Context context;
    private DataSong currentTrack;
    private NewModelSelfUpdateMediaSession mediaSession;
    private AudioManager audioManager;
    private int audioSessionID;
    private A_B_Manager.A_B_Object a_b_object;

    private Equalizer effect;
//    private BassBoost effect;


    @Override
    public void stateChanged(A_B_Manager.A_B_Object a_b_object) {
        this.a_b_object = a_b_object;
    }

    @Override
    public void posChanged(A_B_Manager.A_B_Object a_b_object) {
        this.a_b_object = a_b_object;
    }

    private BaseTaskHolder.ResultReceiver initGetReceiver = new BaseTaskHolder.ResultReceiver() {
        @Override
        public void onResult(Object result, Object info) {
//            currentTrack = (DataSong) ((Object[]) result)[1];
//            start();
//            mediaSession.setSong(currentTrack);
//            changeNotification(currentTrack);
            setCurrentSong((DataSong) ((Object[]) result)[1]);
        }
    };

    private DataBaseManager.DefaultCallback dataBaseCallback = new DataBaseManager.DefaultCallback(true, false) {
        @Override
        public void onTrackChange(long id, DataSong song) {
//            currentTrack = song;
//            start();
//            mediaSession.setSong(currentTrack);
//            changeNotification(currentTrack);
            setCurrentSong(song);
        }
    };

    private void setCurrentSong(DataSong song) {
        currentTrack = song;
        PlayerCommunicates.getINSTANCE().songChanged(song.getId());
        start();
        mediaSession.setSong(currentTrack);
        changeNotification(currentTrack);
    }

    private Handler handler = new Handler(Looper.getMainLooper());

    private Runnable currentPosFinder = new Runnable() {
        private long pos;

        @Override
        public void run() {
            if (isPlaying()) {
                PlayerCommunicates.getINSTANCE().setCurrentPos(pos = getCurrentPos());
                mediaSession.update(pos);
//                changeNotification(currentTrack);
                if (a_b_object.isState() && a_b_object.isaState() && a_b_object.isbState()) {
                    if (getCurrentPos() >= a_b_object.getB()) {
                        PlayerCommunicates.getINSTANCE().seek(a_b_object.getA());
                    }
                }
            }
            handler.postDelayed(this, 300);
        }
    };

    private int generateAudioSessionID() {
        int id = 0;
        int tries = 0;
        while ((id <= 0) && tries++ < 5) {
            id = audioManager.generateAudioSessionId();
        }
        return id > 0 ? id : 0;
    }

    public PlayerManager(Context context, AudioManager audioManager) {
        this.context = context;
        this.audioManager = audioManager;
        audioSessionID = generateAudioSessionID();

        VisualizerManager.INSTANCE.setSession(true, audioSessionID);

        effect = new Equalizer(0, audioSessionID);
        effect.usePreset((short) 0);
//        for (int i = 0; i < effect.getNumberOfPresets(); i++) {
//            Log.i("EFprst", "PlayerManager: " + effect.getPresetName((short) i));
//        }
        effect.setEnabled(true);

        mediaSession = new NewModelSelfUpdateMediaSession(context, "mediaSes", audioSessionID);
        mediaSession.setCallback(mediaSessionCallbacks);
        mediaSession.setFlags(MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS | MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS);
        mediaSession.setActive(true);

        DataBaseManager.getManager().registerCallback(dataBaseCallback);
        PlayerCommunicates.getINSTANCE().registerPlayerManager(this);
        DataBaseManager.getManager().getCurrentQueueAndTrack(initGetReceiver);
        A_B_Manager.INSTANCE.request(A_B_Manager.SET_AGENT, 0, this);
        handler.post(currentPosFinder);
    }

    public abstract void changeNotification(DataSong song);

    private boolean requestAudioFocus() {
        assert audioManager != null;
        int result = audioManager.requestAudioFocus(this, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN);
        if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
            //Focus gained
            return true;
        }
        //Could not gain focus
        return false;
    }

    private boolean removeAudioFocus() {
        return AudioManager.AUDIOFOCUS_REQUEST_GRANTED ==
                audioManager.abandonAudioFocus(this);
    }

    public void release() {
        A_B_Manager.INSTANCE.unregister(this);
        removeAudioFocus();
        PlayerCommunicates.getINSTANCE().setPlayingState(false);
        tryPausing();
        VisualizerManager.INSTANCE.setSession(false, audioSessionID);
        mediaSession.update();
        mediaPlayer.release();
        mediaSession.release();
        DataBaseManager.getManager().unRegisterCallback(dataBaseCallback);
        PlayerCommunicates.getINSTANCE().releasePlayerManager();
        handler.removeCallbacks(currentPosFinder);
    }

    public NewModelSelfUpdateMediaSession getMediaSession() {
        return mediaSession;
    }

    boolean isPlaying() {
        try {
            return mediaPlayer.isPlaying();
        } catch (Exception ignored) {
        }
        return false;
    }

    void tryStarting() {
        shouldStart = true;
        start();
    }

    void tryPausing() {
        shouldStart = false;
        if (isPlaying()) {
            StorageUtils.setStartFromPos(context, getCurrentPos());
            mediaPlayer.pause();
            mediaPlayer.stop();
            PlayerCommunicates.getINSTANCE().setPlayingState(false);
            mediaSession.update();
            changeNotification(currentTrack);
        }
    }

    void seek(long pos) {
        mediaPlayer.seekTo((int) pos);
    }

    void rewind() {
        seek(getCurrentPos() - 5000);
    }

    void fastForward() {
        seek(getCurrentPos() + 5000);
    }

    long getCurrentPos() {
        if (isPlaying()) return mediaPlayer.getCurrentPosition();
        else return 0;
    }


    private void start() {
        if (!shouldStart || currentTrack == null) {
            return;
        }
        if (requestAudioFocus()) {
            resetPlayer();
            try {
                mediaPlayer.setDataSource(currentTrack.getPath());
//                mediaPlayer.prepare();
                mediaPlayer.prepareAsync();
//                mediaPlayer.seekTo((int) StorageUtils.getStartFromPos(context));
//                mediaPlayer.start();
//                PlayerCommunicates.getINSTANCE().setPlayingState(true);
//                mediaSession.update();
//                changeNotification(currentTrack);
            } catch (Exception ignored) {
            }
        }
    }

    private void resetPlayer() {
        if (mediaPlayer == null) {
            mediaPlayer = new MediaPlayer();
            preparePlayer(mediaPlayer);

            /*
            if (audioSessionID > 0) mediaPlayer.setAudioSessionId(audioSessionID);
            mediaPlayer.setOnCompletionListener(this);
            mediaPlayer.setOnPreparedListener(this);
            mediaPlayer.setWakeMode(context, PowerManager.PARTIAL_WAKE_LOCK);
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            */
            return;
        }
        try {
            mediaPlayer.reset();
            preparePlayer(mediaPlayer);
//            if (audioSessionID > 0) mediaPlayer.setAudioSessionId(audioSessionID);
        } catch (Exception e) {
            mediaPlayer = null;
            resetPlayer();
        }
    }

    private void preparePlayer(MediaPlayer player) {
        if (audioSessionID > 0) player.setAudioSessionId(audioSessionID);
        player.setOnCompletionListener(this);
        player.setOnPreparedListener(this);
        player.setWakeMode(context, PowerManager.PARTIAL_WAKE_LOCK);
        player.setAudioStreamType(AudioManager.STREAM_MUSIC);
        AudioAttributes.Builder builder = new AudioAttributes.Builder();
        builder.setContentType(AudioAttributes.CONTENT_TYPE_MUSIC).setUsage(AudioAttributes.USAGE_MEDIA);
        player.setAudioAttributes(builder.build());
//        player.attachAuxEffect(presetReverbEffect.getId());
//        player.setAuxEffectSendLevel(0.5f);
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        if (shouldStart) {
            mp.seekTo((int) StorageUtils.getStartFromPos(context));
            mp.start();
            PlayerCommunicates.getINSTANCE().setPlayingState(true);
            mediaSession.update();
            changeNotification(currentTrack);
        }
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        if (StorageUtils.isRepeat(context)) {
            StorageUtils.setStartFromPos(context, 0);
            tryStarting();
        } else {
            DataBaseManager.getManager().performNext();
        }
    }

    private MediaSessionCompat.Callback mediaSessionCallbacks = new MediaSessionCompat.Callback() {
        @Override
        public void onPlay() {
            tryStarting();
        }

        @Override
        public void onPause() {
            tryPausing();
        }


        @Override
        public void onSkipToNext() {
            DataBaseManager.getManager().performNext();
        }

        @Override
        public void onSkipToPrevious() {
            PlayerCommunicates.getINSTANCE().checkForPrevious();
        }

        @Override
        public void onRewind() {
            PlayerCommunicates.getINSTANCE().rewind();
        }

        @Override
        public void onFastForward() {
            PlayerCommunicates.getINSTANCE().fastForward();
        }

        @Override
        public void onStop() {
        }

        @Override
        public void onSeekTo(long pos) {
            PlayerCommunicates.getINSTANCE().seek(pos);
        }
    };


    @Override
    public void onAudioFocusChange(int focusState) {
        //Invoked when the audio focus of the system is updated.
        switch (focusState) {
            case AudioManager.AUDIOFOCUS_GAIN:
                // resume playback
                tryStarting();
                break;
            case AudioManager.AUDIOFOCUS_LOSS:
                // Lost focus for an unbounded amount of time: stop playback and release media player
                tryPausing();
                break;
            case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT:
                // Lost focus for a short time, but we have to stop
                // playback. We don't release the media player because playback
                // is likely to resume
                tryPausing();
                break;
            case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK:
                // Lost focus for a short time, but it's ok to keep playing
                // at an attenuated level
//                try {
//                    if (mediaPlayer != null && mediaPlayer.isPlaying()) {
//                        mediaPlayer.setVolume(0.1f, 0.1f);
//                    }
//                } catch (Exception ignored) {
//                }
                break;
        }
    }

}
