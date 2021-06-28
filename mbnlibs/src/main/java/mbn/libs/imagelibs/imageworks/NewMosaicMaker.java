package mbn.libs.imagelibs.imageworks;


import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import androidx.annotation.Nullable;

import mbn.libs.backgroundtask.MultiThreadSequentialTasks;


public abstract class NewMosaicMaker extends MultiThreadSequentialTasks<NewMosaicMaker.MosaicRequest, Bitmap> {


    protected NewMosaicMaker(int queueSize, int threadsCount) {
        super(queueSize, threadsCount, true);
    }

    @Override
    public Bitmap neededProcess(MosaicRequest request) {
        request.setup();
        Bitmap out = Bitmap.createBitmap(request.mainWidth, request.mainHeight, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(out);
        boolean hasMethod = request.drawMethod != null;

        int circleRadios = request.circleRadios;
        int space = request.space;
        int mode = request.mode;
        int mainWidth = request.mainWidth, mainHeight = request.mainHeight;
        int circleWidth = request.circleWidth;
        int columnWidth = request.columnWidth;
        float rowHeight = request.rowHeight;

        int[] pixels = new int[mainWidth * mainHeight];
        request.bitmap.getPixels(pixels, 0, mainWidth, 0, 0, mainWidth, mainHeight);

        int columnCount = mainWidth / columnWidth;
        int rowCount = (int) (mainHeight / rowHeight);

        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.WHITE);
        if (hasMethod) {
            request.drawMethod.prepareBackground(canvas, paint);
        } else {
            prepareBackground(mode, canvas, paint, request);
        }

        for (int y = 0; y < rowCount; y++) {
            int minus = y % 2 == 0 ? 0 : 1;
            for (int x = 0; x < columnCount - minus; x++) {
                if (hasMethod) {
                    CircleItem circleItem = new CircleItem(x, y, mainHeight, mainWidth);
                    circleItem.findColor(pixels, space, rowHeight, columnWidth, circleWidth, circleRadios);
                    request.drawMethod.draw(circleItem, canvas, circleRadios, paint);
                } else {
                    CircleItem circleItem = getNewCircleItem(mode, x, y, mainHeight, mainWidth);
                    circleItem.findColor(pixels, space, rowHeight, columnWidth, circleWidth, circleRadios);
                    circleItem.draw(canvas, circleRadios, paint, request);
                }
            }
        }

        return out;
    }

    private void prepareBackground(int mode, Canvas canvas, Paint paint, MosaicRequest request) {
        switch (mode) {
            case 0:
                canvas.drawColor(Color.WHITE);
                break;
            case 3:
                paint.setTextSize(request.circleWidth);
            case 1:
            case 2:
                canvas.drawColor(Color.DKGRAY);
                break;
        }
    }

    private CircleItem getNewCircleItem(int mode, int x, int y, int mainHeight, int mainWidth) {
        CircleItem circleItem = null;
        switch (mode) {
            case 1:
                circleItem = new SizeVaryItem(x, y, mainHeight, mainWidth);
                break;
            case 2:
                circleItem = new ColoredSizeVaryItem(x, y, mainHeight, mainWidth);
                break;
            case 3:
                circleItem = new CharacterItem(x, y, mainHeight, mainWidth);
                break;
            case 0:
                circleItem = new CircleItem(x, y, mainHeight, mainWidth);
                break;
        }
        return circleItem;
    }

    public static class MosaicRequest {
        private Bitmap bitmap;
        private Object optionalObject;
        private int circleRadios;
        private int space;
        private int mode;
        private DrawMethod drawMethod;

        private int mainWidth, mainHeight;
        private int circleWidth;
        private int columnWidth;
        private float rowHeight;

        public MosaicRequest(Bitmap bitmap, int circleRadios, int space, DrawMethod drawMethod, @Nullable Object optionalObject) {
            this.bitmap = bitmap;
            this.circleRadios = circleRadios;
            this.space = space;
            this.drawMethod = drawMethod;
            this.optionalObject = optionalObject;
        }

        public MosaicRequest(Bitmap bitmap, int circleRadios, int space, int mode, @Nullable Object optionalObject) {
            this.bitmap = bitmap;
            this.circleRadios = circleRadios;
            this.space = space;
            this.mode = mode;
            this.optionalObject = optionalObject;
        }

        private void setup() {
            mainWidth = bitmap.getWidth();
            mainHeight = bitmap.getHeight();
            circleWidth = 2 * circleRadios;
            columnWidth = circleWidth + space;
            rowHeight = (float) (Math.sin(Math.PI / 3f) * columnWidth);
        }
    }

    private static class CircleItem {
        //        private int id;
        private int x, y;
        private int mainHeight, mainWidth;

        private int color;
        private int rSum, bSum, gSum;
        /*
        These used for calculation.
         */
        int startXCal, startYCal;
        /*
        These used for final drawing.
         */
        float startY, startX;

        CircleItem(int x, int y, int mainHeight, int mainWidth) {
            this.x = x;
            this.y = y;
            this.mainHeight = mainHeight;
            this.mainWidth = mainWidth;
        }

        public float getStartX() {
            return startX;
        }

        public float getStartY() {
            return startY;
        }

        public int getColor() {
            return color;
        }

        private void fillColors(int pixel) {
            rSum += Color.red(pixel);
            bSum += Color.blue(pixel);
            gSum += Color.green(pixel);
        }

        void findColor(int[] pixels, int space, float rowHeight, int columnWidth, int circleWidth, int circleRadios) {
            startYCal = (int) (startY = y * rowHeight);
            startX = x * columnWidth;
            startX = y % 2 == 0 ? startX : startX + circleWidth + (space / 2f) - circleRadios;
            startXCal = (int) (startX + 0.5f);
            int pixCount = 0;

            for (int picY = startYCal; picY < startYCal + circleWidth; picY++) {
                for (int picX = startXCal; picX < startXCal + circleWidth; picX++) {
                    if (picY < mainHeight && picX < mainWidth) {
                        fillColors(pixels[(picY * mainWidth) + picX]);
//                        fillColors(bitmap.getPixel(picX, picY));
                        pixCount++;
                    }
                }
            }
            color = Color.rgb(rSum / pixCount, gSum / pixCount, bSum / pixCount);

//            synchronized (LOCK) {
//                circleItems_main.add(this);
////                Log.i(TAG, "findColor: " + x + " _ " + y + " _ " + color);
//                LOCK.notifyAll();
//            }
        }

        protected void draw(Canvas canvas, int circleRadios, Paint paint, MosaicRequest request) {
            paint.setColor(getColor());
            canvas.drawCircle(startX + circleRadios, startY + circleRadios, circleRadios, paint);
        }
    }

    private static class SizeVaryItem extends CircleItem {
        SizeVaryItem(int x, int y, int mainHeight, int mainWidth) {
            super(x, y, mainHeight, mainWidth);
        }

        protected float getFactor() {
//            int color = getColor();
//            int sum = 0;
//            sum += Color.blue(color);
//            sum += Color.green(color);
//            sum += Color.red(color);
//            return (sum / 3f) / 255f;
            return Effects.getMonochromePercentage(getColor());
//            return Color.luminance(color);
        }

        @Override
        protected void draw(Canvas canvas, int circleRadios, Paint paint, MosaicRequest request) {
//            int color = getColor();
//            int sum = 0;
//            sum += Color.blue(color);
//            sum += Color.green(color);
//            sum += Color.red(color);
//            float factor = (sum / 3f) / 255;
//            factor += factor < 0.5f ? (0.5f - factor) : 0;
            canvas.drawCircle(startX + circleRadios, startY + circleRadios, circleRadios * getFactor(), paint);
        }
    }

    private static class ColoredSizeVaryItem extends SizeVaryItem {
        ColoredSizeVaryItem(int x, int y, int mainHeight, int mainWidth) {
            super(x, y, mainHeight, mainWidth);
        }

        @Override
        protected void draw(Canvas canvas, int circleRadios, Paint paint, MosaicRequest request) {
            paint.setColor(getColor());
            super.draw(canvas, circleRadios, paint, request);
        }
    }

    private static class CharacterItem extends SizeVaryItem {

        CharacterItem(int x, int y, int mainHeight, int mainWidth) {
            super(x, y, mainHeight, mainWidth);
        }

        //        private String[] chars = new String[]{".", ",", "/", "1", "2", "3", "4", "5", "6", "(7", "8"};
//        private String[] chars = new String[]{".", ",", "/", "1", "3", "C", "D", "O", "(/)", "@", "M"};

        @Override
        protected void draw(Canvas canvas, int circleRadios, Paint paint, MosaicRequest request) {
//            String dChar = chars[(int) (getFactor() * 10)];
            CharPixelCounter charPixelCounter = (CharPixelCounter) request.optionalObject;
            String dChar = charPixelCounter.getCharacter((int) (getFactor() * 100));

//            String dChar = "0/1";

            paint.setTextSize(((getFactor() / 3) * request.circleWidth) + request.circleWidth);

            Rect textSize = new Rect();
            float[] width = new float[dChar.length()];
            int sumWidth = 0;

            paint.getTextBounds(dChar, 0, 1, textSize);
            paint.getTextWidths(dChar, width);
            for (float aWidth : width) {
                sumWidth += aWidth;
            }
            canvas.drawText(dChar, startX + ((request.columnWidth - sumWidth) / 2), startY + (((request.rowHeight - textSize.height()) / 2) + textSize.height()), paint);

//            canvas.drawText(dChar, startX, startY + (2 * circleRadios), paint);
        }
    }

    public interface DrawMethod {
        void draw(CircleItem circleItem, Canvas canvas, int circleRadios, Paint paint);

        void prepareBackground(Canvas canvas, Paint paint);
    }

}
