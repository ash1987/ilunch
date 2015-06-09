package com.company.ilunch.fragment;

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import org.json.JSONException;
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
import android.widget.TextView;
import com.company.ilunch.R;
import com.company.ilunch.adapter.MyLocationSectionAdapter;
import com.company.ilunch.bean.GetSectionBean;
import com.company.ilunch.bean.GetSectionBean.Body;
import com.company.ilunch.net.HttpUrlManager;
import com.company.ilunch.net.RequestListener;
import com.company.ilunch.net.RequestParams;
import com.company.ilunch.preferences.IlunchPreference;
import com.company.ilunch.task.GetSectionTask;
import com.company.ilunch.utils.LogUtil;
import com.company.ilunch.widget.UpRefreshListView;

/**
 * 我的位置-区域
 */
public class MyLocationQyFragment extends BaseFragment implements OnItemClickListener {
	public static final String FRAGMENT_TAG = MyLocationQyFragment.class
			.getSimpleName();

	public final static int MSG_GET_SECTION_SUCCESS = 0x01;// 获取区域列表成功
	public final static int MSG_GET_SECTION_FAIL = 0x02;// 获取区域列表失败

	private UpRefreshListView sectionListView;
	private MyLocationSectionAdapter sectionAdapter;

	private IlunchPreference ilunchPerference;

	private RequestParams requestParams;// 请求参数封装的键值对

	private LinkedList<Body> sectionList;
	private TextView cityInfoTv;

	private SectionCallback callback;

	public interface SectionCallback {
		public void gotoDsPage();
	}

	public MyLocationQyFragment() {
		super();
	}

	public MyLocationQyFragment(SectionCallback callback) {
		this.callback = callback;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.my_location_section_layout, null);

		initData();
		initView(view);
		setAttribute();

		return view;
	}

	private void initData() {
		requestParams = new RequestParams();
		ilunchPerference = new IlunchPreference(MyLocationQyFragment.this.getActivity());
		sectionList = new LinkedList<Body>();
	}

	// 初始化控件
	private void initView(View view) {
		sectionListView = (UpRefreshListView) view.findViewById(R.id.sectionListView);
		cityInfoTv = (TextView) view.findViewById(R.id.cityInfoTv);
	}

	// 设置控件属性
	private void setAttribute() {
		sectionListView.setOnItemClickListener(this);

		sectionAdapter = new MyLocationSectionAdapter(
				MyLocationQyFragment.this.getActivity(), sectionList);
		sectionListView.setAdapter(sectionAdapter);
	}

	@Override
	public void onResume() {
		super.onResume();

		//		if(TextUtils.isEmpty(ilunchPerference.getMyLocationQy())) {
		doGetSection();
		//		}
	}

	class SortByLetter implements Comparator {
		public int compare(Object o1, Object o2) {
			Body b1 = (Body) o1;
			Body b2 = (Body) o2;
			if (Integer.parseInt(b1.getPri()) < Integer.parseInt(b2.getPri()))
				return 1;
			return 0;
		}
	}

	/**
	 * 向服务器请求获取區域列表 <br/>
	 * 
	 */
	private void doGetSection() {
		GetSectionTask task = new GetSectionTask();
		requestParams = new RequestParams();

		JSONObject requestParams = new JSONObject();
		try {
			requestParams.put("CID", ilunchPerference.getMyLocationCityID());
		} catch (JSONException e) {
			e.printStackTrace();
		}

		task.request(MyLocationQyFragment.this.getActivity(),
				HttpUrlManager.GET_SECTION_LIST_STRING, requestParams,
				getSectionListener);
	}

	/**
	 * 获取區域列表接口监听类
	 */
	private RequestListener<GetSectionBean> getSectionListener = new RequestListener<GetSectionBean>() {

		@Override
		public void OnStart() {
			LogUtil.d(FRAGMENT_TAG, "开始请求OnStart-----------");
		}

		@Override
		public void onError(Exception e) {
			LogUtil.d(FRAGMENT_TAG, "onError-----------" + e.getMessage());
		}

		@Override
		public void OnPaserComplete(GetSectionBean bean) {
			if (bean != null) {
				LogUtil.d(FRAGMENT_TAG, "OnPaserComplete:" + bean.getHead());
				if ("00".equals(bean.getHead().getResultCode())) {
					mHandler.obtainMessage(MSG_GET_SECTION_SUCCESS, bean)
					.sendToTarget();
				} else {
					mHandler.obtainMessage(MSG_GET_SECTION_FAIL,
							bean.getHead().getResultInfo()).sendToTarget();
				}
			} else {
				mHandler.obtainMessage(MSG_GET_SECTION_FAIL,
						getString(R.string.getsection_fail)).sendToTarget();
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
			case MSG_GET_SECTION_SUCCESS:// 获取區域列表成功
				GetSectionBean gsBean = (GetSectionBean) object;
				sectionList.clear();
				sectionList.addAll(gsBean.getBody());
				Collections.sort(sectionList, new SortByLetter());

				if (TextUtils.isEmpty(ilunchPerference.getMyLocationQy())
						&& sectionList.size() > 0) {
					ilunchPerference.setMyLocationQy(
							MyLocationQyFragment.this.getActivity(), sectionList
							.get(0).getSectionName());
					ilunchPerference.setMyLocationQyID(MyLocationQyFragment.this.getActivity(), 
							sectionList.get(0).getCid());
				}

				sectionAdapter.notifyDataSetChanged();
				break;
			case MSG_GET_SECTION_FAIL:// 获取區域列表失败

				break;
			default:
				break;
			}
		}
	};

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		ilunchPerference.setMyLocationQy(
				MyLocationQyFragment.this.getActivity(),
				sectionList.get(position - 1).getSectionName());
		ilunchPerference.setMyLocationQyID(MyLocationQyFragment.this.getActivity(), 
				sectionList.get(position - 1).getSectionId());

		callback.gotoDsPage();
	}

	@Override
	protected void lazyLoad() {
		doGetSection();
		cityInfoTv.setText(ilunchPerference.getMyLocationCity());
	}
}