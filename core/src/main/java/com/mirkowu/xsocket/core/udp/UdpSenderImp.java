package com.mirkowu.xsocket.core.udp;


import com.mirkowu.xsocket.core.IPConfig;
import com.mirkowu.xsocket.core.data.IPulseSendData;
import com.mirkowu.xsocket.core.data.ISendData;
import com.mirkowu.xsocket.core.data.IUdpSendData;
import com.mirkowu.xsocket.core.action.ActionBean;
import com.mirkowu.xsocket.core.action.ActionType;
import com.mirkowu.xsocket.core.AbsSender;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.util.concurrent.LinkedBlockingQueue;

public class UdpSenderImp extends AbsSender {

    private LinkedBlockingQueue<IUdpSendData> queue = new LinkedBlockingQueue<>();


    @Override
    public boolean send() throws Exception {
        IUdpSendData sendData = null;
        try {
            sendData = queue.take();
        } catch (InterruptedException e) {
            //e.printStackTrace();
            //throw e;
        }
        byte[] bytes;
        if (sendData != null && (bytes = sendData.getData()) != null) {
            try {
                String ip = sendData.getIp();
                int port = sendData.getPort();
                DatagramPacket packet = new DatagramPacket(bytes, bytes.length, InetAddress.getByName(ip), port);
                datagramSocket.send(packet);

                IPConfig ipConfig = new IPConfig(ip, port);
                if (sendData instanceof IPulseSendData) {
                    dispatcher.dispatchAction(ActionType.ACTION_PULSE_SEND, new ActionBean(sendData, ipConfig));
                } else {
                    dispatcher.dispatchAction(ActionType.ACTION_SEND, new ActionBean(sendData, ipConfig));
                }
                return true;
            } catch (IOException e) {
                e.printStackTrace();
                throw e;
            }
        }

        return false;
    }

    /**
     * 将得到的int类型的IP转换为String类型
     *
     * @param ip
     * @return
     */
    public static String intIP2StringIP(int ip) {
        return (ip & 0xFF) + "." +
                ((ip >> 8) & 0xFF) + "." +
                ((ip >> 16) & 0xFF) + "."
                + 255
                /* (ip >> 24 & 0xFF)*/;
    }

    @Override
    public void offer(ISendData sendData) {
        if (sendData instanceof IUdpSendData) {
            queue.offer((IUdpSendData) sendData);
        } else {
            throw new IllegalArgumentException("ISendData need extends IUdpSendData!");
        }
    }

    @Override
    public void close() {
        if (datagramSocket != null) {
            datagramSocket.close();
        }
    }
}
