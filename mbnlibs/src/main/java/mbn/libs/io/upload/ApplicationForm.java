package mbn.libs.io.upload;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class ApplicationForm extends BaseForm {

    private final StringBuilder stringBuilder = new StringBuilder();
    private boolean readAfter = false;
    private int partsCount;
    private int responseCode;
    private String responseMessage;
    private String afterLoad;

    public ApplicationForm(String url) {
        super(url);
    }

    public ApplicationForm(String url, boolean readAfter) {
        super(url);
        this.readAfter = readAfter;
    }

    public synchronized ApplicationForm addPart(String name, String value) {
        if (partsCount > 0) {
            stringBuilder.append('&');
        }
        stringBuilder.append(name).append('=').append(value);
        partsCount++;
        return this;
    }

    public int getResponseCode() {
        return responseCode;
    }

    public String getResponseMessage() {
        return responseMessage;
    }

    public String getAfterLoad() {
        return afterLoad;
    }

    @Override
    public synchronized boolean execute() {
        HttpURLConnection urlConnection = null;
        boolean done = false;
        try {
            urlConnection = (HttpURLConnection) new URL(getUrl()).openConnection();
            urlConnection.setRequestMethod("POST");
            urlConnection.setUseCaches(false);
            urlConnection.setDoOutput(true);    // indicates POST method
            urlConnection.setDoInput(true);
            urlConnection.addRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            urlConnection.connect();

            OutputStream outputStream = urlConnection.getOutputStream();
            byte[] data = stringToByteArray(stringBuilder.toString());
            outputStream.write(data, 0, data.length);
            outputStream.flush();
            outputStream.close();

            responseCode = urlConnection.getResponseCode();
            responseMessage = urlConnection.getResponseMessage();
            if (responseCode == 200) {
                if (readAfter) {
                    afterLoad = readTheStream(urlConnection.getInputStream());
                    urlConnection.getInputStream().close();
                }
                done = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }
        return done;
    }

    private String readTheStream(InputStream inputStream) {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line;
            try {
                while ((line = reader.readLine()) != null) {
                    output.append(line);
                }
                return output.toString();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
