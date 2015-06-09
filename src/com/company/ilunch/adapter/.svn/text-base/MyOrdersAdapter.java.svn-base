package com.company.ilunch.adapter;

import java.util.ArrayList;
import com.company.ilunch.R;
import com.company.ilunch.bean.GetOrderListBean.Body;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * 我的订单适配器
 */
public class MyOrdersAdapter extends BaseAdapter {
	private Context mContext;
	private ArrayList<Body> dataList;

	public MyOrdersAdapter(Context mContext, ArrayList<Body> dataList) {
		this.mContext = mContext;
		this.dataList = dataList;
	}

	@Override
	public int getCount() {
		return dataList.size();
	}

	@Override
	public Object getItem(int position) {
		return dataList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View converView, ViewGroup arg2) {
		ViewHolder holder = new ViewHolder();
		if (converView == null) {
			converView = LayoutInflater.from(mContext).inflate(
					R.layout.my_orders_item, null);
			holder.orderIdTv = (TextView) converView.findViewById(R.id.orderIdTv);
			holder.orderDateTv = (TextView) converView.findViewById(R.id.orderDateTv);
			holder.OrderRcverTv = (TextView) converView.findViewById(R.id.OrderRcverTv);
			holder.togonameTv = (TextView) converView.findViewById(R.id.togonameTv);
			
			converView.setTag(holder);
		} else {
			holder = (ViewHolder) converView.getTag();
		}

		holder.orderIdTv.setText(dataList.get(position).getOrderid().toString().trim());
		holder.orderDateTv.setText(dataList.get(position).getOrderDateTime().toString().trim());
		holder.OrderRcverTv.setText(dataList.get(position).getOrderRcver().toString().trim());
		holder.togonameTv.setText(dataList.get(position).getTogoname().toString().trim());

		return converView;
	}

	private static class ViewHolder {
		private TextView orderIdTv;
		private TextView orderDateTv;
		private TextView OrderRcverTv;
		private TextView togonameTv;
	}
}
