package com.shareData.chainMarket.session;

import com.shareData.chainMarket.constant.Config;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.util.Attribute;

import java.util.HashMap;
import java.util.Map;

public class WebSocketManager {
    private static WebSocketManager webSocketManager = new WebSocketManager();
    private static Map<Long, ChannelHandlerContext> channelMap = new HashMap<>();

    public static void register(long key, ChannelHandlerContext arg0) {//注册信道
        Attribute<Long> attribute = arg0.channel().attr(Config.CHANNEL_ID);
        attribute.set(key);
        channelMap.put(key, arg0);
    }

    public static void closeChannel(long key) {//关闭链接
        ChannelHandlerContext ch = channelMap.get(key);
        if (ch != null) {
            ch.close();
            channelMap.remove(key);
        }
    }

    public static void putMessage(long key, String msg) {//webSocket 发送字符串
        if (msg != null) {
            ChannelHandlerContext ch = channelMap.get(key);
            if (ch != null) {
                ch.writeAndFlush(new TextWebSocketFrame(msg));
            }
        }
    }

    public static void putByte(long key, byte[] msg) {//webSocket 推送字节码
        if (msg != null) {
            ChannelHandlerContext ch = channelMap.get(key);
            if (ch != null) {
                ByteBuf echo = Unpooled.copiedBuffer(msg);
                ch.writeAndFlush(echo);
            }
        }
    }

    private WebSocketManager() {
    }

}
