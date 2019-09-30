package com.shareData.chainMarket;

import java.net.InetSocketAddress;

import com.shareData.chainMarket.constant.Config;
import com.shareData.chainMarket.constant.HttpsSetting;
import com.shareData.chainMarket.scan.ScanControl;
import com.shareData.chainMarket.session.SessionThread;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;
import io.netty.handler.ssl.SslHandler;

public class HttpServer {
    public void start(int port, String rootUrl, String delimiter, int maxLength, String webSocektUrl,
                      WebSocketBack webSocketBack) {
        if (rootUrl != null && delimiter != null && maxLength > 0 && webSocektUrl != null
                && webSocketBack != null) {
            ScanControl scanControl = new ScanControl();
            try {
                Config.setMessage_Max(maxLength);
                Config.setPOINTER(delimiter);
                Config.setRootUrl(rootUrl);
                Config.setWebSocketUrl(webSocektUrl);
                scanControl.start(rootUrl);
                Thread sessionTh = new Thread(new SessionThread());
                sessionTh.setDaemon(true);
                sessionTh.start();
                connect(port, webSocketBack);
            } catch (Exception e) {
                e.printStackTrace();
            }

        } else {
            System.out.println("服务参数异常，启动失败");
        }
    }

    private void connect(int port, WebSocketBack webSocketBack) {
        EventLoopGroup group = new NioEventLoopGroup();
        EventLoopGroup boss = new NioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(group, boss).channel(NioServerSocketChannel.class).childOption(ChannelOption.TCP_NODELAY, true)
                    .childHandler(new Em(webSocketBack));
            Channel nel = b.bind(new InetSocketAddress(port)).sync().channel();
            System.out.println("http server start port open :" + port);
            System.out.println("welcom to chainMarket");
            nel.closeFuture().sync();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            group.shutdownGracefully();
            boss.shutdownGracefully();
        }
    }
}

class Em extends ChannelInitializer<SocketChannel> {
    private WebSocketBack webSocketBack;

    Em(WebSocketBack webSocketBack) {
        this.webSocketBack = webSocketBack;
    }

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        if (HttpsSetting.sslEnabled) {
            //务必放在第一位
            ch.pipeline().addLast("sslHandler", new SslHandler(HttpSslContextFactory.createSSLEngine()));
        }
        ch.pipeline().addLast("http-decoder", new HttpRequestDecoder());
        ch.pipeline().addLast(new HttpObjectAggregator(Config.getFileMaxLength()));
        ch.pipeline().addLast("http-encoder", new HttpResponseEncoder());
        ch.pipeline().addLast(new Http(webSocketBack));
    }

}