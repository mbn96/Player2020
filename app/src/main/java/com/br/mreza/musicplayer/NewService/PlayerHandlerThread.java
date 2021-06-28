//package com.br.mreza.musicplayer.NewService;
//
//import android.content.Context;
//import android.media.AudioManager;
//import android.media.MediaPlayer;
//import android.os.Build;
//import android.os.Handler;
//import android.os.HandlerThread;
//import android.os.Looper;
//import android.os.PowerManager;
//import android.util.Log;
//
//import com.br.mreza.musicplayer.MbnController;
//
//import static android.content.ContentValues.TAG;
//import static com.br.mreza.musicplayer.ListMaker.getCurrentTrack;
//
//
//public class PlayerHandlerThread implements AudioManager.OnAudioFocusChangeListener,
//        MediaPlayer.OnPreparedListener, MediaPlayer.OnCompletionListener, MediaPlayer.OnSeekCompleteListener {
//    private MediaPlayer mediaPlayer;
//    private HandlerThread thread;
//    private PlayerHandler handler;
//    private AudioManager audioManager;
//    private Context context;
//    private Handler onFinishCallback;
//    private boolean seekInOrder = false;
//    private boolean released = false;
//
//    PlayerHandlerThread(AudioManager audioManager, Context context, Handler onFinishCallback) {
//        this.context = context;
//        this.audioManager = audioManager;
//        this.onFinishCallback = onFinishCallback;
//        thread = new HandlerThread("mThread", HandlerThread.NORM_PRIORITY);
//        thread.start();
//        handler = new PlayerHandler(thread.getLooper());
//        handler.post(currentPosFinder);
//    }
//
//
//    private boolean requestAudioFocus() {
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
//    public void play(boolean seek) {
//        if (requestAudioFocus())
//            if (seek) handler.post(playWithSeekRunnable);
//            else {
////                handler.post(pauseRunnable);
//                handler.post(playRunnable);
//            }
//    }
//
//    public void pause() {
//        handler.post(pauseRunnable);
////        removeAudioFocus();
//    }
//
//    public void seek() {
//        handler.post(seekToRunnable);
//    }
//
//    public void release() {
//        removeAudioFocus();
//        released = true;
//        handler.removeCallbacks(currentPosFinder);
//        handler.post(pauseRunnable);
//        thread.quitSafely();
//    }
//
//
//    private class PlayerHandler extends Handler {
//        private PlayerHandler(Looper looper) {
//            super(looper);
//        }
//    }
//
//
//    private Runnable currentPosFinder = new Runnable() {
//        @Override
//        public void run() {
//            try {
//                if (mediaPlayer != null && mediaPlayer.isPlaying()) {
//                    MusicInfoHolder.setCurrentPos(mediaPlayer.getCurrentPosition());
//                    if ((!seekInOrder) && MusicInfoHolder.hasA() && MusicInfoHolder.hasB() && MusicInfoHolder.getPosB() <= MusicInfoHolder.getCurrentPos()) {
//                        MusicInfoHolder.setSeekPos(MusicInfoHolder.getPosA());
////                        Message message = Message.obtain(onFinishCallback);
////                        message.arg1 = 2;
////                        message.sendToTarget();
//                        handler.post(seekToRunnable);
//                        seekInOrder = true;
//                    }
//                }
//            } catch (Exception ignored) {
//            }
//            if (!released)
//                handler.postDelayed(this, 500);
//        }
//    };
//
//
//    private Runnable playRunnable = new Runnable() {
//        @Override
//        public void run() {
//            try {
//                if (mediaPlayer != null && mediaPlayer.isPlaying()) {
//                    mediaPlayer.stop();
//                    mediaPlayer.release();
//                }
//            } catch (Exception ignored) {
//            }
//            mediaPlayer = null;
//            mediaPlayer = new MediaPlayer();
//            mediaPlayer.setOnPreparedListener(PlayerHandlerThread.this);
//            mediaPlayer.setOnCompletionListener(PlayerHandlerThread.this);
//            mediaPlayer.setOnSeekCompleteListener(PlayerHandlerThread.this);
//            mediaPlayer.setWakeMode(context, PowerManager.PARTIAL_WAKE_LOCK);
//            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
//            try {
//                mediaPlayer.setAudioSessionId(audioManager.generateAudioSessionId());
//                mediaPlayer.setDataSource(getCurrentTrack().getPath());
//                mediaPlayer.prepare();
//                mediaPlayer.start();
//                Log.i(TAG, "PlayRun: " + mediaPlayer.isPlaying());
//                MusicInfoHolder.setDuration(mediaPlayer.getDuration());
////                float v = 0;
////                while ((v += 0.01) <= 1) {
////                    mediaPlayer.setVolume(v, v);
////                }
//            } catch (Exception ignored) {
//
//            }
//        }
//    };
//
//    private Runnable pauseRunnable = new Runnable() {
//        @Override
//        public void run() {
//            try {
//                if (mediaPlayer != null && mediaPlayer.isPlaying()) {
//                    MusicInfoHolder.setSeekPos(mediaPlayer.getCurrentPosition(), context);
////                    float v = 1;
////                    while ((v -= 0.002) > 0) {
////                        mediaPlayer.setVolume(v, v);
////                    }
//                    mediaPlayer.stop();
//                    mediaPlayer.release();
//                }
//            } catch (Exception ignored) {
//            }
//        }
//    };
//
//    private Runnable playWithSeekRunnable = new Runnable() {
//        @Override
//        public void run() {
//            if (mediaPlayer != null) {
//                try {
//                    mediaPlayer.stop();
//                    mediaPlayer.release();
//                } catch (IllegalStateException ignored) {
//                }
//            }
//            mediaPlayer = null;
//            mediaPlayer = new MediaPlayer();
//            mediaPlayer.setOnPreparedListener(PlayerHandlerThread.this);
//            mediaPlayer.setOnCompletionListener(PlayerHandlerThread.this);
//            mediaPlayer.setOnSeekCompleteListener(PlayerHandlerThread.this);
//            mediaPlayer.setWakeMode(context, PowerManager.PARTIAL_WAKE_LOCK);
//            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
//            try {
//                mediaPlayer.setAudioSessionId(audioManager.generateAudioSessionId());
//                mediaPlayer.setDataSource(getCurrentTrack().getPath());
//                mediaPlayer.prepare();
//                mediaPlayer.seekTo((int) MusicInfoHolder.getSeekPos());
//                mediaPlayer.start();
//                MusicInfoHolder.setDuration(mediaPlayer.getDuration());
////                float v = 0;
////                while ((v += 0.01) <= 1) {
////                    mediaPlayer.setVolume(v, v);
////                }
//            } catch (Exception ignored) {
//            }
//        }
//    };
//
//    private Runnable seekToRunnable = new Runnable() {
//        @Override
//        public void run() {
//            try {
//                if (mediaPlayer != null && mediaPlayer.isPlaying()) {
//                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                        mediaPlayer.seekTo((int) MusicInfoHolder.getSeekPos(), MediaPlayer.SEEK_CLOSEST_SYNC);
//                    } else {
//                        mediaPlayer.pause();
//                        mediaPlayer.seekTo((int) MusicInfoHolder.getSeekPos());
//                        mediaPlayer.start();
//                    }
//                    seekInOrder = false;
//                }
//            } catch (IllegalStateException ignored) {
//            }
//        }
//    };
//
//    @Override
//    public void onAudioFocusChange(int focusState) {
//        //Invoked when the audio focus of the system is updated.
//        switch (focusState) {
//            case AudioManager.AUDIOFOCUS_GAIN:
//                // resume playback
//                MbnController.play(context);
//                break;
//            case AudioManager.AUDIOFOCUS_LOSS:
//                // Lost focus for an unbounded amount of time: stop playback and release media player
//                MbnController.pause(context);
//                break;
//            case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT:
//                // Lost focus for a short time, but we have to stop
//                // playback. We don't release the media player because playback
//                // is likely to resume
//                MbnController.pause(context);
//                break;
//            case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK:
//                // Lost focus for a short time, but it's ok to keep playing
//                // at an attenuated level
////                try {
////                    if (mediaPlayer != null && mediaPlayer.isPlaying()) {
////                        mediaPlayer.setVolume(0.1f, 0.1f);
////                    }
////                } catch (Exception ignored) {
////                }
//                break;
//        }
//    }
//
//
//    @Override
//    public void onPrepared(MediaPlayer mp) {
//
//    }
//
//    @Override
//    public void onCompletion(MediaPlayer mp) {
//        onFinishCallback.sendEmptyMessage(0);
//    }
//
//    @Override
//    public void onSeekComplete(MediaPlayer mp) {
//
//    }
//}
