package mbn.libs.fragmanager;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Path;
import android.view.View;


public class BlurImageView extends androidx.appcompat.widget.AppCompatImageView {

//    private String TAG = "blur_view";

    //    private Rect cropZone;
    private Path clipPath = new Path();
    private boolean crop;

    private float density;

    public BlurImageView(Context context, boolean crop) {
        super(context);
        density = context.getResources().getDisplayMetrics().density;
        this.crop = crop;
    }

    public void setCropZone(View child) {
//        this.cropZone = cropZone;

        clipPath.rewind();
        clipPath.addRoundRect(child.getLeft() + child.getTranslationX(),
                child.getTop() + child.getTranslationY(),
                child.getRight() + child.getTranslationX(),
                child.getBottom() + child.getTranslationY(),
                20 * density, 20 * density, Path.Direction.CW);


//        clipPath.addRoundRect(child.getLeft(),
//                child.getTop(),
//                child.getRight(),
//                child.getBottom(),
//                20 * density, 20 * density, Path.Direction.CW);
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (crop) {
            canvas.save();
            canvas.clipPath(clipPath);
            super.onDraw(canvas);
            canvas.restore();
        } else {
            super.onDraw(canvas);
        }
    }
}
