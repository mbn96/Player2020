package mbn.libs.imagelibs.imageworks;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.os.Build;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.text.TextUtils;

import androidx.annotation.FloatRange;
import androidx.annotation.IntRange;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;


public class Effects2 {

    private static final String TAG = "Effects2";

    //----------------------------- SKETCH -------------------------//

    public static Bitmap sketch2(Bitmap src, int strikeDensity) {
        int width = src.getWidth();
        int height = src.getHeight();
        int strikeLength = (int) Math.sqrt((width * width) + (height * height));
        Path strikes = strikeGenerator(width, height, strikeLength, strikeDensity);
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStrokeWidth(strikeDensity / 2f);
        paint.setColor(Color.GRAY);
        paint.setStyle(Paint.Style.STROKE);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        int[] pixels = Effects.getPixels(src);
        int[] mediumDarkPixels = new int[width * height];
        int[] highDarkPixels = new int[width * height];
        Arrays.fill(mediumDarkPixels, Color.TRANSPARENT);
        Arrays.fill(highDarkPixels, Color.TRANSPARENT);

        float dividerFactor = 1f / 3;
        for (int i = 0; i < pixels.length; i++) {
            float pr = Effects.getMonochromePercentage(pixels[i]);
            if (pr <= dividerFactor) {
                highDarkPixels[i] = Color.BLACK;
            } else if (pr <= dividerFactor * 2) {
                mediumDarkPixels[i] = Color.BLACK;
            }
        }
        Bitmap mediumDarkBitmap = Effects.internalBoxBlur(2, 2, mediumDarkPixels, width, height);
        Bitmap highDarkBitmap = Effects.internalBoxBlur(2, 2, highDarkPixels, width, height);
        mediumDarkPixels = highDarkPixels = null;

        Canvas mediumCanvas = new Canvas(mediumDarkBitmap);
        mediumCanvas.drawColor(Color.WHITE, PorterDuff.Mode.SRC_IN);
        printPath(strikes, paint, 45, 0, width, height, mediumCanvas);


        Canvas highCanvas = new Canvas(highDarkBitmap);
        highCanvas.drawColor(Color.WHITE, PorterDuff.Mode.SRC_IN);
        printPath(strikes, paint, 45, 0, width, height, highCanvas);
        printPath(strikes, paint, -45, 0, width, height, highCanvas);

        int[] edgesPixels = Effects.boxFiltering(pixels, width, height, Effects.EDGE_DETECT_KERNEL);
        for (int i = 0; i < edgesPixels.length; i++) {
            int color = edgesPixels[i];
            float pr = Effects.getMonochromePercentage(color);
            if (pr <= 0.3f) {
                edgesPixels[i] = Color.TRANSPARENT;
            } else {
                edgesPixels[i] = Color.BLACK;
//                edgesPixels[i] = Effects.getMonochromePixel(color);
            }
        }
        Bitmap edgesBitmap = Effects.internalBoxBlur(3, 1, edgesPixels, width, height);
        edgesPixels = null;

        Bitmap out = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas outCanvas = new Canvas(out);
        outCanvas.drawColor(Color.WHITE);
        outCanvas.drawBitmap(mediumDarkBitmap, 0, 0, null);
        outCanvas.drawBitmap(highDarkBitmap, 0, 0, null);
        outCanvas.drawBitmap(edgesBitmap, 0, 0, null);
        return out;
    }

    public static Bitmap sketch2_fix(Bitmap src, int strikeDensity, int t) {
        int width = src.getWidth();
        int height = src.getHeight();
        int strikeLength = (int) Math.sqrt((width * width) + (height * height));
        Path strikes = strikeGenerator(width, height, strikeLength, strikeDensity);
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStrokeWidth(strikeDensity / 2f);
        paint.setColor(Color.GRAY);
        paint.setStyle(Paint.Style.STROKE);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        int[] pixels = Effects.getPixels(src);
        int[] mediumDarkPixels = new int[width * height];
        int[] highDarkPixels = new int[width * height];
        Arrays.fill(mediumDarkPixels, Color.TRANSPARENT);
        Arrays.fill(highDarkPixels, Color.TRANSPARENT);

        float dividerFactor = 1f / 3;
        for (int i = 0; i < pixels.length; i++) {
            float pr = Effects.getMonochromePercentage(pixels[i]);
            if (pr <= dividerFactor) {
                highDarkPixels[i] = Color.BLACK;
            } else if (pr <= dividerFactor * 2) {
                mediumDarkPixels[i] = Color.BLACK;
            }
        }
        Bitmap mediumDarkBitmap = Effects.internalBoxBlur(2, 2, mediumDarkPixels, width, height);
        Bitmap highDarkBitmap = Effects.internalBoxBlur(2, 2, highDarkPixels, width, height);
        mediumDarkPixels = highDarkPixels = null;

        Canvas mediumCanvas = new Canvas(mediumDarkBitmap);
        mediumCanvas.drawColor(Color.WHITE, PorterDuff.Mode.SRC_IN);
        printPath(strikes, paint, 45, 0, width, height, mediumCanvas);


        Canvas highCanvas = new Canvas(highDarkBitmap);
        highCanvas.drawColor(Color.WHITE, PorterDuff.Mode.SRC_IN);
        printPath(strikes, paint, 45, 0, width, height, highCanvas);
        printPath(strikes, paint, -45, 0, width, height, highCanvas);

//        sumArrays(edgesPixels, edgesPixels_negative);
        Bitmap edgesBitmap;
        if (t == 0) {
            int[] edgesPixels = Effects.boxFiltering(pixels, width, height, Effects.EDGE_DETECT_KERNEL);
            findEligibleEdge(edgesPixels);
            edgesBitmap = Effects.internalBoxBlur(2, 1, edgesPixels, width, height);
            edgesPixels = null;
        } else {
            int[] edgesPixels_negative = Effects.boxFiltering(Effects.getNegativePixels_newArray(pixels), width, height, Effects.EDGE_DETECT_KERNEL);
            findEligibleEdge(edgesPixels_negative);
            edgesBitmap = Effects.internalBoxBlur(2, 1, edgesPixels_negative, width, height);
            edgesPixels_negative = null;
        }

        Bitmap out = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas outCanvas = new Canvas(out);
        outCanvas.drawColor(Color.WHITE);
        outCanvas.drawBitmap(mediumDarkBitmap, 0, 0, null);
        outCanvas.drawBitmap(highDarkBitmap, 0, 0, null);
        outCanvas.drawBitmap(edgesBitmap, 0, 0, null);
        return out;
    }

    private static Path strikeGenerator(int width, int height, int size, int density) {
        Path out = new Path();
        out.rewind();
        density = Math.max(density, 2);

        int left = -(size - width) / 2;
        int right = left + size;
        int top = -(size - height) / 2;
        int bottom = top + size;

        for (int i = left; i < right; i += (density + 1)) {
            out.moveTo(i, top);
            out.lineTo(i, bottom);
        }
        return out;
    }

    private static Bitmap getStrikeBitmap(int type, Path path, Paint paint, int background, int width, int height) {
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        canvas.drawColor(background);

        canvas.rotate(45, width / 2f, height / 2f);
        canvas.drawPath(path, paint);
        if (type == 0) {
            return bitmap;
        }
        canvas.rotate(-90, width / 2f, height / 2f);
        canvas.drawPath(path, paint);
        return bitmap;
    }

    private static void printPath(Path path, Paint paint, int degree, float transX, int width, int height, Canvas canvas) {
        canvas.save();
        canvas.translate(transX, 0);
        canvas.rotate(degree, width / 2f, height / 2f);
        canvas.drawPath(path, paint);
        canvas.restore();
    }

    private static void findEligibleEdge(int[] edgesPixels) {
        for (int i = 0; i < edgesPixels.length; i++) {
            int color = edgesPixels[i];
            int maxPart = Math.max(Color.green(color), Math.max(Color.blue(color), Color.red(color)));
            float pr = maxPart / 255f;
            if (pr <= 0.25f) {
                edgesPixels[i] = Color.TRANSPARENT;
            } else {
//                edgesPixels[i] = Effects.getMonoChromeColorFromPercentage(1f - pr);
                edgesPixels[i] = Effects.getMonoChromeColorFromPercentage(1f - Effects.getMonochromePercentage(color));
//                edgesPixels[i] = Color.BLACK;
            }
        }
    }

    private static void findEligibleEdge2(int[] edgesPixels) {
        for (int i = 0; i < edgesPixels.length; i++) {
            int color = edgesPixels[i];
            int maxPart = Math.max(Color.green(color), Math.max(Color.blue(color), Color.red(color)));
            float pr = maxPart / 255f;
            if (pr >= 0.93f) {
                edgesPixels[i] = Color.TRANSPARENT;
            } else {
//                edgesPixels[i] = Effects.getMonoChromeColorFromPercentage(1f - pr);
//                edgesPixels[i] = Effects.getMonochromePixel(color);
                edgesPixels[i] = Color.DKGRAY;
            }
        }
    }

//    private static void findEligibleEdge_sketch4(int[] edgesPixels) {
//        for (int i = 0; i < edgesPixels.length; i++) {
//            int color = edgesPixels[i];
//            int maxPart = Math.max(Color.green(color), Math.max(Color.blue(color), Color.red(color)));
//            float pr = maxPart / 255f;
//            if (pr >= 0.97f) {
//                edgesPixels[i] = Color.TRANSPARENT;
//            } else {
//                edgesPixels[i] = MbnUtils.alphaChanger(Color.BLACK, (int) ((1 - (pr * 0.65f)) * 255));
//            }
//        }
//    }

    private static void findEligibleEdge_sketch4_fix(int[] edgesPixels) {
        for (int i = 0; i < edgesPixels.length; i++) {
            int color = edgesPixels[i];
            int maxPart = Math.max(Color.green(color), Math.max(Color.blue(color), Color.red(color)));
            float pr = maxPart / 255f;
            if (pr <= 0.02f) {
                edgesPixels[i] = Color.TRANSPARENT;
            } else {
                edgesPixels[i] = MbnUtils.alphaChanger(Color.BLACK, (int) ((pr * 3.4f) * 255));
            }
        }
    }

    private static void sumArrays(int[] main, int[] secondary) {
        for (int i = 0; i < main.length; i++) {
            main[i] = main[i] | secondary[i];
        }
    }

    public static Bitmap lensBlur(Bitmap bitmap) {
        Bitmap downScaledBitmap;
        int[] downScaledPixels = Effects.getPixels(downScaledBitmap = MbnUtils.createSmallBit(bitmap, 400));
        int[] lightPixels = new int[downScaledPixels.length];
        Arrays.fill(lightPixels, Color.argb(0, 255, 255, 255));

        for (int i = 0; i < lightPixels.length; i++) {
            int color = downScaledPixels[i];
            float pr = Effects.getMonochromePercentage(color);
            if (pr >= 0.8f) {
                lightPixels[i] = color;
            }
        }
        downScaledPixels = null;
        Bitmap blurredLightsBitmap = Effects.internalBoxBlur(2, 2, lightPixels, downScaledBitmap.getWidth(), downScaledBitmap.getHeight());
        downScaledBitmap.recycle();

        Bitmap out = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(out);
        Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        canvas.drawBitmap(bitmap, 0, 0, null);
        canvas.drawBitmap(blurredLightsBitmap, null, rect, null);
        blurredLightsBitmap.recycle();
        return out;
    }

    public static Bitmap sketch3_edgeDetect(Bitmap src, int strikeDensity) {
        int width = src.getWidth();
        int height = src.getHeight();
        int strikeLength = (int) Math.sqrt((width * width) + (height * height));
        Path strikes = strikeGenerator(width, height, strikeLength, strikeDensity);
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStrokeWidth(strikeDensity / 2f);
        paint.setColor(Color.GRAY);
        paint.setStyle(Paint.Style.STROKE);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        int[] pixels = Effects.getPixels(src);
        int[] mediumDarkPixels = new int[width * height];
        int[] highDarkPixels = new int[width * height];
        Arrays.fill(mediumDarkPixels, Color.TRANSPARENT);
        Arrays.fill(highDarkPixels, Color.TRANSPARENT);

        float dividerFactor = 1f / 3;
        for (int i = 0; i < pixels.length; i++) {
            float pr = Effects.getMonochromePercentage(pixels[i]);
            if (pr <= dividerFactor) {
                highDarkPixels[i] = Color.BLACK;
            } else if (pr <= dividerFactor * 2) {
                mediumDarkPixels[i] = Color.BLACK;
            }
        }
        Bitmap mediumDarkBitmap = Effects.internalBoxBlur(2, 2, mediumDarkPixels, width, height);
        Bitmap highDarkBitmap = Effects.internalBoxBlur(2, 2, highDarkPixels, width, height);
        mediumDarkPixels = highDarkPixels = null;

        Canvas mediumCanvas = new Canvas(mediumDarkBitmap);
        mediumCanvas.drawColor(Color.WHITE, PorterDuff.Mode.SRC_IN);
        printPath(strikes, paint, 45, 0, width, height, mediumCanvas);


        Canvas highCanvas = new Canvas(highDarkBitmap);
        highCanvas.drawColor(Color.WHITE, PorterDuff.Mode.SRC_IN);
        printPath(strikes, paint, 45, 0, width, height, highCanvas);
        printPath(strikes, paint, -45, 0, width, height, highCanvas);

        Bitmap edgesBitmap;
        int[] edgesPixels_negative = Effects.boxFiltering(Effects.boxFiltering(Effects.getNegativePixels_newArray(pixels), width, height, Effects.BOX_BLUR_KERNEL)
                , width, height, Effects.EDGE_DETECT_KERNEL);
        Effects.getNegativePixels(edgesPixels_negative);
        findEligibleEdge2(edgesPixels_negative);

        edgesBitmap = Effects.internalBoxBlur(2, 1, edgesPixels_negative, width, height);
        edgesPixels_negative = null;


        Bitmap out = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas outCanvas = new Canvas(out);
        outCanvas.drawColor(Color.WHITE);
        outCanvas.drawBitmap(mediumDarkBitmap, 0, 0, null);
        outCanvas.drawBitmap(highDarkBitmap, 0, 0, null);
        outCanvas.drawBitmap(edgesBitmap, 0, 0, null);
        return out;
    }

    public static Bitmap sketch_4(Bitmap src, int strikeDensity) {
        int width = src.getWidth();
        int height = src.getHeight();
        int strikeLength = (int) Math.sqrt((width * width) + (height * height));
        Path strikes = strikeGenerator(width, height, strikeLength, strikeDensity);
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStrokeWidth(strikeDensity / 2f);
        paint.setColor(Color.GRAY);
        paint.setStyle(Paint.Style.STROKE);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        int[] pixels = Effects.getPixels(src);
        int[] mediumDarkPixels = new int[width * height];
        int[] highDarkPixels = new int[width * height];
        Arrays.fill(mediumDarkPixels, Color.TRANSPARENT);
        Arrays.fill(highDarkPixels, Color.TRANSPARENT);

        float dividerFactor = 1f / 3;
        for (int i = 0; i < pixels.length; i++) {
            float pr = Effects.getMonochromePercentage(pixels[i]);
            if (pr <= dividerFactor) {
                highDarkPixels[i] = Color.BLACK;
            } else if (pr <= dividerFactor * 2) {
                mediumDarkPixels[i] = Color.BLACK;
            }
        }
        Bitmap mediumDarkBitmap = Effects.internalBoxBlur(2, 2, mediumDarkPixels, width, height);
        Bitmap highDarkBitmap = Effects.internalBoxBlur(2, 2, highDarkPixels, width, height);
        mediumDarkPixels = highDarkPixels = null;

        Canvas mediumCanvas = new Canvas(mediumDarkBitmap);
        mediumCanvas.drawColor(Color.WHITE, PorterDuff.Mode.SRC_IN);
        printPath(strikes, paint, 45, 0, width, height, mediumCanvas);


        Canvas highCanvas = new Canvas(highDarkBitmap);
        highCanvas.drawColor(Color.WHITE, PorterDuff.Mode.SRC_IN);
        printPath(strikes, paint, 45, 0, width, height, highCanvas);
        paint.setStrokeWidth(paint.getStrokeWidth() * 2);
        printPath(strikes, paint, 45, 0, width, height, highCanvas);

        Bitmap edgesBitmap;
        int[] edgesPixels_negative = Effects.getNegativePixels_newArray(pixels);
        edgesPixels_negative = Effects.boxFiltering(2, edgesPixels_negative, width, height, Effects.SMOOTH_KERNEL);
//        edgesPixels_negative = Effects.boxFiltering(edgesPixels_negative, width, height, Effects.SMOOTH_KERNEL);
        edgesPixels_negative = Effects.boxFiltering(edgesPixels_negative, width, height, Effects.BOX_BLUR_KERNEL);
        edgesPixels_negative = Effects.boxFiltering(edgesPixels_negative, width, height, Effects.EDGE_DETECT_KERNEL);
//        int[] edgesPixels_negative = Effects.boxFiltering(Effects.boxFiltering(Effects.getNegativePixels_newArray(pixels), width, height, Effects.BOX_BLUR_KERNEL), width, height, Effects.EDGE_DETECT_KERNEL);
        findEligibleEdge_sketch4_fix(edgesPixels_negative);
//        Effects.alphaExtender_sameArray(1, edgesPixels_negative, width, height);
        edgesBitmap = Effects.internalBoxBlur(2, 1, edgesPixels_negative, width, height);
        edgesPixels_negative = null;


        Paint bitDrawPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        bitDrawPaint.setDither(true);
        bitDrawPaint.setFilterBitmap(true);

        Bitmap out = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas outCanvas = new Canvas(out);
        outCanvas.drawColor(Color.WHITE);
        outCanvas.drawBitmap(mediumDarkBitmap, 0, 0, bitDrawPaint);
        outCanvas.drawBitmap(highDarkBitmap, 0, 0, bitDrawPaint);
        outCanvas.drawBitmap(edgesBitmap, 0, 0, bitDrawPaint);
        return out;
    }


    public static Bitmap sketch_5(Bitmap src, int strikeDensity, float strikeWidth, int strikeGroupsCount, boolean color, boolean painting, boolean anime) {
        int[] pixels = Effects.getPixels(src);
        int width = src.getWidth();
        int height = src.getHeight();
        if (anime) {
            pixels = Effects3.dynamicMotionBlurPainting(pixels, width, height, 2, 9, 9);
        }


        int strikeLength = (int) Math.sqrt((width * width) + (height * height));
        Path strikes = strikeGenerator(width, height, strikeLength, strikeDensity);
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStrokeWidth(strikeWidth);
        paint.setColor(Color.GRAY);
        paint.setStyle(Paint.Style.STROKE);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));

        int[][] strikeGroupsPixels = new int[strikeGroupsCount - 1][width * height];
        float dividerFactor = Math.nextAfter(strikeGroupsCount, Float.NEGATIVE_INFINITY);
        for (int i = 0; i < pixels.length; i++) {
            float pr = (Effects.getMonochromePercentage(pixels[i])) * dividerFactor;
            if (pr >= strikeGroupsCount - 1) continue;
            strikeGroupsPixels[(int) pr][i] = 0xFF000000;

        }

        Bitmap[] strikeGroupsBitmaps = new Bitmap[strikeGroupsCount - 1];

        for (int i = 0; i < strikeGroupsCount - 1; i++) {
            Bitmap bitmap = Effects.createAndSetPixels(strikeGroupsPixels[i], width, height);
            strikeGroupsPixels[i] = null;
            Canvas canvas = new Canvas(bitmap);
            if (painting) {
                int alpha = (int) (((strikeGroupsCount - i) / (float) strikeGroupsCount) * 255);
                canvas.drawColor(MbnUtils.alphaChanger(Color.GRAY, alpha), PorterDuff.Mode.SRC_IN);
            } else {
                canvas.drawColor(Color.WHITE, PorterDuff.Mode.SRC_IN);
                float strWidth = ((strikeGroupsCount - i) / (float) strikeGroupsCount) * strikeWidth;
                paint.setStrokeWidth(strWidth);
                printPath(strikes, paint, 45, 0, width, height, canvas);
            }
            strikeGroupsBitmaps[i] = bitmap;
        }


        //---------------------- NEW_PART -----------------------------------//

        if (!anime) {
            pixels = Effects3.dynamicMotionBlurPainting(pixels, width, height, 2, 9, 11);

        }

        pixels = Effects.newEdgeDetect3(pixels, width, height, 3);
        makeBalance_sketch_5(pixels, Effects.getPixels(src));
        pixels = Effects.boxFiltering_withAlpha(2, pixels, width, height, Effects.BOX_BLUR_KERNEL);
        Bitmap edgesBitmap = Effects.createAndSetPixels(pixels, width, height);

        //--------------------------------------------------------------------//

        Paint bitDrawPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        bitDrawPaint.setDither(true);
        bitDrawPaint.setFilterBitmap(true);

        Bitmap out = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas outCanvas = new Canvas(out);
        outCanvas.drawColor(Color.WHITE);


        for (Bitmap bit : strikeGroupsBitmaps) {
            outCanvas.drawBitmap(bit, 0, 0, bitDrawPaint);
            bit.recycle();
        }

        out = Effects.boxBlur(1, 2, out);
        outCanvas = new Canvas(out);

        Bitmap colorBit = null;
        if (color) {
            if (anime) {
                colorBit = Effects3.motionBlurPainting(src, 0.2f, 2, 9, 7);
            } else {
                colorBit = Effects3.motionBlurPainting(src, 0.6f, 5, 9, 9);
            }
            if (anime) {
                bitDrawPaint.setAlpha(120);
            } else {
                bitDrawPaint.setAlpha(200);
            }

            outCanvas.drawBitmap(colorBit, null, new Rect(0, 0, width, height), bitDrawPaint);
            colorBit.recycle();

            if (anime) {
                bitDrawPaint.setAlpha(255);
            } else {
                bitDrawPaint.setAlpha(150);
            }
        }

        outCanvas.drawBitmap(edgesBitmap, 0, 0, bitDrawPaint);
        return out;
    }

    public static Bitmap sketch_5_multiThread(Bitmap src, int strikeDensity, float strikeWidth, int strikeGroupsCount, boolean color, boolean painting, boolean anime) {
        final int[] pixels = Effects.getPixels(src);
        int width = src.getWidth();
        int height = src.getHeight();
        if (anime) {
            System.arraycopy(Effects3.dynamicMotionBlurPainting(pixels, width, height, 2, 9, 9), 0, pixels, 0, pixels.length);
        }

        Paint bitDrawPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        bitDrawPaint.setDither(true);
        bitDrawPaint.setFilterBitmap(true);

        Bitmap[] results = new Bitmap[3];

        Runnable strikesThread = () -> {
            int strikeLength = (int) Math.sqrt((width * width) + (height * height));
            Path strikes = strikeGenerator(width, height, strikeLength, strikeDensity);
            Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
            paint.setStrokeWidth(strikeWidth);
            paint.setColor(Color.GRAY);
            paint.setStyle(Paint.Style.STROKE);
            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));

            int[][] strikeGroupsPixels = new int[strikeGroupsCount - 1][width * height];
            float dividerFactor = Math.nextAfter(strikeGroupsCount, Float.NEGATIVE_INFINITY);
            for (int i = 0; i < pixels.length; i++) {
                float pr = (Effects.getMonochromePercentage(pixels[i])) * dividerFactor;
                if (pr >= strikeGroupsCount - 1) continue;
                strikeGroupsPixels[(int) pr][i] = 0xFF000000;

            }

            Bitmap[] strikeGroupsBitmaps = new Bitmap[strikeGroupsCount - 1];

            for (int i = 0; i < strikeGroupsCount - 1; i++) {
                Bitmap bitmap = Effects.createAndSetPixels(strikeGroupsPixels[i], width, height);
                strikeGroupsPixels[i] = null;
                Canvas canvas = new Canvas(bitmap);
                if (painting) {
                    int alpha = (int) (((strikeGroupsCount - i) / (float) strikeGroupsCount) * 255);
                    canvas.drawColor(MbnUtils.alphaChanger(Color.GRAY, alpha), PorterDuff.Mode.SRC_IN);
                } else {
                    canvas.drawColor(Color.WHITE, PorterDuff.Mode.SRC_IN);
                    float strWidth = ((strikeGroupsCount - i) / (float) strikeGroupsCount) * strikeWidth;
                    paint.setStrokeWidth(strWidth);
                    printPath(strikes, paint, 45, 0, width, height, canvas);
                }
                strikeGroupsBitmaps[i] = bitmap;
            }

            Bitmap out = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            Canvas outCanvas = new Canvas(out);
            outCanvas.drawColor(Color.WHITE);


            for (Bitmap bit : strikeGroupsBitmaps) {
                outCanvas.drawBitmap(bit, 0, 0, bitDrawPaint);
                bit.recycle();
            }

            out = Effects.boxBlur(1, 2, out);
            results[0] = out;
        };


        //---------------------- NEW_PART -----------------------------------//

        Runnable edgesThread = () -> {
            int[] usePixels = Arrays.copyOf(pixels, pixels.length);
            if (!anime) {
                usePixels = Effects3.dynamicMotionBlurPainting(usePixels, width, height, 2, 9, 11);

            }

            usePixels = Effects.newEdgeDetect3(usePixels, width, height, 3);
            makeBalance_sketch_5(usePixels, Effects.getPixels(src));
            usePixels = Effects.boxFiltering_forkJoin_withAlpha(2, usePixels, width, height, Effects.BOX_BLUR_KERNEL);
            Bitmap edgesBitmap = Effects.createAndSetPixels(usePixels, width, height);
            results[1] = edgesBitmap;
        };


        //--------------------------------------------------------------------//


        Runnable colorMask = () -> {
            Bitmap colorBit;
            if (anime) {
                colorBit = Effects3.motionBlurPainting(src, 0.2f, 2, 9, 7);
            } else {
                colorBit = Effects3.motionBlurPainting(src, 0.6f, 5, 9, 9);
            }
            results[2] = colorBit;
        };

        Thread t1 = new Thread(strikesThread);
        t1.start();
        Thread t2 = new Thread(edgesThread);
        t2.start();
        if (color) {
            colorMask.run();
            /*
             Thread t3 = new Thread(colorMask);
            t3.start();
            try {
                t3.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
             */
        }
        try {
            t1.join();
            t2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Canvas outCanvas = new Canvas(results[0]);

        Bitmap colorBit = null;
        if (color) {
            colorBit = results[2];
            if (anime) {
                bitDrawPaint.setAlpha(120);
            } else {
                bitDrawPaint.setAlpha(200);
            }
            outCanvas.drawBitmap(colorBit, null, new Rect(0, 0, width, height), bitDrawPaint);
            colorBit.recycle();
            if (anime) {
                bitDrawPaint.setAlpha(255);
            } else {
                bitDrawPaint.setAlpha(150);
            }
        }

        outCanvas.drawBitmap(results[1], 0, 0, bitDrawPaint);
        return results[0];
    }

    private static void makeBalance_sketch_5(int[] pixels, int[] bitPixels) {
        for (int i = 0; i < pixels.length; i++) {
            int color = pixels[i];
            int maxPart = Math.max(Color.green(color), Math.max(Color.blue(color), Color.red(color)));
            float pr = maxPart / 255f;
//            if (pr <= 0.05f) {
            if (pr == 0) {
                pixels[i] = MbnUtils.alphaChanger(bitPixels[i], 0);
                continue;
            }
            pr *= 2f;
            pr = pr > 1f ? 1 : pr;
            pixels[i] = MbnUtils.alphaChanger(bitPixels[i], (int) (pr * 255));
//            pixels[i] = Color.rgb(maxPart, maxPart, maxPart);

//            float pr = maxPart / 255f;
//            if (pr <= 0.02f) {
//                pixels[i] = Color.TRANSPARENT;
//            } else {
//                pixels[i] = MbnUtils.alphaChanger(Color.BLACK, (int) ((pr * 3.4f) * 255));
//            }
        }
    }

    public static Bitmap clearText(Bitmap src) {
        int[] pixels = Effects.getPixels(src);
        int width = src.getWidth();
        int height = src.getHeight();
        retouch_internal_2(1, pixels, null, width, height, 5, 11);
        Effects.getNegativePixels(pixels);
        pixels = Effects.boxFiltering(pixels, width, height, Effects.createEdgeDetectKernel(5));

        int[] blackFill = new int[pixels.length];
        Arrays.fill(blackFill, Color.BLACK);
        makeBalance_sketch_5(pixels, blackFill);

//        Effects.alphaExtender_sameArray(1, pixels, width, height);
//        pixels = Effects.boxFiltering_withAlpha(1, pixels, width, height, Effects.BOX_BLUR_KERNEL);

        Bitmap edges = Effects.createAndSetPixels(pixels, width, height);

        Paint bitDrawPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        bitDrawPaint.setDither(true);
        bitDrawPaint.setFilterBitmap(true);

        Bitmap out = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas outCanvas = new Canvas(out);
        outCanvas.drawBitmap(src, 0, 0, bitDrawPaint);
        outCanvas.drawBitmap(edges, 0, 0, bitDrawPaint);
        return out;
    }

    //----------------------- Painting -----------------------------//

    public static Bitmap retouch(Bitmap src, int passes) {
        int[] pixels = Effects.getPixels(src);
        retouch_internal(passes, pixels, null, src.getWidth(), src.getHeight());
        return Effects.createAndSetPixels(pixels, src.getWidth(), src.getHeight());
    }

    public static Bitmap retouch_2(Bitmap src, int passes) {
        int[] pixels = Effects.getPixels(src);
        retouch_internal_2(passes, pixels, null, src.getWidth(), src.getHeight(), 10, 11);
        return Effects.createAndSetPixels(pixels, src.getWidth(), src.getHeight());
    }

    public static void retouch_internal(int passes, int[] pixels, int[] edgePixels, int width, int height) {
        passes--;
        if (edgePixels == null) {
            edgePixels = Arrays.copyOf(pixels, pixels.length);
            edgePixels = Effects.boxFiltering(edgePixels, width, height, Effects.BOX_BLUR_KERNEL);
            edgePixels = Effects.boxFiltering(edgePixels, width, height, Effects.EDGE_DETECT_KERNEL);

            for (int i = 0; i < edgePixels.length; i++) {
                int color = edgePixels[i];
                int maxPart = Math.max(Color.green(color), Math.max(Color.blue(color), Color.red(color)));
                float prP = maxPart / 255f;
                if (prP <= 0.05f) {
                    edgePixels[i] = 0;
                    continue;
                }
                prP *= 10f;
                prP = prP > 1f ? 1 : prP;
                edgePixels[i] = Effects.getMonoChromeColorFromPercentage(prP);
            }
        }

        int[] temp = new int[width * height];
        float denominator = 0.0f;
        float share = 0, pr;

        float alpha, red, green, blue;
        int ialpha, ired, igreen, iblue, indexOffset, rgb;
        int[] indices = {
                -(width + 1), -width, -(width - 1),
                -1, 0, +1,
                width - 1, width, width + 1
        };

        for (int i = 1; i < height - 1; i++) {
            for (int j = 1; j < width - 1; j++) {
                alpha = red = green = blue = 0.0f;
                indexOffset = (i * width) + j;

                pr = 1 - Effects.getMonochromePercentage(edgePixels[indexOffset]);
                denominator = 1 + (8 * pr);
                for (int k = 0; k < indices.length; k++) {
                    rgb = pixels[indexOffset + indices[k]];
                    if (indices[k] == 0) {
                        share = 1;
                    } else {
                        share = pr;
                    }
                    alpha += ((rgb & 0xff000000) >>> 24) * share;
                    red += ((rgb & 0xff0000) >> 16) * share;
                    green += ((rgb & 0xff00) >> 8) * share;
                    blue += (rgb & 0xff) * share;
                }
                ialpha = (int) (alpha / denominator);
                ired = (int) (red / denominator);
                igreen = (int) (green / denominator);
                iblue = (int) (blue / denominator);
                if (ialpha > 0xff) ialpha = 0xff;
                else if (ialpha < 0) ialpha = 0;
                if (ired > 0xff) ired = 0xff;
                else if (ired < 0) ired = 0;
                if (igreen > 0xff) igreen = 0xff;
                else if (igreen < 0) igreen = 0;
                if (iblue > 0xff) iblue = 0xff;
                else if (iblue < 0) iblue = 0;
                temp[indexOffset] = ((ialpha << 24) & 0xff000000) | ((ired << 16) & 0xff0000) |
                        ((igreen << 8) & 0xff00) | (iblue & 0xff);
            }
        }
        System.arraycopy(temp, 0, pixels, 0, temp.length);
        temp = null;
        if (passes > 0) {
            retouch_internal(passes, pixels, edgePixels, width, height);
        }
    }

    public static void retouch_internal_2(int passes, int[] pixels, int[] edgePixels, int width, int height, int preBlurPasses, int edgeDetectRadios) {
        passes--;
        if (edgePixels == null) {
            edgePixels = Arrays.copyOf(pixels, pixels.length);
            edgePixels = Effects.boxFiltering(preBlurPasses, edgePixels, width, height, Effects.BOX_BLUR_KERNEL);
            Effects.getNegativePixels(edgePixels);
//            edgePixels = Effects.boxFiltering(edgePixels, width, height, Effects.EDGE_DETECT_KERNEL);
            edgePixels = Effects.boxFiltering(edgePixels, width, height, Effects.createEdgeDetectKernel(edgeDetectRadios));

            for (int i = 0; i < edgePixels.length; i++) {
                int color = edgePixels[i];
                int maxPart = Math.max(Color.green(color), Math.max(Color.blue(color), Color.red(color)));
                float prP = maxPart / 255f;
                if (prP < 1f) {
                    edgePixels[i] = 0;
                    continue;
                }
//                prP *= 10f;
                prP = prP > 1f ? 1 : prP;
                edgePixels[i] = Effects.getMonoChromeColorFromPercentage(prP);
            }
        }

        int[] temp = new int[width * height];
        float denominator = 0.0f;
        float share = 0, pr;

        float alpha, red, green, blue;
        int ialpha, ired, igreen, iblue, indexOffset, rgb;
        int[][] indexMap = new int[7][];
        indexMap[0] = Effects.indexCreator(3, width);
        indexMap[1] = Effects.indexCreator(5, width);
        indexMap[2] = Effects.indexCreator(7, width);
        indexMap[3] = Effects.indexCreator(9, width);
        indexMap[4] = Effects.indexCreator(11, width);
        indexMap[5] = Effects.indexCreator(13, width);
        indexMap[6] = Effects.indexCreator(15, width);
        int[] indices;

        for (int i = 1; i < height - 1; i++) {
            for (int j = 1; j < width - 1; j++) {
                alpha = red = green = blue = 0.0f;
                indexOffset = (i * width) + j;

                pr = 1 - Effects.getMonochromePercentage(edgePixels[indexOffset]);
                indices = indexMap[(int) (Math.nextAfter(indexMap.length, Float.NEGATIVE_INFINITY) * pr)];
                denominator = 0;
                for (int k = 0; k < indices.length; k++) {
                    if (indices[k] + indexOffset >= pixels.length || indices[k] + indexOffset < 0) {
                        continue;
                    }
                    rgb = pixels[indexOffset + indices[k]];
                    if (indices[k] == 0) {
                        share = 1;
                    } else {
                        share = pr;
                    }
//                    alpha += ((rgb & 0xff000000) >>> 24) * share;
                    red += ((rgb & 0xff0000) >> 16) * share;
                    green += ((rgb & 0xff00) >> 8) * share;
                    blue += (rgb & 0xff) * share;
                    denominator += share;
                }
//                ialpha = (int) (alpha / denominator);
                ired = (int) (red / denominator);
                igreen = (int) (green / denominator);
                iblue = (int) (blue / denominator);
//                if (ialpha > 0xff) ialpha = 0xff;
//                else if (ialpha < 0) ialpha = 0;
                if (ired > 0xff) ired = 0xff;
                else if (ired < 0) ired = 0;
                if (igreen > 0xff) igreen = 0xff;
                else if (igreen < 0) igreen = 0;
                if (iblue > 0xff) iblue = 0xff;
                else if (iblue < 0) iblue = 0;
                temp[indexOffset] = (/*(ialpha << 24) &*/ 0xff000000) | ((ired << 16) & 0xff0000) |
                        ((igreen << 8) & 0xff00) | (iblue & 0xff);
            }
        }
        System.arraycopy(temp, 0, pixels, 0, temp.length);
        temp = null;
        if (passes > 0) {
            retouch_internal_2(passes, pixels, edgePixels, width, height, preBlurPasses, edgeDetectRadios);
        }
    }

    public static Bitmap rawPainting(int[] pixels, int width, int height) {
        int[] edgePixels = Arrays.copyOf(pixels, pixels.length);
        Effects.getNegativePixels(edgePixels);
        edgePixels = Effects.boxFiltering(edgePixels, width, height, Effects.BOX_BLUR_KERNEL);
        edgePixels = Effects.boxFiltering(edgePixels, width, height, Effects.EDGE_DETECT_KERNEL);
        makeBalance_sketch_5(edgePixels, pixels);
        Effects.alphaExtender_sameArray(10, edgePixels, width, height);
//        edgePixels = Effects.boxFiltering_withAlpha(2, edgePixels, width, height, Effects.BOX_BLUR_KERNEL);
//        Bitmap edgesBitmap = Effects.createAndSetPixels(edgePixels, width, height);

        for (int i = 0; i < edgePixels.length; i++) {
            int p = edgePixels[i];
            float alP = (p >>> 24) / 255f;
            alP = 1 - alP;
            edgePixels[i] = (int) ((2 * alP));
        }

        internalBoxBlur_withBlurMap(10, pixels, edgePixels, width, height);
        Bitmap edgesBitmap = Effects.createAndSetPixels(pixels, width, height);
        return edgesBitmap;
    }

    public static Bitmap newTechniquePanting(Bitmap src) {
        int[] pixels = Effects.getPixels(src);
        newTechniquePanting_internal(pixels, src.getWidth(), src.getHeight(), 5);
        return Effects.createAndSetPixels(pixels, src.getWidth(), src.getHeight());
    }

    public static void newTechniquePanting_internal(int[] pixels, int width, int height, int res) {
        int[][] pixelGroupsPixels = new int[res - 1][pixels.length];

        float dividerFactor = Math.nextAfter(res, Float.NEGATIVE_INFINITY);
        for (int i = 0; i < pixels.length; i++) {
            float pr = (Effects.getMonochromePercentage(pixels[i])) * dividerFactor;
            if (pr >= res - 1) continue;
            pixelGroupsPixels[(int) pr][i] = pixels[i];
        }

        Bitmap[] strikeGroupsBitmaps = new Bitmap[res - 1];

        for (int i = 0; i < res - 1; i++) {
            Bitmap bitmap = Effects.internalBoxBlur(1, 2, pixelGroupsPixels[i], width, height);
//            Bitmap bitmap = Effects.createAndSetPixels(pixelGroupsPixels[i], width, height);
            pixelGroupsPixels[i] = null;
            bitmap = MbnUtils.stackBlur(bitmap, 15, true);
            strikeGroupsBitmaps[i] = bitmap;
        }

        Paint bitDrawPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        bitDrawPaint.setDither(true);
        bitDrawPaint.setFilterBitmap(true);

        Bitmap out = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas outCanvas = new Canvas(out);
        outCanvas.drawColor(Color.WHITE);

        for (Bitmap bit : strikeGroupsBitmaps) {
            outCanvas.drawBitmap(bit, 0, 0, bitDrawPaint);
            bit.recycle();
        }

        out.getPixels(pixels, 0, width, 0, 0, width, height);
        out.recycle();
    }

    //----------------------- BLUR WORKS ---------------------------//

    public static Bitmap alternativeBlur(int passes, Bitmap bitmap, int[] indices) {
        int[] pixels = Effects.getPixels(bitmap);
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        if (indices == null) {
            indices = new int[]{-(width + 1), -width, -(width - 1),
                    -1, 0, +1,
                    width - 1, width, width + 1};
        }
        alternativeBlurring(passes, pixels, width, height, indices);
        return Effects.createAndSetPixels(pixels, width, height);
    }

    public static Bitmap alternativePaint(int passes, int randomTrials, Bitmap bitmap, int[] indices) {
        int[] pixels = Effects.getPixels(bitmap);
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        if (indices == null) {
            indices = Effects.indexCreator(5, width);
        }
        alternativePainting(passes, randomTrials, pixels, width, height, indices);
        return Effects.createAndSetPixels(pixels, width, height);
    }


    public static void alternativeBlurring(int passes, int[] pixels, int width, int height, int[] indices) {
        passes--;
        float red, green, blue;
        int ired, igreen, iblue, indexOffset, rgb;

        for (int i = 1; i < height - 1; i++) {
            for (int j = 1; j < width - 1; j++) {
                red = green = blue = 0.0f;
                indexOffset = (i * width) + j;
                for (int index : indices) {
                    rgb = pixels[indexOffset + index];
                    red += ((rgb & 0xff0000) >> 16);
                    green += ((rgb & 0xff00) >> 8);
                    blue += (rgb & 0xff);
                }
                ired = (int) (red / indices.length);
                igreen = (int) (green / indices.length);
                iblue = (int) (blue / indices.length);
                if (ired > 0xff) ired = 0xff;
                else if (ired < 0) ired = 0;
                if (igreen > 0xff) igreen = 0xff;
                else if (igreen < 0) igreen = 0;
                if (iblue > 0xff) iblue = 0xff;
                else if (iblue < 0) iblue = 0;
                int color = 0xff000000 | ((ired << 16) & 0xff0000) | ((igreen << 8) & 0xff00) | (iblue & 0xff);
                for (int index : indices) {
                    pixels[indexOffset + index] = color;
                }
            }
        }

        if (passes > 0) {
            alternativeBlurring(passes, pixels, width, height, indices);
        }
    }

    public static void alternativePainting(int passes, int randomTrials, int[] pixels, int width, int height, int[] indices) {
        passes--;
        float red, green, blue;
        int ired, igreen, iblue, indexOffset, rgb;
        Random random = new Random();


        int[] edgePixels = Arrays.copyOf(pixels, pixels.length);
        edgePixels = Effects.boxFiltering(5, edgePixels, width, height, Effects.BOX_BLUR_KERNEL);
        Effects.getNegativePixels(edgePixels);
        edgePixels = Effects.boxFiltering(edgePixels, width, height, Effects.createEdgeDetectKernel(9));

        for (int i = 0; i < edgePixels.length; i++) {
            int color = edgePixels[i];
            int maxPart = Math.max(Color.green(color), Math.max(Color.blue(color), Color.red(color)));
            float prP = maxPart / 255f;
            if (prP < 1f) {
                edgePixels[i] = 0;
                continue;
            }
//                prP *= 10f;
            prP = prP > 1f ? 1 : prP;
//            edgePixels[i] = Effects.getMonoChromeColorFromPercentage(prP);
            edgePixels[i] = (int) prP;
        }


        for (int i = 1; i < randomTrials; i++) {
            red = green = blue = 0.0f;
            indexOffset = random.nextInt(pixels.length);

            for (int index : indices) {
                if (index + indexOffset >= pixels.length || index + indexOffset < 0) continue;
                rgb = pixels[indexOffset + index];
                red += ((rgb & 0xff0000) >> 16);
                green += ((rgb & 0xff00) >> 8);
                blue += (rgb & 0xff);
            }
            ired = (int) (red / indices.length);
            igreen = (int) (green / indices.length);
            iblue = (int) (blue / indices.length);
            if (ired > 0xff) ired = 0xff;
            else if (ired < 0) ired = 0;
            if (igreen > 0xff) igreen = 0xff;
            else if (igreen < 0) igreen = 0;
            if (iblue > 0xff) iblue = 0xff;
            else if (iblue < 0) iblue = 0;
            int color = 0xff000000 | ((ired << 16) & 0xff0000) | ((igreen << 8) & 0xff00) | (iblue & 0xff);
            for (int index : indices) {
                if (index + indexOffset >= pixels.length || index + indexOffset < 0 || edgePixels[index + indexOffset] == 1)
                    continue;
                pixels[indexOffset + index] = color;
            }

        }

        if (passes > 0) {
            alternativePainting(passes, randomTrials, pixels, width, height, indices);
        }
    }

    public static Bitmap radialBlur(int passes, int noBlurRadios, float blurFactor, int x, int y, Bitmap bitmap) {
        int[] pixels = Effects.getPixels(bitmap);
        internalRadialBlur(passes, noBlurRadios, blurFactor, x, y, pixels, bitmap.getWidth(), bitmap.getHeight());
        return Effects.createAndSetPixels(pixels, bitmap.getWidth(), bitmap.getHeight());
    }

    public static void internalRadialBlur(int passes, int noBlurRadios, float blurFactor, int pointX, int pointY, int[] pixels, int width, int height) {
        passes--;
        noBlurRadios = Math.max(noBlurRadios, 10);
        int useRadios;
//        useRadios *= 2;

        int[][] alphas = new int[height][width];
        int[][] reds = new int[height][width];
        int[][] blues = new int[height][width];
        int[][] greens = new int[height][width];

        int a, r, b, g;

        for (int i = 0; i < height; i++) {
            a = r = b = g = 0;
            for (int j = 0; j < width; j++) {
                int color = pixels[(i * width) + j];
                a += Color.alpha(color);
                r += Color.red(color);
                b += Color.blue(color);
                g += Color.green(color);

                alphas[i][j] = a;
                reds[i][j] = r;
                blues[i][j] = b;
                greens[i][j] = g;
            }
        }

        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {

                float distance = (float) Math.sqrt(Math.pow(i - pointY, 2) + Math.pow(j - pointX, 2));
                if (distance < noBlurRadios) {
                    continue;
                }
//                useRadios = (int) (distance / 2);
                useRadios = (int) (blurFactor * Math.pow(Math.log10(distance - noBlurRadios), 3));
//                useRadios = (int) (blurFactor * Math.log10(Math.pow(distance - noBlurRadios, 3)));
                if (useRadios <= 0) useRadios = 1;
//                Log.i(TAG, "internalRadialBlur: " + distance + " -- " + useRadios);

                int left = (j - useRadios) - 1;
                if (left < -1) left = -1;
//                while (left < -1) {
//                    left++;
//                }

                int right = j + useRadios;
                if (right >= width) right = width - 1;
//                while (right >= width) {
//                    right--;
//                }

                int top = i - useRadios;
                if (top < 0) top = 0;
//                while (top < 0) {
//                    top++;
//                }

                int bottom = i + useRadios;
                if (bottom >= height) bottom = height - 1;
//                while (bottom >= height) {
//                    bottom--;
//                }

                int pixelCount = (right - left) * ((bottom - top) + 1);
                a = r = b = g = 0;

                for (int k = top; k <= bottom; k++) {
                    if (left == -1) {
                        a += alphas[k][right];
                        r += reds[k][right];
                        b += blues[k][right];
                        g += greens[k][right];
                    } else {
                        a += alphas[k][right] - alphas[k][left];
                        r += reds[k][right] - reds[k][left];
                        b += blues[k][right] - blues[k][left];
                        g += greens[k][right] - greens[k][left];
                    }
                }
                int outColor = Color.argb(a / pixelCount, r / pixelCount, g / pixelCount, b / pixelCount);
                pixels[(i * width) + j] = outColor;
            }
        }

        if (passes > 0) {
            internalRadialBlur(passes, noBlurRadios, blurFactor, pointX, pointY, pixels, width, height);
        }
    }

    public static Bitmap linearBlur(int passes, int noBlurRadios, float blurFactor, int x, int y, @IntRange(from = 1, to = 179) int degree, Bitmap bitmap) {
        int[] pixels = Effects.getPixels(bitmap);
        internalLinearBlur(passes, noBlurRadios, blurFactor, x, y, degree, pixels, bitmap.getWidth(), bitmap.getHeight());
        return Effects.createAndSetPixels(pixels, bitmap.getWidth(), bitmap.getHeight());
    }

    public static void internalLinearBlur(int passes, int noBlurRadios, float blurFactor, int pointX, int pointY, @IntRange(from = 1, to = 179) int degree, int[] pixels, int width, int height) {
        passes--;
        noBlurRadios = Math.max(noBlurRadios, 10);
        int useRadios;

        float aMath = (float) Math.tan(Math.PI * (degree / 180f));
        float reverse_a = 1 / aMath;
        float a_plus_reverseA = aMath + reverse_a;
        float constant_c_ab = (aMath * -pointX) + -pointY;

        int[][] alphas = new int[height][width];
        int[][] reds = new int[height][width];
        int[][] blues = new int[height][width];
        int[][] greens = new int[height][width];

        int a, r, b, g;

        for (int i = 0; i < height; i++) {
            a = r = b = g = 0;
            for (int j = 0; j < width; j++) {
                int color = pixels[(i * width) + j];
                a += Color.alpha(color);
                r += Color.red(color);
                b += Color.blue(color);
                g += Color.green(color);

                alphas[i][j] = a;
                reds[i][j] = r;
                blues[i][j] = b;
                greens[i][j] = g;
            }
        }

        float zMath;
        int dX, dY;

        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                zMath = (reverse_a * j) - i;
                dX = (int) (((zMath - constant_c_ab) / a_plus_reverseA) + 0.5f);
                dY = (int) -(((-reverse_a * dX) + zMath) + 0.5f);

                float distance = (float) Math.sqrt(Math.pow(i - dY, 2) + Math.pow(j - dX, 2));
                if (distance < noBlurRadios) {
                    continue;
                }
                useRadios = (int) (blurFactor * Math.pow(Math.log10(distance - noBlurRadios), 3));
                if (useRadios <= 0) useRadios = 1;

                int left = (j - useRadios) - 1;
                if (left < -1) left = -1;

                int right = j + useRadios;
                if (right >= width) right = width - 1;

                int top = i - useRadios;
                if (top < 0) top = 0;

                int bottom = i + useRadios;
                if (bottom >= height) bottom = height - 1;

                int pixelCount = (right - left) * ((bottom - top) + 1);
                a = r = b = g = 0;

                for (int k = top; k <= bottom; k++) {
                    if (left == -1) {
                        a += alphas[k][right];
                        r += reds[k][right];
                        b += blues[k][right];
                        g += greens[k][right];
                    } else {
                        a += alphas[k][right] - alphas[k][left];
                        r += reds[k][right] - reds[k][left];
                        b += blues[k][right] - blues[k][left];
                        g += greens[k][right] - greens[k][left];
                    }
                }
                int outColor = Color.argb(a / pixelCount, r / pixelCount, g / pixelCount, b / pixelCount);
                pixels[(i * width) + j] = outColor;
            }
        }

        if (passes > 0) {
            internalLinearBlur(passes, noBlurRadios, blurFactor, pointX, pointY, degree, pixels, width, height);
        }
    }

    public static Bitmap painting(int passes, float factor, Bitmap bitmap) {
        int[] pixels = Effects.getPixels(bitmap);
        internalPainting(passes, factor, pixels, bitmap.getWidth(), bitmap.getHeight());
//        Effects.internalBoxBlur_sameArray(1, 1, pixels, bitmap.getWidth(), bitmap.getHeight());
//        pixels = Effects.boxFiltering(pixels, bitmap.getWidth(), bitmap.getHeight(), Effects.BOX_BLUR_KERNEL);
        return Effects.createAndSetPixels(pixels, bitmap.getWidth(), bitmap.getHeight());
    }

    public static void internalPainting(int passes, float blurFactor, int[] pixels, int width, int height) {
        passes--;
        int useRadios = 0;
//        useRadios *= 2;

        int[][] alphas = new int[height][width];
        int[][] reds = new int[height][width];
        int[][] blues = new int[height][width];
        int[][] greens = new int[height][width];

        int a, r, b, g;

        for (int i = 0; i < height; i++) {
            a = r = b = g = 0;
            for (int j = 0; j < width; j++) {
                int color = pixels[(i * width) + j];
                a += Color.alpha(color);
                r += Color.red(color);
                b += Color.blue(color);
                g += Color.green(color);

                alphas[i][j] = a;
                reds[i][j] = r;
                blues[i][j] = b;
                greens[i][j] = g;
            }
        }

        int blurFix = 0;
        Random random = new Random();
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {

                useRadios = random.nextInt(30) - 26;

//                if (useRadios > 0) useRadios = 1;

//                if (blurFix == 0) {
//                    useRadios = (int) (blurFactor * (Math.random() * 10));
//                }
//                blurFix++;
//                if (blurFix > 5) {
//                    blurFix = 0;
//                }


                if (useRadios <= 0) continue;

                int left = (j - useRadios) - 1;
                if (left < -1) left = -1;
//                while (left < -1) {
//                    left++;
//                }

                int right = j + useRadios;
                if (right >= width) right = width - 1;
//                while (right >= width) {
//                    right--;
//                }

                int top = i - useRadios;
                if (top < 0) top = 0;
//                while (top < 0) {
//                    top++;
//                }

                int bottom = i + useRadios;
                if (bottom >= height) bottom = height - 1;
//                while (bottom >= height) {
//                    bottom--;
//                }

                int pixelCount = (right - left) * ((bottom - top) + 1);
                a = r = b = g = 0;

                for (int k = top; k <= bottom; k++) {
                    if (left == -1) {
                        a += alphas[k][right];
                        r += reds[k][right];
                        b += blues[k][right];
                        g += greens[k][right];
                    } else {
                        a += alphas[k][right] - alphas[k][left];
                        r += reds[k][right] - reds[k][left];
                        b += blues[k][right] - blues[k][left];
                        g += greens[k][right] - greens[k][left];
                    }
                }
                int outColor = Color.argb(a / pixelCount, r / pixelCount, g / pixelCount, b / pixelCount);
                pixels[(i * width) + j] = outColor;
            }
        }
        alphas = reds = blues = greens = null;
        Runtime.getRuntime().gc();
        if (passes > 0) {
            internalPainting(passes, blurFactor, pixels, width, height);
        }
    }

    public static void internalBoxBlur_withBlurMap(int passes, int[] pixels, int[] map, int width, int height) {
        passes--;
        int useRadios;
//        useRadios *= 2;

        int[][] alphas = new int[height][width];
        int[][] reds = new int[height][width];
        int[][] blues = new int[height][width];
        int[][] greens = new int[height][width];

        int a, r, b, g;

        for (int i = 0; i < height; i++) {
            a = r = b = g = 0;
            for (int j = 0; j < width; j++) {
                int color = pixels[(i * width) + j];
                a += Color.alpha(color);
                r += Color.red(color);
                b += Color.blue(color);
                g += Color.green(color);

                alphas[i][j] = a;
                reds[i][j] = r;
                blues[i][j] = b;
                greens[i][j] = g;
            }
        }

        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {


                useRadios = map[(i * width) + j];
                if (useRadios <= 0) continue;
//                if (useRadios <= 0) useRadios = 1;

                int left = (j - useRadios) - 1;
                if (left < -1) left = -1;
//                while (left < -1) {
//                    left++;
//                }

                int right = j + useRadios;
                if (right >= width) right = width - 1;
//                while (right >= width) {
//                    right--;
//                }

                int top = i - useRadios;
                if (top < 0) top = 0;
//                while (top < 0) {
//                    top++;
//                }

                int bottom = i + useRadios;
                if (bottom >= height) bottom = height - 1;
//                while (bottom >= height) {
//                    bottom--;
//                }

                int pixelCount = (right - left) * ((bottom - top) + 1);
                a = r = b = g = 0;

                for (int k = top; k <= bottom; k++) {
                    if (left == -1) {
                        a += alphas[k][right];
                        r += reds[k][right];
                        b += blues[k][right];
                        g += greens[k][right];
                    } else {
                        a += alphas[k][right] - alphas[k][left];
                        r += reds[k][right] - reds[k][left];
                        b += blues[k][right] - blues[k][left];
                        g += greens[k][right] - greens[k][left];
                    }
                }
                int outColor = Color.argb(a / pixelCount, r / pixelCount, g / pixelCount, b / pixelCount);
                pixels[(i * width) + j] = outColor;
            }
        }

        alphas = reds = blues = greens = null;
        Runtime.getRuntime().gc();
        if (passes > 0) {
            internalBoxBlur_withBlurMap(passes, pixels, map, width, height);
        }
    }


    //--------------------------------- The Random Window ---------------------------//

    public static Bitmap blankRandomWindow(Bitmap bitmap) {
        BlankWindow blankWindow = new BlankWindow(bitmap.getWidth(), bitmap.getHeight());
        WindowMaker windowMaker = new WindowMaker(blankWindow);
        return windowMaker.getWindow();
    }

    public static Bitmap painting2(int passes, float factor, Bitmap bitmap) {
        int[] pixels = Effects.getPixels(bitmap);
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int[] blurMap = new int[width * height];
        Random random = new Random();

        MapMaker mapMaker = new MapMaker(width, height);
        for (int i = 0; i < passes; i++) {

            MapItem[] mapItems = mapMaker.buildMap();
            for (MapItem item : mapItems) {

                int blurRadios = random.nextInt(30) - 26;
                if (blurRadios > 0) blurRadios = 1;

//                int blurRadios = (int) (factor * (Math.random() * 10));

                for (int j = item.top; j <= item.bottom; j++) {
                    for (int k = item.left; k <= item.right; k++) {
                        blurMap[(width * j) + k] = blurRadios;
                    }
                }
            }

            internalBoxBlur_withBlurMap(1, pixels, blurMap, width, height);
        }

//        pixels = Effects.boxFiltering(pixels, width, height, Effects.BOX_BLUR_KERNEL);
//        Effects.internalBoxBlur_sameArray(1, 1, pixels, width, height);
        return Effects.createAndSetPixels(pixels, width, height);
    }

    public static Bitmap painting3(int passes, float factor, Bitmap bitmap, boolean test) {
        int[] mapPixels = Effects.getPixels(bitmap);
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        Effects.getNegativePixels(mapPixels);
        mapPixels = Effects.boxFiltering(mapPixels, width, height, Effects.SMOOTH_KERNEL);
        mapPixels = Effects.boxFiltering(mapPixels, width, height, Effects.BOX_BLUR_KERNEL);
        mapPixels = Effects.boxFiltering(mapPixels, width, height, Effects.EDGE_DETECT_KERNEL);
        Effects.getNegativePixels(mapPixels);
//        findEligibleEdge_sketch4_fix(mapPixels);
        findEligibleEdge2(mapPixels);
        Effects.alphaExtender_sameArray(1, mapPixels, width, height);
        Effects.internalBoxBlur_sameArray(2, 1, mapPixels, width, height);
        if (test) {
            return Effects.createAndSetPixels(mapPixels, width, height);
        }

        for (int i = 0; i < mapPixels.length; i++) {
//            mapPixels[i] = (int) ((Effects.getMonochromePercentage(mapPixels[i])) * factor);
            mapPixels[i] = (int) ((1 - (Color.alpha(mapPixels[i]) / 255f)) * 10);
        }

        int[] outPixels = Effects.getPixels(bitmap);
        internalBoxBlur_withBlurMap(passes, outPixels, mapPixels, width, height);
        return Effects.createAndSetPixels(outPixels, width, height);
    }

    private static class MapItem {
        private int left, top, right, bottom;


        MapItem(int left, int top, int right, int bottom) {
            this.left = left;
            this.top = top;
            this.right = right;
            this.bottom = bottom;
        }
    }

    private interface InfoProvider {
        int[] getSize();

        void draw(MapItem[] mapItems, Canvas canvas);
    }

    private static class MapMaker {
        private static final byte EMPTY = 0;
        private static final byte FILLED = 1;
        private int FIRST_LVL_DIV = 5;
        private int SECOND_LVL_DIV = 15;

        private int MIN_SIZE_X;
        private int MAX_SIZE_X;

        private int MIN_SIZE_Y;
        private int MAX_SIZE_Y;

        private int width, height;
        private byte[][] filledMap;
        private ArrayList<MapItem> mapItems = new ArrayList<>();
        private Random random;

        private MapMaker(int width, int height) {
            this.width = width;
            this.height = height;
            MAX_SIZE_Y = height / FIRST_LVL_DIV;
            MIN_SIZE_Y = MAX_SIZE_Y / SECOND_LVL_DIV;

            MAX_SIZE_X = width / FIRST_LVL_DIV;
            MIN_SIZE_X = MAX_SIZE_X / SECOND_LVL_DIV;
        }

        private MapMaker(int width, int height, int firstLvlDiv, int secondLvlDiv) {
            this.width = width;
            this.height = height;
            this.FIRST_LVL_DIV = firstLvlDiv;
            this.SECOND_LVL_DIV = secondLvlDiv;

            MAX_SIZE_Y = height / FIRST_LVL_DIV;
            MIN_SIZE_Y = MAX_SIZE_Y / SECOND_LVL_DIV;

            MAX_SIZE_X = width / FIRST_LVL_DIV;
            MIN_SIZE_X = MAX_SIZE_X / SECOND_LVL_DIV;

        }

        private MapMaker(int width, int height, int firstLvlDiv, int secondLvlDiv, boolean square) {
            this.width = width;
            this.height = height;
            this.FIRST_LVL_DIV = firstLvlDiv;
            this.SECOND_LVL_DIV = secondLvlDiv;

            if (square) {
                int smaller = Math.min(width, height);
                int max = smaller / firstLvlDiv;
                int min = max / secondLvlDiv;

                MAX_SIZE_X = MAX_SIZE_Y = max;
                MIN_SIZE_X = MIN_SIZE_Y = min;
            } else {

                MAX_SIZE_Y = height / FIRST_LVL_DIV;
                MIN_SIZE_Y = MAX_SIZE_Y / SECOND_LVL_DIV;

                MAX_SIZE_X = width / FIRST_LVL_DIV;
                MIN_SIZE_X = MAX_SIZE_X / SECOND_LVL_DIV;
            }
        }

        private int findNext(int row, int index, int searchItem) {
            for (int i = index; i < width; i++) {
                if (filledMap[row][i] == searchItem) return i;
            }
            return -1;
        }

        private void generateRectangle(int row, int index) {
            int possibleRight = findNext(row, index, FILLED);
            if (possibleRight == -1) possibleRight = width;

            int sizeX = possibleRight - index;
            if (sizeX > MAX_SIZE_X) sizeX = MAX_SIZE_X;

            int sizeY = height - row;
            if (sizeY > MAX_SIZE_Y) sizeY = MAX_SIZE_Y;
//            if (sizeY > MAX_SIZE / 3) sizeY = MAX_SIZE / 3;

//            random.setSeed(System.nanoTime());

            int sx = sizeX;
            sizeX = random.nextInt(sizeX);
            if (sizeX < MIN_SIZE_X) {
                if (sx > MIN_SIZE_X) {
                    sizeX = MIN_SIZE_X;
                } else sizeX = sx - 1;
            }

            int sy = sizeY;
            sizeY = random.nextInt(sizeY);
            if (sizeY < MIN_SIZE_Y) {
                if (sy > MIN_SIZE_Y) {
                    sizeY = MIN_SIZE_Y;
                } else sizeY = sy - 1;
            }

//            int sx = sizeX;
//            sizeX = random.nextInt(sizeX);
//            if ((sx - sizeX <= MIN_SIZE / 5 && sx - sizeX >= MIN_SIZE / -5) || sizeX < MIN_SIZE)
//                sizeX = sx - 1;
//
//            int sy = sizeY;
//            sizeY = random.nextInt(sizeY);
//            if ((sy - sizeY <= MIN_SIZE / 5 && sy - sizeY >= MIN_SIZE / -5) || sizeY < MIN_SIZE)
//                sizeY = sy - 1;


//            if (sizeX > MIN_SIZE) {
//                sizeX = random.nextInt(sizeX);
//            } else sizeX--;
//            if (sizeY > MIN_SIZE) {
//                sizeY = random.nextInt(sizeY);
//            } else sizeY--;


            int rightIndex = index + sizeX;
            int bottomIndex = row + sizeY;

            MapItem mapItem = new MapItem(index, row, rightIndex, bottomIndex);
            mapItems.add(mapItem);

            for (int i = row; i <= bottomIndex; i++) {
                for (int j = index; j <= rightIndex; j++) {
                    filledMap[i][j] = FILLED;
                }
            }

        }

        MapItem[] buildMap() {
            filledMap = new byte[height][width];
            random = new Random();
            int index;
            for (int i = 0; i < height; i++) {
                index = 0;
                while ((index = findNext(i, index, EMPTY)) != -1) {
                    generateRectangle(i, index);
                }
            }
            MapItem[] items = new MapItem[mapItems.size()];
            return mapItems.toArray(items);
        }
    }

    private static class WindowMaker {
        private InfoProvider infoProvider;
        private int width, height;

        WindowMaker(InfoProvider infoProvider) {
            this.infoProvider = infoProvider;
        }

        Bitmap getWindow() {
            int[] sizes = infoProvider.getSize();
            width = sizes[0];
            height = sizes[1];
            MapMaker mapMaker = new MapMaker(width, height);
            Bitmap out = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(out);
            infoProvider.draw(mapMaker.buildMap(), canvas);
            return out;
        }

    }

    private static class BlankWindow implements InfoProvider {
        private int width, height;

        BlankWindow(int width, int height) {
            this.width = width;
            this.height = height;
        }

        @Override
        public int[] getSize() {
            return new int[]{width, height};
        }

        @Override
        public void draw(MapItem[] mapItems, Canvas canvas) {
            Random random = new Random();
            canvas.drawColor(Color.WHITE);
            Paint paint = new Paint();
//            Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
            paint.setColor(Color.BLACK);
            paint.setStyle(Paint.Style.STROKE);
//            paint.setStyle(Paint.Style.FILL_AND_STROKE);
            paint.setStrokeWidth(1);

            for (MapItem item : mapItems) {
//                paint.setARGB(255, random.nextInt(256), random.nextInt(256), random.nextInt(256));
                canvas.drawRect(item.left, item.top, item.right, item.bottom, paint);
//                paint.setTextSize(item.right - item.left);
//                canvas.drawText("MBN", item.left, item.bottom, paint);
            }
        }
    }

    //------------------------------- Shadow Works -------------------------------//

    @Nullable
    @RequiresApi(api = Build.VERSION_CODES.M)
    public static Bitmap makeTextBitmap(@Nullable Canvas drawCanvas, int width, int height, String text, int maxLines, int backgroundColor, int textColor, float textSize) {
        TextPaint textPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setTextSize(textSize);
        textPaint.setColor(textColor);
        StaticLayout.Builder stlBuilder = StaticLayout.Builder.obtain(text, 0, text.length(), textPaint, width);
        stlBuilder.setAlignment(Layout.Alignment.ALIGN_CENTER)
                .setMaxLines(maxLines)
                .setEllipsize(TextUtils.TruncateAt.END);
        StaticLayout staticLayout = stlBuilder.build();
        Bitmap out = null;
        if (drawCanvas == null) {
            out = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            drawCanvas = new Canvas(out);
            drawCanvas.drawColor(backgroundColor);
        }
        drawCanvas.translate(0, height / 2f - (staticLayout.getHeight() / 2f));
        staticLayout.draw(drawCanvas);
        return out;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public static Bitmap makeTextShadow(int width, int height, String text, int maxLines, int backgroundColor, int textColor, int shadowColor, float textSize, int shadowLength) {
        TextPaint textPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setTextSize(textSize);
        textPaint.setColor(textColor);
        StaticLayout.Builder stlBuilder = StaticLayout.Builder.obtain(text, 0, text.length(), textPaint, width);
        stlBuilder.setAlignment(Layout.Alignment.ALIGN_CENTER)
                .setMaxLines(maxLines)
                .setEllipsize(TextUtils.TruncateAt.END);
        StaticLayout staticLayout = stlBuilder.build();

        Bitmap textBit = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(textBit);
        canvas.translate(0, height / 2f - (staticLayout.getHeight() / 2f));
        staticLayout.draw(canvas);

        ShadowMakerDrawable shadowMakerDrawable = new ShadowMakerDrawable(45, shadowLength, shadowColor);
        shadowMakerDrawable.setScale(1);
        shadowMakerDrawable.setBitmap(textBit);

        Bitmap out = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas outCanvas = new Canvas(out);
        outCanvas.drawColor(backgroundColor);

        shadowMakerDrawable.draw(outCanvas);
        outCanvas.drawBitmap(textBit, 0, 0, null);
        return out;
    }


    //------------------------------------ NEW-SHADOW ----------------------------------//

    @RequiresApi(api = Build.VERSION_CODES.M)
    public static Bitmap makeTextShadow_2(@Nullable Canvas drawCanvas, int width, int height, String text, int maxLines, int backgroundColor, int textColor, float textSize,
                                          @IntRange(from = 0, to = 359) int shadowAngle, float shadowLength, int shadowColor) {
        return makeShadow_2(drawCanvas, makeTextBitmap(null, width, height, text, maxLines, Color.TRANSPARENT, textColor, textSize), shadowAngle, shadowLength, shadowColor, backgroundColor);
    }

    @Nullable
    public static Bitmap makeShadow_2(@Nullable Canvas outCanvas, Bitmap inBit, @IntRange(from = 0, to = 359) int shadowAngle, float shadowLength, int shadowColor, int backgroundColor) {
//        if (shadowAngle == 90 || shadowAngle == 270) return null;

        int width = inBit.getWidth();
        int height = inBit.getHeight();
        ArrayList<Point> exposedPixels = null;

//            exposedPixels = getExposedPixels_2_parallelRays(Effects.getPixels(inBit), width, height, shadowAngle, 0.5f);
        exposedPixels = getExposedPixels_3_rotation(Effects.getPixels(inBit), width, height, shadowAngle, 1f);


        double radiant = Math.toRadians(shadowAngle);
        double tan = Math.tan(radiant);

        /*
        Nothing ! :))
         */

//        Collections.sort(exposedPixels, (o1, o2) -> {
//            float z1 = (float) (o1.y - (o1.x * tan));
//            float z2 = (float) (o2.y - (o2.x * tan));
//            return Float.compare(z1, z2);
//        });

        float shadowWidth = (float) (Math.cos(radiant) * shadowLength);
        float shadowHeight = (float) (Math.sin(radiant) * shadowLength);

        float centerX = (exposedPixels.get(0).x + exposedPixels.get(exposedPixels.size() - 1).x) / 2f;
        float centerY = (exposedPixels.get(0).y + exposedPixels.get(exposedPixels.size() - 1).y) / 2f;

        Point firstPoint = new Point();
        firstPoint.x = (int) (exposedPixels.get(0).x + shadowWidth * 10);
        firstPoint.y = (int) (shadowHeight * 10 + exposedPixels.get(0).y);

        Point lastPoint = new Point();
        lastPoint.x = (int) (exposedPixels.get(exposedPixels.size() - 1).x + shadowWidth * 10);
        lastPoint.y = (int) (shadowHeight * 10 + exposedPixels.get(exposedPixels.size() - 1).y);

        exposedPixels.add(lastPoint);
        exposedPixels.add(0, firstPoint);

        Path shadowPath = new Path();
//        shadowPath.moveTo(firstPoint.x, firstPoint.y);
        shadowPath.moveTo(exposedPixels.get(0).x, exposedPixels.get(0).y);
        for (int i = 1; i < exposedPixels.size(); i++) {
            shadowPath.lineTo(exposedPixels.get(i).x, exposedPixels.get(i).y);
        }
//        shadowPath.lineTo(lastPoint.x, lastPoint.y);


        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.BLACK);
        paint.setStrokeWidth(5);
        paint.setStyle(Paint.Style.FILL);
        RectF bounds = new RectF();
        shadowPath.computeBounds(bounds, true);

//        float shadowWidth = (float) (Math.cos(radiant) * shadowLength);
//        float shadowHeight = (float) (Math.sin(radiant) * shadowLength);

//        paint.setShader(new LinearGradient(bounds.left, bounds.top, bounds.left + shadowWidth, bounds.top + shadowHeight,
//                shadowColor, MbnUtils.alphaChanger(shadowColor, 0), Shader.TileMode.CLAMP));


        paint.setShader(new LinearGradient(centerX, centerY,
                centerX, shadowHeight + centerY,
                shadowColor, MbnUtils.alphaChanger(shadowColor, 0), Shader.TileMode.CLAMP));


        Bitmap out = null;
        if (outCanvas == null) {
            out = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            outCanvas = new Canvas(out);
            outCanvas.drawColor(backgroundColor);
        }

        shadowPath.close();
        outCanvas.drawPath(shadowPath, paint);
        outCanvas.drawBitmap(inBit, 0, 0, null);
        return out;
    }

    @Deprecated
    public static ArrayList<Point> getExposedPixels(int[] pixels, int width, int height, int angle) {
        double angleTan = Math.tan(Math.toRadians(angle));
        boolean[] exposed = new boolean[width * height];

        for (int i = 0; i < width; i++) {
            for (float mathX = i; mathX < width; mathX += 0.2f) {
                int y = (int) (((mathX - i) * angleTan) + 0.5f);
                int x = (int) (mathX + 0.5f);
                if (x >= width) {
                    x = width - 1;
                }
                if (y >= height || y < 0) {
                    exposed[((height - 1) * width) + (x)] = true;
                    break;
                }
                int pixel = pixels[(y * width) + x];
                if ((pixel >>> 24) > 200) {
                    exposed[(y * width) + x] = true;
                    break;
                }
                if (x >= width - 1) {
                    exposed[((y) * width) + (x)] = true;
                }
            }
        }

        for (int z = 0; z < height; z++) {
            for (float mathX = 0; mathX < width; mathX += 0.2f) {
                int y = (int) (((mathX * angleTan) + z) + 0.5f);
                int x = (int) (mathX + 0.5f);
                if (x >= width) {
                    x = width - 1;
                }
                if (y >= height || y < 0) {
                    exposed[((height - 1) * width) + (x)] = true;
                    break;
                }
                int pixel = pixels[(y * width) + x];
                if ((pixel >>> 24) > 200) {
                    exposed[(y * width) + x] = true;
                    break;
                }
                if (x >= width - 1) {
                    exposed[((y) * width) + (x)] = true;
                }
            }
        }

        ArrayList<Point> points = new ArrayList<>();

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (exposed[(y * width) + x]) {
                    points.add(new Point(x, y));
                }
            }
        }

        Collections.sort(points, (o1, o2) -> {
            float z1 = (float) (o1.y - (o1.x * angleTan));
            float z2 = (float) (o2.y - (o2.x * angleTan));
            return Float.compare(z1, z2);
        });

//        for (int i = 0; i < points.size(); i++) {
//            Point point = points.get(i);
//            if (point.x >= (width - 1) || point.y >= (height - 1)) {
//                points.remove(i);
//            }
//        }

        int count = 0;
        while (count++ < points.size()) {
            Point point = points.get(0);
            if (point.x >= (width - 1) || point.y >= (height - 1)) {
                points.remove(0);
            } else {
                break;
            }
        }
        count = points.size();
        while (count-- > 0) {
            Point point = points.get(points.size() - 1);
            if (point.x >= (width - 1) || point.y >= (height - 1)) {
                points.remove(points.size() - 1);
            } else {
                break;
            }
        }

        return points;
    }

    private static ArrayList<Point> getExposedPixels_2_parallelRays(int[] pixels, int width, int height, int angle, float resolution) {
        double radianA = Math.toRadians(angle);
        double tanA = Math.tan(radianA);
        float[] zSpectrum = new float[4];

        //point 0,0:
        zSpectrum[0] = 0;
        //point width,0
        zSpectrum[1] = (float) (0 - ((width - 1) * tanA));
        //point width,height
        zSpectrum[2] = (float) ((height - 1) - ((width - 1) * tanA));
        //point 0,height
        zSpectrum[3] = (height - 1);

        // sorting Z spectrum
        Arrays.sort(zSpectrum);
//        Log.i(TAG, "getExposedPixels_2_parallelRays: " + Arrays.toString(zSpectrum));

        double reverseF = radianA / (Math.PI / 2);
        boolean reverse = reverseF >= 1 && reverseF <= 3;
        float originX = reverse ? width - 1 : 0;
        Rect bounds = findBounds(pixels, width, height);

        ArrayList<Point> impactPoints = new ArrayList<>();
        for (float i = zSpectrum[0]; i <= zSpectrum[3]; i += resolution) {
            Point point = calculateRayImpactPoint(resolution, originX, i, tanA, width, height, pixels, bounds, reverse);
            if (point != null) impactPoints.add(point);
        }

        int count = 0;
        while (count++ < impactPoints.size()) {
            Point point = impactPoints.get(0);
            if (point.x >= (width - 1) || point.y >= (height - 1) || point.x <= 0 || point.y <= 0) {
                impactPoints.remove(0);
            } else {
                break;
            }
        }
        count = impactPoints.size();
        while (count-- > 0) {
            Point point = impactPoints.get(impactPoints.size() - 1);
            if (point.x >= (width - 1) || point.y >= (height - 1) || point.x <= 0 || point.y <= 0) {
                impactPoints.remove(impactPoints.size() - 1);
            } else {
                break;
            }
        }

        return impactPoints;
    }

    private static Point calculateRayImpactPoint(float resolution, float originX, float z, double tanA, int width, int height, int[] pixels, Rect bounds, boolean reverse) {
        if (Double.isNaN(tanA)) {
            return null;
        }

        float[] xSpectrum = new float[2];
        xSpectrum[0] = (float) (-z / tanA);
        xSpectrum[1] = (float) ((height - z - 1) / tanA);
        Arrays.sort(xSpectrum);
        if (xSpectrum[0] < 0) xSpectrum[0] = 0;
        if (xSpectrum[1] >= width) xSpectrum[1] = width - 1;

        if (xSpectrum[0] > bounds.right || xSpectrum[1] < bounds.left) {
            return null;
        }

        float[] inBoundX_spectrum = new float[]{xSpectrum[0], xSpectrum[1]};
        if (inBoundX_spectrum[0] < bounds.left) inBoundX_spectrum[0] = bounds.left;
        if (inBoundX_spectrum[1] > bounds.right) inBoundX_spectrum[1] = bounds.right;


        if (originX >= inBoundX_spectrum[1]) originX = inBoundX_spectrum[1];
        if (originX < inBoundX_spectrum[0]) originX = inBoundX_spectrum[0];

        boolean isInBound = false;

        if (reverse) {
            for (float i = originX; i >= inBoundX_spectrum[0]; i -= resolution) {
                int y = (int) (((i * tanA) + z) + 0.5f);
                int x = (int) (i + 0.5f);
                if (x >= width) {
                    x = width - 1;
                }

                if (y >= bounds.top && y <= bounds.bottom) isInBound = true;

                /*
                if (y >= height || y < 0) {
                    if (tanA < 0) {
                        if (y < 0) continue;
                        else {
                            y = height - 1;
                            return new Point(x, y);
                        }
                    } else {
                        if (y >= height) continue;
                        y = 0;
                        return new Point(x, y);
                    }
                }
                */

                if (pixels[(y * width) + x] >>> 24 > 200) {
                    return new Point(x, y);
                }
            }
            if (isInBound) {
                return new Point((int) xSpectrum[0], (int) ((Math.floor(xSpectrum[0]) * tanA) + z));
            }
        } else {
            for (float i = originX; i <= inBoundX_spectrum[1]; i += resolution) {
                int y = (int) (((i * tanA) + z) + 0.5f);
                int x = (int) (i + 0.5f);
                if (x >= width) {
                    x = width - 1;
                }

                if (y >= bounds.top && y <= bounds.bottom) isInBound = true;

                /*
                if (y >= height || y < 0) {
                    if (tanA > 0) {
                        if (y < 0) continue;
                        else {
                            y = height - 1;
                            return new Point(x, y);
                        }
                    } else {
                        if (y >= height) continue;
                        y = 0;
                        return new Point(x, y);
                    }
                }
                */

                if (pixels[(y * width) + x] >>> 24 > 200) {
                    return new Point(x, y);
                }
            }
//            return null;
            if (isInBound) {
                return new Point((int) xSpectrum[1], (int) ((Math.ceil(xSpectrum[1]) * tanA) + z));
            }
        }
        return null;
    }

    private static Rect findBounds(int[] pixels, int width, int height) {
        int top = Integer.MAX_VALUE;
        int left = Integer.MAX_VALUE;
        int bottom = Integer.MIN_VALUE;
        int right = Integer.MIN_VALUE;

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (pixels[(y * width) + x] >>> 24 > 200) {
                    if (top > y) top = y;
                    if (left > x) left = x;
                    if (bottom < y) bottom = y;
                    if (right < x) right = x;
                }
            }
        }
        return new Rect(left, top, right, bottom);
    }

    private static ArrayList<Point> getExposedPixels_3_rotation(int[] pixels, int width, int height, int angle, float resolution) {
        double radianA = Math.toRadians(angle);
        Rect bounds = findBounds(pixels, width, height);
        Point[] rotatedBounds = new Point[4];
        rotatedBounds[0] = rotatePoint(bounds.left, -bounds.top, radianA, null, 1);
        rotatedBounds[1] = rotatePoint(bounds.left, -bounds.bottom, radianA, null, 1);
        rotatedBounds[2] = rotatePoint(bounds.right, -bounds.top, radianA, null, 1);
        rotatedBounds[3] = rotatePoint(bounds.right, -bounds.bottom, radianA, null, 1);

        Arrays.sort(rotatedBounds, (o1, o2) -> Integer.compare(o1.y, o2.y));
        int[] ySpectrum = new int[]{rotatedBounds[0].y, rotatedBounds[3].y};
        Arrays.sort(rotatedBounds, (o1, o2) -> Integer.compare(o1.x, o2.x));
        int[] xSpectrum = new int[2];
        xSpectrum[0] = rotatedBounds[0].x;

        // TODO: 3/2/2020 null rotatedBounds...

        int[] xpBoxRotated = new int[4];
        xpBoxRotated[0] = rotatePoint(0, 0, radianA, null, 1).x;
        xpBoxRotated[1] = rotatePoint(0, -(height - 1), radianA, null, 1).x;
        xpBoxRotated[2] = rotatePoint(width - 1, 0, radianA, null, 1).x;
        xpBoxRotated[3] = rotatePoint(width - 1, -(height - 1), radianA, null, 1).x;
        Arrays.sort(xpBoxRotated);
        xSpectrum[1] = xpBoxRotated[3];

//        Log.i(TAG, "getExposedPixels_3_rotation: " + Arrays.toString(ySpectrum) + "  --  " + Arrays.toString(xSpectrum));

        ArrayList<Point> impactPoints = new ArrayList<>();
        for (float y = ySpectrum[0]; y <= ySpectrum[1]; y += resolution) {
            Point point = calculateImpactPoint_2(pixels, width, height, y, xSpectrum, radianA, null, resolution);
            impactPoints.add(point);
        }

        int count = 0;
        while (count++ < impactPoints.size()) {
            Point point = impactPoints.get(0);
            if (point.x >= (width - 1) || point.y >= (height - 1) || point.x <= 0 || point.y <= 0) {
                impactPoints.remove(0);
            } else {
                break;
            }
        }
        count = impactPoints.size();
        while (count-- > 0) {
            Point point = impactPoints.get(impactPoints.size() - 1);
            if (point.x >= (width - 1) || point.y >= (height - 1) || point.x <= 0 || point.y <= 0) {
                impactPoints.remove(impactPoints.size() - 1);
            } else {
                break;
            }
        }

        return impactPoints;
    }

    private static Point calculateImpactPoint_2(int[] pixels, int width, int height, float y, int[] xSpectrum, double radiantA, @Nullable PointF pivot, float resolution) {
        for (float x = xSpectrum[0]; x <= xSpectrum[1]; x += resolution) {
            Point rawPoint = rotatePoint(x, y, radiantA, pivot, -1);
            int rX = rawPoint.x;
            int ry = -rawPoint.y;
            if (rX < 0 || ry < 0 || rX > width - 1 || ry > height - 1) {
                continue;
            }
            if (pixels[(ry * width) + rX] >>> 24 > 200) {
                return new Point(rX, ry);
            }
        }
        Point boarderOut = rotatePoint(xSpectrum[1], y, radiantA, pivot, -1);
        boarderOut.y *= -1;
        return boarderOut;
    }

    private static Point rotatePoint(float x, float y, double radianA, @Nullable PointF pivot, int direction) {
//        float x1 = x;
//        float y1 = y;
        if (pivot != null) {
            x -= pivot.x;
            y -= pivot.y;
        }
        radianA *= direction;

        float x2 = (float) ((x * Math.cos(radianA)) - (y * Math.sin(radianA)));
        float y2 = (float) ((x * Math.sin(radianA)) + (y * Math.cos(radianA)));

        if (pivot != null) {
            x2 += pivot.x;
            y2 += pivot.y;
        }

        return new Point((int) (x2 + 0.5f), (int) (y2 + 0.5f));
    }

    //------------------------------- Some Random Effects -----------------------//


    public static Bitmap logoPrinter(@Nullable Canvas drawCanvas, int width, int height, @FloatRange(from = 0, to = 1f) float occurrence, int divideFactor, Printer printer
            , /* If @drawCanvas is not null , this has no effect*/ int backgroundColor) {
        MapMaker mapMaker = new MapMaker(width, height, divideFactor, 1, true);
        MapItem[] mapItems = mapMaker.buildMap();

        Bitmap out = null;
        if (drawCanvas == null) {
            out = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            drawCanvas = new Canvas(out);
            drawCanvas.drawColor(backgroundColor);
        }

        for (int i = 0; i < mapItems.length; i++) {
            if (Math.random() <= occurrence) {
                MapItem item = mapItems[i];
                int save = drawCanvas.save();
                drawCanvas.translate(item.left, item.top);
                printer.draw(drawCanvas, item.right - item.left, item.bottom - item.top, i);
                drawCanvas.restoreToCount(save);
            }
        }

        return out;
    }


    public interface Printer {
        void draw(Canvas canvas, int width, int height, int cellNumber);
    }

    public static class RandomCharacterPrinter implements Printer {

        private char[] chars;
        private Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);

        public RandomCharacterPrinter(@NonNull String chars, int color) {
            this.chars = chars.toCharArray();
            paint.setColor(color);
            paint.setStyle(Paint.Style.FILL);
            paint.setTextAlign(Paint.Align.CENTER);
        }

        public RandomCharacterPrinter(int color) {
            chars = "QWERTYUIOPASDFGHJKLZXCVBNM".toCharArray();
            paint.setColor(color);
            paint.setStyle(Paint.Style.FILL);
            paint.setTextAlign(Paint.Align.CENTER);
        }

        @Override
        public void draw(Canvas canvas, int width, int height, int cellNumber) {
            float size = Math.nextAfter(chars.length, Float.NEGATIVE_INFINITY);
            paint.setTextSize(Math.min(height / 2f, width / 2f));


//            paint.setColor((int) (Math.random() * 0x8fffffff));
            paint.setStyle(Paint.Style.STROKE);
            canvas.drawRect(0, 0, width, height, paint);
            paint.setStyle(Paint.Style.FILL);


            canvas.save();
            canvas.rotate((float) (Math.random() * 360), width / 2f, height / 2f);
            canvas.drawText("" + chars[(int) (Math.random() * size)], width / 2f, height / 2f, paint);
            canvas.restore();
        }
    }

}
