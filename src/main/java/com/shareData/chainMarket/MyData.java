package com.shareData.chainMarket;

import com.shareData.chainMarket.agreement.ShareMessage;
import com.shareData.chainMarket.def.ResConfig;
import com.shareData.chainMarket.def.UrmAndUrl;
import com.shareData.chainMarket.i.RequestManager;
import io.netty.channel.ChannelHandlerContext;

import java.util.List;
import java.util.Map;

public class MyData extends MySon {
    Map<String, String> urlMap = ResConfig.get().getMyEntity();

    public boolean my(List<FileAndName> fileAndNames, RequestManager http, ChannelHandlerContext ch, String url) {
        Class<?> c;
        boolean isRight = true;
        try {
            //从uri里将实例化的类截取出来
            UrmAndUrl uri = uri(url);
            if (uri != null) {
                c = Class.forName(uri.getUri());
                setMyboss(c);
                //System.out.println("uri=="+uri.getUrl());
                //System.out.println("shareuri=="+share.getUri());
                String urm = url.substring(uri.getUrl().length() - 1);
                isRight = data(fileAndNames, http, ch, urm);
            } else {
                System.out.println("不存在");
                isRight = false;
            }
        } catch (ClassNotFoundException e) {
            isRight = false;
        }
        return isRight;
    }

    public UrmAndUrl uri(String uri) {
        UrmAndUrl urmAndUrl = null;
        for (Map.Entry<String, String> entry : urlMap.entrySet()) {
            if (uri.indexOf(entry.getKey()) > -1) {
                urmAndUrl = new UrmAndUrl();
                urmAndUrl.setUri(entry.getValue());
                urmAndUrl.setUrl(entry.getKey());
                break;
            }
        }
        return urmAndUrl;
    }
}
