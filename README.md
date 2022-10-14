### Import
XSocket[![](https://jitpack.io/v/mirkowu/xsocket.svg)](https://jitpack.io/#mirkowu/xsocket)
```java
//client
implementation 'com.github.mirkowu.xsocket:client:0.0.2'

//server
implementation 'com.github.mirkowu.xsocket:server:0.0.2'

```

### Use

server 
```java
        //服务端，配置端口
        IServerManager serverManager = XSocketServer.getServer(8888,
        ServerOptions.getDefault().setSocketType(SocketType.UDP)...);
        serverManager.registerSocketListener(serverSocketListener);
        serverManager.registerClientStatusListener(clientStatusListener);
        serverManager.listen(); //启动

         //关闭
         serverManager.shutdown();

         //销毁的时候记得 移除监听
         manager.unRegisterSocketListener()
         manager.unRegisterClientStatusListener()
```

client

```java
        //服务端，配置端口
        IConnectManager  manager = XSocket.config(ip, Integer.parseInt(port),
        Options.defaultOptions().setSocketType(socketType)...);
        manager.registerSocketListener(this);
        manager.connect();
        
        //断开
        manager.disconnect();
        
        //销毁的时候记得 移除监听
        manager.unRegisterSocketListener()
        //or
        manager.removeAllSocketListener();
```
