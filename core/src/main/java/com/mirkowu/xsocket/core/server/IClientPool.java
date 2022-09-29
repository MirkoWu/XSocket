package com.mirkowu.xsocket.core.server;

public interface IClientPool<K,V> {


    void cache(V t);

    V findByUniqueTag(K key);

    int size();

    void sendToAll(byte[] bytes);
}
