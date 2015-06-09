package com.company.ilunch.ui;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.company.ilunch.IlunchApplication;
import com.company.ilunch.R;
import com.company.ilunch.adapter.ShopCartListAdapter;
import com.company.ilunch.base.BaseFragmentActivity;
import com.company.ilunch.bean.DelCartBean;
import com.company.ilunch.bean.GetCartListBean;
import com.company.ilunch.bean.GetShopDataBean;
import com.company.ilunch.bean.UpdateCartBean;
import com.company.ilunch.bean.GetShopDataBean.Body;
import com.company.ilunch.fragment.BookingFragment;
import com.company.ilunch.net.HttpUrlManager;
import com.company.ilunch.net.RequestListener;
import com.company.ilunch.preferences.IlunchPreference;
import com.company.ilunch.preferences.LoginPreference;
import com.company.ilunch.task.DelCartTask;
import com.company.ilunch.task.GetCartListTask;
import com.company.ilunch.task.GetShopDataTask;
import com.company.ilunch.task.UpdateCartTask;
import com.company.ilunch.utils.AndroidUtils;
import com.company.ilunch.utils.LogUtil;
import com.company.ilunch.widget.MenuPopupWindow;
import com.company.ilunch.widget.SlideListView;

/**
 * 订座/打包基类
 */
public class FoodListBaseActivity extends BaseFragmentActivity implements
		OnCheckedChangeListener, OnClickListener {
	public final static String TAG = "com.company.ilunch";

	public final static String UPDATE_CART_ACTION_NAME = "UPDATE_CART_ACTION_NAME";
	public final static String DEL_CART_ACTION_NAME = "DEL_CART_ACTION_NAME";

	private final static int MSG_SHOW_MENU_SUCCESS = 0x01;// 显示菜单
	private final static int MSG_SHOW_MENU_FAIL = 0x02;// 显示菜单失敗
	public final static int MSG_GET_SHOP_DATA_SUCESS = 0x03;// 获取菜品列表成功
	private final static int MSG_GET_CART_LIST_SUCCESS = 0x04;// 　获取购物车成功
	private final static int MSG_GET_CART_LIST_FAIL = 0x05;// 获取购物车失敗
	private final static int MSG_UPDATE_CART_SUCCESS = 0x06;// 　更新购物车成功
	private final static int MSG_UPDATE_CART_FAIL = 0x07;// 更新购物车失敗
	private final static int MSG_DEL_CART_SUCCESS = 0x08;// 　删除购物车成功
	private final static int MSG_DEL_CART_FAIL = 0x09;// 删除购物车失敗

	private RadioGroup mRadioGroup;
	private ViewPager mViewPager;
	private ImageView backIv;// 返回

	private ImageView shopCartIv;// 购物车按钮
	private SlideListView shopCartListView;// 购物车列表
	private ShopCartListAdapter scAdapter;
	protected TextView titleTv;
	private Button enSureBt;

	private ImageView lineIv;// radiobutton下方横线
	private ImageView menuIv;// 菜单按钮
	private float mCurrentCheckedRadioLeft;// 当前被选中的RadioButton距离左侧的距离
	private HorizontalScrollView mHorizontalScrollView;// 上面的水平滚动控件

	private RelativeLayout locationRl;
	private TextView locationTv;

	private TextView numTv;
	private TextView totalNumTv;
	private TextView totalPriceTv;

	private ArrayList<Body> bodyList;// 菜品列表
	private int bottomLine;// 菜品列表下划线距离

	protected int SalesMethod;// 销售方式(打包、订座，外卖)
	private IlunchPreference ilunchPerference;
	private LoginPreference loginPreference;

	private ArrayList<GetCartListBean.Body> cartListData;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		initData();
		initView();
		setAttribute();
	}

	private void initData() {
		cartListData = new ArrayList<GetCartListBean.Body>();
		bottomLine = AndroidUtils.dip2px(this, 80);
		ilunchPerference = new IlunchPreference(this);
		loginPreference = new LoginPreference(this);
	}

	private void initView() {
		setContentView(R.layout.foodlist_base_layout);

		mRadioGroup = (RadioGroup) findViewById(R.id.radioGroup);
		backIv = (ImageView) this.findViewById(R.id.backIv);
		lineIv = (ImageView) findViewById(R.id.lineIv);
		menuIv = (ImageView) findViewById(R.id.menuIv);
		mViewPager = (ViewPager) findViewById(R.id.pager);
		mHorizontalScrollView = (HorizontalScrollView) findViewById(R.id.horizontalScrollView);
		shopCartIv = (ImageView) findViewById(R.id.shopCartIv);
		shopCartListView = (SlideListView) findViewById(R.id.shopCartListView);
		titleTv = (TextView) findViewById(R.id.titleTv);
		enSureBt = (Button) findViewById(R.id.enSureBt);
		locationRl = (RelativeLayout) findViewById(R.id.locationRl);
		locationTv = (TextView) findViewById(R.id.locationTv);

		numTv = (TextView) findViewById(R.id.numTv);
		totalNumTv = (TextView) findViewById(R.id.totalNumTv);
		totalPriceTv = (TextView) findViewById(R.id.totalPriceTv);
	}

	private void setAttribute() {
		menuIv.setOnClickListener(this);
		shopCartIv.setOnClickListener(this);
		enSureBt.setOnClickListener(this);
		mRadioGroup.setOnCheckedChangeListener(this);
		mViewPager.setOnPageChangeListener(new MyPagerOnPageChangeListener());
		locationRl.setOnClickListener(this);
		backIv.setOnClickListener(this);

		scAdapter = new ShopCartListAdapter(this, cartListData,
				new ShopCartListAdapter.Callback() {
					@Override
					public void updateCart(
							com.company.ilunch.bean.GetCartListBean.Body body,
							int num) {
						doUpdateCart(body, num);
						
						Intent intent1 = new Intent(BookingFragment.UPDATE_LIST_ACTION_NAME);
						
						// 发送广播
						sendBroadcast(intent1);
					}

					@Override
					public void deleItem(
							com.company.ilunch.bean.GetCartListBean.Body body) {
						doDelCart(body);
						
						Intent intent1 = new Intent(BookingFragment.UPDATE_LIST_ACTION_NAME);
						
						// 发送广播
						sendBroadcast(intent1);

						try {
							shopCartListView.slideBack();
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				});

		shopCartListView.setAdapter(scAdapter);
		shopCartListView.initSlideMode(SlideListView.MOD_RIGHT);

		shopCartIv.setVisibility(View.VISIBLE);
		numTv.setVisibility(View.GONE);
		totalNumTv.setVisibility(View.GONE);
		totalPriceTv.setVisibility(View.GONE);

		IlunchPreference iPreference = new IlunchPreference(this);

		if (TextUtils.isEmpty(iPreference.getShopData())) {
			doGetShopData();
		} else {
			try {
				GetShopDataBean gsdBean = JSON.toJavaObject(
						JSON.parseObject(iPreference.getShopData()),
						GetShopDataBean.class);
				bodyList = gsdBean.getBody();

				mHandler.sendEmptyMessage(MSG_SHOW_MENU_SUCCESS);
			} catch (Exception e) {
				e.printStackTrace();

				mHandler.sendEmptyMessage(MSG_SHOW_MENU_FAIL);
			}
		}

		registerBoradcastReceiver();
	}

	@Override
	protected void onResume() {
		super.onResume();

		doGetCartList();

		if (TextUtils.isEmpty(ilunchPerference.getMyLocationDs())) {
			locationRl.setVisibility(View.GONE);
		} else {
			locationRl.setVisibility(View.VISIBLE);

			locationTv.setText(ilunchPerference.getMyLocationCity()
					+ ilunchPerference.getMyLocationQy()
					+ ilunchPerference.getMyLocationDs());
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();

		unRegisterBoradcastReceiver();
	}

	@SuppressLint("NewApi")
	private void addRadioBtns() {
		if (bodyList == null || bodyList.size() == 0) {
			return;
		}

		for (int i = 0; i < bodyList.size(); i++) {
			RadioButton rb = new RadioButton(this);
			rb.setId(i);
			RadioGroup.LayoutParams params = new RadioGroup.LayoutParams(
					bottomLine, LayoutParams.MATCH_PARENT);
			rb.setLayoutParams(params);
			Bitmap a = null;
			rb.setButtonDrawable(new BitmapDrawable(a));
			if (i == 0) {
				rb.setChecked(true);
			} else {
				rb.setChecked(false);
			}
			rb.setGravity(Gravity.CENTER);
			rb.setTextColor(Color.parseColor("#555555"));
			if ("XLH".equals(AndroidUtils.getDencityType(this))) {
				rb.setTextSize(AndroidUtils.sp2px(this, 5));
			} else {
				rb.setTextSize(AndroidUtils.sp2px(this, 8));
			}
			rb.setText(bodyList.get(i).getClassname());
			rb.setTag(bodyList.get(i));

			mRadioGroup.addView(rb);
		}

		RadioButton rb = new RadioButton(this);
		rb.setId(bodyList.size());
		RadioGroup.LayoutParams params = new RadioGroup.LayoutParams(
				bottomLine, LayoutParams.MATCH_PARENT);
		rb.setLayoutParams(params);
		Bitmap a = null;
		rb.setButtonDrawable(new BitmapDrawable(a));
		rb.setChecked(false);
		rb.setGravity(Gravity.CENTER);
		rb.setPadding(AndroidUtils.dip2px(this, 3),
				AndroidUtils.dip2px(this, 3), AndroidUtils.dip2px(this, 3),
				AndroidUtils.dip2px(this, 3));
		Drawable drawable = this.getResources().getDrawable(
				R.drawable.menu_fav_icon1);
		rb.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, null,
				drawable);
		rb.setTag(null);

		mRadioGroup.addView(rb);
	}

	private void iniVariable() {
		ArrayList<Fragment> fragments = new ArrayList<Fragment>();

		for (int i = 0; i < bodyList.size(); i++) {
			fragments.add(new BookingFragment(bodyList.get(i), SalesMethod));
		}

		fragments.add(new BookingFragment(null, SalesMethod));

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
				return bottomLine * i;
			}
		}

		return 0f;
	}

	/*
	 * radiobutton下方横线动画
	 */
	private void playRbLineAnimation(float currentCheckedRadioLeft,
			int distance, ImageView iv) {
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

		mHorizontalScrollView.smoothScrollTo((int) mCurrentCheckedRadioLeft, 0);
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
		}

		public void setFragments(ArrayList<Fragment> fragments) {
			this.fragments = fragments;

			this.mViewPager.setOffscreenPageLimit(fragments.size());
			this.mViewPager.setAdapter(this);
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
	 * 向服务器请求删除购物车 <br/>
	 * 
	 */
	private void doDelCart(GetCartListBean.Body body) {
		DelCartTask task = new DelCartTask();

		JSONObject requestParams = new JSONObject();
		try {
			requestParams.put("UCode", body.getTempcode());
			requestParams.put("DataID", body.getDataId());
		} catch (JSONException e) {
			e.printStackTrace();
		}

		task.request(this, HttpUrlManager.DEL_CART_STRING, requestParams,
				delCartListener);
	}

	/**
	 * 删除购物车接口监听类
	 */
	private RequestListener<DelCartBean> delCartListener = new RequestListener<DelCartBean>() {

		@Override
		public void OnStart() {
			LogUtil.d(TAG, "开始请求OnStart-----------");
		}

		@Override
		public void onError(Exception e) {
			LogUtil.d(TAG, "onError-----------" + e.getMessage());
		}

		@Override
		public void OnPaserComplete(DelCartBean bean) {
			if (bean != null) {
				LogUtil.d(TAG, "OnPaserComplete:" + bean.getHead());
				if ("00".equals(bean.getHead().getResultCode())) {
					mHandler.obtainMessage(MSG_DEL_CART_SUCCESS, bean)
							.sendToTarget();
				} else {
					mHandler.obtainMessage(MSG_DEL_CART_FAIL,
							bean.getHead().getResultInfo()).sendToTarget();
				}
			} else {
				mHandler.obtainMessage(MSG_DEL_CART_FAIL,
						getString(R.string.del_cart_fail_string))
						.sendToTarget();
			}
		}
	};

	/**
	 * 向服务器请求更新购物车 <br/>
	 * 
	 */
	private void doUpdateCart(GetCartListBean.Body body, int num) {
		UpdateCartTask task = new UpdateCartTask();

		JSONObject requestParams = new JSONObject();
		try {
			requestParams.put("UCode", body.getTempcode());
			if (!TextUtils.isEmpty(body.getId())) {
				requestParams.put("UnId", body.getId());
			}
			requestParams.put("PNum", num);
		} catch (JSONException e) {
			e.printStackTrace();
		}

		task.request(this, HttpUrlManager.UPDATE_CART_STRING, requestParams,
				updateCartListener);
	}

	/**
	 * 更新购物车接口监听类
	 */
	private RequestListener<UpdateCartBean> updateCartListener = new RequestListener<UpdateCartBean>() {

		@Override
		public void OnStart() {
			LogUtil.d(TAG, "开始请求OnStart-----------");
		}

		@Override
		public void onError(Exception e) {
			LogUtil.d(TAG, "onError-----------" + e.getMessage());
		}

		@Override
		public void OnPaserComplete(UpdateCartBean bean) {
			if (bean != null) {
				LogUtil.d(TAG, "OnPaserComplete:" + bean.getHead());
				if ("00".equals(bean.getHead().getResultCode())) {
					mHandler.obtainMessage(MSG_UPDATE_CART_SUCCESS, bean)
							.sendToTarget();
				} else {
					mHandler.obtainMessage(MSG_UPDATE_CART_FAIL,
							bean.getHead().getResultInfo()).sendToTarget();
				}
			} else {
				mHandler.obtainMessage(MSG_UPDATE_CART_FAIL,
						getString(R.string.update_cart_failed_string))
						.sendToTarget();
			}
		}
	};

	/**
	 * 向服务器请求获取购物车 <br/>
	 * 
	 */
	private void doGetCartList() {
		GetCartListTask task = new GetCartListTask();

		JSONObject requestParams = new JSONObject();
		try {
			requestParams.put("UCode", IlunchApplication.cartUcode);
		} catch (JSONException e) {
			e.printStackTrace();
		}

		task.request(this, HttpUrlManager.GET_CART_LIST_STRING, requestParams,
				getCartListListener);
	}

	/**
	 * 获取购物车接口监听类
	 */
	private RequestListener<GetCartListBean> getCartListListener = new RequestListener<GetCartListBean>() {

		@Override
		public void OnStart() {
			LogUtil.d(TAG, "开始请求OnStart-----------");
		}

		@Override
		public void onError(Exception e) {
			LogUtil.d(TAG, "onError-----------" + e.getMessage());
		}

		@Override
		public void OnPaserComplete(GetCartListBean bean) {
			if (bean != null) {
				LogUtil.d(TAG, "OnPaserComplete:" + bean.getHead());
				if ("00".equals(bean.getHead().getResultCode())) {
					mHandler.obtainMessage(MSG_GET_CART_LIST_SUCCESS, bean)
							.sendToTarget();
				} else {
					mHandler.obtainMessage(MSG_GET_CART_LIST_FAIL,
							bean.getHead().getResultInfo()).sendToTarget();
				}
			} else {
				mHandler.obtainMessage(MSG_GET_CART_LIST_FAIL,
						getString(R.string.get_cart_failed_string))
						.sendToTarget();
			}
		}
	};

	/**
	 * 向服务器请求获取菜品分类 <br/>
	 * 
	 */
	private void doGetShopData() {
		showProgress("", "");

		GetShopDataTask task = new GetShopDataTask();

		JSONObject requestParams = new JSONObject();

		task.request(this, HttpUrlManager.GET_SHOP_DATA_STRING, requestParams,
				getShopDataListener);
	}

	/**
	 * 获取菜品分类接口监听类
	 */
	private RequestListener<GetShopDataBean> getShopDataListener = new RequestListener<GetShopDataBean>() {

		@Override
		public void OnStart() {
			LogUtil.d(TAG, "开始请求OnStart-----------");
		}

		@Override
		public void onError(Exception e) {
			dismissProgress();
			LogUtil.d(TAG, "onError-----------" + e.getMessage());
		}

		@Override
		public void OnPaserComplete(GetShopDataBean bean) {
			dismissProgress();
			if (bean != null) {
				LogUtil.d(TAG, "OnPaserComplete:" + bean.getHead());
				if ("00".equals(bean.getHead().getResultCode())) {
					mHandler.obtainMessage(MSG_GET_SHOP_DATA_SUCESS, bean)
							.sendToTarget();
				}
			}
		}
	};

	/*
	 * 保存菜品列表信息
	 */
	private void saveShopData(GetShopDataBean bean) {
		IlunchPreference iPreference = new IlunchPreference(this);
		iPreference.clearShopData(this);

		ArrayList<Body> data = new ArrayList<Body>();
		data.addAll(bean.getBody());

		GetShopDataBean gsDBean = new GetShopDataBean();
		gsDBean.setBody(data);

		iPreference.saveShopData(this, JSON.toJSONString(gsDBean));
	}

	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			super.handleMessage(msg);

			if (msg == null) {
				return;
			}

			switch (msg.what) {
			case MSG_SHOW_MENU_SUCCESS:
				addRadioBtns();

				iniVariable();

				mViewPager.setCurrentItem(0);
				setCheckedTxtColor(0);
				break;
			case MSG_SHOW_MENU_FAIL:

				break;
			case MSG_GET_SHOP_DATA_SUCESS:// 获取菜品列表成功
				GetShopDataBean gsdBean = (GetShopDataBean) msg.obj;
				saveShopData(gsdBean);

				try {
					bodyList = gsdBean.getBody();

					mHandler.sendEmptyMessage(MSG_SHOW_MENU_SUCCESS);
				} catch (Exception e) {
					e.printStackTrace();

					mHandler.sendEmptyMessage(MSG_SHOW_MENU_FAIL);
				}
				break;
			case MSG_GET_CART_LIST_SUCCESS:
				GetCartListBean gclBean = (GetCartListBean) msg.obj;
				if (gclBean != null && gclBean.getBody() != null
						&& gclBean.getBody().size() != 0) {
//					shopCartIv.setVisibility(View.VISIBLE);
					numTv.setVisibility(View.VISIBLE);
					totalNumTv.setVisibility(View.VISIBLE);
					totalPriceTv.setVisibility(View.VISIBLE);

					float totalPrice = 0;
					int totalNum = 0;

					for (int i = 0; i < gclBean.getBody().size(); i++) {
						totalPrice += Float.parseFloat(gclBean.getBody().get(i)
								.getNum())
								* Float.parseFloat(gclBean.getBody().get(i)
										.getPrice());
						totalNum += Integer.parseInt(gclBean.getBody().get(i)
								.getNum());
					}

					numTv.setText("" + totalNum);
					totalNumTv.setText(String.format(
							getResources().getString(R.string.num_fen),
							totalNum));
					totalPriceTv.setText(String.format(getResources()
							.getString(R.string.apply_yuan), totalPrice));

					cartListData.clear();
					cartListData.addAll(gclBean.getBody());
				}

				scAdapter.notifyDataSetChanged();
				break;
			case MSG_GET_CART_LIST_FAIL:
//				shopCartIv.setVisibility(View.GONE);
				numTv.setVisibility(View.GONE);
				totalNumTv.setVisibility(View.GONE);
				totalPriceTv.setVisibility(View.GONE);

				cartListData.clear();

				scAdapter.notifyDataSetChanged();
				break;
			case MSG_UPDATE_CART_SUCCESS:
				UpdateCartBean ucBean = (UpdateCartBean) msg.obj;

				doGetCartList();
				break;
			case MSG_UPDATE_CART_FAIL:
				Toast.makeText(FoodListBaseActivity.this, (String) msg.obj,
						Toast.LENGTH_SHORT).show();
				break;
			case MSG_DEL_CART_SUCCESS:
				doGetCartList();
				break;
			case MSG_DEL_CART_FAIL:
				Toast.makeText(FoodListBaseActivity.this, (String) msg.obj,
						Toast.LENGTH_SHORT).show();
				break;
			default:
				break;
			}
		};
	};

	public void registerBoradcastReceiver() {
		IntentFilter myIntentFilter = new IntentFilter();
		myIntentFilter.addAction(UPDATE_CART_ACTION_NAME);
		myIntentFilter.addAction(DEL_CART_ACTION_NAME);
		// 注册广播
		registerReceiver(mBroadcastReceiver, myIntentFilter);
	}

	public void unRegisterBoradcastReceiver() {
		unregisterReceiver(mBroadcastReceiver);
	}

	private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if (action.equals(UPDATE_CART_ACTION_NAME)) {
				doGetCartList();
			} else if(action.equals(DEL_CART_ACTION_NAME)) {
				if(intent.getExtras() == null || !intent.getExtras().containsKey("pid")) {
					return;
				}
				
				if(cartListData != null && cartListData.size() > 0 ) {
					for(int i=0;i<cartListData.size();i++) {
						if(intent.getExtras().getString("pid").equals(cartListData.get(i).getId())) {
							DelCartTask task = new DelCartTask();

							JSONObject requestParams = new JSONObject();
							try {
								requestParams.put("UCode", cartListData.get(i).getTempcode());
								requestParams.put("DataID", cartListData.get(i).getDataId());
							} catch (JSONException e) {
								e.printStackTrace();
							}

							task.request(FoodListBaseActivity.this, HttpUrlManager.DEL_CART_STRING, requestParams,
									delCartListener);
						}
					}
				}
			}
		}
	};

	@Override
	public void onClick(View v) {
		if (v == null) {
			return;
		}

		if (v.getId() == R.id.backIv) {
			this.finish();
		} else if (v.getId() == R.id.menuIv) {
			MenuPopupWindow mpw = new MenuPopupWindow(this, bodyList,
					new MenuPopupWindow.Callback() {
						@Override
						public void clickResult(int position) {
							performRtn(position);
						}
					});
			mpw.showAsDropDown(menuIv);
		} else if (v.getId() == R.id.shopCartIv) {
			if (shopCartListView.isShown()) {
				shopCartListView.setVisibility(View.GONE);
			} else {
				shopCartListView.setVisibility(View.VISIBLE);
			}
		} else if (v.getId() == R.id.enSureBt) {
			if (cartListData == null || cartListData.size() == 0) {
				Toast.makeText(FoodListBaseActivity.this,
						R.string.shopcart_null, Toast.LENGTH_SHORT).show();
				return;
			} else if (!loginPreference.getLoginState()) {
				startActivity(new Intent(FoodListBaseActivity.this,
						LoginActivity.class));
				return;
			}

			Intent enSureIntent = new Intent(FoodListBaseActivity.this,
					EnSureOrderActivity.class);
			enSureIntent.putExtra("SalesMethod", SalesMethod);
			startActivity(enSureIntent);
		} else if (v.getId() == R.id.locationRl) {
			startActivity(new Intent(this, MyLocationActivity.class));
		}
	}
}