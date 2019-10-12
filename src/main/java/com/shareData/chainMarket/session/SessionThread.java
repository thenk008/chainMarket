package com.shareData.chainMarket.session;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SessionThread implements Runnable {
    private Map<Long, SessionMessage> sessionMessageMap = SessionCore.getSessionMessage();
    private List<Long> sessionList = new ArrayList<>();

    @Override
    public void run() {
        while (true) {
            try {
                if (sessionMessageMap.size() > 0) {
                    for (Map.Entry<Long, SessionMessage> entry : sessionMessageMap.entrySet()) {
                        SessionMessage sessionMessage = entry.getValue();
                        long key = entry.getKey();
                        if (sessionMessage.up() > 1800) {//移除
                            sessionList.add(key);
                        }
                    }
                    for (long key : sessionList) {
                        sessionMessageMap.remove(key);
                    }
                    sessionList.clear();
                }
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
