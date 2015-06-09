package com.company.ilunch.ui;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.company.ilunch.R;
import com.company.ilunch.base.BaseActivity;
import com.company.ilunch.bean.AddMyAddressBean;
import com.company.ilunch.bean.UpdateAddressBean;
import com.company.ilunch.net.HttpUrlManager;
import com.company.ilunch.net.RequestListener;
import com.company.ilunch.preferences.IlunchPreference;
import com.company.ilunch.preferences.LoginPreference;
import com.company.ilunch.task.AddMyAddressTask;
import com.company.ilunch.task.UpdateAddressTask;
import com.company.ilunch.utils.AndroidUtils;
import com.company.ilunch.utils.LogUtil;

/*
 * 添加地址
 */
public class AddAddressActivity extends BaseActivity implements
OnClickListener, OnCheckedChangeListener {
	public final static String TAG = "com.company.ilunch";
	
	public final static int MSG_ADD_MY_ADDRESS_SUCCESS = 0x01;
	public final static int MSG_ADD_MY_ADDRESS_FAILED = 0x02;
	public final static int MSG_UPDATE_MY_ADDRESS_SUCCESS = 0x03;
	public final static int MSG_UPDATE_MY_ADDRESS_FAILED = 0x04;
	
	private RadioButton rb1;
	private RadioButton rb2;
	private RelativeLayout myLocationRl;
	private EditText locationAddressEt;
	private Button btn_add_address;// 保存地址
	private EditText userNameEt;// 用户名
	private EditText phoneEt;// 电话号码
	private TextView dsTv;// 大厦
	private TextView titleTv;
	private ImageView backIv;// 返回

	private int action;
	private String receiver;//联系人
	private String mobilephone;//手机号码
	private String address;//我的位置
	private String dataId;//地址编码
	private String buildingId;//建筑物ID
	private String locationAddress;// 详细地址
	private IlunchPreference ilunchPerference;
	private LoginPreference loginPreference;// 登陆preference

	@Override
	protected void initData() {
		if (getIntent().getExtras() != null) {
			if (getIntent().getExtras().containsKey("action")) {
				action = getIntent().getExtras().getInt(
						"action");
			}
			
			if (getIntent().getExtras().containsKey("receiver")) {
				receiver = getIntent().getExtras().getString(
						"receiver");
			}
			if (getIntent().getExtras().containsKey("mobilephone")) {
				mobilephone = getIntent().getExtras().getString(
						"mobilephone");
			}
			if (getIntent().getExtras().containsKey("address")) {
				address = getIntent().getExtras().getString(
						"address");
			}
			if (getIntent().getExtras().containsKey("dataId")) {
				dataId = getIntent().getExtras().getString(
						"dataId");
			}
			if (getIntent().getExtras().containsKey("buildingId")) {
				buildingId = getIntent().getExtras().getString(
						"buildingId");
			}
			if (getIntent().getExtras().containsKey("locationAddress")) {
				locationAddress = getIntent().getExtras().getString(
						"locationAddress");
			}
		}

		ilunchPerference = new IlunchPreference(this);
		loginPreference = new LoginPreference(this);
	}

	@Override
	protected void initView() {
		setContentView(R.layout.add_address_layout);

		backIv = (ImageView) this.findViewById(R.id.backIv);
		titleTv = (TextView) findViewById(R.id.titleTv);
		rb1 = (RadioButton) findViewById(R.id.rb1);
		rb2 = (RadioButton) findViewById(R.id.rb2);
		myLocationRl = (RelativeLayout) findViewById(R.id.myLocationRl);
		locationAddressEt = (EditText) findViewById(R.id.locationAddressEt);
		btn_add_address = (Button) findViewById(R.id.btn_add_address);
		userNameEt = (EditText) findViewById(R.id.userNameEt);
		phoneEt = (EditText) findViewById(R.id.phoneEt);
		dsTv = (TextView) findViewById(R.id.dsTv);
	}

	@Override
	protected void setAttribute() {
		rb1.setChecked(true);
		backIv.setOnClickListener(this);
		rb1.setOnCheckedChangeListener(this);
		rb2.setOnCheckedChangeListener(this);
		myLocationRl.setOnClickListener(this);
		btn_add_address.setOnClickListener(this);

		if(action == 1) {
			titleTv.setText(R.string.edit_address);
		}
		
		if (!TextUtils.isEmpty(receiver)) {
			userNameEt.setText(receiver);
		}
		
		if (!TextUtils.isEmpty(mobilephone)) {
			phoneEt.setText(mobilephone);
		}
		
		if (!TextUtils.isEmpty(locationAddress)) {
			locationAddressEt.setText(locationAddress);
		}
		
		if (!TextUtils.isEmpty(address)) {
			ilunchPerference.setMyLocationDs(this, address);
		}
	}

	@Override
	protected void onResume() {
		super.onResume();

		if (!TextUtils.isEmpty(ilunchPerference.getMyLocationDs())) {
			dsTv.setText(ilunchPerference.getMyLocationCity()
					+ ilunchPerference.getMyLocationQy()
					+ ilunchPerference.getMyLocationDs());
		}
	}

	/**
	 * 向服务器请求添加地址 <br/>
	 * 
	 */
	private void doAddMyAddress() {
		if(TextUtils.isEmpty(userNameEt.getText())) {
			Toast.makeText(this, R.string.contact_not_null, Toast.LENGTH_SHORT).show();
			return;
		}
		
		if(TextUtils.isEmpty(phoneEt.getText())) {
			Toast.makeText(this, R.string.phone_not_null, Toast.LENGTH_SHORT).show();
			return;
		}
		
		if(!AndroidUtils.isMobileNO(phoneEt.getText().toString())) {
			Toast.makeText(this, R.string.phonenum_error_string, Toast.LENGTH_SHORT).show();
			return;
		}
		
		if(TextUtils.isEmpty(dsTv.getText())) {
			Toast.makeText(this, R.string.my_address_not_null, Toast.LENGTH_SHORT).show();
			return;
		}
		
		if(TextUtils.isEmpty(locationAddressEt.getText())) {
			Toast.makeText(this, R.string.address_details_not_null, Toast.LENGTH_SHORT).show();
			return;
		}
		
		AddMyAddressTask task = new AddMyAddressTask();

		JSONObject requestParams = new JSONObject();
		try {
			requestParams.put("UserId", Integer.parseInt(loginPreference.getDataID()));
			requestParams.put("Receiver", userNameEt.getText().toString().trim());
			requestParams.put("Address", locationAddressEt.getText().toString().trim());
			requestParams.put("BuildingID", Integer.parseInt(ilunchPerference.getMyLocationDsid()));
			requestParams.put("Mobilephone", phoneEt.getText().toString().trim());
		} catch (JSONException e) {
			e.printStackTrace();
		}

		task.request(this,
				HttpUrlManager.ADD_ADD_MY_ADDRESS_STRING, requestParams,
				addMyAddressListener);
	}
	
	/**
	 * 添加地址接口监听类
	 */
	private RequestListener<AddMyAddressBean> addMyAddressListener = new RequestListener<AddMyAddressBean>() {

		@Override
		public void OnStart() {
			LogUtil.d(TAG, "开始请求OnStart-----------");
		}

		@Override
		public void onError(Exception e) {
			LogUtil.d(TAG, "onError-----------" + e.getMessage());
		}

		@Override
		public void OnPaserComplete(AddMyAddressBean bean) {
			if (bean != null) {
				LogUtil.d(TAG, "OnPaserComplete:" + bean.getHead());
				if ("00".equals(bean.getHead().getResultCode())) {
					mHandler.obtainMessage(MSG_ADD_MY_ADDRESS_SUCCESS, bean)
					.sendToTarget();
				} else {
					mHandler.obtainMessage(MSG_ADD_MY_ADDRESS_FAILED,
							bean.getHead().getResultInfo()).sendToTarget();
				}
			} else {
				mHandler.obtainMessage(MSG_ADD_MY_ADDRESS_FAILED,
						getString(R.string.add_my_address_fail)).sendToTarget();
			}
		}
	};
	
	/**
	 * 向服务器请求修改地址 <br/>
	 * 
	 */
	private void doUpdateAddress() {
		if(TextUtils.isEmpty(userNameEt.getText())) {
			Toast.makeText(this, R.string.contact_not_null, Toast.LENGTH_SHORT).show();
			return;
		}
		
		if(TextUtils.isEmpty(phoneEt.getText())) {
			Toast.makeText(this, R.string.phone_not_null, Toast.LENGTH_SHORT).show();
			return;
		}
		
		if(!AndroidUtils.isMobileNO(phoneEt.getText().toString())) {
			Toast.makeText(this, R.string.phonenum_error_string, Toast.LENGTH_SHORT).show();
			return;
		}
		
		if(TextUtils.isEmpty(dsTv.getText())) {
			Toast.makeText(this, R.string.my_address_not_null, Toast.LENGTH_SHORT).show();
			return;
		}
		
		if(TextUtils.isEmpty(locationAddressEt.getText())) {
			Toast.makeText(this, R.string.address_details_not_null, Toast.LENGTH_SHORT).show();
			return;
		}
		
		UpdateAddressTask task = new UpdateAddressTask();

		JSONObject requestParams = new JSONObject();
		try {
			if(!TextUtils.isEmpty(loginPreference.getDataID())) {
				requestParams.put("UserId", Integer.parseInt(loginPreference.getDataID()));
			}
			requestParams.put("Receiver", userNameEt.getText().toString().trim());
			requestParams.put("Address", locationAddressEt.getText().toString().trim());
			if(!TextUtils.isEmpty(ilunchPerference.getMyLocationDsid())) {
				requestParams.put("BuildingID", Integer.parseInt(ilunchPerference.getMyLocationDsid()));
			}
			requestParams.put("Mobilephone", phoneEt.getText().toString().trim());
			requestParams.put("DataId", dataId);
		} catch (JSONException e) {
			e.printStackTrace();
		}

		task.request(this,
				HttpUrlManager.UPDATE_MY_ADDRESS_STRING, requestParams,
				updateAddressListener);
	}
	
	/**
	 * 添加地址接口监听类
	 */
	private RequestListener<UpdateAddressBean> updateAddressListener = new RequestListener<UpdateAddressBean>() {

		@Override
		public void OnStart() {
			LogUtil.d(TAG, "开始请求OnStart-----------");
		}

		@Override
		public void onError(Exception e) {
			LogUtil.d(TAG, "onError-----------" + e.getMessage());
		}

		@Override
		public void OnPaserComplete(UpdateAddressBean bean) {
			if (bean != null) {
				LogUtil.d(TAG, "OnPaserComplete:" + bean.getHead());
				if ("00".equals(bean.getHead().getResultCode())) {
					mHandler.obtainMessage(MSG_UPDATE_MY_ADDRESS_SUCCESS, bean)
					.sendToTarget();
				} else {
					mHandler.obtainMessage(MSG_UPDATE_MY_ADDRESS_FAILED,
							bean.getHead().getResultInfo()).sendToTarget();
				}
			} else {
				mHandler.obtainMessage(MSG_UPDATE_MY_ADDRESS_FAILED,
						getString(R.string.update_address_fail)).sendToTarget();
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
			case MSG_ADD_MY_ADDRESS_SUCCESS:
				Toast.makeText(AddAddressActivity.this, R.string.add_my_address_success, Toast.LENGTH_SHORT).show();
				AddAddressActivity.this.finish();
				break;
			case MSG_ADD_MY_ADDRESS_FAILED:
				Toast.makeText(AddAddressActivity.this, (String)object, Toast.LENGTH_SHORT).show();
				break;
			case MSG_UPDATE_MY_ADDRESS_SUCCESS:
				Toast.makeText(AddAddressActivity.this, R.string.update_address_success, Toast.LENGTH_SHORT).show();
				AddAddressActivity.this.finish();
				break;
			case MSG_UPDATE_MY_ADDRESS_FAILED:
				Toast.makeText(AddAddressActivity.this, (String)object, Toast.LENGTH_SHORT).show();
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
		case R.id.myLocationRl:
			startActivity(new Intent(AddAddressActivity.this,
					MyLocationActivity.class));
			break;
		case R.id.btn_add_address:
			if(action == 1) {
				doUpdateAddress();
			} else {
				doAddMyAddress();
			}
			break;
		default:
			break;
		}
	}

	@Override
	public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
		if (arg1) {
			switch (arg0.getId()) {
			case R.id.rb1:
				rb1.setChecked(true);
				rb2.setChecked(false);
				break;
			case R.id.rb2:
				rb1.setChecked(false);
				rb2.setChecked(true);
				break;
			}
		}
	}
}
