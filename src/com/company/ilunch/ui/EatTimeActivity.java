package com.company.ilunch.ui;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import com.company.ilunch.R;
import com.company.ilunch.base.BaseActivity;
import com.company.ilunch.preferences.IlunchPreference;
import com.company.ilunch.widget.scrollDatePicker.WheelMain;
import com.company.ilunch.widget.scrollDatePicker.WheelView;

/*
 * 用餐提醒
 */
public class EatTimeActivity extends BaseActivity implements OnClickListener {
	private ImageView backIv;// 返回

	private IlunchPreference ilunchPreference;
	private WheelView yearView;
	private WheelView monthView;
	private WheelView dayView;
	private WheelView hourView;
	private WheelView minsView;
	private Button btn_sure;
	private View includeView;

	@Override
	protected void initData() {
		ilunchPreference = new IlunchPreference(this);
	}

	@Override
	protected void initView() {
		setContentView(R.layout.eat_time_layout);

		backIv = (ImageView) this.findViewById(R.id.backIv);
		includeView = findViewById(R.id.includeView);
		yearView = (WheelView) includeView.findViewById(R.id.year);
		monthView = (WheelView) includeView.findViewById(R.id.month);
		dayView = (WheelView) includeView.findViewById(R.id.day);
		hourView = (WheelView) includeView.findViewById(R.id.hour);
		minsView = (WheelView) includeView.findViewById(R.id.mins);
		btn_sure = (Button) includeView.findViewById(R.id.btn_datetime_sure);
	}

	@Override
	protected void setAttribute() {
		backIv.setOnClickListener(this);
		btn_sure.setOnClickListener(this);
		
		WheelMain main = new WheelMain(null, includeView, this);
		main.showDateTimePicker(null);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.backIv:
			this.finish();
			break;
		case R.id.btn_datetime_sure:
			String newTime = hourView.getTextItem(hourView.getCurrentItem())
					+ ":" + minsView.getTextItem(minsView.getCurrentItem());
			ilunchPreference.setLunchTimeAlert(this, newTime);
			finish();
			break;
		default:
			break;
		}
	}
}
