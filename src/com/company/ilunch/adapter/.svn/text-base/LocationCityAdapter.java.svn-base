package com.company.ilunch.adapter;

import java.util.LinkedList;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.SectionIndexer;
import android.widget.TextView;
import com.company.ilunch.R;
import com.company.ilunch.bean.GetCitysBean.Body;

public class LocationCityAdapter extends BaseAdapter implements SectionIndexer {

	private LinkedList<Body> list = null;
	private Context mContext;
	private Callback callback;

	public interface Callback {
		public void reLocation(String newLocation, String newLocationCid);
	}
	
	public LocationCityAdapter(Context mContext, LinkedList<Body> list, Callback callback) {
		this.mContext = mContext;
		this.list = list;
		this.callback = callback;
	}

	public int getCount() {
		return this.list.size();
	}

	public Object getItem(int position) {
		return list.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(final int position, View view, ViewGroup arg2) {
		ViewHolder viewHolder = null;
		if (view == null) {
			viewHolder = new ViewHolder();
			view = LayoutInflater.from(mContext).inflate(R.layout.name_item,
					null);
			viewHolder.tvTitle = (TextView) view
					.findViewById(R.id.name_item_text);
			viewHolder.tvLetter = (TextView) view.findViewById(R.id.catalog);
			view.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) view.getTag();
		}
		final Body mContent = list.get(position);
		if (position == 0) {
			viewHolder.tvLetter.setVisibility(View.VISIBLE);
			viewHolder.tvLetter.setText(mContent.getCode().substring(0, 1));
		} else {
			String lastCatalog = list.get(position - 1).getCode()
					.substring(0, 1);
			if (mContent.getCode().substring(0, 1).equals(lastCatalog)) {
				viewHolder.tvLetter.setVisibility(View.GONE);
			} else {
				viewHolder.tvLetter.setVisibility(View.VISIBLE);
				viewHolder.tvLetter.setText(mContent.getCode().substring(0, 1));
			}
		}

		viewHolder.tvLetter.setOnClickListener(null);
		view.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {
				callback.reLocation(list.get(position).getCityName(), list.get(position).getCid());
			}
		});
		viewHolder.tvTitle.setText(this.list.get(position).getCityName());
		return view;

	}

	final static class ViewHolder {
		TextView tvTitle;
		TextView tvLetter;
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
			mContent = (Body) list.get(i);
			l = mContent.getCode().substring(0, 1);
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