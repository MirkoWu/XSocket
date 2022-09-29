package com.mirkowu.xsocekt;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.mirkowu.xsocket.client.connect.IConnectManager;
import com.mirkowu.xsocket.client.IPConfig;
import com.mirkowu.xsocket.client.listener.ISocketListener;
import com.mirkowu.xsocket.core.XLog;
import com.mirkowu.xsocket.client.XSocket;
import com.mirkowu.xsocket.core.listener.IServerSocketListener;
import com.mirkowu.xsocket.core.server.IClient;
import com.mirkowu.xsocket.core.server.IClientPool;
import com.mirkowu.xsocket.core.server.IServerManager;
import com.mirkowu.xsocket.core.util.ByteUtils;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    IServerManager serverManager;

    public void clickServer(View view) {
        serverManager = new XSocket().getServer(8888);
        serverManager.registerSocketListener(new IServerSocketListener() {
            @Override
            public void onServerListening(int serverPort) {
                XLog.e("server  , onServerListening :" + serverPort);
            }

            @Override
            public void onClientConnected(IClient client, int serverPort, IClientPool clientPool) {
                XLog.e("server  , onClientConnected :" +client.getHostName()+":"+ serverPort );
            }

            @Override
            public void onClientDisconnected(IClient client, int serverPort, IClientPool clientPool) {
                XLog.e("server  , onClientDisconnected :" +client.getHostName()+":"+ serverPort );
            }

            @Override
            public void onServerAlreadyShutdown(int serverPort) {
                XLog.e("server  , onServerListening :" + serverPort);

            }
        });
        serverManager.listen();
    }


    public void clickCloseServer(View view) {
        if (serverManager != null) {
            serverManager.shutdown();
        }
    }

    IConnectManager manager;

    public void clickConnect(View view) {
        manager = new XSocket().connect("127.0.0.1", 8888);
        manager.registerSocketListener(new ISocketListener() {

            @Override
            public void onSend(IPConfig config, byte[] bytes) {
                XLog.e("onSend :" + ByteUtils.bytes2String(bytes));
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
                XLog.e("onDisConnect");
            }

            @Override
            public void onReconnect(IPConfig config) {
                XLog.e("onReConnect");
            }
        });
        manager.connect();

    }

    public void clickSend(View view) {
        if (manager != null) {
            manager.send("hahhahahahha,你好啊，哈哈哈".getBytes());
        }
    }

    public void clickDisConnect(View view) {
        if (manager != null) {
            manager.disconnect();
        }
    }


}