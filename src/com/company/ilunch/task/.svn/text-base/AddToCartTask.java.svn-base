package com.company.ilunch.task;

import java.io.InputStream;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.company.ilunch.bean.AddToCartBean;
import com.company.ilunch.net.AsyncRunner;
import com.company.ilunch.ui.LoginActivity;
import com.company.ilunch.utils.LogUtil;

/**
 * 描述：加入购物车接口json数据解析
 */

public class AddToCartTask extends AsyncRunner<AddToCartBean> {

	@Override
	protected AddToCartBean paserJSON(String json) {
		LogUtil.i(LoginActivity.TAG, "加入购物车接口json == " + json);
		JSONObject object = JSON.parseObject(json);
		if (object != null) {
			return JSON.toJavaObject(object, AddToCartBean.class);
		}
		
		return null;
	}

	@Override
	protected AddToCartBean paserXML(InputStream inputStream) {
		return null;
	}
}
