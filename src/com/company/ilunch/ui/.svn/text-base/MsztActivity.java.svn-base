package com.company.ilunch.ui;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import com.company.ilunch.R;
import com.company.ilunch.adapter.MsztListAdapter;
import com.company.ilunch.base.BaseActivity;
import com.company.ilunch.widget.UpRefreshListView;

public class MsztActivity extends BaseActivity implements OnClickListener {
	private ImageView backIv;// 返回
	private UpRefreshListView msztListView;
	private MsztListAdapter msztListAdapter;
	
	private UpRefreshListView goodsLv;
	@Override
	protected void initData() {
	}

	@Override
	protected void initView() {
		setContentView(R.layout.mszt_layout);
		backIv = (ImageView) this.findViewById(R.id.backIv);
		msztListView = (UpRefreshListView) this.findViewById(R.id.msztListView);
	}

	@Override
	protected void setAttribute() {
		backIv.setOnClickListener(this);
		
		msztListAdapter = new MsztListAdapter(this);
		msztListView.setAdapter(msztListAdapter);
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
}
