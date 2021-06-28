package mbn.libs.imagelibs.imageworks;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Handler;
import android.os.Message;
import androidx.palette.graphics.Palette;



public class ImageMakerThread extends Thread {
    public static final int BLUR = 0;
    public static final int BLACK = 1;
    public static final int WHITE = 2;
    public static final int AVERAGE = 3;

    private int style = 0;
    private String path;
    private Handler handler;

    private int width, height, squareWidth;


    public ImageMakerThread(int style, String path, Handler handler) {
        this.style = style;
        this.path = path;
        this.handler = handler;
        start();
    }

    private void findMeasures() {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);
        width = options.outWidth;
        height = options.outHeight;
        squareWidth = Math.max(width, height);
    }

    private void bakeTheFinalImage(Canvas canvas) {
        Rect rect = new Rect();
        int extWidth, extHeight;

        extWidth = width != squareWidth ? (squareWidth - width) / 2 : 0;
        extHeight = height != squareWidth ? (squareWidth - height) / 2 : 0;

        rect.left = extWidth;
        rect.right = squareWidth - extWidth;
        rect.top = extHeight;
        rect.bottom = squareWidth - extHeight;

        canvas.drawBitmap(BitmapFactory.decodeFile(path), null, rect, null);


    }

    private void findAverage(Canvas canvas) {
        Palette palette = Palette.from(BitmapFactory.decodeFile(path)).generate();
        canvas.drawColor(palette.getDominantColor(Color.BLACK));
    }

    @SuppressWarnings("SuspiciousNameCombination")
    @Override
    public void run() {
        findMeasures();

//        Picture outPci = new Picture();
//        Canvas outPicCan = outPci.beginRecording(squareWidth, squareWidth);

        Bitmap outBitmap = Bitmap.createBitmap(squareWidth, squareWidth, Bitmap.Config.ARGB_8888);
        Canvas outCanvas = new Canvas(outBitmap);

        switch (style) {
            case BLACK:
//                outPicCan.drawColor(Color.BLACK);
                outCanvas.drawColor(Color.BLACK);
                break;
            case WHITE:
//                outPicCan.drawColor(Color.WHITE);
                outCanvas.drawColor(Color.WHITE);
                break;
            case BLUR:
//                makeBlur(outPicCan);
                makeBlur2(outCanvas);
                break;
            case AVERAGE:
//                findAverage(outPicCan);
                findAverage(outCanvas);
                break;
        }


//        bakeTheFinalImage(outPicCan);
        bakeTheFinalImage(outCanvas);
//        outPci.draw(outCanvas);
//        MainActivity.bitmapHashMap.put(style, outBitmap);
        Message message = Message.obtain(handler);
        message.arg1 = 0;
        message.arg2 = style;
        message.sendToTarget();
    }


    private void makeBlur(Canvas canvas) {

        BitmapFactory.Options options = new BitmapFactory.Options();
//        options.inSampleSize = (int) Math.ceil(squareWidth / 500f);
        options.inSampleSize = squareWidth / 500;
        Bitmap bitmap = BitmapFactory.decodeFile(path, options);

        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
//        Bitmap inComeBitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true);
//            Bitmap inComeBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
//            Rect rect = new Rect(0, 0, width, height);
//            Canvas canvas = new Canvas(inComeBitmap);
//            canvas.drawBitmap(bitmap, null, rect, null);

        int[] allTogether = new int[width * height];
        bitmap.getPixels(allTogether, 0, width, 0, 0, width, height);
        int oneIndex, threeIndex, forIndex, fiveIndex, twoIndex, one, two, three, four, five, six, seven, eight, nine, total,
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

        Bitmap blur = Bitmap.createBitmap(allTogether, width, height, Bitmap.Config.ARGB_8888);

        float factor = ((float) squareWidth) / Math.min(width, height);

        int useW = (int) (factor * width);
        int useH = (int) (factor * height);

        Rect rect = new Rect();
        int extWidth, extHeight;

        extWidth = (squareWidth - useW) / 2;
        extHeight = (squareWidth - useH) / 2;

        rect.left = extWidth;
        rect.right = squareWidth - extWidth;
        rect.top = extHeight;
        rect.bottom = squareWidth - extHeight;

        canvas.drawBitmap(blur, null, rect, null);

    }

    private void makeBlur2(Canvas canvas) {

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = squareWidth / 500;
        Bitmap bitmap = BitmapFactory.decodeFile(path, options);

        int width = bitmap.getWidth();
        int height = bitmap.getHeight();


        Bitmap blur = MbnUtils.stackBlur(MbnUtils.createSmallBit(bitmap, 500), 15, true);
        bitmap.recycle();

        float factor = ((float) squareWidth) / Math.min(width, height);

        int useW = (int) (factor * width);
        int useH = (int) (factor * height);

        Rect rect = new Rect();
        int extWidth, extHeight;

        extWidth = (squareWidth - useW) / 2;
        extHeight = (squareWidth - useH) / 2;

        rect.left = extWidth;
        rect.right = squareWidth - extWidth;
        rect.top = extHeight;
        rect.bottom = squareWidth - extHeight;

        canvas.drawBitmap(blur, null, rect, null);

    }


}
