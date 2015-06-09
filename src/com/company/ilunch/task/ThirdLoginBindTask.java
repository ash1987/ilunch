package com.company.ilunch.task;

import java.io.InputStream;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.company.ilunch.bean.LoginBean;
import com.company.ilunch.net.AsyncRunner;
import com.company.ilunch.ui.LoginActivity;
import com.company.ilunch.utils.LogUtil;

public class ThirdLoginBindTask extends AsyncRunner<LoginBean> {

	@Override
	protected LoginBean paserJSON(String json) {
		LogUtil.i(LoginActivity.TAG, "判断第三方账号绑定json == " + json);
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
