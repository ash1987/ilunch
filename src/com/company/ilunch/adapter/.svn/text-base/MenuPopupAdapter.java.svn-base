package com.company.ilunch.adapter;

import java.util.ArrayList;
import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.company.ilunch.R;
import com.company.ilunch.IlunchApplication;
import com.company.ilunch.bean.GetShopDataBean.Body;
import com.company.ilunch.utils.AndroidUtils;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

/**
 * 菜单列表适配器
 */
public class MenuPopupAdapter extends BaseAdapter {

	private Context mContext;
	private LayoutInflater inflater;
	private ArrayList<Body> dataList;
	private ImageLoader imageLoader;
	private DisplayImageOptions options;

	public MenuPopupAdapter(Context mContext, ArrayList<Body> dataList) {
		this.mContext = mContext;
		inflater = LayoutInflater.from(mContext);

		this.dataList = dataList;

		this.imageLoader = ImageLoader.getInstance();

		options = new DisplayImageOptions.Builder().cacheInMemory(true)
				.showImageOnLoading(R.drawable.menu_f_icon)
				.showImageOnFail(R.drawable.menu_f_icon)
				.showImageForEmptyUri(R.drawable.menu_f_icon).cacheOnDisc(true)
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
	public View getView(int position, View converView, ViewGroup viewGroup) {
		ViewHolder holder = new ViewHolder();
		if (converView == null) {
			converView = inflater
					.inflate(R.layout.menu_popup_item_layout, null);
			holder.ItemImage = (ImageView) converView
					.findViewById(R.id.ItemImage);
			holder.ItemText = (TextView) converView.findViewById(R.id.ItemText);

			converView.setTag(holder);
		} else {
			holder = (ViewHolder) converView.getTag();
		}

		holder.ItemText.setText(dataList.get(position).getClassname());
		if (mContext.getString(R.string.menu_sc).equals(
				dataList.get(position).getClassname())) {
			// 收藏
			holder.ItemImage.setImageResource(R.drawable.menu_fav_icon);
		} else {
			if (TextUtils.isEmpty(dataList.get(position).getClassname())) {
				holder.ItemImage.setImageResource(R.drawable.menu_f_icon);
			} else {
				imageLoader.displayImage(
						AndroidUtils.getImgUrlOnServer(dataList.get(position)
								.getPicture()), holder.ItemImage, options);
			}
		}

		return converView;
	}

	private static class ViewHolder {
		private ImageView ItemImage;
		private TextView ItemText;
	}
}