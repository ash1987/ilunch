package com.company.ilunch.adapter;

import java.util.ArrayList;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.company.ilunch.R;
import com.company.ilunch.bean.GetCartListBean.Body;
import com.company.ilunch.utils.AndroidUtils;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

/**
 * 购物车列表适配器
 */
public class ShopCartListAdapter extends BaseAdapter {

	private Context mContext;
	private Callback callback;
	private ArrayList<Body> dataList;
	private ImageLoader imageLoader;
	private DisplayImageOptions options;

	public interface Callback {
		public void updateCart(Body body, int num);
		public void deleItem(Body body);
	}
	
	public ShopCartListAdapter(Context mContext, ArrayList<Body> dataList, Callback callback) {
		this.mContext = mContext;
		this.callback = callback;
		this.dataList = dataList;
		imageLoader = ImageLoader.getInstance();
		options = new DisplayImageOptions.Builder().cacheInMemory(true)
				.showImageOnLoading(R.drawable.img_loading)
				.showImageOnFail(R.drawable.img_loading)
				.showImageForEmptyUri(R.drawable.img_loading).cacheOnDisc(true)
				.resetViewBeforeLoading(true).considerExifParams(true)
				.bitmapConfig(Bitmap.Config.RGB_565)
				.imageScaleType(ImageScaleType.EXACTLY).build();
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
	public View getView(final int position, View converView, ViewGroup group) {
		ViewHolder holder = new ViewHolder();
		if (converView == null) {
			converView = LayoutInflater.from(mContext).inflate(
					R.layout.shopcart_item_popup_layout, null);
			holder.good_iv = (ImageView) converView.findViewById(R.id.good_iv);
			holder.good_name_tv = (TextView) converView
					.findViewById(R.id.good_name_tv);
			holder.up_num_ib = (ImageButton) converView
					.findViewById(R.id.up_num_ib);
			holder.num_tv = (TextView) converView.findViewById(R.id.num_tv);
			holder.down_num_ib = (ImageButton) converView
					.findViewById(R.id.down_num_ib);
			holder.deleteRl = (RelativeLayout) converView
					.findViewById(R.id.deleteRl);
			
			converView.setTag(holder);
		} else {
			holder = (ViewHolder) converView.getTag();
		}
		
		imageLoader.displayImage(AndroidUtils.getImgUrlOnServer(dataList.get(
				position).getPicture()), holder.good_iv, options);
		holder.num_tv.setText(dataList.get(position).getNum());
		holder.good_name_tv.setText(dataList.get(position).getName());
		holder.up_num_ib.setOnClickListener(new UpNumOnclick(holder, dataList.get(position), callback));
		holder.down_num_ib.setOnClickListener(new DownNumOnclick(holder, dataList.get(position), callback));
		holder.deleteRl.setOnClickListener(new delOnclick(dataList.get(position)));

		return converView;
	}
	
	class delOnclick implements OnClickListener {
		private Body body;

		delOnclick(Body body) {
			this.body = body;
		}

		@Override
		public void onClick(View v) {
			callback.deleItem(body);
		}
	}
	
	public class UpNumOnclick implements OnClickListener {
		private ViewHolder holder;
		private Body body;
		private Callback callback;

		public UpNumOnclick(ViewHolder holder, Body body, Callback callback) {
			this.holder = holder;
			this.body = body;
			this.callback = callback;
		}

		@Override
		public void onClick(View v) {
			holder.num_tv.setText(String.valueOf(Integer.parseInt(holder.num_tv
					.getText().toString()) + 1));
			callback.updateCart(body, Integer.parseInt(holder.num_tv.getText().toString().trim()));
		}
	}

	public class DownNumOnclick implements OnClickListener {
		private ViewHolder holder;
		private Body body;
		private Callback callback;
		
		public DownNumOnclick(ViewHolder holder, Body body, Callback callback) {
			this.holder = holder;
			this.body = body;
			this.callback = callback;
		}

		@Override
		public void onClick(View v) {
			if (Integer.parseInt(holder.num_tv.getText().toString()) == 1) {
				callback.deleItem(body);
				return;
			}

			holder.num_tv.setText(String.valueOf(Integer.parseInt(holder.num_tv
					.getText().toString()) - 1));
			callback.updateCart(body, Integer.parseInt(holder.num_tv.getText().toString().trim()));
		}
	}

	private static class ViewHolder {
		private ImageView good_iv;
		private TextView good_name_tv;
		private ImageButton up_num_ib;
		private TextView num_tv;
		private ImageButton down_num_ib;
		private RelativeLayout deleteRl;
	}
}