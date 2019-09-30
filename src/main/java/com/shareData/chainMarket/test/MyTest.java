package com.shareData.chainMarket.test;


import com.shareData.chainMarket.FileAndName;
import com.shareData.chainMarket.i.Central;

import java.util.List;

@Central(url = "/login")
public class MyTest {
    @Central(url = "/wxLogin")
    public String ok(List<FileAndName> fileAndNames) {
        for (FileAndName fileAndName : fileAndNames) {
            boolean isNull = fileAndName.getInputStream() != null ? true : false;
            System.out.println("name="+fileAndName.getName()+",text="+fileAndName.getText()+",file="+isNull);
        }
        return "right";
    }
}
