package com.mirkowu.xsocket.core.server.client;

public interface IClientPool<K,V> {


    void cache(V t);

    V findByUniqueTag(K key);

    int size();

    boolean isFull();

    void sendToAll(byte[] bytes);
}
