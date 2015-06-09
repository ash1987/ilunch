package com.company.ilunch.task;

import java.io.InputStream;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.company.ilunch.bean.AddMyAddressBean;
import com.company.ilunch.net.AsyncRunner;
import com.company.ilunch.ui.LoginActivity;
import com.company.ilunch.utils.LogUtil;

/**
 * 描述：添加地址接口json数据解析
 */

public class AddMyAddressTask extends AsyncRunner<AddMyAddressBean> {

	@Override
	protected AddMyAddressBean paserJSON(String json) {
		LogUtil.i(LoginActivity.TAG, "添加地址接口json == " + json);
		JSONObject object = JSON.parseObject(json);
		if (object != null) {
			return JSON.toJavaObject(object, AddMyAddressBean.class);
		}
		
		return null;
	}

	@Override
	protected AddMyAddressBean paserXML(InputStream inputStream) {
		return null;
	}
}
