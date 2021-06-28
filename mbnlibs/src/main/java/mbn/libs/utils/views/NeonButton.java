package mbn.libs.utils.views;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Outline;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PixelFormat;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.view.MotionEvent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.Arrays;

import mbn.libs.R;

public class NeonButton extends androidx.appcompat.widget.AppCompatButton {

    private NeonDrawable background;
    private Style style = Style.Light;
    private float density;

    public NeonButton(Context context) {
        super(context);
        init(null);
    }

    public NeonButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public NeonButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    private void init(@Nullable AttributeSet attributeSet) {
        background = new NeonDrawable(density = getResources().getDisplayMetrics().density);
        background.setStyle(style);
        setBackground(background);
        setStyle(style);
        setAllCaps(false);

        if (attributeSet != null) {
            TypedArray typedArray = getContext().getTheme().obtainStyledAttributes(attributeSet, R.styleable.CustomViews, 0, 0);
            try {
                setStyle(typedArray.getInteger(R.styleable.CustomViews_style, 0) == 0 ? Style.Light : Style.Dark);
                setBackgroundTintList(ColorStateList.valueOf(typedArray.getColor(R.styleable.CustomViews_rippleColor, 0)));
                setAccentColor(ColorStateList.valueOf(typedArray.getColor(R.styleable.CustomViews_accentColor, 0)));
            } finally {
                typedArray.recycle();
            }
        }
    }

    public void setAccentColor(ColorStateList color) {
        setAccentColor(color.getDefaultColor());
    }

    public void setAccentColor(int color) {
        background.setAccentColor(color);
    }

    public Style getStyle() {
        return style;
    }

    public void setStyle(Style style) {
        this.style = style;
        switch (style) {
            case Light:
                int[][] states = new int[][]{
                        new int[]{android.R.attr.state_pressed},  // pressed
                        new int[]{android.R.attr.state_selected},  // selected
                        new int[]{android.R.attr.state_enabled}, // enabled
                        new int[]{-android.R.attr.state_enabled} // disabled
                };

                int[] colors = new int[]{
                        Color.WHITE,
                        Color.WHITE,
                        Color.BLACK,
                        Color.DKGRAY
                };
                ColorStateList colorStateList = new ColorStateList(states, colors);

                setTextColor(colorStateList);
                background.setStyle(style);
                break;
            case Dark:
                int[][] states_d = new int[][]{
                        new int[]{android.R.attr.state_pressed},  // pressed
                        new int[]{android.R.attr.state_selected},  // selected
                        new int[]{android.R.attr.state_enabled}, // enabled
                        new int[]{-android.R.attr.state_enabled} // disabled
                };

                int[] colors_d = new int[]{
                        Color.BLACK,
                        Color.BLACK,
                        Color.WHITE,
                        Color.DKGRAY
                };
                ColorStateList colorStateList_d = new ColorStateList(states_d, colors_d);
                setTextColor(colorStateList_d);
                background.setStyle(style);
                break;
        }
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {

        if (event.getActionMasked() == MotionEvent.ACTION_DOWN) {
            background.setRippleC(event.getX(), event.getY());
        }

        return super.onTouchEvent(event);
    }

    public enum Style {
        Light, Dark
    }

    public static class NeonDrawable extends Drawable {

        private int alpha = 255;
        private int tintColor = 0;
        private int useColor;
        private int accentColor = 0xff3040aa;
        private Style style;
        private int finalStateL = 0;
        private float currentStateL = finalStateL;
        private int changeStateTime = 200;
        private float changePerMilli = 1f / changeStateTime;
        private Path path = new Path();
//        private Path path_clipOut = new Path();
        private Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        private float density;
        private long currentMillis;
        private BlurMaskFilter maskFilter;
        private BlurMaskFilter maskFilter_ripple;
        private PointF rippleC = new PointF();
        private float rippleRadios;
        private boolean isEnable;
        private boolean isSelected;
        private boolean canSupportBlurMask;

        public NeonDrawable(float density) {
            paint.setStrokeWidth(1.5f * density);
            this.density = density;

            if (canSupportBlurMask = Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            maskFilter = new BlurMaskFilter(5.5f * density, BlurMaskFilter.Blur.OUTER);
            maskFilter_ripple = new BlurMaskFilter(10f * density, BlurMaskFilter.Blur.NORMAL);
            }
        }

        @Override
        public void draw(@NonNull Canvas canvas) {
            if (isEnable) {
                if (canSupportBlurMask) {
                paint.setStyle(Paint.Style.FILL);
                paint.setMaskFilter(maskFilter);
                paint.setAlpha(255);
                paint.setColor(accentColor);
                canvas.save();
//                canvas.clipPath(path_clipOut);
                canvas.drawPath(path, paint);
//                canvas.drawRect(0,0,canvas.getWidth(),canvas.getHeight(),paint);
                canvas.restore();
                paint.setMaskFilter(null);
                paint.setColor(useColor);
                }

                if (isSelected) {
                    paint.setMaskFilter(null);
                    paint.setStyle(Paint.Style.FILL);
                    paint.setAlpha(255);
                    canvas.drawPath(path, paint);
                } else if (currentStateL != finalStateL) {
                    long timeChange = System.currentTimeMillis() - currentMillis;
                    float changeValue = timeChange * changePerMilli;
                    changeValue *= currentStateL > finalStateL ? -1 : 1;
                    currentStateL += changeValue;
                    if (changeValue <= 0) {
                        currentStateL = Math.max(currentStateL, finalStateL);
                    } else {
                        currentStateL = Math.min(currentStateL, finalStateL);
                    }

                    paint.setStyle(Paint.Style.FILL);
//                    if (canSupportBlurMask) {
                    paint.setMaskFilter(maskFilter_ripple);
//                    }
                    canvas.save();
                    canvas.clipPath(path);
//                paint.setAlpha((int) (currentStateL * 255));
//                canvas.drawPath(path, paint);
                    canvas.drawCircle(rippleC.x, rippleC.y, rippleRadios * currentStateL, paint);
                    canvas.restore();
                    paint.setMaskFilter(null);
                    if (currentStateL != finalStateL) {
                        currentMillis = System.currentTimeMillis();
                        invalidateSelf();
                    }
                }

                paint.setMaskFilter(null);
                paint.setStyle(Paint.Style.STROKE);
                paint.setAlpha(255);
                canvas.drawPath(path, paint);
            } else {
                paint.setMaskFilter(null);
                paint.setStyle(Paint.Style.FILL);
                paint.setColor(Color.GRAY);
                paint.setAlpha(255);
                canvas.drawPath(path, paint);
            }
        }

        public void setStyle(Style style) {
            this.style = style;
            if (tintColor == 0) {
                useColor = style == Style.Light ? Color.DKGRAY : Color.WHITE;
                paint.setColor(useColor);
            } else {
                paint.setColor(tintColor);
            }
            invalidateSelf();
        }

        @Override
        protected boolean onStateChange(int[] state) {
//            [16842909, 16842910, 16842919, 16843547, 16843597]
//             [16842909, 16842910, 16843547, 16843597]
            isSelected = false;
            if (Arrays.binarySearch(state, android.R.attr.state_enabled) < 0) {
                isEnable = false;
            } else if (Arrays.binarySearch(state, android.R.attr.state_pressed) >= 0) {
                finalStateL = 1;
                isEnable = true;
            } else {
                finalStateL = 0;
                isEnable = true;
            }
            if (Arrays.binarySearch(state, android.R.attr.state_selected) >= 0) {
                isEnable = true;
                isSelected = true;
            }
            currentMillis = System.currentTimeMillis();
            invalidateSelf();
            return true;
        }

        @Override
        public void setTintList(@Nullable ColorStateList tint) {
            if (tint != null && tint.getDefaultColor() != 0) {
                tintColor = tint.getDefaultColor();
                useColor = tintColor;
                paint.setColor(tintColor);
            }
        }

        @Override
        public boolean getPadding(@NonNull Rect padding) {
            padding.set((int) (22 * density), (int) (18 * density), (int) (22 * density), (int) (18 * density));
            return true;
        }

        public void setAccentColor(int accentColor) {
            if (accentColor != 0) {
                this.accentColor = accentColor;
            }
            invalidateSelf();
        }

        public void setRippleC(float x, float y) {
            this.rippleC.set(x, y);
        }

        @Override
        public void getOutline(@NonNull Outline outline) {
            super.getOutline(outline);
        }

        @Override
        protected void onBoundsChange(Rect bounds) {
            path.rewind();
//            path_clipOut.rewind();
            float margin = 8 * density;
//            float clipMargin = (paint.getStrokeWidth()) + margin;
//            float clipMargin = 0;
            path.addRoundRect(new RectF(bounds.left + margin, bounds.top + margin, bounds.right - margin, bounds.bottom - margin),
                    25 * density, 25 * density, Path.Direction.CW);

//            path_clipOut.addRoundRect(new RectF(bounds.left + clipMargin, bounds.top + clipMargin, bounds.right - clipMargin, bounds.bottom - clipMargin),
//                    25 * density, 25 * density, Path.Direction.CW);
//            path_clipOut.op(path_clipOut, Path.Op.XOR);
            rippleRadios = Math.max(bounds.width(), bounds.height());
            super.onBoundsChange(bounds);
        }

        @Override
        public void setAlpha(int alpha) {
            this.alpha = alpha;
        }

        @Override
        public int getAlpha() {
            return alpha;
        }

        @Override
        public void setColorFilter(@Nullable ColorFilter colorFilter) {
        }

        @Override
        public boolean isStateful() {
            return true;
        }

        @Override
        public int getOpacity() {
            return PixelFormat.OPAQUE;
        }
    }
}
