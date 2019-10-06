package com.shareData.chainMarket.one;

import com.shareData.chainMarket.WebSocketBack;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Pond {
    private static ExecutorService POOL = Executors.newFixedThreadPool(9);
    private static final Pond POD = new Pond();

    private Pond() {
    }

    public static Pond get() {
        return POD;
    }

    public void backBuf(ChannelHandlerContext ch, byte[] message, WebSocketBack socketMessage) {//断线重练
        Buf buf = new Buf(ch, message, socketMessage);
        POOL.execute(buf);
    }

    public void backText(ChannelHandlerContext ch, String message, WebSocketBack socketMessage) {//断线重练
        Text buf = new Text(ch, message, socketMessage);
        POOL.execute(buf);
    }
}

class Buf implements Runnable {//断线重连
    private ChannelHandlerContext ch;
    private byte[] message;
    private WebSocketBack socketMessage;

    public Buf(ChannelHandlerContext ch, byte[] message, WebSocketBack socketMessage) {
        this.ch = ch;
        this.message = message;
        this.socketMessage = socketMessage;
    }

    @Override
    public void run() {//触发回调
        byte[] br = socketMessage.getBuf(message,ch);
        if (br!=null) {
            ByteBuf echo = Unpooled.copiedBuffer(br);
            ch.writeAndFlush(echo);
        }
    }

}

class Text implements Runnable {//断线重连
    private ChannelHandlerContext ch;
    private String message;
    private WebSocketBack socketMessage;

    public Text(ChannelHandlerContext ch, String message, WebSocketBack socketMessage) {
        this.ch = ch;
        this.message = message;
        this.socketMessage = socketMessage;
    }

    @Override
    public void run() {//触发回调
        String br = socketMessage.getText(message,ch);
        if (br!=null) {
            ByteBuf echo = Unpooled.copiedBuffer(br.getBytes());
            ch.writeAndFlush(echo);
        }
    }

}

