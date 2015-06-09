package com.company.ilunch.task;

import java.io.InputStream;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.company.ilunch.bean.GetSectionBean;
import com.company.ilunch.net.AsyncRunner;
import com.company.ilunch.ui.LoginActivity;
import com.company.ilunch.utils.LogUtil;

/**
 * 描述：获取区域列表接口json数据解析
 */

public class GetSectionTask extends AsyncRunner<GetSectionBean> {

	@Override
	protected GetSectionBean paserJSON(String json) {
		LogUtil.i(LoginActivity.TAG, "获取区域列表接口json == " + json);
		JSONObject object = JSON.parseObject(json);
		if (object != null) {
			return JSON.toJavaObject(object, GetSectionBean.class);
		}
		
		return null;
	}

	@Override
	protected GetSectionBean paserXML(InputStream inputStream) {
		return null;
	}
}
