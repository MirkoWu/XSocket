package com.mirkowu.xsocket.client.connect;

import android.net.wifi.WifiManager;

import com.mirkowu.xsocket.core.IPConfig;
import com.mirkowu.xsocket.client.Options;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.MulticastSocket;
import java.net.NetworkInterface;

public class UdpClient implements IClientSocket {

    private DatagramSocket socket;

    private IPConfig ipConfig;
    private Options options;
    private volatile boolean isConnected;
    private InetAddress destAddress;
    private int TIME_OUT = 60 * 1000;

    public UdpClient(IPConfig ipConfig, Options options) {
        this.ipConfig = ipConfig;
        this.options = options;
    }


    @Override
    public void createSocket() throws Exception {
        destAddress = InetAddress.getByName(ipConfig.ip);
        if (destAddress.isMulticastAddress()) {// 检测该地址是否是多播地址
            socket = new MulticastSocket(/*null*/);
        } else {
            socket = new DatagramSocket(/*null*/);
        }

        socket.setReuseAddress(true);//复用端口
        socket.setBroadcast(true);
       // socket.setSoTimeout(TIME_OUT);
        // socket.setTimeToLive(TTL);
//        socket.bind(new InetSocketAddress());
        if (socket instanceof MulticastSocket) {
            //如果主机是多网卡，那么此时就需要注意了，一定要设置用哪个网卡发送和接受数据，因为组播是无法跨网段的，否则会导致数据接收不到。
            // ((MulticastSocket) socket).setNetworkInterface(NetworkInterface.getByInetAddress(InetAddress.getByName("10.206.16.67")));
            ((MulticastSocket) socket).joinGroup(destAddress);
        }

        isConnected = true;
    }
//
//    private void allowMultiCast() {
//        wifiManager = (WifiManager) BaseApplication.getInstance().getApplicationContext().getSystemService(Context.WIFI_SERVICE);
//        multicastLock = wifiManager.createMulticastLock("findDevices");
//        multicastLock.acquire();
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
        isConnected = false;
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
        return socket != null && !socket.isClosed() && isConnected /*&& socket.isConnected()*/;
    }
}
