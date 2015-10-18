package com.company.ilunch.task;

import java.io.InputStream;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.company.ilunch.bean.GetYzmBean;
import com.company.ilunch.net.AsyncRunner;
import com.company.ilunch.ui.LoginActivity;
import com.company.ilunch.utils.LogUtil;

/**
 * 描述：获取验证码接口json数据解析
 */

public class GetYzmTask extends AsyncRunner<GetYzmBean> {

	@Override
	protected GetYzmBean paserJSON(String json) {
		LogUtil.i(LoginActivity.TAG, "获取验证码接口json == " + json);
		JSONObject object = JSON.parseObject(json);
		if (object != null) {
			return JSON.toJavaObject(object, GetYzmBean.class);
		}
		
		return null;
	}

	@Override
	protected GetYzmBean paserXML(InputStream inputStream) {
		return null;
	}
}
