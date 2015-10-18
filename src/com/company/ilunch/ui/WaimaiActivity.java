package com.company.ilunch.ui;

import com.company.ilunch.R;

import android.os.Bundle;

public class WaimaiActivity extends FoodListBaseActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		SalesMethod = 2;
		titleTv.setText(R.string.wm);
	}
}
