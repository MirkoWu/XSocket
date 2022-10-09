package com.mirkowu.xsocket.core.io;


import com.mirkowu.xsocket.core.IPConfig;
import com.mirkowu.xsocket.core.XLog;
import com.mirkowu.xsocket.core.action.ActionBean;
import com.mirkowu.xsocket.core.action.ActionType;
import com.mirkowu.xsocket.core.util.ByteUtils;

import java.io.IOException;
import java.net.DatagramPacket;

public class UdpReceiverImp extends AbsReceiver {
    public static final int BUFF_SIZE = 1024;
    private DatagramPacket packet;

    @Override
    public byte[] receive() throws IOException {
        DatagramPacket packet = getReceivePacket();

        datagramSocket.receive(packet);
        byte[] rec = ByteUtils.sublist(packet.getData(), packet.getOffset(), packet.getLength());

        IPConfig ipConfig = new IPConfig(packet.getAddress().getHostAddress(), packet.getPort());
        dispatcher.dispatchAction(ActionType.ACTION_RECEIVE, new ActionBean(rec, ipConfig));

        return rec;
    }

    private DatagramPacket getReceivePacket() {
        if (packet == null) {
            byte data[] = new byte[2048];
            packet = new DatagramPacket(data, data.length);
        }
        return packet;
    }

    @Override
    public void close() {
        try {
            if (datagramSocket != null) {
                datagramSocket.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
