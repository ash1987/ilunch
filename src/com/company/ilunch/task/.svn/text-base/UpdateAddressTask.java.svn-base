package com.company.ilunch.task;

import java.io.InputStream;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.company.ilunch.bean.UpdateAddressBean;
import com.company.ilunch.net.AsyncRunner;
import com.company.ilunch.ui.LoginActivity;
import com.company.ilunch.utils.LogUtil;

/**
 * 描述：修改地址接口json数据解析
 */

public class UpdateAddressTask extends AsyncRunner<UpdateAddressBean> {

	@Override
	protected UpdateAddressBean paserJSON(String json) {
		LogUtil.i(LoginActivity.TAG, "修改地址接口json == " + json);
		JSONObject object = JSON.parseObject(json);
		if (object != null) {
			return JSON.toJavaObject(object, UpdateAddressBean.class);
		}
		
		return null;
	}

	@Override
	protected UpdateAddressBean paserXML(InputStream inputStream) {
		return null;
	}
}
