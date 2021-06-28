package com.br.mreza.musicplayer.newdesign.newmodelfrags;

import android.media.AudioFormat;
import android.media.AudioTrack;
import android.util.Log;


import com.google.gson.JsonObject;

import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Random;

import mbn.libs.backgroundtask.BaseTaskHolder;
import mbn.libs.io.IO_Utils;


public class MusicFileToByte implements BaseTaskHolder.BaseTask {

    private static String TAG = "DECODER_byte_get";

    private String path;
    private long id;
    private static int kilo = 1000;
    private static int mega = kilo * kilo;

    public MusicFileToByte(String path, long id) {
        this.path = path;
        this.id = id;
    }

    @Override
    public Object onRun() {
        File music = new File(path);
        if (music.isFile()) {
            try {
                return getRawData_2(music);
//                return getRawData(music);
//                return fileToBytes(music);
            } catch (Exception e) {
                return fakeData();
            }
        }
        return null;
    }

    @Override
    public Object getInfo() {
        return id;
    }


    private byte[] fileToBytes(File file) {
        if (false) {
            return IO_Utils.fileToByteArray(file);
        }
        int size = (int) file.length();
        if (size > 5 * mega) {
            size = 5 * mega;
        }
        byte[] bytes = new byte[size];
        try {
            BufferedInputStream buf = new BufferedInputStream(new FileInputStream(file));
            buf.read(bytes, 0, bytes.length);
            buf.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bytes;

    }

    private byte[] getRawData(File file) throws IOException {
        MediaDecoder mediaDecoder = new MediaDecoder(file.getPath());
//        int ignoreRate = mediaDecoder.getSampleRate() > 10 ? mediaDecoder.getSampleRate() / 100 : 1;
//        ignoreRate--;

        int currentInSec = 0;
        int count = 0;

        byte[] data;
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        BufferedOutputStream byteStream = new BufferedOutputStream(byteArrayOutputStream);

//        while (mediaDecoder.readByeData() != null) {
//            count++;
//        }
//
//        count++;

        Log.i(TAG, "PCM: " + mediaDecoder.getPCM());
//        mediaDecoder.readByeData()
        while (true) {
            if (count % 5 == 0) {
                data = mediaDecoder.readByeData();
//                Log.i(TAG, "PCM: " + mediaDecoder.getPCM());

                if (data != null) {
                    byteStream.write(data);
                } else if (!mediaDecoder.advance()) {
                    break;
                }
//                byteStream.write(getSampleAverage(data));
            } else {
//                if (mediaDecoder.readByeData() == null) {
//                    break;
//                }
                if (!mediaDecoder.advance()) {
                    break;
                }
            }
//            currentInSec++;
//            if (currentInSec > ignoreRate) {
//                currentInSec = 0;
//            }
            count++;
        }

//        Log.i(TAG, "Sample count: " + count);
        byteStream.flush();
        byteStream.close();
        return byteArrayOutputStream.toByteArray();
    }

    private byte[] getRawData_2(File file) throws IOException {
        MediaDecoder mediaDecoder = new MediaDecoder(file.getPath());
//        int ignoreRate = mediaDecoder.getSampleRate() > 10 ? mediaDecoder.getSampleRate() / 100 : 1;
//        ignoreRate--;


//        AudioFormat.Builder formatBuilder = new AudioFormat.Builder();
//        formatBuilder.setEncoding(AudioFormat.ENCODING_PCM_16BIT).setSampleRate(mediaDecoder.getSampleRate()).setChannelMask(AudioFormat.CHANNEL_OUT_STEREO);
//        AudioTrack.Builder builder = new AudioTrack.Builder();
//        builder.setAudioFormat(formatBuilder.build()).setTransferMode(AudioTrack.MODE_STREAM).setBufferSizeInBytes(AudioTrack.getMinBufferSize(mediaDecoder.getSampleRate(),
//                AudioFormat.CHANNEL_OUT_STEREO, AudioFormat.ENCODING_PCM_16BIT));
//        AudioTrack track = builder.build();


        int currentInSec = 0;
        int count = 0;

        short[] data;
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        BufferedOutputStream byteStream = new BufferedOutputStream(byteArrayOutputStream);

//        while (mediaDecoder.readByeData() != null) {
//            count++;
//        }
//
//        count++;

        Log.i(TAG, "PCM: " + mediaDecoder.getPCM());
//        mediaDecoder.readByeData()
        while (true) {
            if (count % 10 == 0) {
                data = mediaDecoder.readShortData();
//                Log.i(TAG, "PCM: " + mediaDecoder.getPCM());

                if (data != null) {
//                    byteStream.write(data);
                    shortToByte(data, byteStream);
                } else if (!mediaDecoder.advance()) {
                    break;
                }
//                byteStream.write(getSampleAverage(data));
            } else {
//                if (mediaDecoder.readByeData() == null) {
//                    break;
//                }
                if (!mediaDecoder.advance()) {
                    break;
                }
            }
//            currentInSec++;
//            if (currentInSec > ignoreRate) {
//                currentInSec = 0;
//            }
            count++;
        }

//        Log.i(TAG, "Sample count: " + count);
        byteStream.flush();
        byteStream.close();
        return byteArrayOutputStream.toByteArray();
    }

    private void shortToByte(short[] shorts, OutputStream stream) throws IOException {
        int data = 0;
        for (short aShort : shorts) {
            data = 0;
            data |= aShort;
            data >>>= 8;
            stream.write(data);
        }
    }

    private int getSampleAverage(byte[] sample) {
        if (sample.length == 0) return 0;
        float average = 0;
        double factor = 1d / sample.length;
        for (byte b : sample) {
            average += Math.abs(b * factor);
        }
        return (int) average;
    }

    private byte[] fakeData() {
        byte[] out = new byte[mega];
        Random random = new Random();
        random.nextBytes(out);
        return out;
    }

}
