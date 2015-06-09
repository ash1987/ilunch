package com.company.ilunch.ui;

import com.company.ilunch.R;

import android.os.Bundle;

public class BookingActivity extends FoodListBaseActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		SalesMethod = 0;
		titleTv.setText(R.string.dz);
	}
}
