package com.mirkowu.xsocket.client.connect;

import com.mirkowu.xsocket.client.IPConfig;
import com.mirkowu.xsocket.client.Options;
import com.mirkowu.xsocket.core.io.IOThreadMode;

import java.util.HashMap;

public class CacheManager {

    private static final class Holder {
        private static final CacheManager sInstance = new CacheManager();
    }

    public static CacheManager getInstance() {
        return Holder.sInstance;
    }

    private CacheManager() {
    }

    HashMap<IPConfig, IConnectManager> connectMap = new HashMap<>();

    //    public IConnectManager get(IPConfig config){
//        if(connectMap.containsKey(config)){
//            connectMap.get(config);
//        }
//        return connectMap.get(config);
//    }
//
    public IConnectManager get(IPConfig config) {
//        IConnectManager connectManager=new ConnectManagerImp(config);
        Options options = new Options().setIOThreadMode(IOThreadMode.SIMPLEX);
        IConnectManager connectManager = new ConnectManagerImp(config/*, options*/);
        return connectManager;
    }


}
