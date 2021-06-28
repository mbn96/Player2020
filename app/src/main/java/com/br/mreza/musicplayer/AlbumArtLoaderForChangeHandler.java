package com.br.mreza.musicplayer;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.media.MediaMetadataRetriever;
import androidx.palette.graphics.Palette;


public abstract class AlbumArtLoaderForChangeHandler extends AlbumArtLoaderTask {


    public AlbumArtLoaderForChangeHandler(Context context, String data, int width) {
        super(context, data, width);
    }

    private Bitmap main;
    private Bitmap blur;
    private Palette mPalette;

    public abstract void onFinish(Bitmap main, Bitmap blur);

    public void palette(Palette palette) {
    }

    @Override
    void finish() {

        onFinish(main, blur);
//        palette(mPalette);
    }

    @Override
    public void run() {

        try {
            MediaMetadataRetriever retriever = new MediaMetadataRetriever();
            retriever.setDataSource(getmData());
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
            BitmapFactory.decodeResource(getmContext().getResources(), R.drawable.night_rain_1, options);
            options.inSampleSize = Math.max(options.outHeight, options.outWidth) / 500;
            options.inJustDecodeBounds = false;
            main = BitmapFactory.decodeResource(getmContext().getResources(), R.drawable.night_rain_1, options);

        }

//        mPalette = Palette.from(main).generate();
        createBlur(main);
//        createMain(main);

        super.run();
    }


    private void createBlur(Bitmap bitmap) {

        float bigOne = 200f / Math.max(bitmap.getWidth(), bitmap.getHeight());

        int width = (int) (bitmap.getWidth() * bigOne);
        int height = (int) (bitmap.getHeight() * bigOne);

//        int width = bitmap.getWidth();
//        int height = bitmap.getHeight();

        Bitmap bity = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas cany = new Canvas(bity);
        cany.drawBitmap(bitmap, null, new Rect(0, 0, width, height), null);

        int[] allTogether = new int[width * height];

        bity.getPixels(allTogether, 0, width, 0, 0, width, height);

        int oneIndex;
        int threeIndex;
        int forIndex;
        int fiveIndex;
        int twoIndex;
        int one;
        int two;
        int three;
        int four;
        int five;
        int six;
        int seven;
        int eight;
        int nine;
        int total;
        int r1;
        int r2;
        int r3;
        int r4;
        int r5;
        int r6;
        int r7;
        int r8;
        int r9;
        int rt;
        int b1;
        int b2;
        int b3;
        int b4;
        int b5;
        int b6;
        int b7;
        int b8;
        int b9;
        int bt;
        int g1;
        int g2;
        int g3;
        int g4;
        int g5;
        int g6;
        int g7;
        int g8;
        int g9;
        int gt;
        int a1;
        int a2;
        int a3;
        int a4;
        int a5;
        int a6;
        int a7;
        int a8;
        int a9;
        int at;

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


        blur = Bitmap.createBitmap(allTogether, width, height, Bitmap.Config.ARGB_8888);


    }

    private void createMain(Bitmap bitmap) {

        float bigOne = 500f / Math.max(bitmap.getWidth(), bitmap.getHeight());

        int width = (int) (bitmap.getWidth() * bigOne);
        int height = (int) (bitmap.getHeight() * bigOne);

//        int width = bitmap.getWidth();
//        int height = bitmap.getHeight();

        Bitmap bity = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas cany = new Canvas(bity);
        cany.drawBitmap(bitmap, null, new Rect(0, 0, width, height), null);

        int[] allTogether = new int[width * height];

        bity.getPixels(allTogether, 0, width, 0, 0, width, height);

        int oneIndex;
        int threeIndex;
        int forIndex;
        int fiveIndex;
        int twoIndex;
        int one;
        int two;
        int three;
        int four;
        int five;
        int six;
        int seven;
        int eight;
        int nine;
        int total;
        int r1;
        int r2;
        int r3;
        int r4;
        int r5;
        int r6;
        int r7;
        int r8;
        int r9;
        int rt;
        int b1;
        int b2;
        int b3;
        int b4;
        int b5;
        int b6;
        int b7;
        int b8;
        int b9;
        int bt;
        int g1;
        int g2;
        int g3;
        int g4;
        int g5;
        int g6;
        int g7;
        int g8;
        int g9;
        int gt;
        int a1;
        int a2;
        int a3;
        int a4;
        int a5;
        int a6;
        int a7;
        int a8;
        int a9;
        int at;

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


        main = Bitmap.createBitmap(allTogether, width, height, Bitmap.Config.ARGB_8888);


    }


}
