package com.shareData.chainMarket;

import io.netty.channel.ChannelHandlerContext;

public abstract class WebSocketBack {
    public void active(ChannelHandlerContext ctx) {//激活

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
