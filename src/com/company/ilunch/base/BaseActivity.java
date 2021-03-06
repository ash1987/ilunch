package com.company.ilunch.base;

import android.app.Activity;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.TextView;
import com.company.ilunch.R;

/**
 * Activity基类，用于统一各个界面的弹出框等
 */
public abstract class BaseActivity extends Activity {
	public boolean isRun = false;// 用于处理viewpager循环播放
	public boolean isDown = false;// 用于处理viewpager循环播放
	private Dialog mProgressDialog;

	public final static String INTO_CART_FRAGMENT = "INTO_CART_FRAGMENT";
	private MyBroadCastReceiver receiver;

	private class MyBroadCastReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context arg0, Intent intent) {
			if (intent != null && INTO_CART_FRAGMENT.equals(intent.getAction())) {
				BaseActivity.this.finish();
			}
		}

	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		receiver = new MyBroadCastReceiver();
		IntentFilter filter = new IntentFilter();
		filter.addAction(INTO_CART_FRAGMENT);
		registerReceiver(receiver, filter);
		initData();
		initView();
		setAttribute();
	}

	// 初始化数据
	protected abstract void initData();

	// 初始化UI控件
	protected abstract void initView();

	// 初始化UI控件
	protected abstract void setAttribute();

	public interface CountTimer {

		public void onTimeFinish();

		public void onTimeTick(long millisUntilFinished);
	}
	
	/* 定义一个倒计时的内部类 */
	public class TimeCount extends CountDownTimer {

		private CountTimer timer;

		public TimeCount(CountTimer timer, long millisInFuture,
				long countDownInterval) {
			super(millisInFuture, countDownInterval);// 参数依次为总时长,和计时的时间间隔

			this.timer = timer;
		}

		@Override
		public void onFinish() {// 计时完毕时触发
			timer.onTimeFinish();
		}

		@Override
		public void onTick(long millisUntilFinished) {// 计时过程显示
			timer.onTimeTick(millisUntilFinished);
		}
	}
	
	/**
	 * 显示等待框
	 * 
	 * @param title
	 * @param message
	 */
	public void showProgress(String title, String message) {
		try {
			if (mProgressDialog != null && mProgressDialog.isShowing()) {
				return;
			}
			mProgressDialog = new Dialog(this, R.style.CustomDialog);
			mProgressDialog.setContentView(R.layout.dialog_progress);
			TextView textView = (TextView) mProgressDialog
					.findViewById(R.id.progress_msg);
			textView.setText(message);
			mProgressDialog.show();
		} catch (OutOfMemoryError e) {
			e.printStackTrace();
		}
	}

	/**
	 * 取消等待框
	 */
	public void dismissProgress() {
		if (mProgressDialog != null && mProgressDialog.isShowing()) {
			try {
				mProgressDialog.dismiss();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (receiver != null) {
			unregisterReceiver(receiver);
		}
	}
}
