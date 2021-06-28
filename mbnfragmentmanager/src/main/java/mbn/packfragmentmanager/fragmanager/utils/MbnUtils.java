package mbn.packfragmentmanager.fragmanager.utils;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;

public class MbnUtils {


    public static Bitmap roundedBitmap(Bitmap bitmap) {

        final int rawWidth = bitmap.getWidth();
        final int rawHeight = bitmap.getHeight();


        final int cropSize = Math.min(rawWidth, rawHeight);
        final int difference = Math.abs(rawWidth - rawHeight);

        Bitmap cropBitmap = Bitmap.createBitmap(cropSize, cropSize, Bitmap.Config.ARGB_8888);

        Canvas cropCanvas = new Canvas(cropBitmap);

        final Path cropCircle = new Path();

        cropCircle.addCircle(cropSize / 2, cropSize / 2, cropSize / 2, Path.Direction.CCW);

        Paint mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(Color.WHITE);

        cropCanvas.drawPath(cropCircle, mPaint);

        mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));

        if (rawWidth == cropSize) {
            cropCanvas.drawBitmap(bitmap, 0, -(difference / 2), mPaint);
        } else {
            cropCanvas.drawBitmap(bitmap, -(difference / 2), 0, mPaint);
        }

        bitmap.recycle();
        return cropBitmap;
    }

    public static Bitmap roundedBitmapWithChin(Bitmap bitmap) {

        final int rawWidth = bitmap.getWidth();
        final int rawHeight = bitmap.getHeight();
        final int width;
        final int height;

        final int cropSize = Math.min(rawWidth, rawHeight);
        final int difference = Math.abs(rawWidth - rawHeight);

        width = cropSize;
        height = cropSize;

        Bitmap cropBitmap = Bitmap.createBitmap(cropSize, cropSize, Bitmap.Config.ARGB_8888);

        Canvas cropCanvas = new Canvas(cropBitmap);

        final Path cropCircle = new Path();

        cropCircle.addArc(0, 0, cropSize, cropSize, -320, -260);

        cropCircle.close();

        cropCanvas.clipPath(cropCircle);

        if (rawWidth == cropSize) {

            cropCanvas.drawBitmap(bitmap, 0, -(difference / 2), null);

        } else {

            cropCanvas.drawBitmap(bitmap, -(difference / 2), 0, null);


        }


        return cropBitmap;
    }

    public static Bitmap getGreyBackground(Bitmap input) {

        Bitmap out = Bitmap.createBitmap(200, 200, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(out);

        canvas.drawColor(Color.argb(255, 230, 230, 230));

//        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
//
//        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_OUT));

        canvas.drawBitmap(tintChanger(input), null, new Rect(30, 30, 170, 170), null);

        input.recycle();
        return roundedBitmap(out);
    }

    public static Bitmap addOverLay(int color, int alpha, Bitmap bitmap, boolean useBitmap) {
        Bitmap output;
        if (useBitmap) {
            output = bitmap;
        } else {
            output = bitmap.copy(Bitmap.Config.ARGB_8888, true);
        }
        Canvas canvas = new Canvas(output);
        canvas.drawColor(alphaChanger(color, alpha));
        return output;
    }

    public static Bitmap renderScriptBlur(Context context, Bitmap bitmap, int radius, boolean useTheOriginalBitmap) {
        RenderScript rs = null;
        Allocation input = null;
        Allocation output = null;
        ScriptIntrinsicBlur blur = null;
        Bitmap outBit;
        try {
            rs = RenderScript.create(context);
            rs.setMessageHandler(new RenderScript.RSMessageHandler());
            input = Allocation.createFromBitmap(rs, bitmap, Allocation.MipmapControl.MIPMAP_NONE,
                    Allocation.USAGE_SCRIPT);
            output = Allocation.createTyped(rs, input.getType());
            blur = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs));

            blur.setInput(input);
            blur.setRadius(radius);
            blur.forEach(output);
            if (!useTheOriginalBitmap) {
                outBit = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
                output.copyTo(outBit);
            } else {
                output.copyTo(bitmap);
                outBit = bitmap;
            }
        } catch (Exception e) {
            if (!useTheOriginalBitmap)
                outBit = stackBlur(bitmap, radius, false);
            else outBit = stackBlur(bitmap, radius, true);
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

    public static Bitmap stackBlur(Bitmap sentBitmap, int radius, boolean canReuseInBitmap) {

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

    public static Bitmap createSmallBit(Bitmap bitmap, float size) {
        float bigOne = size / Math.max(bitmap.getWidth(), bitmap.getHeight());
        int width = (int) (bitmap.getWidth() * bigOne);
        int height = (int) (bitmap.getHeight() * bigOne);

//        int width = bitmap.getWidth();
//        int height = bitmap.getHeight();

        Bitmap bity = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas cany = new Canvas(bity);
        cany.drawBitmap(bitmap, null, new Rect(0, 0, width, height), null);
        return bity;
    }

    public static Bitmap createSmallBit_Empty(Bitmap bitmap, float size) {
        float bigOne = size / Math.max(bitmap.getWidth(), bitmap.getHeight());
        int width = (int) (bitmap.getWidth() * bigOne);
        int height = (int) (bitmap.getHeight() * bigOne);
        return Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
    }

    public static Bitmap tintChanger(Bitmap bitmap) {

//        Bitmap out = bitmap.copy(Bitmap.Config.ARGB_8888, true);
        Canvas canvas = new Canvas(bitmap);

//        Paint paint = new Paint();
//        paint.setAntiAlias(true);
//        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_OUT));

        canvas.drawColor(Color.GRAY, PorterDuff.Mode.SRC_IN);


        return bitmap;
    }

    public static int colorWhitener(int color) {
        int inR, inB, inG, outR, outB, outG;

        inR = Color.red(color);
        inB = Color.blue(color);
        inG = Color.green(color);
        int max = Math.max(inR, Math.max(inB, inG));

        outR = (int) (255 - (Math.ceil((max - inR) / 2)));
        outB = (int) (255 - (Math.ceil((max - inB) / 2)));
        outG = (int) (255 - (Math.ceil((max - inG) / 2)));

        return Color.rgb(outR, outG, outB);
    }

    public static int colorWhitener2(int color, int factor) {
        int inR, inB, inG, outR, outB, outG;

        inR = Color.red(color);
        inB = Color.blue(color);
        inG = Color.green(color);

        int whitePortion = factor * 255;

        outR = (inR + whitePortion) / (factor + 1);
        outG = (inG + whitePortion) / (factor + 1);
        outB = (inB + whitePortion) / (factor + 1);

        return Color.rgb(outR, outG, outB);
    }

    public static int alphaChanger(int color, int alpha) {
        return Color.argb(alpha, Color.red(color), Color.green(color), Color.blue(color));
    }

}
