package com.shareData.chainMarket.tools;
import com.shareData.chainMarket.agreement.ShareMessage;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class ShareAgreement {
	public ShareMessage getMessage(byte[] data) {
		ShareMessage message = null;
		int url = (data[8] & 0xff) | ((data[9] & 0xff) << 8);// url长度
		int dl = data.length;
		byte[] ur = new byte[url];// uri
		byte[] body = new byte[dl - (10 + url)];// body
		int j = 0;
		int k = 0;
		long httpId = 0L;
		for (int h = 0; h < 8; h++) {
			httpId = ((data[h] & 0xffL) << (h * 8)) | httpId;
		}
		for (int i = 10; i < dl; i++) {
			if (k < url) {
				ur[k] = data[i];
				k++;
			} else {
				body[j] = data[i];
				j++;
			}
		}
		try {
			String uri = new String(ur, "UTF-8");// url及参数
			String bodyMessage = new String(body, "UTF-8");// body体
			//System.out.println("uri==" + uri + ",bodyMessage===" + bodyMessage + ",httpId===" + httpId);
			message = getPojo(uri, bodyMessage);
			message.setHttpId(httpId);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return message;
	}

	public ShareMessage getPojo(String uri, String bodyMessage) {
		Map<Object, Object> map = new HashMap<>();
		String params;// get 参数
		if (uri.indexOf("?") > 0) {// 存在GET参数
			params = uri.split("\\?")[1];
			uri = uri.split("\\?")[0];
			String[] names = params.split("&");
			for (String name : names) {
				String[] nameAndValue = name.split("=");
				map.put(nameAndValue[0], nameAndValue[1]);
			}
		}
		ShareMessage share = new ShareMessage();
		share.setBody(bodyMessage);
		share.setUri(uri);
		share.setParams(map);
		return share;
	}
}
