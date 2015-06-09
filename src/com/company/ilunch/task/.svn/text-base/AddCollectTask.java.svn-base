package com.company.ilunch.task;

import java.io.InputStream;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.company.ilunch.bean.AddCollectBean;
import com.company.ilunch.net.AsyncRunner;
import com.company.ilunch.ui.LoginActivity;
import com.company.ilunch.utils.LogUtil;

/**
 * 描述：发起评论接口json数据解析
 */

public class AddCollectTask extends AsyncRunner<AddCollectBean> {

	@Override
	protected AddCollectBean paserJSON(String json) {
		LogUtil.i(LoginActivity.TAG, "发起评论接口json == " + json);
		JSONObject object = JSON.parseObject(json);
		if (object != null) {
			return JSON.toJavaObject(object, AddCollectBean.class);
		}
		
		return null;
	}

	@Override
	protected AddCollectBean paserXML(InputStream inputStream) {
		return null;
	}
}
