package com.company.ilunch.ui;

import java.util.ArrayList;
import org.json.JSONException;
import org.json.JSONObject;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import android.widget.RadioGroup.OnCheckedChangeListener;
import com.company.ilunch.R;
import com.company.ilunch.base.BaseFragmentActivity;
import com.company.ilunch.bean.GetOrderListBean;
import com.company.ilunch.bean.GetOrderListBean.Body;
import com.company.ilunch.fragment.MyOrderInfoFragment;
import com.company.ilunch.net.HttpUrlManager;
import com.company.ilunch.net.RequestListener;
import com.company.ilunch.preferences.LoginPreference;
import com.company.ilunch.task.GetOrderListTask;
import com.company.ilunch.utils.AndroidUtils;
import com.company.ilunch.utils.LogUtil;

/**
 * 我的订单
 */
public class MyOrderActivity extends BaseFragmentActivity implements
OnCheckedChangeListener, OnClickListener {
	public final static String TAG = "com.company.ilunch";
	
	private final static int MSG_GET_ORDER_LIST_SUCESS = 0x01;// 获取订单列表成功
	private final static int MSG_GET_ORDER_LIST_FAIL = 0x02;// 　获取订单列表成功

	private RadioGroup mRadioGroup;
	private RadioButton dbRb;
	private RadioButton dzRb;
	private RadioButton wmRb;
	private ViewPager mViewPager;
	private ImageView backIv;// 返回

	private ImageView lineIv;// radiobutton下方横线
	private float mCurrentCheckedRadioLeft;// 当前被选中的RadioButton距离左侧的距离

	private float bottomLine;
	
	private LoginPreference loginPreference;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		initData();
		initView();
		setAttribute();
	}

	private void initData() {
		bottomLine = AndroidUtils.getDeviceWidth(this) / 3;
		loginPreference = new LoginPreference(this);
	}

	private void initView() {
		setContentView(R.layout.my_order_layout);

		backIv = (ImageView) this.findViewById(R.id.backIv);
		mRadioGroup = (RadioGroup) findViewById(R.id.radioGroup);
		dbRb = (RadioButton) findViewById(R.id.dbRb);
		dzRb = (RadioButton) findViewById(R.id.dzRb);
		wmRb = (RadioButton) findViewById(R.id.wmRb);

		lineIv = (ImageView) findViewById(R.id.lineIv);
		mViewPager = (ViewPager) findViewById(R.id.pager);
	}

	private void setAttribute() {
		backIv.setOnClickListener(this);
		mRadioGroup.setOnCheckedChangeListener(this);
		mViewPager.setOnPageChangeListener(new MyPagerOnPageChangeListener());

		if(!loginPreference.getLoginState()) {
			startActivity(new Intent(MyOrderActivity.this, LoginActivity.class));
			return;
		}
		
		doGetOrderList();
	}

	private void iniVariable(ArrayList<Body> bodyList) {
		ArrayList<Fragment> fragments = new ArrayList<Fragment>();

		fragments.add(new MyOrderInfoFragment(bodyList));
		fragments.add(new MyOrderInfoFragment(bodyList));
		fragments.add(new MyOrderInfoFragment(bodyList));

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
		}
	}
	
	/**
	 * 向服务器请求获取订单列表 <br/>
	 * 
	 */
	private void doGetOrderList() {
		showProgress("", "");
		
		GetOrderListTask task = new GetOrderListTask();

		JSONObject requestParams = new JSONObject();
		try {
			requestParams.put("UserId", Integer.parseInt(loginPreference.getDataID()));
			requestParams.put("PageIndex", 1);
			requestParams.put("PageSize", 100);
		} catch (JSONException e) {
			e.printStackTrace();
		}

		task.request(this,
				HttpUrlManager.GET_MY_ORDER_LIST_STRING, requestParams,
				getMyOrderListListener);
	}

	/**
	 * 获取菜品分类接口监听类
	 */
	private RequestListener<GetOrderListBean> getMyOrderListListener = new RequestListener<GetOrderListBean>() {

		@Override
		public void OnStart() {
			LogUtil.d(TAG, "开始请求OnStart-----------");
			dismissProgress();
		}

		@Override
		public void onError(Exception e) {
			LogUtil.d(TAG, "onError-----------" + e.getMessage());
			dismissProgress();
		}

		@Override
		public void OnPaserComplete(GetOrderListBean bean) {
			dismissProgress();
			if (bean != null) {
				LogUtil.d(TAG, "OnPaserComplete:" + bean.getHead());
				if ("00".equals(bean.getHead().getResultCode())) {
					mHandler.obtainMessage(MSG_GET_ORDER_LIST_SUCESS, bean)
							.sendToTarget();
				} else {
					mHandler.obtainMessage(MSG_GET_ORDER_LIST_FAIL,
							bean.getHead().getResultInfo()).sendToTarget();
				}
			} else {
				mHandler.obtainMessage(MSG_GET_ORDER_LIST_FAIL,
						getString(R.string.get_order_list_failed_string))
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
			case MSG_GET_ORDER_LIST_SUCESS:
				GetOrderListBean golBean = (GetOrderListBean)msg.obj;
				
				if(golBean != null && golBean.getBody() != null && golBean.getBody().size() != 0) {
					iniVariable(golBean.getBody());
				}
				
				break;
			case MSG_GET_ORDER_LIST_FAIL:
				Toast.makeText(MyOrderActivity.this, (String)msg.obj, Toast.LENGTH_SHORT).show();
				break;
			default:
				break;
			}
		};
	};

	@Override
	public void onClick(View v) {
		if (v == null) {
			return;
		}

		switch (v.getId()) {
		case R.id.backIv:
			this.finish();
			break;
		default:
			break;
		}
	}
}