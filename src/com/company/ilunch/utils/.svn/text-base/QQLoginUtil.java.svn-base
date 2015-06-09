package com.company.ilunch.utils;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import com.company.ilunch.ui.MainActivity;
import com.company.ilunch.ui.LoginActivity.BaseUiListener;
import com.tencent.connect.UserInfo;
import com.tencent.connect.auth.QQAuth;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;

/**
 * 描述：QQ登录工具类
 */

public class QQLoginUtil {
	private Context mContext;
	public static QQAuth mQQAuth;
	private UserInfo mInfo;// 用户信息
	private Tencent mTencent;
	private final static String mAppid = "1104679322";

	public QQLoginUtil(Context mContext) {
		mQQAuth = QQAuth.createInstance(mAppid,
				mContext.getApplicationContext());
		mTencent = Tencent.createInstance(mAppid, mContext);
		this.mContext = mContext;
	}

	public Tencent getTencent() {
		return mTencent;
	}
	
	// 开始登录
	public void login(IUiListener listener) {

		if (!mQQAuth.isSessionValid()) {
			mQQAuth.login((Activity) mContext, "get_simple_userinfo,add_topic",
					listener);
			/*
			 * mTencent.loginWithOEM((Activity) mContext,
			 * "get_simple_userinfo,add_topic", listener, "10000144",
			 * "10000144", "xxxx");
			 */
		} else {
			mQQAuth.logout(mContext);
		}

	}

	// 判断是否已登录授权
	public static boolean ready(Context context) {
		if (mQQAuth == null) {
			return false;
		}
		boolean ready = mQQAuth.isSessionValid()
				&& mQQAuth.getQQToken().getOpenId() != null;
		// if (!ready)
		// Toast.makeText(context, "请先登录",
		// Toast.LENGTH_SHORT).show();
		return ready;
	}

}
