package com.company.ilunch.fragment;

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import org.json.JSONException;
import org.json.JSONObject;
import android.content.Context;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout.LayoutParams;

import com.company.ilunch.R;
import com.company.ilunch.adapter.MyLocationDsAdapter;
import com.company.ilunch.bean.GetDsBean;
import com.company.ilunch.bean.GetDsBean.Body;
import com.company.ilunch.net.HttpUrlManager;
import com.company.ilunch.net.RequestListener;
import com.company.ilunch.net.RequestParams;
import com.company.ilunch.preferences.IlunchPreference;
import com.company.ilunch.task.GetDsTask;
import com.company.ilunch.utils.LogUtil;
import com.company.ilunch.widget.LetterSilideBar;
import com.company.ilunch.widget.UpRefreshListView;

/**
 * 我的位置-大厦
 */
public class MyLocationDsFragment extends BaseFragment implements OnItemClickListener {
	public static final String FRAGMENT_TAG = MyLocationDsFragment.class
			.getSimpleName();

	public final static int MSG_GET_DS_SUCCESS = 0x01;// 获取大厦列表成功
	public final static int MSG_GET_DS_FAIL = 0x02;// 获取大厦列表失败

	private UpRefreshListView dsListView;
	private MyLocationDsAdapter dsAdapter;
	
	private LetterSilideBar indexBar;
	private TextView mDialogText;
	private WindowManager mWindowManager;

	private IlunchPreference ilunchPerference;

	private RequestParams requestParams;// 请求参数封装的键值对

	private LinkedList<Body> dsList;
	private TextView sectionInfoTv;

	private DsCallback callback;

	public interface DsCallback {
		public void gotoMap(String merName, String lan, String lng);
		public void goBack(String buildName, String buildId);
	}

	public MyLocationDsFragment() {
		super();
	}

	public MyLocationDsFragment(DsCallback callback) {
		this.callback = callback;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.my_location_ds_layout, null);

		initData();
		initView(view);
		setAttribute();

		return view;
	}

	private void initData() {
		requestParams = new RequestParams();
		ilunchPerference = new IlunchPreference(MyLocationDsFragment.this.getActivity());
		dsList = new LinkedList<Body>();
	}

	// 初始化控件
	private void initView(View view) {
		dsListView = (UpRefreshListView) view.findViewById(R.id.dsListView);
		sectionInfoTv = (TextView) view.findViewById(R.id.sectionInfoTv);
		indexBar = (LetterSilideBar) view.findViewById(R.id.sideBar);
	}

	// 设置控件属性
	private void setAttribute() {
		dsListView.setOnItemClickListener(this);

		dsAdapter = new MyLocationDsAdapter(
				MyLocationDsFragment.this.getActivity(), dsList);
		dsListView.setAdapter(dsAdapter);
		
		mDialogText = (TextView) LayoutInflater.from(MyLocationDsFragment.this.getActivity()).inflate(
				R.layout.list_position, null);
		mDialogText.setVisibility(View.INVISIBLE);
		mWindowManager = (WindowManager) MyLocationDsFragment.this.getActivity().getSystemService(Context.WINDOW_SERVICE);
		WindowManager.LayoutParams lp = new WindowManager.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT,
				WindowManager.LayoutParams.TYPE_APPLICATION,
				WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
						| WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
				PixelFormat.TRANSLUCENT);
		mWindowManager.addView(mDialogText, lp);
		indexBar.setTextView(mDialogText);
		
		indexBar.setListView(dsListView, dsAdapter);
	}

	@Override
	public void onResume() {
		super.onResume();

		//		if(TextUtils.isEmpty(ilunchPerference.getMyLocationDs())) {
		doGetDs();
		//		}
	}

	class SortByLetter implements Comparator {
		public int compare(Object o1, Object o2) {
			Body b1 = (Body) o1;
			Body b2 = (Body) o2;
			if (b1.getFirstL().toCharArray()[0] < b2.getFirstL().toCharArray()[0])
				return 1;
			return 0;
		}
	}

	/**
	 * 向服务器请求获取大厦列表 <br/>
	 * 
	 */
	private void doGetDs() {
		GetDsTask task = new GetDsTask();
		requestParams = new RequestParams();

		JSONObject requestParams = new JSONObject();
		try {
			requestParams.put("SID", ilunchPerference.getMyLocationQyID());
		} catch (JSONException e) {
			e.printStackTrace();
		}

		task.request(MyLocationDsFragment.this.getActivity(),
				HttpUrlManager.GET_BUILDING_LIST_STRING, requestParams,
				getDsListener);
	}

	/**
	 * 获取區域列表接口监听类
	 */
	private RequestListener<GetDsBean> getDsListener = new RequestListener<GetDsBean>() {

		@Override
		public void OnStart() {
			LogUtil.d(FRAGMENT_TAG, "开始请求OnStart-----------");
		}

		@Override
		public void onError(Exception e) {
			LogUtil.d(FRAGMENT_TAG, "onError-----------" + e.getMessage());
		}

		@Override
		public void OnPaserComplete(GetDsBean bean) {
			if (bean != null) {
				LogUtil.d(FRAGMENT_TAG, "OnPaserComplete:" + bean.getHead());
				if ("00".equals(bean.getHead().getResultCode())) {
					mHandler.obtainMessage(MSG_GET_DS_SUCCESS, bean)
					.sendToTarget();
				} else {
					mHandler.obtainMessage(MSG_GET_DS_FAIL,
							bean.getHead().getResultInfo()).sendToTarget();
				}
			} else {
				mHandler.obtainMessage(MSG_GET_DS_FAIL,
						getString(R.string.getds_fail)).sendToTarget();
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
			case MSG_GET_DS_SUCCESS:// 获取大廈列表成功
				GetDsBean gdBean = (GetDsBean) object;
				dsList.clear();
				dsList.addAll(gdBean.getBody());
				Collections.sort(dsList, new SortByLetter());

				if (TextUtils.isEmpty(ilunchPerference.getMyLocationDs())
						&& dsList.size() > 0) {
					ilunchPerference.setMyLocationDs(
							MyLocationDsFragment.this.getActivity(), dsList
							.get(0).getName());
					ilunchPerference.setMyLocationDsID(MyLocationDsFragment.this.getActivity(), 
							dsList.get(0).getDataID());
				}

				dsAdapter.notifyDataSetChanged();
				break;
			case MSG_GET_DS_FAIL:// 获取大廈列表失败

				break;
			default:
				break;
			}
		}
	};

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		ilunchPerference.setMyLocationDs(
				MyLocationDsFragment.this.getActivity(),
				dsList.get(position - 1).getName());
		ilunchPerference.setMyLocationDsID(MyLocationDsFragment.this.getActivity(), 
				dsList.get(position - 1).getDataID());

//		callback.gotoMap(dsList.get(position-1).getName(),dsList.get(position-1).getLat(),dsList.get(position-1).getLng());
		callback.goBack(dsList.get(position -1).getName(), dsList.get(position -1).getDataID());
	}

	@Override
	protected void lazyLoad() {
		doGetDs();
		sectionInfoTv.setText(ilunchPerference.getMyLocationCity()+"-"+ilunchPerference.getMyLocationQy());
	}
}