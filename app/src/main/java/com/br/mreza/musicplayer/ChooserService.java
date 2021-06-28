//package com.br.mreza.musicplayer;
//
//import android.app.Service;
//import android.content.BroadcastReceiver;
//import android.content.Context;
//import android.content.Intent;
//import android.media.AudioManager;
//import android.media.MediaPlayer;
//import android.os.IBinder;
//import android.telephony.PhoneStateListener;
//import android.telephony.TelephonyManager;
//
//public class ChooserService extends Service implements MediaPlayer.OnCompletionListener,
//        MediaPlayer.OnPreparedListener, MediaPlayer.OnErrorListener, MediaPlayer.OnSeekCompleteListener,
//        MediaPlayer.OnInfoListener, MediaPlayer.OnBufferingUpdateListener,
//        AudioManager.OnAudioFocusChangeListener {
//
//    public ChooserService() {
//    }
//
//    private MediaPlayer mediaPlayer;
//
//    private AudioManager audioManager;
//
//    //Handle incoming phone calls
//    private boolean ongoingCall = false;
//
//    private static final int NOTIFICATION_ID = 96;
//
//    static final String ACTION_PLAY_CHOOSER = "com.br.mreza.musicplayer.ACTION_PLAY_CHOOSER";
//
//
//    // Binder given to clients
//    private BroadcastReceiver noisyReceiver;
//
//
//    @Override
//    public IBinder onBind(Intent intent) {
//        // TODO: Return the communication channel to the service.
//        throw new UnsupportedOperationException("Not yet implemented");
//    }
//
//
//    @Override
//    public void onAudioFocusChange(int i) {
//
//    }
//
//    @Override
//    public void onBufferingUpdate(MediaPlayer mediaPlayer, int i) {
//
//    }
//
//    @Override
//    public void onCompletion(MediaPlayer mediaPlayer) {
//
//    }
//
//    @Override
//    public boolean onError(MediaPlayer mediaPlayer, int i, int i1) {
//        return false;
//    }
//
//    @Override
//    public boolean onInfo(MediaPlayer mediaPlayer, int i, int i1) {
//        return false;
//    }
//
//    @Override
//    public void onPrepared(MediaPlayer mediaPlayer) {
//
//    }
//
//    @Override
//    public void onSeekComplete(MediaPlayer mediaPlayer) {
//
//    }
//
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
//                            MbnController.pause(ChooserService.this);
//                            ongoingCall = true;
//                        }
//                        break;
//                    case TelephonyManager.CALL_STATE_IDLE:
//                        // Phone idle. Start playing.
//                        if (mediaPlayer != null) {
//                            if (ongoingCall) {
//                                ongoingCall = false;
//                                MbnController.play(ChooserService.this);
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
//
//
//}
