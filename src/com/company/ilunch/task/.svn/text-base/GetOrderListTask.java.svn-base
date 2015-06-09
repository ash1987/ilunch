package com.company.ilunch.task;

import java.io.InputStream;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.company.ilunch.bean.GetOrderListBean;
import com.company.ilunch.net.AsyncRunner;
import com.company.ilunch.ui.LoginActivity;
import com.company.ilunch.utils.LogUtil;

/**
 * 描述：获取订单列表json数据解析
 */

public class GetOrderListTask extends AsyncRunner<GetOrderListBean> {

	@Override
	protected GetOrderListBean paserJSON(String json) {
		LogUtil.i(LoginActivity.TAG, "获取订单列表json == " + json);
		JSONObject object = JSON.parseObject(json);
		if (object != null) {
			return JSON.toJavaObject(object, GetOrderListBean.class);
		}
		return null;
	}

	@Override
	protected GetOrderListBean paserXML(InputStream inputStream) {
		return null;
	}

}
