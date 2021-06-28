//package com.br.mreza.musicplayer;
//
//import android.app.Notification;
//import android.app.NotificationChannel;
//import android.app.NotificationManager;
//import android.app.PendingIntent;
//import android.app.Service;
//import android.content.BroadcastReceiver;
//import android.content.Context;
//import android.content.Intent;
//import android.content.IntentFilter;
//import android.media.AudioManager;
//import android.media.MediaPlayer;
//import android.os.AsyncTask;
//import android.os.Build;
//import android.os.IBinder;
//import android.support.annotation.RequiresApi;
//import android.support.v4.app.TaskStackBuilder;
//import android.support.v7.app.NotificationCompat;
//
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.Timer;
//import java.util.TimerTask;
//
//import static com.br.mreza.musicplayer.ListMaker.albumsList;
//import static com.br.mreza.musicplayer.ListMaker.currentQueue;
//
//
////import static com.br.mreza.musicplayer.ListMaker.albumContents;
////import static com.br.mreza.musicplayer.ListMaker.current;
//
//public class PlayerService extends Service {
//
//
//    private BroadcastReceiver noisyReceiver;
//    private String channel_id;
//
//    public class SyncPlayer extends AsyncTask<Void, Void, Void> {
//
//
//        ArrayList<ListActivity.MbnSong> synckerPlayerList;
//        int pos;
//
//        @Override
//        protected Void doInBackground(Void... params) {
//
//
//            if (currentQueue.size() > 0) {
//
//                synckerPlayerList = currentQueue.getList();
//                currentTrackCode = synckerPlayerList.get(position).getHashCode();
//
//                pos = position;
//            }
//
//
//            if (playing) {
//
//                player.reset();
//                player = null;
//
//            }
//
////            player = MediaPlayer.create(PlayerService.this.getBaseContext(), Uri.parse(synckerPlayerList.get(pos).getPath()));
//            player = new MediaPlayer();
//            try {
//                player.setDataSource(synckerPlayerList.get(pos).getPath());
//                player.prepare();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//
//            player.start();
//            seekMax = player.getDuration();
//
//            player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
//                @Override
//                public void onCompletion(MediaPlayer mp) {
//
////                    System.out.println("Hi");
//                    try {
//
//
//                        if (position < currentQueue.size() - 1) {
//
//                            position++;
//
//                            currentTrackCode = synckerPlayerList.get(position).getHashCode();
//
//                            new SyncPlayer().execute();
//                        }
//                    } catch (Exception ignored) {
//                    }
//
//                }
//            });
//
//            new Timer().scheduleAtFixedRate(new TimerTask() {
//                @Override
//                public void run() {
//
//                    seekPosition = player.getCurrentPosition();
//
//                }
//            }, 0, 1000);
//
//
//            playing = true;
//
//
//            return null;
//        }
//    }
//
//    static MediaPlayer player;
//    boolean playing = false;
//
//    static int position;
//    static int positionAlbum;
//    static int currentTrackCode;
//
//    public static int seekPosition = 0;
//    public static int seekMax = 0;
//
//
//    public PlayerService() {
//
//    }
//
//
//    @Override
//    public IBinder onBind(Intent intent) {
//        // TODO: Return the communication channel to the service.
//
//
//        throw new UnsupportedOperationException("Not yet implemented");
//
//
//    }
//
//    @Override
//    public void onCreate() {
//        super.onCreate();
//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            makeNotifyChannel();
//        }
//        makeNotification();
//
//    }
//
//    @Override
//    public int onStartCommand(Intent intent, int flags, int startId) {
//
//
//        try {
//
//
//            positionAlbum = intent.getIntExtra("alPos", 0);
//
//
//            currentQueue = albumsList.get(positionAlbum);
//
//
//            if (intent.getIntExtra("sPos", 0) < currentQueue.size() && intent.getIntExtra("sPos", 0) >= 0) {
//
//                position = intent.getIntExtra("sPos", 0);
//
//            }
//
//
//            System.out.println("im working!!------------------->" + position);
//
//
//            SyncPlayer syncPlayer = new SyncPlayer();
//
//            syncPlayer.execute();
//
//            player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
//                @Override
//                public void onCompletion(MediaPlayer mp) {
//
////                    System.out.println("Hi");
//
//                }
//
//
//            });
//
////            if (playing) {
////
////                player.reset();
////
////            }
////
////            player = MediaPlayer.create(getApplicationContext(), Uri.parse(current.get(position).getPath()));
////
////            player.start();
////            seekMax = player.getDuration();
////
////            new Timer().scheduleAtFixedRate(new TimerTask() {
////                @Override
////                public void run() {
////
////                    seekPosition = player.getCurrentPosition();
////
////                }
////            }, 0, 1000);
////
////
////            playing = true;
//        } catch (Exception ignored) {
//        }
//
//        noisyReceiver = new BroadcastReceiver() {
//            @Override
//            public void onReceive(Context cont, Intent intent) {
//
//                try {
//                    player.pause();
//                } catch (IllegalStateException e) {
//                    e.printStackTrace();
//                }
//
//            }
//        };
//
//        IntentFilter filter = new IntentFilter(AudioManager.ACTION_AUDIO_BECOMING_NOISY);
//
//        registerReceiver(noisyReceiver, filter);
//
//
//
//        return super.onStartCommand(intent, flags, startId);
//
//
//    }
//
//    public void makeNotification() {
//
////        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
//
//        Intent intent = new Intent(this, PlayerActivity.class);
//        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
//        stackBuilder.addParentStack(PlayerActivity.class);
//        stackBuilder.addNextIntent(intent);
//        PendingIntent pendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
//
//
//        Notification notification = null;
//        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
//            notification = new Notification.Builder(this, channel_id).setContentTitle("MBN MusicPlayer")
//                    .setContentText("Hello").setTicker("MBN").setContentIntent(pendingIntent)
//                    .setSmallIcon(R.drawable.ic_play_button).setStyle(new Notification.MediaStyle().setShowActionsInCompactView())
//                    .build();
//        } else {
//
//            notification = new NotificationCompat.Builder(this).setContentText("Hello").setTicker("MBN").setContentIntent(pendingIntent)
//                    .setSmallIcon(R.drawable.ic_play_button).setContentTitle("MBN MusicPlayer")
//                    .build();
//        }
//
//
////                .setContentTitle("MBN is Here!")
////                .setContentText("Hello")
////                .setSmallIcon(R.mipmap.ic_launcher_round)
////                .setContentIntent(pendingIntent)
////                .setPriority(Notification.PRIORITY_HIGH)
////                .setTicker("dont know").build();
//
//
//        startForeground(1, notification);
//
//        System.out.println("------------------Did it----------------");
//
//
//    }
//
//    @RequiresApi(api = Build.VERSION_CODES.O)
//    private String makeNotifyChannel() {
//
//        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
//
//        channel_id = getPackageName();
//        CharSequence channelName = "MediaControl";
//        int importance = NotificationManager.IMPORTANCE_LOW;
//
//        NotificationChannel channel = new NotificationChannel(channel_id, channelName, importance);
//        manager.createNotificationChannel(channel);
//
//        return channel_id;
//    }
//
//
//    @Override
//    public void onDestroy() {
//
//
//        unregisterReceiver(noisyReceiver);
//
//
//        super.onDestroy();
//    }
//
//    //    @Override
////    public void onStart(Intent intent, int startId) {
////        super.onStart(intent, startId);
////
////
////    }
//
////
////    public void controller(int job, int pos) {
////        switch (job) {
////            case 0:
////                player.pause();
////                break;
////            case 1:
////                player.start();
////                break;
////            case 2:
////                player.seekTo(pos);
////                break;
////
////
////        }
////
////
////    }
//
//}
