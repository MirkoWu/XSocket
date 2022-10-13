package com.mirkowu.xsocket.server.client;

import com.mirkowu.xsocket.core.data.ISendData;

public interface IClientPool<K,V> {


    void cache(V t);

    V findByUniqueTag(K key);

    int size();

    boolean isFull();

    void sendToAll(ISendData sendData);
}
