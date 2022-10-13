package com.mirkowu.xsocket.core.tcp;

import com.mirkowu.xsocket.core.data.IPulseSendData;
import com.mirkowu.xsocket.core.data.ISendData;
import com.mirkowu.xsocket.core.action.ActionBean;
import com.mirkowu.xsocket.core.action.ActionType;
import com.mirkowu.xsocket.core.AbsSender;

import java.io.IOException;
import java.util.concurrent.LinkedBlockingQueue;

public class TcpSenderImp extends AbsSender {

    private LinkedBlockingQueue<ISendData> queue = new LinkedBlockingQueue<>();


    @Override
    public boolean send() throws Exception {
        ISendData sendData = null;
        try {
            sendData = queue.take();
        } catch (InterruptedException e) {
            //e.printStackTrace();
            //throw e;
        }
        byte[] bytes;
        if (sendData != null && (bytes = sendData.getData()) != null) {
            try {
                outputStream.write(bytes);
                outputStream.flush();

                if (sendData instanceof IPulseSendData) {
                    dispatcher.dispatchAction(ActionType.ACTION_PULSE_SEND, new ActionBean(sendData));
                } else {
                    dispatcher.dispatchAction(ActionType.ACTION_SEND, new ActionBean(sendData));
                }
                return true;
            } catch (IOException e) {
                e.printStackTrace();
                throw e;
            }
        }

        return false;
    }

    @Override
    public void offer(ISendData sendData) {
        queue.offer(sendData);
    }

    @Override
    public void close() {
        if (outputStream != null) {
            try {
                outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
