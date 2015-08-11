package com.company.ilunch.ui;

import java.net.URLEncoder;
import java.util.ArrayList;
import org.json.JSONException;
import org.json.JSONObject;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.alipay.android.app.sdk.AliPay;
import com.company.ilunch.IlunchApplication;
import com.company.ilunch.R;
import com.company.ilunch.adapter.EnSureOrderFoodListAdapter;
import com.company.ilunch.alipay.Keys;
import com.company.ilunch.alipay.Result;
import com.company.ilunch.alipay.Rsa;
import com.company.ilunch.base.BaseActivity;
import com.company.ilunch.bean.GetCartListBean;
import com.company.ilunch.bean.GetSTemplateListBean;
import com.company.ilunch.bean.SubmitMyOrderBean;
import com.company.ilunch.bean.UpdateOrderStatusBean;
import com.company.ilunch.net.HttpUrlManager;
import com.company.ilunch.net.RequestListener;
import com.company.ilunch.preferences.IlunchPreference;
import com.company.ilunch.preferences.LoginPreference;
import com.company.ilunch.task.GetCartListTask;
import com.company.ilunch.task.GetSTemplateListTask;
import com.company.ilunch.task.SubmitMyOrderTask;
import com.company.ilunch.task.UpdateOrderStatusTask;
import com.company.ilunch.utils.AndroidUtils;
import com.company.ilunch.utils.LogUtil;

/*
 * 确认支付
 */
public class EnSureOrderActivity extends BaseActivity implements
OnClickListener {
	public final static String TAG = "com.company.ilunch";

	private final static int MSG_GET_CART_LIST_SUCCESS = 0x01;// 　获取购物车成功
	private final static int MSG_GET_CART_LIST_FAIL = 0x02;// 获取购物车失敗
	private final static int MSG_GET_STEMPLATE_LIST_SUCCESS = 0x03;// 　获取备注成功
	private final static int MSG_GET_STEMPLATE_LIST_FAIL = 0x04;// 获取备注失敗
	private final static int MSG_SHOW_BEIZHU_SUCCESS = 0x05;// 显示备注
	private final static int MSG_SUBMIT_ORDER_SUCCESS = 0x06;// 　提交订单成功
	private final static int MSG_SUBMIT_ORDER_FAIL = 0x07;// 提交订单失敗
	private static final int RQF_PAY = 0x08;
	private final static int MSG_UPDATE_ORDER_STATUS_SUCCESS = 0x09;// 　更新订单状态成功
	private final static int MSG_UPDATE_ORDER_STATUS_FAIL = 0x0a;// 更新订单状态失敗

	private ImageView backIv;// 返回
	private ListView goodsListView;
	private EnSureOrderFoodListAdapter esoflAdapter;
	private ArrayList<GetCartListBean.Body> cartListData;
	private IlunchPreference ilunchPerference;
	private LoginPreference loginPreference;

	private ArrayList<GetSTemplateListBean.Body> stllDataList;

	private TextView titleTv;
	private TextView numTv;
	private TextView totalPriceTv;
	private TextView my_address_tv;
	private RelativeLayout msztRl;
	private RelativeLayout eatTimeRl;
	private TextView eatTimeTv;
	private RelativeLayout cpbzRl;
	private RadioGroup cpbzRg;
	private ImageView cpbzIv;
	private Button btn_sure;
	private LinearLayout payTypeContainerLl;
	private LinearLayout containserLl;
	private View payTypeLineView;

	private LinearLayout orderInfoLl;
	private TextView orderInfoDesc;
	private TextView orderInfoAddressTv;
	private TextView orderInfoTimeTv;
	private TextView orderBzTv;

	private RadioButton zfbRb;
	private RadioButton wetchatRb;

	private int SalesMethod;// 打包、订座、外卖
	private String Remark;
	private String sendAddress;
	private String currentId;

	private String subject;
	private String body;
	private String total_fee;

	@Override
	protected void initData() {
		cartListData = new ArrayList<GetCartListBean.Body>();
		stllDataList = new ArrayList<GetSTemplateListBean.Body>();
		ilunchPerference = new IlunchPreference(this);
		loginPreference = new LoginPreference(this);

		if (getIntent() != null && getIntent().getExtras() != null) {
			if (getIntent().getExtras().containsKey("SalesMethod")) {
				SalesMethod = getIntent().getExtras().getInt("SalesMethod");
			}
		}
	}

	@Override
	protected void initView() {
		setContentView(R.layout.ensure_order_layout);

		titleTv = (TextView) findViewById(R.id.titleTv);
		backIv = (ImageView) findViewById(R.id.backIv);
		goodsListView = (ListView) findViewById(R.id.goodsListView);
		numTv = (TextView) findViewById(R.id.numTv);
		totalPriceTv = (TextView) findViewById(R.id.totalPriceTv);
		my_address_tv = (TextView) findViewById(R.id.my_address_tv);
		msztRl = (RelativeLayout) findViewById(R.id.msztRl);
		eatTimeRl = (RelativeLayout) findViewById(R.id.eatTimeRl);
		eatTimeTv = (TextView) findViewById(R.id.eatTimeTv);
		cpbzRl = (RelativeLayout) findViewById(R.id.cpbzRl);
		cpbzRg = (RadioGroup) findViewById(R.id.cpbzRg);
		cpbzIv = (ImageView) findViewById(R.id.cpbzIv);
		zfbRb = (RadioButton) findViewById(R.id.zfbRb);
		wetchatRb = (RadioButton) findViewById(R.id.wetchatRb);
		btn_sure = (Button) findViewById(R.id.btn_sure);
		payTypeContainerLl = (LinearLayout) findViewById(R.id.payTypeContainerLl);
		containserLl = (LinearLayout) findViewById(R.id.containserLl);
		payTypeLineView = findViewById(R.id.payTypeLineView);

		orderInfoLl = (LinearLayout) findViewById(R.id.orderInfoLl);
		orderInfoDesc = (TextView) findViewById(R.id.orderInfoDesc);
		orderInfoAddressTv = (TextView) findViewById(R.id.orderInfoAddressTv);
		orderInfoTimeTv = (TextView) findViewById(R.id.orderInfoTimeTv);
		orderBzTv = (TextView) findViewById(R.id.orderBzTv);
	}

	@Override
	protected void setAttribute() {
		backIv.setOnClickListener(this);
		cpbzRl.setOnClickListener(this);
		btn_sure.setOnClickListener(this);
		eatTimeRl.setOnClickListener(this);

		zfbRb.setOnCheckedChangeListener(new zfbOnchecked());
		wetchatRb.setOnCheckedChangeListener(new wetchatOnchecked());

		cpbzRl.setVisibility(View.GONE);
		cpbzRg.setVisibility(View.GONE);

		esoflAdapter = new EnSureOrderFoodListAdapter(this, cartListData);
		goodsListView.setAdapter(esoflAdapter);

		doGetCartList();
		AndroidUtils.setListViewHeightBasedOnChildren(goodsListView);

		doGetSTemplateList();
	}

	class zfbOnchecked implements OnCheckedChangeListener {

		@Override
		public void onCheckedChanged(CompoundButton arg0, boolean ischecked) {
			if (ischecked) {
				zfbRb.setChecked(true);
				wetchatRb.setChecked(false);
			} else {
				wetchatRb.setChecked(true);
				zfbRb.setChecked(false);
			}
		}
	}

	class wetchatOnchecked implements OnCheckedChangeListener {

		@Override
		public void onCheckedChanged(CompoundButton arg0, boolean ischecked) {
			if (ischecked) {
				wetchatRb.setChecked(true);
				zfbRb.setChecked(false);
			} else {
				zfbRb.setChecked(true);
				wetchatRb.setChecked(false);
			}
		}
	}

	@Override
	protected void onResume() {
		super.onResume();

		if (TextUtils.isEmpty(ilunchPerference.getMyLocationDs())) {
			my_address_tv.setText(R.string.add_addres_desc);
			msztRl.setOnClickListener(new AddressOnclick(1));
		} else {
			int addressResId = R.string.default_address_desc;
			if(SalesMethod == 0) {
				addressResId = R.string.yc_address_string;
			} else if(SalesMethod == 1) {
				addressResId = R.string.qc_address_string;
			} else if(SalesMethod == 2) {
				addressResId = R.string.sc_address_string;
			}

			my_address_tv.setText(String.format(
					getString(addressResId),
					ilunchPerference.getMyLocationCity()
					+ ilunchPerference.getMyLocationQy()
					+ ilunchPerference.getMyLocationDs()));
			sendAddress = ilunchPerference.getMyLocationCity()
					+ ilunchPerference.getMyLocationQy()
					+ ilunchPerference.getMyLocationDs();
			msztRl.setOnClickListener(new AddressOnclick(2));
		}

		String ilunchTimeAlert = ilunchPerference.getLunchTimeAlert();
		if (!TextUtils.isEmpty(ilunchTimeAlert)) {
			eatTimeTv.setText(String.format(getString(R.string.eat_time_desc),
					ilunchPerference.getLunchTimeAlert()));
		}
	}

	class AddressOnclick implements OnClickListener {
		private int action;

		public AddressOnclick(int action) {
			this.action = action;
		}

		@Override
		public void onClick(View v) {
			if (action == 1) {
				startActivity(new Intent(EnSureOrderActivity.this,
						AddAddressActivity.class));
			} else {
				startActivity(new Intent(EnSureOrderActivity.this,
						MyLocationActivity.class));
			}
		}
	}
	
	/**
	 * 向服务器请求更新订单状态 <br/>
	 * 
	 */
	private void doUpdateOrderStatus(String payState) {
		UpdateOrderStatusTask task = new UpdateOrderStatusTask();

		JSONObject requestParams = new JSONObject();
		try {
			requestParams.put("OrderId", currentId);
			requestParams.put("PayMode", zfbRb.isChecked()?"1":"2");
			requestParams.put("PayState", payState);
		} catch (JSONException e) {
			e.printStackTrace();
		}

		task.request(this, HttpUrlManager.getUpdateOrderStatusUrl(), requestParams,
				updateOrderStatusListener);
	}

	/**
	 * 更新订单状态 接口监听类
	 */
	private RequestListener<UpdateOrderStatusBean> updateOrderStatusListener = new RequestListener<UpdateOrderStatusBean>() {

		@Override
		public void OnStart() {
			LogUtil.d(TAG, "开始请求OnStart-----------");
		}

		@Override
		public void onError(Exception e) {
			LogUtil.d(TAG, "onError-----------" + e.getMessage());
		}

		@Override
		public void OnPaserComplete(UpdateOrderStatusBean bean) {
			if (bean != null) {
				LogUtil.d(TAG, "OnPaserComplete:" + bean.getHead());
				if ("00".equals(bean.getHead().getResultCode())) {
					mHandler.obtainMessage(MSG_UPDATE_ORDER_STATUS_SUCCESS, bean)
					.sendToTarget();
				} else {
					mHandler.obtainMessage(MSG_UPDATE_ORDER_STATUS_FAIL,
							bean.getHead().getResultInfo()).sendToTarget();
				}
			} else {
				mHandler.obtainMessage(MSG_UPDATE_ORDER_STATUS_FAIL,
						getString(R.string.update_order_failed_string))
						.sendToTarget();
			}
		}
	};

	/**
	 * 向服务器请求获取备注 <br/>
	 * 
	 */
	private void doGetSTemplateList() {
		GetSTemplateListTask task = new GetSTemplateListTask();

		JSONObject requestParams = new JSONObject();

		task.request(this, HttpUrlManager.GET_STEMPLATE_LIST_STRING,
				requestParams, getSTemplateListBeanListener);
	}

	/**
	 * 获取菜品分类接口监听类
	 */
	private RequestListener<GetSTemplateListBean> getSTemplateListBeanListener = new RequestListener<GetSTemplateListBean>() {

		@Override
		public void OnStart() {
			LogUtil.d(TAG, "开始请求OnStart-----------");
		}

		@Override
		public void onError(Exception e) {
			LogUtil.d(TAG, "onError-----------" + e.getMessage());
		}

		@Override
		public void OnPaserComplete(GetSTemplateListBean bean) {
			if (bean != null) {
				LogUtil.d(TAG, "OnPaserComplete:" + bean.getHead());
				if ("00".equals(bean.getHead().getResultCode())) {
					mHandler.obtainMessage(MSG_GET_STEMPLATE_LIST_SUCCESS, bean)
					.sendToTarget();
				} else {
					mHandler.obtainMessage(MSG_GET_STEMPLATE_LIST_FAIL,
							bean.getHead().getResultInfo()).sendToTarget();
				}
			} else {
				mHandler.obtainMessage(MSG_GET_STEMPLATE_LIST_FAIL,
						getString(R.string.get_stemplate_list_fail))
						.sendToTarget();
			}
		}
	};

	/**
	 * 向服务器请求提交订单 <br/>
	 * 
	 */
	private void doSubmitOrder() {
		if (!loginPreference.getLoginState()) {
			startActivity(new Intent(EnSureOrderActivity.this,
					LoginActivity.class));
			return;
		}

		if(TextUtils.isEmpty(sendAddress)) {
			Toast.makeText(this, R.string.please_set_address, Toast.LENGTH_SHORT).show();
			return;
		}

		if(TextUtils.isEmpty(ilunchPerference.getLunchTimeAlert())) {
			Toast.makeText(this, R.string.please_set_alert_time, Toast.LENGTH_SHORT).show();
			return;
		}

		if(TextUtils.isEmpty(Remark)) {
			Toast.makeText(this, R.string.please_set_remind, Toast.LENGTH_SHORT).show();
			return;
		}

		showProgress("", "");

		SubmitMyOrderTask task = new SubmitMyOrderTask();

		JSONObject requestParams = new JSONObject();
		try {
			requestParams.put("UCode", IlunchApplication.cartUcode);
			requestParams.put("UserId",
					Integer.parseInt(loginPreference.getDataID()));
			requestParams.put("Address", sendAddress);
			requestParams.put("AddressID", Integer.parseInt(ilunchPerference.getMyLocationDsid()));
			requestParams.put("Remark", Remark);
			requestParams.put("SendTime", ilunchPerference.getLunchTimeAlert());
			requestParams.put("OrderType", SalesMethod);
		} catch (JSONException e) {
			e.printStackTrace();
		}

		task.request(this, HttpUrlManager.SUBMIT_MY_ORDER_STRING, requestParams,
				submitMyOrderListener);
	}

	/**
	 * 提交订单接口监听类
	 */
	private RequestListener<SubmitMyOrderBean> submitMyOrderListener = new RequestListener<SubmitMyOrderBean>() {

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
		public void OnPaserComplete(SubmitMyOrderBean bean) {
			dismissProgress();

			if (bean != null) {
				LogUtil.d(TAG, "OnPaserComplete:" + bean.getHead());
				if ("00".equals(bean.getHead().getResultCode())) {
					mHandler.obtainMessage(MSG_SUBMIT_ORDER_SUCCESS, bean)
					.sendToTarget();
				} else {
					mHandler.obtainMessage(MSG_SUBMIT_ORDER_FAIL,
							bean.getHead().getResultInfo()).sendToTarget();
				}
			} else {
				mHandler.obtainMessage(MSG_SUBMIT_ORDER_FAIL,
						getString(R.string.submit_order_failed_string))
						.sendToTarget();
			}
		}
	};

	/**
	 * 向服务器请求获取购物车 <br/>
	 * 
	 */
	private void doGetCartList() {
		GetCartListTask task = new GetCartListTask();

		JSONObject requestParams = new JSONObject();
		try {
			requestParams.put("UCode", IlunchApplication.cartUcode);
		} catch (JSONException e) {
			e.printStackTrace();
		}

		task.request(this, HttpUrlManager.GET_CART_LIST_STRING, requestParams,
				getCartListListener);
	}

	/**
	 * 获取菜品分类接口监听类
	 */
	private RequestListener<GetCartListBean> getCartListListener = new RequestListener<GetCartListBean>() {

		@Override
		public void OnStart() {
			LogUtil.d(TAG, "开始请求OnStart-----------");
		}

		@Override
		public void onError(Exception e) {
			LogUtil.d(TAG, "onError-----------" + e.getMessage());
		}

		@Override
		public void OnPaserComplete(GetCartListBean bean) {
			if (bean != null) {
				LogUtil.d(TAG, "OnPaserComplete:" + bean.getHead());
				if ("00".equals(bean.getHead().getResultCode())) {
					mHandler.obtainMessage(MSG_GET_CART_LIST_SUCCESS, bean)
					.sendToTarget();
				} else {
					mHandler.obtainMessage(MSG_GET_CART_LIST_FAIL,
							bean.getHead().getResultInfo()).sendToTarget();
				}
			} else {
				mHandler.obtainMessage(MSG_GET_CART_LIST_FAIL,
						getString(R.string.get_cart_failed_string))
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
			case MSG_GET_CART_LIST_SUCCESS:
				GetCartListBean gclBean = (GetCartListBean) msg.obj;
				if (gclBean != null && gclBean.getBody() != null
						&& gclBean.getBody().size() != 0) {
					float totalPrice = 0;
					int totalNum = 0;
					StringBuilder nameBuilder = new StringBuilder();

					for (int i = 0; i < gclBean.getBody().size(); i++) {
						totalPrice += Float.parseFloat(gclBean.getBody().get(i)
								.getNum())
								* Float.parseFloat(gclBean.getBody().get(i)
										.getPrice());
						totalNum += Integer.parseInt(gclBean.getBody().get(i)
								.getNum());
						nameBuilder.append(gclBean.getBody().get(i).getName());
						if(i != gclBean.getBody().size() - 1) {
							nameBuilder.append("/");
						}
					}

					numTv.setText("" + totalNum);
					totalPriceTv.setText(String.format(getResources()
							.getString(R.string.shopcart_total_price),
							totalPrice));

					subject = body = nameBuilder.toString();
					total_fee = totalPrice + "";

					cartListData.clear();
					cartListData.addAll(gclBean.getBody());
				}

				esoflAdapter.notifyDataSetChanged();
				AndroidUtils.setListViewHeightBasedOnChildren(goodsListView);
				break;
			case MSG_GET_CART_LIST_FAIL:
				Toast.makeText(EnSureOrderActivity.this, (String) msg.obj,
						Toast.LENGTH_SHORT).show();
				break;
			case MSG_GET_STEMPLATE_LIST_SUCCESS:
				GetSTemplateListBean gstlBean = (GetSTemplateListBean) msg.obj;

				if (gstlBean != null && gstlBean.getBody() != null
						&& gstlBean.getBody().size() != 0) {
					stllDataList.clear();
					stllDataList.addAll(gstlBean.getBody());
				}

				mHandler.sendEmptyMessage(MSG_SHOW_BEIZHU_SUCCESS);
				break;
			case MSG_GET_STEMPLATE_LIST_FAIL:
				Toast.makeText(EnSureOrderActivity.this, (String) msg.obj,
						Toast.LENGTH_SHORT).show();

				cpbzRl.setVisibility(View.GONE);
				cpbzRg.setVisibility(View.GONE);
				break;
			case MSG_SHOW_BEIZHU_SUCCESS:
				addRadioBtns();

				cpbzRl.setVisibility(View.VISIBLE);
				cpbzRg.setVisibility(View.GONE);
				break;
			case MSG_SUBMIT_ORDER_SUCCESS:
				SubmitMyOrderBean smoBean = (SubmitMyOrderBean) msg.obj;

				try{
					currentId = smoBean.getBody().get(0).getOrderId();
				}catch(Exception e) {
					e.printStackTrace();
				}

				titleTv.setText(R.string.ensure_pay_string);
				btn_sure.setText(R.string.ensure_pay_string);
				payTypeContainerLl.setVisibility(View.VISIBLE);

				payTypeContainerLl.setVisibility(View.VISIBLE);
				orderInfoLl.setVisibility(View.VISIBLE);

				containserLl.setVisibility(View.GONE);
				payTypeLineView.setVisibility(View.GONE);
				cpbzRl.setVisibility(View.GONE);
				cpbzRg.setVisibility(View.GONE);

				orderInfoAddressTv.setText(String.format(
						getString(R.string.default_address_desc),
						ilunchPerference.getMyLocationCity()
						+ ilunchPerference.getMyLocationQy()
						+ ilunchPerference.getMyLocationDs()));
				orderInfoTimeTv.setText(String.format(getString(R.string.eat_time_desc),
						ilunchPerference.getLunchTimeAlert()));
				orderBzTv.setText(String.format(getString(R.string.cpbe_desc),
						Remark));

				IlunchApplication.cartUcode = "0";
				break;
			case MSG_SUBMIT_ORDER_FAIL:
				Toast.makeText(EnSureOrderActivity.this, (String) msg.obj,
						Toast.LENGTH_SHORT).show();
				break;
			case RQF_PAY:
				Result result = new Result((String) msg.obj);
				Toast.makeText(EnSureOrderActivity.this, result.showResult(),
						Toast.LENGTH_SHORT).show();
				
				String payResult = "0";
				
				if("操作成功".equals(result.showResult())) {
					payResult = "1";
				}
				
				doUpdateOrderStatus(payResult);
				break;
			case MSG_UPDATE_ORDER_STATUS_SUCCESS:
			case MSG_UPDATE_ORDER_STATUS_FAIL:
				Intent moiIntent = new Intent(EnSureOrderActivity.this, MyOrderInfoActivity.class);
				moiIntent.putExtra("SalesMethod", SalesMethod);
				moiIntent.putExtra("OrderId", currentId);
				moiIntent.putExtra("alertTime", ilunchPerference.getLunchTimeAlert());
				
				startActivity(moiIntent);
				
				EnSureOrderActivity.this.finish();
				break;
			default:
				break;
			}
		};
	};

	@SuppressLint("NewApi")
	private void addRadioBtns() {
		if (stllDataList == null || stllDataList.size() == 0) {
			return;
		}

		LinearLayout ll = null;
		for (int i = 0; i < stllDataList.size(); i++) {
			if (i % 3 == 0) {
				ll = new LinearLayout(this);
				RadioGroup.LayoutParams params = new RadioGroup.LayoutParams(
						LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
				ll.setLayoutParams(params);
				ll.setOrientation(LinearLayout.HORIZONTAL);
				ll.setWeightSum(3);

				cpbzRg.addView(ll);
			}
			RadioButton rb = new RadioButton(this);
			rb.setId(i);
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0,
					LayoutParams.WRAP_CONTENT, 1);
			rb.setLayoutParams(params);
			rb.setButtonDrawable(R.drawable.goods_cpbz_selector);

			if (stllDataList.get(i).isIscheck()) {
				rb.setChecked(true);
				Remark = stllDataList.get(i).getRemark();
			} else {
				rb.setChecked(false);
			}

			rb.setTextColor(Color.parseColor("#666666"));
			rb.setTextSize(AndroidUtils.sp2px(this, 6));
			rb.setText(stllDataList.get(i).getRemark());
			rb.setTag(stllDataList.get(i));

			ll.addView(rb);

			rb.setOnCheckedChangeListener(new BzOnCheckedListener(i));
		}
	}

	class BzOnCheckedListener implements OnCheckedChangeListener {
		private int index;

		public BzOnCheckedListener(int i) {
			this.index = i;
		}

		@Override
		public void onCheckedChanged(CompoundButton cb, boolean isChecked) {
			cb.setChecked(true);
			for (int i = 0; i < stllDataList.size(); i++) {
				stllDataList.get(i).setIscheck(false);
			}
			stllDataList.get(index).setIscheck(true);

			cpbzRg.removeAllViews();
			addRadioBtns();
		}
	}

	private void zfbPay(String outTradeNo) {
		try {
			String info = getNewOrderInfo(outTradeNo);
			String sign = Rsa.sign(info, Keys.PRIVATE);
			sign = URLEncoder.encode(sign);
			info += "&sign=\"" + sign + "\"&" + getSignType();

			Log.e("ash", info);

			final String orderInfo = info;
			new Thread() {
				public void run() {
					AliPay alipay = new AliPay(EnSureOrderActivity.this, mHandler);

					// 设置为沙箱模式，不设置默认为线上环境
					// alipay.setSandBox(true);

					String result = alipay.pay(orderInfo);

					Message msg = new Message();
					msg.what = RQF_PAY;
					msg.obj = result;
					mHandler.sendMessage(msg);
				}
			}.start();

		} catch (Exception ex) {
			ex.printStackTrace();
			Toast.makeText(EnSureOrderActivity.this, R.string.remote_call_failed,
					Toast.LENGTH_SHORT).show();
		}
	}

	private String getNewOrderInfo(String outTradeNo) {
		StringBuilder sb = new StringBuilder();
		sb.append("partner=\"");
		sb.append(Keys.DEFAULT_PARTNER);
		sb.append("\"&out_trade_no=\"");
		sb.append(outTradeNo);
		sb.append("\"&subject=\"");
		sb.append(subject);
		sb.append("\"&body=\"");
		sb.append(body);
		sb.append("\"&total_fee=\"");
		sb.append(total_fee);
		sb.append("\"&notify_url=\"");

		// 网址需要做URL编码
		sb.append(URLEncoder
				.encode("http://121.199.15.192/awc/success.aspx"));
		sb.append("\"&service=\"mobile.securitypay.pay");
		sb.append("\"&_input_charset=\"utf-8");
		sb.append("\"&return_url=\"");
		sb.append(URLEncoder.encode("http://m.alipay.com"));
		sb.append("\"&payment_type=\"1");
		sb.append("\"&seller_id=\"");
		sb.append(Keys.DEFAULT_SELLER);

		// 如果show_url值为空，可不传
		// sb.append("\"&show_url=\"");
		sb.append("\"&it_b_pay=\"1m");
		sb.append("\"");

		return new String(sb);
	}

	private String getSignType() {
		return "sign_type=\"RSA\"";
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.backIv:
			this.finish();
			break;
		case R.id.cpbzRl:
			if (cpbzRg.isShown()) {
				cpbzRg.setVisibility(View.GONE);
				cpbzIv.setImageResource(R.drawable.icon_down);
			} else {
				cpbzRg.setVisibility(View.VISIBLE);
				cpbzIv.setImageResource(R.drawable.icon_up);
			}
			break;
		case R.id.eatTimeRl:
			startActivity(new Intent(EnSureOrderActivity.this,
					EatTimeActivity.class));
			break;
		case R.id.btn_sure:
			if(getString(R.string.submit_order_string).equals(btn_sure.getText().toString().trim())) {
				doSubmitOrder();
			} else if(getString(R.string.ensure_pay_string).equals(btn_sure.getText().toString().trim())) {
				if(zfbRb.isChecked()) {
					zfbPay(currentId);
				} else {
					
				}
			}
			break;
		default:
			break;
		}
	}
}