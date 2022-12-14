package com.mirkowu.xsocekt;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.mirkowu.xsocket.client.connect.IConnectManager;
import com.mirkowu.xsocket.core.IPConfig;
import com.mirkowu.xsocket.client.listener.ISocketListener;
import com.mirkowu.xsocket.core.data.IPulseSendData;
import com.mirkowu.xsocket.core.data.ISendData;
import com.mirkowu.xsocket.core.data.IUdpSendData;
import com.mirkowu.xsocket.core.SocketType;
import com.mirkowu.xsocket.core.XLog;
import com.mirkowu.xsocket.client.XSocket;
import com.mirkowu.xsocket.server.listener.IClientStatusListener;
import com.mirkowu.xsocket.server.listener.IServerSocketListener;
import com.mirkowu.xsocket.server.IShutdown;
import com.mirkowu.xsocket.server.client.IClient;
import com.mirkowu.xsocket.server.client.IClientPool;
import com.mirkowu.xsocket.server.IServerManager;
import com.mirkowu.xsocket.core.util.ByteUtils;
import com.mirkowu.xsocket.server.ServerOptions;
import com.mirkowu.xsocket.server.XSocketServer;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        XSocket.setDebug(true);
    }

    IServerManager serverManager;
    public static volatile long startTime;
    IServerSocketListener serverSocketListener=new IServerSocketListener() {

        @Override
        public void onServerListening(int serverPort) {
            XLog.e("server  , onServerListening :" + serverPort);
        }


        @Override
        public void onReceiveFromClient(byte[] bytes, IClient client, IClientPool<String, IClient> clientPool) {
            XLog.e("server onReceiveFromClient :" +client.getHostIp()+":"+client.getHostPort()+" "+ ByteUtils.bytes2String(bytes));
            if (bytes == null || bytes.length == 0) return;
            if (client.getSocketType() == SocketType.TCP) {
                client.send(new ISendData() {
                    @Override
                    public byte[] getData() {
                        return ByteUtils.concat(bytes, "-???????????????".getBytes());
                    }
                });
            } else {
//                    startTime = System.currentTimeMillis();
//                    for (int i = 0; i < 1000; i++) {
//                        XLog.e("????????????  :" + i);

                client.send(new IUdpSendData() {
                    @Override
                    public String getIp() {
                        return client.getHostIp();
                    }

                    @Override
                    public int getPort() {
                        return client.getHostPort();
                    }

                    @Override
                    public byte[] getData() {
                        return ByteUtils.concat(bytes, "-????????????1111???".getBytes());
                    }
                });
//                    }
//                    XLog.e("???????????? server ??????  end:" + (System.currentTimeMillis() - startTime));

            }


        }

        @Override
        public void onSendToClient(ISendData sendData, IClient client, IClientPool<String, IClient> clientPool) {
            XLog.e("server onSendToClient :"+client.getHostIp()+":"+client.getHostPort()+" " + ByteUtils.bytes2String(sendData.getData()));

        }

        @Override
        public void onServerWillBeShutdown(int serverPort, IShutdown shutdown, IClientPool clientPool, Exception e) {
            XLog.e("server  , onServerWillBeShutdown :" + serverPort + (e != null ? e.toString() : "null"));
        }

        @Override
        public void onServerAlreadyShutdown(int serverPort, Exception e) {
            XLog.e("server  , onServerAlreadyShutdown :" + serverPort + (e != null ? e.toString() : "null"));

        }
    };

    IClientStatusListener clientStatusListener= new IClientStatusListener() {
        @Override
        public void onClientConnected(IClient client, int serverPort, IClientPool clientPool) {
            XLog.e("server  , onClientConnected :" + client.getHostName() + ":" + serverPort);

            if (client != null) {
                if (client.getSocketType() == SocketType.TCP) {
                    client.send(new ISendData() {
                        @Override
                        public byte[] getData() {
                            return ("?????????????????????" + " ???????????? " + clientPool.size() + " ???").getBytes();
                        }
                    });
                }
            }

        }

        @Override
        public void onClientDisconnected(IClient client, int serverPort, IClientPool clientPool, Exception e) {
            XLog.e("server  , onClientDisconnected :" + client.getHostName() + ":" + serverPort + (e != null ? e.toString() : "null"));
        }
    };
    public void clickServer(View view) {
        serverManager = XSocketServer.getServer(8888,
                ServerOptions.getDefault().setSocketType(SocketType.UDP));
        serverManager.registerSocketListener(serverSocketListener);
        serverManager.registerClientStatusListener(clientStatusListener);
        serverManager.listen();
    }


    public void clickCloseServer(View view) {
        if (serverManager != null) {
            serverManager.shutdown();
        }
    }

    IConnectManager manager;
    ISocketListener socketListener=new ISocketListener() {

        @Override
        public void onSend(IPConfig config, ISendData sendData) {
            XLog.e("onSend :" + ByteUtils.bytes2String(sendData.getData()));
        }

        @Override
        public void onReceive(IPConfig config, byte[] bytes) {
            XLog.e("onReceive :" + ByteUtils.bytes2String(bytes));
        }

        @Override
        public void onConnectSuccess(IPConfig config) {
            XLog.e("onConnect");
        }

        @Override
        public void onConnectFail(IPConfig config, Exception e) {
            XLog.e("onConnectFail");
        }

        @Override
        public void onDisConnect(IPConfig config, Exception e) {
            XLog.e("onDisConnect :" + (e == null ? "null" : e.toString()));
        }

        @Override
        public void onReconnect(IPConfig config) {
            XLog.e("onReConnect");
        }

        @Override
        public void onPulseSend(IPConfig config, IPulseSendData sendData) {
            XLog.e("onPulseSend" + ByteUtils.bytes2String(sendData.getData()));
        }
    };
    public void clickConnect(View view) {
        if (manager != null && manager.isConnected()) {
            manager.disconnect();
        }
        manager = XSocket.config("192.168.1.1", 80);
//        manager = XSocket.config("127.0.0.1", 8888);
//        manager = XSocket.config("192.168.2.104", 8888);
        manager.registerSocketListener(socketListener);
        manager.connect();

    }

    @Override
    protected void onDestroy() {
        if (manager != null) {
            manager.disconnect();
        }
        if (serverManager != null) {
            serverManager.shutdown();
        }
        super.onDestroy();
    }

    /**
     * ??????wifi???????????????
     *
     * @return
     */
    public static JSONObject getWifiList() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("method", "getWifi");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    /**
     * ???????????????
     *
     * @return
     */
    public static JSONObject getHeart() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("method", "HB");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    public void clickSend(View view) {

        if (manager != null) {
            manager.send(new ISendData() {
                @Override
                public byte[] getData() {
                    return getWifiList().toString().getBytes();
                }
            });
        }
    }

    public void clickDisConnect(View view) {
        if (manager != null) {
            manager.disconnect();
        }
    }


    public void clickTCP(View view) {
        ClientActivity.start(this,SocketType.TCP);
    }

    public void clickUDP(View view) {
        ClientActivity.start(this,SocketType.UDP);
    }
}