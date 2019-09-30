package com.shareData.chainMarket.constant;

import io.netty.util.AttributeKey;

public class Config {
    private static byte[] POINTER;//分隔符
    private static int Message_Max;//信息包最大长度
    private static String rootUrl;//根路径
    private static String webSocketUrl;//websocketURL
    private static int fileMaxLength = 6553666;//文件最大长度

    public static int getFileMaxLength() {
        return fileMaxLength;
    }

    public static void setFileMaxLength(int fileMaxLength) {
        Config.fileMaxLength = fileMaxLength;
    }

    public static String getWebSocketUrl() {
        return webSocketUrl;
    }

    public static void setWebSocketUrl(String webSocketUrl) {
        Config.webSocketUrl = webSocketUrl;
    }

    public static final AttributeKey<Long> HTTP_ID = AttributeKey.valueOf("httpId");

    public static String getRootUrl() {
        return rootUrl;
    }

    public static void setRootUrl(String rootUrl) {
        Config.rootUrl = rootUrl;
    }

    public static byte[] getPOINTER() {
        return POINTER;
    }

    public static void setPOINTER(String POINTER) {
        Config.POINTER = POINTER.getBytes();
    }

    public static int getMessage_Max() {
        return Message_Max;
    }

    public static void setMessage_Max(int message_Max) {
        Message_Max = message_Max;
    }
}
