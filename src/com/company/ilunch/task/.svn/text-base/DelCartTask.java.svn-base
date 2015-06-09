package com.company.ilunch.task;

import java.io.InputStream;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.company.ilunch.bean.DelCartBean;
import com.company.ilunch.net.AsyncRunner;
import com.company.ilunch.ui.LoginActivity;
import com.company.ilunch.utils.LogUtil;

/**
 * 描述：删除购物车接口json数据解析
 */

public class DelCartTask extends AsyncRunner<DelCartBean> {

	@Override
	protected DelCartBean paserJSON(String json) {
		LogUtil.i(LoginActivity.TAG, "删除购物车接口json == " + json);
		JSONObject object = JSON.parseObject(json);
		if (object != null) {
			return JSON.toJavaObject(object, DelCartBean.class);
		}
		
		return null;
	}

	@Override
	protected DelCartBean paserXML(InputStream inputStream) {
		return null;
	}
}
