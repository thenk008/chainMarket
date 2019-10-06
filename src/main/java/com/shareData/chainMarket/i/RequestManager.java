package com.shareData.chainMarket.i;

import io.netty.channel.ChannelHandlerContext;

import java.io.UnsupportedEncodingException;

public interface RequestManager {
	//返回管理后台登录用户信息
	void response(ChannelHandlerContext ch,Object msg,byte httpCode) throws UnsupportedEncodingException;
}
