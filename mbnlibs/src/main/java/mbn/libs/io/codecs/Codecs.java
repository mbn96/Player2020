package mbn.libs.io.codecs;

import android.graphics.Bitmap;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import mbn.libs.imagelibs.imageworks.Effects;

public class Codecs {

    public static boolean writeTo_MPG(Bitmap bitmap, OutputStream outputStream) {
        boolean done = false;
        try {
            internal_MPG_encoder(bitmap.getWidth(), bitmap.getHeight(), Effects.getPixels(bitmap), outputStream);
            done = true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return done;
    }

    public static void internal_MPG_encoder(int width, int height, int[] pixels, OutputStream stream) throws IOException {
        BufferedOutputStream outputStream = new BufferedOutputStream(stream);
        byte[] intBuffer = new byte[4];
        int codecName = 0x19961375;

        getIntBytes(intBuffer, width);
        outputStream.write(intBuffer);

        getIntBytes(intBuffer, height);
        outputStream.write(intBuffer);

        getIntBytes(intBuffer, codecName);
        outputStream.write(intBuffer);

        for (int pixel : pixels) {
            getIntBytes(intBuffer, pixel);
            outputStream.write(intBuffer);
        }
        outputStream.flush();
        outputStream.close();
    }

    public static void getIntBytes(byte[] buffer, int integer) {
        for (int i = 0; i < 4; i++) {
            int b = 0xff;
            int shifter = (3 - i) * 8;
            b <<= shifter;
            b &= integer;
            b >>>= shifter;
            buffer[i] = (byte) b;
        }
    }






}
