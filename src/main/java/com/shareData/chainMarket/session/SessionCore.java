package com.shareData.chainMarket.session;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.shareData.chainMarket.tools.HttpIdCreator;

public class SessionCore {
	private static final SessionCore SESSION = new SessionCore();
	private static Map<Long, Map<String, Object>> sessionMaps = new ConcurrentHashMap<>();
    public Map<String,Object> getSession(long sessionid) {
    	return sessionMaps.get(sessionid);
    }
    public void putSession(long sessionid,Map<String, Object> map) {
    	sessionMaps.put(sessionid, map);
    	
    }
    public long createSession() {
    	long sessionid = HttpIdCreator.get().nextId();
    	sessionMaps.put(sessionid, new HashMap<String,Object>());
    	return sessionid;
    }
	private SessionCore() {
	}

	public static SessionCore get() {
		return SESSION;
	}
}
