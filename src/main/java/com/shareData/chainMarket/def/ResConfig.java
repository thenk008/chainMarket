package com.shareData.chainMarket.def;

import java.util.HashMap;
import java.util.Map;

public class ResConfig {
    private static ResConfig CONFIG = new ResConfig();
    private static Map<String, String> myEntity = new HashMap<>();
    private static String control;

    private ResConfig() {
    }

    public Map<String, String> getMyEntity() {
        return myEntity;
    }

    public void setControl(String url) {
        control = url;
    }

    public String getControl() {
        return control;
    }

    public static ResConfig get() {
        return CONFIG;
    }
}
