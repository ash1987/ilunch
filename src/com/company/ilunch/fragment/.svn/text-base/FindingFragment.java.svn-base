package com.company.ilunch.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import com.company.ilunch.R;
import com.company.ilunch.ui.MsztActivity;

/**
 * 发现fragment
 */
public class FindingFragment extends Fragment implements OnClickListener {
	public static final String FRAGMENT_TAG = FindingFragment.class
			.getSimpleName();
	private RelativeLayout msztRl;// 美食侦探

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.finding_fragment_layout, null);

		initView(view);
		setAttribute();

		return view;
	}

	// 初始化控件
	private void initView(View view) {
		msztRl = (RelativeLayout) view.findViewById(R.id.msztRl);
	}

	// 设置控件属性
	private void setAttribute() {
		msztRl.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		if (v == null) {
			return;
		}

		switch (v.getId()) {
		case R.id.msztRl:
			startActivity(new Intent(FindingFragment.this.getActivity(), MsztActivity.class));
			break;
		default:
			break;
		}
	}
}