package com.company.ilunch.adapter;

import java.util.LinkedList;

import com.company.ilunch.R;
import com.company.ilunch.bean.GetSectionBean.Body;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * 我的位置-區域适配器
 */
public class MyLocationSectionAdapter extends BaseAdapter {
	private Context mContext;
	private LinkedList<Body> dataList;

	public MyLocationSectionAdapter(Context mContext, LinkedList<Body> dataList) {
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
					R.layout.mylocation_city_item, null);
			holder.cityTxt = (TextView) converView.findViewById(R.id.cityTxt);
			converView.setTag(holder);
		} else {
			holder = (ViewHolder) converView.getTag();
		}

		holder.cityTxt.setText(dataList.get(position).getSectionName());

		return converView;
	}

	private static class ViewHolder {
		private TextView cityTxt;
	}
}
