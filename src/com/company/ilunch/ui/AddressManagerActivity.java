package com.company.ilunch.ui;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.location.LocationManagerProxy;
import com.amap.api.location.LocationProviderProxy;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.GeocodeSearch.OnGeocodeSearchListener;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.company.ilunch.R;
import com.company.ilunch.adapter.MyAddressListAdapter;
import com.company.ilunch.adapter.MyAddressListAdapter.DeleCallback;
import com.company.ilunch.base.BaseActivity;
import com.company.ilunch.bean.DeleteAddressBean;
import com.company.ilunch.bean.GetAddressListBean.Body;
import com.company.ilunch.bean.GetAddressListBean;
import com.company.ilunch.net.HttpUrlManager;
import com.company.ilunch.net.RequestListener;
import com.company.ilunch.preferences.LoginPreference;
import com.company.ilunch.task.DeleteAddressTask;
import com.company.ilunch.task.GetAddressListTask;
import com.company.ilunch.utils.LogUtil;
import com.company.ilunch.widget.SlideListView;

/*
 * 地址管理
 */
public class AddressManagerActivity extends BaseActivity implements
		OnClickListener, OnGeocodeSearchListener, AMapLocationListener, OnItemClickListener {
	public final static String TAG = "com.company.ilunch";
	public final static int MSG_GET_ADDRESS_LIST_SUCCESS = 0x01;
	public final static int MSG_GET_ADDRESS_LIST_FAILED = 0x02;
	public final static int MSG_DEL_ADDRESS_SUCCESS = 0x03;
	public final static int MSG_DEL_ADDRESS_FAILED = 0x04;
	
	private Button btn_add_address;// 添加地址
	private TextView locationAddressTv;// 定位
	private LinearLayout historyLocationLl;//历史地址容器
	private SlideListView addressList;//历史地址
	private ImageView backIv;// 返回

	private GeocodeSearch geocoderSearch;
	private LocationManagerProxy mAMapLocManager = null;
	
	private MyAddressListAdapter malAdapter;
	private ArrayList<Body> dataList;
	
	private LoginPreference loginPreference;// 登陆preference

	@Override
	protected void initData() {
		loginPreference = new LoginPreference(this);
	}

	@Override
	protected void initView() {
		setContentView(R.layout.address_manager_layout);
		
		btn_add_address = (Button) findViewById(R.id.btn_add_address);
		locationAddressTv = (TextView) findViewById(R.id.locationAddressTv);
		historyLocationLl = (LinearLayout) findViewById(R.id.historyLocationLl);
		addressList = (SlideListView) findViewById(R.id.addressList);
		backIv = (ImageView) this.findViewById(R.id.backIv);
	}

	@Override
	protected void setAttribute() {
		btn_add_address.setOnClickListener(this);
		locationAddressTv.setOnClickListener(this);
		backIv.setOnClickListener(this);

		geocoderSearch = new GeocodeSearch(this);
		geocoderSearch.setOnGeocodeSearchListener(this);
		
		dataList = new ArrayList<Body>();
		malAdapter = new MyAddressListAdapter(this, dataList, new DeleCallback() {
			@Override
			public void deleteItem(int pos) {
				doDeleAddress(pos);
				addressList.slideBack();
			}
		});
		
		addressList.setAdapter(malAdapter);
		addressList.initSlideMode(SlideListView.MOD_RIGHT);
		
		addressList.setOnItemClickListener(this);
		
		// 判断是否登录
		if (!loginPreference.getLoginState()) {
			startActivity(new Intent(this, LoginActivity.class));
			finish();
		}
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		
		doGetMyAddress();
	}
	
	/**
	 * 向服务器请求删除地址 <br/>
	 * 
	 */
	private void doDeleAddress(int pos) {
		DeleteAddressTask task = new DeleteAddressTask();

		JSONObject requestParams = new JSONObject();
		try {
			requestParams.put("UserId", Integer.parseInt(loginPreference.getDataID()));
			requestParams.put("DataId", Integer.parseInt(dataList.get(pos).getDataID()));
			requestParams.put("Receiver", dataList.get(pos).getReceiver());
			requestParams.put("Address", dataList.get(pos).getAddress());
//			requestParams.put("BuildingID", dataList.get(pos).get);
//			requestParams.put("Mobilephone", dataList.get(pos).get);
		} catch (JSONException e) {
			e.printStackTrace();
		}

		task.request(this,
				HttpUrlManager.DEL_MY_ADDRESS_STRING, requestParams,
				delAddressListener);
	}
	
	/**
	 * 删除地址接口监听类
	 */
	private RequestListener<DeleteAddressBean> delAddressListener = new RequestListener<DeleteAddressBean>() {

		@Override
		public void OnStart() {
			LogUtil.d(TAG, "开始请求OnStart-----------");
		}

		@Override
		public void onError(Exception e) {
			LogUtil.d(TAG, "onError-----------" + e.getMessage());
		}

		@Override
		public void OnPaserComplete(DeleteAddressBean bean) {
			if (bean != null) {
				LogUtil.d(TAG, "OnPaserComplete:" + bean.getHead());
				if ("00".equals(bean.getHead().getResultCode())) {
					mHandler.obtainMessage(MSG_DEL_ADDRESS_SUCCESS, bean)
					.sendToTarget();
				} else {
					mHandler.obtainMessage(MSG_DEL_ADDRESS_FAILED,
							bean.getHead().getResultInfo()).sendToTarget();
				}
			} else {
				mHandler.obtainMessage(MSG_DEL_ADDRESS_FAILED,
						getString(R.string.del_address_fail)).sendToTarget();
			}
		}
	};

	/**
	 * 向服务器请求查询地址列表 <br/>
	 * 
	 */
	private void doGetMyAddress() {
		GetAddressListTask task = new GetAddressListTask();

		JSONObject requestParams = new JSONObject();
		try {
			requestParams.put("UserId", Integer.parseInt(loginPreference.getDataID()));
			requestParams.put("PageIndex", 1);
			requestParams.put("PageSize", 5);
		} catch (JSONException e) {
			e.printStackTrace();
		}

		task.request(this,
				HttpUrlManager.GET_MY_ADDRESS_STRING, requestParams,
				getAddressListListener);
	}
	
	/**
	 * 查询地址列表接口监听类
	 */
	private RequestListener<GetAddressListBean> getAddressListListener = new RequestListener<GetAddressListBean>() {

		@Override
		public void OnStart() {
			LogUtil.d(TAG, "开始请求OnStart-----------");
		}

		@Override
		public void onError(Exception e) {
			LogUtil.d(TAG, "onError-----------" + e.getMessage());
		}

		@Override
		public void OnPaserComplete(GetAddressListBean bean) {
			if (bean != null) {
				LogUtil.d(TAG, "OnPaserComplete:" + bean.getHead());
				if ("00".equals(bean.getHead().getResultCode())) {
					mHandler.obtainMessage(MSG_GET_ADDRESS_LIST_SUCCESS, bean)
					.sendToTarget();
				} else {
					mHandler.obtainMessage(MSG_GET_ADDRESS_LIST_FAILED,
							bean.getHead().getResultInfo()).sendToTarget();
				}
			} else {
				mHandler.obtainMessage(MSG_GET_ADDRESS_LIST_FAILED,
						getString(R.string.no_data)).sendToTarget();
			}
		}
	};

	/**
	 * handler用于处理网络请求的返回数据
	 */
	private Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			Object object = msg.obj;
			switch (msg.what) {
			case MSG_GET_ADDRESS_LIST_SUCCESS:
				GetAddressListBean galBean = (GetAddressListBean) object;
				
				if(galBean != null && galBean.getBody().size() > 0) {
					dataList.clear();
					dataList.addAll(galBean.getBody());
					
					historyLocationLl.setVisibility(View.VISIBLE);
				} else {
					historyLocationLl.setVisibility(View.GONE);
				}
				
				malAdapter.notifyDataSetChanged();
				break;
			case MSG_GET_ADDRESS_LIST_FAILED:
				historyLocationLl.setVisibility(View.GONE);
//				Toast.makeText(AddressManagerActivity.this, (String)object, Toast.LENGTH_SHORT).show();
				break;
			case MSG_DEL_ADDRESS_SUCCESS:
				doGetMyAddress();
				break;
			case MSG_DEL_ADDRESS_FAILED:
				Toast.makeText(AddressManagerActivity.this, (String)object, Toast.LENGTH_SHORT).show();
				break;
			default:
				break;
			}
		}
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
		case R.id.btn_add_address:
			startActivity(new Intent(AddressManagerActivity.this,
					AddAddressActivity.class));
			break;
		case R.id.locationAddressTv:
			locationAddressTv.setText(R.string.locationing);

			// 定位
			mAMapLocManager = LocationManagerProxy.getInstance(this);
			mAMapLocManager.requestLocationUpdates(
					LocationProviderProxy.AMapNetwork, 2000, 10, this);
			mAMapLocManager.setGpsEnable(false);
			break;
		}
	}

	/**
	 * 响应逆地理编码
	 */

	public void getAddress(final LatLonPoint latLonPoint) {
		RegeocodeQuery query = new RegeocodeQuery(latLonPoint, 200,
				GeocodeSearch.AMAP);// 第一个参数表示一个Latlng，第二参数表示范围多少米，第三个参数表示是火系坐标系还是GPS原生坐标系
		geocoderSearch.getFromLocationAsyn(query);// 设置同步逆地理编码请求
	}

	/**
	 * 地理编码查询回调
	 */
	@Override
	public void onGeocodeSearched(GeocodeResult arg0, int arg1) {

	}

	/**
	 * 逆地理编码回调
	 */
	@Override
	public void onRegeocodeSearched(RegeocodeResult result, int rCode) {
		dismissProgress();
		if (rCode == 0) {
			if (result != null && result.getRegeocodeAddress() != null
					&& result.getRegeocodeAddress().getFormatAddress() != null) {
				String addressName = result.getRegeocodeAddress()
						.getFormatAddress()
						+ getResources().getString(R.string.nearby_string);
				
				Intent intent = new Intent(AddressManagerActivity.this, AddAddressActivity.class);
				intent.putExtra("locationAddress", addressName);
				startActivity(intent);
			} else {
				Toast.makeText(this,
						getResources().getString(R.string.have_no_result),
						Toast.LENGTH_SHORT).show();
			}
		} else {
			Toast.makeText(this,
					getResources().getString(R.string.net_word_error),
					Toast.LENGTH_SHORT).show();
		}
	}

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

			LatLonPoint ll = new LatLonPoint(location.getLatitude(), location.getLongitude());
			
			getAddress(ll);
		}
	}

	@Override
	public void onItemClick(AdapterView parent, View view, int position, long id) {
		Intent intent = new Intent(this, AddAddressActivity.class);
		intent.putExtra("action", 1);
		intent.putExtra("receiver", dataList.get(position).getReceiver());
//		intent.putExtra("mobilephone", dataList.get(position).get);
		intent.putExtra("address", dataList.get(position).getBuildingName());
		intent.putExtra("dataId", dataList.get(position).getDataID());
//		intent.putExtra("buildingId", dataList.get(position).get);
		intent.putExtra("locationAddress", dataList.get(position).getAddress());
		startActivity(intent);
	}
}
