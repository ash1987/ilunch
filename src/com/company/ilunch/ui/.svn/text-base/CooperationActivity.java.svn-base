package com.company.ilunch.ui;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import com.company.ilunch.R;
import com.company.ilunch.base.BaseActivity;

public class CooperationActivity extends BaseActivity implements OnClickListener {
	private ImageView backIv;// 返回

	@Override
	protected void initData() {
	}

	@Override
	protected void initView() {
		setContentView(R.layout.cooperation_layout);

		backIv = (ImageView) this.findViewById(R.id.backIv);
	}

	@Override
	protected void setAttribute() {
		backIv.setOnClickListener(this);
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
