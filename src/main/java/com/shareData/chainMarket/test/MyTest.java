package com.shareData.chainMarket.test;


import com.shareData.chainMarket.i.Central;

import java.util.Map;

@Central(url = "/login")
public class MyTest {
    @Central(url = "/wxLogin")
    public String ok (String ma, Map<Object,Object> map){
        System.out.println("来了===");
     return "ok";
    }
}
