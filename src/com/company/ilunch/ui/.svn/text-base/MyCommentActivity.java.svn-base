package com.company.ilunch.ui;

import java.util.ArrayList;
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
import com.company.ilunch.adapter.MyCommentAdapter;
import com.company.ilunch.adapter.MyCommentAdapter.Callback;
import com.company.ilunch.base.BaseActivity;
import com.company.ilunch.bean.CommentBean;
import com.company.ilunch.bean.CommentBean.Body;
import com.company.ilunch.net.HttpUrlManager;
import com.company.ilunch.net.RequestListener;
import com.company.ilunch.preferences.LoginPreference;
import com.company.ilunch.task.CommentListTask;
import com.company.ilunch.utils.LogUtil;
import com.company.ilunch.widget.CustomToast;
import com.company.ilunch.widget.SlideListView;

/**
 * 我的点评 界面
 */
public class MyCommentActivity extends BaseActivity implements OnClickListener {
	public final static String TAG = "com.company.ilunch";

	public final static int REQUEST_SUCESS = 0x01;// 成功
	public final static int REQUEST_FAILED = 0x02;// 失败
	private final static int MSG_DEL_COLLECT_SUCCESS = 0x03;// 　删除评论成功
	private final static int MSG_DEL_COLLECT_FAIL = 0x04;// 删除评论失敗

	private LoginPreference loginPreference;

	private ImageView backIv;// 返回
	private SlideListView commentListView;// 点评列表
	private MyCommentAdapter commentAdapter;//
	private ArrayList<Body> commentLists = new ArrayList<Body>();

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
		setContentView(R.layout.my_comment_layout);
		backIv = (ImageView) this.findViewById(R.id.backIv);
		commentListView = (SlideListView) this.findViewById(R.id.myCommentList);
		commentListView.initSlideMode(SlideListView.MOD_RIGHT);

		commentAdapter = new MyCommentAdapter(this, commentLists,
				new Callback() {
					@Override
					public void deleItem(int pos) {
						// doDelMyCollect(commentLists.get(pos));
						commentListView.slideBack();
					}
				});
	}

	@Override
	protected void setAttribute() {
		backIv.setVisibility(View.VISIBLE);
		backIv.setOnClickListener(this);
		commentListView.setAdapter(commentAdapter);
		commentListView.setItemsCanFocus(false);
		// commentListView.setonRefreshListener(new RefleshListener());
		// commentListView.setonMoreListener(new MoreListener());
		commentListView.setOnItemClickListener(new MyItemSelectListener());

		if (loginPreference.getLoginState()) {
			// 获取评论列表
			getCommentList();
		} else {
			startActivity(new Intent(MyCommentActivity.this,
					LoginActivity.class));
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

	// 获取评论列表
	private void getCommentList() {
		showProgress(null, null);
		CommentListTask task = new CommentListTask();

		JSONObject requestParams = new JSONObject();

		try {
			requestParams.put("UserId",
					Integer.parseInt(loginPreference.getDataID()));
			requestParams.put("Page", pageNo);
//			requestParams.put("PageSize", pageSize);
			 requestParams.put("TogoId", 811);
		} catch (Exception e) {
			e.printStackTrace();
		}

		task.request(this, HttpUrlManager.GET_COMMENT_STRING, requestParams,
				listener);
	}

	/**
	 * 获取收藏列表
	 */
	private RequestListener<CommentBean> listener = new RequestListener<CommentBean>() {

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
		public void OnPaserComplete(CommentBean bean) {
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
						getString(R.string.get_comment_failed_string))
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
			case REQUEST_SUCESS:// 获取列表成功
				commentLists.clear();
				// total = 0;
				// }

				CommentBean cBean = (CommentBean) msg.obj;

				if (cBean != null && cBean.getBody() != null
						&& cBean.getBody().size() != 0) {
					total += cBean.getBody().size();
					commentLists.addAll(cBean.getBody());
				}

				commentAdapter.notifyDataSetChanged();

				// favoriteListView.onRefreshComplete();
				// favoriteListView.hideAutoLoadFooterView();
				break;
			case REQUEST_FAILED:// 获取列表失败
				isRefreshing = false;

				// favoriteListView.hideAutoLoadFooterView();
				// favoriteListView.onRefreshComplete();
				if (!TextUtils.isEmpty(String.valueOf(object))) {
					CustomToast.getToast(MyCommentActivity.this,
							String.valueOf(object), Toast.LENGTH_SHORT).show();
				}
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
