package com.company.ilunch.fragment;

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import org.json.JSONObject;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import com.company.ilunch.R;
import com.company.ilunch.adapter.MyLocationCityAdapter;
import com.company.ilunch.bean.GetCitysBean;
import com.company.ilunch.bean.GetCitysBean.Body;
import com.company.ilunch.net.HttpUrlManager;
import com.company.ilunch.net.RequestListener;
import com.company.ilunch.net.RequestParams;
import com.company.ilunch.preferences.IlunchPreference;
import com.company.ilunch.task.GetCitysTask;
import com.company.ilunch.utils.LogUtil;
import com.company.ilunch.widget.UpRefreshListView;

/**
 * 我的位置-城市
 */
public class MyLocationCityFragment extends BaseFragment implements
OnItemClickListener {
	public static final String FRAGMENT_TAG = MyLocationCityFragment.class
			.getSimpleName();

	public final static int MSG_GET_CITYS_SUCCESS = 0x01;// 获取城市列表成功
	public final static int MSG_GET_CITYS_FAIL = 0x02;// 获取城市列表失败

	private UpRefreshListView cityListView;
	private MyLocationCityAdapter cityAdapter;

	private IlunchPreference ilunchPerference;

	private RequestParams requestParams;// 请求参数封装的键值对

	private LinkedList<Body> cityList;
	private CityCallback callback;

	public interface CityCallback {
		public void gotoSectionPage();
	}

	public MyLocationCityFragment() {
		super();
	}

	public MyLocationCityFragment(CityCallback callback) {
		this.callback = callback;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.my_location_city_layout, null);

		initData();
		initView(view);
		setAttribute();

		return view;
	}

	private void initData() {
		requestParams = new RequestParams();
		ilunchPerference = new IlunchPreference(
				MyLocationCityFragment.this.getActivity());
		cityList = new LinkedList<GetCitysBean.Body>();

	}

	// 初始化控件
	private void initView(View view) {
		cityListView = (UpRefreshListView) view.findViewById(R.id.cityListView);
	}

	// 设置控件属性
	private void setAttribute() {
		cityListView.setOnItemClickListener(this);

		cityAdapter = new MyLocationCityAdapter(
				MyLocationCityFragment.this.getActivity(), cityList);
		cityListView.setAdapter(cityAdapter);
	}

	@Override
	public void onResume() {
		super.onResume();

		//		if (TextUtils.isEmpty(ilunchPerference.getMyLocationCity())) {
		doGetCitys();
		//		}
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
		requestParams = new RequestParams();

		JSONObject requestParams = new JSONObject();

		task.request(MyLocationCityFragment.this.getActivity(),
				HttpUrlManager.GET_CITY_LIST_STRING, requestParams,
				getCitysListener);
	}

	/**
	 * 获取城市列表接口监听类
	 */
	private RequestListener<GetCitysBean> getCitysListener = new RequestListener<GetCitysBean>() {

		@Override
		public void OnStart() {
			LogUtil.d(FRAGMENT_TAG, "开始请求OnStart-----------");
		}

		@Override
		public void onError(Exception e) {
			LogUtil.d(FRAGMENT_TAG, "onError-----------" + e.getMessage());
		}

		@Override
		public void OnPaserComplete(GetCitysBean bean) {
			if (bean != null) {
				LogUtil.d(FRAGMENT_TAG, "OnPaserComplete:" + bean.getHead());
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

				if (TextUtils.isEmpty(ilunchPerference.getMyLocationCity())
						&& cityList.size() > 0) {
					ilunchPerference.setMyLocationCity(
							MyLocationCityFragment.this.getActivity(), cityList
							.get(0).getCityName());
					ilunchPerference.setMyLocationCityID(MyLocationCityFragment.this.getActivity(), 
							cityList.get(0).getCid());
				}

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
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		ilunchPerference.setMyLocationCity(
				MyLocationCityFragment.this.getActivity(),
				cityList.get(position - 1).getCityName());
		ilunchPerference.setMyLocationCityID(MyLocationCityFragment.this.getActivity(), 
				cityList.get(position - 1).getCid());

		callback.gotoSectionPage();
	}

	@Override
	protected void lazyLoad() {
		doGetCitys();
	}
}