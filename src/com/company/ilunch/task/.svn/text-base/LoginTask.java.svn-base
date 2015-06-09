package com.company.ilunch.task;

import java.io.InputStream;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.company.ilunch.bean.LoginBean;
import com.company.ilunch.net.AsyncRunner;
import com.company.ilunch.ui.LoginActivity;
import com.company.ilunch.utils.LogUtil;

/**
 * 描述：登录接口json数据解析
 */

public class LoginTask extends AsyncRunner<LoginBean> {

	@Override
	protected LoginBean paserJSON(String json) {
		LogUtil.i(LoginActivity.TAG, "登录接口json == " + json);
		JSONObject object = JSON.parseObject(json);
		if (object != null) {
			return JSON.toJavaObject(object, LoginBean.class);
		}
		return null;
	}

	@Override
	protected LoginBean paserXML(InputStream inputStream) {
		return null;
	}
}
