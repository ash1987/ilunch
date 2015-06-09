package com.company.ilunch.ui;

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import org.json.JSONObject;
import android.content.Context;
import android.graphics.PixelFormat;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;
import com.company.ilunch.R;
import com.company.ilunch.adapter.LocationCityAdapter;
import com.company.ilunch.adapter.LocationCityAdapter.Callback;
import com.company.ilunch.base.BaseActivity;
import com.company.ilunch.bean.GetCitysBean;
import com.company.ilunch.bean.GetCitysBean.Body;
import com.company.ilunch.net.HttpUrlManager;
import com.company.ilunch.net.RequestListener;
import com.company.ilunch.net.RequestParams;
import com.company.ilunch.preferences.IlunchPreference;
import com.company.ilunch.task.GetCitysTask;
import com.company.ilunch.utils.LogUtil;
import com.company.ilunch.widget.LetterSilideBar;

/*
 * 定位城市界面
 */
public class LocationCityActivity extends BaseActivity implements
		OnClickListener {
	public final static String TAG = "com.company.ilunch";

	public final static int MSG_GET_CITYS_SUCCESS = 0x01;// 获取城市列表成功
	public final static int MSG_GET_CITYS_FAIL = 0x02;// 获取城市列表失败

	private ImageView backIv;// 返回
	private ListView cityListView;
	private LocationCityAdapter cityAdapter;
	private LetterSilideBar indexBar;
	private TextView mDialogText;
	private WindowManager mWindowManager;
	
	private RequestParams requestParams;// 请求参数封装的键值对
	
	private LinkedList<Body> cityList;

	@Override
	protected void initData() {
		requestParams = new RequestParams();
		cityList = new LinkedList<GetCitysBean.Body>();
	}

	@Override
	protected void initView() {
		setContentView(R.layout.location_city_layout);

		backIv = (ImageView) this.findViewById(R.id.backIv);
		cityListView = (ListView) this.findViewById(R.id.name_list);
		indexBar = (LetterSilideBar) this.findViewById(R.id.sideBar);
	}

	@Override
	protected void setAttribute() {
		backIv.setOnClickListener(this);
		
		mDialogText = (TextView) LayoutInflater.from(this).inflate(
				R.layout.list_position, null);
		mDialogText.setVisibility(View.INVISIBLE);
		mWindowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
		WindowManager.LayoutParams lp = new WindowManager.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT,
				WindowManager.LayoutParams.TYPE_APPLICATION,
				WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
						| WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
				PixelFormat.TRANSLUCENT);
		mWindowManager.addView(mDialogText, lp);
		indexBar.setTextView(mDialogText);

		cityAdapter = new LocationCityAdapter(this, cityList, new Callback() {
			@Override
			public void reLocation(String newLocation, String newLocationCid) {
				IlunchPreference ilPreference = new IlunchPreference(LocationCityActivity.this);
				ilPreference.setLocationCity(LocationCityActivity.this, newLocation, newLocationCid);
				
				LocationCityActivity.this.finish();
			}
		});
		cityListView.setAdapter(cityAdapter);
		indexBar.setListView(cityListView, cityAdapter);

		doGetCitys();
	}
	
	class SortByLetter implements Comparator {
		public int compare(Object o1, Object o2) {
			Body b1 = (Body) o1;
			Body b2 = (Body) o2;
			if (Integer.parseInt(b1.getSort()) < Integer.parseInt(b2.getSort()))
				return 1;
			return 0;
		}
	}

	/**
	 * 向服务器请求获取城市列表 <br/>
	 * 
	 */
	private void doGetCitys() {
		GetCitysTask task = new GetCitysTask();
		requestParams.clear();

		JSONObject requestParams = new JSONObject();

		task.request(this, HttpUrlManager.GET_CITY_LIST_STRING, requestParams,
				getCitysListener);
	}

	/**
	 * 获取城市列表接口监听类
	 */
	private RequestListener<GetCitysBean> getCitysListener = new RequestListener<GetCitysBean>() {

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
		public void OnPaserComplete(GetCitysBean bean) {
			dismissProgress();
			if (bean != null) {
				LogUtil.d(TAG, "OnPaserComplete:" + bean.getHead());
				if ("00".equals(bean.getHead().getResultCode())) {
					mHandler.obtainMessage(MSG_GET_CITYS_SUCCESS, bean)
							.sendToTarget();
				} else {
					mHandler.obtainMessage(MSG_GET_CITYS_FAIL,
							bean.getHead().getResultInfo()).sendToTarget();
				}
			} else {
				mHandler.obtainMessage(MSG_GET_CITYS_FAIL,
						getString(R.string.getcity_fail)).sendToTarget();
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
			case MSG_GET_CITYS_SUCCESS:// 获取城市列表成功
				GetCitysBean gcBean = (GetCitysBean) object;
				cityList.clear();
				cityList.addAll(gcBean.getBody());
				Collections.sort(cityList, new SortByLetter());
				cityAdapter.notifyDataSetChanged();
				break;
			case MSG_GET_CITYS_FAIL:// 获取城市列表失败
				
				break;
			default:
				break;
			}
		}
	};

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.backIv:
			this.finish();
			break;
		default:
			break;
		}
	}
}
