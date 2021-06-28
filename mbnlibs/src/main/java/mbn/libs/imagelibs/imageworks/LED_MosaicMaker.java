package mbn.libs.imagelibs.imageworks;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;


import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

import mbn.libs.backgroundtask.BaseTaskHolder;
import mbn.libs.backgroundtask.ThreadManager;

public class LED_MosaicMaker {

    //    private ThreadManager threadManager = new ThreadManager();
    private ThreadManager threadManager = new ThreadManager(15, true);
    private int[] pixels;
    private Bitmap bitmap;
    private int totalCirclesCount;
    private AtomicInteger finishedCircles = new AtomicInteger(0);
    private int mainWidth, mainHeight;
    private int circleRadios = 10;
    private int space = 3;
    private int circleWidth = 2 * circleRadios;
    private int columnWidth = circleWidth + space;
    private float rowHeight = (float) (Math.sin(Math.PI / 3) * columnWidth);
    private int rowHeight_Integer = (int) rowHeight;
    private final Object LOCK = new Object();
    private Callback callback;
    //    private ArrayList<CircleItem> circleItems_main = new ArrayList<>();
    private CircleItem[] circleItemsArray;

    private String TAG = "MOSAIC";

    private BaseTaskHolder.ResultReceiver finalBitmapReceiver = new BaseTaskHolder.ResultReceiver() {
        @Override
        public void onResult(Object result, Object info) {
            callback.onFinish((Bitmap) result);

        }
    };

    public LED_MosaicMaker(String path, Callback callback) {
        this.callback = callback;
        threadManager.getTaskHolder().StartTask(new PrepareJob(path), null);
    }

    private class PrepareJob implements BaseTaskHolder.BaseTask {

        private String path;
        private int rowCount, columnCount;
        private ArrayList<CircleJob> circleJobs = new ArrayList<>();

        PrepareJob(String path) {
            this.path = path;
        }

        private void createBitmap() {
            BitmapFactory.Options options = new BitmapFactory.Options();

//            options.inJustDecodeBounds = true;
//            BitmapFactory.decodeFile(path, options);
//            options.inSampleSize = Math.max(options.outHeight, options.outWidth) / 600;
//            options.inJustDecodeBounds = false;

            bitmap = MbnUtils.createSmallBit(BitmapFactory.decodeFile(path, options), 1000);
        }

        private void createCircleJobs() {
            CircleJob currentJob = new CircleJob();
            circleJobs.add(currentJob);
            for (int y = 0; y < rowCount; y++) {
                int minus = y % 2 == 0 ? 0 : 1;
                for (int x = 0; x < columnCount - minus; x++) {
                    if (!currentJob.put(new CircleItem(totalCirclesCount, x, y))) {
                        currentJob = new CircleJob();
                        currentJob.put(new CircleItem(totalCirclesCount, x, y));
                        circleJobs.add(currentJob);
                    }
                    totalCirclesCount++;
                }
            }
            circleItemsArray = new CircleItem[totalCirclesCount];
            for (CircleJob job : circleJobs) {
                threadManager.getTaskHolder().StartTask(job, null);
            }
        }

        @Override
        public Object onRun() {
            if (bitmap == null) {
                createBitmap();
            }

            mainWidth = bitmap.getWidth();
            mainHeight = bitmap.getHeight();

            pixels = new int[mainWidth * mainHeight];
            bitmap.getPixels(pixels, 0, mainWidth, 0, 0, mainWidth, mainHeight);


            columnCount = mainWidth / columnWidth;
            rowCount = (int) (mainHeight / rowHeight);

            createCircleJobs();

            threadManager.getTaskHolder().StartTask(new FinalJob(), finalBitmapReceiver);

            return null;
        }

        @Override
        public Object getInfo() {
            return null;
        }
    }

    private class CircleItem {
        private int id;
        private int x, y;
        private int color;
        private int rSum, bSum, gSum;
        private int startX, startY;

        CircleItem(int id, int x, int y) {
            this.id = id;
            this.x = x;
            this.y = y;
        }

        private void fillColors(int pixel) {
            rSum += Color.red(pixel);
            bSum += Color.blue(pixel);
            gSum += Color.green(pixel);
        }

        void findColor() {
            startY = (int) (y * rowHeight);
            startX = x * columnWidth;
            startX = y % 2 == 0 ? startX : (int) (startX + (circleWidth + (space / 2f) + 0.5f) - circleRadios);
            int pixCount = 0;

            for (int picY = startY; picY < startY + circleWidth; picY++) {
                for (int picX = startX; picX < startX + circleWidth; picX++) {
                    if (picY < mainHeight && picX < mainWidth) {
                        fillColors(pixels[(picY * mainWidth) + picX]);
//                        fillColors(bitmap.getPixel(picX, picY));

                        pixCount++;
                    }
                }
            }

            color = Color.rgb(rSum / pixCount, gSum / pixCount, bSum / pixCount);

            circleItemsArray[id] = this;
            finishedCircles.incrementAndGet();
//            synchronized (LOCK) {
//                circleItems_main.add(this);
////                Log.i(TAG, "findColor: " + x + " _ " + y + " _ " + color);
//                LOCK.notifyAll();
//            }
        }

        public int getColor() {
            return color;
        }
    }

    private class CircleJob implements BaseTaskHolder.BaseTask {

        private ArrayList<CircleItem> circleItems = new ArrayList<>();

        public boolean put(CircleItem circleItem) {
            if (circleItems.size() < 10) {
                circleItems.add(circleItem);
                return true;
            }
            return false;
        }


        @Override
        public Object onRun() {
            for (CircleItem item : circleItems) {
                item.findColor();
            }
            return null;
        }

        @Override
        public Object getInfo() {
            return null;
        }
    }

    private class FinalJob implements BaseTaskHolder.BaseTask {

        private Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        private Path path = new Path();

        @Override
        public Object onRun() {
//            paint.setStyle(Paint.Style.FILL_AND_STROKE);
            paint.setStyle(Paint.Style.FILL);
//            paint.setStrokeWidth(7);
//            paint.setColor(Color.argb(10, 255, 255, 255));
            Bitmap out = Bitmap.createBitmap(mainWidth, mainHeight, Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(out);
            canvas.drawColor(Color.WHITE);
            int done = 0;

            while (finishedCircles.get() < totalCirclesCount) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException ignored) {
                }
            }

            while (done < totalCirclesCount) {
//                synchronized (LOCK) {
//                    if (circleItems_main.isEmpty()) {
//                        try {
//                            LOCK.wait();
//                        } catch (InterruptedException ignored) {
//                            continue;
//                        }
//                    }
                CircleItem currentItem = circleItemsArray[done];
                paint.setColor(currentItem.getColor());
//                path.rewind();
//                if (currentItem.y % 2 == 0) {
//                path.moveTo(currentItem.startX, currentItem.startY);
//                path.lineTo(currentItem.startX + circleWidth, currentItem.startY);
//                path.lineTo(currentItem.startX + circleWidth, currentItem.startY + circleWidth);
//                path.lineTo(currentItem.startX, currentItem.startY + circleWidth);
//                } else {
//                    path.moveTo(currentItem.startX, currentItem.startY + circleRadios);
//                    path.lineTo(currentItem.startX + circleWidth, currentItem.startY + circleWidth);
//                    path.lineTo(currentItem.startX + circleWidth, currentItem.startY);
//                }
//                path.addCircle(currentItem.startX + circleRadios, currentItem.startY + circleRadios, circleRadios, Path.Direction.CW);
//                paint.setShadowLayer(5, 0, 0, currentItem.getColor());
//                canvas.drawPath(path, paint);
                canvas.drawCircle(currentItem.startX + circleRadios, currentItem.startY + circleRadios, circleRadios, paint);
//                }
                done++;
//                Log.i(TAG, "onRun: " + totalCirclesCount + " _ " + done);
            }
//            Log.i(TAG, "onRun: Finish");
            return out;
        }


        @Override
        public Object getInfo() {
            return null;
        }
    }

    public interface Callback {
        void onFinish(Bitmap result);
    }

}
