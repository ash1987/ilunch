package com.company.ilunch.adapter;

import java.util.ArrayList;
import com.company.ilunch.R;
import com.company.ilunch.bean.GetAddressListBean.Body;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * 地址列表适配器
 */
public class MyAddressListAdapter extends BaseAdapter {
	private Context mContext;
	private ArrayList<Body> dataList;
	private DeleCallback delCallback;

	public MyAddressListAdapter(Context mContext, ArrayList<Body> dataList,
			DeleCallback delCallback) {
		this.mContext = mContext;
		this.dataList = dataList;
		this.delCallback = delCallback;
	}
	
	public interface DeleCallback {
		public void deleteItem(int pos);
	}

	@Override
	public int getCount() {
		if (dataList == null) {
			return 0;
		}
		return dataList.size();
	}

	@Override
	public Object getItem(int arg0) {
		return null;
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@Override
	public View getView(final int position, View converView, ViewGroup arg2) {
		ViewHolder holder = new ViewHolder();
		if (converView == null) {
			converView = LayoutInflater.from(mContext).inflate(
					R.layout.myaddr_list_item_layout, null);
			holder.addrTv = (TextView) converView.findViewById(R.id.addrTv);
			holder.moreIv = (ImageView) converView.findViewById(R.id.moreIv);
			holder.deleteRl = (RelativeLayout) converView.findViewById(R.id.deleteRl);
			converView.setTag(holder);
		} else {
			holder = (ViewHolder) converView.getTag();
		}

		holder.addrTv.setText(dataList.get(position).getBuildingName());
		holder.deleteRl.setOnClickListener(new delOnclick(position));

		return converView;
	}
	
	class delOnclick implements OnClickListener {
		private int pos;
		
		delOnclick(int pos) {
			this.pos = pos;
		}
		
		@Override
		public void onClick(View v) {
			delCallback.deleteItem(pos);
		}
	}

	private static class ViewHolder {
		private TextView addrTv;// 地址
		private ImageView moreIv;
		private RelativeLayout deleteRl;
	}
}
