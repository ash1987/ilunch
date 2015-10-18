package com.company.ilunch.ui;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.widget.CompoundButton.OnCheckedChangeListener;
import com.company.ilunch.R;
import com.company.ilunch.base.BaseActivity;
import com.company.ilunch.bean.GetSTemplateListBean;
import com.company.ilunch.bean.RegisterBean;
import com.company.ilunch.net.HttpUrlManager;
import com.company.ilunch.net.RequestListener;
import com.company.ilunch.preferences.LoginPreference;
import com.company.ilunch.task.GetSTemplateListTask;
import com.company.ilunch.task.RegisterTask;
import com.company.ilunch.utils.AndroidUtils;
import com.company.ilunch.utils.LogUtil;
import com.company.ilunch.widget.CustomToast;

public class RegisterActivity extends BaseActivity implements OnClickListener {
	public final static String TAG = "com.company.ilunch";

	private final static int MSG_REGISTER_SUCCESS = 0x01;// 　注册成功
	private final static int MSG_REGISTER_FAIL = 0x02;// 注册失敗

	private LinearLayout backLl;// 返回
	
	private LoginPreference loginPreference;

	private EditText userEt;
	private EditText phoneEt;
	private EditText pwdEt;
	private EditText pwd1Et;
	private Button registerBtn;
	
	private String nickName;

	@Override
	protected void initData() {
		loginPreference = new LoginPreference(this);
	}

	@Override
	protected void initView() {
		setContentView(R.layout.register_layout);

		backLl = (LinearLayout) this.findViewById(R.id.backLl);
		registerBtn = (Button) findViewById(R.id.registerBtn);
		userEt = (EditText) findViewById(R.id.userEt);
		phoneEt = (EditText) findViewById(R.id.phoneEt);
		pwdEt = (EditText) findViewById(R.id.pwdEt);
		pwd1Et = (EditText) findViewById(R.id.pwd1Et);
	}

	@Override
	protected void setAttribute() {
		backLl.setOnClickListener(this);
		registerBtn.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.backLl:
			this.finish();
			break;
		case R.id.registerBtn:
			doRegister();
			break;
		default:
			break;
		}
	}

	// 判断手机号码的合法性
	private boolean isMobileRight(String mobileNum) {
		// 判断手机号是否为空
		if (TextUtils.isEmpty(mobileNum)) {
			CustomToast.getToast(RegisterActivity.this,
					getString(R.string.phonenum_null_string),
					Toast.LENGTH_SHORT).show();
			return false;
		}

		// 判断手机号是否合法
		if (!AndroidUtils.isMobileNO(mobileNum)) {
			CustomToast.getToast(RegisterActivity.this,
					getString(R.string.phonenum_error_string),
					Toast.LENGTH_SHORT).show();
			return false;
		}
		return true;
	}

	/**
	 * 向服务器请求注册 <br/>
	 * 
	 */
	private void doRegister() {
		// 判断网络是否可用
		if (!AndroidUtils.isNetworkAvailable(RegisterActivity.this)) {
			CustomToast.getToast(RegisterActivity.this,
					getString(R.string.networkoff), Toast.LENGTH_SHORT).show();
			return;
		}

		// 用户名/昵称
		nickName = userEt.getText().toString().trim();

		// 昵称不能为空
		if (TextUtils.isEmpty(nickName)) {
			CustomToast
					.getToast(RegisterActivity.this,
							getString(R.string.account_null_string),
							Toast.LENGTH_SHORT).show();
			return;
		}

		// 判断手机号码的有效性
		String mobileString = phoneEt.getText().toString().trim();

		if (!isMobileRight(mobileString)) {
			return;
		}

		// 密码
		String password = pwdEt.getText().toString().trim();
		// 确认密码
		String confirmPw = pwd1Et.getText().toString().trim();
		// 密码为空
		if (TextUtils.isEmpty(password)) {
			CustomToast.getToast(RegisterActivity.this,
					getString(R.string.password_null_string),
					Toast.LENGTH_SHORT).show();
			return;
		}

		// 密码不足6位
		if (password.length() < 6) {
			CustomToast.getToast(RegisterActivity.this,
					getString(R.string.password_error_string),
					Toast.LENGTH_SHORT).show();
			return;
		}

		// 确认密码为空
		if (TextUtils.isEmpty(confirmPw)) {
			CustomToast.getToast(RegisterActivity.this,
					getString(R.string.confirmPw_null_string),
					Toast.LENGTH_SHORT).show();
			return;
		}

		// 两次输入的密码不同
		if (!confirmPw.equals(password)) {
			CustomToast.getToast(RegisterActivity.this,
					getString(R.string.confirmPw_error_string),
					Toast.LENGTH_SHORT).show();
			return;
		}

		RegisterTask task = new RegisterTask();

		JSONObject requestParams = new JSONObject();
		
		try {
			requestParams.put("LoginName", userEt.getText().toString().trim());
			requestParams.put("Phone", phoneEt.getText().toString().trim());
			requestParams.put("PassWord", pwdEt.getText().toString().trim());
		} catch (JSONException e) {
			e.printStackTrace();
		}

		task.request(this, HttpUrlManager.USER_REGION_STRING,
				requestParams, registerListener);
	}

	/**
	 * 注册接口监听类
	 */
	private RequestListener<RegisterBean> registerListener = new RequestListener<RegisterBean>() {

		@Override
		public void OnStart() {
			LogUtil.d(TAG, "开始请求OnStart-----------");
		}

		@Override
		public void onError(Exception e) {
			LogUtil.d(TAG, "onError-----------" + e.getMessage());
		}

		@Override
		public void OnPaserComplete(RegisterBean bean) {
			if (bean != null) {
				LogUtil.d(TAG, "OnPaserComplete:" + bean.getHead());
				if ("00".equals(bean.getHead().getResultCode())) {
					mHandler.obtainMessage(MSG_REGISTER_SUCCESS, bean)
							.sendToTarget();
				} else {
					mHandler.obtainMessage(MSG_REGISTER_FAIL,
							bean.getHead().getResultInfo()).sendToTarget();
				}
			} else {
				mHandler.obtainMessage(MSG_REGISTER_FAIL,
						getString(R.string.register_failed))
						.sendToTarget();
			}
		}
	};

	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			super.handleMessage(msg);

			if (msg == null) {
				return;
			}

			switch (msg.what) {
			case MSG_REGISTER_SUCCESS:
				Toast.makeText(RegisterActivity.this, R.string.register_success,
						Toast.LENGTH_SHORT).show();
				loginPreference.setLoginOut(RegisterActivity.this);
				loginPreference.setUserName(RegisterActivity.this, nickName);
				finish();
				break;
			case MSG_REGISTER_FAIL:
				Toast.makeText(RegisterActivity.this, (String) msg.obj,
						Toast.LENGTH_SHORT).show();
				break;
			default:
				break;
			}
		};
	};
}