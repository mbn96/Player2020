package com.br.mreza.musicplayer.newmodel.theme;


import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Handler;
import android.os.Looper;

import androidx.palette.graphics.Palette;

import com.br.mreza.musicplayer.DataSong;
import com.br.mreza.musicplayer.MBN.bitmapUtils.MbnUtils;
import com.br.mreza.musicplayer.newdesign.MbnMusicPlayer;
import com.br.mreza.musicplayer.newmodel.database.DataBaseManager;
import com.br.mreza.musicplayer.newmodel.database.DataBaseUtils;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import mbn.libs.UI.ThemeCoordinator;
import mbn.libs.backgroundtask.BaseTaskHolder;
import mbn.libs.backgroundtask.ThreadManager;

public class ThemeEngine {

    private static final ThemeEngine INSTANCE = new ThemeEngine();

    private ThemeEngine() {
    }

    public static ThemeEngine getINSTANCE() {
        return INSTANCE;
    }

    private WeakReference<Context> contextWeakReference;

    private DataSong track;
    private ThemeTask.Result result;

    private Handler mainThreadHandler = new Handler(Looper.getMainLooper());
    private boolean justAsked = false;

    //    private Bitmap main;
//    private Bitmap blur;
//    private int accentColor;

    private ArrayList<DefaultThemeCallback> callbacks = new ArrayList<>();

    public void init(Context context) {
        contextWeakReference = new WeakReference<>(context);
    }

    private boolean haveContext() {
        return contextWeakReference.get() != null;
    }

    private Context getContext() {
        return contextWeakReference.get();
    }

    public void trackChanged(DataSong song) {
        track = song;
        if (haveContext()) {
            ThreadManager.getAppGlobalTask().StartTask(new ThemeTask(song.getId(), song.getPath(), getContext().getResources()), resultReceiver);
        }
    }

    private BaseTaskHolder.ResultReceiver resultReceiver = new BaseTaskHolder.ResultReceiver() {
        @Override
        public void onResult(Object result, Object info) {
            ThemeEngine.this.result = (ThemeTask.Result) result;
//            MbnMusicPlayer.THEME_COORDINATOR.addItem(new ThemeCoordinator.ColorItem(ThemeCoordinator.DefaultThemeTypes.BackgroundColor,
//                    mbn.libs.imagelibs.imageworks.MbnUtils.colorShift((((ThemeTask.Result) result).accent), 2)));
            MbnMusicPlayer.THEME_COORDINATOR.addItem(new ThemeCoordinator.ColorItem(ThemeCoordinator.DefaultThemeTypes.AccentColor, ((ThemeTask.Result) result).accent));
            for (DefaultThemeCallback callback : callbacks) {
                callback.process((ThemeTask.Result) result);
            }

        }
    };

    public void getResult(DefaultThemeCallback callback) {
        if (result == null) {
            if (!justAsked) {
                mainThreadHandler.postDelayed(checkForResult, 500);
                justAsked = true;
            }
            return;
        }
        callback.process(result);
    }

    private Runnable checkForResult = new Runnable() {
        @Override
        public void run() {
            if (track == null || result == null) {
                DataBaseManager.getManager().startThemeEngine();
            }
            justAsked = false;
        }
    };

    public void registerCallback(DefaultThemeCallback callback) {
        if (!callbacks.contains(callback)) callbacks.add(callback);
    }

    public void unRegisterCallback(DefaultThemeCallback callback) {
        callbacks.remove(callback);
    }

    //----------------------------- Callbacks -------------------------//

    private interface ThemeCallback {
        void onMainBitmap(Bitmap main);

        void onBlurBitmap(Bitmap blur);

        void onAccentColor(int color);
    }

    public static abstract class DefaultThemeCallback implements ThemeCallback {

        private boolean wantMain, wantBlur, wantAccent;

        public DefaultThemeCallback(boolean wantMain, boolean wantBlur, boolean wantAccent) {
            this.wantMain = wantMain;
            this.wantBlur = wantBlur;
            this.wantAccent = wantAccent;
        }

        private void process(ThemeTask.Result result) {
            if (wantMain) onMainBitmap(result.getMain());
            if (wantBlur) onBlurBitmap(result.getBlur());
            if (wantAccent) onAccentColor(result.getAccent());
            onProcessFinished();
        }

        @Override
        public void onMainBitmap(Bitmap main) {

        }

        @Override
        public void onBlurBitmap(Bitmap blur) {

        }

        @Override
        public void onAccentColor(int color) {

        }

        public abstract void onProcessFinished();
    }

    //---------------------------  Tasks ---------------------------------//

    private static class ThemeTask implements BaseTaskHolder.BaseTask {

        private long id;
        private Bitmap blur;
        private String path;
        private Resources resources;
        //        private int accent = Color.DKGRAY;
//        private int accent = Color.parseColor("#00b4ff");
        private int accent = 0xff00b4ff;
//        private int accent = Color.parseColor("#003968");

        public ThemeTask(long id, String path, Resources resources) {
            this.id = id;
            this.path = path;
            this.resources = resources;
        }

        @Override
        public Object onRun() {
            Bitmap main;
//            Bitmap forBlur;
//            Bitmap blur;
            main = DataBaseUtils.getSongArtwork(id, 500, true, bitmap -> blur = bitmap);
//            try {
//                MediaMetadataRetriever retriever = new MediaMetadataRetriever();
//                retriever.setDataSource(path);
//                byte[] bytes = retriever.getEmbeddedPicture();
//                BitmapFactory.Options options = new BitmapFactory.Options();
//                options.inJustDecodeBounds = true;
//                BitmapFactory.decodeByteArray(bytes, 0, bytes.length, options);
//                options.inSampleSize = Math.max(options.outHeight, options.outWidth) / 500;
//                options.inJustDecodeBounds = false;
//                main = forBlur = BitmapFactory.decodeByteArray(bytes, 0, bytes.length, options);
//
//            } catch (Exception e) {
////            e.printStackTrace();
//            }
//            if (main == null) {
//                BitmapFactory.Options options = new BitmapFactory.Options();
//                options.inJustDecodeBounds = true;
//                BitmapFactory.decodeResource(resources, R.drawable.night_rain_1, options);
//                options.inSampleSize = Math.max(options.outHeight, options.outWidth) / 500;
//                options.inJustDecodeBounds = false;
//                main = forBlur = BitmapFactory.decodeResource(resources, R.drawable.mbn_back_dr, options);
////                forBlur = BitmapFactory.decodeResource(resources, R.drawable.day_fog);
//
//            } else
            accent = Palette.from(main).generate().getVibrantColor(accent);

//            =MbnUtils.stackBlur(MbnUtils.createSmallBit(forBlur, 270), 12, true);

            if (Math.max(main.getWidth(), main.getHeight()) > 600) {
                Bitmap outMain = MbnUtils.createSmallBit(main, 600);
                main.recycle();
//                forBlur.recycle();
                return new Result(outMain, blur, accent);
            }

            return new Result(main, blur, accent);
        }

        @Override
        public Object getInfo() {
            return id;
        }

        public class Result {
            private Bitmap main;
            private Bitmap blur;
            private int accent;

            public Result(Bitmap main, Bitmap blur, int accent) {
                this.main = main;
                this.blur = blur;
                this.accent = accent;
            }

            Bitmap getMain() {
                return main;
            }

            public Bitmap getBlur() {
                return blur;
            }

            public int getAccent() {
                return accent;
            }
        }


    }
}
