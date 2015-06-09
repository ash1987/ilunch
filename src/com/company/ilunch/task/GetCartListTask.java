package com.company.ilunch.task;

import java.io.InputStream;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.company.ilunch.bean.GetCartListBean;
import com.company.ilunch.net.AsyncRunner;
import com.company.ilunch.ui.LoginActivity;
import com.company.ilunch.utils.LogUtil;

/**
 * 描述：获取购物车接口json数据解析
 */

public class GetCartListTask extends AsyncRunner<GetCartListBean> {

	@Override
	protected GetCartListBean paserJSON(String json) {
		LogUtil.i(LoginActivity.TAG, "获取购物车接口json == " + json);
		GetCartListBean gclBean = null;
		JSONObject object = JSON.parseObject(json);
		if (object != null) {
			try {
				gclBean = JSON.toJavaObject(object, GetCartListBean.class);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return gclBean;
	}

	@Override
	protected GetCartListBean paserXML(InputStream inputStream) {
		return null;
	}
}
