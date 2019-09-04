package com.shareData.chainMarket.agreement;

import java.util.Map;

public class ShareMessage {
	private String uri;
	private String body;
	private long httpId;
	private Map<Object, Object> params;
	public String getUri() {
		return uri;
	}

	public long getHttpId() {
		return httpId;
	}

	public void setHttpId(long httpId) {
		this.httpId = httpId;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public Map<Object, Object> getParams() {
		return params;
	}

	public void setParams(Map<Object, Object> params) {
		this.params = params;
	}

}
