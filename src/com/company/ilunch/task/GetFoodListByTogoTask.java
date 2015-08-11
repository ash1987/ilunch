package com.company.ilunch.task;

import java.io.InputStream;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.company.ilunch.bean.GetFoodListByTogoBean;
import com.company.ilunch.net.AsyncRunner;
import com.company.ilunch.ui.LoginActivity;
import com.company.ilunch.utils.LogUtil;

/**
 * 描述：获取商家菜品列表接口json数据解析
 */

public class GetFoodListByTogoTask extends AsyncRunner<GetFoodListByTogoBean> {

	@Override
	protected GetFoodListByTogoBean paserJSON(String json) {
		LogUtil.i(LoginActivity.TAG, "获取商家菜品列表接口json == " + json);
		JSONObject object = JSON.parseObject(json);
		if (object != null) {
			return JSON.toJavaObject(object, GetFoodListByTogoBean.class);
		}
		
		return null;
	}

	@Override
	protected GetFoodListByTogoBean paserXML(InputStream inputStream) {
		return null;
	}
}
