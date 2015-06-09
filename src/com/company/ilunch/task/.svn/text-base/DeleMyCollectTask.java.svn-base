package com.company.ilunch.task;

import java.io.InputStream;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.company.ilunch.bean.DeleMyCollectBean;
import com.company.ilunch.net.AsyncRunner;
import com.company.ilunch.ui.LoginActivity;
import com.company.ilunch.utils.LogUtil;

/**
 * 描述：删除收藏接口json数据解析
 */

public class DeleMyCollectTask extends AsyncRunner<DeleMyCollectBean> {

	@Override
	protected DeleMyCollectBean paserJSON(String json) {
		LogUtil.i(LoginActivity.TAG, "删除收藏接口json == " + json);
		JSONObject object = JSON.parseObject(json);
		if (object != null) {
			return JSON.toJavaObject(object, DeleMyCollectBean.class);
		}
		
		return null;
	}

	@Override
	protected DeleMyCollectBean paserXML(InputStream inputStream) {
		return null;
	}
}
