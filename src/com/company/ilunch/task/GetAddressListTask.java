package com.company.ilunch.task;

import java.io.InputStream;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.company.ilunch.bean.GetAddressListBean;
import com.company.ilunch.net.AsyncRunner;
import com.company.ilunch.ui.LoginActivity;
import com.company.ilunch.utils.LogUtil;

/**
 * 描述：获取地址列表接口json数据解析
 */

public class GetAddressListTask extends AsyncRunner<GetAddressListBean> {

	@Override
	protected GetAddressListBean paserJSON(String json) {
		LogUtil.i(LoginActivity.TAG, "获取地址列表接口json == " + json);
		GetAddressListBean bean = null;
		JSONObject object = JSON.parseObject(json);
		if (object != null) {
			try{
				bean = JSON.toJavaObject(object, GetAddressListBean.class);
			}catch(Exception e) {
				e.printStackTrace();
			}
		}

		return bean;
	}

	@Override
	protected GetAddressListBean paserXML(InputStream inputStream) {
		return null;
	}
}
