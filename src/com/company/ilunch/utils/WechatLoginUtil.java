package com.company.ilunch.utils;

import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.SendAuth;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import android.content.Context;

/**
 * 描述：微信登录工具类
 */

public class WechatLoginUtil {
	public static final String APP_ID = "";
	private static final String SCOPE = "snsapi_userinfo";
	private static final String STATE = "wechat_sdk_demo";
	
	public IWXAPI WXapi;
	public Context context;
	
	public WechatLoginUtil(Context mContext) {
		this.context = mContext;
		
		WXapi = WXAPIFactory.createWXAPI(context, APP_ID, true);
		WXapi.registerApp(APP_ID);
	}

	// 开始登录
	public void login() {
		SendAuth.Req req = new SendAuth.Req();
		req.scope = SCOPE;
		req.state = STATE;
		WXapi.sendReq(req);
	}
}
