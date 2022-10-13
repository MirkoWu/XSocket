package com.mirkowu.xsocekt;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ScrollView;
import android.widget.Toast;

import com.mirkowu.xsocekt.databinding.ActivityTcpBinding;
import com.mirkowu.xsocket.core.AbsReceiver;
import com.mirkowu.xsocket.core.IPConfig;
import com.mirkowu.xsocket.client.Options;
import com.mirkowu.xsocket.client.XSocket;
import com.mirkowu.xsocket.client.connect.IConnectManager;
import com.mirkowu.xsocket.client.listener.ISocketListener;
import com.mirkowu.xsocket.core.data.IPulseSendData;
import com.mirkowu.xsocket.core.data.ISendData;
import com.mirkowu.xsocket.core.SocketType;
import com.mirkowu.xsocket.core.XLog;
import com.mirkowu.xsocket.core.util.ByteUtils;

import java.io.IOException;

public class TcpActivity extends AppCompatActivity implements ISocketListener {

    ActivityTcpBinding binding;

    IConnectManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTcpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


    }

    public void clickConnect(View view) {
        if (manager == null || !manager.isConnected()) {
            String ip = binding.etIp.getText().toString().trim();
            String port = binding.etPort.getText().toString().trim();
            if (TextUtils.isEmpty(ip)) {
                Toast.makeText(this, "请输入ip", Toast.LENGTH_SHORT).show();
                return;
            }
            if (TextUtils.isEmpty(port)) {
                Toast.makeText(this, "请输入port", Toast.LENGTH_SHORT).show();
                return;
            }
            manager = XSocket.config(ip, Integer.parseInt(port),
                    Options.defaultOptions().setSocketType(SocketType.UDP));
            manager.registerSocketListener(this);
            manager.connect();
        } else {
            manager.disconnect();
        }
    }

    @Override
    protected void onDestroy() {
        if (manager != null) {
            manager.disconnect();
            manager.removeAllSocketListener();
        }
        super.onDestroy();
    }

    public void clickClearLog(View view) {
        binding.tvContentSend.setText("");
        binding.tvContentReceive.setText("");
    }

    public void clickSend(View view) {
        String str = binding.etSend.getText().toString();
        if (TextUtils.isEmpty(str)) {
            Toast.makeText(this, "内容不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        if (manager != null) {
            manager.send(new ISendData() {
                @Override
                public byte[] getData() {
                    return str.getBytes();
                }
            });
        }
    }

    public void sendAppend(String content) {
        binding.tvContentSend.append(content + "\n");
        binding.svSend.fullScroll(ScrollView.FOCUS_DOWN);
    }

    public void receiveAppend(String content) {
        binding.tvContentReceive.append(content + "\n");
        binding.svReceive.fullScroll(ScrollView.FOCUS_DOWN);
    }

    @Override
    public void onConnectSuccess(IPConfig config) {
        binding.btnConnect.setText("已连接，点击断开连接");
        binding.btnSend.setEnabled(true);
    }

    @Override
    public void onConnectFail(IPConfig config, Exception e) {
        XLog.e("client onConnectFail :" + (e == null ? "null" : e.toString()));
        binding.btnConnect.setText("点击连接");
        binding.btnSend.setEnabled(false);
    }

    @Override
    public void onDisConnect(IPConfig config, Exception e) {
        XLog.e("client onDisConnect :" + (e == null ? "null" : e.toString()));

        binding.btnConnect.setText("点击连接");
        binding.btnSend.setEnabled(false);
    }

    @Override
    public void onReconnect(IPConfig config) {

    }

    @Override
    public void onSend(IPConfig config, ISendData sendData) {
        XLog.e("client  onSend :" + config.toString() + " " + ByteUtils.bytes2String(sendData.getData()));

        sendAppend(ByteUtils.bytes2String(sendData.getData()));
    }

    @Override
    public void onPulseSend(IPConfig config, IPulseSendData sendData) {

    }

    private int count = -1;
    private long firstTime = 0;

    @Override
    public void onReceive(IPConfig config, byte[] bytes) {
//        count++;
//        if (count == 0) {
//            firstTime = System.currentTimeMillis();
//            XLog.e("发送测试 首次接收   :" + (firstTime - MainActivity.startTime));
//        } else if (count == 999) {
//            XLog.e("发送测试 接收  end:" + (System.currentTimeMillis() - firstTime));
//            XLog.e("发送测试 发送-接收  end:" + (System.currentTimeMillis() - MainActivity.startTime));
//
//        }


        XLog.e("client  onReceive :" + config.toString() + " " + ByteUtils.bytes2String(bytes));
        receiveAppend(ByteUtils.bytes2String(bytes));
    }
}