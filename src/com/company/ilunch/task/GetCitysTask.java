package com.company.ilunch.task;

import java.io.InputStream;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.company.ilunch.bean.GetCitysBean;
import com.company.ilunch.net.AsyncRunner;
import com.company.ilunch.ui.LoginActivity;
import com.company.ilunch.utils.LogUtil;

/**
 * 描述：获取城市列表接口json数据解析
 */

public class GetCitysTask extends AsyncRunner<GetCitysBean> {

	@Override
	protected GetCitysBean paserJSON(String json) {
		LogUtil.i(LoginActivity.TAG, "获取城市列表接口json == " + json);
		JSONObject object = JSON.parseObject(json);
		if (object != null) {
			return JSON.toJavaObject(object, GetCitysBean.class);
		}
		
		return null;
	}

	@Override
	protected GetCitysBean paserXML(InputStream inputStream) {
		return null;
	}
}
