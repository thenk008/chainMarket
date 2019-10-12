package com.shareData.chainMarket.test;


import com.shareData.chainMarket.FileAndName;
import com.shareData.chainMarket.i.Central;

import java.util.List;
import java.util.Map;

@Central(url = "/login")
public class MyTest {
    @Central(url = "/getAddress")
    public String ok(String message , Map<Object,Object> map) {
     System.out.println("yes============");
        return "right";
    }
}
