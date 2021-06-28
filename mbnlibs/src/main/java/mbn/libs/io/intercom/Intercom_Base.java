package mbn.libs.io.intercom;

import android.os.Handler;
import android.os.Looper;
import android.os.Process;
import android.util.Log;

import androidx.annotation.Nullable;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import mbn.libs.backgroundtask.BaseTaskHolder;
import mbn.libs.backgroundtask.SimpleThreadManager;
import mbn.libs.backgroundtask.ThreadManager;
import mbn.libs.io.codecs.Codecs;
import mbn.libs.io.json.MbnJSON;
import mbn.libs.utils.JavaUtils;
import mbn.libs.utils.LinkedBlockingQueue_noException;

public class Intercom_Base {
    private static final byte MANAGEMENT = 0x10;
    private static final int MANAGEMENT_types = 1;
    private static final int MANAGEMENT_ARE_U_THERE = MANAGEMENT_types << 1;
    private static final int CHECK_THEM = MANAGEMENT_types << 2;
    private static final int CONNECT = MANAGEMENT_types << 3;
    private static final int DISCONNECT = MANAGEMENT_types << 4;

    static final byte INTRODUCE_YOURSELF = 0;
    //    static final byte INTRODUCE_TO_ADVERTISER = 1;
    private static final byte THIS_IS_ME = 6;
    //    private static final byte INTRODUCE_TO_DISCOVERER = 2;
//    private static final byte DISCONNECT_PEERS = 5;
    private static final byte MESSAGE_SIMPLE = 3;
    private static final byte MESSAGE_FILE = 4;
    private static final byte MESSAGE_SIMULTANEOUS = 11;
    private final static String RESULT_OK = "Result ok 200.";
    private final static String RESULT_FALSE = "Result 400.";
    private static final byte[] RESULT_OK_BYTES = RESULT_OK.getBytes();
    private static final byte[] RESULT_FALSE_BYTES = RESULT_FALSE.getBytes();

    private Handler mainHandler;
    private List<Peer> peersList = Collections.synchronizedList(new ArrayList<>());
    private List<Peer> ignoreList = Collections.synchronizedList(new ArrayList<>());
    private final ServerSocket serverSocket;
    private final String userNickName;
    private final Peer self_peer;
    private final String serviceName;
    private ListeningThread listeningThread;
    private SimpleThreadManager queueThreadManager = new SimpleThreadManager(true);
    private final File downloadDir;
    private ThreadManager sendingThreadManager;
    private ThreadManager processManager;
    private ThreadManager managementThreadManager = new ThreadManager(3, true);
    private InterComListener listener;
    private boolean isStop = false;
    private int peersTillShutDown;
    protected boolean isFirst;
    private int peersCheckInterval;
    private final LinkedBlockingQueue_noException<Socket> socketQueue = new LinkedBlockingQueue_noException<>();
    private Runnable checkRunnable;


    private final BaseTaskHolder.ResultReceiver incomingReceiver = (result, info) -> {
        if (result instanceof ManagementResultHolder) {
            ((ManagementResultHolder) result).manage((ManagementResultHolder) result);
            return;
        }
        byte type = (byte) info;
        if (type == MANAGEMENT) {
            return;
        }
        switch (type) {
//            case INTRODUCE_TO_ADVERTISER:
//                if (addToPeers((Peer) result)) {
//                    internalSend(new SendingMessage(INTRODUCE_TO_DISCOVERER, (Peer) result, "", null, null, null));
//                }
//                break;
            case INTRODUCE_YOURSELF:
                internalSend(new SendingMessage(THIS_IS_ME, (Peer) result, "", null, null, null));
                break;
            case THIS_IS_ME:
                introductionArrived((Peer) result);
                break;
//            case INTRODUCE_TO_DISCOVERER:
//                addToPeers((Peer) result);
//                break;
//            case DISCONNECT_PEERS:
//                removePeer((Peer) result);
//                break;
            case MESSAGE_SIMULTANEOUS:
            case MESSAGE_SIMPLE:
            case MESSAGE_FILE:
                Object[] objects = (Object[]) result;
                //noinspection SuspiciousMethodCalls
                if (peersList.contains(objects[0])) {
                    sendToListeners(objects, type);
                }
                break;
        }
    };

    private final BaseTaskHolder.ResultReceiver sendingResults = new BaseTaskHolder.ResultReceiver() {
        @Override
        public void onResult(Object result, Object info) {
            SendingMessage message = (SendingMessage) result;
            boolean s = (boolean) info;
            if (s && (message.type == MESSAGE_SIMPLE || message.type == MESSAGE_FILE || message.type == MESSAGE_SIMULTANEOUS) && listener != null) {
                listener.onMessageSent(message.receiver, message.getProceededMessage(), message.file, message.startNanoTime, message.executionTime);
            }
//            if (isStop && s && message.type == DISCONNECT_PEERS) {
//                peersTillShutDown--;
//                if (peersTillShutDown <= 0) {
//                    sendingThreadManager.shutDown();
//                }
//            }
        }
    };

    /**
     * @param userNickName     A Name to be shown to the peers.
     * @param serviceName      The Service Name for discovery.
     * @param receivedFilesDir The directory to save received files in.
     * @param processThreads   Number of Threads that process incoming connections.
     * @param sendingThreads   Number of Threads that process outgoing messages.
     * @param checkInterval    Interval in which service should check for peers validity. put 0 (zero) for no validation checks.
     * @throws IOException if something goes wrong in creating the server socket.
     */
    Intercom_Base(String userNickName, String serviceName, File receivedFilesDir, int processThreads, int sendingThreads, int checkInterval) throws IOException {
        serverSocket = new ServerSocket(0);
        this.userNickName = userNickName;
        this.serviceName = serviceName;
        self_peer = new Peer(serverSocket.getLocalPort(), serverSocket.getInetAddress(), userNickName);
        downloadDir = receivedFilesDir;
        if (!downloadDir.exists()) {
            downloadDir.mkdirs();
        }
        mainHandler = new Handler(Looper.getMainLooper());
        if (checkInterval > 0) {
            peersCheckInterval = checkInterval;
            mainHandler.postDelayed(checkRunnable = new Runnable() {
                @Override
                public void run() {
                    managementThreadManager.getTaskHolder().StartTask(new ManagementProcessor(CHECK_THEM, copyPeersList()), incomingReceiver);
                    mainHandler.postDelayed(this, peersCheckInterval);
                }
            }, peersCheckInterval);
        }
        processManager = new ThreadManager(processThreads, false);
        sendingThreadManager = new ThreadManager(sendingThreads, false);
        listeningThread = new ListeningThread(serviceName + "_listen");

        queueThreadManager.submit(queuingThread, 2);
    }

    public void stop() {
        isStop = true;
        queueThreadManager.interruptAll();
        mainHandler.removeCallbacks(checkRunnable);
        try {
            serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        peersTillShutDown = peersList.size();
        for (Peer p : peersList) {
            disconnectPeer(p);
        }
        mainHandler.postDelayed(() -> sendingThreadManager.shutDown(), 1000);
        mainHandler.postDelayed(() -> processManager.shutDown(), 1000);
        mainHandler.postDelayed(() -> managementThreadManager.shutDown(), 1000);
    }

    public String getServiceName() {
        return serviceName;
    }

    protected Peer getSelf_peer() {
        return self_peer;
    }

    public InterComListener getListener() {
        return listener;
    }

    public void setListener(InterComListener listener) {
        this.listener = listener;
    }

    public void tryConnectingToPeer(Peer peer) {
//        internalSend(new SendingMessage(INTRODUCE_TO_ADVERTISER, peer, "", null, null, null));
        managementThreadManager.getTaskHolder().StartTask(new ManagementProcessor(CONNECT, peer), incomingReceiver);
    }

    private boolean addToPeers(Peer peer) {
        if (!peersList.contains(peer)) {
            peersList.add(peer);
            if (listener != null) {
                listener.onAddedToPeers(peer, getPeersList());
            }
            return true;
        }
        return false;
    }

    private boolean removePeer(Peer peer) {
        if (listener != null) {
            listener.onRemovedFromPeers(peer, getPeersList());
        }
        return peersList.remove(peer);
    }

    private void sendToListeners(Object[] message, byte type) {
        if (listener == null) {
            return;
        }
        UserMessage userMessage = null;
        switch (type) {
            case MESSAGE_SIMULTANEOUS:
                userMessage = new UserMessage((String) message[1], null);
                userMessage.startNanoTime = (long) message[3];
                userMessage.executionTime = (long) message[4];
                break;
            case MESSAGE_SIMPLE:
                userMessage = new UserMessage((String) message[1], null);
                break;
            case MESSAGE_FILE:
                userMessage = new UserMessage((String) message[1], (File) message[2]);
                break;
        }
        if (userMessage != null) {
            listener.onNewMessage((Peer) message[0], userMessage);
        }
    }

    public List<Peer> getPeersList() {
        return peersList;
    }

    protected List<Peer> copyPeersList() {
        return new ArrayList<>(peersList);
    }

    protected void introductionArrived(Peer peer) {
    }

    protected void internalSend(SendingMessage message) {
        sendingThreadManager.getTaskHolder().StartTask(new Sender(message), sendingResults);
    }

    public void sendMessage(Peer receiver, String message) {
        internalSend(new SendingMessage(MESSAGE_SIMPLE, receiver, message, null, null, null));
    }

    public void sendMessage(Peer receiver, MbnJSON.JsonObjectBuilder message) {
        internalSend(new SendingMessage(MESSAGE_SIMPLE, receiver, message, null, null, null));
    }

    public void sendMessage(Peer receiver, String message, String fileName, String fileExtension, File file) {
        internalSend(new SendingMessage(MESSAGE_FILE, receiver, message, fileName, fileExtension, file));
    }

    public void sendMessage(Peer receiver, MbnJSON.JsonObjectBuilder message, String fileName, String fileExtension, File file) {
        internalSend(new SendingMessage(MESSAGE_FILE, receiver, message, fileName, fileExtension, file));
    }

    public void sendSimultaneousMessage(List<Peer> receivers, String message) {
        sendingThreadManager.getTaskHolder().StartTask(new SimultaneousSender(receivers,
                new SendingMessage(MESSAGE_SIMULTANEOUS, null, message, null, null, null)), sendingResults/*, Process.THREAD_PRIORITY_AUDIO*/);
    }

    public void sendSimultaneousMessage(List<Peer> receivers, MbnJSON.JsonObjectBuilder message) {
        sendingThreadManager.getTaskHolder().StartTask(new SimultaneousSender(receivers,
                new SendingMessage(MESSAGE_SIMULTANEOUS, null, message, null, null, null)), sendingResults/*, Process.THREAD_PRIORITY_AUDIO*/);
    }

    public void disconnectPeer(Peer peer) {
//        internalSend(new SendingMessage(DISCONNECT_PEERS, peer, "", null, null, null));
        managementThreadManager.getTaskHolder().StartTask(new ManagementProcessor(DISCONNECT, peer), incomingReceiver);
    }

    private final Runnable queuingThread = () -> {
        while (!isStop) {
            Socket socket = socketQueue.take();
            if (socket != null) {
                try {
                    socket.setSoTimeout(5_000);
                    byte b = (byte) socket.getInputStream().read();
//                    Log.i("QUEUE", "onRun: " + b);
                    if (b == MANAGEMENT) {
                        int type = JavaUtils.readIntFromStream(socket.getInputStream());
                        managementThreadManager.getTaskHolder().StartTask(new ManagementProcessor(type, socket), incomingReceiver);
                    } else {
                        processManager.getTaskHolder().StartTask(new Processor(socket, b), incomingReceiver);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    try {
                        socket.close();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        }
    };

    private class ManagementResultHolder {
        private int type;

        public final void manage(ManagementResultHolder holder) {
            switch (holder.type) {
                case CHECK_THEM:
                    PeersCheckResult checkResult = (PeersCheckResult) holder;
                    for (Peer p : checkResult.removePeers) {
                        if (!ignoreList.contains(p))
                            removePeer(p);
                    }
                    break;
                case CONNECT:
                    ConnectResult connectResult = (ConnectResult) holder;
                    addToPeers(connectResult.peer);
                    mainHandler.postDelayed(() -> ignoreList.remove(connectResult.peer), 3000);
                    break;
                case DISCONNECT:
                    DisconnectResult disconnectResult = (DisconnectResult) holder;
                    removePeer(disconnectResult.peer);
                    ignoreList.remove(disconnectResult.peer);
                    break;
            }
        }

        public ManagementResultHolder(int type) {
            this.type = type;
        }

        public int getType() {
            return type;
        }
    }

    private class PeersCheckResult extends ManagementResultHolder {
        private ArrayList<Peer> removePeers = new ArrayList<>();

        PeersCheckResult() {
            super(CHECK_THEM);

        }

        void add(Peer peer) {
            if (!removePeers.contains(peer))
                removePeers.add(peer);
        }
    }

    private class ConnectResult extends ManagementResultHolder {
        private volatile Peer peer;

        ConnectResult(Peer peer) {
            super(CONNECT);
            this.peer = peer;
        }

    }

    private class DisconnectResult extends ManagementResultHolder {
        private volatile Peer peer;

        DisconnectResult(Peer peer) {
            super(DISCONNECT);
            this.peer = peer;
        }

    }


    private class ManagementProcessor implements BaseTaskHolder.BaseTask {
        private volatile Socket clientSocket;
        private volatile int type;
        private volatile List<Peer> peers;
        private volatile Peer peer;

        public ManagementProcessor(int type, Peer peer) {
            this.type = type;
            this.peer = peer;
        }

        public ManagementProcessor(int type, List<Peer> peers) {
            this.peers = peers;
            this.type = type;
        }

        public ManagementProcessor(int type, Socket clientSocket) {
            this.clientSocket = clientSocket;
            this.type = type;
        }

        @Override
        public Object onRun() {
            switch (type) {
                case CHECK_THEM:
                    PeersCheckResult checkResult = new PeersCheckResult();
                    for (Peer p : peers) {
                        try {
                            if (!ignoreList.contains(p) && !check(p)) {
                                checkResult.add(p);
//                                Log.i("REMOVE_PEER", "onRun: " + p.userNickName + " - " + Thread.currentThread().getName());
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            checkResult.add(p);
                        }
                    }
                    return checkResult;
                case MANAGEMENT_ARE_U_THERE:
                    try {
                        InputStream inputStream = clientSocket.getInputStream();
                        OutputStream outputStream = clientSocket.getOutputStream();
                        int length = JavaUtils.readIntFromStream(inputStream);
                        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                        JavaUtils.copyStream(inputStream, byteArrayOutputStream, length, length);
                        String incomeMsg = new String(byteArrayOutputStream.toByteArray());
                        byteArrayOutputStream.close();
                        try {
                            JSONObject jsonObject = new JSONObject(incomeMsg);
                            String hostS = jsonObject.getString("host_address");
                            int port = jsonObject.getInt("server_port");
                            String userNname = jsonObject.getString("user_nick_name");
                            Peer peer = new Peer(port, InetAddress.getByName(hostS), userNname);
//                            Log.i("PEER_CHECK", "onRun: " + peer.userNickName+ " - " + Thread.currentThread().getName());
                            if (peersList.contains(peer)) {
                                outputStream.write(0xaa);
                            } else {
                                outputStream.write(0);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    } finally {
                        try {
                            clientSocket.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    break;
                case CONNECT:
                    if (clientSocket == null) {
                        return connect_outGoing(peer);
                    } else {
                        return connect_incoming();
                    }
                case DISCONNECT:
                    if (clientSocket == null) {
                        return disconnect_outGoing(peer);
                    } else {
                        return disconnect_incoming();
                    }
            }
            return null;
        }

        private boolean check(Peer peer) {
            Socket socket = null;
            try {
                socket = new Socket(peer.hostAddress, peer.serverPort);
                socket.setSoTimeout(5_000);
                InputStream inputStream = socket.getInputStream();
                OutputStream outputStream = socket.getOutputStream();
                outputStream.write(MANAGEMENT);
                byte[] outBytes = self_peer.getJSON().getBytes();
                JavaUtils.writeIntToStream(outputStream, MANAGEMENT_ARE_U_THERE, outBytes.length);
                outputStream.write(outBytes);
                outputStream.flush();
                byte response = (byte) inputStream.read();
                if (response == (byte) 0xaa) {
                    return true;
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (socket != null) {
                        socket.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return false;
        }

        @Nullable
        private ConnectResult connect_outGoing(Peer peer) {
            Socket socket = null;
            try {
                ignoreList.add(peer);
                socket = new Socket(peer.hostAddress, peer.serverPort);
                socket.setSoTimeout(5_000);
                InputStream inputStream = socket.getInputStream();
                OutputStream outputStream = socket.getOutputStream();
                outputStream.write(MANAGEMENT);
                byte[] outBytes = self_peer.getJSON().getBytes();
                JavaUtils.writeIntToStream(outputStream, CONNECT, outBytes.length);
                outputStream.write(outBytes);
                outputStream.flush();
                byte response = (byte) inputStream.read();
                if (response == (byte) 0xaa) {
                    return new ConnectResult(peer);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (socket != null) {
                        socket.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Nullable
        private ConnectResult connect_incoming() {
            try {
                InputStream inputStream = clientSocket.getInputStream();
                OutputStream outputStream = clientSocket.getOutputStream();
                int length = JavaUtils.readIntFromStream(inputStream);
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                JavaUtils.copyStream(inputStream, byteArrayOutputStream, length, length);
                String incomeMsg = new String(byteArrayOutputStream.toByteArray());
                byteArrayOutputStream.close();
                try {
                    JSONObject jsonObject = new JSONObject(incomeMsg);
                    String hostS = jsonObject.getString("host_address");
                    int port = jsonObject.getInt("server_port");
                    String userNname = jsonObject.getString("user_nick_name");
                    Peer peer = new Peer(port, InetAddress.getByName(hostS), userNname);
                    ignoreList.add(peer);
                    outputStream.write(0xaa);
                    return new ConnectResult(peer);
                } catch (JSONException e) {
                    e.printStackTrace();
                    outputStream.write(0);

                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    clientSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        private DisconnectResult disconnect_outGoing(Peer peer) {
            Socket socket = null;
            try {
                socket = new Socket(peer.hostAddress, peer.serverPort);
                socket.setSoTimeout(5_000);
                OutputStream outputStream = socket.getOutputStream();
                outputStream.write(MANAGEMENT);
                byte[] outBytes = self_peer.getJSON().getBytes();
                JavaUtils.writeIntToStream(outputStream, DISCONNECT, outBytes.length);
                outputStream.write(outBytes);
                outputStream.flush();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (socket != null) {
                        socket.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return new DisconnectResult(peer);
        }

        @Nullable
        private DisconnectResult disconnect_incoming() {
            try {
                InputStream inputStream = clientSocket.getInputStream();
                int length = JavaUtils.readIntFromStream(inputStream);
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                JavaUtils.copyStream(inputStream, byteArrayOutputStream, length, length);
                String incomeMsg = new String(byteArrayOutputStream.toByteArray());
                byteArrayOutputStream.close();
                try {
                    JSONObject jsonObject = new JSONObject(incomeMsg);
                    String hostS = jsonObject.getString("host_address");
                    int port = jsonObject.getInt("server_port");
                    String userNname = jsonObject.getString("user_nick_name");
                    Peer peer = new Peer(port, InetAddress.getByName(hostS), userNname);
                    return new DisconnectResult(peer);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    clientSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        public Object getInfo() {
            return MANAGEMENT;
        }
    }


    private class ListeningThread extends Thread {
        private ListeningThread(String name) {
            super(name);
            start();
        }

        @Override
        public void run() {
            isFirst = true;
            while (!serverSocket.isClosed()) {
                try {
                    final Socket clientSocket = serverSocket.accept();
                    if (isFirst) {
                        self_peer.hostAddress = clientSocket.getLocalAddress();
                        isFirst = false;
                    }
                    socketQueue.tryPut(clientSocket);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            processManager.shutDown();
        }
    }

    private class Processor implements BaseTaskHolder.BaseTask {
        private final Socket clientSocket;
        private volatile byte messageType = -1;
        private long sleepTime = 0;
        private long sNanoT;
        private volatile byte mType;

        public Processor(Socket clientSocket, byte mType) {
            this.clientSocket = clientSocket;
            this.mType = mType;
        }

        @Override
        public Object onRun() {
            try {
                InputStream inputStream = clientSocket.getInputStream();
                switch (mType) {
                    case INTRODUCE_YOURSELF:
                    case THIS_IS_ME:
//                    case INTRODUCE_TO_ADVERTISER:
//                    case INTRODUCE_TO_DISCOVERER:
//                    case DISCONNECT_PEERS:
                        Peer peer = introductionHandling(inputStream);
                        if (peer == null) {
                            clientSocket.getOutputStream().write(RESULT_FALSE_BYTES);
                            clientSocket.getOutputStream().flush();
                            return null;
                        }
                        clientSocket.getOutputStream().write(RESULT_OK_BYTES);
                        clientSocket.getOutputStream().flush();
                        messageType = mType;
                        if (!peer.hostAddress.equals(clientSocket.getInetAddress())) {
                            peer.hostAddress = clientSocket.getInetAddress();
                        }
                        return peer;
                    case MESSAGE_SIMPLE:
                        Object[] message = simpleMessageHandling(inputStream);
                        if (message == null) {
                            clientSocket.getOutputStream().write(RESULT_FALSE_BYTES);
                            clientSocket.getOutputStream().flush();
                            return null;
                        }
                        clientSocket.getOutputStream().write(RESULT_OK_BYTES);
                        clientSocket.getOutputStream().flush();
                        messageType = mType;
                        return message;
                    case MESSAGE_FILE:
                        Object[] message_file = fileMessageHandling(inputStream);
                        if (message_file == null) {
                            clientSocket.getOutputStream().write(RESULT_FALSE_BYTES);
                            clientSocket.getOutputStream().flush();
                            return null;
                        }
                        clientSocket.getOutputStream().write(RESULT_OK_BYTES);
                        clientSocket.getOutputStream().flush();
                        messageType = mType;
                        return message_file;
                    case MESSAGE_SIMULTANEOUS:
                        Object[] message_simul = simultaneousMessageHandling(clientSocket);
                        if (message_simul == null) {
                            clientSocket.getOutputStream().write(0xaa);
                            clientSocket.getOutputStream().flush();
                            return null;
                        }
                        messageType = mType;
                        return message_simul;
                }

            } catch (IOException e) {
                e.printStackTrace();
                try {
                    clientSocket.getOutputStream().write(RESULT_FALSE_BYTES);
                    clientSocket.getOutputStream().flush();
                    clientSocket.getOutputStream().close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            } finally {
                try {
                    clientSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        private Peer introductionHandling(InputStream inputStream) throws IOException {
            int descriptionLength_int = JavaUtils.readIntFromStream(inputStream);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(JavaUtils.readByteArrayFromStream(inputStream, descriptionLength_int))));

            StringBuilder stringBuilder = new StringBuilder();
            String s = null;
            while ((s = bufferedReader.readLine()) != null) {
                stringBuilder.append(s);
            }
            try {
                JSONObject jsonObject = new JSONObject(stringBuilder.toString());
                String hostS = jsonObject.getString("host_address");
                int port = jsonObject.getInt("server_port");
                String userNname = jsonObject.getString("user_nick_name");
                return new Peer(port, InetAddress.getByName(hostS), userNname);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        private Object[] simpleMessageHandling(InputStream inputStream) throws IOException {
            int descriptionLength_int = JavaUtils.readIntFromStream(inputStream);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(JavaUtils.readByteArrayFromStream(inputStream, descriptionLength_int))));
            StringBuilder stringBuilder = new StringBuilder();
            String s = null;
            while ((s = bufferedReader.readLine()) != null) {
                stringBuilder.append(s);
            }
            try {
                JSONObject jsonObject = new JSONObject(stringBuilder.toString());
                JSONObject peerJson = jsonObject.getJSONObject("peer");
                String hostS = peerJson.getString("host_address");
                int port = peerJson.getInt("server_port");
                String userNname = peerJson.getString("user_nick_name");
                Peer peer = new Peer(port, InetAddress.getByName(hostS), userNname);

                String message;
                JSONObject messageJson = jsonObject.optJSONObject("message");
                if (messageJson != null) {
                    message = messageJson.toString();
                } else {
                    message = jsonObject.optString("message");
                }

                return new Object[]{peer, message};
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        private Object[] simultaneousMessageHandling(Socket clientS) throws IOException {
            Process.setThreadPriority(Process.THREAD_PRIORITY_AUDIO);
            int descriptionLength_int = JavaUtils.readIntFromStream(clientS.getInputStream());
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(JavaUtils.readByteArrayFromStream(clientS.getInputStream(), descriptionLength_int))));
            StringBuilder stringBuilder = new StringBuilder();
            String s = null;
            while ((s = bufferedReader.readLine()) != null) {
                stringBuilder.append(s);
            }
            try {
                JSONObject jsonObject = new JSONObject(stringBuilder.toString());
                JSONObject peerJson = jsonObject.getJSONObject("peer");
                String hostS = peerJson.getString("host_address");
                int port = peerJson.getInt("server_port");
                String userNname = peerJson.getString("user_nick_name");
                Peer peer = new Peer(port, InetAddress.getByName(hostS), userNname);

                String message;
                JSONObject messageJson = jsonObject.optJSONObject("message");
                if (messageJson != null) {
                    message = messageJson.toString();
                } else {
                    message = jsonObject.optString("message");
                }


                Object[] outMessage = new Object[]{peer, message, null, null, null};

//                long nt1 = System.nanoTime();
//                clientS.getInetAddress().isReachable(1500);
//                clientS.getInetAddress().isReachable(1500);
//                clientS.getInetAddress().isReachable(1500);
//                clientS.getInetAddress().isReachable(1500);
//                long nt2 = System.nanoTime();
//
//                long ping = ((nt2 - nt1) / 1_000_000) / 4;
//
//                Log.i("PING", "simultaneousMessageHandling: " + ping);

//                clientS.setTcpNoDelay(true);

                sNanoT = System.nanoTime();
                JavaUtils.writeIntToStream(clientS.getOutputStream(), 1000);
                sleepTime = JavaUtils.readIntFromStream(clientS.getInputStream());
                long tE = System.nanoTime();
                long ping = ((tE - sNanoT) / 1_000_000) / 2;
                sleepTime -= ping;
                sNanoT = tE;

//                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
//                clientS.getOutputStream().write(0xaa);
//                clientS.getOutputStream().flush();
//                JavaUtils.copyStream(clientS.getInputStream(), byteArrayOutputStream, 4, 4);
//                sNanoT = System.nanoTime();
//                sleepTime = JavaUtils.byteArrayToInt(byteArrayOutputStream.toByteArray()) - (ping);
//                byteArrayOutputStream.close();


                outMessage[3] = sNanoT;
                outMessage[4] = sleepTime;

                return outMessage;
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        private Object[] fileMessageHandling(InputStream inputStream) throws IOException {
            int descriptionLength_int = JavaUtils.readIntFromStream(inputStream);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(JavaUtils.readByteArrayFromStream(inputStream, descriptionLength_int))));
            StringBuilder stringBuilder = new StringBuilder();
            String s = null;
            while ((s = bufferedReader.readLine()) != null) {
                stringBuilder.append(s);
            }
            try {
                JSONObject jsonObject = new JSONObject(stringBuilder.toString());
                JSONObject peerJson = jsonObject.getJSONObject("peer");
                String hostS = peerJson.getString("host_address");
                int port = peerJson.getInt("server_port");
                String userNname = peerJson.getString("user_nick_name");
                Peer peer = new Peer(port, InetAddress.getByName(hostS), userNname);

                String message;
                JSONObject messageJson = jsonObject.optJSONObject("message");
                if (messageJson != null) {
                    message = messageJson.toString();
                } else {
                    message = jsonObject.optString("message");
                }

                JSONObject fileDescriptionJson = jsonObject.getJSONObject("file");
                String fileName = fileDescriptionJson.getString("name");
                String fileExtension = fileDescriptionJson.getString("extension");
                long fileSize = fileDescriptionJson.getLong("size");

                final File outPutFile = new File(downloadDir, fileName + '.' + fileExtension);
                BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
                BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream(outPutFile));
                JavaUtils.copyStream(bufferedInputStream, outputStream, fileSize, 100_000);
                outputStream.close();
                return new Object[]{peer, message, outPutFile};
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        public Object getInfo() {
            return messageType;
        }
    }

    private class Sender implements BaseTaskHolder.BaseTask {
        private final SendingMessage message;
        private volatile boolean success = false;

        public Sender(SendingMessage message) {
            this.message = message;
        }

        @Override
        public Object onRun() {
            Socket sendSocket = null;
            try {
                sendSocket = new Socket(message.receiver.hostAddress, message.receiver.serverPort);
                OutputStream outputStream = sendSocket.getOutputStream();
                outputStream.write(message.type);
                switch (message.type) {
                    case INTRODUCE_YOURSELF:
                    case THIS_IS_ME:
//                    case INTRODUCE_TO_ADVERTISER:
//                    case INTRODUCE_TO_DISCOVERER:
//                    case DISCONNECT_PEERS:
                        BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(outputStream);
                        byte[] outBytes = self_peer.getJSON().getBytes();
                        JavaUtils.writeIntToStream(bufferedOutputStream, outBytes.length);
                        bufferedOutputStream.write(outBytes);
                        bufferedOutputStream.flush();
                        break;
                    case MESSAGE_SIMPLE:
                        sendSimpleMessage(outputStream);
                        break;
                    case MESSAGE_FILE:
                        sendFileMessage(outputStream);
                        break;
                }
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(sendSocket.getInputStream()));
                StringBuilder stringBuilder = new StringBuilder();
                String s = null;
                while ((s = bufferedReader.readLine()) != null) {
                    stringBuilder.append(s);
                }
                success = RESULT_OK.equals(stringBuilder.toString());
            } catch (IOException e) {
                e.printStackTrace();
                success = false;
            } finally {
                if (sendSocket != null) {
                    try {
                        sendSocket.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            return message;
        }

        private void sendSimpleMessage(OutputStream outputStream) throws IOException {
            BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(outputStream);
            MbnJSON.JsonObjectBuilder builder = new MbnJSON.JsonObjectBuilder();
            builder.putJsonObject("peer", self_peer.getJSON_builder());
            message.putMessageIn(builder);
            String outString = builder.Build();
            byte[] outBytes = outString.getBytes();
            JavaUtils.writeIntToStream(bufferedOutputStream, outBytes.length);
            bufferedOutputStream.write(outBytes);
            bufferedOutputStream.flush();
        }

        private void sendFileMessage(OutputStream outputStream) throws IOException {
            BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(outputStream);
            MbnJSON.JsonObjectBuilder builder = new MbnJSON.JsonObjectBuilder();
            builder.putJsonObject("peer", self_peer.getJSON_builder());
            message.putMessageIn(builder);
            builder.putJsonObject("file", new MbnJSON.JsonObjectBuilder()
                    .putString("name", message.fileName)
                    .putString("extension", message.fileExtension)
                    .putLong("size", message.file.length())).Build();
            String outString = builder.Build();
            byte[] description = outString.getBytes();
            JavaUtils.writeIntToStream(bufferedOutputStream, description.length);
            bufferedOutputStream.write(description);
            bufferedOutputStream.flush();

            BufferedInputStream inputStream = new BufferedInputStream(new FileInputStream(message.file));
            byte[] buffer = new byte[8 * 1024];
            int countRead = 0;
            while ((countRead = inputStream.read(buffer)) != -1) {
                bufferedOutputStream.write(buffer, 0, countRead);
            }
            bufferedOutputStream.flush();
            inputStream.close();
        }

        @Override
        public Object getInfo() {
            return success;
        }
    }

    private class SimultaneousSender implements BaseTaskHolder.BaseTask {

        private final List<Peer> receivers;
        private final SendingMessage message;
        private volatile boolean success = false;
        private volatile byte[] outBytes;
        private volatile byte[] messageLength;
        private volatile long executeTime;
        private volatile long sNanoT;


        SimultaneousSender(List<Peer> receivers, SendingMessage message) {
            this.receivers = receivers;
            this.message = message;
        }


        @Override
        public Object onRun() {

            MbnJSON.JsonObjectBuilder builder = new MbnJSON.JsonObjectBuilder();
            builder.putJsonObject("peer", self_peer.getJSON_builder());
            message.putMessageIn(builder);
            String outString = builder.Build();
            outBytes = outString.getBytes();
            messageLength = new byte[4];
            Codecs.getIntBytes(messageLength, outBytes.length);

            executeTime = 1000;
            sNanoT = System.nanoTime();

//            ArrayList<Thread> threads = new ArrayList<>();
            for (Peer p : receivers) {
                try {
//                    Socket socket = new Socket(p.hostAddress, p.serverPort);

                    Socket socket = new Socket();
                    socket.setTrafficClass(24);
                    socket.connect(new InetSocketAddress(p.hostAddress, p.serverPort));


                    new SendRun(socket).run();
                    /*
                    Thread t = new Thread(new SendRun(socket));
                    threads.add(t);
                    t.start();
                     */
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            success = true;
            SendingMessage outMessage = new SendingMessage(MESSAGE_SIMULTANEOUS, null, message.message, message.jsonMsg, null, null, null);
            outMessage.startNanoTime = sNanoT;
            outMessage.executionTime = (int) executeTime;
            return outMessage;
        }

        private void send(Socket socket) throws IOException {
            BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(socket.getOutputStream());
            MbnJSON.JsonObjectBuilder builder = new MbnJSON.JsonObjectBuilder();
            builder.putJsonObject("peer", self_peer.getJSON_builder());
            message.putMessageIn(builder);
            String outString = builder.Build();
            byte[] outBytes = outString.getBytes();
            byte[] messageLength = new byte[4];
            Codecs.getIntBytes(messageLength, outBytes.length);

            bufferedOutputStream.write(MESSAGE_SIMULTANEOUS);
            bufferedOutputStream.write(messageLength);
            bufferedOutputStream.write(outBytes);
            bufferedOutputStream.flush();

        }

        private class SendRun implements Runnable {
            private volatile Socket socket;

            SendRun(Socket socket) {
                this.socket = socket;
            }

            @Override
            public void run() {
                try {
                    BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(socket.getOutputStream());
                    bufferedOutputStream.write(MESSAGE_SIMULTANEOUS);
                    bufferedOutputStream.write(messageLength);
                    bufferedOutputStream.write(outBytes);
                    bufferedOutputStream.flush();

//                    JavaUtils.writeIntToStream(socket.getOutputStream(), 1000);

//                    int byte_int = socket.getInputStream().read();
//                    JavaUtils.writeLongToStream(bufferedOutputStream, System.currentTimeMillis());

//                    socket.setTcpNoDelay(true);
                    JavaUtils.readIntFromStream(socket.getInputStream());
                    JavaUtils.writeIntToStream(socket.getOutputStream(), (int) (executeTime - ((System.nanoTime() - sNanoT) / 1000_000)));
                    socket.getOutputStream().flush();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        socket.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        @Override
        public Object getInfo() {
            return success;
        }
    }

    public class Peer {
        private int serverPort;
        InetAddress hostAddress;
        private String userNickName;

        Peer(int serverPort, InetAddress hostAddress, String userNickName) {
            this.serverPort = serverPort;
            this.hostAddress = hostAddress;
            this.userNickName = userNickName;
        }

        public int getServerPort() {
            return serverPort;
        }

        public InetAddress getHostAddress() {
            return hostAddress;
        }

        public String getUserNickName() {
            return userNickName;
        }

        private String getJSON() {
            return getJSON_builder().Build();
        }

        private MbnJSON.JsonObjectBuilder getJSON_builder() {
            return new MbnJSON.JsonObjectBuilder().putString("host_address", hostAddress.getHostAddress())
                    .putInt("server_port", serverPort).putString("user_nick_name", userNickName);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Peer)) return false;
            Peer peer = (Peer) o;
            return serverPort == peer.serverPort &&
                    Objects.equals(hostAddress, peer.hostAddress) &&
                    Objects.equals(userNickName, peer.userNickName);
        }

        @Override
        public int hashCode() {
            return Objects.hash(serverPort, hostAddress, userNickName);
        }


        //        @Override
//        public boolean equals(@Nullable Object obj) {
//            if (this == obj) {
//                return true;
//            }
//            if (obj instanceof Peer) {
//                return (serverPort == ((Peer) obj).serverPort) && (userNickName.equals(((Peer) obj).userNickName)) && hostAddress.getHostAddress().equals(((Peer) obj).hostAddress.getHostAddress());
//            }
//            return false;
//        }
//
//        @Override
//        public int hashCode() {
//            return hostAddress.getHostAddress().hashCode();
//        }
    }

    class SendingMessage {
        private byte type;
        private Peer receiver;
        private String message;
        private MbnJSON.JsonObjectBuilder jsonMsg;
        private String fileName, fileExtension;
        private File file;
        private volatile long startNanoTime;
        private volatile long executionTime;

        SendingMessage(byte type, Peer receiver, String message, MbnJSON.JsonObjectBuilder jsonMsg, String fileName, String fileExtension, File file) {
            this.type = type;
            this.receiver = receiver;
            this.message = message;
            this.jsonMsg = jsonMsg;
            this.fileName = fileName;
            this.fileExtension = fileExtension;
            this.file = file;
        }

        SendingMessage(byte type, Peer receiver, String message, String fileName, String fileExtension, File file) {
            this.type = type;
            this.receiver = receiver;
            this.message = message;
            this.fileName = fileName;
            this.fileExtension = fileExtension;
            this.file = file;
        }

        SendingMessage(byte type, Peer receiver, MbnJSON.JsonObjectBuilder jsonMsg, String fileName, String fileExtension, File file) {
            this.type = type;
            this.receiver = receiver;
            this.jsonMsg = jsonMsg;
            this.fileName = fileName;
            this.fileExtension = fileExtension;
            this.file = file;
        }

        public long getStartNanoTime() {
            return startNanoTime;
        }

        public long getExecutionTime() {
            return executionTime;
        }

        String getProceededMessage() {
            if (jsonMsg == null) {
                return message;
            } else {
                return jsonMsg.Build();
            }
        }

        void putMessageIn(MbnJSON.JsonObjectBuilder builder) {
            if (jsonMsg == null) {
                builder.putString("message", message);
            } else {
                builder.putJsonObject("message", jsonMsg);
            }
        }

    }

    public static class UserMessage {
        private String message;
        //        private String fileName, fileExtension;
        private File file;
        public volatile long startNanoTime;
        public volatile long executionTime;

        public UserMessage(String message, File file) {
            this.message = message;
//            this.fileName = fileName;
//            this.fileExtension = fileExtension;
            this.file = file;
        }

        public String getMessage() {
            return message;
        }

//        public String getFileName() {
//            return fileName;
//        }
//
//        public String getFileExtension() {
//            return fileExtension;
//        }

        public File getFile() {
            return file;
        }
    }

    public interface InterComListener {
        void onAddedToPeers(Peer peer, List<Peer> peersList);

        void onRemovedFromPeers(Peer peer, List<Peer> peersList);

        void onNewMessage(Peer peer, UserMessage message);

        void onMessageSent(Peer receiver, String message, File file, long startNanoTime, long executionTime);
    }
}
