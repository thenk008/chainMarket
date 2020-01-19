package com.shareData.chainMarket.test;

import com.shareData.chainMarket.WebSocketBack;
import com.shareData.chainMarket.session.WebSocketManager;
import io.netty.channel.ChannelHandlerContext;

public class MyWebSocket extends WebSocketBack {
    @Override
    public void active(ChannelHandlerContext ctx) {
        System.out.println("激活了======");
    }

    @Override
    public String getText(String message, ChannelHandlerContext ctx) {
        WebSocketManager.register(1,ctx);
        WebSocketManager.putMessage(1,"发送信息");
        return "ok";
    }
}
