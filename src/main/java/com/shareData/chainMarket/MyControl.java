package com.shareData.chainMarket;

import com.shareData.chainMarket.agreement.ShareMessage;
import com.shareData.chainMarket.def.ResConfig;
import com.shareData.chainMarket.def.UrmAndUrl;
import com.shareData.chainMarket.i.RequestManager;
import io.netty.channel.ChannelHandlerContext;
import java.util.Map;

public class MyControl extends MySon {
    Map<String, String> urlMap = ResConfig.get().getMyEntity();

    public boolean my(ShareMessage share, RequestManager http, ChannelHandlerContext ch) {
        Class<?> c;
        boolean isRight = true;
        try {
            //从uri里将实例化的类截取出来
            UrmAndUrl uri = uri(share.getUri());
            if (uri != null) {
                c = Class.forName(uri.getUri());
                setMyboss(c);
                //System.out.println("uri=="+uri.getUrl());
                //System.out.println("shareuri=="+share.getUri());
                String urm = share.getUri().substring(uri.getUrl().length() - 1);
                isRight = body(share.getBody(), share.getParams(), http, ch, urm);
            } else {
                System.out.println("NOT FOUND URL1");
                isRight = false;
            }
        } catch (Exception e) {
            e.printStackTrace();
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