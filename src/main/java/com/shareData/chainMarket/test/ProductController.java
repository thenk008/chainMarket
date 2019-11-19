package com.shareData.chainMarket.test;

import com.shareData.chainMarket.FileAndName;
import com.shareData.chainMarket.i.Central;

import java.util.List;

/**
 * @author tengyy
 * @date 2019/9/21 13:57
 */
@Central(url = "/product")
public class ProductController {

    @Central(url = "/list")
    public String list(List<FileAndName> forms) {
        return "a";
    }
}
