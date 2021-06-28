package com.br.mreza.musicplayer.MBN.bitmapUtils;


import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Rect;

import java.util.Random;

public class MbnBitmapBucket {

    public final static int MBN_PAINING = 1;
    public final static int MBN_BLUR = 2;
    public final static int MBN_BLACK_AND_WHITE = 3;
    //    public final static int MBN_AVERAGE_FINDER = 4;
    public final static int MBN_WEAK = 1;
    public final static int MBN_MID = 5;
    public final static int MBN_HIGH = 10;

    public static Bitmap imageMaster(Bitmap bitmap, int mode, int power) {

        int width = bitmap.getWidth();
        int height = bitmap.getHeight();

        Bitmap output = Bitmap.createBitmap(bitmap.getWidth() / 4, bitmap.getHeight() / 4, Bitmap.Config.ARGB_8888);
        Rect rect = new Rect(0, 0, bitmap.getWidth() / 4, bitmap.getHeight() / 4);
        Canvas canvas = new Canvas(output);
        Matrix matrix = new Matrix();
        matrix.postScale(0.25f, 0.25f);
//        canvas.drawBitmap(bitmap, null, rect, null);
        canvas.drawBitmap(bitmap, matrix, null);


        int[] pixels = new int[(width / 4) * (height / 4)];

        int pixel;
        int pixelCount = 0;

        for (int i = 0; i < height / 4; i++) {
            for (int a = 0; a < width / 4; a++) {

                pixel = output.getPixel(a, i);
                pixels[pixelCount] = pixel;


                pixelCount++;
            }


        }

        if (mode == MBN_BLUR) {

            output.setPixels(blurWork(pixels, width / 4, power), 0, width / 4, 0, 0, width / 4, height / 4);
        }
        if (mode == MBN_PAINING) {
            output.setPixels(paintingWork(pixels, width / 4, power), 0, width / 4, 0, 0, width / 4, height / 4);
        }
        if (mode == MBN_BLACK_AND_WHITE) {
            output.setPixels(blackAndWhite(pixels), 0, width / 4, 0, 0, width / 4, height / 4);
        }
        return output;
    }


    private static int[] blurWork(int[] a, int w, int power) {

//        Random random = new Random();
        int counter = 0;
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

//        int avreg = a[0];
        System.out.println(a.length * 4);
        while (counter <= power) {

            for (int i = 0; i < a.length; i++) {
                oneIndex = i;
//            if (oneIndex != 0 && oneIndex < a.length - 10) {
                try {

//
//                twoIndex = random.nextInt(w) + oneIndex;
//                forIndex = random.nextInt(w) + oneIndex;
//                threeIndex = random.nextInt(w) + oneIndex;
//                fiveIndex = random.nextInt(w) + oneIndex;
                    twoIndex = oneIndex - 1;
                    threeIndex = oneIndex + 1;
                    forIndex = oneIndex - w;
                    fiveIndex = oneIndex + w;

                    one = a[oneIndex];
                    r1 = Color.red(one);
                    b1 = Color.blue(one);
                    g1 = Color.green(one);
                    a1 = Color.alpha(one);

                    two = a[twoIndex];
                    r2 = Color.red(two);
                    b2 = Color.blue(two);
                    g2 = Color.green(two);
                    a2 = Color.alpha(two);

                    three = a[threeIndex];
                    r3 = Color.red(three);
                    b3 = Color.blue(three);
                    g3 = Color.green(three);
                    a3 = Color.alpha(three);

                    four = a[forIndex];
                    r4 = Color.red(four);
                    b4 = Color.blue(four);
                    g4 = Color.green(four);
                    a4 = Color.alpha(four);

                    six = a[forIndex + 1];
                    r6 = Color.red(six);
                    b6 = Color.blue(six);
                    g6 = Color.green(six);
                    a6 = Color.alpha(six);

                    seven = a[forIndex - 1];
                    r7 = Color.red(seven);
                    b7 = Color.blue(seven);
                    g7 = Color.green(seven);
                    a7 = Color.alpha(seven);

                    five = a[fiveIndex];
                    r5 = Color.red(five);
                    b5 = Color.blue(five);
                    g5 = Color.green(five);
                    a5 = Color.alpha(five);

                    eight = a[fiveIndex - 1];
                    r8 = Color.red(eight);
                    b8 = Color.blue(eight);
                    g8 = Color.green(eight);
                    a8 = Color.alpha(eight);

                    nine = a[fiveIndex + 1];
                    r9 = Color.red(nine);
                    b9 = Color.blue(nine);
                    g9 = Color.green(nine);
                    a9 = Color.alpha(nine);

                    rt = (r1 + r2 + r3 + r4 + r5 + r6 + r7 + r8 + r9) / 9;
                    bt = (b1 + b2 + b3 + b4 + b5 + b6 + b7 + b8 + b9) / 9;
                    gt = (g1 + g2 + g3 + g4 + g5 + g6 + g7 + g8 + g9) / 9;
                    at = (a1 + a2 + a3 + a4 + a5 + a6 + a7 + a8 + a9) / 9;

                    total = Color.argb(at, rt, gt, bt);

                    a[oneIndex] = total;
                    a[twoIndex] = total;
                    a[threeIndex] = total;
                    a[forIndex] = total;
                    a[fiveIndex] = total;
                    a[fiveIndex - 1] = total;
                    a[fiveIndex + 1] = total;
                    a[forIndex - 1] = total;
                    a[forIndex + 1] = total;


//                    a[fiveIndex + w] = total;
//                    a[twoIndex - 1] = total;
//                    a[threeIndex + 1] = total;
//                    a[forIndex - w] = total;

//                    a[fiveIndex - 2] = total;
//                    a[fiveIndex + 2] = total;
//                    a[twoIndex - 2] = total;
//                    a[threeIndex + 2] = total;
//                    a[forIndex - 2] = total;
//                    a[forIndex + 2] = total;
//                    a[fiveIndex + w + 1] = total;
//                    a[forIndex - w + 1] = total;
//                    a[fiveIndex + w - 1] = total;
//                    a[forIndex - w - 1] = total;


                } catch (Exception ignored) {
                }


//            a[counter] = avreg;
//            counter++;

//            avreg = (avreg + a[counter]) / 2;
            }
            counter++;
        }

        return a;
    }


    private static int[] paintingWork(int[] a, int w, int power) {
        Random random = new Random();
        int counter = 0;
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

//        int avreg = a[0];
        System.out.println(a.length * 4);

        while (counter < (a.length / 22) * power) {
            oneIndex = random.nextInt(a.length);
//            if (oneIndex != 0 && oneIndex < a.length - 10) {
            try {

//
//                twoIndex = random.nextInt(w) + oneIndex;
//                forIndex = random.nextInt(w) + oneIndex;
//                threeIndex = random.nextInt(w) + oneIndex;
//                fiveIndex = random.nextInt(w) + oneIndex;
                twoIndex = oneIndex - 1;
                threeIndex = oneIndex + 1;
                forIndex = oneIndex - w;
                fiveIndex = oneIndex + w;

                one = a[oneIndex];
                r1 = Color.red(one);
                b1 = Color.blue(one);
                g1 = Color.green(one);
                a1 = Color.alpha(one);

                two = a[twoIndex];
                r2 = Color.red(two);
                b2 = Color.blue(two);
                g2 = Color.green(two);
                a2 = Color.alpha(two);

                three = a[threeIndex];
                r3 = Color.red(three);
                b3 = Color.blue(three);
                g3 = Color.green(three);
                a3 = Color.alpha(three);

                four = a[forIndex];
                r4 = Color.red(four);
                b4 = Color.blue(four);
                g4 = Color.green(four);
                a4 = Color.alpha(four);

                six = a[forIndex + 1];
                r6 = Color.red(six);
                b6 = Color.blue(six);
                g6 = Color.green(six);
                a6 = Color.alpha(six);

                seven = a[forIndex - 1];
                r7 = Color.red(seven);
                b7 = Color.blue(seven);
                g7 = Color.green(seven);
                a7 = Color.alpha(seven);

                five = a[fiveIndex];
                r5 = Color.red(five);
                b5 = Color.blue(five);
                g5 = Color.green(five);
                a5 = Color.alpha(five);

                eight = a[fiveIndex - 1];
                r8 = Color.red(eight);
                b8 = Color.blue(eight);
                g8 = Color.green(eight);
                a8 = Color.alpha(eight);

                nine = a[fiveIndex + 1];
                r9 = Color.red(nine);
                b9 = Color.blue(nine);
                g9 = Color.green(nine);
                a9 = Color.alpha(nine);

                rt = (r1 + r2 + r3 + r4 + r5 + r6 + r7 + r8 + r9) / 9;
                bt = (b1 + b2 + b3 + b4 + b5 + b6 + b7 + b8 + b9) / 9;
                gt = (g1 + g2 + g3 + g4 + g5 + g6 + g7 + g8 + g9) / 9;
                at = (a1 + a2 + a3 + a4 + a5 + a6 + a7 + a8 + a9) / 9;

                total = Color.argb(at, rt, gt, bt);

                a[oneIndex] = total;
                a[twoIndex] = total;
                a[threeIndex] = total;
                a[forIndex] = total;
                a[fiveIndex] = total;
                a[fiveIndex - 1] = total;
                a[fiveIndex + 1] = total;
                a[forIndex - 1] = total;
                a[forIndex + 1] = total;


                a[fiveIndex + w] = total;
                a[twoIndex - 1] = total;
                a[threeIndex + 1] = total;
                a[forIndex - w] = total;

                a[fiveIndex - 2] = total;
                a[fiveIndex + 2] = total;
                a[twoIndex - 2] = total;
                a[threeIndex + 2] = total;
                a[forIndex - 2] = total;
                a[forIndex + 2] = total;
                a[fiveIndex + w + 1] = total;
                a[forIndex - w + 1] = total;
                a[fiveIndex + w - 1] = total;
                a[forIndex - w - 1] = total;


            } catch (Exception ignored) {
            }


//            a[counter] = avreg;
//            counter++;

//            avreg = (avreg + a[counter]) / 2;

            counter++;
        }


        return a;
    }


    private static int[] blackAndWhite(int[] a) {

        int red;
        int green;
        int blue;
        int alpha;
        int total;

        for (int i = 0; i < a.length; i++) {

            red = Color.red(a[i]);
            green = Color.green(a[i]);
            blue = Color.blue(a[i]);
            alpha = Color.alpha(a[i]);

            total = (red + green + blue) / 3;

            a[i] = Color.argb(alpha, total, total, total);


        }


        return a;
    }


    public static Bitmap toneChanger(Bitmap bitmap, int color) {

        int width = bitmap.getWidth();
        int height = bitmap.getHeight();

        Bitmap output = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        canvas.drawBitmap(bitmap, 0, 0, null);

        int[] pixels = new int[width * height];

        int pixel;
        int pixelCount = 0;

        for (int i = 0; i < height; i++) {

            for (int a = 0; a < width; a++) {

                pixel = output.getPixel(a, i);

                pixels[pixelCount] = pixel;


                pixelCount++;
            }


        }

//        pixels = blackAndWhite(pixels);

        pixels = toneChangerBackgroundWork(pixels, color);


        output.setPixels(pixels, 0, width, 0, 0, width, height);


        return output;
    }


    private static int[] toneChangerBackgroundWork(int[] a, int color) {

        int red;
        int green;
        int blue;
        int alpha;
//        int total;
        int pattern_red = Color.red(color);
        int pattern_green = Color.green(color);
        int pattern_blue = Color.blue(color);


//        int pattern_alpha;
//        int pattern_total;

        for (int i = 0; i < a.length; i++) {

            red = ((Color.red(a[i])) + pattern_red) / 2;
            green = (Color.green(a[i]) + pattern_green) / 2;
            blue = (Color.blue(a[i]) + pattern_blue) / 2;
            alpha = Color.alpha(a[i]);


//            total = (red + green + blue) / 3;

            a[i] = Color.argb(alpha, red, green, blue);


        }


        return a;


    }


    public static int averageFinder(Bitmap bitmap) {

        int width = bitmap.getWidth() / 8;
        int height = bitmap.getHeight() / 8;

        Bitmap output = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Rect rect = new Rect(0, 0, width, height);
        Canvas canvas = new Canvas(output);
//        Matrix matrix = new Matrix();
//        matrix.postScale(0.25f, 0.25f);
        canvas.drawBitmap(bitmap, null, rect, null);
//        canvas.drawBitmap(bitmap, matrix, null);


        int[] a = new int[(width) * (height)];
//        int[] a = new int[1];

        int pixel;
        int pixelCount = 0;

        for (int i = 0; i < height; i++) {
            for (int b = 0; b < width; b++) {

                pixel = output.getPixel(b, i);
                a[pixelCount] = pixel;


                pixelCount++;
            }


        }


        int red = Color.red(a[0]);

        int green = Color.green(a[0]);

        int blue = Color.blue(a[0]);

        int alpha = Color.alpha(a[0]);

        for (int i = 1; i < a.length; i++) {

            red = red + Color.red(a[i]);

            green = green + Color.green(a[i]);

            blue = blue + Color.blue(a[i]);

            alpha = alpha + Color.alpha(a[i]);


        }


        red = red / a.length;
        green = green / a.length;
        blue = blue / a.length;
        alpha = alpha / a.length;

//        if (red < 100) {
//
//            red += 50;
//        }
//        if (green < 100) {
//
//            green += 50;
//        }
//        if (blue < 100) {
//
//            blue += 50;
//        }
//        if (alpha < 100) {
//
//            alpha += 50;
//        }


        return Color.argb(255, red - 20, green - 20, blue - 20);
    }


    public static int averageOf2Colors(int c1, int c2) {

        int balance = 110;

        int a1 = Color.alpha(c1);
        int r1 = Color.red(c1);
        int b1 = Color.blue(c1);
        int g1 = Color.green(c1);
        if (Math.min(Math.min(r1, b1), g1) < 120) {


            r1 += balance;
            b1 += balance;
            g1 += balance;

        }

        int a2 = Color.alpha(c2);
        int r2 = Color.red(c2);
        int b2 = Color.blue(c2);
        int g2 = Color.green(c2);

        int aF = (a1 + a2) / 2;
        int rF = (r1 + r2) / 2;
        int bF = (b1 + b2) / 2;
        int gF = (g1 + g2) / 2;


        return Color.argb(aF, rF, gF, bF);


    }


    public static int makeColorBright(int color) {


        int balance = 120;

        int a1 = Color.alpha(color);
        int r1 = Color.red(color);
        int b1 = Color.blue(color);
        int g1 = Color.green(color);
        if (Math.max(Math.max(r1, b1), g1) < 120) {


            r1 += balance;
            b1 += balance;
            g1 += balance;

        }


        return Color.argb(a1, r1, g1, b1);
    }

}
