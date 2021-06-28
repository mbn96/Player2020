package com.br.mreza.musicplayer.MBN.bitmapUtils;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.media.MediaMetadataRetriever;
import android.os.AsyncTask;

import com.br.mreza.musicplayer.R;

import java.io.File;

public class AdvancedMbnBlur {

    private int width;
    private int height;
    private int[] allTogether;
    //    private ArrayList<BlurringThread> threads = new ArrayList<>();

//    private Bitmap input;


    public void makeBlur(Context context, String path) {

//        String temp = path.substring(path.lastIndexOf(File.separator), path.length()) + ".jpg";

        File coverBlur = new File(path);
//        File coverBlur = new File(context.getFilesDir().getPath() + File.separator + "BlurredCovers" + File.separator + temp);

        if (coverBlur.exists()) {

            Bitmap cover = BitmapFactory.decodeFile(coverBlur.getPath());

            if (cover != null) {

//                helper.onFinish(cover);

                width = cover.getWidth();
                height = cover.getHeight();

                allTogether = new int[width * height];

                cover.getPixels(allTogether, 0, width, 0, 0, width, height);

                afterBurner();

            } else {
                new FirstWork().execute(new Carrier(context, path));
            }

        } else {
            new FirstWork().execute(new Carrier(context, path));
        }


    }


    private void afterBurner() {

        new FinalWork().execute();

//
//        Thread a = new Thread(new Runnable() {
//            @Override
//            public void run() {
//
//                threads.clear();
//
//                for (int i = 0; i < 1; i++) {
//
//                    threads.add(new BlurringThread(i));
//
//                }
//
//                startThread(0);
//
//            }
//        });
//        a.start();

    }

//    private void startThread(int pos) {
//
//
//        threads.get(pos).start();
//
//
//    }

    private class Carrier {

        private Context context;
        private String path;

        public Carrier(Context context, String path) {
            this.context = context;
            this.path = path;
        }

        public Context getContext() {
            return context;
        }

        public String getPath() {
            return path;
        }
    }

    private class FirstWork extends AsyncTask<Carrier, Void, Void> {
        @Override
        protected Void doInBackground(Carrier... carriers) {

//            Bitmap bitmap = bitmaps[0];

            Context context = carriers[0].getContext();
            String path = carriers[0].getPath();

            Bitmap bitmap;

            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 6;
            try {
                MediaMetadataRetriever retriever = new MediaMetadataRetriever();
                retriever.setDataSource(path);
                byte[] coverArray = retriever.getEmbeddedPicture();
                retriever.release();
                bitmap = BitmapFactory.decodeByteArray(coverArray, 0, coverArray.length, options);
            } catch (Exception e) {
                options.inSampleSize = 10;
                bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.night_rain_3, options);

            }


            width = bitmap.getWidth();
            height = bitmap.getHeight();
            Bitmap inComeBitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true);
//            Bitmap inComeBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
//            Rect rect = new Rect(0, 0, width, height);
//            Canvas canvas = new Canvas(inComeBitmap);
//            canvas.drawBitmap(bitmap, null, rect, null);

            allTogether = new int[width * height];

            inComeBitmap.getPixels(allTogether, 0, width, 0, 0, width, height);

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            afterBurner();
        }
    }


    private class FinalWork extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {

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

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {


            Bitmap outComeBitmap = Bitmap.createBitmap(allTogether, width, height, Bitmap.Config.ARGB_8888);
//            Bitmap outComeBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);

//            outComeBitmap.setPixels(allTogether, 0, width, 0, 0, width, height);

            helper.onFinish(outComeBitmap);


            super.onPostExecute(aVoid);
        }
    }

    public void setHelper(MbnAdvancedBlurHelper helper) {
        this.helper = helper;
    }

    private MbnAdvancedBlurHelper helper;

    public interface MbnAdvancedBlurHelper {

        void onFinish(Bitmap blurred);

    }


//    private class BlurringThread extends Thread {
//
//        private int pos;
//
//        BlurringThread(int pos) {
//            this.pos = pos;
//        }
//
//        @Override
//        public void run() {
//
//
//            int counter = 0;
//            int oneIndex;
//            int threeIndex;
//            int forIndex;
//            int fiveIndex;
//            int twoIndex;
//            int one;
//            int two;
//            int three;
//            int four;
//            int five;
//            int six;
//            int seven;
//            int eight;
//            int nine;
//            int total;
//            int r1;
//            int r2;
//            int r3;
//            int r4;
//            int r5;
//            int r6;
//            int r7;
//            int r8;
//            int r9;
//            int rt;
//            int b1;
//            int b2;
//            int b3;
//            int b4;
//            int b5;
//            int b6;
//            int b7;
//            int b8;
//            int b9;
//            int bt;
//            int g1;
//            int g2;
//            int g3;
//            int g4;
//            int g5;
//            int g6;
//            int g7;
//            int g8;
//            int g9;
//            int gt;
//            int a1;
//            int a2;
//            int a3;
//            int a4;
//            int a5;
//            int a6;
//            int a7;
//            int a8;
//            int a9;
//            int at;
//
//            for (int x = 0; x < 3; x++) {
//
//                for (int i = 0; i < allTogether.length; i++) {
//                    oneIndex = i;
//                    try {
//
//                        twoIndex = oneIndex - 1;
//                        threeIndex = oneIndex + 1;
//                        forIndex = oneIndex - width;
//                        fiveIndex = oneIndex + width;
//
//                        one = allTogether[oneIndex];
//                        r1 = Color.red(one);
//                        b1 = Color.blue(one);
//                        g1 = Color.green(one);
//                        a1 = Color.alpha(one);
//
//                        two = allTogether[twoIndex];
//                        r2 = Color.red(two);
//                        b2 = Color.blue(two);
//                        g2 = Color.green(two);
//                        a2 = Color.alpha(two);
//
//                        three = allTogether[threeIndex];
//                        r3 = Color.red(three);
//                        b3 = Color.blue(three);
//                        g3 = Color.green(three);
//                        a3 = Color.alpha(three);
//
//                        four = allTogether[forIndex];
//                        r4 = Color.red(four);
//                        b4 = Color.blue(four);
//                        g4 = Color.green(four);
//                        a4 = Color.alpha(four);
//
//                        six = allTogether[forIndex + 1];
//                        r6 = Color.red(six);
//                        b6 = Color.blue(six);
//                        g6 = Color.green(six);
//                        a6 = Color.alpha(six);
//
//                        seven = allTogether[forIndex - 1];
//                        r7 = Color.red(seven);
//                        b7 = Color.blue(seven);
//                        g7 = Color.green(seven);
//                        a7 = Color.alpha(seven);
//
//                        five = allTogether[fiveIndex];
//                        r5 = Color.red(five);
//                        b5 = Color.blue(five);
//                        g5 = Color.green(five);
//                        a5 = Color.alpha(five);
//
//                        eight = allTogether[fiveIndex - 1];
//                        r8 = Color.red(eight);
//                        b8 = Color.blue(eight);
//                        g8 = Color.green(eight);
//                        a8 = Color.alpha(eight);
//
//                        nine = allTogether[fiveIndex + 1];
//                        r9 = Color.red(nine);
//                        b9 = Color.blue(nine);
//                        g9 = Color.green(nine);
//                        a9 = Color.alpha(nine);
//
//                        rt = (r1 + r2 + r3 + r4 + r5 + r6 + r7 + r8 + r9) / 9;
//                        bt = (b1 + b2 + b3 + b4 + b5 + b6 + b7 + b8 + b9) / 9;
//                        gt = (g1 + g2 + g3 + g4 + g5 + g6 + g7 + g8 + g9) / 9;
//                        at = (a1 + a2 + a3 + a4 + a5 + a6 + a7 + a8 + a9) / 9;
//
//                        total = Color.argb(at, rt, gt, bt);
//
//                        allTogether[oneIndex] = total;
//                        allTogether[twoIndex] = total;
//                        allTogether[threeIndex] = total;
//                        allTogether[forIndex] = total;
//                        allTogether[fiveIndex] = total;
//                        allTogether[fiveIndex - 1] = total;
//                        allTogether[fiveIndex + 1] = total;
//                        allTogether[forIndex - 1] = total;
//                        allTogether[forIndex + 1] = total;
//
//
//                    } catch (Exception ignored) {
//                    }
//
//                    counter++;
//
//                    if (counter == (20 * width) + 150 && threads.size() != pos + 1) {
//
//                        try {
//                            startThread(pos + 1);
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//
//                    }
//
//                }
//            }
//
//            if (pos == threads.size() - 1) {
//                new FinalWork().execute();
//
//            }
//
//
//            super.run();
//
//        }
//    }

}
