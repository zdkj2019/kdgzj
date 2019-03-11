package com.zj.webservice;

import java.util.HashMap;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import com.zj.activity.FrameActivity;
import com.zj.activity.login.LoginActivity;
import com.zj.common.Constant;

public class WsClient {
	private Object result;

	public Object loginTest(HashMap<String, Object> param, String method,
			String webServiceName, Context mContext) {
		WebService server = new WebService();
		if ("ZD".equals(webServiceName)) {
			result = server.getWebService(Constant.STM_NAMESPACE, Constant.STM_WEBSERVICE_URL_dx, method,
					param);
		}
		return result;
	}

}
