package com.company.ilunch.adapter;

import java.util.LinkedList;

import com.company.ilunch.R;
import com.company.ilunch.bean.GetDsBean.Body;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.SectionIndexer;
import android.widget.TextView;

/**
 * 我的位置-大廈适配器
 */
public class MyLocationDsAdapter extends BaseAdapter implements SectionIndexer {
	private Context mContext;
	private LinkedList<Body> dataList;

	public MyLocationDsAdapter(Context mContext, LinkedList<Body> dataList) {
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

		holder.cityTxt.setText(dataList.get(position).getName());

		return converView;
	}

	private static class ViewHolder {
		private TextView cityTxt;
	}

	public Object[] getSections() {
		return null;
	}

	public int getSectionForPosition(int position) {

		return 0;
	}

	public int getPositionForSection(int section) {
		Body mContent;
		String l;
		for (int i = 0; i < getCount(); i++) {
			mContent = (Body) dataList.get(i);
			l = mContent.getFirstL();
			char firstChar = l.toUpperCase().charAt(0);
			if (firstChar == section) {
				return i;
			}

		}
		mContent = null;
		l = null;
		return -1;
	}
}
