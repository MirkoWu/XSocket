package com.mirkowu.xsocket.client.connect;

import android.net.wifi.WifiManager;

import com.mirkowu.xsocket.core.IPConfig;
import com.mirkowu.xsocket.client.Options;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class UdpClient implements IClientSocket {

    DatagramSocket socket;

    private IPConfig ipConfig;
    private Options options;

    private boolean allowMultiCast;

    private volatile boolean isConnected;

    public UdpClient(IPConfig ipConfig, Options options) {
        this.ipConfig = ipConfig;
        this.options = options;
        allowMultiCast = options.isAllowMultiCast();
    }

    private InetAddress destAddress;
    private WifiManager wifiManager;
    private WifiManager.MulticastLock multicastLock;
    int TIME_OUT = 60 * 1000;

    @Override
    public void createSocket() throws Exception {


        // allowMultiCast();
        destAddress = InetAddress.getByName(ipConfig.ip);
//        if (!destAddress.isMulticastAddress()) {// 检测该地址是否是多播地址
//            return;
//        }
//        socket = new MulticastSocket(null);
        socket = new DatagramSocket(/*null*/);
        socket.setReuseAddress(true);//复用端口
        socket.setBroadcast(true);
        socket.setSoTimeout(TIME_OUT);
        // socket.setTimeToLive(TTL);
     //   socket.bind(new InetSocketAddress(ipConfig.port));
        //  socket.joinGroup(destAddress);

        isConnected=true;
    }
//
//    private void allowMultiCast() {
//        wifiManager = (WifiManager) BaseApplication.getInstance().getApplicationContext().getSystemService(Context.WIFI_SERVICE);
//        multicastLock = wifiManager.createMulticastLock("findDevices");
//        multicastLock.acquire();
//
//    }

//    @Override
//    public void send(byte[] data) throws Exception {
//
//        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
//        String ipAddress = intIP2StringIP(wifiInfo.getIpAddress());//得到IPV4地址
//        DatagramPacket dp = new DatagramPacket(data, data.length, InetAddress.getByName(ipAddress), port);
//        socket.send(dp);
//    }
//
//    @Override
//    public byte[] receive() throws Exception {
//        DatagramPacket packet = getReceivePacket();
//
//        socket.receive(packet);
//        byte[] rec = sublist(packet.getData(), packet.getOffset(), packet.getLength());
//        L.d(new String(rec));
//        return rec;
//
//    }

    @Override
    public InputStream getInputStream() {
        return null;
    }

    @Override
    public OutputStream getOutputStream() {
        return null;
    }

    @Override
    public DatagramSocket getDatagramSocket() {
        return socket;
    }

    @Override
    public void close() throws IOException {
        isConnected=false;
        if (socket != null) {
            socket.close();
        }
    }

    @Override
    public boolean isClosed() {
        return socket != null && socket.isClosed();
    }

    @Override
    public boolean isConnected() {
        return socket != null && !socket.isClosed() &&isConnected /*&& socket.isConnected()*/;
    }
}
