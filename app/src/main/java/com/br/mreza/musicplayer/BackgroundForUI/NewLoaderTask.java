package com.br.mreza.musicplayer.BackgroundForUI;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.media.MediaMetadataRetriever;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RSRuntimeException;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;

import com.br.mreza.musicplayer.MBN.bitmapUtils.MbnUtils;
import com.br.mreza.musicplayer.R;

import mbn.libs.backgroundtask.BaseTaskHolder;


public class NewLoaderTask implements BaseTaskHolder.BaseTask {

    private long id;
    private String path;
    private Resources resources;
    private Context context;

    private String TAG = "BLUR_TEST";


    private int[] originalPixels, blurPixels;
    private int threadCount;
    private int doneThreads = 0;
    private boolean isDone = false;
    private int width;
    private int height;
    private boolean change;

    private int getPixelAt(int a) {
        return originalPixels[a];
    }

    private void setBlurPixelAt(int index, int pixel) {
        blurPixels[index] = pixel;
    }

    private synchronized void threadDone() {
        doneThreads++;
        if (doneThreads >= threadCount) {
            isDone = true;
            this.notifyAll();
//            Log.i(TAG, "onNotify: ");
        }
    }

    NewLoaderTask(long id, String path, Context context, boolean change) {
        this.id = id;
        this.path = path;
        this.context = context;
        resources = context.getResources();
        this.change = change;
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

        if (true)
            return new Result(main, MbnUtils.stackBlur(MbnUtils.createSmallBit(main, 300), 7, true), change);


//        try {
//            return new Result(main, rs(context, main, 15), change);
//        } catch (Exception e) {
//            return new Result(main, stackBlur(main, 15, false), change);
//        }


//        mPalette = Palette.from(main).generate();
//        blur = createBlur(main);
//        blur = createBlur2(main);


        createArray(main);

        threadCount = 3;
        int currentSpot = 0;
        int interval = originalPixels.length / threadCount;
        for (int i = 0; i < threadCount; i++) {
            if (i == threadCount - 1) {
                new BlurringThread(currentSpot, originalPixels.length).start();
            } else {
                new BlurringThread(currentSpot, currentSpot += interval).start();
            }
        }

        while (!isDone) {
            synchronized (this) {
                try {
//                    Log.i(TAG, "onBeforeW: ");
                    this.wait();
                } catch (InterruptedException ignored) {
                }
            }
        }

//        Log.i(TAG, "onResult: ");
//        return new Result(main, Bitmap.createBitmap(blurPixels, width, height, Bitmap.Config.ARGB_8888));
        return new Result(main, Bitmap.createBitmap(originalPixels, width, height, Bitmap.Config.ARGB_8888), change);

    }

    @Override
    public Object getInfo() {
        return id;
    }

    private void createArray(Bitmap bitmap) {
        float bigOne = 300f / Math.max(bitmap.getWidth(), bitmap.getHeight());

        width = (int) (bitmap.getWidth() * bigOne);
        height = (int) (bitmap.getHeight() * bigOne);

//        int width = bitmap.getWidth();
//        int height = bitmap.getHeight();

        Bitmap bity = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas cany = new Canvas(bity);
        cany.drawBitmap(bitmap, null, new Rect(0, 0, width, height), null);

        originalPixels = new int[width * height];
//        blurPixels = new int[width * height];
        bity.getPixels(originalPixels, 0, width, 0, 0, width, height);
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

        for (int x = 0; x < 5; x++) {
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

    private Bitmap rs(Context context, Bitmap bitmap, int radius) throws RSRuntimeException {
        RenderScript rs = null;
        Allocation input = null;
        Allocation output = null;
        ScriptIntrinsicBlur blur = null;
        Bitmap outBit;
        try {
            outBit = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
            rs = RenderScript.create(context);
            rs.setMessageHandler(new RenderScript.RSMessageHandler());
            input = Allocation.createFromBitmap(rs, bitmap, Allocation.MipmapControl.MIPMAP_NONE,
                    Allocation.USAGE_SCRIPT);
            output = Allocation.createTyped(rs, input.getType());
            blur = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs));

            blur.setInput(input);
            blur.setRadius(radius);
            blur.forEach(output);
            output.copyTo(outBit);
//            output.copyTo(bitmap);
        } finally {
            if (rs != null) {
                rs.destroy();
            }
            if (input != null) {
                input.destroy();
            }
            if (output != null) {
                output.destroy();
            }
            if (blur != null) {
                blur.destroy();
            }
        }

        return outBit;
    }

    private static Bitmap stack(Bitmap sentBitmap, int radius, boolean canReuseInBitmap) {

        // Stack Blur v1.0 from
        // http://www.quasimondo.com/StackBlurForCanvas/StackBlurDemo.html
        //
        // Java Author: Mario Klingemann <mario at quasimondo.com>
        // http://incubator.quasimondo.com
        // created Feburary 29, 2004
        // Android port : Yahel Bouaziz <yahel at kayenko.com>
        // http://www.kayenko.com
        // ported april 5th, 2012

        // This is a compromise between Gaussian Blur and Box blur
        // It creates much better looking blurs than Box Blur, but is
        // 7x faster than my Gaussian Blur implementation.
        //
        // I called it Stack Blur because this describes best how this
        // filter works internally: it creates a kind of moving stackBlur
        // of colors whilst scanning through the image. Thereby it
        // just has to add one new block of color to the right side
        // of the stackBlur and remove the leftmost color. The remaining
        // colors on the topmost layer of the stackBlur are either added on
        // or reduced by one, depending on if they are on the right or
        // on the left side of the stackBlur.
        //
        // If you are using this algorithm in your code please add
        // the following line:
        //
        // Stack Blur Algorithm by Mario Klingemann <mario@quasimondo.com>

        Bitmap bitmap;
        if (canReuseInBitmap) {
            bitmap = sentBitmap;
        } else {
            bitmap = sentBitmap.copy(sentBitmap.getConfig(), true);
        }

        if (radius < 1) {
            return (null);
        }

        int w = bitmap.getWidth();
        int h = bitmap.getHeight();

        int[] pix = new int[w * h];
        bitmap.getPixels(pix, 0, w, 0, 0, w, h);

        int wm = w - 1;
        int hm = h - 1;
        int wh = w * h;
        int div = radius + radius + 1;

        int r[] = new int[wh];
        int g[] = new int[wh];
        int b[] = new int[wh];
        int rsum, gsum, bsum, x, y, i, p, yp, yi, yw;
        int vmin[] = new int[Math.max(w, h)];

        int divsum = (div + 1) >> 1;
        divsum *= divsum;
        int dv[] = new int[256 * divsum];
        for (i = 0; i < 256 * divsum; i++) {
            dv[i] = (i / divsum);
        }

        yw = yi = 0;

        int[][] stack = new int[div][3];
        int stackpointer;
        int stackstart;
        int[] sir;
        int rbs;
        int r1 = radius + 1;
        int routsum, goutsum, boutsum;
        int rinsum, ginsum, binsum;

        for (y = 0; y < h; y++) {
            rinsum = ginsum = binsum = routsum = goutsum = boutsum = rsum = gsum = bsum = 0;
            for (i = -radius; i <= radius; i++) {
                p = pix[yi + Math.min(wm, Math.max(i, 0))];
                sir = stack[i + radius];
                sir[0] = (p & 0xff0000) >> 16;
                sir[1] = (p & 0x00ff00) >> 8;
                sir[2] = (p & 0x0000ff);
                rbs = r1 - Math.abs(i);
                rsum += sir[0] * rbs;
                gsum += sir[1] * rbs;
                bsum += sir[2] * rbs;
                if (i > 0) {
                    rinsum += sir[0];
                    ginsum += sir[1];
                    binsum += sir[2];
                } else {
                    routsum += sir[0];
                    goutsum += sir[1];
                    boutsum += sir[2];
                }
            }
            stackpointer = radius;

            for (x = 0; x < w; x++) {

                r[yi] = dv[rsum];
                g[yi] = dv[gsum];
                b[yi] = dv[bsum];

                rsum -= routsum;
                gsum -= goutsum;
                bsum -= boutsum;

                stackstart = stackpointer - radius + div;
                sir = stack[stackstart % div];

                routsum -= sir[0];
                goutsum -= sir[1];
                boutsum -= sir[2];

                if (y == 0) {
                    vmin[x] = Math.min(x + radius + 1, wm);
                }
                p = pix[yw + vmin[x]];

                sir[0] = (p & 0xff0000) >> 16;
                sir[1] = (p & 0x00ff00) >> 8;
                sir[2] = (p & 0x0000ff);

                rinsum += sir[0];
                ginsum += sir[1];
                binsum += sir[2];

                rsum += rinsum;
                gsum += ginsum;
                bsum += binsum;

                stackpointer = (stackpointer + 1) % div;
                sir = stack[(stackpointer) % div];

                routsum += sir[0];
                goutsum += sir[1];
                boutsum += sir[2];

                rinsum -= sir[0];
                ginsum -= sir[1];
                binsum -= sir[2];

                yi++;
            }
            yw += w;
        }
        for (x = 0; x < w; x++) {
            rinsum = ginsum = binsum = routsum = goutsum = boutsum = rsum = gsum = bsum = 0;
            yp = -radius * w;
            for (i = -radius; i <= radius; i++) {
                yi = Math.max(0, yp) + x;

                sir = stack[i + radius];

                sir[0] = r[yi];
                sir[1] = g[yi];
                sir[2] = b[yi];

                rbs = r1 - Math.abs(i);

                rsum += r[yi] * rbs;
                gsum += g[yi] * rbs;
                bsum += b[yi] * rbs;

                if (i > 0) {
                    rinsum += sir[0];
                    ginsum += sir[1];
                    binsum += sir[2];
                } else {
                    routsum += sir[0];
                    goutsum += sir[1];
                    boutsum += sir[2];
                }

                if (i < hm) {
                    yp += w;
                }
            }
            yi = x;
            stackpointer = radius;
            for (y = 0; y < h; y++) {
                // Preserve alpha channel: ( 0xff000000 & pix[yi] )
                pix[yi] = (0xff000000 & pix[yi]) | (dv[rsum] << 16) | (dv[gsum] << 8) | dv[bsum];

                rsum -= routsum;
                gsum -= goutsum;
                bsum -= boutsum;

                stackstart = stackpointer - radius + div;
                sir = stack[stackstart % div];

                routsum -= sir[0];
                goutsum -= sir[1];
                boutsum -= sir[2];

                if (x == 0) {
                    vmin[y] = Math.min(y + r1, hm) * w;
                }
                p = x + vmin[y];

                sir[0] = r[p];
                sir[1] = g[p];
                sir[2] = b[p];

                rinsum += sir[0];
                ginsum += sir[1];
                binsum += sir[2];

                rsum += rinsum;
                gsum += ginsum;
                bsum += binsum;

                stackpointer = (stackpointer + 1) % div;
                sir = stack[stackpointer];

                routsum += sir[0];
                goutsum += sir[1];
                boutsum += sir[2];

                rinsum -= sir[0];
                ginsum -= sir[1];
                binsum -= sir[2];

                yi += w;
            }
        }

        bitmap.setPixels(pix, 0, w, 0, 0, w, h);

        return bitmap;
    }

    public class Result {
        private Bitmap main;
        private Bitmap blur;
        private boolean change;

        public Result(Bitmap main, Bitmap blur, boolean change) {
            this.main = main;
            this.blur = blur;
            this.change = change;
        }

        Bitmap getMain() {
            return main;
        }

        public Bitmap getBlur() {
            return blur;
        }

        public boolean isChange() {
            return change;
        }
    }


    private class BlurringThread extends Thread {
        private int startPoint, endPoint;


        BlurringThread(int start, int end) {
            this.startPoint = start;
            this.endPoint = end;
        }


        @Override
        public void run() {
//            super.run();

//            Log.i(TAG, "run: " + startPoint + " _ " + endPoint);

            int oneIndex, threeIndex, forIndex, fiveIndex, twoIndex,
                    one, two, three, four, five, six, seven, eight, nine, total,
                    r1, r2, r3, r4, r5, r6, r7, r8, r9, rt,
                    b1, b2, b3, b4, b5, b6, b7, b8, b9, bt,
                    g1, g2, g3, g4, g5, g6, g7, g8, g9, gt,
                    a1, a2, a3, a4, a5, a6, a7, a8, a9, at;

            for (int x = 0; x < 9; x++) {
                for (int i = startPoint; i < endPoint; i++) {
                    if ((i + 1) > width && (i + 1) % width > 1 && (i + 1) / width < height - 1) {

                        oneIndex = i;
                        try {

                            twoIndex = oneIndex - 1;
                            threeIndex = oneIndex + 1;
                            forIndex = oneIndex - width;
                            fiveIndex = oneIndex + width;

                            one = originalPixels[oneIndex];
                            r1 = Color.red(one);
                            b1 = Color.blue(one);
                            g1 = Color.green(one);
                            a1 = Color.alpha(one);

                            two = originalPixels[twoIndex];
                            r2 = Color.red(two);
                            b2 = Color.blue(two);
                            g2 = Color.green(two);
                            a2 = Color.alpha(two);

                            three = originalPixels[threeIndex];
                            r3 = Color.red(three);
                            b3 = Color.blue(three);
                            g3 = Color.green(three);
                            a3 = Color.alpha(three);

                            four = originalPixels[forIndex];
                            r4 = Color.red(four);
                            b4 = Color.blue(four);
                            g4 = Color.green(four);
                            a4 = Color.alpha(four);

                            six = originalPixels[forIndex + 1];
                            r6 = Color.red(six);
                            b6 = Color.blue(six);
                            g6 = Color.green(six);
                            a6 = Color.alpha(six);

                            seven = originalPixels[forIndex - 1];
                            r7 = Color.red(seven);
                            b7 = Color.blue(seven);
                            g7 = Color.green(seven);
                            a7 = Color.alpha(seven);

                            five = originalPixels[fiveIndex];
                            r5 = Color.red(five);
                            b5 = Color.blue(five);
                            g5 = Color.green(five);
                            a5 = Color.alpha(five);

                            eight = originalPixels[fiveIndex - 1];
                            r8 = Color.red(eight);
                            b8 = Color.blue(eight);
                            g8 = Color.green(eight);
                            a8 = Color.alpha(eight);

                            nine = originalPixels[fiveIndex + 1];
                            r9 = Color.red(nine);
                            b9 = Color.blue(nine);
                            g9 = Color.green(nine);
                            a9 = Color.alpha(nine);

                            rt = (r1 + r2 + r3 + r4 + r5 + r6 + r7 + r8 + r9) / 9;
                            bt = (b1 + b2 + b3 + b4 + b5 + b6 + b7 + b8 + b9) / 9;
                            gt = (g1 + g2 + g3 + g4 + g5 + g6 + g7 + g8 + g9) / 9;
                            at = (a1 + a2 + a3 + a4 + a5 + a6 + a7 + a8 + a9) / 9;

                            total = Color.argb(at, rt, gt, bt);
                            originalPixels[oneIndex] = total;
                        } catch (Exception ignored) {
                        }
                    }
                }
            }
            threadDone();

        }
    }

}
