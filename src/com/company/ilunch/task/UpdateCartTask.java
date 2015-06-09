package com.company.ilunch.task;

import java.io.InputStream;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.company.ilunch.bean.UpdateCartBean;
import com.company.ilunch.net.AsyncRunner;
import com.company.ilunch.ui.LoginActivity;
import com.company.ilunch.utils.LogUtil;

/**
 * 描述：更新购物车接口json数据解析
 */

public class UpdateCartTask extends AsyncRunner<UpdateCartBean> {

	@Override
	protected UpdateCartBean paserJSON(String json) {
		LogUtil.i(LoginActivity.TAG, "更新购物车接口json == " + json);
		JSONObject object = JSON.parseObject(json);
		if (object != null) {
			return JSON.toJavaObject(object, UpdateCartBean.class);
		}
		
		return null;
	}

	@Override
	protected UpdateCartBean paserXML(InputStream inputStream) {
		return null;
	}
}
