package com.br.mreza.musicplayer.newdesign;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.media.MediaMetadataRetriever;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;

import com.br.mreza.musicplayer.DataSong;
import com.br.mreza.musicplayer.MBN.bitmapUtils.MbnUtils;
import com.br.mreza.musicplayer.R;
import com.br.mreza.musicplayer.newmodel.database.DataBaseManager;
import com.br.mreza.musicplayer.newmodel.database.DataBaseUtils;
import com.br.mreza.musicplayer.newmodel.jobs.ContextJobs;
import com.davemorrissey.labs.subscaleview.ImageSource;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;

import mbn.libs.backgroundtask.BaseTaskHolder;
import mbn.libs.backgroundtask.ThreadManager;
import mbn.libs.fragmanager.BaseMbnDialog;
import mbn.libs.fragmanager.CustomFragmentSwipeBackAnimator;
import mbn.libs.fragmanager.ResultReceivingManager;
import mbn.libs.fragmanager.views.CropChangerImageView;


public class AlbumCoverShowDialog extends BaseMbnDialog {


    private SubsamplingScaleImageView cover;
    private CropChangerImageView cropChangerImageView;
    private ViewGroup functionButtonsLayout;
    private ViewGroup imageViewParent;
    private long currentId;
    private DataSong currentSong;
    private InfoGetter infoGetter;
//    private Handler uiH = new Handler(Looper.getMainLooper());


    public static AlbumCoverShowDialog newInstance(int resultId) {
        Bundle args = new Bundle();
        args.putInt("INFO", resultId);
        AlbumCoverShowDialog fragment = new AlbumCoverShowDialog();
        fragment.setArguments(args);
        return fragment;
    }

    private BaseTaskHolder.ResultReceiver resultReceiver = new BaseTaskHolder.ResultReceiver() {
        @Override
        public void onResult(Object result, Object info) {
            if (currentId == (long) info) {
                Bitmap[] results = (Bitmap[]) result;
                cover.setImage(ImageSource.bitmap(results[0]));
                cropChangerImageView.setBitmap(results[1]);
                setCanStartAnim(true);
            }
        }
    };


    @Override
    public void makeState(Bundle state) {
    }

    @Override
    public void checkForRestoreState() {

    }

    @Override
    public Point getCirclePoint() {
        return new Point(getDialogBase().getWidth() / 2, getDialogBase().getHeight() / 2);
    }

    @Override
    public int getAnimationMode() {
        return CustomFragmentSwipeBackAnimator.ANIM_CUSTOM_VERTICAL;
    }

    private void validateInfo() {
        if (infoGetter == null) {
            int resultId = getArguments().getInt("INFO");
            infoGetter = new InfoGetter();
            ResultReceivingManager.sendResult(resultId, infoGetter);
        }
    }

    @Override
    public void onFractionChange(float fraction) {
        super.onFractionChange(fraction);

        functionButtonsLayout.setScaleX(1 - fraction);
        functionButtonsLayout.setAlpha(1 - fraction);

        if (fraction == 0) {
//                currentImageView.setMinimumScaleType(SubsamplingScaleImageView.SCALE_TYPE_CENTER_INSIDE);
//                currentImageView.setPanEnabled(true);
//                currentImageView.setVisibility(View.VISIBLE);
            cropChangerImageView.setVisibility(View.INVISIBLE);
//                currentImageView.resetScaleAndCenter();
        } else {
//                currentImageView.setMinimumScaleType(SubsamplingScaleImageView.SCALE_TYPE_CENTER_CROP);
//                currentImageView.setPanEnabled(false);
            cropChangerImageView.setVisibility(View.VISIBLE);
//                currentImageView.setVisibility(View.INVISIBLE);
//                currentImageView.resetScaleAndCenter();
        }


        validateInfo();
        int[] info = infoGetter.getInfo();

        int startX = info[0] - imageViewParent.getLeft();
        int startY = info[1] - imageViewParent.getTop();
        int statWidth = info[2];
        int startHeight = info[3];
        int finalWidth = imageViewParent.getWidth();
        int finalHeight = imageViewParent.getHeight();

        float alpha = 1 - (fraction * 50);
        if (alpha <= 0) {
            cover.setVisibility(View.INVISIBLE);
        } else {
            cover.setVisibility(View.VISIBLE);
            cover.setAlpha(alpha);
        }

        ViewGroup.LayoutParams layoutParams = cropChangerImageView.getLayoutParams();
        layoutParams.height = (int) (finalHeight + (fraction * (startHeight - finalHeight)));
        layoutParams.width = (int) (finalWidth + (fraction * (statWidth - finalWidth)));

        cropChangerImageView.setLayoutParams(layoutParams);
        cropChangerImageView.setX(cropChangerImageView.getLeft() + ((startX - cropChangerImageView.getLeft()) * fraction));
        cropChangerImageView.setY(cropChangerImageView.getTop() + ((startY - cropChangerImageView.getTop()) * fraction));
        cropChangerImageView.setFraction(fraction);

    }

    @Override
    public boolean hasAppBar() {
        return false;
    }

//    private Runnable blurChange = new Runnable() {
//        @Override
//        public void run() {
//            invalidateBackBlur();
//            uiH.postDelayed(this, 150);
//        }
//    };

    @Override
    public void onPrepareDialog() {
        setCanStartAnim(false);
        FrameLayout layout = (FrameLayout) getLayoutInflater().inflate(R.layout.album_cover_show_dialog_layout, getDialogBase(), false);
        cover = layout.findViewById(R.id.cover_show_image);
        cropChangerImageView = layout.findViewById(R.id.frag_transition_view);
        cropChangerImageView.setShouldResize(false);
        cropChangerImageView.setMakeCropCircular(true);
        imageViewParent = (ViewGroup) cover.getParent();

        ImageButton shareButton = layout.findViewById(R.id.cover_show_share);
        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new ContextJobs.ShareCoverJob(getContext(), currentId, true);
            }
        });
        ImageButton saveButton = layout.findViewById(R.id.cover_show_save);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new ContextJobs.ShareCoverJob(getContext(), currentId, false);
            }
        });
        functionButtonsLayout = (ViewGroup) saveButton.getParent();
        addLayout(layout);
        functionButtonsLayout.setPivotX(0);

//        getView().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                invalidateBackBlur();
//                getView().postDelayed(this, 10);
//            }
//        }, 10);
    }

    private DataBaseManager.DefaultCallback callback = new DataBaseManager.DefaultCallback(true, false) {
        @Override
        public void onTrackChange(long id, DataSong song) {
            currentSong = song;
            currentId = id;
            ThreadManager.getAppGlobalTask().StartTask(new LargeImageLoader(currentSong.getPath(), currentId, getResources()), resultReceiver);
        }
    };

    @Override
    public void onResume() {
        super.onResume();
//        uiH.post(blurChange);
        getActivity().hideStatusBar(true);

        DataBaseManager.getManager().registerCallback(callback);
        DataBaseManager.getManager().getCurrentQueueAndTrack(new BaseTaskHolder.ResultReceiver() {
            @Override
            public void onResult(Object result, Object info) {
                currentSong = (DataSong) ((Object[]) result)[1];
                currentId = currentSong.getId();
                ThreadManager.getAppGlobalTask_MultiThread().StartTask(new LargeImageLoader(currentSong.getPath(), currentSong.getId(), getResources()), resultReceiver);
            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();
//        uiH.removeCallbacks(blurChange);
        getActivity().hideStatusBar(false);

        DataBaseManager.getManager().unRegisterCallback(callback);
    }


    private class LargeImageLoader implements BaseTaskHolder.BaseTask {


        private String path;
        private long id;
        private Resources resources;

        LargeImageLoader(String path, long id, Resources resources) {
            this.path = path;
            this.id = id;
            this.resources = resources;
        }

        @Override
        public Object onRun() {
            Bitmap send = DataBaseUtils.getSongArtwork(id, 0, false, null);
            Bitmap sendSmall = null;
//            try {
//                MediaMetadataRetriever retriever = new MediaMetadataRetriever();
//                retriever.setDataSource(path);
//                byte[] bytes = retriever.getEmbeddedPicture();
////                BitmapFactory.Options options = new BitmapFactory.Options();
////                options.inJustDecodeBounds = true;
////                BitmapFactory.decodeByteArray(bytes, 0, bytes.length, options);
////                options.inSampleSize = Math.max(options.outHeight, options.outWidth) / 500;
////                options.inJustDecodeBounds = false;
//                send = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
//
//            } catch (Exception e) {
////            e.printStackTrace();
//            }

//            if (send == null) {
//                BitmapFactory.Options options = new BitmapFactory.Options();
//                options.inJustDecodeBounds = true;
//                BitmapFactory.decodeResource(resources, R.drawable.night_rain_1, options);
//                options.inSampleSize = Math.max(options.outHeight, options.outWidth) / 500;
//                options.inJustDecodeBounds = false;
//                send = BitmapFactory.decodeResource(resources, R.drawable.night_rain_1, options);
//            }
            sendSmall = MbnUtils.createSmallBit(send, 500);
            return new Bitmap[]{send, sendSmall};
        }

        @Override
        public Object getInfo() {
            return id;
        }
    }

    public class InfoGetter {
        private int[] info;

        public int[] getInfo() {
            return info;
        }

        public void setInfo(int[] info) {
            this.info = info;
        }

    }


}
