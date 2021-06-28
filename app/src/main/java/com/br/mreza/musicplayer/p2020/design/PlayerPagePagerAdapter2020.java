package com.br.mreza.musicplayer.p2020.design;


import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.util.Log;
import android.util.LongSparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.core.util.Pools;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.br.mreza.musicplayer.R;
import com.br.mreza.musicplayer.newdesign.materialtheme.PlayerPageBackgroundMaterial;
import com.br.mreza.musicplayer.newdesign.materialtheme.pageDecorationMaterialTheme;
import com.br.mreza.musicplayer.newmodel.database.DataBaseManager;
import com.br.mreza.musicplayer.newmodel.database.SelfShrinkMap;

import java.util.ArrayList;

import mbn.libs.backgroundtask.BaseTaskHolder;
import mbn.libs.imagelibs.imageworks.ShadowBackground;

public class PlayerPagePagerAdapter2020 extends PagerAdapter implements pageDecorationMaterialTheme.HeightChangerView {

    private String TAG = "adapter_pager";

    private int screenHeight;
    private View bottomView;
    private Bitmap defaultBitmap;
    private ViewPager viewPager;
    private ArrayList<Long> queue = new ArrayList<>();
    private long currentId;
    private Pools.SimplePool<FrameLayout> items_pool = new Pools.SimplePool<>(10);
    private SelfShrinkMap<Bitmap> bitmaps = new SelfShrinkMap<>(10);
    private ArrayList<ImageView> inUseImageViews = new ArrayList<>();
    private LongSparseArray<ImageView> requestingImageViews = new LongSparseArray<>();

    private BaseTaskHolder.ResultReceiver bitmapReceiver = new BaseTaskHolder.ResultReceiver() {
        @Override
        public void onResult(Object result, Object info) {
            long id = (long) info;
            Bitmap bitmap;
            if (result != null) {
                bitmap = (Bitmap) result;
            } else bitmap = defaultBitmap;
            bitmaps.put(id, bitmap);
            if (requestingImageViews.get(id) != null && inUseImageViews.contains(requestingImageViews.get(id))) {
                requestingImageViews.get(id).setImageBitmap(bitmap);
                requestingImageViews.remove(id);
            }

        }
    };


    public void setQueue(ArrayList<Long> queue_new) {
        queue.clear();
        queue.addAll(queue_new);
        requestingImageViews.clear();
        inUseImageViews.clear();
        notifyDataSetChanged();
        viewPager.setAdapter(this);
    }

    public void setCurrentId(long currentId) {
        this.currentId = currentId;
        viewPager.post(changeRunnable);
    }

    private Runnable changeRunnable = new Runnable() {
        @Override
        public void run() {
            viewPager.setCurrentItem(queue.indexOf(currentId), true);
        }
    };


    public PlayerPagePagerAdapter2020(Bitmap defaultBitmap, ViewPager viewPager, View bottomView) {
        this.bottomView = bottomView;
        this.defaultBitmap = defaultBitmap;
        this.viewPager = viewPager;
        viewPager.setOffscreenPageLimit(3);

        viewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            private boolean changed = false;
            private int pos;

            @Override
            public void onPageSelected(int position) {
                pos = position;
                changed = true;
                long id = queue.get(pos);
                if (id != currentId) {
                    DataBaseManager.getManager().setCurrentTrack(id);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
//                if (state == ViewPager.SCROLL_STATE_IDLE && changed) {
//                    long id = queue.get(pos);
//                    if (id != currentId) {
////                        DataBaseManager.getManager().setCurrentTrack(id);
//                    }
//                    changed = false;
//                }
            }
        });


        viewPager.setPageTransformer(false, pageTransformer);
    }

    @Override
    public void setScreenHeight(int screenHeight) {
        this.screenHeight = screenHeight;
//        for (PlayerPageBackgroundMaterial img : inUseImageViews) {
//            img.setScreenHeight(screenHeight);
//        }
    }

    @Override
    public int getScreenHeight() {
        return screenHeight;
    }

    private void requestBitmap(long id) {
        DataBaseManager.getManager().getSongArtWork(id, 600, bitmapReceiver);
    }

    private void prepareItem(int pos, ImageView imageView) {
        long id = queue.get(pos);
        Bitmap bitmap = bitmaps.get(id);
        if (bitmap == null) {
            requestingImageViews.put(id, imageView);
            requestBitmap(id);
            return;
        }
        imageView.setImageBitmap(bitmap);
    }

    private FrameLayout getItem(Context context) {
        FrameLayout view = items_pool.acquire();
//        FrameLayout view = null;
        if (view == null) {
            view = (FrameLayout) LayoutInflater.from(context).inflate(R.layout.album_art_2020, viewPager, false);
//            view = new FrameLayout(context);
            View image = view.getChildAt(0);
            image.setClipToOutline(true);
            int side = (int) (Math.min(((View) viewPager).getWidth(), ((View) viewPager).getHeight() - bottomView.getHeight()) * 0.6);
            image.getLayoutParams().height = side;
            image.getLayoutParams().width = side;

//            ShadowBackground imageBackShadow = new ShadowBackground(context, image, 45, (int) (15 * context.getResources().getDisplayMetrics().density), Color.LTGRAY);
//            image.setBackground(imageBackShadow);

//            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
//            view.addView(imageView);
        }
        return view;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        FrameLayout mainView = getItem(container.getContext());
        ImageView imageView = (ImageView) mainView.getChildAt(0);
        inUseImageViews.add(imageView);
//        imageView.setScreenHeight(screenHeight);
        prepareItem(position, imageView);
        container.addView(mainView);
        return mainView;
    }

    @Override
    public int getCount() {
        return queue.size();
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
        FrameLayout mainView = (FrameLayout) object;
        ImageView imageView = (ImageView) mainView.getChildAt(0);
        inUseImageViews.remove(imageView);
//        requestingImageViews.remove(queue.get(position));
//        requestingImageViews.removeAt(requestingImageViews.indexOfValue(imageView));
        items_pool.release((FrameLayout) object);
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return object == view;
    }

    private ViewPager.PageTransformer pageTransformer = new ViewPager.PageTransformer() {
        @SuppressLint("WrongConstant")
        @Override
        public void transformPage(@NonNull View page, float position) {
            if (position <= -2 || position >= 2) {
                page.setVisibility(View.INVISIBLE);
            } else {
                ImageView image = (ImageView) ((ViewGroup) page).getChildAt(0);
                page.setVisibility(View.VISIBLE);
                page.setTranslationX((page.getWidth() * 0.5f) * -position);
//                page.setTranslationX(10 * position);
//                image.setTranslationX(image.getWidth() * -(position / 2));
                image.setScaleX(1 - (0.5f * Math.abs(position)));
                image.setScaleY(1 - (0.5f * Math.abs(position)));
            }
        }
    };
}
