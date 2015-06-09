package com.company.ilunch.adapter;

import com.company.ilunch.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * 我的订单适配器
 */
public class MyOrderAdapter extends BaseExpandableListAdapter {
	private Context mContext;

	public MyOrderAdapter(Context mContext) {
		this.mContext = mContext;
	}

	@Override
	public Object getChild(int arg0, int arg1) {
		return null;
	}

	@Override
	public long getChildId(int arg0, int arg1) {
		return 0;
	}

	@Override
	public View getChildView(final int groupPosition, int childPosition,
			boolean isLastChild, View converView, ViewGroup parent) {
		ChildViewHolder holder = new ChildViewHolder();
		if (converView == null) {
			converView = LayoutInflater.from(mContext).inflate(
					R.layout.myorder_item_layout, null);
			holder.orderImage = (ImageView) converView
					.findViewById(R.id.orderImage);
			holder.ordersGoodName = (TextView) converView
					.findViewById(R.id.ordersGoodName);
			holder.ordersGoodPrice = (TextView) converView
					.findViewById(R.id.ordersGoodPrice);
			holder.ordersGoodCount = (TextView) converView
					.findViewById(R.id.ordersGoodCount);

			converView.setTag(holder);
		} else {
			holder = (ChildViewHolder) converView.getTag();
		}

		return converView;
	}

	@Override
	public int getChildrenCount(int arg0) {
		return 2;
	}

	@Override
	public Object getGroup(int arg0) {
		return null;
	}

	@Override
	public int getGroupCount() {
		return 2;
	}

	@Override
	public long getGroupId(int arg0) {
		return arg0;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View converView, ViewGroup parent) {
		GroupViewHolder holder = new GroupViewHolder();
		if (converView == null) {
			converView = LayoutInflater.from(mContext).inflate(
					R.layout.myorder_group_item_layout, null);
			holder.orderResultTv = (TextView) converView
					.findViewById(R.id.orderResultTv);
			holder.orderTimeText = (TextView) converView
					.findViewById(R.id.orderTimeText);
			converView.setTag(holder);
		} else {
			holder = (GroupViewHolder) converView.getTag();
		}

		return converView;
	}

	@Override
	public boolean hasStableIds() {
		return false;
	}

	@Override
	public boolean isChildSelectable(int arg0, int arg1) {
		return false;
	}

	private static class ChildViewHolder {
		private ImageView orderImage;// 商品图片
		private TextView ordersGoodName;// 商品名称
		private TextView ordersGoodPrice;// 商品价格
		private TextView ordersGoodCount;// 商品数量
	}

	private static class GroupViewHolder {
		private TextView orderResultTv;// 订单结果
		private TextView orderTimeText;// 时间
	}
}