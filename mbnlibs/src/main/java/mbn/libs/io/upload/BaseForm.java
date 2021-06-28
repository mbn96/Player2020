package mbn.libs.io.upload;

import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;

public abstract class BaseForm {
    private final String url;

    public BaseForm(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public abstract boolean execute();

    public static byte[] stringToByteArray(String string) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        PrintWriter printWriter = new PrintWriter(byteArrayOutputStream, true);
        printWriter.append(string);
        printWriter.flush();
        printWriter.close();
        return byteArrayOutputStream.toByteArray();
    }
}
