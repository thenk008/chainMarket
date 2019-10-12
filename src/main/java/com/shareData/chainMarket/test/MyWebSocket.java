package com.shareData.chainMarket.test;

import com.shareData.chainMarket.WebSocketBack;
import io.netty.channel.ChannelHandlerContext;

public class MyWebSocket extends WebSocketBack {
    @Override
    public void active(ChannelHandlerContext ctx) {
        System.out.println("激活了======");
    }

    @Override
    public String getText(String message, ChannelHandlerContext ctx) {
        return "ok";
    }
}
