package com.company.ilunch.ui;

import java.util.ArrayList;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;
import com.company.ilunch.R;
import com.company.ilunch.base.BaseFragmentActivity;
import com.company.ilunch.fragment.MyLocationCityFragment;
import com.company.ilunch.fragment.MyLocationCityFragment.CityCallback;
import com.company.ilunch.fragment.MyLocationDsFragment;
import com.company.ilunch.fragment.MyLocationDsFragment.DsCallback;
import com.company.ilunch.fragment.MyLocationQyFragment;
import com.company.ilunch.fragment.MyLocationQyFragment.SectionCallback;
import com.company.ilunch.net.RequestParams;
import com.company.ilunch.preferences.IlunchPreference;
import com.company.ilunch.utils.AndroidUtils;

/**
 * 我的位置
 */
public class MyLocationActivity extends BaseFragmentActivity implements
		OnCheckedChangeListener, OnClickListener {
	public final static String TAG = "com.company.ilunch";

	private RadioGroup mRadioGroup;
	private RadioButton cityRb;
	private RadioButton qyRb;
	private RadioButton dsRb;
	private ViewPager mViewPager;
	private RequestParams requestParams;// 请求参数封装的键值对

	protected TextView titleTv;
	private Button btn_save_address;
	private ImageView backIv;// 返回

	private IlunchPreference ilunchPerference;

	private ImageView lineIv;// radiobutton下方横线
	private float mCurrentCheckedRadioLeft;// 当前被选中的RadioButton距离左侧的距离

	private float bottomLine;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		initData();
		initView();
		setAttribute();
	}

	private void initData() {
		requestParams = new RequestParams();
		bottomLine = AndroidUtils.getDeviceWidth(this) / 3;
		ilunchPerference = new IlunchPreference(this);
	}

	private void initView() {
		setContentView(R.layout.my_location_layout);

		mRadioGroup = (RadioGroup) findViewById(R.id.radioGroup);
		cityRb = (RadioButton) findViewById(R.id.cityRb);
		qyRb = (RadioButton) findViewById(R.id.qyRb);
		dsRb = (RadioButton) findViewById(R.id.dsRb);
		backIv = (ImageView) this.findViewById(R.id.backIv);

		lineIv = (ImageView) findViewById(R.id.lineIv);
		mViewPager = (ViewPager) findViewById(R.id.pager);
		titleTv = (TextView) findViewById(R.id.titleTv);

		btn_save_address = (Button) findViewById(R.id.btn_save_address);
	}

	private void setAttribute() {
		backIv.setOnClickListener(this);
		btn_save_address.setOnClickListener(this);
		mRadioGroup.setOnCheckedChangeListener(this);
		mViewPager.setOnPageChangeListener(new MyPagerOnPageChangeListener());

		iniVariable();
	}

	private void iniVariable() {
		ArrayList<Fragment> fragments = new ArrayList<Fragment>();

		fragments.add(new MyLocationCityFragment(new CityCallback() {
			@Override
			public void gotoSectionPage() {
				qyRb.performClick();
			}
		}));
		fragments.add(new MyLocationQyFragment(new SectionCallback() {
			@Override
			public void gotoDsPage() {
				dsRb.performClick();
			}
		}));
		fragments.add(new MyLocationDsFragment(new DsCallback() {
			@Override
			public void gotoMap(String merName, String lan, String lng) {
				// Intent intent = new Intent(MyLocationActivity.this,
				// MyLocationMapActivity.class);
				// intent.putExtra("endPoint", lan + "," + lng);
				// intent.putExtra("merName", merName);
				// startActivity(intent);
			}

			@Override
			public void goBack(String buildName, String buildId) {
				ilunchPerference.setMyLocationDs(MyLocationActivity.this,
						buildName);
				ilunchPerference.setMyLocationDsID(MyLocationActivity.this,
						buildId);

				MyLocationActivity.this.finish();
			}
		}));

		FragAdapter fAdapter = new FragAdapter(getSupportFragmentManager(),
				mViewPager);
		fAdapter.setFragments(fragments);

		mViewPager.setCurrentItem(0);
	}

	/**
	 * 获得当前被选中的RadioButton距离左侧的距离
	 */
	private float getCurrentCheckedRadioLeft() {
		for (int i = 0; i < mRadioGroup.getChildCount(); i++) {
			RadioButton rb = (RadioButton) mRadioGroup.getChildAt(i);
			if (rb.isChecked()) {
				return bottomLine * i + AndroidUtils.dip2px(this, 22);
			}
		}

		return 0f;
	}

	/*
	 * radiobutton下方横线动画
	 */
	private void playRbLineAnimation(float currentCheckedRadioLeft,
			float distance, ImageView iv) {
		AnimationSet _AnimationSet = new AnimationSet(true);
		TranslateAnimation _TranslateAnimation = new TranslateAnimation(
				currentCheckedRadioLeft, distance, 0f, 0f);

		_AnimationSet.addAnimation(_TranslateAnimation);
		_AnimationSet.setFillBefore(false);
		_AnimationSet.setFillAfter(true);
		_AnimationSet.setDuration(100);

		iv.startAnimation(_AnimationSet);
	}

	/*
	 * 设置选中的字体颜色
	 */
	private void setCheckedTxtColor(int id) {
		for (int i = 0; i < mRadioGroup.getChildCount(); i++) {
			RadioButton rb = (RadioButton) mRadioGroup.getChildAt(i);
			rb.setTextColor(Color.parseColor("#555555"));

			if (rb.isChecked()) {
				rb.setTextColor(Color.parseColor("#61c82c"));
			}
		}
	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		int selectId = 0;

		for (int i = 0; i < mRadioGroup.getChildCount(); i++) {
			RadioButton rb = (RadioButton) mRadioGroup.getChildAt(i);

			if (rb.getId() == checkedId) {
				selectId = i;
				playRbLineAnimation(mCurrentCheckedRadioLeft, bottomLine * i,
						lineIv);
				mViewPager.setCurrentItem(i);
			}
		}

		setCheckedTxtColor(selectId);

		mCurrentCheckedRadioLeft = getCurrentCheckedRadioLeft();
	}

	/**
	 * FragmentPager适配器
	 * 
	 */
	public class FragAdapter extends FragmentPagerAdapter {
		private FragmentManager fm;
		private ArrayList<Fragment> fragments;
		private ViewPager mViewPager;

		public FragAdapter(FragmentManager fm, ViewPager viewPager) {
			super(fm);

			this.fm = fm;
			this.fragments = new ArrayList<Fragment>();
			this.mViewPager = viewPager;

			this.mViewPager.setAdapter(this);
		}

		public void setFragments(ArrayList<Fragment> fragments) {
			this.fragments = fragments;

			this.mViewPager.setOffscreenPageLimit(fragments.size());
			notifyDataSetChanged();
		}

		@Override
		public Fragment getItem(int arg0) {
			return fragments.get(arg0);
		}

		@Override
		public int getCount() {
			return fragments.size();
		}

		@Override
		public int getItemPosition(Object object) {
			return super.getItemPosition(object);
		}
	}

	private class MyPagerOnPageChangeListener implements OnPageChangeListener {

		@Override
		public void onPageScrollStateChanged(int arg0) {

		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {

		}

		/**
		 * 滑动ViewPager的时候,让上方的HorizontalScrollView自动切换
		 */
		@Override
		public void onPageSelected(int position) {
			performRtn(position);
		}
	}

	private void performRtn(int position) {
		for (int i = 0; i < mRadioGroup.getChildCount(); i++) {
			RadioButton rb = (RadioButton) mRadioGroup.getChildAt(i);

			if (position == i) {
				rb.performClick();
			}

			// if(i == mRadioGroup.getChildCount()-1) {
			// btn_save_address.setVisibility(View.VISIBLE);
			// } else {
			// btn_save_address.setVisibility(View.GONE);
			// }
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
		case R.id.btn_save_address:
			MyLocationActivity.this.finish();
			break;
		}
	}
}