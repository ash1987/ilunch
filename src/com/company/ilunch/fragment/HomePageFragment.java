package com.company.ilunch.fragment;

import java.util.ArrayList;
import org.json.JSONObject;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.alibaba.fastjson.JSON;
import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.location.LocationManagerProxy;
import com.amap.api.location.LocationProviderProxy;
import com.company.ilunch.R;
import com.company.ilunch.base.BaseFragment;
import com.company.ilunch.bean.GetShopDataBean;
import com.company.ilunch.net.HttpUrlManager;
import com.company.ilunch.net.RequestListener;
import com.company.ilunch.net.RequestParams;
import com.company.ilunch.preferences.IlunchPreference;
import com.company.ilunch.task.GetShopDataTask;
import com.company.ilunch.ui.BookingActivity;
import com.company.ilunch.ui.LocationCityActivity;
import com.company.ilunch.ui.MyLocationActivity;
import com.company.ilunch.ui.TakePackageActivity;
import com.company.ilunch.utils.LogUtil;
import com.company.ilunch.widget.CustomDialog;
import com.company.ilunch.bean.GetShopDataBean.Body;

/**
 * 首页Fragment
 */
public class HomePageFragment extends BaseFragment implements OnClickListener,
		AMapLocationListener {
	public final static String TAG = "com.company.ilunch";

	public static final String FRAGMENT_TAG = HomePageFragment.class
			.getSimpleName();

	public final static int GET_SHOP_DATA_SUCESS = 0x01;// 获取菜品列表成功
	private LocationManagerProxy mAMapLocManager = null;

	private RequestParams requestParams;// 请求参数封装的键值对

	private ImageView dzIv;// 订座按钮
	private ImageView dbIv;// 打包按钮
	private TextView userAddressTv;// 所在城市

	private CustomDialog customDialog;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initData();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.home_page, null);
		initView(view);
		setAttribute();
		return view;
	}

	@Override
	public void onResume() {
		super.onResume();

//		IlunchPreference ilPreference = new IlunchPreference(
//				HomePageFragment.this.getActivity());
//		if (TextUtils.isEmpty(ilPreference.getMyLocationDs())) {
//			Resources resource = HomePageFragment.this.getActivity()
//					.getApplicationContext().getResources();
//			showUpdateCityDialog(
//					resource.getString(R.string.update_city_title),
//					resource.getString(R.string.update_city_content1),
//					resource.getString(R.string.ensure),
//					resource.getString(R.string.cancel));
//		} else {
//			userAddressTv.setText(ilPreference.getLocationCity());
//		}
		IlunchPreference ilPreference = new IlunchPreference(
				HomePageFragment.this.getActivity());
		if (TextUtils.isEmpty(ilPreference.getMyLocationDs())) {
			Resources resource = HomePageFragment.this.getActivity()
					.getApplicationContext().getResources();
			showUpdateCityDialog1(
					resource.getString(R.string.update_city_title),
					resource.getString(R.string.update_city_content2),
					resource.getString(R.string.ensure));
		}
	}

	// 初始化数据
	private void initData() {
		requestParams = new RequestParams();
	}

	// 初始化控件
	private void initView(View view) {
		dzIv = (ImageView) view.findViewById(R.id.dzIv);
		dbIv = (ImageView) view.findViewById(R.id.dbIv);
		userAddressTv = (TextView) view.findViewById(R.id.userAddressTv);
	}

	// 设置控件属性
	private void setAttribute() {
		dzIv.setOnClickListener(this);
		dbIv.setOnClickListener(this);
		userAddressTv.setOnClickListener(this);

		// 定位
		mAMapLocManager = LocationManagerProxy
				.getInstance(HomePageFragment.this.getActivity());
		mAMapLocManager.requestLocationUpdates(
				LocationProviderProxy.AMapNetwork, 2000, 10, this);
		mAMapLocManager.setGpsEnable(false);

		doGetShopData();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.dzIv:// 订座
			startActivity(new Intent(HomePageFragment.this.getActivity(),
					BookingActivity.class));
			break;
		case R.id.dbIv:// 打包
			startActivity(new Intent(HomePageFragment.this.getActivity(),
					TakePackageActivity.class));
			break;
		case R.id.userAddressTv:
			startActivity(new Intent(HomePageFragment.this.getActivity(),
					LocationCityActivity.class));
			break;
		default:
			break;
		}
	}

	/**
	 * 向服务器请求获取菜品分类 <br/>
	 * 
	 */
	private void doGetShopData() {
		GetShopDataTask task = new GetShopDataTask();
		requestParams.clear();

		JSONObject requestParams = new JSONObject();

		task.request(HomePageFragment.this.getActivity(),
				HttpUrlManager.GET_SHOP_DATA_STRING, requestParams,
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
					mHandler.obtainMessage(GET_SHOP_DATA_SUCESS, bean)
							.sendToTarget();
				}
			}
		}
	};

	/*
	 * 保存菜品列表信息
	 */
	private void saveShopData(GetShopDataBean bean) {
		IlunchPreference iPreference = new IlunchPreference(
				HomePageFragment.this.getActivity());
		iPreference.clearShopData(HomePageFragment.this.getActivity());

		ArrayList<Body> data = new ArrayList<Body>();
		data.addAll(bean.getBody());

		GetShopDataBean gsDBean = new GetShopDataBean();
		gsDBean.setBody(data);

		iPreference.saveShopData(HomePageFragment.this.getActivity(),
				JSON.toJSONString(gsDBean));
	}

	/**
	 * handler用于处理网络请求的返回数据
	 */
	private Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			Object object = msg.obj;
			switch (msg.what) {
			case GET_SHOP_DATA_SUCESS:// 获取菜品列表成功
				saveShopData((GetShopDataBean) object);
				break;
			default:
				break;
			}
		}
	};

	@Override
	public void onLocationChanged(Location location) {
	}

	@Override
	public void onProviderDisabled(String provider) {

	}

	@Override
	public void onProviderEnabled(String provider) {

	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {

	}

	/**
	 * 混合定位回调函数
	 */
	@Override
	public void onLocationChanged(AMapLocation location) {
		if (location != null) {
			if (mAMapLocManager != null) {
				mAMapLocManager.removeUpdates(this);
				mAMapLocManager.destory();
				mAMapLocManager = null;
			}

			compareCity(location.getCity());
		}
	}

	/*
	 * 比较当前城市是否正确
	 */
	private void compareCity(String newLocationCity) {
		IlunchPreference ilPreference = new IlunchPreference(
				HomePageFragment.this.getActivity());
		if (TextUtils.isEmpty(ilPreference.getLocationCity())) {
			// 首次使用
		} else {
			if (newLocationCity.contains(userAddressTv.getText().toString())
					|| userAddressTv.getText().toString()
							.contains(newLocationCity)) {

			} else {
				Resources resource = HomePageFragment.this.getActivity()
						.getApplicationContext().getResources();
				showUpdateCityDialog(
						resource.getString(R.string.update_city_title),
						String.format(resource
								.getString(R.string.update_city_content),
								newLocationCity),
						resource.getString(R.string.update_city),
						resource.getString(R.string.cancel));
			}
		}
	}

	/**
	 * 城市切换对话框
	 */
	private void showUpdateCityDialog(String title, String content,
			String positive, String nagetive) {
		if (customDialog!=null && customDialog.isShowing()) {
			return;
		}

		DisplayMetrics dm = new DisplayMetrics();
		dm = HomePageFragment.this.getActivity().getApplicationContext()
				.getResources().getDisplayMetrics();
		int screenWidth = dm.widthPixels;
		customDialog = new CustomDialog(HomePageFragment.this.getActivity(),
				R.layout.custom_dialog, 2, screenWidth);

		customDialog.setDialogGravity(Gravity.CENTER);
		customDialog.setTitleContent(title, content);
		customDialog.setPositiveButton(positive,
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						startActivity(new Intent(HomePageFragment.this
								.getActivity(), LocationCityActivity.class));
					}
				});

		customDialog.setNagetiveButton(nagetive,
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				});

		customDialog.show();
	}
	
	/**
	 * 选择地址对话框
	 */
	private void showUpdateCityDialog1(String title, String content,
			String positive) {
		if (customDialog!=null && customDialog.isShowing()) {
			return;
		}

		DisplayMetrics dm = new DisplayMetrics();
		dm = HomePageFragment.this.getActivity().getApplicationContext()
				.getResources().getDisplayMetrics();
		int screenWidth = dm.widthPixels;
		customDialog = new CustomDialog(HomePageFragment.this.getActivity(),
				R.layout.custom_dialog, 1, screenWidth);

		customDialog.setCancelable(false);
		customDialog.setDialogGravity(Gravity.CENTER);
		customDialog.setTitleContent(title, content);
		customDialog.setPositiveButton(positive,
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						startActivity(new Intent(HomePageFragment.this
								.getActivity(), MyLocationActivity.class));
					}
				});

		customDialog.show();
	}

}
