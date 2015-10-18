package com.company.ilunch.ui;

import org.json.JSONException;
import org.json.JSONObject;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;
import com.company.ilunch.R;
import com.company.ilunch.base.BaseActivity;
import com.company.ilunch.bean.GetYzmBean;
import com.company.ilunch.net.HttpUrlManager;
import com.company.ilunch.net.RequestListener;
import com.company.ilunch.preferences.LoginPreference;
import com.company.ilunch.task.FindPwdTask;
import com.company.ilunch.task.GetYzmTask;
import com.company.ilunch.utils.AndroidUtils;
import com.company.ilunch.utils.LogUtil;
import com.company.ilunch.widget.CustomToast;
import com.company.ilunch.base.BaseActivity.CountTimer;

public class ForgetPwdActivity extends BaseActivity implements OnClickListener,CountTimer {
	public final static String TAG = "com.company.ilunch";

	private final static int MSG_GET_YZM_SUCCESS = 0x01;// 　获取验证码成功
	private final static int MSG_GET_YZM_FAIL = 0x02;// 获取验证码失敗
	private final static int MSG_FIND_SUCCESS = 0x03;// 　找回成功
	private final static int MSG_FIND_FAIL = 0x04;// 找回失敗

	private LinearLayout backLl;// 返回
	
	private LoginPreference loginPreference;

	private EditText phoneEt;
	private EditText yzmEt;
	private Button getYzmBtn;
	private Button submitBtn;
	
	private TimeCount time;
	
	@Override
	protected void initData() {
		loginPreference = new LoginPreference(this);
		time = new TimeCount(this, 60000, 1000);// 构造CountDownTimer对象
	}

	@Override
	protected void initView() {
		setContentView(R.layout.forget_pwd_layout);

		backLl = (LinearLayout) this.findViewById(R.id.backLl);
		getYzmBtn = (Button) findViewById(R.id.getYzmBtn);
		submitBtn = (Button) findViewById(R.id.submitBtn);
		phoneEt = (EditText) findViewById(R.id.phoneEt);
		yzmEt = (EditText) findViewById(R.id.yzmEt);
	}

	@Override
	protected void setAttribute() {
		backLl.setOnClickListener(this);
		getYzmBtn.setOnClickListener(this);
		submitBtn.setOnClickListener(this);
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.backLl:
			this.finish();
			break;
		case R.id.getYzmBtn:
			doGetYzm();
			break;
		case R.id.submitBtn:
			doFindPwd();
			break;
		default:
			break;
		}
	}

	// 判断手机号码的合法性
	private boolean isMobileRight(String mobileNum) {
		// 判断手机号是否为空
		if (TextUtils.isEmpty(mobileNum)) {
			CustomToast.getToast(ForgetPwdActivity.this,
					getString(R.string.phonenum_null_string),
					Toast.LENGTH_SHORT).show();
			return false;
		}

		// 判断手机号是否合法
		if (!AndroidUtils.isMobileNO(mobileNum)) {
			CustomToast.getToast(ForgetPwdActivity.this,
					getString(R.string.phonenum_error_string),
					Toast.LENGTH_SHORT).show();
			return false;
		}
		return true;
	}

	/**
	 * 向服务器请求验证码 <br/>
	 * 
	 */
	private void doGetYzm() {
		// 判断网络是否可用
		if (!AndroidUtils.isNetworkAvailable(ForgetPwdActivity.this)) {
			CustomToast.getToast(ForgetPwdActivity.this,
					getString(R.string.networkoff), Toast.LENGTH_SHORT).show();
			return;
		}

		// 判断手机号码的有效性
		String mobileString = phoneEt.getText().toString().trim();

		if (!isMobileRight(mobileString)) {
			return;
		}
		
		GetYzmTask task = new GetYzmTask();

		JSONObject requestParams = new JSONObject();
		
		try {
			requestParams.put("Phone", phoneEt.getText().toString().trim());
		} catch (JSONException e) {
			e.printStackTrace();
		}

		task.request(this, HttpUrlManager.GET_PWD_STRING,
				requestParams, getYzmListener);
	}

	/**
	 * 验证码接口监听类
	 */
	private RequestListener<GetYzmBean> getYzmListener = new RequestListener<GetYzmBean>() {

		@Override
		public void OnStart() {
			LogUtil.d(TAG, "开始请求OnStart-----------");
		}

		@Override
		public void onError(Exception e) {
			LogUtil.d(TAG, "onError-----------" + e.getMessage());
		}

		@Override
		public void OnPaserComplete(GetYzmBean bean) {
			if (bean != null) {
				LogUtil.d(TAG, "OnPaserComplete:" + bean.getHead());
				if ("00".equals(bean.getHead().getResultCode())) {
					mHandler.obtainMessage(MSG_GET_YZM_SUCCESS, bean)
							.sendToTarget();
				} else {
					mHandler.obtainMessage(MSG_GET_YZM_FAIL,
							bean.getHead().getResultInfo()).sendToTarget();
				}
			} else {
				mHandler.obtainMessage(MSG_GET_YZM_FAIL,
						getString(R.string.get_code_failed))
						.sendToTarget();
			}
		}
	};
	
	/**
	 * 向服务器请求找回密码 <br/>
	 * 
	 */
	private void doFindPwd() {
		// 判断网络是否可用
		if (!AndroidUtils.isNetworkAvailable(ForgetPwdActivity.this)) {
			CustomToast.getToast(ForgetPwdActivity.this,
					getString(R.string.networkoff), Toast.LENGTH_SHORT).show();
			return;
		}

		// 判断手机号码的有效性
		String mobileString = phoneEt.getText().toString().trim();

		if (!isMobileRight(mobileString)) {
			return;
		}
		
		String yzmCode = yzmEt.getText().toString().trim();
		
		if (TextUtils.isEmpty(yzmCode)) {
			CustomToast.getToast(ForgetPwdActivity.this,
					getString(R.string.sms_null_string),
					Toast.LENGTH_SHORT).show();
			return;
		}
		
		FindPwdTask task = new FindPwdTask();

		JSONObject requestParams = new JSONObject();
		
		try {
			requestParams.put("Phone", phoneEt.getText().toString().trim());
		} catch (JSONException e) {
			e.printStackTrace();
		}

//		task.request(this, HttpUrlManager.GET_CHECKCODE_STRING,
//				requestParams, getYzmListener);
	}

	/**
	 * 验证码接口监听类
	 */
//	private RequestListener<GetYzmBean> getYzmListener = new RequestListener<GetYzmBean>() {
//
//		@Override
//		public void OnStart() {
//			LogUtil.d(TAG, "开始请求OnStart-----------");
//		}
//
//		@Override
//		public void onError(Exception e) {
//			LogUtil.d(TAG, "onError-----------" + e.getMessage());
//		}
//
//		@Override
//		public void OnPaserComplete(GetYzmBean bean) {
//			if (bean != null) {
//				LogUtil.d(TAG, "OnPaserComplete:" + bean.getHead());
//				if ("00".equals(bean.getHead().getResultCode())) {
//					mHandler.obtainMessage(MSG_GET_YZM_SUCCESS, bean)
//							.sendToTarget();
//				} else {
//					mHandler.obtainMessage(MSG_GET_YZM_FAIL,
//							bean.getHead().getResultInfo()).sendToTarget();
//				}
//			} else {
//				mHandler.obtainMessage(MSG_GET_YZM_FAIL,
//						getString(R.string.get_code_failed))
//						.sendToTarget();
//			}
//		}
//	};

	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			super.handleMessage(msg);

			if (msg == null) {
				return;
			}

			switch (msg.what) {
			case MSG_GET_YZM_SUCCESS:
				Toast.makeText(ForgetPwdActivity.this, R.string.get_code_success,
						Toast.LENGTH_SHORT).show();
				
				if (time != null) {
					time.start();
				}
				break;
			case MSG_GET_YZM_FAIL:
				Toast.makeText(ForgetPwdActivity.this, (String) msg.obj,
						Toast.LENGTH_SHORT).show();
				break;
			default:
				break;
			}
		};
	};
	
	/**
	 * 
	 * 重置验证码
	 */
	private void resetTimer() {
		if (time != null) {
			time.cancel();
			time = new TimeCount(this, 60000, 1000);
		}
		getYzmBtn.setEnabled(true);
		getYzmBtn.setText(R.string.get_verify);
	}

	@Override
	public void onTimeFinish() {
		resetTimer();
	}

	@Override
	public void onTimeTick(long millisUntilFinished) {
		getYzmBtn.setEnabled(false);
		getYzmBtn.setText(String.format(getString(R.string.count_down_re_verify), millisUntilFinished / 1000));
	}
}