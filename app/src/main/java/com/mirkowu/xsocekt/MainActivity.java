package com.mirkowu.xsocekt;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.mirkowu.xsocket.core.IConnectManager;
import com.mirkowu.xsocket.core.Packet;
import com.mirkowu.xsocket.core.XLog;
import com.mirkowu.xsocket.core.XSocket;
import com.mirkowu.xsocket.core.listener.IClientActionListener;
import com.mirkowu.xsocket.core.util.ByteUtils;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }


    public void clickServer(View view) {
        new XSocket().startServer().start();
    }


    public void clickCloseServer(View view) {
    }

    IConnectManager manager;

    public void clickConnect(View view) {
        manager = new XSocket().connect("127.0.0.1", 8888);
        manager.connect();
        manager.registerActionListener(new IClientActionListener() {
            @Override
            public void onSend(byte[] bytes) {
                XLog.e("onSend :" + ByteUtils.bytes2String(bytes));
            }

            @Override
            public void onReceive(byte[] bytes) {
                XLog.e("onReceive :" + ByteUtils.bytes2String(bytes));
            }

            @Override
            public void onConnect() {
                XLog.e("onConnect");
            }

            @Override
            public void onDisConnect() {
                XLog.e("onDisConnect");
            }

            @Override
            public void onReConnect() {
                XLog.e("onReConnect");
            }
        });
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