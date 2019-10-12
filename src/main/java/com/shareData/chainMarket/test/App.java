package com.shareData.chainMarket.test;

import com.shareData.chainMarket.HttpServer;
import com.shareData.chainMarket.constant.Config;
import com.shareData.chainMarket.constant.HttpsSetting;

public class App {
    public static void main(String[] args) {
        init();
        HttpServer httpServer = new HttpServer();
        httpServer.start(8080, "com.shareData.chainMarket.test", "$_", 1024,
                "ws://127.0.0.1:8080/websocket", new MyWebSocket());
    }
    public static void  init(){
        HttpsSetting.sslEnabled = false;
        Config.setFileMaxLength(6553666);
    }

}
