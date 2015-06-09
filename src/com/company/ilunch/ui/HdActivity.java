package com.company.ilunch.ui;

import java.util.ArrayList;
import org.json.JSONException;
import org.json.JSONObject;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import com.company.ilunch.R;
import com.company.ilunch.adapter.HdListAdapter;
import com.company.ilunch.base.BaseActivity;
import com.company.ilunch.bean.AddInteractionBean;
import com.company.ilunch.bean.GetInteractionListBean;
import com.company.ilunch.net.HttpUrlManager;
import com.company.ilunch.net.RequestListener;
import com.company.ilunch.preferences.LoginPreference;
import com.company.ilunch.task.AddInteractionTask;
import com.company.ilunch.task.GetInteractionListTask;
import com.company.ilunch.utils.LogUtil;
import com.company.ilunch.widget.UpRefreshListView;

/*
 * 互动界面
 */
public class HdActivity extends BaseActivity implements OnClickListener {
	public final static String TAG = "com.company.ilunch";

	public final static int MSG_ADD_INTERACTION_SUCCESS = 0x01;// 提交互动成功
	public final static int MSG_ADD_INTERACTION_FAIL = 0x02;// 提交互动失败
	public final static int MSG_GET_INTERACTION_LIST_SUCCESS = 0x03;// 获取互动列表成功
	public final static int MSG_GET_INTERACTION_LIST_FAIL = 0x04;// 获取互动列表失败
	
	private ImageView backIv;// 返回
	private UpRefreshListView hdListView;
	private EditText contentEt;
	private Button commitBt;
	
	private HdListAdapter hdListAdapter;
	private ArrayList<GetInteractionListBean.Body> dataList;

	private LoginPreference loginPreference;// 登陆preference

	@Override
	protected void initData() {
		loginPreference = new LoginPreference(this);
		dataList = new ArrayList<GetInteractionListBean.Body>();
	}

	@Override
	protected void initView() {
		setContentView(R.layout.hd_layout);

		backIv = (ImageView) this.findViewById(R.id.backIv);
		hdListView = (UpRefreshListView) this.findViewById(R.id.hdListView);
		contentEt = (EditText) this.findViewById(R.id.contentEt);
		commitBt = (Button) this.findViewById(R.id.commitBt);
	}

	@Override
	protected void setAttribute() {
		backIv.setOnClickListener(this);
		commitBt.setOnClickListener(this);
		
		hdListAdapter = new HdListAdapter(this, dataList);
		hdListView.setAdapter(hdListAdapter);
		
		doGetInteractionList();
	}

	/**
	 * 向服务器请求提交互动 <br/>
	 * 
	 */
	private void doAddInteraction() {
		if(TextUtils.isEmpty(contentEt.getText().toString().trim())) {
			Toast.makeText(this, R.string.content_not_null, Toast.LENGTH_SHORT).show();
			return;
		}
		
		AddInteractionTask task = new AddInteractionTask();

		JSONObject requestParams = new JSONObject();
		try {
			requestParams.put("UserId", Integer.parseInt(loginPreference.getDataID()));
			requestParams.put("UserName", loginPreference.getUserName());
			requestParams.put("Content", contentEt.getText().toString().trim());
		} catch (JSONException e) {
			e.printStackTrace();
		}

		task.request(this, HttpUrlManager.ADD_INTERACTION_STRING, requestParams,
				addInteractionListener);
	}

	/**
	 * 提交互动接口监听类
	 */
	private RequestListener<AddInteractionBean> addInteractionListener = new RequestListener<AddInteractionBean>() {

		@Override
		public void OnStart() {
			LogUtil.d(TAG, "开始请求OnStart-----------");
		}

		@Override
		public void onError(Exception e) {
			LogUtil.d(TAG, "onError-----------" + e.getMessage());
		}

		@Override
		public void OnPaserComplete(AddInteractionBean bean) {
			if (bean != null) {
				LogUtil.d(TAG, "OnPaserComplete:" + bean.getHead());
				if ("00".equals(bean.getHead().getResultCode())) {
					mHandler.obtainMessage(MSG_ADD_INTERACTION_SUCCESS, bean)
							.sendToTarget();
				} else {
					mHandler.obtainMessage(MSG_ADD_INTERACTION_FAIL,
							bean.getHead().getResultInfo()).sendToTarget();
				}
			} else {
				mHandler.obtainMessage(MSG_ADD_INTERACTION_FAIL,
						getString(R.string.add_interaction_fail)).sendToTarget();
			}
		}
	};
	
	/**
	 * 向服务器请求互动列表 <br/>
	 * 
	 */
	private void doGetInteractionList() {
		if(!loginPreference.getLoginState()) {
			return;
		}
		
		GetInteractionListTask task = new GetInteractionListTask();

		JSONObject requestParams = new JSONObject();
		try {
			requestParams.put("UserId", Integer.parseInt(loginPreference.getDataID()));
			requestParams.put("PageIndex", 1000);
			requestParams.put("PageSize", 1);
		} catch (JSONException e) {
			e.printStackTrace();
		}

		task.request(this, HttpUrlManager.GET_INTERACTION_LIST_STRING, requestParams,
				getInteractionListListener);
	}

	/**
	 * 互动列表接口监听类
	 */
	private RequestListener<GetInteractionListBean> getInteractionListListener = new RequestListener<GetInteractionListBean>() {

		@Override
		public void OnStart() {
			LogUtil.d(TAG, "开始请求OnStart-----------");
		}

		@Override
		public void onError(Exception e) {
			LogUtil.d(TAG, "onError-----------" + e.getMessage());
		}

		@Override
		public void OnPaserComplete(GetInteractionListBean bean) {
			if (bean != null) {
				LogUtil.d(TAG, "OnPaserComplete:" + bean.getHead());
				if ("00".equals(bean.getHead().getResultCode())) {
					mHandler.obtainMessage(MSG_GET_INTERACTION_LIST_SUCCESS, bean)
							.sendToTarget();
				} else {
					mHandler.obtainMessage(MSG_GET_INTERACTION_LIST_FAIL,
							bean.getHead().getResultInfo()).sendToTarget();
				}
			} else {
				mHandler.obtainMessage(MSG_GET_INTERACTION_LIST_FAIL,
						getString(R.string.get_interaction_fail)).sendToTarget();
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
			case MSG_ADD_INTERACTION_SUCCESS:// 提交互动成功
				Toast.makeText(HdActivity.this, R.string.add_interaction_success, Toast.LENGTH_SHORT).show();
				doGetInteractionList();
				break;
			case MSG_ADD_INTERACTION_FAIL:// 提交互动失败
				Toast.makeText(HdActivity.this, (String)object, Toast.LENGTH_SHORT).show();
				break;
			case MSG_GET_INTERACTION_LIST_SUCCESS://获取互动列表成功
				GetInteractionListBean gilBean = (GetInteractionListBean) object;
				if(gilBean != null && gilBean.getBody().size() > 0) {
					dataList.clear();
					dataList.addAll(gilBean.getBody());
				}
				hdListAdapter.notifyDataSetChanged();
				break;
			case MSG_GET_INTERACTION_LIST_FAIL://获取互动列表失败
				Toast.makeText(HdActivity.this, (String)object, Toast.LENGTH_SHORT).show();
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
		case R.id.commitBt:
			// 判断是否登录
			if (loginPreference.getLoginState()) {
				doAddInteraction();
			} else {
				startActivity(new Intent(this, LoginActivity.class));
			}
			break;
		default:
			break;
		}
	}
}
