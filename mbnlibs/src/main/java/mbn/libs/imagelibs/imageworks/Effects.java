package mbn.libs.imagelibs.imageworks;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Looper;
import android.view.View;

import androidx.annotation.FloatRange;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.palette.graphics.Palette;

import java.util.Arrays;
import java.util.Random;

import mbn.libs.backgroundtask.BaseTaskHolder;
import mbn.libs.backgroundtask.ThreadManager;
import mbn.libs.utils.JavaUtils;

public class Effects {

    public static final float[] EDGE_DETECT_KERNEL = new float[]{-1, -1, -1, -1, 8, -1, -1, -1, -1};
    public static final float[] BOX_BLUR_KERNEL = new float[]{1, 1, 1, 1, 1, 1, 1, 1, 1};
    public static final float[] SMOOTH_KERNEL = new float[]{1, 1, 1, 1, 0, 1, 1, 1, 1};
    public static final float[] SMOOTH_BOX_BLUR_KERNEL = new float[]{1, 1, 1, 1, 2, 1, 1, 1, 1};
    public static final float[] BOX_BLUR_DIAMOND_KERNEL = new float[]{
            0.2f, 1, 0.2f,
            1, 1, 1,
            0.2f, 1, 0.2f};
    public static final float[] MOTION_BLUR_HORIZONTAL_KERNEL = new float[]{
            0, 0, 0,
            1, 1, 1,
            0, 0, 0};
    public static final float[] MOTION_BLUR_HORIZONTAL_KERNEL_TEST = new float[]{
            0, 0, 0,
            1, 1, -1,
            0, 0, 0};
    public static final float[] RISE_KERNEL = new float[]{-1, -1, -1, -1, 10, -1, -1, -1, -1};
    public static final float[] GAUSSIAN_KERNEL = new float[]{1, 3, 1, 3, 9, 3, 1, 3, 1};
    public static final float[] TEST_KERNEL_1 = new float[]{
            -1, -1, -1,
            2, 2, 2,
            -1, -1, -1};
    public static final float[] TEST_KERNEL_2 = new float[]{
            -1, 2, -1,
            -1, 2, -1,
            -1, 2, -1};
    public static final float[] TEST_KERNEL_3 = new float[]{
            -1, -1, 2,
            -1, 2, -1,
            2, -1, -1};
    public static final float[] TEST_KERNEL_Y = new float[]{
            -1, -2, -1,
            0, 0, 0,
            1, 2, 1};
    public static final float[] TEST_KERNEL_X = new float[]{
            -1, 0, 1,
            -2, 0, 2,
            -1, 0, 1};

    public static Bitmap sketch(Bitmap bitmap) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        Bitmap out = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(out);
        canvas.drawColor(Color.WHITE);
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (isBorder(x, y, bitmap)) out.setPixel(x, y, Color.DKGRAY);
            }
        }
        return out;
    }

    private static boolean isBorder(int x, int y, Bitmap bitmap) {
        float currentPixel = getMonochromePercentage(bitmap.getPixel(x, y));
        float difScale = 0.1f;
        return (y - 1 >= 0) && (Math.abs(currentPixel - getMonochromePercentage(bitmap.getPixel(x, y - 1))) >= difScale)
                || (y + 1 < bitmap.getHeight()) && (Math.abs(currentPixel - getMonochromePercentage(bitmap.getPixel(x, y + 1))) >= difScale)
                || (x - 1 >= 0) && (Math.abs(currentPixel - getMonochromePercentage(bitmap.getPixel(x - 1, y))) >= difScale)
                || (x + 1 < bitmap.getWidth()) && (Math.abs(currentPixel - getMonochromePercentage(bitmap.getPixel(x + 1, y))) >= difScale);

    }

//    public static int negativeColor(int color) {
//        int r = 255 - Color.red(color);
//        int b = 255 - Color.blue(color);
//        int g = 255 - Color.green(color);
//        return Color.argb(Color.alpha(color), r, g, b);
//    }

    public static int negativeColor(int color) {
        int a = color & 0xff000000;
        color = ~(color | 0xff000000);
        return a | color;
    }

    public static void getNegativePixels(int[] pixels) {
        for (int i = 0; i < pixels.length; i++) {
            pixels[i] = negativeColor(pixels[i]);
        }
    }

    public static int[] getNegativePixels_newArray(int[] pixels) {
        int[] out = new int[pixels.length];
        for (int i = 0; i < pixels.length; i++) {
            out[i] = negativeColor(pixels[i]);
        }
        return out;
    }

    public static Bitmap getNegativeBitmap(Bitmap bitmap) {
        int[] pixels = getPixels(bitmap);
        getNegativePixels(pixels);
        return createAndSetPixels(pixels, bitmap.getWidth(), bitmap.getHeight());
    }

    public static int getMonochromePixel(int color) {
        int r = Color.red(color);
        int b = Color.blue(color);
        int g = Color.green(color);
        int average = (int) ((r + b + g) / 3f);
        return Color.rgb(average, average, average);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public static int getMonochromePixel_lumination(int color) {
        return getMonoChromeColorFromPercentage(getMonochromePercentage_lumination(color));
    }

    public static float getMonochromePercentage(int color) {
        int r = Color.red(color);
        int b = Color.blue(color);
        int g = Color.green(color);
        float average = (r + b + g) / 3f;
        return average / 255f;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public static float getMonochromePercentage_lumination(int color) {
        return Color.luminance(color);
    }

    public static int getMonoChromeColorFromPercentage(float pr) {
        int value = (int) (255 * pr);
        return Color.rgb(value, value, value);
    }

    public static Bitmap newBlur(int radios, Bitmap bitmap) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();

        int[] pixels = getPixels(bitmap);
        int[] outPixels = new int[width * height];
        int[] reds = new int[width * height];
        int[] blues = new int[width * height];
        int[] greens = new int[width * height];
        int[][] distances = new int[radios + 1][radios + 1];
        findDistances(distances, radios);
        extractPixels(pixels, reds, blues, greens);

//        CalculatedPixel[] calculatedPixels = new CalculatedPixel[width * height];

        int sR, sB, sG;
        float count;
        float factor;
        float distance;
        int curX, curY;
        int num;

        int useA;
        int useB;
        int min;
        int max;

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
//                findBlurredPixel(radios, x, y, width, height, outPixels, reds, blues, greens, distances);

                sR = 0;
                sB = 0;
                sG = 0;
                count = 0;
                for (int a = -radios; a < radios; a++) {
                    for (int b = -radios; b < radios; b++) {
                        curX = x + b;
                        curY = y + a;
                        if (curX >= 0 && curX < width && curY >= 0 && curY < height) {
                            useA = Math.abs(a);
                            useB = Math.abs(b);
                            min = Math.min(useA, useB);
                            max = Math.max(useA, useB);
                            distance = distances[min][max];
                            if (distance < radios) {
                                if ((factor = ((radios - distance) / radios)) > 0) {

                                    count += factor;
                                    num = (curY * width) + curX;

                                    sR += (reds[num] * factor);
                                    sB += (blues[num] * factor);
                                    sG += (greens[num] * factor);
                                }
                            }
                        }
                    }
                }
                outPixels[(y * width) + x] = Color.rgb((int) (sR / count), (int) (sG / count), (int) (sB / count));
            }
        }
        return createAndSetPixels(outPixels, width, height);
    }

    public static Bitmap createAndSetPixels(int[] pixels, int width, int height) {
        Bitmap out = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        out.setPixels(pixels, 0, width, 0, 0, width, height);
        return out;
    }

    public static int[] getPixels(Bitmap bitmap) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int[] pixels = new int[width * height];
        bitmap.getPixels(pixels, 0, width, 0, 0, width, height);
        return pixels;
    }

    public static Bitmap createBlackAndWhite(Bitmap bitmap) {
        int[] pixels = getPixels(bitmap);
        for (int i = 0; i < pixels.length; i++) {
//            pixels[i] = getMonoChromeColorFromPercentage(Color.luminance(pixels[i]));
            pixels[i] = getMonochromePixel(pixels[i]);
        }
        return createAndSetPixels(pixels, bitmap.getWidth(), bitmap.getHeight());
    }

    public static void createBlackAndWhite(int[] pixels) {
        for (int i = 0; i < pixels.length; i++) {
//            pixels[i] = getMonoChromeColorFromPercentage(Color.luminance(pixels[i]));
            pixels[i] = getMonochromePixel(pixels[i]);
        }
    }

    public static int[] createBlackAndWhite_pixels(Bitmap bitmap) {
        int[] pixels = getPixels(bitmap);
        for (int i = 0; i < pixels.length; i++) {
//            pixels[i] = getMonoChromeColorFromPercentage(Color.luminance(pixels[i]));
            pixels[i] = getMonochromePixel(pixels[i]);
        }
        return pixels;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public static Bitmap createBlackAndWhite_luminance(Bitmap bitmap) {
        int[] pixels = getPixels(bitmap);
        for (int i = 0; i < pixels.length; i++) {
            pixels[i] = getMonochromePixel_lumination(pixels[i]);
        }
        return createAndSetPixels(pixels, bitmap.getWidth(), bitmap.getHeight());
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public static void createBlackAndWhite_luminance(int[] pixels) {
        for (int i = 0; i < pixels.length; i++) {
            pixels[i] = getMonochromePixel_lumination(pixels[i]);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public static int[] createBlackAndWhite_pixels_luminance(Bitmap bitmap) {
        int[] pixels = getPixels(bitmap);
        for (int i = 0; i < pixels.length; i++) {
            pixels[i] = getMonochromePixel_lumination(pixels[i]);
        }
        return pixels;
    }

    public static void palePixels(int[] pixels, int factor) {
        for (int i = 0; i < pixels.length; i++) {
            pixels[i] = MbnUtils.colorWhitener2(pixels[i], factor);
        }
    }

    public static Bitmap createPaleBitmap(Bitmap bitmap, int factor) {
        int[] pixels = getPixels(bitmap);
        palePixels(pixels, factor);
        return createAndSetPixels(pixels, bitmap.getWidth(), bitmap.getHeight());
    }

    private static int findBlurredPixel(int radios, int x, int y, int width, int height, int[] outPixels, int[] reds, int[] blues, int[] greens, int[][] distances) {
        int sR = 0, sB = 0, sG = 0;
        float count = 0;
        float factor;
        float distance;
        int curX, curY;

        for (int a = -radios; a < radios; a++) {
            for (int b = -radios; b < radios; b++) {
                curX = x + b;
                curY = y + a;

                if (curX >= 0 && curX < width && curY >= 0 && curY < height &&
                        (distance = searchForDistance(b, a, distances)) < radios &&
                        (factor = ((radios - distance) / radios)) > 0) {
                    count += factor;
                    int num = (curY * width) + curX;

                    sR += (reds[num] * factor);
                    sB += (blues[num] * factor);
                    sG += (greens[num] * factor);


                    /*
                    CalculatedPixel calculatedPixel = calculatedPixels[num];
                    if (calculatedPixel == null) {
                        calculatedPixel = new CalculatedPixel();
                        calculatedPixels[num] = calculatedPixel;
                    }
                    if (!calculatedPixel.isBaked()) {
                        calculatedPixel.setColor(pixels[num]);
                    }

                    sR += (calculatedPixel.r * factor);
                    sB += (calculatedPixel.b * factor);
                    sG += (calculatedPixel.g * factor);
                    */
                }
            }
        }

        int outColor = Color.rgb((int) (sR / count), (int) (sG / count), (int) (sB / count));
        outPixels[(y * width) + x] = outColor;

//        for (int a = -radios; a < radios; a++) {
//            for (int b = -radios; b < radios; b++) {
//                int curX = x + b;
//                int curY = y + a;
//                if (curX >= 0 && curX < width && curY >= 0 && curY < height) {
//                    pixels[(curY * width) + curX] = outColor;
//                }
//            }
//        }

        return outColor;
    }

    private static void findDistances(int[][] distArray, int radios) {
        for (int a = 0; a <= radios; a++) {
            for (int b = a; b <= radios; b++) {
                distArray[a][b] = getDistance(a, b);
            }
        }
    }

    private static void extractPixels(int[] pixels, int[] reds, int[] blues, int[] greens) {
        for (int i = 0; i < pixels.length; i++) {
            int p = pixels[i];

            reds[i] = (p & 0xff0000) >> 16;
            greens[i] = (p & 0x00ff00) >> 8;
            blues[i] = (p & 0x0000ff);

//            reds[i] = Color.red(pixels[i]);
//            blues[i] = Color.blue(pixels[i]);
//            greens[i] = Color.green(pixels[i]);
        }
    }

    private static float searchForDistance(int a, int b, int[][] distances) {
        int useA = Math.abs(a);
        int useB = Math.abs(b);
        int min = Math.min(useA, useB);
        int max = Math.max(useA, useB);
        return distances[min][max];
    }

    private static int getDistance(int x, int y) {
        return (int) Math.sqrt(x * x + y * y);
    }

    public static void cropToSize(Bitmap outBitmap, Bitmap inBitmap, @Nullable Paint paint) {
        Canvas canvas = new Canvas(outBitmap);
        float factor = Math.max(((float) outBitmap.getWidth()) / inBitmap.getWidth(), ((float) outBitmap.getHeight()) / inBitmap.getHeight());

        int useW = (int) (factor * inBitmap.getWidth());
        int useH = (int) (factor * inBitmap.getHeight());

        int diffHalfWidth = (useW - outBitmap.getWidth()) / 2;
        int diffHalfHeight = (useH - outBitmap.getHeight()) / 2;

        Rect rect = new Rect(-diffHalfWidth, -diffHalfHeight, outBitmap.getWidth() + diffHalfWidth, outBitmap.getHeight() + diffHalfHeight);
        canvas.drawBitmap(inBitmap, null, rect, paint);
    }

    public static void fit(Bitmap outBitmap, Bitmap inBitmap, @Nullable Paint paint) {
        Canvas canvas = new Canvas(outBitmap);
        float factor = Math.min(((float) outBitmap.getWidth()) / inBitmap.getWidth(), ((float) outBitmap.getHeight()) / inBitmap.getHeight());

        int useW = (int) (factor * inBitmap.getWidth());
        int useH = (int) (factor * inBitmap.getHeight());

        int diffHalfWidth = (useW - outBitmap.getWidth()) / 2;
        int diffHalfHeight = (useH - outBitmap.getHeight()) / 2;

        Rect rect = new Rect(-diffHalfWidth, -diffHalfHeight, outBitmap.getWidth() + diffHalfWidth, outBitmap.getHeight() + diffHalfHeight);
        canvas.drawBitmap(inBitmap, null, rect, paint);
    }

    public static Bitmap createLegoBitmap(int backgroundColor, @Nullable Bitmap backgroundBitmap, int overlayColor, @Nullable Bitmap overLayBitmap, LegoProvider legoProvider, int bitmapWidth, int bitmapHeight, int columnCount, int emptySpaceCount) {

        if (emptySpaceCount > 1) columnCount += columnCount % emptySpaceCount == 0 ? 1 : 0;
        float cellWidth = ((float) bitmapWidth) / columnCount;
        legoProvider.setCellWidth(cellWidth);
        int rowCount = (int) Math.ceil(bitmapHeight / cellWidth);
        int totalCount = rowCount * columnCount;

        Bitmap legoBitmap = Bitmap.createBitmap(bitmapWidth, bitmapHeight, Bitmap.Config.ARGB_8888);
        Canvas legoCanvas = new Canvas(legoBitmap);

        for (int i = 0; i < totalCount; i++) {
            if (i % emptySpaceCount == 0) {
                LegoPlacer placer = legoProvider.generateLegoPlacer();
                drawLego(legoCanvas, i, columnCount, placer);
            }
        }
        if (overLayBitmap != null) {
            Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
            cropToSize(legoBitmap, overLayBitmap, paint);
        } else {
            legoCanvas.drawColor(overlayColor, PorterDuff.Mode.SRC_IN);
        }

        Bitmap outBitmap = Bitmap.createBitmap(bitmapWidth, bitmapHeight, Bitmap.Config.ARGB_8888);
        Canvas outCanvas = new Canvas(outBitmap);

        if (backgroundBitmap != null) {
            cropToSize(outBitmap, backgroundBitmap, null);
        } else {
            outCanvas.drawColor(backgroundColor);
        }

        outCanvas.drawBitmap(legoBitmap, 0, 0, null);
        return outBitmap;
    }

    public static void drawLego(Canvas canvas, int number, int cellColumnCount, LegoPlacer legoPlacer) {
        int save = canvas.save();

        int row = number / cellColumnCount;
        int column = number % cellColumnCount;
        int startX = (int) (column * legoPlacer.cellWidth);
        int startY = (int) (row * legoPlacer.cellWidth);

        canvas.translate(startX, startY);
        legoPlacer.draw(canvas);

        canvas.restoreToCount(save);
    }

    public static Bitmap boxBlur(int passes, int radios, Bitmap bitmap) {
        return internalBoxBlur(passes, radios, getPixels(bitmap), bitmap.getWidth(), bitmap.getHeight());
    }

    public static Bitmap internalBoxBlur(int passes, int radios, int[] pixels, int width, int height) {
        passes--;
        int[] outPixels = new int[pixels.length];
        int useRadios = radios % 2 == 0 ? radios + 1 : radios;
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
                outPixels[(i * width) + j] = outColor;
            }
        }

        if (passes > 0) {
            return internalBoxBlur(passes, radios, outPixels, width, height);
        } else return createAndSetPixels(outPixels, width, height);
    }

    public static int[] internalBoxBlur_newPixelsArray(int passes, int radios, int[] pixels, int width, int height) {
//        passes--;
//        int[] outPixels = new int[pixels.length];
        int[] outPixels = Arrays.copyOf(pixels, pixels.length);
//        int useRadios = radios % 2 == 0 ? radios + 1 : radios;
////        useRadios *= 2;
//
//        int[][] alphas = new int[height][width];
//        int[][] reds = new int[height][width];
//        int[][] blues = new int[height][width];
//        int[][] greens = new int[height][width];
//
//        int a, r, b, g;
//
//        for (int i = 0; i < height; i++) {
//            a = r = b = g = 0;
//            for (int j = 0; j < width; j++) {
//                int color = pixels[(i * width) + j];
//                a += Color.alpha(color);
//                r += Color.red(color);
//                b += Color.blue(color);
//                g += Color.green(color);
//
//                alphas[i][j] = a;
//                reds[i][j] = r;
//                blues[i][j] = b;
//                greens[i][j] = g;
//            }
//        }
//
//        for (int i = 0; i < height; i++) {
//            for (int j = 0; j < width; j++) {
//                int left = (j - useRadios) - 1;
//                if (left < -1) left = -1;
////                while (left < -1) {
////                    left++;
////                }
//
//                int right = j + useRadios;
//                if (right >= width) right = width - 1;
////                while (right >= width) {
////                    right--;
////                }
//
//                int top = i - useRadios;
//                if (top < 0) top = 0;
////                while (top < 0) {
////                    top++;
////                }
//
//                int bottom = i + useRadios;
//                if (bottom >= height) bottom = height - 1;
////                while (bottom >= height) {
////                    bottom--;
////                }
//
//                int pixelCount = (right - left) * ((bottom - top) + 1);
//                a = r = b = g = 0;
//
//                for (int k = top; k <= bottom; k++) {
//                    if (left == -1) {
//                        a += alphas[k][right];
//                        r += reds[k][right];
//                        b += blues[k][right];
//                        g += greens[k][right];
//                    } else {
//                        a += alphas[k][right] - alphas[k][left];
//                        r += reds[k][right] - reds[k][left];
//                        b += blues[k][right] - blues[k][left];
//                        g += greens[k][right] - greens[k][left];
//                    }
//                }
//                int outColor = Color.argb(a / pixelCount, r / pixelCount, g / pixelCount, b / pixelCount);
//                outPixels[(i * width) + j] = outColor;
//            }
//        }

        internalBoxBlur_sameArray(passes, radios, outPixels, width, height);
        return outPixels;
    }

    public static void internalBoxBlur_sameArray(int passes, int radios, int[] pixels, int width, int height) {
        passes--;
        int useRadios = radios % 2 == 0 ? radios + 1 : radios;
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
            internalBoxBlur_sameArray(passes, radios, pixels, width, height);
        }
    }

    public static void alphaExtender_sameArray(int passes, int[] pixels, int width, int height) {
        passes--;
        int currentAlpha = 0;
        int useAlpha = 0;
        int offset;
        int color;
//        int[] indices = new int[]{0, 1, -1, width, -width};
        int[] indices = new int[]{0, 1, -1, width, -width, width - 1, width + 1, -(width - 1), -(width + 1)};
        int[] temp = new int[pixels.length];

        for (int i = 1; i < height - 1; i++) {
            for (int j = 1; j < width - 1; j++) {
                useAlpha = 0;
                offset = (i * width) + j;
                for (int index : indices) {
                    useAlpha += Color.alpha(pixels[offset + index]);
//                    if (useAlpha > currentAlpha) currentAlpha = useAlpha;
//                    currentAlpha = useAlpha > currentAlpha ? useAlpha : currentAlpha;
                }
                color = pixels[offset];
                useAlpha = useAlpha / indices.length;
                currentAlpha = color >>> 24;
                if (useAlpha > currentAlpha) {
                    temp[offset] = Color.argb(useAlpha, Color.red(color), Color.green(color), Color.blue(color));
                } else {
                    temp[offset] = color;
                }
            }
        }
        System.arraycopy(temp, 0, pixels, 0, pixels.length);
        temp = null;
        if (passes > 0) {
            alphaExtender_sameArray(passes, pixels, width, height);
        }
    }

    public static int[] alphaExtender_newArray(int passes, int[] pixels, int width, int height) {
        int[] temp = Arrays.copyOf(pixels, pixels.length);
        alphaExtender_sameArray(passes, temp, width, height);
        return temp;
    }

    public static Bitmap edgeDetect(Bitmap bitmap) {
        return createAndSetPixels(boxFiltering(getPixels(bitmap), bitmap.getWidth(), bitmap.getHeight(), EDGE_DETECT_KERNEL), bitmap.getWidth(), bitmap.getHeight());
    }

    public static Bitmap boxFilter(Bitmap bitmap, float[] kernel) {
        return createAndSetPixels(boxFiltering(getPixels(bitmap), bitmap.getWidth(), bitmap.getHeight(), kernel), bitmap.getWidth(), bitmap.getHeight());
    }

    public static Bitmap boxFilter_forkJoin(Bitmap bitmap, float[] kernel) {
        return createAndSetPixels(boxFiltering_forkJoin(getPixels(bitmap), bitmap.getWidth(), bitmap.getHeight(), kernel), bitmap.getWidth(), bitmap.getHeight());
    }

    public static Bitmap boxFilter_forkJoin_withAlpha(Bitmap bitmap, float[] kernel) {
        return createAndSetPixels(boxFiltering_forkJoin_withAlpha(getPixels(bitmap), bitmap.getWidth(), bitmap.getHeight(), kernel), bitmap.getWidth(), bitmap.getHeight());
    }

    public static Bitmap boxFilter_forkJoin(int passes, Bitmap bitmap, float[] kernel) {
        return createAndSetPixels(boxFiltering_forkJoin(passes, getPixels(bitmap), bitmap.getWidth(), bitmap.getHeight(), kernel), bitmap.getWidth(), bitmap.getHeight());
    }

    public static Bitmap boxFilter_forkJoin_withAlpha(int passes, Bitmap bitmap, float[] kernel) {
        return createAndSetPixels(boxFiltering_forkJoin_withAlpha(passes, getPixels(bitmap), bitmap.getWidth(), bitmap.getHeight(), kernel), bitmap.getWidth(), bitmap.getHeight());
    }

    public static Bitmap boxFilter(int passes, Bitmap bitmap, float[] kernel) {
        return createAndSetPixels(boxFiltering(passes, getPixels(bitmap), bitmap.getWidth(), bitmap.getHeight(), kernel), bitmap.getWidth(), bitmap.getHeight());
    }

    public static float[] createEdgeDetectKernel(int side) {
        if (side % 2 == 0 || side < 3) {
            throw new RuntimeException("Value is inappropriate.");
        }
        float[] kernel = new float[side * side];
        Arrays.fill(kernel, -1f);
        kernel[kernel.length / 2] = kernel.length - 1;

//        int center = side / 2;
//        int count = 0;
//        for (int h = 0; h < side; h++) {
//            for (int w = 0; w < side; w++) {
//                if (h == w && h == center) {
//                    kernel[count] = kernel.length - 1;
//                } else {
//                    kernel[count] = -1;
//                }
//                count++;
//            }
//        }

        return kernel;
    }

    public static float[] createGaussianEdgeDetectKernel(int side) {
        if (side % 2 == 0 || side < 3) {
            throw new RuntimeException("Value is inappropriate.");
        }
        float[] kernel = new float[side * side];
        int center = side / 2;
        double baseR = Math.sqrt(2 * Math.pow(center, 2));
        int count = 0;
        float sum = 0;
        for (int h = 0; h < side; h++) {
            for (int w = 0; w < side; w++) {
                if (!(h == w && h == center)) {
                    kernel[count] = (float) (-(1 - (Math.sqrt(Math.pow((h - center), 2) + Math.pow((w - center), 2)) / baseR)));
                    sum += kernel[count];
                }
                count++;
            }
        }
        kernel[kernel.length / 2] = -sum;
//        Log.i("test_gass", "createGaussianEdgeDetectKernel: " + Arrays.toString(kernel));
        return kernel;
    }

    public static void combine(int[] ps1, int[] ps2) {
        if (ps1.length != ps2.length) {
            throw new RuntimeException("Arrays mus have the same length.");
        }
        for (int i = 0; i < ps1.length; i++) {
            ps1[i] |= ps2[i];
        }
    }

    //----------------------- BOX FILTERING ---------------------------------//

    public static int[] boxFiltering(int[] pixels, int width, int height, float[] kernel) {
        int side = checkKernel(kernel);
        int[] temp = new int[width * height];
        float denominator = calculateDenominator(kernel);
//        float red, green, blue;
//        int ired, igreen, iblue, indexOffset, rgb;
        int[] indices = indexCreator(side, width);
        applyKernel(pixels, temp, width, height, denominator, side / 2, indices, kernel);
//        for (int i = 1; i < height - 1; i++) {
//            for (int j = 1; j < width - 1; j++) {
//                red = green = blue = 0.0f;
//                indexOffset = (i * width) + j;
//                for (int k = 0; k < kernel.length; k++) {
//                    rgb = pixels[indexOffset + indices[k]];
//                    red += ((rgb & 0xff0000) >> 16) * kernel[k];
//                    green += ((rgb & 0xff00) >> 8) * kernel[k];
//                    blue += (rgb & 0xff) * kernel[k];
//                }
//                ired = (int) (red / denominator);
//                igreen = (int) (green / denominator);
//                iblue = (int) (blue / denominator);
//                if (ired > 0xff) ired = 0xff;
//                else if (ired < 0) ired = 0;
//                if (igreen > 0xff) igreen = 0xff;
//                else if (igreen < 0) igreen = 0;
//                if (iblue > 0xff) iblue = 0xff;
//                else if (iblue < 0) iblue = 0;
//                temp[indexOffset] = 0xff000000 | ((ired << 16) & 0xff0000) |
//                        ((igreen << 8) & 0xff00) | (iblue & 0xff);
//            }
//        }
        return temp;
    }

    public static int[] boxFiltering_withAlpha(int[] pixels, int width, int height, float[] kernel) {
        int side = checkKernel(kernel);
        int[] temp = new int[width * height];
        float denominator = calculateDenominator(kernel);
        int[] indices = indexCreator(side, width);
        applyKernel_withAlpha(pixels, temp, width, height, denominator, side / 2, indices, kernel);
//        int[] temp = new int[width * height];
//        float denominator = 0.0f;
//        float alpha, red, green, blue;
//        int ialpha, ired, igreen, iblue, indexOffset, rgb;
//        int[] indices = {
//                -(width + 1), -width, -(width - 1),
//                -1, 0, +1,
//                width - 1, width, width + 1
//        };
//        for (int i = 0; i < kernel.length; i++) {
//            denominator += kernel[i];
//        }
//        if (denominator == 0.0f) denominator = 1.0f;
//        for (int i = 1; i < height - 1; i++) {
//            for (int j = 1; j < width - 1; j++) {
//                alpha = red = green = blue = 0.0f;
//                indexOffset = (i * width) + j;
//                for (int k = 0; k < kernel.length; k++) {
//                    rgb = pixels[indexOffset + indices[k]];
//                    alpha += ((rgb & 0xff000000) >>> 24) * kernel[k];
//                    red += ((rgb & 0xff0000) >> 16) * kernel[k];
//                    green += ((rgb & 0xff00) >> 8) * kernel[k];
//                    blue += (rgb & 0xff) * kernel[k];
//                }
//                ialpha = (int) (alpha / denominator);
//                ired = (int) (red / denominator);
//                igreen = (int) (green / denominator);
//                iblue = (int) (blue / denominator);
//                if (ialpha > 0xff) ialpha = 0xff;
//                else if (ialpha < 0) ialpha = 0;
//                if (ired > 0xff) ired = 0xff;
//                else if (ired < 0) ired = 0;
//                if (igreen > 0xff) igreen = 0xff;
//                else if (igreen < 0) igreen = 0;
//                if (iblue > 0xff) iblue = 0xff;
//                else if (iblue < 0) iblue = 0;
//                temp[indexOffset] = ((ialpha << 24) & 0xff000000) | ((ired << 16) & 0xff0000) |
//                        ((igreen << 8) & 0xff00) | (iblue & 0xff);
//            }
//        }
        return temp;
    }

    public static int[] boxFiltering_withAlpha(int passes, int[] pixels, int width, int height, float[] kernel) {
        passes--;
        int side = checkKernel(kernel);
        int[] temp = new int[width * height];
        float denominator = calculateDenominator(kernel);
        int[] indices = indexCreator(side, width);
        applyKernel_withAlpha(pixels, temp, width, height, denominator, side / 2, indices, kernel);

//        for (int i = 1; i < height - 1; i++) {
//            for (int j = 1; j < width - 1; j++) {
//                alpha = red = green = blue = 0.0f;
//                indexOffset = (i * width) + j;
//                for (int k = 0; k < kernel.length; k++) {
//                    rgb = pixels[indexOffset + indices[k]];
//                    alpha += ((rgb & 0xff000000) >>> 24) * kernel[k];
//                    red += ((rgb & 0xff0000) >> 16) * kernel[k];
//                    green += ((rgb & 0xff00) >> 8) * kernel[k];
//                    blue += (rgb & 0xff) * kernel[k];
//                }
//                ialpha = (int) (alpha / denominator);
//                ired = (int) (red / denominator);
//                igreen = (int) (green / denominator);
//                iblue = (int) (blue / denominator);
//                if (ialpha > 0xff) ialpha = 0xff;
//                else if (ialpha < 0) ialpha = 0;
//                if (ired > 0xff) ired = 0xff;
//                else if (ired < 0) ired = 0;
//                if (igreen > 0xff) igreen = 0xff;
//                else if (igreen < 0) igreen = 0;
//                if (iblue > 0xff) iblue = 0xff;
//                else if (iblue < 0) iblue = 0;
//                temp[indexOffset] = ((ialpha << 24) & 0xff000000) | ((ired << 16) & 0xff0000) |
//                        ((igreen << 8) & 0xff00) | (iblue & 0xff);
//            }
//        }

        if (passes > 0) {
            return boxFiltering_withAlpha(passes, temp, width, height, kernel);
        } else return temp;
    }

    public static int[] boxFiltering(int passes, int[] pixels, int width, int height, float[] kernel) {
        passes--;
        int side = checkKernel(kernel);
        int[] temp = new int[width * height];
        float denominator = calculateDenominator(kernel);
        int[] indices = indexCreator(side, width);
        applyKernel(pixels, temp, width, height, denominator, side / 2, indices, kernel);
//        passes--;
//        int[] temp = new int[width * height];
//        float denominator = 0.0f;
//        float red, green, blue;
//        int ired, igreen, iblue, indexOffset, rgb;
//        int[] indices = {
//                -(width + 1), -width, -(width - 1),
//                -1, 0, +1,
//                width - 1, width, width + 1
//        };
//        for (int i = 0; i < kernel.length; i++) {
//            denominator += kernel[i];
//        }
//        if (denominator == 0.0f) denominator = 1.0f;
//        for (int i = 1; i < height - 1; i++) {
//            for (int j = 1; j < width - 1; j++) {
//                red = green = blue = 0.0f;
//                indexOffset = (i * width) + j;
//                for (int k = 0; k < kernel.length; k++) {
//                    rgb = pixels[indexOffset + indices[k]];
//                    red += ((rgb & 0xff0000) >> 16) * kernel[k];
//                    green += ((rgb & 0xff00) >> 8) * kernel[k];
//                    blue += (rgb & 0xff) * kernel[k];
//                }
//                ired = (int) (red / denominator);
//                igreen = (int) (green / denominator);
//                iblue = (int) (blue / denominator);
//                if (ired > 0xff) ired = 0xff;
//                else if (ired < 0) ired = 0;
//                if (igreen > 0xff) igreen = 0xff;
//                else if (igreen < 0) igreen = 0;
//                if (iblue > 0xff) iblue = 0xff;
//                else if (iblue < 0) iblue = 0;
//                temp[indexOffset] = 0xff000000 | ((ired << 16) & 0xff0000) |
//                        ((igreen << 8) & 0xff00) | (iblue & 0xff);
//            }
//        }
        if (passes > 0) {
            return boxFiltering(passes, temp, width, height, kernel);
        } else return temp;

    }

    public static void applyKernel(int[] pixels, int[] newPixels, int width, int height, float denominator, int edgeWidth, int[] indices, float[] kernel) {
        float red, green, blue;
        int ired, igreen, iblue, indexOffset, rgb;

        for (int i = edgeWidth; i < height - (edgeWidth); i++) {
            for (int j = edgeWidth; j < width - (edgeWidth); j++) {
                red = green = blue = 0.0f;
                indexOffset = (i * width) + j;
                for (int k = 0; k < kernel.length; k++) {
                    rgb = pixels[indexOffset + indices[k]];
                    red += ((rgb & 0xff0000) >> 16) * kernel[k];
                    green += ((rgb & 0xff00) >> 8) * kernel[k];
                    blue += (rgb & 0xff) * kernel[k];
                }
                ired = (int) (red / denominator);
                igreen = (int) (green / denominator);
                iblue = (int) (blue / denominator);
                if (ired > 0xff) ired = 0xff;
                else if (ired < 0) ired = 0;
                if (igreen > 0xff) igreen = 0xff;
                else if (igreen < 0) igreen = 0;
                if (iblue > 0xff) iblue = 0xff;
                else if (iblue < 0) iblue = 0;
                newPixels[indexOffset] = 0xff000000 | ((ired << 16) & 0xff0000) |
                        ((igreen << 8) & 0xff00) | (iblue & 0xff);
            }
        }
    }


    public static int[] boxFiltering_forkJoin(int[] pixels, int width, int height, float[] kernel) {
        int side = checkKernel(kernel);
        float denominator = calculateDenominator(kernel);
        int[] indices = indexCreator(side, width);

        int forkCount = 8;
        int pixelPerFork = pixels.length / forkCount;
        ForkJoinBoxFilteringTask[] tasks = new ForkJoinBoxFilteringTask[forkCount];
        Thread[] threads = new Thread[forkCount - 1];
        int arrayStartIndex, arrayEndIndex, size, startX, startY, endX, endY, edgeWidth = side / 2;

        for (int i = 0; i < forkCount; i++) {
            arrayStartIndex = i * pixelPerFork;
            arrayEndIndex = (i + 1) * pixelPerFork;
            arrayEndIndex = (pixels.length - arrayEndIndex) < pixelPerFork ? pixels.length : arrayEndIndex;
            size = arrayEndIndex - arrayStartIndex;
            startX = arrayStartIndex % width;
            startY = arrayStartIndex / width;
            ForkJoinBoxFilteringTask task = new ForkJoinBoxFilteringTask(arrayStartIndex, arrayEndIndex, size, startX, startY,
                    edgeWidth, width, height, kernel, indices, pixels, denominator);
            tasks[i] = task;
            if (i < threads.length) {
                Thread t = new Thread(task);
                threads[i] = t;
                t.start();
            } else {
                task.run();
            }
        }

        for (Thread t : threads) {
            try {
                t.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        int[][] pixelsGroup = new int[forkCount][];
        for (int i = 0; i < pixelsGroup.length; i++) {
            pixelsGroup[i] = tasks[i].getNewPixels();
        }

        return JavaUtils.putTogether(pixelsGroup);
    }

    public static int[] boxFiltering_forkJoin_withAlpha(int[] pixels, int width, int height, float[] kernel) {
        int side = checkKernel(kernel);
        float denominator = calculateDenominator(kernel);
        int[] indices = indexCreator(side, width);

        int forkCount = 8;
        int pixelPerFork = pixels.length / forkCount;
        ForkJoinBoxFilteringTask_withAlpha[] tasks = new ForkJoinBoxFilteringTask_withAlpha[forkCount];
        Thread[] threads = new Thread[forkCount - 1];
        int arrayStartIndex, arrayEndIndex, size, startX, startY, endX, endY, edgeWidth = side / 2;

        for (int i = 0; i < forkCount; i++) {
            arrayStartIndex = i * pixelPerFork;
            arrayEndIndex = (i + 1) * pixelPerFork;
            arrayEndIndex = (pixels.length - arrayEndIndex) < pixelPerFork ? pixels.length : arrayEndIndex;
            size = arrayEndIndex - arrayStartIndex;
            startX = arrayStartIndex % width;
            startY = arrayStartIndex / width;
            ForkJoinBoxFilteringTask_withAlpha task = new ForkJoinBoxFilteringTask_withAlpha(arrayStartIndex, arrayEndIndex, size, startX, startY,
                    edgeWidth, width, height, kernel, indices, pixels, denominator);
            tasks[i] = task;
            if (i < threads.length) {
                Thread t = new Thread(task);
                threads[i] = t;
                t.start();
            } else {
                task.run();
            }
        }

        for (Thread t : threads) {
            try {
                t.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        int[][] pixelsGroup = new int[forkCount][];
        for (int i = 0; i < pixelsGroup.length; i++) {
            pixelsGroup[i] = tasks[i].getNewPixels();
        }

        return JavaUtils.putTogether(pixelsGroup);
    }

    public static int[] boxFiltering_forkJoin(int passes, int[] pixels, int width, int height, float[] kernel) {
        passes--;
        int[] outPixels = boxFiltering_forkJoin(pixels, width, height, kernel);
        if (passes <= 0) {
            return outPixels;
        } else {
            return boxFiltering_forkJoin(passes, outPixels, width, height, kernel);
        }
    }

    public static int[] boxFiltering_forkJoin_withAlpha(int passes, int[] pixels, int width, int height, float[] kernel) {
        passes--;
        int[] outPixels = boxFiltering_forkJoin_withAlpha(pixels, width, height, kernel);
        if (passes <= 0) {
            return outPixels;
        } else {
            return boxFiltering_forkJoin_withAlpha(passes, outPixels, width, height, kernel);
        }
    }

    private static class ForkJoinBoxFilteringTask implements Runnable {
        private int arrayStartIndex, arrayEndIndex, size, startX, startY, edgeWidth, width, height;
        private float[] kernel;
        private int[] indices, pixels;
        private float denominator;
        private int[] newPixels;

        public int[] getNewPixels() {
            return newPixels;
        }

        public ForkJoinBoxFilteringTask(int arrayStartIndex, int arrayEndIndex, int size, int startX, int startY,
                                        int edgeWidth, int width, int height, float[] kernel,
                                        int[] indices, int[] pixels, float denominator) {
            this.arrayStartIndex = arrayStartIndex;
            this.arrayEndIndex = arrayEndIndex;
            this.size = size;
            this.startX = startX;
            this.startY = startY;
            this.edgeWidth = edgeWidth;
            this.width = width;
            this.height = height;
            this.kernel = kernel;
            this.indices = indices;
            this.pixels = pixels;
            this.denominator = denominator;
        }

        @Override
        public void run() {
            newPixels = new int[size];
            float red, green, blue;
            int ired, igreen, iblue, indexOffset, rgb;
            int count = 0;
            boolean first = true;
            retry:
            for (int i = startY; i < height; i++) {
                for (int j = first ? startX : 0; j < width; j++) {
                    if (count++ >= size) {
                        break retry;
                    }
                    if (i < edgeWidth || i >= (height - edgeWidth) || j < edgeWidth || j >= (width - edgeWidth)) {
                        continue;
                    }
                    red = green = blue = 0.0f;
                    indexOffset = (i * width) + j;
                    for (int k = 0; k < kernel.length; k++) {
                        rgb = pixels[indexOffset + indices[k]];
                        red += ((rgb & 0xff0000) >> 16) * kernel[k];
                        green += ((rgb & 0xff00) >> 8) * kernel[k];
                        blue += (rgb & 0xff) * kernel[k];
                    }
                    ired = (int) (red / denominator);
                    igreen = (int) (green / denominator);
                    iblue = (int) (blue / denominator);
                    if (ired > 0xff) ired = 0xff;
                    else if (ired < 0) ired = 0;
                    if (igreen > 0xff) igreen = 0xff;
                    else if (igreen < 0) igreen = 0;
                    if (iblue > 0xff) iblue = 0xff;
                    else if (iblue < 0) iblue = 0;
                    newPixels[indexOffset - arrayStartIndex] = 0xff000000 | ((ired << 16) & 0xff0000) |
                            ((igreen << 8) & 0xff00) | (iblue & 0xff);
                }
                first = false;
            }
        }
    }

    private static class ForkJoinBoxFilteringTask_withAlpha implements Runnable {
        private int arrayStartIndex, arrayEndIndex, size, startX, startY, edgeWidth, width, height;
        private float[] kernel;
        private int[] indices, pixels;
        private float denominator;
        private int[] newPixels;

        public int[] getNewPixels() {
            return newPixels;
        }

        public ForkJoinBoxFilteringTask_withAlpha(int arrayStartIndex, int arrayEndIndex, int size, int startX, int startY,
                                                  int edgeWidth, int width, int height, float[] kernel,
                                                  int[] indices, int[] pixels, float denominator) {
            this.arrayStartIndex = arrayStartIndex;
            this.arrayEndIndex = arrayEndIndex;
            this.size = size;
            this.startX = startX;
            this.startY = startY;
            this.edgeWidth = edgeWidth;
            this.width = width;
            this.height = height;
            this.kernel = kernel;
            this.indices = indices;
            this.pixels = pixels;
            this.denominator = denominator;
        }

        @Override
        public void run() {
            newPixels = new int[size];
            float alpha, red, green, blue;
            int ialpha, ired, igreen, iblue, indexOffset, rgb;
            int count = 0;
            boolean first = true;
            retry:
            for (int i = startY; i < height; i++) {
                for (int j = first ? startX : 0; j < width; j++) {
                    if (count++ >= size) {
                        break retry;
                    }
                    if (i < edgeWidth || i >= (height - edgeWidth) || j < edgeWidth || j >= (width - edgeWidth)) {
                        continue;
                    }
                    alpha = red = green = blue = 0.0f;
                    indexOffset = (i * width) + j;
                    for (int k = 0; k < kernel.length; k++) {
                        rgb = pixels[indexOffset + indices[k]];
                        alpha += ((rgb & 0xff000000) >>> 24) * kernel[k];
                        red += ((rgb & 0xff0000) >> 16) * kernel[k];
                        green += ((rgb & 0xff00) >> 8) * kernel[k];
                        blue += (rgb & 0xff) * kernel[k];
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
                    newPixels[indexOffset - arrayStartIndex] = ((ialpha << 24) & 0xff000000) | ((ired << 16) & 0xff0000) |
                            ((igreen << 8) & 0xff00) | (iblue & 0xff);
                }
                first = false;
            }
        }
    }

    public static void applyKernel_withAlpha(int[] pixels, int[] newPixels, int width, int height, float denominator, int edgeWidth, int[] indices, float[] kernel) {
        float alpha, red, green, blue;
        int ialpha, ired, igreen, iblue, indexOffset, rgb;

        for (int i = edgeWidth; i < height - (edgeWidth); i++) {
            for (int j = edgeWidth; j < width - (edgeWidth); j++) {
                alpha = red = green = blue = 0.0f;
                indexOffset = (i * width) + j;
                for (int k = 0; k < kernel.length; k++) {
                    rgb = pixels[indexOffset + indices[k]];
                    alpha += ((rgb & 0xff000000) >>> 24) * kernel[k];
                    red += ((rgb & 0xff0000) >> 16) * kernel[k];
                    green += ((rgb & 0xff00) >> 8) * kernel[k];
                    blue += (rgb & 0xff) * kernel[k];
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
                newPixels[indexOffset] = ((ialpha << 24) & 0xff000000) | ((ired << 16) & 0xff0000) |
                        ((igreen << 8) & 0xff00) | (iblue & 0xff);
            }
        }
    }

    public static Bitmap newEdgeDetect(Bitmap bitmap, int kernelSide, float detectFactor) {
        int[] pixels = getPixels(bitmap);
//        pixels = boxFiltering(pixels, bitmap.getWidth(), bitmap.getHeight(), BOX_BLUR_KERNEL);
//        getNegativePixels(pixels);
        pixels = newEdgeDetect(pixels, bitmap.getWidth(), bitmap.getHeight(), kernelSide, detectFactor);
        return createAndSetPixels(pixels, bitmap.getWidth(), bitmap.getHeight());
    }

    public static Bitmap newEdgeDetect2(Bitmap bitmap, int kernelSide) {
        int[] pixels = getPixels(bitmap);
//        pixels = boxFiltering(pixels, bitmap.getWidth(), bitmap.getHeight(), BOX_BLUR_KERNEL);
//        getNegativePixels(pixels);
        pixels = newEdgeDetect2(pixels, bitmap.getWidth(), bitmap.getHeight(), kernelSide);
        return createAndSetPixels(pixels, bitmap.getWidth(), bitmap.getHeight());
    }

    @NonNull
    public static int[] newEdgeDetect(@NonNull int[] pixels, int width, int height, int kernelSide, float detectFactor) {
        int[] outPixels = new int[pixels.length];
        int[] indices = indexCreator(kernelSide, width);
        int edgeWidth = kernelSide / 2;

        int alpha, red, green, blue;
        int ialpha, ired, igreen, iblue, indexOffset, rgb;

        for (int i = edgeWidth; i < height - (edgeWidth); i++) {
            for (int j = edgeWidth; j < width - (edgeWidth); j++) {
                alpha = red = green = blue = 0xff;
                indexOffset = (i * width) + j;
                for (int k = 0; k < indices.length; k++) {
                    if (indices[k] != 0) {
                        rgb = pixels[indexOffset + indices[k]];
//                    alpha &= ((rgb) >>> 24);
                        red &= ((rgb & 0xff0000) >> 16);
                        green &= ((rgb & 0xff00) >> 8);
                        blue &= (rgb & 0xff);
                    }
                }
                rgb = pixels[indexOffset];
//                ialpha = Math.abs((alpha - (rgb >>> 24)) / 255f) > detectFactor ? 0xff : 0;

//                ired = Math.abs((red - ((rgb & 0xff0000) >> 16)) / 255f) > detectFactor ? (rgb & 0xff0000) >> 16 : 0;
//                igreen = Math.abs((green - ((rgb & 0xff00) >> 8)) / 255f) > detectFactor ? (rgb & 0xff00) >> 8 : 0;
//                iblue = Math.abs((blue - (rgb & 0xff)) / 255f) > detectFactor ? rgb & 0xff : 0;

//                ired = Math.abs((red - ((rgb & 0xff0000) >> 16)) / 255f) > detectFactor ? 0xff : 0;
//                igreen = Math.abs((green - ((rgb & 0xff00) >> 8)) / 255f) > detectFactor ? 0xff : 0;
//                iblue = Math.abs((blue - (rgb & 0xff)) / 255f) > detectFactor ? 0xff : 0;


//                float pow = (float) Math.sqrt(kernelSide) * 1.7f;
                float pow = (float) Math.sqrt(kernelSide);
//                float pow = kernelSide;

                ired = (int) (Math.pow(Math.abs((red - ((rgb & 0xff0000) >> 16)) / 255f), pow) * 255);
                igreen = (int) (Math.pow(Math.abs((green - ((rgb & 0xff00) >> 8)) / 255f), pow) * 255);
                iblue = (int) (Math.pow(Math.abs((blue - (rgb & 0xff)) / 255f), pow) * 255);

//                double maxRange = Math.pow(255, pow);
//                ired = (int) JavaUtils.rangeChange(Math.pow(Math.abs((red - ((rgb & 0xff0000) >> 16))), pow), 0, maxRange, 0, 255);
//                igreen = (int) JavaUtils.rangeChange(Math.pow(Math.abs((green - ((rgb & 0xff00) >> 8))), pow), 0, maxRange, 0, 255);
//                iblue = (int) JavaUtils.rangeChange(Math.pow(Math.abs((blue - (rgb & 0xff))), pow), 0, maxRange, 0, 255);

//                ired = (int) (Math.pow(Math.abs(((red) & 0xff)), pow) / Math.pow(255, pow));
//                igreen = (int) (Math.pow(Math.abs(((green) & 0xff)), pow) / Math.pow(255, pow));
//                iblue = (int) (Math.pow(Math.abs(((blue) & 0xff)), pow) / Math.pow(255, pow));


//                if (ialpha > 0xff) ialpha = 0xff;
//                else if (ialpha < 0) ialpha = 0;
//                if (ired > 0xff) ired = 0xff;
//                else if (ired < 0) ired = 0;
//                if (igreen > 0xff) igreen = 0xff;
//                else if (igreen < 0) igreen = 0;
//                if (iblue > 0xff) iblue = 0xff;
//                else if (iblue < 0) iblue = 0;

//                outPixels[indexOffset] = (0xff << 24) | ((ired << 16) & 0xff0000) | ((igreen << 8) & 0xff00) | (iblue & 0xff);

                int cutOut = (int) (255 * 0.05f);

                if (ired <= cutOut || igreen <= cutOut || iblue <= cutOut) {
                    outPixels[indexOffset] = 0xff000000;

                } else {
//                    ired = (int) JavaUtils.rangeChange(ired, cutOut, 255, 50, 255);
//                    igreen = (int) JavaUtils.rangeChange(igreen, cutOut, 255, 50, 255);
//                    iblue = (int) JavaUtils.rangeChange(iblue, cutOut, 255, 50, 255);
                    //noinspection NumericOverflow
                    outPixels[indexOffset] = getMonochromePixel((0xff << 24) | ((ired << 16) & 0xff0000) | ((igreen << 8) & 0xff00) | (iblue & 0xff));
                }
            }
        }

        return outPixels;
    }

    @NonNull
    public static int[] newEdgeDetect2(@NonNull int[] pixels, int width, int height, int kernelSide) {
        int[] outPixels = new int[pixels.length];
        int[] indices = indexCreator(kernelSide, width);
        int edgeWidth = kernelSide / 2;
        float[] kernel = createGaussianEdgeDetectKernel(kernelSide);
        float denominator = calculateDenominator(kernel);

        float red, green, blue;
        int ired, igreen, iblue, indexOffset, rgb;

        for (int i = edgeWidth; i < height - (edgeWidth); i++) {
            for (int j = edgeWidth; j < width - (edgeWidth); j++) {
                red = green = blue = 0.0f;
                indexOffset = (i * width) + j;
                for (int k = 0; k < kernel.length; k++) {
                    rgb = pixels[indexOffset + indices[k]];
                    red += ((rgb & 0xff0000) >> 16) * kernel[k];
                    green += ((rgb & 0xff00) >> 8) * kernel[k];
                    blue += (rgb & 0xff) * kernel[k];
                }
                ired = (int) (red / denominator);
                igreen = (int) (green / denominator);
                iblue = (int) (blue / denominator);


                if (ired < 0) ired *= -1;
                if (ired > 0xff) ired = 0xff;

                if (igreen < 0) igreen *= -1;
                if (igreen > 0xff) igreen = 0xff;

                if (iblue < 0) iblue *= -1;
                if (iblue > 0xff) iblue = 0xff;


//                rgb = pixels[indexOffset];
                float pow = 3f;
                ired = (int) (Math.pow(ired / 255f, pow) * 255);
                igreen = (int) (Math.pow(igreen / 255f, pow) * 255);
                iblue = (int) (Math.pow(iblue / 255f, pow) * 255);

//                outPixels[indexOffset] = 0xff000000 | ((ired << 16) & 0xff0000) | ((igreen << 8) & 0xff00) | (iblue & 0xff);

                //noinspection NumericOverflow
                outPixels[indexOffset] = getMonochromePixel((0xff << 24) | ((ired << 16) & 0xff0000) | ((igreen << 8) & 0xff00) | (iblue & 0xff));
            }
        }

        return outPixels;
    }

    public static Bitmap newEdgeDetect3(Bitmap bitmap, int kernelSide) {
        int[] pixels = getPixels(bitmap);
//        pixels = boxFiltering(pixels, bitmap.getWidth(), bitmap.getHeight(), BOX_BLUR_KERNEL);
//        getNegativePixels(pixels);
        pixels = newEdgeDetect3(pixels, bitmap.getWidth(), bitmap.getHeight(), kernelSide);
        return createAndSetPixels(pixels, bitmap.getWidth(), bitmap.getHeight());
    }

    @NonNull
    public static int[] newEdgeDetect3(@NonNull int[] pixels, int width, int height, int kernelSide) {
        int[] outPixels = new int[pixels.length];
        int[] indices = indexCreator(kernelSide, width);
        int edgeWidth = kernelSide / 2;

        float red, green, blue;
        int ired, igreen, iblue, indexOffset, rgb, rbgMain;

//        float pow = (float) Math.sqrt(kernelSide);
        float pow = 1.1f;
        float factor = 2f;
        int count = (kernelSide * kernelSide) - 1;

        for (int i = edgeWidth; i < height - (edgeWidth); i++) {
            for (int j = edgeWidth; j < width - (edgeWidth); j++) {
                red = green = blue = 0;
                indexOffset = (i * width) + j;
                rbgMain = pixels[indexOffset];
                for (int k = 0; k < indices.length; k++) {
                    if (indices[k] != 0) {
                        rgb = pixels[indexOffset + indices[k]];
                        red += Math.abs(((rgb & 0xff0000) >> 16) - ((rbgMain & 0xff0000) >> 16));
                        green += Math.abs(((rgb & 0xff00) >> 8) - ((rbgMain & 0xff00) >> 8));
                        blue += Math.abs((rgb & 0xff) - (rbgMain & 0xff));
                    }
                }

                red = red / (count * 255f);
                green = green / (count * 255f);
                blue = blue / (count * 255f);

                red = (float) (1 - JavaUtils.rangeChange(Math.abs(0.5f - red), 0, 0.5, 0, 1));
                green = (float) (1 - JavaUtils.rangeChange(Math.abs(0.5f - green), 0, 0.5, 0, 1));
                blue = (float) (1 - JavaUtils.rangeChange(Math.abs(0.5f - blue), 0, 0.5, 0, 1));

//                float pow = (float) Math.sqrt(kernelSide) * 1.7f;
                ired = (int) (Math.pow(red, pow) * 255);
                igreen = (int) (Math.pow(green, pow) * 255);
                iblue = (int) (Math.pow(blue, pow) * 255);


//                outPixels[indexOffset] = (0xff << 24) | ((ired << 16) & 0xff0000) | ((igreen << 8) & 0xff00) | (iblue & 0xff);
                //noinspection NumericOverflow
                outPixels[indexOffset] = getMonochromePixel((0xff << 24) | ((ired << 16) & 0xff0000) | ((igreen << 8) & 0xff00) | (iblue & 0xff));

//                int cutOut = (int) (255 * 0.05f);
//                if (ired <= cutOut || igreen <= cutOut || iblue <= cutOut) {
//                    outPixels[indexOffset] = 0xff000000;
//                } else {
//                    //noinspection NumericOverflow
//                    outPixels[indexOffset] = getMonochromePixel((0xff << 24) | ((ired << 16) & 0xff0000) | ((igreen << 8) & 0xff00) | (iblue & 0xff));
//                }
            }
        }

        return outPixels;
    }

    public static Bitmap newEdgeDetect4(Bitmap bitmap) {
        int[] pixels = getPixels(bitmap);
        pixels = newEdgeDetect4(pixels, bitmap.getWidth(), bitmap.getHeight());
        return createAndSetPixels(pixels, bitmap.getWidth(), bitmap.getHeight());
    }

    public static int[] newEdgeDetect4(@NonNull int[] pixels, int width, int height) {
        int[] noiseReduced = noiseReduction2(pixels, width, height, 3);
        getNegativePixels(noiseReduced);
        noiseReduced = boxFiltering_forkJoin(noiseReduced, width, height, EDGE_DETECT_KERNEL);
        for (int i = 0; i < noiseReduced.length; i++) {
            noiseReduced[i] = getMonochromePixel(noiseReduced[i]);
        }
        return noiseReduced;
    }

    public static Bitmap noiseReduction(Bitmap bitmap, int kernelSide) {
        int[] pixels = getPixels(bitmap);
//        pixels = boxFiltering(pixels, bitmap.getWidth(), bitmap.getHeight(), BOX_BLUR_KERNEL);
//        getNegativePixels(pixels);
        pixels = noiseReduction(pixels, bitmap.getWidth(), bitmap.getHeight(), kernelSide);
        return createAndSetPixels(pixels, bitmap.getWidth(), bitmap.getHeight());
    }

    @NonNull
    public static int[] noiseReduction(@NonNull int[] pixels, int width, int height, int kernelSide) {
        int[] mapPixels = new int[pixels.length];
        int[] indices = indexCreator(kernelSide, width);
        int edgeWidth = kernelSide / 2;

        float red, green, blue;
//        int ired, igreen, iblue;
        int rgb, rbgMain, indexOffset;

//        float pow = (float) Math.sqrt(kernelSide);
        float pow = 1.5f;
        float factor = 0.9f;
        int count = (kernelSide * kernelSide) - 1;
        for (int i = edgeWidth; i < height - (edgeWidth); i++) {
            for (int j = edgeWidth; j < width - (edgeWidth); j++) {
                red = green = blue = 0;
                indexOffset = (i * width) + j;
                rbgMain = pixels[indexOffset];
                for (int k = 0; k < indices.length; k++) {
                    if (indices[k] != 0) {
                        rgb = pixels[indexOffset + indices[k]];
                        red += Math.abs(((rgb & 0xff0000) >> 16) - ((rbgMain & 0xff0000) >> 16));
                        green += Math.abs(((rgb & 0xff00) >> 8) - ((rbgMain & 0xff00) >> 8));
                        blue += Math.abs((rgb & 0xff) - (rbgMain & 0xff));
                    }
                }

                red = red / (count * 255f);
                green = green / (count * 255f);
                blue = blue / (count * 255f);

                red = (float) (JavaUtils.rangeChange(Math.abs(0.5f - red), 0, 0.5, 0, 1));
                green = (float) (JavaUtils.rangeChange(Math.abs(0.5f - green), 0, 0.5, 0, 1));
                blue = (float) (JavaUtils.rangeChange(Math.abs(0.5f - blue), 0, 0.5, 0, 1));

//                float pow = (float) Math.sqrt(kernelSide) * 1.7f;
                red = (float) Math.pow(red, pow);
                green = (float) Math.pow(green, pow);
                blue = (float) Math.pow(blue, pow);

                if (red >= factor || green >= factor || blue >= factor) {
                    mapPixels[indexOffset] = 3;
                } else {
                    mapPixels[indexOffset] = 1;
                }
//                outPixels[indexOffset] = (0xff << 24) | ((ired << 16) & 0xff0000) | ((igreen << 8) & 0xff00) | (iblue & 0xff);
//                noinspection NumericOverflow
//                outPixels[indexOffset] = getMonochromePixel((0xff << 24) | ((ired << 16) & 0xff0000) | ((igreen << 8) & 0xff00) | (iblue & 0xff));

//                int cutOut = (int) (255 * 0.05f);
//                if (ired <= cutOut || igreen <= cutOut || iblue <= cutOut) {
//                    outPixels[indexOffset] = 0xff000000;
//                } else {
//                    //noinspection NumericOverflow
//                    outPixels[indexOffset] = getMonochromePixel((0xff << 24) | ((ired << 16) & 0xff0000) | ((igreen << 8) & 0xff00) | (iblue & 0xff));
//                }
            }
        }

        int[] outPixels = Arrays.copyOf(pixels, pixels.length);
        Effects2.internalBoxBlur_withBlurMap(1, outPixels, mapPixels, width, height);
        return outPixels;
    }

    public static Bitmap noiseReduction2(Bitmap bitmap, int kernelSide) {
        int[] pixels = getPixels(bitmap);
//        pixels = boxFiltering(pixels, bitmap.getWidth(), bitmap.getHeight(), BOX_BLUR_KERNEL);
//        getNegativePixels(pixels);
        pixels = noiseReduction2(pixels, bitmap.getWidth(), bitmap.getHeight(), kernelSide);
        return createAndSetPixels(pixels, bitmap.getWidth(), bitmap.getHeight());
    }

    @NonNull
    public static int[] noiseReduction2(@NonNull int[] pixels, int width, int height, int kernelSide) {
        int[] outPixels = new int[pixels.length];
        int[] indices = indexCreator(kernelSide, width);
        int edgeWidth = kernelSide / 2;

        float alpha, red, green, blue, luminance;
        int ialpha, ired, igreen, iblue, indexOffset, rgb, rbgMain, base;
        int count = (kernelSide * kernelSide);
        for (int i = edgeWidth; i < height - (edgeWidth); i++) {
            for (int j = edgeWidth; j < width - (edgeWidth); j++) {
                luminance = red = green = blue = 0;
                indexOffset = (i * width) + j;
                rbgMain = pixels[indexOffset];
                for (int k = 0; k < indices.length; k++) {
//                    if (indices[k] != 0) {
                    rgb = pixels[indexOffset + indices[k]];
                    luminance += getMonochromePercentage(rgb);
//                    }
                }

                luminance = luminance / count;
//                base = (int) (luminance * (((rbgMain & 0xff0000) >> 16) + ((rbgMain & 0xff00) >> 8) + (rbgMain & 0xff)));
                base = (int) (luminance * 255);
//                base *= 3;

//                double[] colorParts = JavaUtils.bringTo_1_Range((rbgMain & 0xff0000) >> 16, (rbgMain & 0xff00) >> 8, (rbgMain & 0xff));

//                ired = (int) (colorParts[0] * base);
//                igreen = (int) (colorParts[1] * base);
//                iblue = (int) (colorParts[2] * base);

                ired = (rbgMain & 0xff0000) >> 16;
                igreen = (rbgMain & 0xff00) >> 8;
                iblue = (rbgMain & 0xff);

//                int cBase = ired + igreen + iblue;

                int biggest = Math.max(ired, Math.max(igreen, iblue));
                float f = ((float) base) / biggest;
                ired *= f;
                igreen *= f;
                iblue *= f;
//

                outPixels[indexOffset] = (0xff << 24) | ((ired << 16) & 0xff0000) | ((igreen << 8) & 0xff00) | (iblue & 0xff);
            }
        }

        return outPixels;
    }


    public static float calculateDenominator(@NonNull float[] kernel) {
        float denominator = 0;
        for (float v : kernel) {
            denominator += v;
        }
        if (denominator == 0.0f || (Math.abs(denominator) < 0.01)) denominator = 1.0f;
        return denominator;
    }

    public static int checkKernel(float[] kernel) {
        float side = (float) Math.sqrt(kernel.length);
        if (side % 1 != 0 || side % 2 == 0 || side < 3) {
            throw new RuntimeException("Value is inappropriate.");
        }
        return (int) side;
    }

    public static int[] indexCreator(int side, int bitWidth) {
        if (side % 2 == 0 || side < 3) {
            throw new RuntimeException("Value is inappropriate.");
        }
        int[] indices = new int[side * side];
        int center = side / 2;
        int count = 0;
        for (int h = 0; h < side; h++) {
            for (int w = 0; w < side; w++) {
                indices[count] = ((h - center) * bitWidth) + (w - center);
                count++;
            }
        }
        return indices;
    }

    //-----------------------------------------------------------------------//

    private static class CalculatedPixel {
        private int r, b, g;
        private boolean baked = false;

        private void setColor(int color) {
            r = Color.red(color);
            b = Color.blue(color);
            g = Color.green(color);
            baked = true;
        }

        public int getR() {
            return r;
        }

        public int getB() {
            return b;
        }

        public int getG() {
            return g;
        }

        public boolean isBaked() {
            return baked;
        }
    }

    public abstract static class LegoPlacer {
        private float cellWidth;
        private LegoProvider legoProvider;

        public LegoPlacer(float cellWidth, LegoProvider legoProvider) {
            this.cellWidth = cellWidth;
            this.legoProvider = legoProvider;
        }

        public float getCellWidth() {
            return cellWidth;
        }

        public LegoProvider getLegoProvider() {
            return legoProvider;
        }

        public abstract void draw(Canvas canvas);
    }

    public abstract static class LegoProvider {
        private float cellWidth;

        public float getCellWidth() {
            return cellWidth;
        }

        public void setCellWidth(float cellWidth) {
            this.cellWidth = cellWidth;
        }

        public abstract LegoPlacer generateLegoPlacer();
    }

    public static class Blender {
        private GradientDrawable.Orientation orientation;
        private int gradientType;
        private GradientDrawable gradientDrawable;
        private Bitmap mainBitmap, secondaryBitmap;
        private int color;
        private int type;
        private boolean colorCalculated = false;

        public final static int TYPE_BITMAP = 0;
        public final static int TYPE_COLOR = 1;
        public final static int TYPE_COLOR_AVERAGE = 3;
        public final static int TYPE_BLUR = 2;

        public Blender(GradientDrawable.Orientation orientation, int gradientType, Bitmap mainBitmap, @Nullable Bitmap secondaryBitmap, int color, int type) {
            this.orientation = orientation;
            this.gradientType = gradientType;
            this.mainBitmap = mainBitmap;
            this.secondaryBitmap = secondaryBitmap;
            this.color = color;
            this.type = type;
        }

        public GradientDrawable.Orientation getOrientation() {
            return orientation;
        }

        public int getGradientType() {
            return gradientType;
        }

        public GradientDrawable getGradientDrawable() {
            if (gradientDrawable == null) generateGradientDrawable();
            return gradientDrawable;
        }

        public Bitmap getMainBitmap() {
            return mainBitmap;
        }

        public Bitmap getSecondaryBitmap() {
            return secondaryBitmap;
        }

        public int getColor() {
            if (type == TYPE_COLOR_AVERAGE && !colorCalculated) {
                Palette palette = Palette.from(mainBitmap).generate();
                color = palette.getVibrantColor(Color.WHITE);
                colorCalculated = true;
            }
            return color;
        }

        public int getType() {
            return type;
        }

        public void setGradientDrawable(GradientDrawable gradientDrawable) {
            this.gradientDrawable = gradientDrawable;
        }

        public int getMainWidth() {
            return mainBitmap.getWidth();
        }

        public int getMainHeight() {
            return mainBitmap.getHeight();
        }

        public void generateGradientDrawable() {
            GradientDrawable drawable = new GradientDrawable(getOrientation(), new int[]{Color.WHITE, Color.WHITE, MbnUtils.alphaChanger(Color.WHITE, (255) / 3), MbnUtils.alphaChanger(Color.WHITE, 0)});
            if (type == TYPE_COLOR || type == TYPE_COLOR_AVERAGE) {
                drawable.setColors(new int[]{getColor(), getColor(), MbnUtils.alphaChanger(getColor(), (255) / 3), MbnUtils.alphaChanger(getColor(), 0)});
            }
            drawable.setGradientType(getGradientType());
            drawable.setBounds(0, 0, getMainWidth(), getMainHeight());
            if (gradientType == GradientDrawable.RADIAL_GRADIENT) {
                drawable.setGradientRadius(Math.min(getMainHeight(), getMainWidth()));
            }
            gradientDrawable = drawable;
        }

        protected Bitmap getGradientBitmap() {
            Bitmap gradientBitmap = Bitmap.createBitmap(getMainWidth(), getMainHeight(), Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(gradientBitmap);
            getGradientDrawable().draw(canvas);
            if (type == TYPE_COLOR || type == TYPE_COLOR_AVERAGE) {
                return gradientBitmap;
            }
            if (type == TYPE_BLUR) {
                secondaryBitmap = MbnUtils.stackBlur(MbnUtils.createSmallBit(mainBitmap, 400), 30, true);
            }
            Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
            cropToSize(gradientBitmap, secondaryBitmap, paint);
            return gradientBitmap;
        }

        public Bitmap blend() {
            Bitmap out = Bitmap.createBitmap(getMainWidth(), getMainHeight(), Bitmap.Config.ARGB_8888);
            cropToSize(out, mainBitmap, null);
            cropToSize(out, getGradientBitmap(), null);
            return out;
        }

    }

    public static class CharLegoProvider extends LegoProvider {
        private Random random = new Random();
        private String[] chars = new String[]{"Q", "W", "E", "R", "T", "Y", "U", "O", "P", "#", "$", "%", "+", "A", "*",
                "S", "D", "F", "G", "H", "J", "K", "L", "Z", "X", "C", "V",
                "B", "N", "M", "2", "3", "4", "5", "6", "7", "8", "9"};

        private Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        private Rect rect = new Rect();

        public CharLegoProvider() {
            paint.setStyle(Paint.Style.FILL);
            paint.setColor(Color.BLACK);
        }

        @Override
        public void setCellWidth(float cellWidth) {
            super.setCellWidth(cellWidth);
//            paint.setTextSize(cellWidth * 1.15f);
            paint.setTypeface(Typeface.DEFAULT_BOLD);
        }

        public Paint getPaint() {
            return paint;
        }

        public Rect getRect() {
            return rect;
        }

        String getRandomChar() {
            return chars[random.nextInt(chars.length)];
        }

        int getRandomAngle() {
            return 10 * random.nextInt(37);
        }

        void setRandomSize() {
            paint.setTextSize((getCellWidth()) * (random.nextFloat() + 1.1f));
        }

        @Override
        public LegoPlacer generateLegoPlacer() {
            return new CharLegoPlacer(getCellWidth(), this);
        }
    }

    public static class CharLegoPlacer extends LegoPlacer {

        CharLegoPlacer(float cellWidth, LegoProvider legoProvider) {
            super(cellWidth, legoProvider);
        }

        @Override
        public void draw(Canvas canvas) {
            canvas.save();
            CharLegoProvider charLegoProvider = (CharLegoProvider) getLegoProvider();
            canvas.rotate(charLegoProvider.getRandomAngle(), getCellWidth() / 2, getCellWidth() / 2);
            charLegoProvider.setRandomSize();

            Paint paint = charLegoProvider.getPaint();
            String chr = charLegoProvider.getRandomChar();
            Rect textSize = charLegoProvider.getRect();
            float[] width = new float[chr.length()];

            paint.getTextBounds(chr, 0, 1, textSize);
            paint.getTextWidths(chr, width);
            canvas.drawText(chr, (getCellWidth() - width[0]) / 2, ((getCellWidth() - textSize.height()) / 2) + textSize.height(), paint);

            canvas.restore();
        }
    }

    public static class IconLegoProvider extends LegoProvider {
        private Random random = new Random();
        private int[] iconIDs;
        private Bitmap[] iconBitmaps;
        private Context context;
        private RectF rect = new RectF();

        public IconLegoProvider(int[] iconIDs, Context context) {
            this.iconIDs = iconIDs;
            this.context = context;
            iconBitmaps = new Bitmap[iconIDs.length];
        }

        @SuppressWarnings("SuspiciousNameCombination")
        @Override
        public void setCellWidth(float cellWidth) {
            super.setCellWidth(cellWidth);
            rect.set(0, 0, cellWidth, cellWidth);
        }

        private void loadBitmapIfNecessary(int number) {
            if (iconBitmaps[number] == null)
                iconBitmaps[number] = BitmapFactory.decodeResource(context.getResources(), iconIDs[number]);
        }

        Bitmap getRandomBitmap() {
            int num = random.nextInt(iconIDs.length);
            loadBitmapIfNecessary(num);
            return iconBitmaps[num];
        }

        int getRandomAngle() {
            return 10 * random.nextInt(37);
        }

        float getRandomSize() {
//            return 2 - random.nextFloat();
//            return 1 + (-0.5f + random.nextFloat());
            return 1 + (0.5f * random.nextFloat());
        }

        public RectF getRect() {
            return rect;
        }

        @Override
        public LegoPlacer generateLegoPlacer() {
            return new IconLegoPlacer(getCellWidth(), this);
        }
    }

    public static class IconLegoPlacer extends LegoPlacer {


        IconLegoPlacer(float cellWidth, LegoProvider legoProvider) {
            super(cellWidth, legoProvider);
        }

        @Override
        public void draw(Canvas canvas) {
            canvas.save();
            IconLegoProvider iconLegoProvider = (IconLegoProvider) getLegoProvider();
            canvas.rotate(iconLegoProvider.getRandomAngle(), getCellWidth() / 2, getCellWidth() / 2);
            float scale = iconLegoProvider.getRandomSize();
            canvas.scale(scale, scale, getCellWidth() / 2, getCellWidth() / 2);

            canvas.drawBitmap(iconLegoProvider.getRandomBitmap(), null, iconLegoProvider.getRect(), null);

            canvas.restore();
        }
    }

    //----------------------View-related-methods--------------------//


    @SuppressWarnings("unchecked")
    public static void drawViewTree(View fromView, View toView, @FloatRange(from = 0f, to = 1f) float scale, boolean blur, int blurRadios, Object infoKey, BaseTaskHolder.ResultReceiver resultReceiver) {
        if (Thread.currentThread() != Looper.getMainLooper().getThread()) {
            throw new RuntimeException("Should be called from main thread.");
        }
        Drawable viewBackground = toView.getBackground();
        ErrorThrowDrawable errorThrowDrawable = new ErrorThrowDrawable(new StopDrawException());
        toView.setBackground(errorThrowDrawable);

        Bitmap out = Bitmap.createBitmap((int) (fromView.getWidth() * scale), (int) (fromView.getHeight() * scale), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(out);
        canvas.scale(scale, scale, 0, 0);

        try {
            errorThrowDrawable.shouldThrowError(true);
            fromView.draw(canvas);
        } catch (StopDrawException ignored) {
        } finally {
            errorThrowDrawable.shouldThrowError(false);
            toView.setBackground(viewBackground);
        }

        if (blur) {
            ThreadManager.getAppGlobalTask_MultiThread().StartTask(new MakeBlur(out, infoKey, blurRadios), resultReceiver);
        } else {
            resultReceiver.onResult(out, infoKey);
        }

    }

    private static class StopDrawException extends RuntimeException {
    }

    private static class ErrorThrowDrawable extends Drawable {

        private boolean shouldThrowError = false;
        private RuntimeException thrownError;

        ErrorThrowDrawable(RuntimeException thrownError) {
            this.thrownError = thrownError;
        }

        void shouldThrowError(boolean shouldThrowError) {
            this.shouldThrowError = shouldThrowError;
        }

        @Override
        public void draw(@NonNull Canvas canvas) {
            if (shouldThrowError) throw thrownError;
        }

        @Override
        public void setAlpha(int alpha) {

        }

        @Override
        public void setColorFilter(@Nullable ColorFilter colorFilter) {

        }

        @Override
        public int getOpacity() {
            return 0;
        }
    }

    private static class MakeBlur implements BaseTaskHolder.BaseTask {
        private Bitmap bitmap;
        private Object info;
        private int radios;

        public MakeBlur(Bitmap bitmap, Object info, int radios) {
            this.bitmap = bitmap;
            this.info = info;
            this.radios = radios;
        }

        @Override
        public Object onRun() {
            return MbnUtils.stackBlur(bitmap, radios, true);
        }

        @Override
        public Object getInfo() {
            return info;
        }
    }
}
