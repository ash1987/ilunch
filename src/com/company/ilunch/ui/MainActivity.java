package com.company.ilunch.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Toast;
import com.company.ilunch.IlunchApplication;
import com.company.ilunch.R;
import com.company.ilunch.base.BaseFragmentActivity;
import com.company.ilunch.fragment.FindingFragment;
import com.company.ilunch.fragment.HomePageFragment;
import com.company.ilunch.fragment.UserFragment;
import com.company.ilunch.preferences.IlunchPreference;
import com.company.ilunch.utils.AndroidUtils;
import com.company.ilunch.widget.CustomToast;

/**
 * 主界面，RadioGroup + Fragment的框架
 */
public class MainActivity extends BaseFragmentActivity {

	public static final int HOMEPAGE_FRAGMENT_FLAG = 0;// 首页
	public static final int FINDING_FRAGMENT_FLAG = 1;// 发现
	public static final int MYACCOUNT_FRAGMENT_FLAG = 2;// 我的
	private String currentFragmentTag = HomePageFragment.FRAGMENT_TAG;
	private static final String STATE_FRAGMENT_TAG = "current:fragment_tag";

	private RadioGroup radioGroup;// 底部RadioGroup
	private float dencity = 0f;

	// radioButton
	private RadioButton homeRadioButton;
	private RadioButton settingRadioButton;
	private RadioButton myAccountRadioButton;
	
	private IlunchPreference ilunchPerference;

	private final static String INTO_CART_FRAGMENT = "INTO_CART_FRAGMENT";
	private MyBroadCastReceiver receiver;

	private class MyBroadCastReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context arg0, Intent intent) {
			if (intent != null && INTO_CART_FRAGMENT.equals(intent.getAction())) {
				RadioButton radioButton = (RadioButton) radioGroup
						.getChildAt(1);
				if (radioButton != null) {
					radioButton.setChecked(true);
				}
			}
		}

	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initData();
		setContentView(R.layout.activity_main);

		receiver = new MyBroadCastReceiver();
		IntentFilter filter = new IntentFilter();
		filter.addAction(INTO_CART_FRAGMENT);
		registerReceiver(receiver, filter);

		if (savedInstanceState != null) {
			String saveFlag = savedInstanceState.getString(STATE_FRAGMENT_TAG);
			if (!TextUtils.isEmpty(saveFlag)) {
				currentFragmentTag = saveFlag;
			}
		}
		
		/************************************************test start********************************/
		ilunchPerference = new IlunchPreference(this);
		ilunchPerference.setMyLocationCity(this, "深圳");
		ilunchPerference.setMyLocationCityID(this, "4");
		ilunchPerference.setMyLocationQy(this, "南山区");
		ilunchPerference.setMyLocationQyID(this, "22");
		ilunchPerference.setMyLocationDs(this, "腾讯大厦");
		ilunchPerference.setMyLocationDsID(this, "227");
		/************************************************test end********************************/
		
		initView();
		setAttribute();
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		outState.putString(STATE_FRAGMENT_TAG, currentFragmentTag);
		super.onSaveInstanceState(outState);
	}

	// 初始化数据
	private void initData() {
		dencity = AndroidUtils.getDeviceDisplayDensity(this);
	}

	// 初始化控件
	private void initView() {
		radioGroup = (RadioGroup) this.findViewById(R.id.home_radioBtn_group);
		homeRadioButton = (RadioButton) this.findViewById(R.id.homeRadioButton);
		settingRadioButton = (RadioButton) this
				.findViewById(R.id.settingRadioButton);
		myAccountRadioButton = (RadioButton) this
				.findViewById(R.id.myAccountRadioButton);

		setRadioBtnSize(Math.round(30 * dencity));
	}

	// 设置控件属性
	private void setAttribute() {
		radioGroup.setOnCheckedChangeListener(new MyRadioGroupCheckListenner());
		changeFragment(getFragmentFlag(currentFragmentTag));
	}

	// 获取Fragment对应的Flag
	private int getFragmentFlag(String fragmentFlag) {
		int flag = -1;
		if (HomePageFragment.FRAGMENT_TAG.equals(fragmentFlag)) {
			flag = HOMEPAGE_FRAGMENT_FLAG;
		} else if (FindingFragment.FRAGMENT_TAG.equals(fragmentFlag)) {
			flag = FINDING_FRAGMENT_FLAG;
		} else if (UserFragment.FRAGMENT_TAG.equals(fragmentFlag)) {
			flag = MYACCOUNT_FRAGMENT_FLAG;
		}
		return flag;
	}

	// RadioGroup选中事件
	private class MyRadioGroupCheckListenner implements OnCheckedChangeListener {

		@Override
		public void onCheckedChanged(RadioGroup group, int checkedId) {
			switch (checkedId) {
			case R.id.homeRadioButton:// 首页
				changeFragment(HOMEPAGE_FRAGMENT_FLAG);
				break;
			case R.id.settingRadioButton:// 发现
				changeFragment(FINDING_FRAGMENT_FLAG);
				break;
			case R.id.myAccountRadioButton:// 我的
				changeFragment(MYACCOUNT_FRAGMENT_FLAG);
				break;
			default:
				break;
			}
		}

	}

	// 加载Fragment
	private void changeFragment(int index) {
		final Fragment fragment;
		final String tag;

		final FragmentManager fm = getSupportFragmentManager();
		final FragmentTransaction tr = fm.beginTransaction();
		if (currentFragmentTag != null) {
			final Fragment currentFragment = fm
					.findFragmentByTag(currentFragmentTag);
			if (currentFragment != null) {
				tr.hide(currentFragment);// 将当前当前即将消失的fragment移除
			}
		}

		switch (index) {
		case HOMEPAGE_FRAGMENT_FLAG:// 显示首页Fragment
			homeRadioButton.setChecked(true);

			tag = HomePageFragment.FRAGMENT_TAG;
			final HomePageFragment homePageFragment = (HomePageFragment) fm
					.findFragmentByTag(tag);
			if (homePageFragment != null) {
				fragment = homePageFragment;
			} else {
				final HomePageFragment homePage = new HomePageFragment();
				fragment = homePage;
			}
			break;
		case FINDING_FRAGMENT_FLAG:// 显示设发现Fragment
			settingRadioButton.setChecked(true);

			tag = FindingFragment.FRAGMENT_TAG;
			final FindingFragment setFragment = (FindingFragment) fm
					.findFragmentByTag(tag);
			if (setFragment != null) {
				fragment = setFragment;
			} else {
				final FindingFragment settingFragment = new FindingFragment();
				fragment = settingFragment;
			}

			break;
		case MYACCOUNT_FRAGMENT_FLAG:// 显示我的Fragment
			myAccountRadioButton.setChecked(true);

			tag = UserFragment.FRAGMENT_TAG;
			final UserFragment myFragment = (UserFragment) fm
					.findFragmentByTag(tag);
			if (myFragment != null) {
				fragment = myFragment;
				// tr.remove(myFragment);
			} else {
				final UserFragment mine = new UserFragment();
				fragment = mine;
			}

			break;
		default:
			return;
		}

		// 选择显示的fragment,并提交事务
		if (fragment.isAdded()) {
			tr.show(fragment);
		} else {
			tr.add(R.id.mainFragmentLayout, fragment, tag);
		}
		tr.commitAllowingStateLoss();
		currentFragmentTag = tag;
	}

	// 重构RadioBtn的图片大小
	private void setRadioBtnSize(int size) {
		for (int i = 0; i < radioGroup.getChildCount(); i++) {
			RadioButton button = (RadioButton) radioGroup.getChildAt(i);
			Drawable[] drawables = button.getCompoundDrawables();
			drawables[1].setBounds(0, 0, size, size);
			button.setCompoundDrawables(drawables[0], drawables[1],
					drawables[2], drawables[3]);
		}
	}

	private long touchTime = 0;// 按第一次返回键的时间
	private long waitTime = 2000;// 两次返回键的有效时间

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			// 实现按两次“返回键”按退出程序
			long currentTime = System.currentTimeMillis();
			if (currentTime - touchTime > waitTime) {
				// Toast提示再次点击
				CustomToast.getToast(MainActivity.this,
						getResources().getString(R.string.click_again_exit),
						Toast.LENGTH_SHORT).show();
				touchTime = currentTime;// 记录第一次点击的时间
			} else {
				// 退出程序，关闭Activity并结束进程
				IlunchApplication.cartUcode = "-1";
				this.finish();
			}
			return true;
		}
		return false;
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (receiver != null) {
			unregisterReceiver(receiver);
		}
	}
}
