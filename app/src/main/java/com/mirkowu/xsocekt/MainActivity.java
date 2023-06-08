package com.mirkowu.xsocekt;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

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

    RadioGroup rgType;
    RadioButton rbTcp;
    RadioButton rbUdp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        XSocket.setDebug(true);

        rgType = findViewById(R.id.rgType);
        rbTcp = findViewById(R.id.rbTcp);
        rbUdp = findViewById(R.id.rbUdp);
    }

    SocketType socketType = SocketType.TCP;

    public static final String PULSE_DATA = "0x0101";
    IServerManager serverManager;
    public static volatile long startTime;
    IServerSocketListener serverSocketListener = new IServerSocketListener() {

        @Override
        public void onServerListening(int serverPort) {
            XLog.e("server  , onServerListening :" + serverPort);
        }


        @Override
        public void onReceiveFromClient(byte[] bytes, IClient client, IClientPool<String, IClient> clientPool) {
            XLog.e("server onReceiveFromClient :" + client.getHostIp() + ":" + client.getHostPort() + " " + ByteUtils.bytes2String(bytes));
            if (bytes == null || bytes.length == 0) return;

            if (client.getSocketType() == SocketType.TCP) {
                client.send(new ISendData() {
                    @Override
                    public byte[] getData() {
                        //检测心跳,并ack
                        if (PULSE_DATA.equals(ByteUtils.bytes2String(bytes))) {
                            return PULSE_DATA.getBytes();
                        }
                        return ByteUtils.concat(bytes, "-我已收到：".getBytes());
                    }
                });
            } else {
//                    startTime = System.currentTimeMillis();
//                    for (int i = 0; i < 1000; i++) {
//                        XLog.e("发送测试  :" + i);

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
                        //检测心跳，并ack
                        if (PULSE_DATA.equals(ByteUtils.bytes2String(bytes))) {
                            return PULSE_DATA.getBytes();
                        }
                        return ByteUtils.concat(bytes, "-我已收到1111：".getBytes());
                    }
                });
//                    }
//                    XLog.e("发送测试 server 耗时  end:" + (System.currentTimeMillis() - startTime));

            }


        }

        @Override
        public void onSendToClient(ISendData sendData, IClient client, IClientPool<String, IClient> clientPool) {
            XLog.e("server onSendToClient :" + client.getHostIp() + ":" + client.getHostPort() + " " + ByteUtils.bytes2String(sendData.getData()));

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

    IClientStatusListener clientStatusListener = new IClientStatusListener() {
        @Override
        public void onClientConnected(IClient client, int serverPort, IClientPool clientPool) {
            XLog.e("server  , onClientConnected :" + client.getHostName() + ":" + serverPort);

            if (client != null) {
                if (client.getSocketType() == SocketType.TCP) {
                    client.send(new ISendData() {
                        @Override
                        public byte[] getData() {
                            return ("你已进入聊天室" + " 当前共有 " + clientPool.size() + " 人").getBytes();
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
        socketType = rbTcp.isChecked() ? SocketType.TCP : SocketType.UDP;
        serverManager = XSocketServer.getServer(8888,
                ServerOptions.getDefault().setSocketType(socketType));
        serverManager.registerSocketListener(serverSocketListener);
        serverManager.registerClientStatusListener(clientStatusListener);
        serverManager.listen();
    }


    public void clickCloseServer(View view) {
        if (serverManager != null) {
            serverManager.shutdown();
        }
    }
//
//    IConnectManager manager;
//    ISocketListener socketListener=new ISocketListener() {
//
//        @Override
//        public void onSend(IPConfig config, ISendData sendData) {
//            XLog.e("onSend :" + ByteUtils.bytes2String(sendData.getData()));
//        }
//
//        @Override
//        public void onReceive(IPConfig config, byte[] bytes) {
//            XLog.e("onReceive :" + ByteUtils.bytes2String(bytes));
//            if (PULSE_DATA.equals(ByteUtils.bytes2String(bytes))) {
//                manager.getPulseManager().feed();//收到返回心跳后喂狗
//            }
//        }
//
//        @Override
//        public void onConnectSuccess(IPConfig config) {
//            XLog.e("onConnect");
//            manager.getPulseManager().pulse();//开启心跳
//        }
//
//        @Override
//        public void onConnectFail(IPConfig config, Exception e) {
//            XLog.e("onConnectFail");
//        }
//
//        @Override
//        public void onDisConnect(IPConfig config, Exception e) {
//            XLog.e("onDisConnect :" + (e == null ? "null" : e.toString()));
//        }
//
//        @Override
//        public void onReconnect(IPConfig config) {
//            XLog.e("onReConnect");
//        }
//
//        @Override
//        public void onPulseSend(IPConfig config, IPulseSendData sendData) {
//            XLog.e("onPulseSend" + ByteUtils.bytes2String(sendData.getData()));
//        }
//    };
//    public void clickConnect(View view) {
//        if (manager != null && manager.isConnected()) {
//            manager.disconnect();
//        }
//        manager = XSocket.config("192.168.1.1", 80);
////        manager = XSocket.config("127.0.0.1", 8888);
////        manager = XSocket.config("192.168.2.104", 8888);
//        manager.registerSocketListener(socketListener);
//        manager.getPulseManager().setPulseSendData(new IPulseSendData() {
//            @Override
//            public byte[] getData() {
//                return PULSE_DATA.getBytes();
//            }
//        });
//
//        manager.connect();
//
//    }

    @Override
    protected void onDestroy() {
//        if (manager != null) {
//            manager.disconnect();
//        }
        if (serverManager != null) {
            serverManager.shutdown();
        }
        super.onDestroy();
    }
//
//    public void clickSend(View view) {
//
//        if (manager != null) {
//            manager.send(new ISendData() {
//                @Override
//                public byte[] getData() {
//                    return getWifiList().toString().getBytes();
//                }
//            });
//        }
//    }
//
//    public void clickDisConnect(View view) {
//        if (manager != null) {
//            manager.disconnect();
//        }
//    }


    public void clickClient(View view) {
        ClientActivity.start(this, socketType);
    }
}