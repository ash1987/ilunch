package com.company.ilunch.ui;

import java.util.UUID;

import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import com.company.ilunch.IlunchApplication;
import com.company.ilunch.R;
import com.company.ilunch.base.BaseActivity;
import com.company.ilunch.preferences.LoginPreference;

public class SettingActivity extends BaseActivity implements OnClickListener {
	private ImageView backIv;// 返回
	private RelativeLayout lunchRemindRl;// 午餐提醒
	private RelativeLayout hdRL;// 互动
	private RelativeLayout cooperationRl;// 招商
	private RelativeLayout checkVersionLl;// 检查更新
	private RelativeLayout aboutUsLl;// 关于我们
	private Button btn_layout;

	private LoginPreference loginPreference;

	@Override
	protected void initData() {
		loginPreference = new LoginPreference(this);
	}

	@Override
	protected void initView() {
		setContentView(R.layout.setting);

		backIv = (ImageView) this.findViewById(R.id.backIv);
		lunchRemindRl = (RelativeLayout) findViewById(R.id.lunchRemindRl);
		hdRL = (RelativeLayout) findViewById(R.id.hdRL);
		cooperationRl = (RelativeLayout) findViewById(R.id.cooperationRl);
		aboutUsLl = (RelativeLayout) findViewById(R.id.aboutUsLl);
		checkVersionLl = (RelativeLayout) findViewById(R.id.checkVersionLl);
		btn_layout = (Button) findViewById(R.id.btn_layout);
	}

	@Override
	protected void setAttribute() {
		backIv.setOnClickListener(this);
		lunchRemindRl.setOnClickListener(this);
		hdRL.setOnClickListener(this);
		cooperationRl.setOnClickListener(this);
		aboutUsLl.setOnClickListener(this);
		checkVersionLl.setOnClickListener(this);
		btn_layout.setOnClickListener(this);

		// 判断是否登录
		if (loginPreference.getLoginState()) {
			btn_layout.setVisibility(View.VISIBLE);
		} else {
			btn_layout.setVisibility(View.GONE);
		}
	}

	@Override
	public void onClick(View v) {
		if (v == null) {
			return;
		}

		switch (v.getId()) {
		case R.id.backIv:
			this.finish();
			break;
		case R.id.lunchRemindRl:
			startActivity(new Intent(this, LunchAlertActivity.class));
			break;
		case R.id.hdRL:
			startActivity(new Intent(this, HdActivity.class));
			break;
		case R.id.cooperationRl:
			startActivity(new Intent(this, CooperationActivity.class));
			break;
		case R.id.aboutUsLl:
			startActivity(new Intent(this, AboutActivity.class));
			break;
		case R.id.checkVersionLl:

			break;
		case R.id.btn_layout:
			IlunchApplication.cartUcode = "-1";
			loginPreference.setLoginOut(SettingActivity.this);
			finish();
			break;
		default:
			break;
		}
	}
}
