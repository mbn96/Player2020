package mbn.libs.imagelibs.imageworks;


import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

public class CharPixelCounter {

    private int cellWidth = 100;
    private int cellColumnCount = 10;
    private float selectiveFactor = 10f;
    private int sheetWidth = cellWidth * cellColumnCount;

    private HashMap<Float, Character> characterHashMap = new HashMap<>();

    public Bitmap prepareWithRawString(String characters) {
        char[] chars = characters.toCharArray();
        Bitmap out = Bitmap.createBitmap(sheetWidth, (int) Math.ceil(chars.length / (float) cellColumnCount) * cellWidth, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(out);

        ArrayList<CharForCal> forCals = new ArrayList<>();

        Paint paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.WHITE);
        paint.setTextSize(100);

        float[] width = new float[1];
        Rect textSize = new Rect();

        for (int i = 0; i < chars.length; i++) {
            String chr = String.valueOf(chars[i]);
            CharForCal charForCal = new CharForCal(chr);
            drawChar(paint, width, textSize, canvas, chr, i, charForCal);
            calculatePixels(out, charForCal);
            forCals.add(charForCal);
        }
        Collections.sort(forCals, new Comparator<CharForCal>() {
            @Override
            public int compare(CharForCal o1, CharForCal o2) {
                int out = 0;
                if (o1.actualValue > o2.actualValue) out = -1;
                else if (o1.actualValue < o2.actualValue) out = 1;
                return out;
            }
        });

        createMap(forCals);

        return out;
    }

    private void createMap(ArrayList<CharForCal> charForCals) {
        int lastOne = Integer.MAX_VALUE;
        for (float i = 100; i >= 0; i -= selectiveFactor) {
            sortByDelta(i, lastOne, charForCals);
            lastOne = charForCals.get(0).getActualValue();
            characterHashMap.put(i, new Character(charForCals.remove(0).getChr()));
        }

//        Log.i("pixels", "createMap: " + characterHashMap);
//        Log.i("pixels", "createMap: " + getCharacter(97));
//        Log.i("pixels", "createMap: " + getCharacter(50));
//        Log.i("pixels", "createMap: " + getCharacter(31));

    }

    private void sortByDelta(float value, int lastOne, ArrayList<CharForCal> charForCals) {
        for (CharForCal cal : charForCals) {
            cal.setCurrentDelta((int) (value * 100));
        }
        Collections.sort(charForCals, new Comparator<CharForCal>() {
            @Override
            public int compare(CharForCal o1, CharForCal o2) {
                int out = 0;
                if (o1.currentDelta < o2.currentDelta) out = -1;
                else if (o1.currentDelta > o2.currentDelta) out = 1;
                return out;
            }
        });

        while (lastOne - charForCals.get(0).getActualValue() < 152) {
            charForCals.remove(0);
        }

    }

    public String getCharacter(int vl) {
        float value = vl;
        if (value < 0 || value > 100) {
            return ".";
        }
        if (value % selectiveFactor == 0) {
            return characterHashMap.get(value).getChr();
        }
        float factor = value % selectiveFactor >= selectiveFactor / 2 ? 0.5f : -0.5f;
        while (value % selectiveFactor != 0f) {
            value += factor;
        }
        return characterHashMap.get(value).getChr();
    }

    private void calculatePixels(Bitmap bitmap, CharForCal charForCal) {
        int count = 0;
        for (int y = charForCal.startY; y < charForCal.startY + cellWidth; y++) {
            for (int x = charForCal.startX; x < charForCal.startX + cellWidth; x++) {
                int color = bitmap.getPixel(x, y);
                if (color == Color.WHITE) count++;
            }
        }
        charForCal.setActualValue(count);

//        Log.i("pixels", "calculatePixels: " + charForCal);
    }

    private void drawChar(Paint paint, float[] width, Rect textSize, Canvas canvas, String chr, int number, CharForCal charForCal) {
        canvas.save();
        int row = number / cellColumnCount;
        int column = number % cellColumnCount;
        charForCal.startX = column * cellWidth;
        charForCal.startY = row * cellWidth;
        canvas.translate(charForCal.startX, charForCal.startY);

        paint.getTextBounds(chr, 0, 1, textSize);
        paint.getTextWidths(chr, width);
        canvas.drawText(chr, (cellWidth - width[0]) / 2, ((cellWidth - textSize.height()) / 2) + textSize.height(), paint);

        canvas.restore();
    }

    private class Character {
        private String chr;

        Character(String chr) {
            this.chr = chr;
        }


        public String getChr() {
            return chr;
        }

        @Override
        public String toString() {
            return chr;
        }
    }

    private class CharForCal extends Character {

        private int actualValue, currentDelta, startX, startY;

        CharForCal(String chr) {
            super(chr);
        }

        public void setActualValue(int actualValue) {
            this.actualValue = actualValue;
        }

        public void setCurrentDelta(int value) {
            this.currentDelta = Math.abs(value - actualValue);
        }

        public int getActualValue() {
            return actualValue;
        }

        public int getCurrentDelta() {
            return currentDelta;
        }


        @Override
        public String toString() {
            return getChr() + " => " + getActualValue();
        }
    }

}
