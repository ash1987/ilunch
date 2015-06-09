package com.company.ilunch.adapter;

import java.util.ArrayList;

import com.company.ilunch.R;
import com.company.ilunch.bean.GetCartListBean;
import com.company.ilunch.utils.AndroidUtils;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * 确认订单页面物品适配器
 */
public class EnSureOrderFoodListAdapter extends BaseAdapter {

	private Context mContext;
	private ArrayList<GetCartListBean.Body> dataList;
	private ImageLoader imageLoader;
	private DisplayImageOptions options;

	public EnSureOrderFoodListAdapter(Context mContext, ArrayList<GetCartListBean.Body> dataList) {
		this.mContext = mContext;
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
	public View getView(int position, View converView, ViewGroup group) {
		ViewHolder holder = new ViewHolder();
		if (converView == null) {
			converView = LayoutInflater.from(mContext).inflate(
					R.layout.ensure_order_food_list_layout, null);
			holder.good_iv = (ImageView) converView.findViewById(R.id.good_iv);
			holder.good_name_tv = (TextView) converView.findViewById(R.id.good_name_tv);
			holder.good_num_tv = (TextView) converView.findViewById(R.id.good_num_tv);
			holder.good_price_tv = (TextView) converView.findViewById(R.id.good_price_tv);
			converView.setTag(holder);
		} else {
			holder = (ViewHolder) converView.getTag();
		}
		
		imageLoader.displayImage(AndroidUtils.getImgUrlOnServer(dataList.get(
				position).getPicture()), holder.good_iv, options);
		holder.good_name_tv.setText(dataList.get(position).getName());
		holder.good_num_tv.setText(String.format(mContext.getString(R.string.num_fen1), dataList.get(position).getNum()));
		holder.good_price_tv.setText(String.format(mContext.getString(R.string.cart_product_price), dataList.get(position).getPrice()));
		
		return converView;
	}

	private static class ViewHolder {
		private ImageView good_iv;
		private TextView good_name_tv;
		private TextView good_num_tv;
		private TextView good_price_tv;
	}
}
