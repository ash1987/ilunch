package com.company.ilunch.fragment;

import java.util.ArrayList;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import com.company.ilunch.R;
import com.company.ilunch.adapter.MyOrderAdapter;
import com.company.ilunch.bean.GetOrderListBean.Body;
import com.company.ilunch.net.RequestParams;
import com.company.ilunch.preferences.IlunchPreference;
import com.company.ilunch.widget.UnRefreshExpandableListView;

/**
 * 我的订单
 */
public class MyOrderInfoFragment extends Fragment implements
		OnItemClickListener {
	public static final String FRAGMENT_TAG = MyOrderInfoFragment.class
			.getSimpleName();

	private UnRefreshExpandableListView orderListView;
	private MyOrderAdapter orderAdapter;

	private ArrayList<Body> dataList;
	
	private IlunchPreference ilunchPerference;

	private RequestParams requestParams;// 请求参数封装的键值对

	public MyOrderInfoFragment(ArrayList<Body> dataList) {
		super();
		
		this.dataList = dataList;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.my_fragment_order_layout, null);

		initData();
		initView(view);
		setAttribute();

		return view;
	}

	private void initData() {
		requestParams = new RequestParams();
		ilunchPerference = new IlunchPreference(
				MyOrderInfoFragment.this.getActivity());

	}

	// 初始化控件
	private void initView(View view) {
		orderListView = (UnRefreshExpandableListView) view
				.findViewById(R.id.orderListView);
	}

	// 设置控件属性
	private void setAttribute() {
		orderListView.setOnItemClickListener(this);

		orderAdapter = new MyOrderAdapter(
				MyOrderInfoFragment.this.getActivity());
		orderListView.setAdapter(orderAdapter);
	}

	@Override
	public void onResume() {
		super.onResume();
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		
	}
}