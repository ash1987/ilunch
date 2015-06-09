package com.company.ilunch.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.widget.Toast;

import com.sina.weibo.sdk.api.share.IWeiboShareAPI;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.WeiboAuth;
import com.sina.weibo.sdk.auth.WeiboAuthListener;
import com.sina.weibo.sdk.auth.sso.SsoHandler;
import com.sina.weibo.sdk.exception.WeiboException;

/**
 * 生成日期：2014-6-13 <br/>
 * 描述：新浪微博登录工具类
 */

public class WeiBoLoginUtil {

	/** 当前 DEMO 应用的 APP_KEY，第三方应用应该使用自己的 APP_KEY 替换该 APP_KEY */
	public static final String APP_KEY = "515757539";

	/**
	 * 当前 DEMO 应用的回调页，第三方应用可以使用自己的回调页。
	 * 
	 * <p>
	 * 注：关于授权回调页对移动客户端应用来说对用户是不可见的，所以定义为何种形式都将不影响， 但是没有定义将无法使用 SDK 认证登录。
	 * 建议使用默认回调页：https://api.weibo.com/oauth2/default.html
	 * </p>
	 */
	public static final String REDIRECT_URL = "http://www.sina.com";

	/**
	 * Scope 是 OAuth2.0 授权机制中 authorize 接口的一个参数。通过 Scope，平台将开放更多的微博
	 * 核心功能给开发者，同时也加强用户隐私保护，提升了用户体验，用户在新 OAuth2.0 授权页中有权利 选择赋予应用的功能。
	 * 
	 * 我们通过新浪微博开放平台-->管理中心-->我的应用-->接口管理处，能看到我们目前已有哪些接口的 使用权限，高级权限需要进行申请。
	 * 
	 * 目前 Scope 支持传入多个 Scope 权限，用逗号分隔。
	 * 
	 * 有关哪些 OpenAPI 需要权限申请，请查看：http://open.weibo.com/wiki/%E5%BE%AE%E5%8D%9AAPI
	 * 关于 Scope 概念及注意事项，请查看：http://open.weibo.com/wiki/Scope
	 */
	public static final String SCOPE = "email,direct_messages_read,direct_messages_write,"
			+ "friendships_groups_read,friendships_groups_write,statuses_to_me_read,"
			+ "follow_app_official_microblog," + "invitation_write";

	/** 微博 Web 授权类，提供登陆等功能 */
	private WeiboAuth mWeiboAuth;
	/** 注意：SsoHandler 仅当 SDK 支持 SSO 时有效 */
	private SsoHandler mSsoHandler;
	/** 封装了 "access_token"，"expires_in"，"refresh_token"，并提供了他们的管理功能 */
	private Oauth2AccessToken mAccessToken;
	private IWeiboShareAPI mWeiboShareAPI;
	private Context mContext;
	private Handler.Callback callback;

	public WeiBoLoginUtil(Context mContext, Handler.Callback callback) {
		this.mContext = mContext;
		this.callback = callback;
		// 创建微博实例
		mWeiboAuth = new WeiboAuth(mContext, APP_KEY, REDIRECT_URL, SCOPE);
	}

	// 授权登录
	public void login() {
		mSsoHandler = new SsoHandler((Activity) mContext, mWeiboAuth);
		mSsoHandler.authorize(new AuthListener());
	}

	/**
	 * 微博认证授权回调类。 1. SSO 授权时，需要在 {@link #onActivityResult} 中调用
	 * {@link SsoHandler#authorizeCallBack} 后， 该回调才会被执行。 2. 非 SSO
	 * 授权时，当授权结束后，该回调就会被执行。 当授权成功后，请保存该 access_token、expires_in、uid 等信息到
	 * SharedPreferences 中。
	 */
	class AuthListener implements WeiboAuthListener {

		@Override
		public void onComplete(Bundle values) {
			// 从 Bundle 中解析 Token
			mAccessToken = Oauth2AccessToken.parseAccessToken(values);
			if (mAccessToken.isSessionValid()) {
				Message message = new Message();
				message.what = 0;
				message.obj = mAccessToken.getToken();
				callback.handleMessage(message);

				// 保存 Token 到 SharedPreferences
				AccessTokenKeeper.writeAccessToken(mContext, mAccessToken);
				Toast.makeText(mContext, "授权成功", Toast.LENGTH_SHORT).show();
			} else {
				// 以下几种情况，您会收到 Code：
				// 1. 当您未在平台上注册的应用程序的包名与签名时；
				// 2. 当您注册的应用程序包名与签名不正确时；
				// 3. 当您在平台上注册的包名和签名与您当前测试的应用的包名和签名不匹配时。
				String code = values.getString("code");
				String message = "授权失败";
				if (!TextUtils.isEmpty(code)) {
					message = message + "\nObtained the code: " + code;
				}
				Toast.makeText(mContext, message, Toast.LENGTH_LONG).show();
			}
		}

		@Override
		public void onCancel() {
			Toast.makeText(mContext, "取消授权", Toast.LENGTH_SHORT).show();
		}

		@Override
		public void onWeiboException(WeiboException e) {
			Toast.makeText(mContext, "授权失败", Toast.LENGTH_LONG).show();
		}
	}

	public void setOnActivityResult(int requestCode, int resultCode, Intent data) {
		// SSO 授权回调
		// 重要：发起 SSO 登陆的 Activity 必须重写 onActivityResult
		if (mSsoHandler != null) {
			mSsoHandler.authorizeCallBack(requestCode, resultCode, data);
		}
	}

}
