package com.company.ilunch.task;

import java.io.InputStream;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.company.ilunch.bean.SubmitMyOrderBean;
import com.company.ilunch.net.AsyncRunner;
import com.company.ilunch.ui.LoginActivity;
import com.company.ilunch.utils.LogUtil;

/**
 * 描述：提交订单接口json数据解析
 */

public class SubmitMyOrderTask extends AsyncRunner<SubmitMyOrderBean> {

	@Override
	protected SubmitMyOrderBean paserJSON(String json) {
		LogUtil.i(LoginActivity.TAG, "提交订单接口json == " + json);
		JSONObject object = JSON.parseObject(json);
		if (object != null) {
			return JSON.toJavaObject(object, SubmitMyOrderBean.class);
		}
		
		return null;
	}

	@Override
	protected SubmitMyOrderBean paserXML(InputStream inputStream) {
		return null;
	}
}
