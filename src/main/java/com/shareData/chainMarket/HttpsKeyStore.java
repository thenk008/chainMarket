package com.shareData.chainMarket;

import com.shareData.chainMarket.constant.HttpsSetting;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class HttpsKeyStore {
    public static InputStream getKeyStoreStream() {
        InputStream inStream = null;
        try {
            inStream = new FileInputStream(HttpsSetting.keystorePath);
        } catch (FileNotFoundException e) {
        }
        return inStream;
    }
    /**
     * 获取安全证书密码 (用于创建KeyManagerFactory)
     * @date 2012-9-11
     * @version V1.0.0
     * @return char[]
     */
    public static char[] getCertificatePassword() {

        return HttpsSetting.certificatePassword.toCharArray();
    }
    /**
     * 获取密钥密码(证书别名密码) (用于创建KeyStore)
     * @date 2012-9-11
     * @version V1.0.0
     * @return char[]
     */
    public static char[] getKeyStorePassword() {
        return HttpsSetting.keystorePassword.toCharArray();
    }
}
