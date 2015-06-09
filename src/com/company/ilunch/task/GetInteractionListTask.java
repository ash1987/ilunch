package com.company.ilunch.task;

import java.io.InputStream;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.company.ilunch.bean.GetInteractionListBean;
import com.company.ilunch.net.AsyncRunner;
import com.company.ilunch.ui.LoginActivity;
import com.company.ilunch.utils.LogUtil;

/**
 * 描述：获取互动列表接口json数据解析
 */

public class GetInteractionListTask extends AsyncRunner<GetInteractionListBean> {

	@Override
	protected GetInteractionListBean paserJSON(String json) {
		LogUtil.i(LoginActivity.TAG, "获取互动列表接口json == " + json);
		JSONObject object = JSON.parseObject(json);
		if (object != null) {
			return JSON.toJavaObject(object, GetInteractionListBean.class);
		}
		
		return null;
	}

	@Override
	protected GetInteractionListBean paserXML(InputStream inputStream) {
		return null;
	}
}
