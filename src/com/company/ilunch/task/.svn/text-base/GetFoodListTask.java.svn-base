package com.company.ilunch.task;

import java.io.InputStream;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.company.ilunch.bean.GetFoodListBean;
import com.company.ilunch.net.AsyncRunner;
import com.company.ilunch.ui.LoginActivity;
import com.company.ilunch.utils.LogUtil;

/**
 * 描述：获取菜品列表接口json数据解析
 */

public class GetFoodListTask extends AsyncRunner<GetFoodListBean> {

	@Override
	protected GetFoodListBean paserJSON(String json) {
		LogUtil.i(LoginActivity.TAG, "获取菜品列表接口json == " + json);
		GetFoodListBean bean = null;
		JSONObject object = JSON.parseObject(json);
		if (object != null) {
			try {
				bean = JSON.toJavaObject(object, GetFoodListBean.class);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return bean;
	}

	@Override
	protected GetFoodListBean paserXML(InputStream inputStream) {
		return null;
	}
}
