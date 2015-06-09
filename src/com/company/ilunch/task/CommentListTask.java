package com.company.ilunch.task;

import java.io.InputStream;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.company.ilunch.bean.CommentBean;
import com.company.ilunch.net.AsyncRunner;
import com.company.ilunch.ui.LoginActivity;
import com.company.ilunch.utils.LogUtil;

/**
 * 描述：点评json数据解析
 */

public class CommentListTask extends AsyncRunner<CommentBean> {

	@Override
	protected CommentBean paserJSON(String json) {
		LogUtil.i(LoginActivity.TAG, "点评列表json == " + json);
		JSONObject object = JSON.parseObject(json);
		if (object != null) {
			return JSON.toJavaObject(object, CommentBean.class);
		}
		return null;
	}

	@Override
	protected CommentBean paserXML(InputStream inputStream) {
		return null;
	}

}
