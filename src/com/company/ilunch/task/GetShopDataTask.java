package com.company.ilunch.task;

import java.io.InputStream;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.company.ilunch.bean.GetShopDataBean;
import com.company.ilunch.net.AsyncRunner;
import com.company.ilunch.ui.LoginActivity;
import com.company.ilunch.utils.LogUtil;

/**
 * 描述：获取菜品分类接口json数据解析
 */

public class GetShopDataTask extends AsyncRunner<GetShopDataBean> {

	@Override
	protected GetShopDataBean paserJSON(String json) {
		LogUtil.i(LoginActivity.TAG, "获取菜品分类接口json == " + json);
		JSONObject object = JSON.parseObject(json);
		if (object != null) {
			return JSON.toJavaObject(object, GetShopDataBean.class);
		}
		
		return null;
	}

	@Override
	protected GetShopDataBean paserXML(InputStream inputStream) {
		return null;
	}
}
