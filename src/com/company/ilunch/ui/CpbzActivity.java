package com.company.ilunch.ui;

import java.util.ArrayList;
import org.json.JSONObject;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.widget.CompoundButton.OnCheckedChangeListener;
import com.company.ilunch.R;
import com.company.ilunch.base.BaseActivity;
import com.company.ilunch.bean.GetSTemplateListBean;
import com.company.ilunch.net.HttpUrlManager;
import com.company.ilunch.net.RequestListener;
import com.company.ilunch.task.GetSTemplateListTask;
import com.company.ilunch.utils.AndroidUtils;
import com.company.ilunch.utils.LogUtil;

public class CpbzActivity extends BaseActivity implements OnClickListener {
	public final static String TAG = "com.company.ilunch";

	private final static int MSG_GET_STEMPLATE_LIST_SUCCESS = 0x03;// 　获取备注成功
	private final static int MSG_GET_STEMPLATE_LIST_FAIL = 0x04;// 获取备注失敗

	private ImageView backIv;// 返回

	private RelativeLayout cpbzRl;
	private RadioGroup cpbzRg;
	private Button btn_submit;
	private EditText bzEt;

	private String Remark;
	private String editRemark;
	private ArrayList<GetSTemplateListBean.Body> stllDataList;

	@Override
	protected void initData() {
		stllDataList = new ArrayList<GetSTemplateListBean.Body>();

		// if (getIntent() != null) {
		// Bundle bundle = getIntent().getExtras();
		// if (bundle != null && bundle.containsKey("Remark")) {
		// Remark = bundle.getString("Remark");
		// }
		// }
	}

	@Override
	protected void initView() {
		setContentView(R.layout.cpbz_layout);

		backIv = (ImageView) this.findViewById(R.id.backIv);
		cpbzRl = (RelativeLayout) findViewById(R.id.cpbzRl);
		cpbzRg = (RadioGroup) findViewById(R.id.cpbzRg);
		btn_submit = (Button) findViewById(R.id.btn_submit);
		bzEt = (EditText) findViewById(R.id.bzEt);
	}

	@Override
	protected void setAttribute() {
		backIv.setOnClickListener(this);
		btn_submit.setOnClickListener(this);

		doGetSTemplateList();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.backIv:
			this.finish();
			break;
		case R.id.btn_submit:
			Intent intent = new Intent();
			intent.putExtra("Remark", bzEt.getText().toString().trim());
			CpbzActivity.this.setResult(1, intent);
			CpbzActivity.this.finish();
			break;
		default:
			break;
		}
	}

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

	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			super.handleMessage(msg);

			if (msg == null) {
				return;
			}

			switch (msg.what) {
			case MSG_GET_STEMPLATE_LIST_SUCCESS:
				GetSTemplateListBean gstlBean = (GetSTemplateListBean) msg.obj;

				if (gstlBean != null && gstlBean.getBody() != null
						&& gstlBean.getBody().size() != 0) {
					stllDataList.clear();
					stllDataList.addAll(gstlBean.getBody());
				}

				addRadioBtns();

				// if(!TextUtils.isEmpty(Remark) && cpbzRg.getChildCount()>0) {
				// for(int i=0;i<cpbzRg.getChildCount();i++) {
				// LinearLayout ll = (LinearLayout) cpbzRg.getChildAt(i);
				// for(int j=0;j<ll.getChildCount();j++) {
				// RadioButton rb = (RadioButton) ll.getChildAt(j);
				//
				// if(Remark.equals(rb.getText().toString())) {
				// rb.setChecked(true);
				// break;
				// }
				// }
				// }
				// }
				break;
			case MSG_GET_STEMPLATE_LIST_FAIL:
				Toast.makeText(CpbzActivity.this, (String) msg.obj,
						Toast.LENGTH_SHORT).show();
				break;
			default:
				break;
			}
		};
	};

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
				editRemark = bzEt.getText().toString().trim();
				bzEt.setText(editRemark + stllDataList.get(i).getRemark());
				try {
					bzEt.setSelection(bzEt.length());
				} catch (Exception e) {
					e.printStackTrace();
				}
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
}