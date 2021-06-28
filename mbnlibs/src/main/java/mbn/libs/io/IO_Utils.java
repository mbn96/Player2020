package mbn.libs.io;

import android.net.Uri;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class IO_Utils {

    public static boolean copy(File input, File destination) {
        if (!input.exists()) {
            return false;
        }
        if (!destination.getParentFile().exists()) {
            destination.getParentFile().mkdirs();
        }

        boolean done = false;
        try {
            BufferedInputStream bufferedInputStream = new BufferedInputStream(new FileInputStream(input));
            BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream(destination));
            int data;
            while ((data = bufferedInputStream.read()) != -1) {
                outputStream.write(data);
            }
            outputStream.flush();
            outputStream.close();
            bufferedInputStream.close();
            done = true;
        } catch (IOException ignored) {
        }
        return done;
    }

    public static boolean cut(File input, File destination) {
        boolean done = copy(input, destination);
        if (done)  //noinspection ResultOfMethodCallIgnored
            input.delete();
        if (!done) //noinspection ResultOfMethodCallIgnored
            destination.delete();

        return done;
    }

    public static boolean transfer(@NonNull InputStream inputStream, OutputStream outputStream) {
        boolean result = false;
        int data;
        try {
            while ((data = inputStream.read()) != -1) {
                outputStream.write(data);
            }
            inputStream.close();
            outputStream.flush();
            outputStream.close();
            result = true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Nullable
    public static byte[] fileToByteArray(@NonNull File input) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        if (!input.exists()) {
            throw new RuntimeException("Empty File!");
        }
        try {
            BufferedInputStream inputStream = new BufferedInputStream(new FileInputStream(input));
            BufferedOutputStream outputStream = new BufferedOutputStream(byteArrayOutputStream);
            boolean done = transfer(inputStream, outputStream);
            if (!done) return null;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
        return byteArrayOutputStream.toByteArray();
    }

    public static boolean byteArrayToFile(byte[] bytes, File dest) {
        try {
            BufferedInputStream inputStream = new BufferedInputStream(new ByteArrayInputStream(bytes));
            BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream(dest));
            return transfer(inputStream, outputStream);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static URL makeURL(String base, @Nullable UrlPart... urlParts) {
        Uri.Builder builtUri = Uri.parse(base).buildUpon();
        if (urlParts != null) {
            for (UrlPart p : urlParts) {
                builtUri.appendQueryParameter(p.paramKey, p.paramValue);
            }
        }
        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException ignored) {
        }
        return url;
    }

    public static boolean isResumable(URL url) {
        if (url == null) {
            return false;
        }
        boolean result = false;
        HttpURLConnection urlConnection = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.setRequestMethod("HEAD");
            urlConnection.connect();
            // If the request was successful (response code 200),
            // then read the input stream and parse the response.
            if (urlConnection.getResponseCode() == 200) {
                String ranges = urlConnection.getHeaderField("accept-ranges");
                if (ranges != null && !ranges.isEmpty()) result = true;
            }
        } catch (IOException ignored) {
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }
        return result;
    }

    public static boolean makeHttpRequest(URL url, OperationOnInputStream operation, String requestMethod, @Nullable HeaderInfo[] headerInfos, int resumePos) {
        if (url == null) {
            return false;
        }

        boolean canResume = false;
        if (resumePos > 0) canResume = isResumable(url);

        boolean result = false;

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;

        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.setRequestMethod(requestMethod);
            if (headerInfos != null) {
                for (HeaderInfo info : headerInfos) {
                    urlConnection.setRequestProperty(info.headerKey, info.headerValue);
                }
            }

            if (canResume) {
                urlConnection.setRequestProperty("range", "bytes=" + resumePos + "-");
            }

            urlConnection.connect();

            // If the request was successful (response code 200),
            // then read the input stream and parse the response.
            Log.i("request", "makeHttpRequest: " + urlConnection.getResponseCode() + " -- " + urlConnection.getResponseMessage());
            if (urlConnection.getResponseCode() == 200 || urlConnection.getResponseCode() == 206) {
                inputStream = urlConnection.getInputStream();

                if (canResume)
                    result = operation.read(inputStream, resumePos, urlConnection.getContentLength());
                else result = operation.read(inputStream, 0, urlConnection.getContentLength());

                inputStream.close();
            }
        } catch (IOException e) {
            Log.e("MBN I/O Utils", e.getMessage());
            e.printStackTrace();
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }
        return result;
    }

    public static boolean download(URL url, @Nullable HeaderInfo[] headerInfos, File destination, DownloadListenerBase downloadListener) {
        return makeHttpRequest(url, new DownloadOperation(destination, downloadListener), "GET", headerInfos, downloadListener.startFrom());
    }

    public static byte[] downloadToByteArray(URL url, @Nullable HeaderInfo... headerInfos) {
        ByteDownloadOperation byteDownloadOperation = new ByteDownloadOperation();
        if (makeHttpRequest(url, byteDownloadOperation, "GET", headerInfos, 0)) {
            return byteDownloadOperation.byteArrayOutputStream.toByteArray();
        }
        return null;
    }

    public static String getTextResponse(URL url, @Nullable HeaderInfo... headerInfos) {
        ReadTextOperation operation = new ReadTextOperation();
        makeHttpRequest(url, operation, "GET", headerInfos, 0);
        return operation.responseText;
    }

    private static class ReadTextOperation implements OperationOnInputStream {
        String responseText;

        @Override
        public boolean read(InputStream inputStream, int startPos, long length) {
            boolean result = false;
            StringBuilder output = new StringBuilder();
            if (inputStream != null) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
                BufferedReader reader = new BufferedReader(inputStreamReader);
                String line;
                try {
                    while ((line = reader.readLine()) != null) {
                        output.append(line);
                    }
                    responseText = output.toString();
                    result = true;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return result;
        }
    }

    private static class ByteDownloadOperation implements OperationOnInputStream {

        private ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        private BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(byteArrayOutputStream);

        @Override
        public boolean read(InputStream inputStream, int startPos, long length) {
            boolean result = false;
            try {
                BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
                int data;
                while ((data = bufferedInputStream.read()) != -1) {
                    bufferedOutputStream.write(data);
                }
                result = true;
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (bufferedOutputStream != null) {
                        bufferedOutputStream.flush();
                        bufferedOutputStream.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return result;
        }
    }

    private static class DownloadOperation implements OperationOnInputStream {
        private File destination;
        private DownloadListenerBase downloadListener;


        DownloadOperation(File destination, DownloadListenerBase downloadListener) {
            this.destination = destination;
            this.downloadListener = downloadListener;
        }

        @Override
        public boolean read(InputStream inputStream, int startPos, long length) {
            boolean result = false;

            if (!destination.getParentFile().exists()) {
                //noinspection ResultOfMethodCallIgnored
                destination.getParentFile().mkdirs();
            }

            BufferedOutputStream outputStream = null;
            try {
                FileOutputStream fileOutputStream = new FileOutputStream(destination, true);
//                fileOutputStream.getChannel().position(startPos);
                outputStream = new BufferedOutputStream(fileOutputStream);

                BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
                downloadListener.setStartPos(startPos);
                downloadListener.setLength((int) length);
                int data;
                while ((data = bufferedInputStream.read()) != -1 && !downloadListener.isCanceled()) {
                    outputStream.write(data);
                    downloadListener.nextByte();
                }
//                outputStream.flush();
//                outputStream.close();
                result = !downloadListener.isCanceled();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (outputStream != null) {
                        outputStream.flush();
                        outputStream.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return result;
        }


    }

    public static class HeaderInfo {
        private String headerKey, headerValue;

        public HeaderInfo(String headerKey, String headerValue) {
            this.headerKey = headerKey;
            this.headerValue = headerValue;
        }
    }

    public interface OperationOnInputStream {
        boolean read(InputStream inputStream, int startPos, long length);
    }

    public static abstract class DownloadListenerBase {
        private final Object LOCK = new Object();
        private boolean canceled = false;
        int startPos;
        private int length;

        boolean isCanceled() {
            synchronized (LOCK) {
                return canceled;
            }
        }

        void setCanceled(boolean canceled) {
            synchronized (LOCK) {
                this.canceled = canceled;
            }
        }

        int startFrom() {
            return startPos;
        }

        void setStartPos(int startPos) {
            this.startPos = startPos;
        }

        public int getLength() {
            return length;
        }

        public void setLength(int length) {
            this.length = length;
        }

        public abstract void nextByte();
    }

    public static class UrlPart {
        private String paramKey, paramValue;

        public UrlPart(String paramKey, String paramValue) {
            this.paramKey = paramKey;
            this.paramValue = paramValue;
        }
    }
}
