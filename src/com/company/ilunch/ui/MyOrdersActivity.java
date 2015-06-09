package com.company.ilunch.ui;

import java.util.ArrayList;

import org.json.JSONObject;

import android.content.Intent;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.Toast;

import com.company.ilunch.R;
import com.company.ilunch.adapter.MyOrdersAdapter;
import com.company.ilunch.base.BaseActivity;
import com.company.ilunch.bean.GetOrderListBean;
import com.company.ilunch.bean.GetOrderListBean.Body;
import com.company.ilunch.net.HttpUrlManager;
import com.company.ilunch.net.RequestListener;
import com.company.ilunch.preferences.LoginPreference;
import com.company.ilunch.task.GetOrderListTask;
import com.company.ilunch.utils.LogUtil;
import com.company.ilunch.widget.UpRefreshListView;

/**
 * 我的订单
 */
public class MyOrdersActivity extends BaseActivity implements OnClickListener {
	public final static String TAG = "com.company.ilunch";

	private final static int MSG_GET_ORDER_LIST_SUCESS = 0x01;// 获取订单列表成功
	private final static int MSG_GET_ORDER_LIST_FAIL = 0x02;// 　获取订单列表成功

	private LoginPreference loginPreference;
	
	private UpRefreshListView orderListView;
	private ArrayList<Body> dataList;
	private ImageView backIv;// 返回
	
	private MyOrdersAdapter moAdapter;

	@Override
	public void initData() {
		loginPreference = new LoginPreference(this);
		dataList = new ArrayList<GetOrderListBean.Body>();
	}

	@Override
	public void initView() {
		setContentView(R.layout.my_orders_layout);
		
		backIv = (ImageView) this.findViewById(R.id.backIv);
		orderListView = (UpRefreshListView) findViewById(R.id.orderListView);
	}

	@Override
	public void setAttribute() {
		backIv.setOnClickListener(this);
		moAdapter = new MyOrdersAdapter(this, dataList);
		orderListView.setAdapter(moAdapter);

		if (loginPreference.getLoginState()) {
			doGetOrderList();
		} else {
			startActivity(new Intent(MyOrdersActivity.this, LoginActivity.class));
			finish();
		}
	}

	/**
	 * 向服务器请求获取订单列表 <br/>
	 * 
	 */
	private void doGetOrderList() {
		showProgress("", "");

		GetOrderListTask task = new GetOrderListTask();

		JSONObject requestParams = new JSONObject();
		try {
			requestParams.put("UserId",
					Integer.parseInt(loginPreference.getDataID()));
			requestParams.put("PageIndex", 1);
			requestParams.put("PageSize", 100);
		} catch (Exception e) {
			e.printStackTrace();
		}

		task.request(this, HttpUrlManager.GET_MY_ORDER_LIST_STRING,
				requestParams, getMyOrderListListener);
	}

	/**
	 * 获取菜品分类接口监听类
	 */
	private RequestListener<GetOrderListBean> getMyOrderListListener = new RequestListener<GetOrderListBean>() {

		@Override
		public void OnStart() {
			LogUtil.d(TAG, "开始请求OnStart-----------");
			dismissProgress();
		}

		@Override
		public void onError(Exception e) {
			LogUtil.d(TAG, "onError-----------" + e.getMessage());
			dismissProgress();
		}

		@Override
		public void OnPaserComplete(GetOrderListBean bean) {
			dismissProgress();
			if (bean != null) {
				LogUtil.d(TAG, "OnPaserComplete:" + bean.getHead());
				if ("00".equals(bean.getHead().getResultCode())) {
					mHandler.obtainMessage(MSG_GET_ORDER_LIST_SUCESS, bean)
							.sendToTarget();
				} else {
					mHandler.obtainMessage(MSG_GET_ORDER_LIST_FAIL,
							bean.getHead().getResultInfo()).sendToTarget();
				}
			} else {
				mHandler.obtainMessage(MSG_GET_ORDER_LIST_FAIL,
						getString(R.string.get_order_list_failed_string))
						.sendToTarget();
			}
		}
	};

	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			super.handleMessage(msg);

			if (msg == null) {
				return;
			}

			switch (msg.what) {
			case MSG_GET_ORDER_LIST_SUCESS:
				GetOrderListBean golBean = (GetOrderListBean) msg.obj;

				if (golBean != null && golBean.getBody() != null
						&& golBean.getBody().size() != 0) {
					dataList.clear();
					dataList.addAll(golBean.getBody());
				}
				
				moAdapter.notifyDataSetChanged();
				break;
			case MSG_GET_ORDER_LIST_FAIL:
				Toast.makeText(MyOrdersActivity.this, (String) msg.obj,
						Toast.LENGTH_SHORT).show();
				break;
			default:
				break;
			}
		};
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
		default:
			break;
		}
	}
}