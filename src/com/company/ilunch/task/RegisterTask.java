package com.company.ilunch.task;

import java.io.InputStream;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.company.ilunch.bean.RegisterBean;
import com.company.ilunch.net.AsyncRunner;
import com.company.ilunch.ui.LoginActivity;
import com.company.ilunch.utils.LogUtil;

/**
 * 描述：用户注册接口json数据解析
 */

public class RegisterTask extends AsyncRunner<RegisterBean> {

	@Override
	protected RegisterBean paserJSON(String json) {
		LogUtil.i(LoginActivity.TAG, "用户注册接口json == " + json);
		JSONObject object = JSON.parseObject(json);
		if (object != null) {
			return JSON.toJavaObject(object, RegisterBean.class);
		}
		
		return null;
	}

	@Override
	protected RegisterBean paserXML(InputStream inputStream) {
		return null;
	}
}
