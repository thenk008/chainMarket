package com.shareData.chainMarket.session;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.shareData.chainMarket.tools.HttpIdCreator;

public class SessionCore {
    private static SessionCore SESSION = new SessionCore();
    private static Map<Long, SessionMessage> session = new HashMap<>();
    public static  SessionCore get(){
        return  SESSION;
    }
    public static Map<Long, SessionMessage> getSessionMessage() {
        return session;
    }

    public static SessionMessage getSession(long sessionId) {
        return session.get(sessionId);
    }

    public static long createSession() {
        SessionMessage sessionMessage = new SessionMessage();
        long sessionId = HttpIdCreator.get().nextId();
        session.put(sessionId, sessionMessage);
        return sessionId;
    }

    private SessionCore() {
    }
}
