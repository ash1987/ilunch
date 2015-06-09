package com.company.ilunch.task;

import java.io.InputStream;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.company.ilunch.bean.GetSTemplateListBean;
import com.company.ilunch.net.AsyncRunner;
import com.company.ilunch.ui.LoginActivity;
import com.company.ilunch.utils.LogUtil;

/**
 * 描述：获取备注接口json数据解析
 */

public class GetSTemplateListTask extends AsyncRunner<GetSTemplateListBean> {

	@Override
	protected GetSTemplateListBean paserJSON(String json) {
		LogUtil.i(LoginActivity.TAG, "获取备注接口json == " + json);
		JSONObject object = JSON.parseObject(json);
		if (object != null) {
			return JSON.toJavaObject(object, GetSTemplateListBean.class);
		}
		
		return null;
	}

	@Override
	protected GetSTemplateListBean paserXML(InputStream inputStream) {
		return null;
	}
}
