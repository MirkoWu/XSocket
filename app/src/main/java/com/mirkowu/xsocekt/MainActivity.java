package com.mirkowu.xsocekt;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.mirkowu.xsocket.client.connect.IConnectManager;
import com.mirkowu.xsocket.client.IPConfig;
import com.mirkowu.xsocket.client.listener.ISocketListener;
import com.mirkowu.xsocket.core.IPulseSendData;
import com.mirkowu.xsocket.core.ISendData;
import com.mirkowu.xsocket.core.XLog;
import com.mirkowu.xsocket.client.XSocket;
import com.mirkowu.xsocket.server.IServerSocketListener;
import com.mirkowu.xsocket.server.IShutdown;
import com.mirkowu.xsocket.server.IClient;
import com.mirkowu.xsocket.server.IClientIOListener;
import com.mirkowu.xsocket.server.IClientPool;
import com.mirkowu.xsocket.server.IServerManager;
import com.mirkowu.xsocket.core.util.ByteUtils;
import com.mirkowu.xsocket.server.XSocketServer;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity implements IClientIOListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        XSocket.setDebug(true);
    }

    IServerManager serverManager;

    public void clickServer(View view) {
        serverManager = XSocketServer.getServer(8888);
        serverManager.registerSocketListener(new IServerSocketListener() {
            @Override
            public void onServerListening(int serverPort) {
                XLog.e("server  , onServerListening :" + serverPort);
            }

            @Override
            public void onClientConnected(IClient client, int serverPort, IClientPool clientPool) {

                XLog.e("server  , onClientConnected :" + client.getHostName() + ":" + serverPort);

                client.addClientIOListener(MainActivity.this);

                client.send(new ISendData() {
                    @Override
                    public byte[] getData() {
                        return ("你已进入聊天室" + " 当前共有 " + clientPool.size() + " 人").getBytes();
                    }
                });

            }


            @Override
            public void onClientDisconnected(IClient client, int serverPort, IClientPool clientPool) {
                XLog.e("server  , onClientDisconnected :" + client.getHostName() + ":" + serverPort);
            }

            @Override
            public void onServerWillBeShutdown(int serverPort, IShutdown shutdown, IClientPool clientPool, Throwable e) {
                XLog.e("server  , onServerWillBeShutdown :" + serverPort + (e != null ? e.toString() : "null"));
            }

            @Override
            public void onServerAlreadyShutdown(int serverPort, Throwable e) {
                XLog.e("server  , onServerAlreadyShutdown :" + serverPort + (e != null ? e.toString() : "null"));

            }
        });
        serverManager.listen();
    }

    @Override
    public void onReceiveFromClient(byte[] bytes, IClient client, IClientPool<String, IClient> clientPool) {
        XLog.e("server onReceiveFromClient :" + ByteUtils.bytes2String(bytes));

    }

    @Override
    public void onSendToClient(ISendData sendData, IClient client, IClientPool<String, IClient> clientPool) {
        XLog.e("server onSendToClient :" + ByteUtils.bytes2String(sendData.getData()));

    }

    public void clickCloseServer(View view) {
        if (serverManager != null) {
            serverManager.shutdown();
        }
    }

    IConnectManager manager;

    public void clickConnect(View view) {
        if(manager!=null && manager.isConnected()) {
            manager.disconnect();
        }
        manager = new XSocket().connect("192.168.1.1", 80);
//        manager = new XSocket().connect("127.0.0.1", 8888);
//        manager = XSocket.connect("192.168.2.104", 8888);
        manager.registerSocketListener(new ISocketListener() {

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
        });
        manager.connect();

    }

    @Override
    protected void onDestroy() {
        if(manager!=null){
            manager.disconnect();
        }
        if(serverManager!=null){
            serverManager.shutdown();
        }
        super.onDestroy();
    }

    /**
     * 获取wifi列表的请求
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
     * 发送心跳包
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


}