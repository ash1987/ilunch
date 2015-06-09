package com.company.ilunch.task;

import java.io.InputStream;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.company.ilunch.bean.AddMyCollectBean;
import com.company.ilunch.net.AsyncRunner;
import com.company.ilunch.ui.LoginActivity;
import com.company.ilunch.utils.LogUtil;

/**
 * 描述：添加收藏接口json数据解析
 */

public class AddMyCollectTask extends AsyncRunner<AddMyCollectBean> {

	@Override
	protected AddMyCollectBean paserJSON(String json) {
		LogUtil.i(LoginActivity.TAG, "添加收藏接口json == " + json);
		JSONObject object = JSON.parseObject(json);
		if (object != null) {
			return JSON.toJavaObject(object, AddMyCollectBean.class);
		}
		
		return null;
	}

	@Override
	protected AddMyCollectBean paserXML(InputStream inputStream) {
		return null;
	}
}
