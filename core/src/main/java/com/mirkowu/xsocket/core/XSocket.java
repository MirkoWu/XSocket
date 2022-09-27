package com.mirkowu.xsocket.core;

public class XSocket {

    ISocket socket;

//    public XSocket setIP(String ip,int port){
//
//    }

    public XSocket connect(String ip,int port){

       return this;
    }

    public XSocket setSender(ISender sender){

        return this;
    }
    public XSocket setReceiver(IReceiver receiver){

        return this;
    }

    public XSocket setTimer(){
        return this;
    }
     public XSocket setPulseManager(){
        return this;
    }
     public XSocket setReconnectManager(){
        return this;
    }




}
