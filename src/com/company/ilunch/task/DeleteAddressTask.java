package com.company.ilunch.task;

import java.io.InputStream;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.company.ilunch.bean.DeleteAddressBean;
import com.company.ilunch.net.AsyncRunner;
import com.company.ilunch.ui.LoginActivity;
import com.company.ilunch.utils.LogUtil;

/**
 * 描述：删除地址接口json数据解析
 */

public class DeleteAddressTask extends AsyncRunner<DeleteAddressBean> {

	@Override
	protected DeleteAddressBean paserJSON(String json) {
		LogUtil.i(LoginActivity.TAG, "删除地址接口json == " + json);
		JSONObject object = JSON.parseObject(json);
		if (object != null) {
			return JSON.toJavaObject(object, DeleteAddressBean.class);
		}
		
		return null;
	}

	@Override
	protected DeleteAddressBean paserXML(InputStream inputStream) {
		return null;
	}
}
