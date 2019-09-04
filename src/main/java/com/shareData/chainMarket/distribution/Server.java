package com.shareData.chainMarket.distribution;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

import java.net.InetSocketAddress;

public class Server {
	public  void start(long host) {
		EventLoopGroup group = new NioEventLoopGroup();
		EventLoopGroup boss = new NioEventLoopGroup();
		try {
			ServerBootstrap b = new ServerBootstrap();
			b.group(group, boss).channel(NioServerSocketChannel.class).childOption(ChannelOption.TCP_NODELAY, true)
			.handler(new LoggingHandler(LogLevel.INFO))
			.childHandler(new Et());
			Channel nel = b.bind(new InetSocketAddress((int)host)).sync().channel();
			nel.closeFuture().sync();
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			group.shutdownGracefully();
			boss.shutdownGracefully();
		}
	}

}

class Et extends ChannelInitializer<SocketChannel> {

	@Override
	protected void initChannel(SocketChannel ch) throws Exception {
		ByteBuf def = Unpooled.copiedBuffer("$_".getBytes());
		ch.pipeline().addLast(new DelimiterBasedFrameDecoder(2048,def));
		ch.pipeline().addLast(new PlayServer());
		
	}

}