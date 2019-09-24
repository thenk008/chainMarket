package com.shareData.chainMarket.test;

import com.shareData.chainMarket.i.Central;

import java.util.Map;

@Central(url = "/productType")
public class ProductTypeController {

    /**
     * 获取所有商品分类
     *
     * @param message
     * @param map
     * @return
     */
    @Central(url = "/list")
    public String list(String message, Map<Object, Object> map){
        System.out.println("abcd");
        return "abcd";
    }
}
