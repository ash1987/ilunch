package com.company.ilunch.ui;

import org.json.JSONException;
import org.json.JSONObject;

import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.company.ilunch.R;
import com.company.ilunch.base.BaseActivity;
import com.company.ilunch.bean.AddCollectBean;
import com.company.ilunch.net.HttpUrlManager;
import com.company.ilunch.net.RequestListener;
import com.company.ilunch.preferences.LoginPreference;
import com.company.ilunch.task.AddCollectTask;
import com.company.ilunch.utils.AndroidUtils;
import com.company.ilunch.utils.LogUtil;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

/*
 * 发起评论
 */
public class CommentFoodActivity extends BaseActivity implements
		OnClickListener {
	public final static String TAG = "com.company.ilunch";
	public final static int REQUEST_SUCESS = 0x01;// 成功
	public final static int REQUEST_FAILED = 0x02;// 失败

	private ImageView backIv;// 返回
	private TextView foodNameTv;
	private EditText contentEt;
	private Button commitBt;
	private ImageView foodIv;

	private String foodName;// 食品名称
	private int TogoId;// 商户ID
	private String picture;//食品图片

	private LoginPreference loginPerference;
	private ImageLoader imageLoader;
	private DisplayImageOptions options;

	@Override
	protected void initData() {
		loginPerference = new LoginPreference(this);
		imageLoader = ImageLoader.getInstance();
		options = new DisplayImageOptions.Builder().cacheInMemory(true)
				.showImageOnLoading(R.drawable.img_loading)
				.showImageOnFail(R.drawable.img_loading)
				.showImageForEmptyUri(R.drawable.img_loading).cacheOnDisc(true)
				.resetViewBeforeLoading(true).considerExifParams(true)
				.bitmapConfig(Bitmap.Config.RGB_565)
				.imageScaleType(ImageScaleType.EXACTLY).build();

		if (getIntent().getExtras() != null) {
			if (getIntent().getExtras().containsKey("foodName")) {
				foodName = getIntent().getStringExtra("foodName");
			}
			if (getIntent().getExtras().containsKey("TogoId")) {
				TogoId = getIntent().getIntExtra("TogoId", 0);
			}
			if (getIntent().getExtras().containsKey("picture")) {
				picture = getIntent().getStringExtra("picture");
			}
		}
	}

	@Override
	protected void initView() {
		setContentView(R.layout.comment_food_layout);

		backIv = (ImageView) this.findViewById(R.id.backIv);
		foodNameTv = (TextView) findViewById(R.id.foodNameTv);
		contentEt = (EditText) findViewById(R.id.contentEt);
		commitBt = (Button) findViewById(R.id.commitBt);
		foodIv = (ImageView) findViewById(R.id.foodIv);
	}

	@Override
	protected void setAttribute() {
		backIv.setOnClickListener(this);
		commitBt.setOnClickListener(this);

		foodNameTv.setText(foodName);
		imageLoader.displayImage(AndroidUtils.getImgUrlOnServer(picture), foodIv, options);
	}

	/**
	 * 向服务器请求发起评论<br/>
	 * 
	 */
	private void doAddComment() {
		if (TextUtils.isEmpty(contentEt.getText().toString())) {
			Toast.makeText(this, R.string.content_null, Toast.LENGTH_SHORT)
					.show();
			return;
		}

		AddCollectTask task = new AddCollectTask();

		JSONObject requestParams = new JSONObject();
		try {
			requestParams.put("UserId",
					Integer.parseInt(loginPerference.getDataID()));
			requestParams.put("UserName", loginPerference.getUserName());
			requestParams.put("TogoId", TogoId);
			requestParams.put("Comment", contentEt.getText().toString().trim());
			requestParams.put("Attitude", 5);
			requestParams.put("Quality", 5);
			requestParams.put("Speed", 5);
		} catch (JSONException e) {
			e.printStackTrace();
		}

		task.request(this, HttpUrlManager.ADD_COMMENT_STRING, requestParams,
				addMyCollectListener);
	}

	/**
	 * 发起评论接口监听类
	 */
	private RequestListener<AddCollectBean> addMyCollectListener = new RequestListener<AddCollectBean>() {

		@Override
		public void OnStart() {
			LogUtil.d(TAG, "开始请求OnStart-----------");
		}

		@Override
		public void onError(Exception e) {
			LogUtil.d(TAG, "onError-----------" + e.getMessage());
		}

		@Override
		public void OnPaserComplete(AddCollectBean bean) {
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
						getString(R.string.addcollect_success_string))
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
				Toast.makeText(CommentFoodActivity.this,
						R.string.addcollect_success_string, Toast.LENGTH_SHORT)
						.show();
				finish();
				break;
			case REQUEST_FAILED:// 获取列表失败
				Toast.makeText(CommentFoodActivity.this, (String) object,
						Toast.LENGTH_SHORT).show();
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
			doAddComment();
			break;
		default:
			break;
		}
	}
}
