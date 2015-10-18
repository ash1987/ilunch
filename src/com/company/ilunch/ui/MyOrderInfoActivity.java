package com.company.ilunch.ui;

import java.util.Calendar;
import java.util.Date;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import com.company.ilunch.R;
import com.company.ilunch.base.BaseActivity;
import com.company.ilunch.utils.AndroidUtils;

/*
 * 订单信息
 */
public class MyOrderInfoActivity extends BaseActivity implements OnClickListener {
	private ImageView backIv;// 返回
	
	private int SalesMethod;
	private String OrderId;
	private String alertTime;
	
	private TextView timeTv;
	private TextView orderIdTv;
	private ImageView orderTypeIv;
	private TextView orderTypeTv;

	@Override
	protected void initData() {
		if(getIntent().getExtras() != null) {
			if(getIntent().getExtras().containsKey("SalesMethod")) {
				SalesMethod = getIntent().getExtras().getInt("SalesMethod");
			}
			if(getIntent().getExtras().containsKey("OrderId")) {
				OrderId = getIntent().getExtras().getString("OrderId");
			}
			if(getIntent().getExtras().containsKey("alertTime")) {
				alertTime = getIntent().getExtras().getString("alertTime");
			}
		}
	}

	@Override
	protected void initView() {
		setContentView(R.layout.my_orderinfo_layout);

		backIv = (ImageView) this.findViewById(R.id.backIv);
		timeTv = (TextView) findViewById(R.id.timeTv);
		orderIdTv = (TextView) findViewById(R.id.orderIdTv);
		orderTypeIv = (ImageView) findViewById(R.id.orderTypeIv);
		orderTypeTv = (TextView) findViewById(R.id.orderTypeTv);
	}

	@Override
	protected void setAttribute() {
		backIv.setOnClickListener(this);
		
		if(SalesMethod == 0) {
			//打包
			orderTypeIv.setImageResource(R.drawable.home_icon_pack);
			orderTypeTv.setText(R.string.pack_content_string);
		} else if(SalesMethod == 1) {
			//订座
			orderTypeIv.setImageResource(R.drawable.home_icon_book);
			orderTypeTv.setText(R.string.book_content_string);
		} else {
			//外卖
			orderTypeIv.setImageResource(R.drawable.home_icon_takeout);
			orderTypeTv.setText(R.string.waimai_content_string);
		}
		
		orderIdTv.setText(OrderId);
		
		long localAlertTime = AndroidUtils.getStringToDate(alertTime, "HH:mm");
		
		Calendar c = Calendar.getInstance();
		c.setTime(new Date());
		long nowTime = AndroidUtils.getStringToDate(
				c.get(Calendar.HOUR_OF_DAY) + ":" + c.get(Calendar.MINUTE),
				"HH:mm");
		
		long between = (localAlertTime - nowTime) / 1000;
		if(between < 0) {
			between += 24*60*60*1000;
		}
		long hour=between%(24*3600)/3600;
		long minute = between % 3600 / 60;
		
		timeTv.setText(hour+"时" + minute+"分");
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
