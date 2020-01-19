package com.shareData.chainMarket.test;

import com.shareData.chainMarket.HttpServer;
import com.shareData.chainMarket.constant.Config;
import com.shareData.chainMarket.constant.HttpsSetting;

public class App {
    public static void main(String[] args) {
        init();
        //初始化chainMarket服务
        HttpServer httpServer = new HttpServer();
        //参数分别是 端口号，control类所在的包路径（接口入口类），
        // 允许接收的最大字节数（超过这个数量，将直接返回400错误码）
        //websocket url,
        //websocket 回调类
        //启动chainMarket服务
        httpServer.start(8080, "com.shareData.chainMarket.test", 1024,
                null, null);
        //若要启动WEBSOCKET服务，请将后两个参数传入
        //
//        httpServer.start(8080, "com.shareData.chainMarket.test", 1024,
//                "ws://127.0.0.1:8080/websocket", new MyWebSocket());
    }

    public static void init() {
        //是否启动SSL服务，若启动则服务器无法接收HTTP请求，只能接收HTTPS请求
        //反之也一样
        HttpsSetting.sslEnabled = false;//不启动SSL服务，不设置就是默认不启动
        HttpsSetting.keystorePath = "SSL证书在磁盘上的地址";
        HttpsSetting.certificatePassword = "SSL证书密码";
        HttpsSetting.keystorePassword = "SSL证书密码";//上下两者等同
        Config.setFileMaxLength(6553666);//上传文件的最大大小,单位BIT
    }

}
