package com.company.ilunch.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.MarkerOptions;
import com.company.ilunch.R;

/**
 * 显示店铺位置
 */
public class MyLocationMapActivity extends Activity implements OnClickListener {
	private String endPoint;
	private String merName;

	private MapView mapView;
	private AMap aMap;

	private Button reouteBt;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.map_main);

		if (getIntent() != null) {
			handleIntent(getIntent().getExtras());
		}

		reouteBt = (Button) findViewById(R.id.reoute_bt);

		reouteBt.setOnClickListener(this);

		mapView = (MapView) findViewById(R.id.map);
		mapView.onCreate(savedInstanceState);

		init();

		setLatlon();
	}

	private void setLatlon() {
		String[] end = endPoint.split(",");

		LatLng mTarget = new LatLng(Double.valueOf(end[0]),
				Double.valueOf(end[1]));
		aMap.animateCamera(CameraUpdateFactory
				.newCameraPosition(new CameraPosition(mTarget, 15, 0, 30)),
				1000, null);
		aMap.addMarker(new MarkerOptions()
				.position(mTarget)
				.icon(BitmapDescriptorFactory
						.fromResource(R.drawable.point_start))
				.perspective(true).draggable(false));
	}

	private void init() {
		if (aMap == null) {
			aMap = mapView.getMap();
			setUpMap();
		}
	}

	private void setUpMap() {
		aMap.setMyLocationEnabled(false);
		UiSettings mUiSettings = aMap.getUiSettings();
		mUiSettings.setZoomControlsEnabled(false);
	}

	@Override
	protected void onResume() {
		super.onResume();

		mapView.onResume();
	}

	@Override
	protected void onPause() {
		super.onPause();
		mapView.onPause();
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		mapView.onSaveInstanceState(outState);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		mapView.onDestroy();
	}

	private void handleIntent(Bundle bundle) {
		if (bundle == null) {
			return;
		}

		endPoint = bundle.getString("endPoint");
		merName = bundle.getString("merName");
	}

	@Override
	public void onClick(View v) {
		if (v == null) {
			return;
		}

		switch (v.getId()) {
		case R.id.reoute_bt:
			Intent rIntent = new Intent(this, RouteActivity.class);

			rIntent.putExtra("endPoint", endPoint);

			startActivity(rIntent);
			break;
		default:
			break;
		}
	}
}