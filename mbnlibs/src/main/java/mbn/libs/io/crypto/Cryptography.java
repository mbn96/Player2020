package mbn.libs.io.crypto;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Proxy;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketException;
import java.net.SocketImpl;
import java.net.SocketImplFactory;
import java.net.UnknownHostException;
import java.nio.channels.SocketChannel;
import java.util.Arrays;

public class Cryptography {

    public static void encrypt(InputStream inputStream, OutputStream encryptedOutput, byte[] key) throws IOException {
        BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
        BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(encryptedOutput);

        int count = 0;
        int b;
        while ((b = bufferedInputStream.read()) != -1) {
            int encrypted = (~(b | key[count])) | (b & key[count]);
            bufferedOutputStream.write(encrypted);
            count++;
            if (count >= key.length) {
                count = 0;
            }
        }

        bufferedOutputStream.flush();
        bufferedOutputStream.close();
        bufferedInputStream.close();
    }

    public static void decrypt(InputStream encryptedInput, OutputStream decryptedOutputStream, byte[] key) throws IOException {
        BufferedInputStream bufferedInputStream = new BufferedInputStream(encryptedInput);
        BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(decryptedOutputStream);

        int count = 0;
        int b;
        while ((b = bufferedInputStream.read()) != -1) {
            int decrypted = (b & key[count]) | ~(b | key[count]);
            bufferedOutputStream.write(decrypted);
            count++;
            if (count >= key.length) {
                count = 0;
            }
        }

        bufferedOutputStream.flush();
        bufferedOutputStream.close();
        bufferedInputStream.close();
    }

    public abstract static class CryptoBase {
        private final byte[] key;
        private int position = 0;
        private int markPos;

        CryptoBase(byte[] key) {
            this.key = Arrays.copyOf(key, key.length);
        }

        public final void skip(long n) {
            long skip = n % key.length;
            for (int i = 0; i < skip; i++) {
                move();
            }
        }

        public final void mark() {
            markPos = position;
        }

        public final void resetMark() {
            position = markPos;
        }

        protected final byte move() {
            byte outByte = key[position];
            position++;
            if (position >= key.length) {
                position = 0;
            }
            return outByte;
        }

        public abstract int cryptoAction(int byteInt);

        public abstract void cryptoAction(byte[] inBytes, int offset, long length);
    }

    public static class Encrypter extends CryptoBase {

        Encrypter(byte[] key) {
            super(key);
        }

        @Override
        public int cryptoAction(int byteInt) {
            byte bp = move();
            return (~(byteInt | bp)) | (byteInt & bp);
        }

        @Override
        public void cryptoAction(byte[] inBytes, int offset, long length) {
            byte bp;
            byte enB;
            for (int i = offset; i < length + offset; i++) {
                enB = inBytes[i];
                bp = move();
                inBytes[i] = (byte) (((~(enB | bp)) | (enB & bp)) /*& 0xff*/);
            }
        }
    }

    public static class Decrypter extends CryptoBase {
        Decrypter(byte[] key) {
            super(key);
        }

        @Override
        public int cryptoAction(int byteInt) {
            byte bp = move();
            return (byteInt & bp) | ~(byteInt | bp);
        }

        @Override
        public void cryptoAction(byte[] inBytes, int offset, long length) {
            byte bp;
            byte dyB;
            for (int i = offset; i < length + offset; i++) {
                dyB = inBytes[i];
                bp = move();
                inBytes[i] = (byte) (((dyB & bp) | ~(dyB | bp)) /*& 0xff*/);
            }
        }
    }

    public enum CryptoMode {
        Encryption, Decryption
    }

    public static class CryptoInputStream extends InputStream {
        private final InputStream inStream;
        private final CryptoBase crypto;

        public CryptoInputStream(InputStream inStream, CryptoMode cryptoMode, byte[] key) {
            this.inStream = inStream;
            switch (cryptoMode) {
                case Encryption:
                    crypto = new Encrypter(key);
                    break;
                case Decryption:
                    crypto = new Decrypter(key);
                    break;
                default:
                    throw new RuntimeException("No defined mode.");
            }
        }

        @Override
        public int read() throws IOException {
            int read = inStream.read();
            if (read != -1) read = crypto.cryptoAction(read);
            return read;
        }

        @Override
        public int read(byte[] b) throws IOException {
            int read = inStream.read(b);
            if (read != -1) crypto.cryptoAction(b, 0, read);
            return read;
        }

        @Override
        public int read(byte[] b, int off, int len) throws IOException {
            int read = inStream.read(b, off, len);
            if (read != -1) crypto.cryptoAction(b, off, read);
            return read;
        }

        @Override
        public long skip(long n) throws IOException {
            long skip = inStream.skip(n);
            crypto.skip(skip);
            return skip;
        }

        @Override
        public int available() throws IOException {
            return inStream.available();
        }

        @Override
        public void close() throws IOException {
            inStream.close();
        }

        @Override
        public void mark(int readlimit) {
            inStream.mark(readlimit);
            crypto.mark();
        }

        @Override
        public void reset() throws IOException {
            inStream.reset();
            crypto.resetMark();
        }

        @Override
        public boolean markSupported() {
            return inStream.markSupported();
        }
    }

    public static class CryptoOutputStream extends OutputStream {
        private final OutputStream outStream;
        private final CryptoBase crypto;

        public CryptoOutputStream(OutputStream inStream, CryptoMode cryptoMode, byte[] key) {
            this.outStream = inStream;
            switch (cryptoMode) {
                case Encryption:
                    crypto = new Encrypter(key);
                    break;
                case Decryption:
                    crypto = new Decrypter(key);
                    break;
                default:
                    throw new RuntimeException("No defined mode.");
            }
        }

        @Override
        public void write(int b) throws IOException {
            outStream.write(crypto.cryptoAction(b));
        }

        @Override
        public void write(byte[] b) throws IOException {
            crypto.cryptoAction(b, 0, b.length);
            outStream.write(b);
        }

        @Override
        public void write(byte[] b, int off, int len) throws IOException {
            crypto.cryptoAction(b, off, len);
            outStream.write(b, off, len);
        }

        @Override
        public void flush() throws IOException {
            outStream.flush();
        }

        @Override
        public void close() throws IOException {
            outStream.close();
        }
    }


    public static class CryptoSocketWrapper extends Socket {
        private final Socket mSocket;
        private final byte[] key;
        private CryptoInputStream inputStream;
        private CryptoOutputStream outputStream;

        public CryptoSocketWrapper(byte[] key) {
            mSocket = new Socket();
            this.key = key;
        }

        public CryptoSocketWrapper(Socket mSocket, byte[] key) {
            this.mSocket = mSocket;
            this.key = key;

        }

        public CryptoSocketWrapper(Proxy proxy, byte[] key) {
            mSocket = new Socket(proxy);
            this.key = key;
        }


        public CryptoSocketWrapper(String host, int port, byte[] key) throws IOException {
            mSocket = new Socket(host, port);
            this.key = key;
        }

        public CryptoSocketWrapper(InetAddress address, int port, byte[] key) throws IOException {
            mSocket = new Socket(address, port);
            this.key = key;
        }

        public CryptoSocketWrapper(String host, int port, InetAddress localAddr, int localPort, byte[] key) throws IOException {
            mSocket = new Socket(host, port, localAddr, localPort);
            this.key = key;
        }

        public CryptoSocketWrapper(InetAddress address, int port, InetAddress localAddr, int localPort, byte[] key) throws IOException {
            mSocket = new Socket(address, port, localAddr, localPort);
            this.key = key;
        }

        @Override
        public void connect(SocketAddress endpoint) throws IOException {
            mSocket.connect(endpoint);
        }

        @Override
        public void connect(SocketAddress endpoint, int timeout) throws IOException {
            mSocket.connect(endpoint, timeout);
        }

        @Override
        public void bind(SocketAddress bindpoint) throws IOException {
            mSocket.bind(bindpoint);
        }

        @Override
        public InetAddress getInetAddress() {
            return mSocket.getInetAddress();
        }

        @Override
        public InetAddress getLocalAddress() {
            return mSocket.getLocalAddress();
        }

        @Override
        public int getPort() {
            return mSocket.getPort();
        }

        @Override
        public int getLocalPort() {
            return mSocket.getLocalPort();
        }

        @Override
        public SocketAddress getRemoteSocketAddress() {
            return mSocket.getRemoteSocketAddress();
        }

        @Override
        public SocketAddress getLocalSocketAddress() {
            return mSocket.getLocalSocketAddress();
        }

        @Override
        public SocketChannel getChannel() {
            return mSocket.getChannel();
        }

        @Override
        public InputStream getInputStream() throws IOException {
            if (inputStream == null) {
                inputStream = new CryptoInputStream(mSocket.getInputStream(), CryptoMode.Decryption, key);
            }
            return inputStream;
        }

        public InputStream getInputStream_NoCrypto() throws IOException {
            return mSocket.getInputStream();
        }

        @Override
        public OutputStream getOutputStream() throws IOException {
            if (outputStream == null) {
                outputStream = new CryptoOutputStream(mSocket.getOutputStream(), CryptoMode.Encryption, key);
            }
            return outputStream;
        }

        public OutputStream getOutputStream_NoCrypto() throws IOException {
            return mSocket.getOutputStream();
        }

        @Override
        public void setTcpNoDelay(boolean on) throws SocketException {
            mSocket.setTcpNoDelay(on);
        }

        @Override
        public boolean getTcpNoDelay() throws SocketException {
            return mSocket.getTcpNoDelay();
        }

        @Override
        public void setSoLinger(boolean on, int linger) throws SocketException {
            mSocket.setSoLinger(on, linger);
        }

        @Override
        public int getSoLinger() throws SocketException {
            return mSocket.getSoLinger();
        }

        @Override
        public void sendUrgentData(int data) throws IOException {
            mSocket.sendUrgentData(data);
        }

        @Override
        public void setOOBInline(boolean on) throws SocketException {
            mSocket.setOOBInline(on);
        }

        @Override
        public boolean getOOBInline() throws SocketException {
            return mSocket.getOOBInline();
        }

        @Override
        public void setSoTimeout(int timeout) throws SocketException {
            mSocket.setSoTimeout(timeout);
        }

        @Override
        public int getSoTimeout() throws SocketException {
            return mSocket.getSoTimeout();
        }

        @Override
        public void setSendBufferSize(int size) throws SocketException {
            mSocket.setSendBufferSize(size);
        }

        @Override
        public int getSendBufferSize() throws SocketException {
            return mSocket.getSendBufferSize();
        }

        @Override
        public void setReceiveBufferSize(int size) throws SocketException {
            mSocket.setReceiveBufferSize(size);
        }

        @Override
        public int getReceiveBufferSize() throws SocketException {
            return mSocket.getReceiveBufferSize();
        }

        @Override
        public void setKeepAlive(boolean on) throws SocketException {
            mSocket.setKeepAlive(on);
        }

        @Override
        public boolean getKeepAlive() throws SocketException {
            return mSocket.getKeepAlive();
        }

        @Override
        public void setTrafficClass(int tc) throws SocketException {
            mSocket.setTrafficClass(tc);
        }

        @Override
        public int getTrafficClass() throws SocketException {
            return mSocket.getTrafficClass();
        }

        @Override
        public void setReuseAddress(boolean on) throws SocketException {
            mSocket.setReuseAddress(on);
        }

        @Override
        public boolean getReuseAddress() throws SocketException {
            return mSocket.getReuseAddress();
        }

        @Override
        public void close() throws IOException {
            mSocket.close();
        }

        @Override
        public void shutdownInput() throws IOException {
            mSocket.shutdownInput();
        }

        @Override
        public void shutdownOutput() throws IOException {
            mSocket.shutdownOutput();
        }

        @Override
        public String toString() {
            return mSocket.toString();
        }

        @Override
        public boolean isConnected() {
            return mSocket.isConnected();
        }

        @Override
        public boolean isBound() {
            return mSocket.isBound();
        }

        @Override
        public boolean isClosed() {
            return mSocket.isClosed();
        }

        @Override
        public boolean isInputShutdown() {
            return mSocket.isInputShutdown();
        }

        @Override
        public boolean isOutputShutdown() {
            return mSocket.isOutputShutdown();
        }

        public static void setSocketImplFactory(SocketImplFactory fac) throws IOException {
            Socket.setSocketImplFactory(fac);
        }

        @Override
        public void setPerformancePreferences(int connectionTime, int latency, int bandwidth) {
            mSocket.setPerformancePreferences(connectionTime, latency, bandwidth);
        }
    }

}
