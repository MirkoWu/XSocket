package com.mirkowu.xsocekt;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.client.xsocekt.R;
import com.mirkowu.xsocket.core.IConnectManager;
import com.mirkowu.xsocket.core.XSocket;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        IConnectManager manager= new XSocket().connect("192.168.1.1",8080);
        manager.connect();
    }
}