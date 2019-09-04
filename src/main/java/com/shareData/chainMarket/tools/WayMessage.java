package com.shareData.chainMarket.tools;

public class WayMessage {
private int type;
private String data;
private long httpId;

public long getHttpId() {
	return httpId;
}
public void setHttpId(long httpId) {
	this.httpId = httpId;
}
public int getType() {
	return type;
}
public void setType(int type) {
	this.type = type;
}
public String getData() {
	return data;
}
public void setData(String data) {
	this.data = data;
}

}
