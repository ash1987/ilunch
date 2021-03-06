package com.company.ilunch.ui;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.company.ilunch.IlunchApplication;
import com.company.ilunch.R;
import com.company.ilunch.adapter.BookingListAdapter;
import com.company.ilunch.adapter.BookingListAdapter.Callback;
import com.company.ilunch.base.BaseActivity;
import com.company.ilunch.bean.AddMyCollectBean;
import com.company.ilunch.bean.AddToCartBean;
import com.company.ilunch.bean.DeleMyCollectBean;
import com.company.ilunch.bean.GetFoodListBean;
import com.company.ilunch.bean.GetFoodListBean.Body;
import com.company.ilunch.bean.UpdateCartBean;
import com.company.ilunch.fragment.BookingFragment;
import com.company.ilunch.net.HttpUrlManager;
import com.company.ilunch.net.RequestListener;
import com.company.ilunch.preferences.LoginPreference;
import com.company.ilunch.task.AddMyCollectTask;
import com.company.ilunch.task.AddToCartTask;
import com.company.ilunch.task.DeleMyCollectTask;
import com.company.ilunch.task.GetFoodListTask;
import com.company.ilunch.task.UpdateCartTask;
import com.company.ilunch.utils.LogUtil;
import com.company.ilunch.widget.CustomToast;
import com.company.ilunch.widget.UpRefreshListView;

public class FoodListByTogoActivity extends BaseActivity implements
OnClickListener {
	public final static String TAG = "com.company.ilunch";

	private final static int MSG_GET_FOODLIST_BY_TOGO_SUCCESS = 0x01;// 　获取商家菜品列表成功
	private final static int MSG_GET_FOODLIST_BY_TOGO_FAIL = 0x02;// 获取商家菜品列表失敗
	private final static int MSG_ADD_TO_CART_SUCCESS = 0x03;// 　加入购物车成功
	private final static int MSG_ADD_TO_CART_FAIL = 0x04;// 加入购物车失敗
	private final static int MSG_UPDATE_CART_SUCCESS = 0x05;// 　更新购物车成功
	private final static int MSG_UPDATE_CART_FAIL = 0x06;// 更新购物车失敗
	private final static int MSG_ADD_COLLECT_SUCCESS = 0x07;// 　添加收藏成功
	private final static int MSG_ADD_COLLECT_FAIL = 0x08;// 添加收藏失敗
	private final static int MSG_DEL_COLLECT_SUCCESS = 0x09;// 　删除收藏成功
	private final static int MSG_DEL_COLLECT_FAIL = 0x0a;// 删除收藏失敗

	private BookingListAdapter bookingListAdapter;
	private ArrayList<GetFoodListBean.Body> foodList;
	private LoginPreference loginPreference;

	private String togoId;// 商户ID
	private String fpName;// 商户名称

	private ImageView backIv;// 返回
	private TextView titleTv;
	private UpRefreshListView goodsLv;

	@Override
	protected void initData() {
		if (getIntent().getExtras() != null) {
			Bundle bundle = getIntent().getExtras();
			if (bundle.containsKey("master")) {
				togoId = bundle.getString("master");
				fpName = bundle.getString("fpName");
			}
		}

		foodList = new ArrayList<GetFoodListBean.Body>();
	}

	//	@Override
	//	public boolean onKeyDown(int keyCode, KeyEvent event) {
	//		if (keyCode == KeyEvent.KEYCODE_BACK) {
	//			this.finish();
	//			overridePendingTransition(R.anim.popup_enter, R.anim.popup_exit);
	//			return true;
	//		}
	//
	//		return super.onKeyDown(keyCode, event);
	//	}
	//
	//	@Override  
	//	public boolean onTouchEvent(MotionEvent event) {  
	//		if (event.getAction() == MotionEvent.ACTION_DOWN && isOutOfBounds(this, event)) {  
	//			this.finish();
	//			overridePendingTransition(R.anim.popup_enter, R.anim.popup_exit);  
	//		}  
	//		return super.onTouchEvent(event);  
	//	}  
	//
	//	private boolean isOutOfBounds(Activity context, MotionEvent event) {  
	//		final int x = (int) event.getX();  
	//		final int y = (int) event.getY();  
	//		final int slop = ViewConfiguration.get(context).getScaledWindowTouchSlop();  
	//		final View decorView = context.getWindow().getDecorView();  
	//		return (x < -slop) || (y < -slop)|| (x > (decorView.getWidth() + slop))|| (y > (decorView.getHeight() + slop));  
	//	}

	@Override
	protected void initView() {
		setContentView(R.layout.foodlistbytogo);

		backIv = (ImageView) findViewById(R.id.backIv);
		titleTv = (TextView) this.findViewById(R.id.titleTv);
		goodsLv = (UpRefreshListView) findViewById(R.id.goodsListView);
	}

	@Override
	protected void setAttribute() {
		titleTv.setText(fpName);
		backIv.setOnClickListener(this);
		//		get_root_view(FoodListByTogoActivity.this).setVisibility(View.GONE);
		loginPreference = new LoginPreference(this);

		doGetFoodListByTogo();

		bookingListAdapter = new BookingListAdapter(this, foodList, null,
				new Callback() {

			@Override
			public void addToCart(
					com.company.ilunch.bean.GetFoodListBean.Body body) {
				doAddToCart(body);
			}

			@Override
			public void updateCart(
					com.company.ilunch.bean.GetFoodListBean.Body body,
					int num) {
				doUpdateCart(body, num);
			}

			@Override
			public void addFav(
					com.company.ilunch.bean.GetFoodListBean.Body body) {
				if (!loginPreference.getLoginState()) {
					Toast.makeText(FoodListByTogoActivity.this,
							R.string.please_login, Toast.LENGTH_SHORT)
							.show();
					return;
				}

				doAddMyCollect(body);
			}

			@Override
			public void deleCart(
					com.company.ilunch.bean.GetFoodListBean.Body body) {
				Intent intent3 = new Intent(
						FoodListBaseActivity.DEL_CART_ACTION_NAME);
				intent3.putExtra("pid", body.getUnid());

				// 发送广播
				FoodListByTogoActivity.this.sendBroadcast(intent3);
			}

			@Override
			public void addComment(
					com.company.ilunch.bean.GetFoodListBean.Body body) {
				if (!loginPreference.getLoginState()) {
					Toast.makeText(FoodListByTogoActivity.this,
							R.string.please_login, Toast.LENGTH_SHORT)
							.show();
					return;
				}

				Intent cfIntent = new Intent(
						FoodListByTogoActivity.this,
						CommentFoodActivity.class);
				cfIntent.putExtra("TogoId", body.getMaster());
				cfIntent.putExtra("foodName", body.getFoodName());
				cfIntent.putExtra("picture", body.getPicture());
				startActivity(cfIntent);
			}

			@Override
			public void ShowFoodList(
					com.company.ilunch.bean.GetFoodListBean.Body body) {
			}

			@Override
			public void delFav(Body body) {
				if (!loginPreference.getLoginState()) {
					Toast.makeText(FoodListByTogoActivity.this,
							R.string.please_login, Toast.LENGTH_SHORT)
							.show();
					return;
				}

				doDelMyCollect(body);
			}
		});
		goodsLv.setAdapter(bookingListAdapter);
	}

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

	/**
	 * 向服务器请求删除收藏 <br/>
	 * 
	 */
	private void doDelMyCollect(GetFoodListBean.Body body) {
		DeleMyCollectTask task = new DeleMyCollectTask();

		JSONObject requestParams = new JSONObject();
		try {
			if (loginPreference.getLoginState()) {
				requestParams.put("UserId",
						Integer.parseInt(loginPreference.getDataID()));
			} else {
				requestParams.put("UserId", "");
			}
			requestParams.put("FoodId", Integer.parseInt(body.getUnid()));
		} catch (JSONException e) {
			e.printStackTrace();
		}

		task.request(FoodListByTogoActivity.this, HttpUrlManager.DEL_MY_COLLECT_STRING, requestParams,
				delMyCollectListener);
	}

	/**
	 * 删除收藏接口监听类
	 */
	private RequestListener<DeleMyCollectBean> delMyCollectListener = new RequestListener<DeleMyCollectBean>() {

		@Override
		public void OnStart() {
			LogUtil.d(TAG, "开始请求OnStart-----------");
		}

		@Override
		public void onError(Exception e) {
			LogUtil.d(TAG, "onError-----------" + e.getMessage());
		}

		@Override
		public void OnPaserComplete(DeleMyCollectBean bean) {
			if (bean != null) {
				LogUtil.d(TAG, "OnPaserComplete:" + bean.getHead());
				if ("00".equals(bean.getHead().getResultCode())) {
					mHandler.obtainMessage(MSG_DEL_COLLECT_SUCCESS, bean)
					.sendToTarget();
				} else {
					mHandler.obtainMessage(MSG_DEL_COLLECT_FAIL,
							bean.getHead().getResultInfo()).sendToTarget();
				}
			} else {
				mHandler.obtainMessage(MSG_DEL_COLLECT_FAIL,
						getString(R.string.del_collect_failed_string))
						.sendToTarget();
			}
		}
	};

	/**
	 * 向服务器请求更新购物车 <br/>
	 * 
	 */
	private void doUpdateCart(GetFoodListBean.Body body, int num) {
		UpdateCartTask task = new UpdateCartTask();

		JSONObject requestParams = new JSONObject();
		try {
			requestParams.put("UCode", IlunchApplication.cartUcode);
			if (!TextUtils.isEmpty(body.getUnid())) {
				requestParams.put("UnId", Integer.parseInt(body.getUnid()));
			}
			requestParams.put("PNum", num);
		} catch (JSONException e) {
			e.printStackTrace();
		}

		task.request(this, HttpUrlManager.UPDATE_CART_STRING, requestParams,
				updateCartListener);
	}

	/**
	 * 获取菜品分类接口监听类
	 */
	private RequestListener<UpdateCartBean> updateCartListener = new RequestListener<UpdateCartBean>() {

		@Override
		public void OnStart() {
			LogUtil.d(TAG, "开始请求OnStart-----------");
		}

		@Override
		public void onError(Exception e) {
			LogUtil.d(TAG, "onError-----------" + e.getMessage());
		}

		@Override
		public void OnPaserComplete(UpdateCartBean bean) {
			if (bean != null) {
				LogUtil.d(TAG, "OnPaserComplete:" + bean.getHead());
				if ("00".equals(bean.getHead().getResultCode())) {
					mHandler.obtainMessage(MSG_UPDATE_CART_SUCCESS, bean)
					.sendToTarget();
				} else {
					mHandler.obtainMessage(MSG_UPDATE_CART_FAIL,
							bean.getHead().getResultInfo()).sendToTarget();
				}
			} else {
				mHandler.obtainMessage(MSG_UPDATE_CART_FAIL,
						getString(R.string.update_cart_failed_string))
						.sendToTarget();
			}
		}
	};

	/**
	 * 向服务器请求加入购物车 <br/>
	 * 
	 */
	private void doAddToCart(GetFoodListBean.Body body) {
		AddToCartTask task = new AddToCartTask();

		JSONObject requestParams = new JSONObject();
		try {
			requestParams.put("UCode", IlunchApplication.cartUcode);
			if (TextUtils.isEmpty(loginPreference.getDataID())) {
				requestParams.put("UserId", -1);
			} else {
				requestParams.put("UserId",
						Integer.parseInt(loginPreference.getDataID()));
			}
			if (!TextUtils.isEmpty(body.getMaster())) {
				requestParams.put("TogoId", Integer.parseInt(body.getMaster()));
			}
			requestParams.put("TogoName", body.getName());
			if (!TextUtils.isEmpty(body.getUnid())) {
				requestParams.put("UnId", Integer.parseInt(body.getUnid()));
			}
			requestParams.put("PName", body.getFoodName());
			requestParams.put("PPrice", body.getPrice());
			requestParams.put("PNum", 1);
			requestParams.put("TullPrice", body.getFullPrice());
		} catch (JSONException e) {
			e.printStackTrace();
		}

		task.request(this, HttpUrlManager.ADD_TO_CART_STRING, requestParams,
				addToCartListener);
	}

	/**
	 * 获取菜品分类接口监听类
	 */
	private RequestListener<AddToCartBean> addToCartListener = new RequestListener<AddToCartBean>() {

		@Override
		public void OnStart() {
			LogUtil.d(TAG, "开始请求OnStart-----------");
		}

		@Override
		public void onError(Exception e) {
			LogUtil.d(TAG, "onError-----------" + e.getMessage());
		}

		@Override
		public void OnPaserComplete(AddToCartBean bean) {
			if (bean != null) {
				LogUtil.d(TAG, "OnPaserComplete:" + bean.getHead());
				if ("00".equals(bean.getHead().getResultCode())) {
					mHandler.obtainMessage(MSG_ADD_TO_CART_SUCCESS, bean)
					.sendToTarget();
				} else {
					mHandler.obtainMessage(MSG_ADD_TO_CART_FAIL,
							bean.getHead().getResultInfo()).sendToTarget();
				}
			} else {
				mHandler.obtainMessage(MSG_ADD_TO_CART_FAIL,
						getString(R.string.add_cart_failed_string))
						.sendToTarget();
			}
		}
	};

	/**
	 * 向服务器请求添加收藏 <br/>
	 * 
	 */
	private void doAddMyCollect(GetFoodListBean.Body body) {
		AddMyCollectTask task = new AddMyCollectTask();

		JSONObject requestParams = new JSONObject();
		try {
			if (loginPreference.getLoginState()) {
				requestParams.put("UserId",
						Integer.parseInt(loginPreference.getDataID()));
			} else {
				requestParams.put("UserId", "");
			}
			requestParams.put("FoodId", Integer.parseInt(body.getUnid()));
			requestParams.put("Togoid", Integer.parseInt(body.getMaster()));
		} catch (JSONException e) {
			e.printStackTrace();
		}

		task.request(this, HttpUrlManager.ADD_MY_COLLECT_STRING, requestParams,
				addMyCollectListener);
	}

	/**
	 * 添加收藏分类接口监听类
	 */
	private RequestListener<AddMyCollectBean> addMyCollectListener = new RequestListener<AddMyCollectBean>() {

		@Override
		public void OnStart() {
			LogUtil.d(TAG, "开始请求OnStart-----------");
		}

		@Override
		public void onError(Exception e) {
			LogUtil.d(TAG, "onError-----------" + e.getMessage());
		}

		@Override
		public void OnPaserComplete(AddMyCollectBean bean) {
			if (bean != null) {
				LogUtil.d(TAG, "OnPaserComplete:" + bean.getHead());
				if ("00".equals(bean.getHead().getResultCode())) {
					mHandler.obtainMessage(MSG_ADD_COLLECT_SUCCESS, bean)
					.sendToTarget();
				} else {
					mHandler.obtainMessage(MSG_ADD_COLLECT_FAIL,
							bean.getHead().getResultInfo()).sendToTarget();
				}
			} else {
				mHandler.obtainMessage(MSG_ADD_COLLECT_FAIL,
						getString(R.string.add_collect_failed_string))
						.sendToTarget();
			}
		}
	};

	/**
	 * 向服务器请求获取商家菜品列表 <br/>
	 * 
	 */
	private void doGetFoodListByTogo() {
		showProgress("", "获取商家菜品中...");

		GetFoodListTask task = new GetFoodListTask();

		JSONObject requestParams = new JSONObject();
		try {
			requestParams.put("PageSize", 100);
			requestParams.put("PageIndex", 1);
			requestParams.put("TogoId", togoId);
			if (loginPreference.getLoginState()) {
				requestParams.put("UserId",
						Integer.parseInt(loginPreference.getDataID()));
			} else {
				requestParams.put("UserId", "");
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}

		task.request(this, HttpUrlManager.GET_FOODLIST_BY_TOGO_STRING,
				requestParams, getFoodListByTogoListener);
	}

	/**
	 * 获取菜品分类接口监听类
	 */
	private RequestListener<GetFoodListBean> getFoodListByTogoListener = new RequestListener<GetFoodListBean>() {

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
		public void OnPaserComplete(GetFoodListBean bean) {
			dismissProgress();

			if (bean != null) {
				LogUtil.d(TAG, "OnPaserComplete:" + bean.getHead());
				if ("00".equals(bean.getHead().getResultCode())) {
					mHandler.obtainMessage(MSG_GET_FOODLIST_BY_TOGO_SUCCESS,
							bean).sendToTarget();
				} else {
					mHandler.obtainMessage(MSG_GET_FOODLIST_BY_TOGO_FAIL,
							bean.getHead().getResultInfo()).sendToTarget();
				}
			} else {
				mHandler.obtainMessage(MSG_GET_FOODLIST_BY_TOGO_FAIL,
						getString(R.string.get_foodlist_by_togo_failed_string))
						.sendToTarget();
			}
		}
	};

	private View get_root_view(Activity context) {    
		return ((ViewGroup)context.findViewById(android.R.id.content)).getChildAt(0);    
	} 

	/**
	 * handler用于处理网络请求的返回数据
	 */
	private Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			Object object = msg.obj;
			switch (msg.what) {
			case MSG_GET_FOODLIST_BY_TOGO_SUCCESS:
				GetFoodListBean gflBean = (GetFoodListBean) object;

				if (gflBean != null && gflBean.getBody() != null
						&& gflBean.getBody().size() != 0) {
					foodList.addAll(gflBean.getBody());
				}

				bookingListAdapter.notifyDataSetChanged();

				//				get_root_view(FoodListByTogoActivity.this).setVisibility(View.VISIBLE);
				break;
			case MSG_GET_FOODLIST_BY_TOGO_FAIL:
				Toast.makeText(FoodListByTogoActivity.this,
						getString(R.string.get_foodlist_by_togo_fail),
						Toast.LENGTH_SHORT).show();
				finish();
				break;
			case MSG_ADD_TO_CART_SUCCESS:
				AddToCartBean atcBean = (AddToCartBean) object;

				Intent intent1 = new Intent(
						FoodListBaseActivity.UPDATE_CART_ACTION_NAME);
				// 发送广播
				FoodListByTogoActivity.this.sendBroadcast(intent1);
				break;
			case MSG_ADD_TO_CART_FAIL:
				Toast.makeText(FoodListByTogoActivity.this, (String) object,
						Toast.LENGTH_SHORT).show();
				break;
			case MSG_UPDATE_CART_SUCCESS:
				UpdateCartBean ucBean = (UpdateCartBean) object;

				Intent intent2 = new Intent(
						FoodListBaseActivity.UPDATE_CART_ACTION_NAME);

				// 发送广播
				FoodListByTogoActivity.this.sendBroadcast(intent2);
				break;
			case MSG_UPDATE_CART_FAIL:
				Toast.makeText(FoodListByTogoActivity.this, (String) object,
						Toast.LENGTH_SHORT).show();
				break;
			case MSG_ADD_COLLECT_SUCCESS:
				Toast.makeText(FoodListByTogoActivity.this,
						R.string.add_collect_success_string, Toast.LENGTH_SHORT)
						.show();

				Intent intent3 = new Intent(
						BookingFragment.UPDATE_LIST_ACTION_NAME);

				// 发送广播
				sendBroadcast(intent3);

				if(foodList != null) {
					foodList.clear();
				}

				doGetFoodListByTogo();
				break;
			case MSG_ADD_COLLECT_FAIL:
				Toast.makeText(FoodListByTogoActivity.this, (String) object,
						Toast.LENGTH_SHORT).show();
				break;
			case MSG_DEL_COLLECT_SUCCESS:// 删除成功
				Toast.makeText(FoodListByTogoActivity.this,
						R.string.del_collect_success_string, Toast.LENGTH_SHORT)
						.show();

				Intent intent4 = new Intent(
						BookingFragment.UPDATE_LIST_ACTION_NAME);

				// 发送广播
				sendBroadcast(intent4);

				if(foodList != null) {
					foodList.clear();
				}

				doGetFoodListByTogo();
				break;
			case MSG_DEL_COLLECT_FAIL:
				CustomToast.getToast(FoodListByTogoActivity.this,
						String.valueOf(object), Toast.LENGTH_SHORT).show();
				break;		
			default:
				break;
			}
		}
	};
}