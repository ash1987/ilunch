package com.company.ilunch.adapter;

import com.company.ilunch.R;
import com.company.ilunch.widget.CustomImageView;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

/**
 * 没事侦探adapter
 */
public class MsztListAdapter extends BaseAdapter {

	private Context mContext;
	private LayoutInflater inflater;

	public MsztListAdapter(Context mContext) {
		this.mContext = mContext;
		inflater = LayoutInflater.from(mContext);
	}

	@Override
	public int getCount() {
		return 10;
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View converView, ViewGroup viewGroup) {
		ViewHolder holder = new ViewHolder();
		if (converView == null) {
			converView = inflater.inflate(R.layout.mszt_list_layout, null);

			holder.userCIV = (CustomImageView) converView
					.findViewById(R.id.userCIV);

			converView.setTag(holder);
		} else {
			holder = (ViewHolder) converView.getTag();
		}

		return converView;
	}

	private static class ViewHolder {
		private CustomImageView userCIV;
	}
}