package com.mirkowu.xsocket.core.server;

import com.mirkowu.xsocket.core.exception.CacheException;

public class ClientPoolImp extends AbsClientPool<String, IClient> implements IClientPool<String, IClient> {


    public ClientPoolImp(int capacity) {
        super(capacity);
    }

    @Override
    public void cache(IClient client) {
        super.set(client.getUniqueTag(), client);
    }

    @Override
    public IClient findByUniqueTag(String key) {
        return get(key);
    }

    @Override
    public int size() {
        return super.size();
    }

    public void unCache(IClient iClient) {
        remove(iClient.getUniqueTag());
    }

    public void unCache(String key) {
        remove(key);
    }

    @Override
    public void sendToAll(byte[] bytes) {
        echoRun(new Echo<String, IClient>() {
            @Override
            public void onEcho(String key, IClient client) {
                client.send(bytes);
            }
        });
    }

    public void serverShutdown() {
        echoRun(new Echo<String, IClient>() {
            @Override
            public void onEcho(String key, IClient client) {
                client.disconnect();
            }
        });
        removeAll();
    }

    @Override
    void onCacheFull(String key, IClient lastOne) {
        lastOne.disconnect(new CacheException("cache is full,you need remove"));
        unCache(lastOne);
    }

    @Override
    void onCacheDuplicate(String key, IClient oldOne) {
        oldOne.disconnect(new CacheException("there are cached in this server.it need removed before new cache"));
        unCache(oldOne);
    }

    @Override
    void onCacheEmpty() {

    }


}
