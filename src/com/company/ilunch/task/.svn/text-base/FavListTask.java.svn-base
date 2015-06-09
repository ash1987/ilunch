package com.company.ilunch.task;

import java.io.InputStream;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.company.ilunch.bean.FavBean;
import com.company.ilunch.net.AsyncRunner;
import com.company.ilunch.ui.LoginActivity;
import com.company.ilunch.utils.LogUtil;

/**
 * 描述：收藏json数据解析
 */

public class FavListTask extends AsyncRunner<FavBean> {

	@Override
	protected FavBean paserJSON(String json) {
		LogUtil.i(LoginActivity.TAG, "收藏商品列表json == " + json);
		JSONObject object = JSON.parseObject(json);
		if (object != null) {
			return JSON.toJavaObject(object, FavBean.class);
		}
		return null;
	}

	@Override
	protected FavBean paserXML(InputStream inputStream) {
		return null;
	}

}
