package com.company.ilunch.ui;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Toast;
import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.location.LocationManagerProxy;
import com.amap.api.location.LocationProviderProxy;
import com.amap.api.maps.AMap;
import com.amap.api.maps.AMapUtils;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.AMap.InfoWindowAdapter;
import com.amap.api.maps.AMap.OnMapClickListener;
import com.amap.api.maps.AMap.OnMarkerClickListener;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyLocationStyle;
import com.amap.api.maps.overlay.DrivingRouteOverlay;
import com.amap.api.maps.overlay.WalkRouteOverlay;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.GeocodeSearch.OnGeocodeSearchListener;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.amap.api.services.route.BusRouteResult;
import com.amap.api.services.route.DrivePath;
import com.amap.api.services.route.DriveRouteResult;
import com.amap.api.services.route.RouteSearch;
import com.amap.api.services.route.RouteSearch.DriveRouteQuery;
import com.amap.api.services.route.RouteSearch.OnRouteSearchListener;
import com.amap.api.services.route.WalkPath;
import com.amap.api.services.route.WalkRouteResult;
import com.company.ilunch.R;
import com.company.ilunch.preferences.IlunchPreference;
import com.company.ilunch.utils.AndroidUtils;

/*
 * 导航
 */
public class RouteActivity extends Activity implements OnMarkerClickListener,
		OnMapClickListener, InfoWindowAdapter, AMapLocationListener,
		OnRouteSearchListener, OnGeocodeSearchListener, LocationSource {

	private AMap aMap;
	private MapView mapView;

	private ProgressDialog progDialog = null;// 搜索时进度条
	private int walkMode = RouteSearch.WalkDefault;// 步行默认模式
	private WalkRouteResult walkRouteResult;// 步行模式查询结果
	private DriveRouteResult driveRouteResult;// 驾车模式查询结果
	private int drivingMode = RouteSearch.DrivingDefault;// 驾车默认模式
	private int routeType = 2;// 1代表公交模式，2代表驾车模式，3代表步行模式
	private LatLonPoint startPoint = null;
	private LatLonPoint endPoint = null;

	private boolean isClickStart = false;
	private boolean isClickTarget = false;
	private Marker startMk, targetMk;
	private RouteSearch routeSearch;
	public ArrayAdapter<String> aAdapter;

	private GeocodeSearch geocoderSearch;
	private UiSettings mUiSettings;

	private IlunchPreference mPreference;
	
	private boolean isFirst = true;

	private OnLocationChangedListener mListener;
	private final static int LOCATION_UPDATE_TIME = 5000; // 监听时间
	private final static int LOCATION_UPDATE_DISTANCE = 10; // 监听距离
	private LocationManagerProxy mAMapLocationManager;

	// private DecimalFormat df = new DecimalFormat("0.0000000000");

	@Override
	protected void onCreate(Bundle bundle) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		getWindow().requestFeature(Window.FEATURE_PROGRESS);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
				WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

		super.onCreate(bundle);

		mPreference = new IlunchPreference(this);

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.route_activity);
		mapView = (MapView) findViewById(R.id.map);
		mapView.onCreate(bundle);// 此方法必须重写
		init();

		if (getIntent() != null) {
			handleIntent(getIntent().getExtras());
		}
	}

	private void handleIntent(Bundle bundle) {
		if (bundle == null) {
			return;
		}

		String[] end = bundle.getString("endPoint").split(",");

		endPoint = convertToLatLonPoint(new LatLng(Double.valueOf(end[0]),
				Double.valueOf(end[1])));
	}

	/**
	 * 初始化AMap对象
	 */
	private void init() {
		if (aMap == null) {
			aMap = mapView.getMap();
			registerListener();
			aMap.moveCamera(CameraUpdateFactory.zoomTo(15));// 设置地图缩放比例，最大值为20

			setUpMap();
		}
		routeSearch = new RouteSearch(this);
		routeSearch.setRouteSearchListener(this);
	}

	/**
	 * mark监听以及定位
	 */
	private void setUpMap() {
		// 自定义系统定位小蓝点
		MyLocationStyle myLocationStyle = new MyLocationStyle();
		myLocationStyle.myLocationIcon(BitmapDescriptorFactory.fromBitmap(AndroidUtils
				.drawableToBitmap(getResources().getDrawable(
						R.drawable.point_start))));// 设置小蓝点的图标
		myLocationStyle.strokeColor(Color.argb(0, 255, 255, 255));// 设置圆形的边框颜色
		myLocationStyle.radiusFillColor(Color.argb(0, 255, 255, 255));// 设置圆形的填充颜色
		myLocationStyle.strokeWidth(5);// 设置圆形的边框粗细
		aMap.setMyLocationStyle(myLocationStyle);
		aMap.setLocationSource(this);
		aMap.setMyLocationEnabled(true);
		geocoderSearch = new GeocodeSearch(this);
		geocoderSearch.setOnGeocodeSearchListener(this);
		mUiSettings = aMap.getUiSettings();
		mUiSettings.setZoomControlsEnabled(true);
		mUiSettings.setMyLocationButtonEnabled(true);
		mUiSettings.setCompassEnabled(true);
		mUiSettings.setScaleControlsEnabled(true);
	}

	/**
	 * 方法必须重写
	 */
	@Override
	protected void onResume() {
		super.onResume();
		mapView.onResume();
	}

	/**
	 * 方法必须重写
	 */
	@Override
	protected void onPause() {
		super.onPause();
		mapView.onPause();

		deactivate();
	}

	/**
	 * 方法必须重写
	 */
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		mapView.onSaveInstanceState(outState);
	}

	/**
	 * 方法必须重写
	 */
	@Override
	protected void onDestroy() {
		super.onDestroy();
		mapView.onDestroy();
	}

	/**
	 * 选择步行模式
	 */
	private void walkRoute() {
		routeType = 3;// 标识为步行模式
		walkMode = RouteSearch.WalkMultipath;
	}

	/**
	 * 选择驾车模式
	 */
	private void drivingRoute() {
		routeType = 2;// 标识为驾车模式
		drivingMode = RouteSearch.DrivingSaveMoney;
	}

	/**
	 * 点击搜索按钮开始Route搜索
	 */
	public void searchRoute() {
		startSearchResult();// 开始搜终点
	}

	/**
	 * 把LatLng对象转化为LatLonPoint对象
	 */
	public static LatLonPoint convertToLatLonPoint(LatLng latlon) {
		return new LatLonPoint(latlon.latitude, latlon.longitude);
	}

	@Override
	public boolean onMarkerClick(Marker marker) {
		if (marker.isInfoWindowShown()) {
			marker.hideInfoWindow();
		} else {
			marker.showInfoWindow();
		}
		return false;
	}

	@Override
	public void onMapClick(LatLng latng) {
		if (isClickStart) {
			startMk = aMap.addMarker(new MarkerOptions()
					.anchor(0.5f, 1)
					.icon(BitmapDescriptorFactory
							.fromResource(R.drawable.point)).position(latng)
					.title("点击选择为起点"));
			startMk.showInfoWindow();
		} else if (isClickTarget) {
			targetMk = aMap.addMarker(new MarkerOptions()
					.anchor(0.5f, 1)
					.icon(BitmapDescriptorFactory
							.fromResource(R.drawable.point)).position(latng)
					.title("点击选择为目的地"));
			targetMk.showInfoWindow();
		}
	}

	@Override
	public View getInfoContents(Marker marker) {
		return null;
	}

	@Override
	public View getInfoWindow(Marker marker) {
		return null;
	}

	/**
	 * 注册监听
	 */
	private void registerListener() {
		aMap.setOnMapClickListener(RouteActivity.this);
		aMap.setOnMarkerClickListener(RouteActivity.this);
		aMap.setInfoWindowAdapter(RouteActivity.this);
	}

	/**
	 * 显示进度框
	 */
	private void showProgressDialog() {
		if (progDialog == null)
			progDialog = new ProgressDialog(this);
		progDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		progDialog.setIndeterminate(false);
		progDialog.setCancelable(true);
		progDialog.setMessage("正在搜索");
		progDialog.show();
	}

	/**
	 * 隐藏进度框
	 */
	private void dissmissProgressDialog() {
		if (progDialog != null) {
			progDialog.dismiss();
		}
	}

	/**
	 * 查询路径规划起点
	 */
	public void startSearchResult() {
		endSearchResult();
	}

	/**
	 * 查询路径规划终点
	 */
	public void endSearchResult() {
		searchRouteResult(startPoint, endPoint);
	}

	/**
	 * 开始搜索路径规划方案
	 */
	public void searchRouteResult(LatLonPoint startPoint, LatLonPoint endPoint) {
		showProgressDialog();
		final RouteSearch.FromAndTo fromAndTo = new RouteSearch.FromAndTo(
				startPoint, endPoint);
		if (routeType == 2) {// 驾车路径规划
			DriveRouteQuery query = new DriveRouteQuery(fromAndTo, drivingMode,
					null, null, "");// 第一个参数表示路径规划的起点和终点，第二个参数表示驾车模式，第三个参数表示途经点，第四个参数表示避让区域，第五个参数表示避让道路
			routeSearch.calculateDriveRouteAsyn(query);// 异步路径规划驾车模式查询
		}
	}

	/**
	 * 步行路线结果回调
	 */
	@Override
	public void onWalkRouteSearched(WalkRouteResult result, int rCode) {
		dissmissProgressDialog();
		if (rCode == 0) {
			if (result != null && result.getPaths() != null
					&& result.getPaths().size() > 0) {
				walkRouteResult = result;
				WalkPath walkPath = walkRouteResult.getPaths().get(0);
				aMap.clear();// 清理地图上的所有覆盖物
				WalkRouteOverlay walkRouteOverlay = new WalkRouteOverlay(this,
						aMap, walkPath, walkRouteResult.getStartPos(),
						walkRouteResult.getTargetPos());
				walkRouteOverlay.removeFromMap();
				walkRouteOverlay.addToMap();
				walkRouteOverlay.zoomToSpan();
			} else {
				Toast.makeText(RouteActivity.this, R.string.no_result,
						Toast.LENGTH_SHORT).show();
				finish();
			}
		} else {
			Toast.makeText(RouteActivity.this, R.string.error_network,
					Toast.LENGTH_SHORT).show();
			finish();
		}
	}

	@Override
	public void onBusRouteSearched(BusRouteResult arg0, int arg1) {

	}

	/**
	 * 驾车结果回调
	 */
	@Override
	public void onDriveRouteSearched(DriveRouteResult result, int rCode) {
		dissmissProgressDialog();
		if (rCode == 0) {
			if (result != null && result.getPaths() != null
					&& result.getPaths().size() > 0) {
				driveRouteResult = result;
				DrivePath drivePath = driveRouteResult.getPaths().get(0);
				aMap.clear();// 清理地图上的所有覆盖物
				DrivingRouteOverlay drivingRouteOverlay = new DrivingRouteOverlay(
						this, aMap, drivePath, driveRouteResult.getStartPos(),
						driveRouteResult.getTargetPos());
				drivingRouteOverlay.removeFromMap();
				drivingRouteOverlay.addToMap();
				drivingRouteOverlay.zoomToSpan();
			} else {
				Toast.makeText(this, R.string.no_result, Toast.LENGTH_SHORT)
						.show();
			}
		} else {
			Toast.makeText(this, R.string.error_network, Toast.LENGTH_SHORT)
					.show();
		}
	}

	/**
	 * 激活定位
	 */
	@Override
	public void activate(OnLocationChangedListener listener) {
		// TODO Auto-generated method stub
		mListener = listener;
		if (mAMapLocationManager == null) {
			mAMapLocationManager = LocationManagerProxy.getInstance(this);
			/*
			 * mAMapLocManager.setGpsEnable(false);
			 * 1.0.2版本新增方法，设置true表示混合定位中包含gps定位，false表示纯网络定位，默认是true Location
			 * API定位采用GPS和网络混合定位方式
			 * ，第一个参数是定位provider，第二个参数时间最短是5000毫秒，第三个参数距离间隔单位是米，第四个参数是定位监听者
			 */
			mAMapLocationManager.requestLocationUpdates(
					LocationProviderProxy.AMapNetwork, LOCATION_UPDATE_TIME,
					LOCATION_UPDATE_DISTANCE, this);
		}
	}

	/**
	 * 停止定位
	 */
	@Override
	public void deactivate() {
		mListener = null;
		if (mAMapLocationManager != null) {
			mAMapLocationManager.removeUpdates(this);
			mAMapLocationManager.destory();
		}
		mAMapLocationManager = null;

	}

	@Override
	public void onGeocodeSearched(GeocodeResult arg0, int arg1) {

	}

	@Override
	public void onRegeocodeSearched(RegeocodeResult arg0, int arg1) {

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

	// 保存经纬度
	private void setLatLng(double lat, double lng) {
		mPreference.saveLatlng(this, String.valueOf(lat), String.valueOf(lng));
	}

	// 获取保存的经纬度
	private LatLng getLatLng() {
		double lat = Double.parseDouble(mPreference.getPerferenceLat());
		double lng = Double.parseDouble(mPreference.getPerferenceLng());
		return new LatLng(lat, lng);
	}

	// 计算两个经纬度之间的距离，单位(米)
	private float getdestance(LatLng startLatlng, LatLng endLatlng) {
		return AMapUtils.calculateLineDistance(startLatlng, endLatlng);
	}

	@Override
	public void onLocationChanged(AMapLocation aLocation) {
		if (mListener != null) {
			mListener.onLocationChanged(aLocation);// 显示系统小蓝点
			Log.d("RouteActivity", "adCode = " + aLocation.getAdCode());
			Log.d("RouteActivity", "cityCode = " + aLocation.getCityCode());
			Log.d("RouteActivity", "city = " + aLocation.getCity());
			Log.d("RouteActivity", "district = " + aLocation.getDistrict());

			LatLng nowLatLng = new LatLng(aLocation.getLatitude(),
					aLocation.getLongitude());

			// 判断与上次定位到的地点距离 是否超过100米
			if (getdestance(getLatLng(), nowLatLng) > LOCATION_UPDATE_DISTANCE) {
				aMap.animateCamera(CameraUpdateFactory
						.newCameraPosition(new CameraPosition(nowLatLng, 12, 0,
								30)), 1000, null);

				// 保存经纬度
				setLatLng(aLocation.getLatitude(), aLocation.getLongitude());
			}
			
			if(isFirst) {
				isFirst = false;
				
				startPoint = convertToLatLonPoint(nowLatLng);

				drivingRoute();
				searchRoute();
			}
		}
	}
}
