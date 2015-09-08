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
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.Toast;
import com.company.ilunch.R;
import com.company.ilunch.adapter.MyFavAdapter;
import com.company.ilunch.adapter.MyFavAdapter.Callback;
import com.company.ilunch.base.BaseActivity;
import com.company.ilunch.bean.DeleMyCollectBean;
import com.company.ilunch.bean.FavBean;
import com.company.ilunch.bean.FavBean.Body;
import com.company.ilunch.net.HttpUrlManager;
import com.company.ilunch.net.RequestListener;
import com.company.ilunch.preferences.LoginPreference;
import com.company.ilunch.task.DeleMyCollectTask;
import com.company.ilunch.task.FavListTask;
import com.company.ilunch.utils.LogUtil;
import com.company.ilunch.widget.CustomToast;
import com.company.ilunch.widget.SlideListView;

/**
 * 我的收藏 界面
 */
public class MyFavActivity extends BaseActivity implements OnClickListener {

	public final static String TAG = "com.company.ilunch";
	public final static int REQUEST_SUCESS = 0x01;// 成功
	public final static int REQUEST_FAILED = 0x02;// 失败
	private final static int MSG_DEL_COLLECT_SUCCESS = 0x03;// 　删除收藏成功
	private final static int MSG_DEL_COLLECT_FAIL = 0x04;// 删除收藏失敗

	private LoginPreference loginPreference;

	private ImageView backIv;// 返回
	private SlideListView favoriteListView;// 收藏列表
	private MyFavAdapter favoriteAdapter;//
	private ArrayList<Body> favLists = new ArrayList<Body>();

	private int pageNo = 1;
	private int pageSize = 1000;
	private boolean isRefreshing = true;
	private int total = 0;

	@Override
	protected void initData() {
		loginPreference = new LoginPreference(this);
	}

	@Override
	protected void initView() {
		setContentView(R.layout.my_fav_layout);
		backIv = (ImageView) this.findViewById(R.id.backIv);
		favoriteListView = (SlideListView) this
				.findViewById(R.id.myFavoriteList);
		favoriteListView.initSlideMode(SlideListView.MOD_RIGHT);

		favoriteAdapter = new MyFavAdapter(this, favLists, new Callback() {
			@Override
			public void sendCollect(int pos) {
				Intent cfIntent = new Intent(MyFavActivity.this,
						CommentFoodActivity.class);
				cfIntent.putExtra("TogoId", favLists.get(pos).getTogoid());
				cfIntent.putExtra("foodName", favLists.get(pos).getFoodName());
				cfIntent.putExtra("picture", favLists.get(pos).getPicture());
				startActivity(cfIntent);

				favoriteListView.slideBack();
			}

			@Override
			public void deleItem(int pos) {
				doDelMyCollect(favLists.get(pos));
				favoriteListView.slideBack();
			}
		});
	}

	@Override
	protected void setAttribute() {
		backIv.setVisibility(View.VISIBLE);
		backIv.setOnClickListener(this);
		favoriteListView.setAdapter(favoriteAdapter);
		favoriteListView.setItemsCanFocus(false);
		// favoriteListView.setonRefreshListener(new RefleshListener());
		// favoriteListView.setonMoreListener(new MoreListener());
		favoriteListView.setOnItemClickListener(new MyItemSelectListener());

		if (loginPreference.getLoginState()) {
			// 获取收藏列表
			getFavList();
		} else {
			startActivity(new Intent(MyFavActivity.this, LoginActivity.class));
			finish();
		}
	}

	// listView选中
	private class MyItemSelectListener implements OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {

		}

	}

	// /**
	// * 下拉刷新事件监听
	// */
	// private class RefleshListener implements OnRefreshListener {
	//
	// @Override
	// public void onRefresh() {
	// // 下拉刷新
	// isRefreshing = true;
	// pageNo = 1;
	// getFavList();
	// }
	//
	// }
	//
	// /**
	// * 上拉加载更多
	// */
	// private class MoreListener implements onMoreListener {
	//
	// @Override
	// public void onMore() {
	// // 加载下一页
	// if (total > favLists.size()) {
	// pageNo++;
	// favoriteListView.addAutoLoadFooterView(MyFavActivity.this);
	// getFavList();
	// }
	// }
	// }

	/**
	 * 向服务器请求删除收藏 <br/>
	 * 
	 */
	private void doDelMyCollect(FavBean.Body body) {
		DeleMyCollectTask task = new DeleMyCollectTask();

		JSONObject requestParams = new JSONObject();
		try {
			requestParams.put("UserId", Integer.parseInt(body.getUserid()));
			requestParams.put("FoodId", Integer.parseInt(body.getFoodId()));
		} catch (JSONException e) {
			e.printStackTrace();
		}

		task.request(this, HttpUrlManager.DEL_MY_COLLECT_STRING, requestParams,
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

	// 获取收藏列表
	private void getFavList() {
		showProgress(null, null);
		FavListTask task = new FavListTask();

		JSONObject requestParams = new JSONObject();

		try {
			requestParams.put("UserId", loginPreference.getDataID());
			requestParams.put("PageIndex", pageNo);
			requestParams.put("PageSize", pageSize);
		} catch (Exception e) {
			e.printStackTrace();
		}

		task.request(this, HttpUrlManager.GET_MY_COLLECT_STRING, requestParams,
				listener);
	}

	/**
	 * 获取收藏列表
	 */
	private RequestListener<FavBean> listener = new RequestListener<FavBean>() {

		@Override
		public void OnStart() {
			LogUtil.d(TAG, "开始请求OnStart-----------");
		}

		@Override
		public void onError(Exception e) {
			dismissProgress();
			LogUtil.d(TAG, "onError-----------" + e.getMessage());
			mHandler.obtainMessage(REQUEST_FAILED, e.getMessage())
					.sendToTarget();
		}

		@Override
		public void OnPaserComplete(FavBean bean) {
			dismissProgress();
			if (bean != null) {
				LogUtil.d(TAG, "OnPaserComplete:" + bean.getHead());
				if ("00".equals(bean.getHead().getResultCode())) {
					mHandler.obtainMessage(REQUEST_SUCESS, bean).sendToTarget();
				} else {
					mHandler.obtainMessage(REQUEST_FAILED,
							bean.getHead().getResultInfo()).sendToTarget();
				}
			} else {
				mHandler.obtainMessage(REQUEST_FAILED,
						getString(R.string.login_failed_string)).sendToTarget();
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
			case REQUEST_SUCESS:// 获取列表成功
				favLists.clear();
				// total = 0;
				// }

				FavBean favBean = (FavBean) msg.obj;

				if (favBean != null && favBean.getBody() != null
						&& favBean.getBody().size() != 0) {
					total += favBean.getBody().size();
					favLists.addAll(favBean.getBody());
				}

				favoriteAdapter.notifyDataSetChanged();

				// favoriteListView.onRefreshComplete();
				// favoriteListView.hideAutoLoadFooterView();
				break;
			case REQUEST_FAILED:// 获取列表失败
				isRefreshing = false;

				// favoriteListView.hideAutoLoadFooterView();
				// favoriteListView.onRefreshComplete();
				if (!TextUtils.isEmpty(String.valueOf(object))) {
					CustomToast.getToast(MyFavActivity.this,
							String.valueOf(object), Toast.LENGTH_SHORT).show();
				}
				break;
			case MSG_DEL_COLLECT_SUCCESS:// 删除成功
				Toast.makeText(MyFavActivity.this,
						R.string.del_collect_success_string, Toast.LENGTH_SHORT)
						.show();

				getFavList();
				break;
			case MSG_DEL_COLLECT_FAIL:
				CustomToast.getToast(MyFavActivity.this,
						String.valueOf(object), Toast.LENGTH_SHORT).show();
				break;
			default:
				break;
			}
		}
	};

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.backIv:// 返回
			this.finish();
			break;

		default:
			break;
		}
	}
}
