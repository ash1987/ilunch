package com.company.ilunch.wxapi;

import com.company.ilunch.utils.WechatLoginUtil;
import com.tencent.mm.sdk.openapi.BaseReq;
import com.tencent.mm.sdk.openapi.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

public class WXEntryActivity extends Activity implements IWXAPIEventHandler {
	private IWXAPI api;
	private BaseResp resp = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		api = WXAPIFactory.createWXAPI(this, WechatLoginUtil.APP_ID, false);
		api.handleIntent(getIntent(), this);
	}

	// 微信发送请求到第三方应用时，会回调到该方法
	@Override
	public void onReq(BaseReq req) {
		finish();
	}

	// 第三方应用发送到微信的请求处理后的响应结果，会回调到该方法
	@Override
	public void onResp(BaseResp resp) {
		String result = "";
		if (resp != null) {
			resp = resp;
		}
		
		switch (resp.errCode) {
		case BaseResp.ErrCode.ERR_OK:
			result = "发送成功";
			Toast.makeText(this, result, Toast.LENGTH_LONG).show();
			finish();
			break;
		case BaseResp.ErrCode.ERR_USER_CANCEL:
			result = "发送取消";
			Toast.makeText(this, result, Toast.LENGTH_LONG).show();
			finish();
			break;
		case BaseResp.ErrCode.ERR_AUTH_DENIED:
			result = "发送被拒绝";
			Toast.makeText(this, result, Toast.LENGTH_LONG).show();
			finish();
			break;
		default:
			result = "发送返回";
			Toast.makeText(this, result, Toast.LENGTH_LONG).show();
			finish();
			break;
		}
	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		setIntent(intent);
		api.handleIntent(intent, this);
		finish();
	}
}