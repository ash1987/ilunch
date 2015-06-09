package com.company.ilunch.task;

import java.io.InputStream;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.company.ilunch.bean.AddInteractionBean;
import com.company.ilunch.net.AsyncRunner;
import com.company.ilunch.ui.LoginActivity;
import com.company.ilunch.utils.LogUtil;

/**
 * 描述：提交互动接口json数据解析
 */

public class AddInteractionTask extends AsyncRunner<AddInteractionBean> {

	@Override
	protected AddInteractionBean paserJSON(String json) {
		LogUtil.i(LoginActivity.TAG, "提交互动接口json == " + json);
		JSONObject object = JSON.parseObject(json);
		if (object != null) {
			return JSON.toJavaObject(object, AddInteractionBean.class);
		}
		
		return null;
	}

	@Override
	protected AddInteractionBean paserXML(InputStream inputStream) {
		return null;
	}
}
