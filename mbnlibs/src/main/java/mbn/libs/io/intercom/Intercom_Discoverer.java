package mbn.libs.io.intercom;

import android.content.Context;
import android.net.nsd.NsdManager;
import android.net.nsd.NsdServiceInfo;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Intercom_Discoverer extends Intercom_Base {
    private NsdManager nsdManager;
    private String serviceType = "_http._tcp.";
    private static final String TAG = "INTERCOM_Discoverer";
    private boolean isDiscovering = false;
    private ArrayList<Peer> availablePeers = new ArrayList<>();
    private DiscoveryListener discoveryListener_intercom;

    public Intercom_Discoverer(String userNickName, String serviceName, File receivedFilesDir, int processThreads, int sendingThreads, int checkInterval, Context context, DiscoveryListener listener) throws IOException {
        super(userNickName, serviceName, receivedFilesDir, processThreads, sendingThreads, checkInterval);
        nsdManager = (NsdManager) context.getSystemService(Context.NSD_SERVICE);
        discoveryListener_intercom = listener;
    }

    public void startDiscovery() {
        if (isDiscovering) {
            return;
        }
        nsdManager.discoverServices(serviceType, NsdManager.PROTOCOL_DNS_SD, discoveryListener);
    }

    public void stopDiscovery() {
        if (isDiscovering) {
            nsdManager.stopServiceDiscovery(discoveryListener);
        }
    }

    public boolean isDiscovering() {
        return isDiscovering;
    }

    @Override
    public void stop() {
        super.stop();
        stopDiscovery();
    }

    @Override
    protected void introductionArrived(Peer peer) {
        super.introductionArrived(peer);
        if (!availablePeers.contains(peer)) {
            availablePeers.add(peer);
            discoveryListener_intercom.onPeerAvailable(peer, availablePeers);
        }
    }


    public ArrayList<Peer> getAvailablePeers() {
        return availablePeers;
    }

    private final NsdManager.DiscoveryListener discoveryListener = new NsdManager.DiscoveryListener() {

        // Called as soon as service discovery begins.
        @Override
        public void onDiscoveryStarted(String regType) {
            Log.d(TAG, "Service discovery started");
            isDiscovering = true;
        }

        @Override
        public void onServiceFound(NsdServiceInfo service) {
            // A service was found! Do something with it.
            Log.d(TAG, "Service discovery success" + service);
            if (!service.getServiceType().equals(serviceType)) {
                // Service type is the string containing the protocol and
                // transport layer for this service.
                Log.d(TAG, "Unknown Service Type: " + service.getServiceType());
            }/* else if (service.getServiceName().equals(serviceName)) {
                // The name of the service tells the user what they'd be
                // connecting to. It could be "Bob's Chat App".
                Log.d(TAG, "Same machine: " + serviceName);
            }*/ else if (service.getServiceName().contains(getServiceName())) {
                nsdManager.resolveService(service, resolveListener);
//                nsdManager.stopServiceDiscovery(this);
            }
        }

        @Override
        public void onServiceLost(NsdServiceInfo service) {
            // When the network service is no longer available.
            // Internal bookkeeping code goes here.
            Log.e(TAG, "service lost: " + service);
        }

        @Override
        public void onDiscoveryStopped(String serviceType) {
            Log.i(TAG, "Discovery stopped: " + serviceType);
            isDiscovering = false;
        }

        @Override
        public void onStartDiscoveryFailed(String serviceType, int errorCode) {
            Log.e(TAG, "Discovery failed: Error code:" + errorCode);
            isDiscovering = false;
            nsdManager.stopServiceDiscovery(this);
        }

        @Override
        public void onStopDiscoveryFailed(String serviceType, int errorCode) {
            Log.e(TAG, "Discovery failed: Error code:" + errorCode);
            isDiscovering = false;
            nsdManager.stopServiceDiscovery(this);
        }
    };

    private final NsdManager.ResolveListener resolveListener = new NsdManager.ResolveListener() {

        @Override
        public void onResolveFailed(NsdServiceInfo serviceInfo, int errorCode) {
            // Called when the resolve fails. Use the error code to debug.
            Log.e(TAG, "Resolve failed: " + errorCode);
        }

        @Override
        public void onServiceResolved(NsdServiceInfo serviceInfo) {
            Log.e(TAG, "Resolve Succeeded. " + serviceInfo);

//            if (serviceInfo.getServiceName().equals(serviceName)) {
//                Log.d(TAG, "Same IP.");
//                return;
//            }
            int foundPort = serviceInfo.getPort();
            InetAddress foundHost = serviceInfo.getHost();
            if (isFirst) {
                try {
                    Socket socket = new Socket(foundHost, foundPort);
                    getSelf_peer().hostAddress = socket.getLocalAddress();
                    isFirst = false;
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            internalSend(new SendingMessage(INTRODUCE_YOURSELF, new Peer(foundPort, foundHost, null), "", null, null, null));
        }
    };

    public interface DiscoveryListener {
        void onPeerAvailable(Peer peer, List<Peer> availablePeersList);
    }
}
