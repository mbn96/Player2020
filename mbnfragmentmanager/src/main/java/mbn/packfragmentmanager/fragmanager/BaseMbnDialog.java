package mbn.packfragmentmanager.fragmanager;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import mbn.packfragmentmanager.fragmanager.utils.MbnUtils;

import static mbn.packfragmentmanager.fragmanager.CustomFragmentSwipeBackAnimator.ANIM_DIALOG_RTL;
import static mbn.packfragmentmanager.fragmanager.CustomFragmentSwipeBackAnimator.ANIM_DIALOG_TTB;


public abstract class BaseMbnDialog extends BaseFragment {

    private BlurImageView backBlur = null;
    //    private ImageView backBlur = null;
    private View darkener;

    private FrameLayout dialogBase;
    private View childDialog = null;

    private static final String CACHE_CODE = "cache_code";

    @Override
    public int getAnimationMode() {
        return ANIM_DIALOG_RTL;
    }

    @Override
    public boolean canHavePending() {
        return true;
    }

    @Override
    public View onCreateViewForChild(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return null;
    }

    public FrameLayout getDialogBase() {
        return dialogBase;
    }

    public View getChildDialog() {
        return childDialog;
    }

    public void addLayout(View view) {
        dialogBase.addView(view);
        childDialog = view;
    }

    @Override
    public void onFractionChange(float fraction) {
//        dialogBase.setBackgroundColor(Color.argb((int) ((255 / 2) * (1 - fraction)), 0, 0, 0));

        if (backBlur != null && !(this instanceof BasicMbnPopupMenu)) {
            backBlur.setAlpha(1 - fraction);
        }
        if (darkener != null) {
            darkener.setAlpha((1f / 3) * (1 - fraction));
        }

        if (childDialog != null) {
            switch (getAnimationMode()) {
                case ANIM_DIALOG_RTL:
                    childDialog.setTranslationX(fraction * dialogBase.getWidth());
//                    backBlur.setTranslationX(fraction * dialogBase.getWidth());
                    break;
                case ANIM_DIALOG_TTB:
                    childDialog.setTranslationY(fraction * dialogBase.getHeight());
//                    backBlur.setTranslationY(fraction * dialogBase.getHeight());
                    break;
            }
            cropBlurBack();
        }
    }


    @Override
    protected void cropBlurBack() {
        if (this instanceof BasicMbnPopupMenu) {
            backBlur.setCropZone(childDialog);
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        dialogBase = (FrameLayout) view;
        dialogBase.setBackgroundColor(Color.argb(0, 0, 0, 0));

        backBlur = new BlurImageView(getContext(), this instanceof BasicMbnPopupMenu);
        backBlur.setScaleType(ImageView.ScaleType.CENTER_CROP);
        backBlur.setImageBitmap(getBackgroundCache());
//        backBlur.setImageTintList(ColorStateList.valueOf(Color.argb(255 / 2, 0, 0, 0)));
//        backBlur.setImageTintMode(PorterDuff.Mode.SCREEN);

        if (!(this instanceof BasicMbnPopupMenu)) {
            dialogBase.addView(backBlur);
        }
        backBlur.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isDestroyed())
                    getFragmentSwipeBackManager().popFragment();
            }
        });


        darkener = new View(getContext());
        darkener.setBackgroundColor(Color.BLACK);
        darkener.setAlpha(1f / 3);
        dialogBase.addView(darkener);

        if (this instanceof BasicMbnPopupMenu) {
            dialogBase.addView(backBlur);
        }

        onPrepareDialog();
    }

//    @Override
//    public View makeToolBar(LayoutInflater inflater) {
//        return null;
//    }


    @Override
    public boolean hasAppBar() {
        return false;
    }

    public abstract void onPrepareDialog();

    public Bitmap getBackgroundCache() {
        if (getArguments() == null) {
            setArguments(new Bundle());
            return null;
        }
        return (Bitmap) getArguments().getParcelable(CACHE_CODE);
    }

    public void setBackgroundCache(Context context, Bitmap backgroundCache) {
        if (getArguments() == null) setArguments(new Bundle());
        getArguments().putParcelable(CACHE_CODE, MbnUtils.stackBlur(MbnUtils.createSmallBit(backgroundCache, 300), 7, true));
//        getArguments().putParcelable(CACHE_CODE, makeBlur(backgroundCache));
    }


    private Bitmap makeBlur(Bitmap bitmap) {

        float factor = 300f / (Math.max(bitmap.getWidth(), bitmap.getHeight()));

        int width = (int) (bitmap.getWidth() * factor);
        int height = (int) (bitmap.getHeight() * factor);
//        Bitmap inComeBitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true);
//            Bitmap inComeBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
//            Rect rect = new Rect(0, 0, width, height);
//            Canvas canvas = new Canvas(inComeBitmap);
//            canvas.drawBitmap(bitmap, null, rect, null);

        Bitmap use = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(use);
        canvas.drawBitmap(bitmap, null, new Rect(0, 0, width, height), null);

        int[] allTogether = new int[width * height];
        use.getPixels(allTogether, 0, width, 0, 0, width, height);
        int oneIndex, threeIndex, forIndex, fiveIndex, twoIndex, one, two, three, four, five, six, seven, eight, nine, total,
                r1, r2, r3, r4, r5, r6, r7, r8, r9, rt,
                b1, b2, b3, b4, b5, b6, b7, b8, b9, bt,
                g1, g2, g3, g4, g5, g6, g7, g8, g9, gt,
                a1, a2, a3, a4, a5, a6, a7, a8, a9, at;

        for (int x = 0; x < 8; x++) {
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
//                        allTogether[twoIndex] = total;
//                        allTogether[threeIndex] = total;
//                        allTogether[forIndex] = total;
//                        allTogether[fiveIndex] = total;
//                        allTogether[fiveIndex - 1] = total;
//                        allTogether[fiveIndex + 1] = total;
//                        allTogether[forIndex - 1] = total;
//                        allTogether[forIndex + 1] = total;

                    } catch (Exception ignored) {
                    }
                }
            }
        }

        return Bitmap.createBitmap(allTogether, width, height, Bitmap.Config.ARGB_8888);

    }


}
