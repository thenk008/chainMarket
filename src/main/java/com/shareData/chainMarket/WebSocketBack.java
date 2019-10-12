package com.shareData.chainMarket;

import com.shareData.chainMarket.session.WebSocketManager;
import io.netty.channel.ChannelHandlerContext;

public abstract class WebSocketBack {
    public void active(ChannelHandlerContext ctx) {//激活
        WebSocketManager.register(1,ctx);
        WebSocketManager.putMessage(1,"您有新的订单");
        WebSocketManager.closeChannel(1);
    }
    public void error(ChannelHandlerContext ctx){

    }
    public String getText(String message, ChannelHandlerContext ctx) {
        return null;
    }

    public byte[] getBuf(byte[] message, ChannelHandlerContext ctx) {
        return null;
    }
}
