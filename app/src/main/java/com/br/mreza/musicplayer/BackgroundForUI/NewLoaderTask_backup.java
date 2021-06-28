package com.br.mreza.musicplayer.BackgroundForUI;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.media.MediaMetadataRetriever;

import com.br.mreza.musicplayer.R;

import mbn.libs.backgroundtask.BaseTaskHolder;


public class NewLoaderTask_backup implements BaseTaskHolder.BaseTask {

    private long id;
    private String path;
    private Resources resources;

    NewLoaderTask_backup(long id, String path, Resources resources) {
        this.id = id;
        this.path = path;
        this.resources = resources;
    }

    @Override
    public Object onRun() {
        Bitmap main = null;
        Bitmap blur;
        try {
            MediaMetadataRetriever retriever = new MediaMetadataRetriever();
            retriever.setDataSource(path);
            byte[] bytes = retriever.getEmbeddedPicture();
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeByteArray(bytes, 0, bytes.length, options);
            options.inSampleSize = Math.max(options.outHeight, options.outWidth) / 500;
            options.inJustDecodeBounds = false;
            main = BitmapFactory.decodeByteArray(bytes, 0, bytes.length, options);

        } catch (Exception e) {
//            e.printStackTrace();
        }

        if (main == null) {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeResource(resources, R.drawable.night_rain_1, options);
            options.inSampleSize = Math.max(options.outHeight, options.outWidth) / 500;
            options.inJustDecodeBounds = false;
            main = BitmapFactory.decodeResource(resources, R.drawable.night_rain_1, options);

        }

//        mPalette = Palette.from(main).generate();
//        blur = createBlur(main);
        blur = createBlur2(main);
        return new Result(main, blur);
    }

    @Override
    public Object getInfo() {
        return id;
    }


    private Bitmap createBlur(Bitmap bitmap) {

        float bigOne = 300f / Math.max(bitmap.getWidth(), bitmap.getHeight());

        int width = (int) (bitmap.getWidth() * bigOne);
        int height = (int) (bitmap.getHeight() * bigOne);

//        int width = bitmap.getWidth();
//        int height = bitmap.getHeight();

        Bitmap bity = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas cany = new Canvas(bity);
        cany.drawBitmap(bitmap, null, new Rect(0, 0, width, height), null);

        int[] allTogether = new int[width * height];

        bity.getPixels(allTogether, 0, width, 0, 0, width, height);

        int oneIndex, threeIndex, forIndex, fiveIndex, twoIndex,
                one, two, three, four, five, six, seven, eight, nine, total,
                r1, r2, r3, r4, r5, r6, r7, r8, r9, rt,
                b1, b2, b3, b4, b5, b6, b7, b8, b9, bt,
                g1, g2, g3, g4, g5, g6, g7, g8, g9, gt,
                a1, a2, a3, a4, a5, a6, a7, a8, a9, at;

        for (int x = 0; x < 3; x++) {

            for (int i = 0; i < allTogether.length; i++) {
                if ((i + 1) > width && (i + 1) % width > 1 && (i + 1) / width < height - 1) {

                    oneIndex = i;
                    try {

                        twoIndex = oneIndex - 1;
                        threeIndex = oneIndex + 1;
                        forIndex = oneIndex - width;
                        fiveIndex = oneIndex + width;

                        one = allTogether[oneIndex];
                        r1 = Color.red(one);
                        b1 = Color.blue(one);
                        g1 = Color.green(one);
                        a1 = Color.alpha(one);

                        two = allTogether[twoIndex];
                        r2 = Color.red(two);
                        b2 = Color.blue(two);
                        g2 = Color.green(two);
                        a2 = Color.alpha(two);

                        three = allTogether[threeIndex];
                        r3 = Color.red(three);
                        b3 = Color.blue(three);
                        g3 = Color.green(three);
                        a3 = Color.alpha(three);

                        four = allTogether[forIndex];
                        r4 = Color.red(four);
                        b4 = Color.blue(four);
                        g4 = Color.green(four);
                        a4 = Color.alpha(four);

                        six = allTogether[forIndex + 1];
                        r6 = Color.red(six);
                        b6 = Color.blue(six);
                        g6 = Color.green(six);
                        a6 = Color.alpha(six);

                        seven = allTogether[forIndex - 1];
                        r7 = Color.red(seven);
                        b7 = Color.blue(seven);
                        g7 = Color.green(seven);
                        a7 = Color.alpha(seven);

                        five = allTogether[fiveIndex];
                        r5 = Color.red(five);
                        b5 = Color.blue(five);
                        g5 = Color.green(five);
                        a5 = Color.alpha(five);

                        eight = allTogether[fiveIndex - 1];
                        r8 = Color.red(eight);
                        b8 = Color.blue(eight);
                        g8 = Color.green(eight);
                        a8 = Color.alpha(eight);

                        nine = allTogether[fiveIndex + 1];
                        r9 = Color.red(nine);
                        b9 = Color.blue(nine);
                        g9 = Color.green(nine);
                        a9 = Color.alpha(nine);

                        rt = (r1 + r2 + r3 + r4 + r5 + r6 + r7 + r8 + r9) / 9;
                        bt = (b1 + b2 + b3 + b4 + b5 + b6 + b7 + b8 + b9) / 9;
                        gt = (g1 + g2 + g3 + g4 + g5 + g6 + g7 + g8 + g9) / 9;
                        at = (a1 + a2 + a3 + a4 + a5 + a6 + a7 + a8 + a9) / 9;

                        total = Color.argb(at, rt, gt, bt);

                        allTogether[oneIndex] = total;
                        allTogether[twoIndex] = total;
                        allTogether[threeIndex] = total;
                        allTogether[forIndex] = total;
                        allTogether[fiveIndex] = total;
                        allTogether[fiveIndex - 1] = total;
                        allTogether[fiveIndex + 1] = total;
                        allTogether[forIndex - 1] = total;
                        allTogether[forIndex + 1] = total;


                    } catch (Exception ignored) {
                    }
                }
            }
        }
        return Bitmap.createBitmap(allTogether, width, height, Bitmap.Config.ARGB_8888);
    }

    private Bitmap createBlur2(Bitmap bitmap) {

        float bigOne = 300f / Math.max(bitmap.getWidth(), bitmap.getHeight());

        int width = (int) (bitmap.getWidth() * bigOne);
        int height = (int) (bitmap.getHeight() * bigOne);

//        int width = bitmap.getWidth();
//        int height = bitmap.getHeight();

        Bitmap bity = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas cany = new Canvas(bity);
        cany.drawBitmap(bitmap, null, new Rect(0, 0, width, height), null);

        int[] allTogether = new int[width * height];

        bity.getPixels(allTogether, 0, width, 0, 0, width, height);

        int oneIndex, threeIndex, forIndex, fiveIndex, twoIndex,
                one, two, three, four, five, six, seven, eight, nine, total,
                r1, r2, r3, r4, r5, r6, r7, r8, r9, rt,
                b1, b2, b3, b4, b5, b6, b7, b8, b9, bt,
                g1, g2, g3, g4, g5, g6, g7, g8, g9, gt,
                a1, a2, a3, a4, a5, a6, a7, a8, a9, at;

        for (int x = 0; x < 8; x++) {
            for (int i = 0; i < allTogether.length; i++) {
                if ((i + 1) > width && (i + 1) % width > 1 && (i + 1) / width < height - 1) {

                    oneIndex = i;
                    try {

                        twoIndex = oneIndex - 1;
                        threeIndex = oneIndex + 1;
                        forIndex = oneIndex - width;
                        fiveIndex = oneIndex + width;

                        one = allTogether[oneIndex];
                        r1 = Color.red(one);
                        b1 = Color.blue(one);
                        g1 = Color.green(one);
                        a1 = Color.alpha(one);

                        two = allTogether[twoIndex];
                        r2 = Color.red(two);
                        b2 = Color.blue(two);
                        g2 = Color.green(two);
                        a2 = Color.alpha(two);

                        three = allTogether[threeIndex];
                        r3 = Color.red(three);
                        b3 = Color.blue(three);
                        g3 = Color.green(three);
                        a3 = Color.alpha(three);

                        four = allTogether[forIndex];
                        r4 = Color.red(four);
                        b4 = Color.blue(four);
                        g4 = Color.green(four);
                        a4 = Color.alpha(four);

                        six = allTogether[forIndex + 1];
                        r6 = Color.red(six);
                        b6 = Color.blue(six);
                        g6 = Color.green(six);
                        a6 = Color.alpha(six);

                        seven = allTogether[forIndex - 1];
                        r7 = Color.red(seven);
                        b7 = Color.blue(seven);
                        g7 = Color.green(seven);
                        a7 = Color.alpha(seven);

                        five = allTogether[fiveIndex];
                        r5 = Color.red(five);
                        b5 = Color.blue(five);
                        g5 = Color.green(five);
                        a5 = Color.alpha(five);

                        eight = allTogether[fiveIndex - 1];
                        r8 = Color.red(eight);
                        b8 = Color.blue(eight);
                        g8 = Color.green(eight);
                        a8 = Color.alpha(eight);

                        nine = allTogether[fiveIndex + 1];
                        r9 = Color.red(nine);
                        b9 = Color.blue(nine);
                        g9 = Color.green(nine);
                        a9 = Color.alpha(nine);

                        rt = (r1 + r2 + r3 + r4 + r5 + r6 + r7 + r8 + r9) / 9;
                        bt = (b1 + b2 + b3 + b4 + b5 + b6 + b7 + b8 + b9) / 9;
                        gt = (g1 + g2 + g3 + g4 + g5 + g6 + g7 + g8 + g9) / 9;
                        at = (a1 + a2 + a3 + a4 + a5 + a6 + a7 + a8 + a9) / 9;

                        total = Color.argb(at, rt, gt, bt);

                        allTogether[oneIndex] = total;


                    } catch (Exception ignored) {
                    }
                }
            }
        }
        return Bitmap.createBitmap(allTogether, width, height, Bitmap.Config.ARGB_8888);
    }

    public class Result {
        private Bitmap main;
        private Bitmap blur;

        Result(Bitmap main, Bitmap blur) {
            this.main = main;
            this.blur = blur;
        }

        Bitmap getMain() {
            return main;
        }

        public Bitmap getBlur() {
            return blur;
        }
    }
}
