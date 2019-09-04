package com.shareData.chainMarket.test;

import com.shareData.chainMarket.HttpServer;
import com.shareData.chainMarket.constant.HttpsSetting;

public class App {
    public static void main(String[] args) {
        HttpsSetting.sslEnabled =false;
        HttpServer httpServer = new HttpServer();
        httpServer.start(8080, "com.shareData.chainMarket.test", "$_", 1024,
                "ws://127.0.0.1:8080/websocket", new MyWebSocket());
    }
}
