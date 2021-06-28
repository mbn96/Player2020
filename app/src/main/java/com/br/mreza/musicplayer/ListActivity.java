package com.br.mreza.musicplayer;

import android.animation.ValueAnimator;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.br.mreza.musicplayer.newdesign.FirstNewDesignActivity;
import com.br.mreza.musicplayer.newdesign.asyncloader.AsyncLoaderManager;
import com.br.mreza.musicplayer.newdesign.customviews.DotProgressBar;
import com.br.mreza.musicplayer.p2020.design.MainActivity2020;

import mbn.libs.utils.AndroidUtils;

public class ListActivity extends AppCompatActivity implements AsyncLoaderManager.ProgressListener, AsyncLoaderManager.FinishListener {


    SharedPreferences sharedPreferences;
    private TextView scanning;

    @Override
    public void onFinish() {
        AsyncLoaderManager.INSTANCE.unRigesterFinishListener(this);
        AsyncLoaderManager.INSTANCE.unRigesterProgressListener(this);
        endThis();

    }

    @Override
    public void onProgress(String title, int[] values) {
        progressBar.setVisibility(View.VISIBLE);
        percentText.setVisibility(View.VISIBLE);
        scanning.setVisibility(View.VISIBLE);

//            String path = intent.getStringExtra("path");
//
//            if (path != null) {
//                scanning.setText("Scanning...\n" + path);
//            } else {

        scanning.setText("Preparing the new song...\n" + title);

        progressBar.setMax(values[0]);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            progressBar.setProgress(values[1], true);
        } else {
            progressBar.setProgress(values[1]);

        }

//            float f1, f2;
//            f1 = values[1];
//            f2 = values[0];


        percentText.setText((values[1] * 100) / values[0] + "%");
    }


//    class MbnSong {
//
////        private class InformationCatcher extends AsyncTask<Void, Void, Void> {
////            @Override
////            protected Void doInBackground(Void... voids) {
////
////                MediaMetadataRetriever retriever = new MediaMetadataRetriever();
////                retriever.setDataSource(path);
////                duration = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_BITRATE);
////                retriever.release();
////
////                return null;
////            }
////        }
//
//
//
//        MbnSong(String title, String albumTitle, String path, String artist, int duration, long dateAdded) {
//            this.title = title;
//            this.albumTitle = albumTitle;
//            this.path = path;
//            this.hashCode = title + path;
//            this.artist = artist;
//            this.duration = duration;
//            this.dateAdded = dateAdded;
//
////            new InformationCatcher().execute();
//
//
//        }
//
//        private long dateAdded;
//
//        public long getDateAdded() {
//            return dateAdded;
//        }
//
//        @Override
//        public String toString() {
//
//
//            return title;
//        }
//
//        String getHashCode() {
//            return hashCode;
//        }
//
//        private String title;
//
//        int getDuration() {
//            return duration;
//        }
//
//        private int duration;
//
//        private String bitRate;
//
//        private String hashCode;
//
//        private String artist;
//
//        private String albumTitle;
//        private String path;
//
//
//        private Bitmap cover;
//
//        private void catchBitrate() {
//
//            try {
//                MediaMetadataRetriever retriever = new MediaMetadataRetriever();
//                retriever.setDataSource(path);
//                bitRate = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_BITRATE);
//                retriever.release();
//            } catch (IllegalArgumentException e) {
//                e.printStackTrace();
//                bitRate = "0";
//            }
//        }
//
//        String getBitRate() {
//
//            if (bitRate != null) {
//                return bitRate;
//            }
//            catchBitrate();
//
//            return bitRate;
//        }
//
////        MbnSong(String song) {
//////            retriever.setDataSource(Uri.parse(song).getPath());
//////
//////            cover = retriever.getEmbeddedPicture();
//////
//////            title = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE);
//////            albumTitle = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUM);
////
////            if (title == null) {
////
////
////                title = "Unknown";
////
//////                title = song.getName().substring(0, song.getName().length() - 4);
////            }
////            if (albumTitle == null) {
////
////                albumTitle = "Album Unknown";
////            }
////
////
////            this.path = Uri.parse(song).getPath();
////
//////            if (cover == null) {
//////                Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.night_rain_1);
//////
//////                cover = bitmap.
//////
//////            }
////
//////            System.out.println(title);
////        }
//
//        public String getAlbumTitle() {
//            return albumTitle;
//        }
//
//        String getTitle() {
//            return title;
//        }
//
//        public String getArtistTitle() {
//            return artist;
//        }
//
//        private void makeCover() {
//
//
//            try {
//                MediaMetadataRetriever retriever = new MediaMetadataRetriever();
//                retriever.setDataSource(path);
//                byte[] coverArray = retriever.getEmbeddedPicture();
//                retriever.release();
//                cover = BitmapFactory.decodeByteArray(coverArray, 0, coverArray.length);
//            } catch (Exception e) {
//
//                Random random = new Random();
//
//                int rand = random.nextInt(5);
//                if (rand == 0) {
//                    cover = BitmapFactory.decodeResource(getResources(), R.drawable.music_love5_ppcorn);
//                } else if (rand == 1) {
//                    cover = BitmapFactory.decodeResource(getResources(), R.drawable.night_rain_1);
//                } else if (rand == 2) {
//                    cover = BitmapFactory.decodeResource(getResources(), R.drawable.night_rain_2);
//                } else if (rand == 3) {
//                    cover = BitmapFactory.decodeResource(getResources(), R.drawable.night_rain_4);
//                } else {
//                    cover = BitmapFactory.decodeResource(getResources(), R.drawable.day_fog);
//                }
//            }
//
//
//        }
//
//        public Bitmap getCover() {
//
//            if (cover != null) {
//
//                return cover;
//            }
//            makeCover();
//
//            return cover;
//        }
//
//        String getPath() {
//            return path;
//        }
//
//    }


//    private class MusicFinder extends AsyncTask<Void, Integer, Void> {
//
//        int counter = 0;
//
////        ProgressDialog dialog;
//
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//
////            dialog = ProgressDialog.show(ListActivity.this, "Scannig for musics !", counter + " Music found !", false, false);
////            dialog.setMax(200);
//        }
//
//
//        @Override
//        protected void onProgressUpdate(Integer... values) {
//            super.onProgressUpdate(values);
//
//            scanning.setText("Scanning...\n" + counter + " Music Found !");
//
//
////            dialog.setMessage(counter + " Music found !");
//
////            dialog.setProgress(counter);
//
//
//        }
//
//        final String PATH = Environment.getExternalStorageDirectory().getPath();
////        final String PATH2 = "/storage";
////        private String pattern = ".mp3";
//
//        private void finder() {
//
//            ContentResolver contentResolver = getContentResolver();
//
//            Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
//            String selection = MediaStore.Audio.Media.IS_MUSIC + "!= 0";
//            String sortOrder = MediaStore.Audio.Media.TITLE + " ASC";
//            Cursor cursor = contentResolver.query(uri, null, selection, null, sortOrder);
//
//
//            if (cursor != null && cursor.getCount() > 0) {
//                while (cursor.moveToNext()) {
//                    String data = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA));
//                    String title = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE));
//                    String album = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM));
//                    String artist = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST));
//                    int duration = cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.Media.DURATION));
//                    long date = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.DATE_ADDED));
//
//
//                    counter++;
//                    list.add(new MbnSong(title, album, data, artist, duration, date));
//
//                    publishProgress(counter);
////                        audioList.add(new Audio(data, title, album, artist));
//                }
//            }
//            assert cursor != null;
//            cursor.close();
//
//
////            System.out.println("finder");
//
////            File[] files = rawFile.listFiles();
////
//////            System.out.println(files[3]);
////
////
////            if (files.length > 0) {
////
////                for (File song : files) {
////
////                    if (song.isDirectory()) {
////
////                        finder(song);
////                    } else {
////
////                        if (song.getName().toLowerCase().endsWith(pattern)) {
////
////                            list.add(new MbnSong(song));
////
////                            counter++;
////
////                            publishProgress(counter);
//////                            System.out.println(song.getName());
////
////
////                        }
////
////
////                    }
////
////
////                }
////            }
//
//
//        }
//
//
//        @Override
//        protected Void doInBackground(Void... params) {
//
////            System.out.println("first iiiiiiiiiiii");
//
////            File home = new File(PATH);
////            File home2 = new File(PATH2);
//
//
////            System.out.println("hiiiiiiiiiii");
//
////            System.out.println(home.getPath());
////            System.out.println(home2.getPath());
//
//
////            Comparator<MbnSong> comparator = new Comparator<MbnSong>() {
////
////
////                @Override
////                public int compare(MbnSong o1, MbnSong o2) {
////
////
////                    return o1.getTitle().compareTo(o2.getTitle());
////                }
////
////            };
////            Collections.sort(list, comparator);
//
//
////            ListMaker.listReceiver(list, ListActivity.this);
//
//
////            System.out.println("here iiiiiiiiiiiii");
//
//
//            return null;
//        }
//
//        @Override
//        protected void onPostExecute(Void aVoid) {
//
//
////            Comparator<MbnSong> comparator = new Comparator<MbnSong>() {
////
////
////                @Override
////                public int compare(MbnSong o1, MbnSong o2) {
////
////
////                    return o1.getTitle().compareTo(o2.getTitle());
////                }
////
//////                @Override
//////                public int compare(Object o1, Object o2) {
//////
//////                    List<String> compare = new ArrayList<>();
//////
//////                    compare.add(o1.toString());
//////                    compare.add(o2.toString());
//////
//////                    Collections.sort(compare);
////////                    Object returnOBJ;
//////                    if (compare.get(0).equals(o1.toString())) {
//////
//////
//////                        return 0;
//////                    } else {
//////
//////                        return 1;
//////                    }
//////
//////
//////                }
////            };
////            Collections.sort(list, comparator);
////
////
////            ListMaker.listReceiver(list, ListActivity.this);
//
//
//            super.onPostExecute(aVoid);
//
//
//            Intent intent = new Intent(ListActivity.this, FirstNewDesignActivity.class);
//
//            startActivity(intent);
//            ListActivity.this.finish();
//
////            System.out.println("end iiiiiiiiiii");
//
//        }
//    }

//    public static List<MbnSong> list = new ArrayList<>();


//    BroadcastReceiver newFile = new BroadcastReceiver() {
//        @SuppressLint("SetTextI18n")
//        @Override
//        public void onReceive(Context context, Intent intent) {
//
//            progressBar.setVisibility(View.VISIBLE);
//            percentText.setVisibility(View.VISIBLE);
//
////            String path = intent.getStringExtra("path");
////
////            if (path != null) {
////                scanning.setText("Scanning...\n" + path);
////            } else {
//
//            scanning.setText("Preparing the new song...\n" + intent.getStringExtra("song"));
//
//            int[] values = intent.getIntArrayExtra("loader");
//
//            progressBar.setMax(values[0]);
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//                progressBar.setProgress(values[1], true);
//            } else {
//                progressBar.setProgress(values[1]);
//
//            }
//
////            float f1, f2;
////            f1 = values[1];
////            f2 = values[0];
//
//
//            percentText.setText((values[1] * 100) / values[0] + "%");
//
//
////            }
//
//
//        }
//    };

    private DotProgressBar progressBar;
    private TextView percentText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        scanning = findViewById(R.id.scannig_tv);
        progressBar = findViewById(R.id.load_progress_first);
        progressBar.setVisibility(View.INVISIBLE);
        progressBar.setAccentColor(Color.DKGRAY);
//        progressBar.setAccentColor(Color.parseColor("#00b4ff"));
        percentText = findViewById(R.id.load_percent_textView);
        percentText.setVisibility(View.INVISIBLE);
        getWindow().setStatusBarColor(Color.TRANSPARENT);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
//        LocalBroadcastManager.getInstance(getApplicationContext()).registerReceiver(newFile, new IntentFilter("newFile"));

        getSupportActionBar().hide();


        sharedPreferences = this.getSharedPreferences(getPackageName(), MODE_PRIVATE);

        animator();

//        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
//            // TODO: Consider calling
//            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 9619);
//
//            return;
//        }


//        new DataLoaderAsync.Loader().execute(getApplicationContext());

        if (!AsyncLoaderManager.INSTANCE.isHasStarted()) {
            AsyncLoaderManager.INSTANCE.registerFinishListener(this);
            AsyncLoaderManager.INSTANCE.registerProgressListener(this);
        } else {
            endThis();
        }
//
//        DataLoaderAsync.finder(getApplicationContext());

//        scanning.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//
//                Intent intent = new Intent(ListActivity.this, FirstActivity.class);
//                startActivity(intent);
//                ListActivity.this.finish();
//
//
//            }
//        },1500);


//        MusicFinder finder = new MusicFinder();
//        finder.execute();


//        System.out.println("create");


    }

    private void endThis() {
        Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(ListActivity.this, MainActivity2020.class);
                startActivity(intent);
                finish();
            }
        }, 2000);
    }

//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//
//
//        if (requestCode == 9619) {
//
//
//            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//
////                new DataLoaderAsync.Loader().execute(getApplicationContext());
//
////                scanning.postDelayed(new Runnable() {
////                    @Override
////                    public void run() {
////
////                        Intent intent = new Intent(ListActivity.this, FirstActivity.class);
////                        startActivity(intent);
////                        ListActivity.this.finish();
////
////
////                    }
////                },2500);
//
//                MusicFinder finder = new MusicFinder();
//                finder.execute();
//
//            }
//
//
//        }
//
//    }

    private void animator() {

        TextView wellTV = findViewById(R.id.wellcome_tv);
        View icon = findViewById(R.id.progress_indicader);


//        wellTV.setAlpha(0);
        scanning.setAlpha(0);

        AndroidUtils.coolViewReveal(1700, 250, 0, icon, wellTV);

//        wellTV.animate().alpha(1).setDuration(900);


        scanning.animate().alpha(1).setDuration(900).start();

//        ValueAnimator animator = ValueAnimator.ofFloat(1, 2).setDuration(400);
//        animator.setRepeatCount(ValueAnimator.INFINITE);
//        animator.setRepeatMode(ValueAnimator.REVERSE);
//        animator.start();

    }

}
