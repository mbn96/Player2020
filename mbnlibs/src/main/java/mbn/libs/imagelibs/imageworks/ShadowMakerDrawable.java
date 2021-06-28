package mbn.libs.imagelibs.imageworks;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PixelFormat;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class ShadowMakerDrawable extends Drawable {

    private Context context;
    private int shadowAngle = 45;
    private float displayDensity;
    private int shadowLength;
    private int[] colors = new int[3];
    private int color = Color.GRAY;
    private int colorMiddle = MbnUtils.alphaChanger(color, 255 / 5);
    private int colorEnd = MbnUtils.alphaChanger(color, 0);
    protected int currentWidth, currentHeight;
    private float shadowWidth, shadowHeight;
    private Path path = new Path();
    private Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);

    protected int[] edges;
    protected float scale = 2;


    public ShadowMakerDrawable(int shadowAngle, int shadowLength, int color) {
        this.shadowAngle = shadowAngle;
        this.shadowLength = shadowLength;
        setColor(color);
        calculateSpecs();
    }

    public ShadowMakerDrawable(@NonNull Context context) {
        this.context = context;
        displayDensity = context.getResources().getDisplayMetrics().density;
        shadowLength = (int) (10 * displayDensity);
        calculateSpecs();
    }

    public ShadowMakerDrawable(Context context, int shadowAngle, int shadowLength, int color) {
        this.context = context;
        this.shadowAngle = shadowAngle;
        this.shadowLength = shadowLength;
        setColor(color);
        colorMiddle = MbnUtils.alphaChanger(color, 255 / 5);
        colorEnd = MbnUtils.alphaChanger(color, 0);
        displayDensity = context.getResources().getDisplayMetrics().density;
        calculateSpecs();
    }

    public void setShadowAngle(int shadowAngle) {
        this.shadowAngle = shadowAngle;
        calculateSpecs();
    }

    public int getShadowAngle() {
        return shadowAngle;
    }

    public int getShadowLength() {
        return shadowLength;
    }

    public void setScale(float scale) {
        this.scale = scale;
        calculateSpecs();
    }

    public void setShadowLength(int shadowLength) {
        this.shadowLength = shadowLength;
        calculateSpecs();
    }

    public void setColor(int color) {
        this.color = color;
        colorMiddle = MbnUtils.alphaChanger(color, 255 / 15);
        colorEnd = MbnUtils.alphaChanger(color, 0);
        calculateSpecs();
    }

    public int getColor() {
        return color;
    }

    private void calculateSpecs() {
        colors[0] = color;
        colors[1] = colorMiddle;
        colors[2] = colorEnd;
        double radiant = Math.toRadians(shadowAngle);
        shadowWidth = (float) (Math.cos(radiant) * shadowLength);
        shadowHeight = (float) (Math.sin(radiant) * shadowLength);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(scale);
        paint.setColor(color);
    }

    public void setBitmap(Bitmap bitmap) {
        Bitmap small = MbnUtils.createSmallBit(bitmap, Math.max(bitmap.getWidth(), bitmap.getHeight()) / scale);
        currentWidth = small.getWidth();
        currentHeight = small.getHeight();
        edges = Effects.getPixels(small);
//        edges = Effects.boxFiltering_withAlpha(edges, currentWidth, currentHeight, Effects.BOX_BLUR_KERNEL);
//        edges = Effects.boxFiltering_withAlpha(Effects.getPixels(bitmap), bitmap.getWidth(), bitmap.getHeight(), Effects.EDGE_DETECT_KERNEL);
//        preparePath(edges, bitmap.getWidth(), bitmap.getHeight());
    }

//    private void preparePath(int[] edges, int width, int height) {
//        path.rewind();
//        for (int h = 0; h < height; h++) {
//            for (int w = 0; w < width; w++) {
//                int pixel = edges[(h * width) + w];
//                if ((pixel >>> 24) > 0) {
//                    path.moveTo(w, h);
//                    path.lineTo(w + shadowWidth, h + shadowHeight);
//                }
//            }
//        }
//    }

    private void drawShadow(Canvas canvas) {
        path.rewind();
        for (int h = 0; h < currentHeight; h++) {
            for (int w = 0; w < currentWidth; w++) {
                int pixel = edges[(h * currentWidth) + w];
                if ((pixel >>> 24) > 200) {
                    paint.setShader(new LinearGradient(w, h, w + shadowWidth, h + shadowHeight, colors, null, Shader.TileMode.CLAMP));
                    canvas.drawLine(w, h, w + shadowWidth, h + shadowHeight, paint);
                }
            }
        }
    }

    @Override
    public void draw(@NonNull Canvas canvas) {
        if (edges != null) {
            canvas.save();
            canvas.scale(scale, scale, 0, 0);
            drawShadow(canvas);
            canvas.restore();
        }
    }

    @Override
    public void setAlpha(int alpha) {
        // TODO: 2/2/2020 implement if needed.
    }

    @Override
    public void setColorFilter(@Nullable ColorFilter colorFilter) {
        // TODO: 2/2/2020 if needed.
    }

    @Override
    public int getOpacity() {
        return PixelFormat.TRANSLUCENT;
    }
}
