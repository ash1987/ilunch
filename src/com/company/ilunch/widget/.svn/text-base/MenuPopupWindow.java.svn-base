package com.company.ilunch.widget;

import java.util.ArrayList;
import com.alibaba.fastjson.JSON;
import com.company.ilunch.R;
import com.company.ilunch.adapter.MenuPopupAdapter;
import com.company.ilunch.bean.GetShopDataBean;
import com.company.ilunch.bean.GetShopDataBean.Body;
import com.company.ilunch.preferences.IlunchPreference;
import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.PopupWindow;

public class MenuPopupWindow extends PopupWindow {
	private Context mContext;

	private View mMenuView;

	private ArrayList<Body> list = new ArrayList<Body>();
	
	public interface Callback {
		void clickResult(int position);
	}

	public MenuPopupWindow(Activity context, ArrayList<Body> bodyList, Callback callback) {
		super(context);

		this.mContext = context;

		initData();

		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mMenuView = inflater.inflate(R.layout.menu_popup_layout, null);

		GridView menuGv = (GridView) mMenuView.findViewById(R.id.gridview);

		MenuPopupAdapter saImageItems = new MenuPopupAdapter(mContext, list);
		menuGv.setAdapter(saImageItems);
		menuGv.setOnItemClickListener(new ItemClickListener(callback));

		// 控制popupwindow的宽度和高度自适应
		menuGv.measure(View.MeasureSpec.UNSPECIFIED,
				View.MeasureSpec.UNSPECIFIED);

		// 设置SelectPicPopupWindow的View
		this.setContentView(mMenuView);
		// 设置SelectPicPopupWindow弹出窗体的宽
		this.setWidth(LayoutParams.MATCH_PARENT);
		// 设置SelectPicPopupWindow弹出窗体的高
		this.setHeight(LayoutParams.WRAP_CONTENT);

		// 设置SelectPicPopupWindow弹出窗体可点击
		this.setFocusable(true);
		// 设置SelectPicPopupWindow弹出窗体动画效果
//		this.setAnimationStyle(R.style.PopupAnimation);
		// // 实例化一个ColorDrawable颜色为半透明
		ColorDrawable dw = new ColorDrawable(0xffffff);
		// // 设置SelectPicPopupWindow弹出窗体的背景
		this.setBackgroundDrawable(dw);
	}

	class ItemClickListener implements OnItemClickListener {
		private Callback callback;
		
		public ItemClickListener(Callback callback) {
			this.callback = callback;
		}
		
		@Override
		public void onItemClick(AdapterView parent, View view, int position, long id) {
			callback.clickResult(position);
			
			MenuPopupWindow.this.dismiss();
		}
	}

	private void initData() {
		list.clear();
		
		IlunchPreference iPreference = new IlunchPreference(mContext);
		GetShopDataBean gsdBean = JSON.toJavaObject(
				JSON.parseObject(iPreference.getShopData()),
				GetShopDataBean.class);
		list = gsdBean.getBody();
		
		Body scBody = new Body();
		scBody.setClassname(mContext.getString(R.string.menu_sc));
		list.add(scBody);
	}
}
