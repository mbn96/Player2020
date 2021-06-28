package com.br.mreza.musicplayer;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.util.Log;

import com.br.mreza.musicplayer.MBN.bitmapUtils.MbnUtils;

import mbn.libs.backgroundtask.BaseTaskHolder;

import static com.br.mreza.musicplayer.AlbumArtTaskForSongs.HOLDER_MAP;


public class NewAlbumArtForSongsLoader implements BaseTaskHolder.BaseTask {


    private long id;
    private String path;
    private Resources resources;

    public NewAlbumArtForSongsLoader(long id, String path, Resources resources) {
        this.id = id;
        this.path = path;
        this.resources = resources;
    }

    @Override
    public Object onRun() {

        Bitmap mBitmap = HOLDER_MAP.get((int) id);

        if (mBitmap == null) {
            try {
                MediaMetadataRetriever retriever = new MediaMetadataRetriever();
                retriever.setDataSource(path);
                byte[] coverArray = retriever.getEmbeddedPicture();
                retriever.release();
                BitmapFactory.Options options = new BitmapFactory.Options();

                options.inJustDecodeBounds = true;

                BitmapFactory.decodeByteArray(coverArray, 0, coverArray.length, options);

                int max = Math.max(options.outHeight, options.outWidth);

                Log.i("MAX", String.valueOf(max));

                options.inSampleSize = max / 200;
                options.inJustDecodeBounds = false;

                mBitmap = MbnUtils.roundedBitmap(BitmapFactory.decodeByteArray(coverArray, 0, coverArray.length, options));
            } catch (Exception e) {
                mBitmap = MbnUtils.getGreyBackground(BitmapFactory.decodeResource(resources, R.drawable.ic_note_png));
//            mBitmap = MbnUtils.roundedBitmap(BitmapFactory.decodeResource(mContext.getResources(), R.drawable.night_rain_1));
            }
            HOLDER_MAP.put((int) id, mBitmap);
        }

        return new Result(mBitmap);
    }

    @Override
    public Object getInfo() {
        return id;
    }

    class Result {
        private Bitmap bitmap;

        Result(Bitmap bitmap) {
            this.bitmap = bitmap;
        }

        public Bitmap getBitmap() {
            return bitmap;
        }
    }
}
