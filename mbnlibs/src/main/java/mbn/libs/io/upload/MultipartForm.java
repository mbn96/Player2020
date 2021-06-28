package mbn.libs.io.upload;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Random;

public class MultipartForm extends BaseForm {

    private final Object LOCK = new Object();
    private String boundary;
    private static final String LINE_FEED = "\r\n";
    private static final byte[] LINE_FEED_BYTES = stringToByteArray(LINE_FEED);
    private boolean inExecution = false;
    private String charset = "UTF-8";
    private int responseCode;
    private String responseMessage;

    private ArrayList<FormFrame> parts = new ArrayList<>();
    private static final String TAG = "MultiPart";

    public static String generateBoundary() {
        StringBuilder buffer = new StringBuilder();
        Random rand = new Random();
        int count = rand.nextInt(11) + 30; // a random size from 30 to 40
        for (int i = 0; i < count; i++) {
            buffer.append(MULTIPART_CHARS[rand.nextInt(MULTIPART_CHARS.length)]);
        }
        return buffer.toString();
    }

    private final static char[] MULTIPART_CHARS =
            "-_1234567890abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ"
                    .toCharArray();


    public MultipartForm(String url) {
        super(url);
    }

    public MultipartForm appTextPart(String name, String text) {
        synchronized (LOCK) {
            if (inExecution) {
                throw new RuntimeException("The multipartForm is in execution.");
            }
            parts.add(new TextForm(name, text));
            return this;
        }
    }

    public MultipartForm appFilePart(String name, File file) {
        synchronized (LOCK) {
            if (inExecution) {
                throw new RuntimeException("The multipartForm is in execution.");
            }
            parts.add(new FileForm(name, file));
            return this;
        }
    }

    public int getResponseCode() {
        return responseCode;
    }

    public String getResponseMessage() {
        return responseMessage;
    }

    @Override
    public boolean execute() {
        synchronized (LOCK) {
            HttpURLConnection urlConnection = null;
            boolean done = false;
            try {
                inExecution = true;
                boundary = "---" + generateBoundary() + "---";
                urlConnection = (HttpURLConnection) new URL(getUrl()).openConnection();
                urlConnection.setRequestMethod("POST");
                urlConnection.setUseCaches(false);
                urlConnection.setDoOutput(true);    // indicates POST method
                urlConnection.setDoInput(true);
                urlConnection.setRequestProperty("Content-Type",
                        "multipart/form-data; boundary=" + boundary);
                long length = 0;
                for (FormFrame p : parts) {
                    length += p.calculateLength();
                }

                String finalBoundary = LINE_FEED +
                        "--" + boundary + "--" + LINE_FEED;
                byte[] finalBoundaryBytes = stringToByteArray(finalBoundary);

                length += finalBoundaryBytes.length;
                urlConnection.addRequestProperty("Content-Length", String.valueOf(length));
                urlConnection.connect();
                OutputStream outputStream = urlConnection.getOutputStream();

                for (FormFrame p : parts) {
                    p.write(outputStream);
                }
                outputStream.flush();
                outputStream.write(finalBoundaryBytes, 0, finalBoundaryBytes.length);
                outputStream.flush();
                outputStream.close();

                responseCode = urlConnection.getResponseCode();
                responseMessage = urlConnection.getResponseMessage();
                if (responseCode == 200) done = true;
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                inExecution = false;
            }
            return done;
        }
    }


    private abstract class FormFrame {
        private String name;

        FormFrame(String name) {
            this.name = name;
        }

        String getName() {
            return name;
        }

        protected abstract void makeDescription();

        abstract long calculateLength();

        abstract void write(OutputStream os) throws Exception;
    }

    private class TextForm extends FormFrame {

        private String text;
        private String dataString;
        private byte[] dataBytes;

        TextForm(String name, String text) {
            super(name);
            this.text = text;
        }

        @Override
        protected void makeDescription() {
            dataString = "--" + boundary + LINE_FEED +
                    "Content-Disposition: form-data; name=\"" + getName() + "\"" +
                    LINE_FEED +
                    "Content-Type: text/plain; charset=" + charset +
                    LINE_FEED +
                    LINE_FEED +
                    text + LINE_FEED;
            dataBytes = stringToByteArray(dataString);
        }

        @Override
        long calculateLength() {
            makeDescription();
            return dataBytes.length;
        }

        @Override
        void write(OutputStream os) throws Exception {
            BufferedOutputStream outputStream = new BufferedOutputStream(os);
            outputStream.write(dataBytes, 0, dataBytes.length);
            outputStream.flush();
        }
    }

    private class FileForm extends FormFrame {

        private File file;
        private String descriptionString;
        private byte[] descriptionBytes;

        FileForm(String name, File file) {
            super(name);
            this.file = file;
        }

        @Override
        protected void makeDescription() {
            String fileName = file.getName();
            descriptionString = "--" + boundary + LINE_FEED +
                    "Content-Disposition: form-data; name=\"" + getName() + "\"; filename=\"" + fileName + "\"" +
                    LINE_FEED +
                    "Content-Type: "
                    + URLConnection.guessContentTypeFromName(fileName) +
                    LINE_FEED +
                    "Content-Transfer-Encoding: binary" + LINE_FEED +
                    LINE_FEED;
            descriptionBytes = stringToByteArray(descriptionString);
        }

        @Override
        long calculateLength() {
            makeDescription();
            return descriptionBytes.length + file.length() + LINE_FEED_BYTES.length;
        }

        @Override
        void write(OutputStream os) throws Exception {
            BufferedOutputStream outputStream = new BufferedOutputStream(os, 1024 * 8);
            outputStream.write(descriptionBytes, 0, descriptionBytes.length);
            outputStream.flush();

            BufferedInputStream inputStream = new BufferedInputStream(new FileInputStream(file), 1024 * 8);
            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
            inputStream.close();

            outputStream.write(LINE_FEED_BYTES, 0, LINE_FEED_BYTES.length);
            outputStream.flush();
        }
    }

}
