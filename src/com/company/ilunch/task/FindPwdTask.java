package com.company.ilunch.task;

import java.io.InputStream;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.company.ilunch.bean.FindPwdBean;
import com.company.ilunch.net.AsyncRunner;
import com.company.ilunch.ui.LoginActivity;
import com.company.ilunch.utils.LogUtil;

/**
 * 描述：找回密码接口json数据解析
 */

public class FindPwdTask extends AsyncRunner<FindPwdBean> {

	@Override
	protected FindPwdBean paserJSON(String json) {
		LogUtil.i(LoginActivity.TAG, "找回密码接口json == " + json);
		JSONObject object = JSON.parseObject(json);
		if (object != null) {
			return JSON.toJavaObject(object, FindPwdBean.class);
		}
		
		return null;
	}

	@Override
	protected FindPwdBean paserXML(InputStream inputStream) {
		return null;
	}
}
