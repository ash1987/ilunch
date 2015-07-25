package com.company.ilunch.task;

import java.io.InputStream;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.company.ilunch.bean.UpdateOrderStatusBean;
import com.company.ilunch.net.AsyncRunner;
import com.company.ilunch.ui.LoginActivity;
import com.company.ilunch.utils.LogUtil;

/**
 * 描述：更新订单状态接口json数据解析
 */

public class UpdateOrderStatusTask extends AsyncRunner<UpdateOrderStatusBean> {

	@Override
	protected UpdateOrderStatusBean paserJSON(String json) {
		LogUtil.i(LoginActivity.TAG, "更新订单状态接口json == " + json);
		UpdateOrderStatusBean gclBean = null;
		JSONObject object = JSON.parseObject(json);
		if (object != null) {
			try {
				gclBean = JSON.toJavaObject(object, UpdateOrderStatusBean.class);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return gclBean;
	}

	@Override
	protected UpdateOrderStatusBean paserXML(InputStream inputStream) {
		return null;
	}
}
