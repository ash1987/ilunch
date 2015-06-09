package com.company.ilunch.ui;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.company.ilunch.R;
import com.company.ilunch.base.BaseActivity;
import com.company.ilunch.preferences.IlunchPreference;
import com.company.ilunch.preferences.LoginPreference;
import com.company.ilunch.widget.SelectDatePopupWindow;
import com.company.ilunch.widget.scrollDatePicker.WheelView;

public class LunchAlertActivity extends BaseActivity implements OnClickListener {
	private IlunchPreference ilunchPreference;
	private LoginPreference loginPreference;

	private SelectDatePopupWindow dataPopupWindow;
	private RelativeLayout dqsjRl;// 提醒时间
	private ImageView backIv;// 返回
	private TextView alertTimeTv;// 午餐提醒时间

	private WheelView yearView;
	private WheelView monthView;
	private WheelView dayView;
	private WheelView hourView;
	private WheelView minsView;

	@Override
	protected void initData() {
		ilunchPreference = new IlunchPreference(this);
	}

	@Override
	protected void initView() {
		setContentView(R.layout.lunch_alert_layout);

		dqsjRl = (RelativeLayout) this.findViewById(R.id.dqsjRl);
		backIv = (ImageView) this.findViewById(R.id.backIv);
		alertTimeTv = (TextView) this.findViewById(R.id.alertTimeTv);
	}

	@Override
	protected void setAttribute() {
		backIv.setOnClickListener(this);
		dqsjRl.setOnClickListener(this);
		loginPreference = new LoginPreference(this);
		
		String ilunchTimeAlert = ilunchPreference.getLunchTimeAlert();
		if (TextUtils.isEmpty(ilunchTimeAlert)) {
			alertTimeTv.setText("11:45");
			ilunchPreference.setLunchTimeAlert(this, "11:45");
		} else {
			alertTimeTv.setText(ilunchPreference.getLunchTimeAlert());
		}
		
		if (!loginPreference.getLoginState()) {
			startActivity(new Intent(this, LoginActivity.class));
			finish();
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.backIv:
			this.finish();
			break;
		case R.id.dqsjRl:
			createTimeWindow();
			break;
		default:
			break;
		}
	}

	/*
	 * 创建时间设置窗口
	 */
	private void createTimeWindow() {
		dataPopupWindow = new SelectDatePopupWindow(this, dateOnClick);

		View view = dataPopupWindow.getView();

		yearView = (WheelView) view.findViewById(R.id.year);
		monthView = (WheelView) view.findViewById(R.id.month);
		dayView = (WheelView) view.findViewById(R.id.day);
		hourView = (WheelView) view.findViewById(R.id.hour);
		minsView = (WheelView) view.findViewById(R.id.mins);

		dataPopupWindow.show(findViewById(R.id.container_ll));
	}

	// 为弹出窗口实现监听类
	private OnClickListener dateOnClick = new OnClickListener() {

		public void onClick(View v) {
			if (dataPopupWindow != null && dataPopupWindow.isShowing()) {
				dataPopupWindow.dismiss();
			}
			switch (v.getId()) {
			case R.id.btn_datetime_sure:
				String newTime = hourView
						.getTextItem(hourView.getCurrentItem())
						+ ":"
						+ minsView.getTextItem(minsView.getCurrentItem());
				alertTimeTv.setText(newTime);
				ilunchPreference.setLunchTimeAlert(LunchAlertActivity.this,
						newTime);

				dataPopupWindow.dismiss();
				break;
			default:
				break;
			}
		}
	};
}