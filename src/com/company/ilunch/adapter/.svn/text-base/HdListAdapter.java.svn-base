package com.company.ilunch.adapter;

import java.util.ArrayList;
import com.company.ilunch.R;
import com.company.ilunch.bean.GetInteractionListBean.Body;
import com.company.ilunch.widget.CustomImageView;
import com.nostra13.universalimageloader.core.ImageLoader;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * 互动列表适配器
 */
public class HdListAdapter extends BaseAdapter {
	private Context mContext;
	private ArrayList<Body> dataList;
	private ImageLoader imageLoader;
	
	public HdListAdapter(Context mContext, ArrayList<Body> dataList) {
		this.mContext = mContext;
		this.dataList = dataList;
		
		imageLoader = ImageLoader.getInstance();
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
					R.layout.hd_list_item_layout, null);
			holder.userCIV = (CustomImageView) converView
					.findViewById(R.id.userCIV);
			holder.userNameTv = (TextView) converView
					.findViewById(R.id.userNameTv);
			holder.contentTv = (TextView) converView
					.findViewById(R.id.contentTv);
			converView.setTag(holder);
		} else {
			holder = (ViewHolder) converView.getTag();
		}
		
//		imageLoader.displayImage(dataList.get(position).get, holder.userCIV,
//				WeetApplication.initDisplayOptions(true,
//						R.drawable.ic_launcher));
		holder.userNameTv.setText(dataList.get(position).getUserName());
		holder.contentTv.setText(dataList.get(position).getConent());
		
		return converView;
	}

	private static class ViewHolder {
		private CustomImageView userCIV;
		private TextView userNameTv;
		private TextView contentTv;
	}
}
