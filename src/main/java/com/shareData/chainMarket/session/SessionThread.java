package com.shareData.chainMarket.session;

import java.util.Map;

public class SessionThread implements Runnable {
    Map<Long, SessionMessage> sessionMessageMap = SessionCore.getSessionMessage();

    @Override
    public void run() {
        while (true) {
            try {
                if (sessionMessageMap.size() > 0) {
                    for (Map.Entry<Long, SessionMessage> entry : sessionMessageMap.entrySet()) {
                        SessionMessage sessionMessage = entry.getValue();
                        long key = entry.getKey();
                        if (sessionMessage.up() > 1800) {//移除
                            sessionMessageMap.remove(key);
                        }
                    }
                }
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
