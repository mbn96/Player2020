package mbn.packfragmentmanager.fragmanager.views;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.RectF;
import androidx.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import mbn.packfragmentmanager.fragmanager.backgroundtask.BaseTaskHolder;
import mbn.packfragmentmanager.fragmanager.backgroundtask.ThreadManager;
import mbn.packfragmentmanager.fragmanager.utils.MbnUtils;


public class CropChangerImageView extends View {

    private Bitmap bitmap;
    private String currentPath;
    private float fraction = 0;
    private boolean shouldResize = true;
    private int imageBigSide = 500;
    private RectF rect = new RectF();
    private Path cropPath = new Path();
    private boolean makeCropCircular = false;


    private BaseTaskHolder.ResultReceiver bitmapReceiver = new BaseTaskHolder.ResultReceiver() {
        @Override
        public void onResult(Object result, Object info) {
            if (currentPath != null && currentPath.equals(info))
                setBitmapInternal((Bitmap) result);
        }
    };

    public CropChangerImageView(Context context) {
        super(context);
    }

    public CropChangerImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public CropChangerImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public CropChangerImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public void setFraction(float fraction) {
        this.fraction = fraction;
        invalidate();
    }

    public void setShouldResize(boolean shouldResize) {
        this.shouldResize = shouldResize;
    }

    public void setImageBigSide(int imageBigSide) {
        this.imageBigSide = imageBigSide;
    }

    public void setBitmap(Bitmap bitmap) {
        currentPath = null;
        if (!shouldResize) {
            setBitmapInternal(bitmap);
        } else {
            setBitmapInternal(MbnUtils.createSmallBit(bitmap, imageBigSide));
        }
    }

    public void setBitmap(String path) {
        currentPath = path;
        ThreadManager.getAppGlobalTask_MultiThread().StartTask(new Loader(path, shouldResize, imageBigSide), bitmapReceiver);
    }

    public void setMakeCropCircular(boolean makeCropCircular) {
        this.makeCropCircular = makeCropCircular;
        invalidate();
    }

    private void setBitmapInternal(Bitmap bitmapInternal) {
        bitmap = bitmapInternal;
        invalidate();
    }

    private void makeCropPath() {
        cropPath.rewind();
        cropPath.addRoundRect(0, 0, getWidth(), getHeight(), (getWidth() / 2) * fraction, (getWidth() / 2) * fraction, Path.Direction.CW);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (bitmap != null) {
            canvas.save();

            float cropFactor = Math.max(((float) getWidth()) / bitmap.getWidth(), ((float) getHeight()) / bitmap.getHeight());
            float fitFactor = Math.min(((float) getWidth()) / bitmap.getWidth(), ((float) getHeight()) / bitmap.getHeight());

            float cropFraction = fraction;
            float fitFraction = 1 - fraction;

            float useFactor = (cropFactor * cropFraction) + (fitFactor * fitFraction);

            int useW = (int) (useFactor * bitmap.getWidth());
            int useH = (int) (useFactor * bitmap.getHeight());

            int diffHalfWidth = (useW - getWidth()) / 2;
            int diffHalfHeight = (useH - getHeight()) / 2;

            rect.set(-diffHalfWidth, -diffHalfHeight, getWidth() + diffHalfWidth, getHeight() + diffHalfHeight);

            if (makeCropCircular) {
                cropPath.rewind();
                cropPath.addRoundRect(0, 0, getWidth(), getHeight(), (getWidth() / 2) * fraction, (getHeight() / 2) * fraction, Path.Direction.CW);
                canvas.clipPath(cropPath);
            }

            canvas.drawBitmap(bitmap, null, rect, null);

            canvas.restore();
        }
    }

    private static class Loader implements BaseTaskHolder.BaseTask {
        private String path;
        private boolean resize;
        private int maxSize;

        Loader(String path, boolean resize, int maxSize) {
            this.path = path;
            this.resize = resize;
            this.maxSize = maxSize;
        }

        @Override
        public Object onRun() {
            if (resize) {
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inJustDecodeBounds = true;
                BitmapFactory.decodeFile(path, options);
                options.inSampleSize = (int) (Math.max(options.outHeight, options.outWidth) / (float) maxSize);
                options.inJustDecodeBounds = false;
                return MbnUtils.createSmallBit(BitmapFactory.decodeFile(path, options), maxSize);
            } else {
                return BitmapFactory.decodeFile(path);
            }
        }

        @Override
        public Object getInfo() {
            return path;
        }
    }

}
