package com.company.ilunch.fragment;

import java.util.ArrayList;
import org.json.JSONException;
import org.json.JSONObject;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;
import com.company.ilunch.IlunchApplication;
import com.company.ilunch.R;
import com.company.ilunch.adapter.BookingListAdapter;
import com.company.ilunch.adapter.BookingListAdapter.Callback;
import com.company.ilunch.bean.AddMyCollectBean;
import com.company.ilunch.bean.AddToCartBean;
import com.company.ilunch.bean.GetCartListBean;
import com.company.ilunch.bean.GetFoodListBean;
import com.company.ilunch.bean.GetShopDataBean.Body;
import com.company.ilunch.bean.UpdateCartBean;
import com.company.ilunch.net.HttpUrlManager;
import com.company.ilunch.net.RequestListener;
import com.company.ilunch.preferences.IlunchPreference;
import com.company.ilunch.preferences.LoginPreference;
import com.company.ilunch.task.AddMyCollectTask;
import com.company.ilunch.task.AddToCartTask;
import com.company.ilunch.task.GetCartListTask;
import com.company.ilunch.task.GetFoodListTask;
import com.company.ilunch.task.UpdateCartTask;
import com.company.ilunch.ui.FoodListBaseActivity;
import com.company.ilunch.utils.LogUtil;
import com.company.ilunch.widget.UpRefreshListView;
import com.company.ilunch.widget.UpRefreshListView.OnRefreshListener;
import com.company.ilunch.widget.UpRefreshListView.onMoreListener;

/**
 * 订座/打包fragment
 */
public class BookingFragment extends BaseFragment {
	public static final String FRAGMENT_TAG = BookingFragment.class
			.getSimpleName();
	public final static String UPDATE_LIST_ACTION_NAME = "UPDATE_LIST_ACTION_NAME";
	
	private final static int MSG_GET_FOOD_SUCCESS = 0x01;// 獲取菜品列表成功
	private final static int MSG_GET_FOOD_FAIL = 0x02;// 獲取菜品列表失敗
	private final static int MSG_ADD_TO_CART_SUCCESS = 0x03;// 　加入购物车成功
	private final static int MSG_ADD_TO_CART_FAIL = 0x04;// 加入购物车失敗
	private final static int MSG_UPDATE_CART_SUCCESS = 0x05;// 　更新购物车成功
	private final static int MSG_UPDATE_CART_FAIL = 0x06;// 更新购物车失敗
	private final static int MSG_ADD_COLLECT_SUCCESS = 0x07;// 　添加收藏成功
	private final static int MSG_ADD_COLLECT_FAIL = 0x08;// 添加收藏失敗
	private final static int MSG_GET_CART_LIST_SUCCESS = 0x09;// 　获取购物车成功
	private final static int MSG_GET_CART_LIST_FAIL = 0x0a;// 获取购物车失敗

	private BookingListAdapter bookingListAdapter;
	private ArrayList<GetFoodListBean.Body> foodList;
	private ArrayList<GetCartListBean.Body> cartListData;
	private LoginPreference loginPreference;

	private UpRefreshListView goodsLv;

	private int pageNo = 1;
	private int pageSize = 8;
	private boolean isRefreshing = true;
	private int total = 0;

	private Body data;
	private int SalesMethod;// 销售方式

	public BookingFragment() {
		super();
	}

	public BookingFragment(Body body, int SalesMethod) {
		this.data = body;
		this.SalesMethod = SalesMethod;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.food_list_layout, null);

		initView(view);
		setAttribute();

		return view;
	}

	// 初始化控件
	private void initView(View view) {
		cartListData = new ArrayList<GetCartListBean.Body>();
		
		foodList = new ArrayList<GetFoodListBean.Body>();
		goodsLv = (UpRefreshListView) view.findViewById(R.id.goodsListView);
	}

	// 设置控件属性
	private void setAttribute() {
		loginPreference = new LoginPreference(
				BookingFragment.this.getActivity());
		bookingListAdapter = new BookingListAdapter(this.getActivity(),
				foodList, cartListData, new Callback() {

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
						if(!loginPreference.getLoginState()) {
							Toast.makeText(BookingFragment.this.getActivity(), R.string.please_login, Toast.LENGTH_SHORT).show();
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
						BookingFragment.this.getActivity().sendBroadcast(intent3);
					}
				});
		goodsLv.setAdapter(bookingListAdapter);

		goodsLv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View view,
					int position, long arg3) {

			}
		});

		goodsLv.setonRefreshListener(new OnRefreshListener() {

			@Override
			public void onRefresh() {
				isRefreshing = true;
				pageNo = 1;
				
				doGetCartList();
				doGetFoodList();
			}
		});
		goodsLv.setonMoreListener(new onMoreListener() {

			@Override
			public void onMore() {
//				if (total > foodList.size()) {
					pageNo++;
					goodsLv.addAutoLoadFooterView(BookingFragment.this
							.getActivity());
					
					doGetCartList();
					doGetFoodList();
//				}
			}
		});
		
		registerBoradcastReceiver();
	}
	
	@Override
	public void onDestroyView() {
		super.onDestroyView();
		
		unRegisterBoradcastReceiver();
	}
	
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

		task.request(this.getActivity(), HttpUrlManager.GET_CART_LIST_STRING, requestParams,
				getCartListListener);
	}

	/**
	 * 获取购物车接口监听类
	 */
	private RequestListener<GetCartListBean> getCartListListener = new RequestListener<GetCartListBean>() {

		@Override
		public void OnStart() {
			LogUtil.d(FRAGMENT_TAG, "开始请求OnStart-----------");
		}

		@Override
		public void onError(Exception e) {
			LogUtil.d(FRAGMENT_TAG, "onError-----------" + e.getMessage());
		}

		@Override
		public void OnPaserComplete(GetCartListBean bean) {
			if (bean != null) {
				LogUtil.d(FRAGMENT_TAG, "OnPaserComplete:" + bean.getHead());
				if ("00".equals(bean.getHead().getResultCode())) {
					mHandler.obtainMessage(MSG_GET_CART_LIST_SUCCESS, bean)
							.sendToTarget();
				} else {
					mHandler.obtainMessage(MSG_GET_CART_LIST_FAIL,
							bean.getHead().getResultInfo()).sendToTarget();
				}
			} else {
				if(isAdded()) {
					mHandler.obtainMessage(MSG_GET_CART_LIST_FAIL,
							getString(R.string.get_cart_failed_string))
							.sendToTarget();
				}
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
			requestParams.put("UserId", Integer.parseInt(loginPreference.getDataID()));
			requestParams.put("FoodId", Integer.parseInt(body.getUnid()));
			requestParams.put("Togoid", Integer.parseInt(body.getMaster()));
		} catch (JSONException e) {
			e.printStackTrace();
		}

		task.request(BookingFragment.this.getActivity(),
				HttpUrlManager.ADD_MY_COLLECT_STRING, requestParams,
				addMyCollectListener);
	}

	/**
	 * 添加收藏分类接口监听类
	 */
	private RequestListener<AddMyCollectBean> addMyCollectListener = new RequestListener<AddMyCollectBean>() {

		@Override
		public void OnStart() {
			LogUtil.d(FRAGMENT_TAG, "开始请求OnStart-----------");
		}

		@Override
		public void onError(Exception e) {
			LogUtil.d(FRAGMENT_TAG, "onError-----------" + e.getMessage());
		}

		@Override
		public void OnPaserComplete(AddMyCollectBean bean) {
			if (bean != null) {
				LogUtil.d(FRAGMENT_TAG, "OnPaserComplete:" + bean.getHead());
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

		task.request(BookingFragment.this.getActivity(),
				HttpUrlManager.UPDATE_CART_STRING, requestParams,
				updateCartListener);
	}

	/**
	 * 获取菜品分类接口监听类
	 */
	private RequestListener<UpdateCartBean> updateCartListener = new RequestListener<UpdateCartBean>() {

		@Override
		public void OnStart() {
			LogUtil.d(FRAGMENT_TAG, "开始请求OnStart-----------");
		}

		@Override
		public void onError(Exception e) {
			LogUtil.d(FRAGMENT_TAG, "onError-----------" + e.getMessage());
		}

		@Override
		public void OnPaserComplete(UpdateCartBean bean) {
			if (bean != null) {
				LogUtil.d(FRAGMENT_TAG, "OnPaserComplete:" + bean.getHead());
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

		task.request(BookingFragment.this.getActivity(),
				HttpUrlManager.ADD_TO_CART_STRING, requestParams,
				addToCartListener);
	}

	/**
	 * 获取菜品分类接口监听类
	 */
	private RequestListener<AddToCartBean> addToCartListener = new RequestListener<AddToCartBean>() {

		@Override
		public void OnStart() {
			LogUtil.d(FRAGMENT_TAG, "开始请求OnStart-----------");
		}

		@Override
		public void onError(Exception e) {
			LogUtil.d(FRAGMENT_TAG, "onError-----------" + e.getMessage());
		}

		@Override
		public void OnPaserComplete(AddToCartBean bean) {
			if (bean != null) {
				LogUtil.d(FRAGMENT_TAG, "OnPaserComplete:" + bean.getHead());
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
	 * 向服务器请求获取菜品列表 <br/>
	 * 
	 */
	private void doGetFoodList() {
		IlunchPreference ilunchPerference = new IlunchPreference(
				BookingFragment.this.getActivity());
		LoginPreference loginPreference = new LoginPreference(
				BookingFragment.this.getActivity());

		GetFoodListTask task = new GetFoodListTask();

		JSONObject requestParams = new JSONObject();
		try {
			requestParams.put("PageSize", pageSize);
			requestParams.put("PageIndex", pageNo);
			requestParams
					.put("BuId",
							TextUtils.isEmpty(ilunchPerference
									.getMyLocationDsid()) ? "1"
									: ilunchPerference.getMyLocationDsid());
			requestParams.put("KeyWords", "");
			requestParams.put("SalesMethod", SalesMethod);
			if (data == null) {
				requestParams.put("FoodType", "sc");
			} else {
				requestParams.put("FoodType", data.getClassname());
			}

			if (loginPreference.getLoginState()) {
				requestParams.put("UserId",
						Integer.parseInt(loginPreference.getDataID()));
			} else {

				requestParams.put("UserId", "");
			
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}

		task.request(BookingFragment.this.getActivity(),
				HttpUrlManager.GET_FOOD_LIST_STRING, requestParams,
				getFoodListListener);
	}

	/**
	 * 获取菜品分类接口监听类
	 */
	private RequestListener<GetFoodListBean> getFoodListListener = new RequestListener<GetFoodListBean>() {

		@Override
		public void OnStart() {
			LogUtil.d(FRAGMENT_TAG, "开始请求OnStart-----------");
		}

		@Override
		public void onError(Exception e) {
			LogUtil.d(FRAGMENT_TAG, "onError-----------" + e.getMessage());
		}

		@Override
		public void OnPaserComplete(GetFoodListBean bean) {
			if (bean != null) {
				LogUtil.d(FRAGMENT_TAG, "OnPaserComplete:" + bean.getHead());
				if ("00".equals(bean.getHead().getResultCode())) {
					mHandler.obtainMessage(MSG_GET_FOOD_SUCCESS, bean)
							.sendToTarget();
				} else {
					mHandler.obtainMessage(MSG_GET_FOOD_FAIL,
							bean.getHead().getResultInfo()).sendToTarget();
				}
			} else {
				mHandler.obtainMessage(MSG_GET_FOOD_FAIL,
						getString(R.string.get_foodlist_failed_string))
						.sendToTarget();
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
			case MSG_GET_FOOD_SUCCESS:// 获取菜品列表成功
				if (isRefreshing) {
					isRefreshing = false;
					foodList.clear();
					total = 0;
				}

				GetFoodListBean gflBean = (GetFoodListBean) object;

				if (gflBean != null && gflBean.getBody() != null
						&& gflBean.getBody().size() != 0) {
					total += gflBean.getBody().size();
					foodList.addAll(gflBean.getBody());
				}

				bookingListAdapter.notifyDataSetChanged();

				goodsLv.onRefreshComplete();
				goodsLv.hideAutoLoadFooterView();
				break;
			case MSG_GET_FOOD_FAIL:
				isRefreshing = false;

				goodsLv.onRefreshComplete();
				goodsLv.hideAutoLoadFooterView();
				break;
			case MSG_ADD_TO_CART_SUCCESS:
				AddToCartBean atcBean = (AddToCartBean) object;

				Intent intent1 = new Intent(
						FoodListBaseActivity.UPDATE_CART_ACTION_NAME);
				// 发送广播
				BookingFragment.this.getActivity().sendBroadcast(intent1);
				break;
			case MSG_ADD_TO_CART_FAIL:
				Toast.makeText(BookingFragment.this.getActivity(),
						(String) object, Toast.LENGTH_SHORT).show();
				break;
			case MSG_UPDATE_CART_SUCCESS:
				UpdateCartBean ucBean = (UpdateCartBean) object;

				Intent intent2 = new Intent(
						FoodListBaseActivity.UPDATE_CART_ACTION_NAME);

				// 发送广播
				BookingFragment.this.getActivity().sendBroadcast(intent2);
				break;
			case MSG_UPDATE_CART_FAIL:
				Toast.makeText(BookingFragment.this.getActivity(),
						(String) object, Toast.LENGTH_SHORT).show();
				break;
			case MSG_ADD_COLLECT_SUCCESS:
				Toast.makeText(BookingFragment.this.getActivity(),
						R.string.add_collect_success_string, Toast.LENGTH_SHORT).show();
				
				isRefreshing = true;
				pageNo = 1;
				
				doGetCartList();
				doGetFoodList();
				break;
			case MSG_ADD_COLLECT_FAIL:
				Toast.makeText(BookingFragment.this.getActivity(),
						(String) object, Toast.LENGTH_SHORT).show();
				break;
			case MSG_GET_CART_LIST_SUCCESS:
				GetCartListBean gclBean = (GetCartListBean) msg.obj;
				if (gclBean != null && gclBean.getBody() != null
						&& gclBean.getBody().size() != 0) {
					cartListData.clear();
					cartListData.addAll(gclBean.getBody());
				}
				
				bookingListAdapter.notifyDataSetChanged();
				break;
			case MSG_GET_CART_LIST_FAIL:
				cartListData.clear();
				
				bookingListAdapter.notifyDataSetChanged();
				break;
			default:
				break;
			}
		}
	};
	
	public void registerBoradcastReceiver() {
		IntentFilter myIntentFilter = new IntentFilter();
		myIntentFilter.addAction(UPDATE_LIST_ACTION_NAME);
		// 注册广播
		this.getActivity().registerReceiver(mBroadcastReceiver, myIntentFilter);
	}

	public void unRegisterBoradcastReceiver() {
		this.getActivity().unregisterReceiver(mBroadcastReceiver);
	}

	private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if (action.equals(UPDATE_LIST_ACTION_NAME)) {
				doGetCartList();
				doGetFoodList();
			}
		}
	};

	@Override
	protected void lazyLoad() {
		isRefreshing = true;
		pageNo = 1;
		
		doGetCartList();
		doGetFoodList();
	}

	@Override
	protected void onInvisible() {
		super.onInvisible();
		
		if(foodList != null) {
			foodList.clear();
		}
	}
}