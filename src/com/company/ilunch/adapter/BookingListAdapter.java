package com.company.ilunch.adapter;

import java.util.ArrayList;
import com.company.ilunch.R;
import com.company.ilunch.bean.GetCartListBean;
import com.company.ilunch.bean.GetFoodListBean.Body;
import com.company.ilunch.utils.AndroidUtils;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * 打包/订座adapter
 */
public class BookingListAdapter extends BaseAdapter {
	private Context mContext;
	private Callback callback;
	private LayoutInflater inflater;
	private ArrayList<Body> data;
	private ArrayList<GetCartListBean.Body> cartList;
	private ImageLoader imageLoader;
	private DisplayImageOptions options;

	public BookingListAdapter(Context mContext, ArrayList<Body> list,
			ArrayList<GetCartListBean.Body> cartList, Callback callback) {
		this.mContext = mContext;
		this.data = list;
		this.cartList = cartList;
		this.callback = callback;
		inflater = LayoutInflater.from(mContext);
		this.imageLoader = ImageLoader.getInstance();
		options = new DisplayImageOptions.Builder().cacheInMemory(true)
				.showImageOnLoading(R.drawable.img_loading)
				.showImageOnFail(R.drawable.img_loading)
				.showImageForEmptyUri(R.drawable.img_loading).cacheOnDisc(true)
				.resetViewBeforeLoading(true).considerExifParams(true)
				.bitmapConfig(Bitmap.Config.RGB_565)
				.imageScaleType(ImageScaleType.EXACTLY).build();
	}

	public interface Callback {
		public void addToCart(Body body);

		public void updateCart(Body body, int num);

		public void addComment(Body body);

		public void addFav(Body body);
		
		public void delFav(Body body);

		public void deleCart(Body body);

		public void ShowFoodList(Body body);
	}

	@Override
	public int getCount() {
		return data.size();
	}

	@Override
	public Object getItem(int position) {
		return data.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View converView, ViewGroup viewGroup) {
		ViewHolder holder = new ViewHolder();
		// if (converView == null) {
		converView = inflater.inflate(R.layout.food_list_item_layout, null);

		holder.good_iv = (ImageView) converView.findViewById(R.id.good_iv);
		holder.addToCartIv = (ImageView) converView
				.findViewById(R.id.addToCartIv);
		holder.up_num_ib = (ImageButton) converView
				.findViewById(R.id.up_num_ib);
		holder.num_tv = (TextView) converView.findViewById(R.id.num_tv);
		holder.down_num_ib = (ImageButton) converView
				.findViewById(R.id.down_num_ib);
		holder.favIv = (ImageView) converView.findViewById(R.id.favIv);
		holder.commentIv = (ImageView) converView.findViewById(R.id.commentIv);
		holder.commentTv = (TextView) converView.findViewById(R.id.commentTv);
		holder.good_name_tv = (TextView) converView
				.findViewById(R.id.good_name_tv);
		holder.good_mall_price_tv = (TextView) converView
				.findViewById(R.id.good_mall_price_tv);
		holder.priceTv = (TextView) converView.findViewById(R.id.priceTv);
		holder.hasMaiTv = (TextView) converView.findViewById(R.id.hasMaiTv);
		holder.goodRl = (RelativeLayout) converView.findViewById(R.id.goodRl);

		// converView.setTag(holder);
		// } else {
		// holder = (ViewHolder) converView.getTag();
		// }

		converView.setTag(data.get(position));
		imageLoader
				.displayImage(AndroidUtils.getImgUrlOnServer(data.get(position)
						.getPicture()), holder.good_iv, options);
		if (Boolean.parseBoolean(data.get(position).getIsCollected())) {
			holder.favIv.setImageResource(R.drawable.icon_heart00_s);
		} else {
			holder.favIv.setImageResource(R.drawable.icon_heart01_s);
		}

		holder.commentIv.setOnClickListener(new AddComment(data.get(position),
				callback));
		holder.favIv
				.setOnClickListener(new AddOrDelFav(data.get(position), callback));
		holder.goodRl.setOnClickListener(new ShowMerchantFoodList(data
				.get(position), callback));

		if (!TextUtils.isEmpty(data.get(position).getCommentNum())) {
			holder.commentTv.setText(data.get(position).getCommentNum());
		} else {
			holder.commentTv.setText("0");
		}

		holder.addToCartIv.setVisibility(View.VISIBLE);
		holder.num_tv.setVisibility(View.GONE);
		holder.up_num_ib.setVisibility(View.GONE);
		holder.down_num_ib.setVisibility(View.GONE);

		if (cartList != null && cartList.size() > 0) {
			for (int i = 0; i < cartList.size(); i++) {
				GetCartListBean.Body bean = cartList.get(i);
				if (bean.getId().equals(data.get(position).getUnid())) {
					holder.num_tv.setText(bean.getNum());

					holder.addToCartIv.setVisibility(View.GONE);
					holder.num_tv.setVisibility(View.VISIBLE);
					holder.up_num_ib.setVisibility(View.VISIBLE);
					holder.down_num_ib.setVisibility(View.VISIBLE);
				}
			}
		}

		holder.good_name_tv.setText(data.get(position).getFoodName());

		holder.good_mall_price_tv.setText(data.get(position).getName());

		holder.priceTv.setText("￥" + data.get(position).getPrice());

		holder.hasMaiTv.setText(String.format(mContext
				.getString(R.string.ramain_num), data.get(position)
				.getRemains()));

		holder.addToCartIv.setOnClickListener(new AddOnclick(holder, data
				.get(position), callback));

		holder.up_num_ib.setOnClickListener(new UpNumOnclick(holder, data
				.get(position), callback));

		holder.down_num_ib.setOnClickListener(new DownNumOnclick(holder, data
				.get(position), callback));

		return converView;
	}

	public class ShowMerchantFoodList implements OnClickListener {
		private Body body;
		private Callback callback;

		ShowMerchantFoodList(Body body, Callback callback) {
			this.body = body;
			this.callback = callback;
		}

		@Override
		public void onClick(View v) {
			callback.ShowFoodList(body);
		}
	}

	public class AddComment implements OnClickListener {
		private Body body;
		private Callback callback;

		AddComment(Body body, Callback callback) {
			this.body = body;
			this.callback = callback;
		}

		@Override
		public void onClick(View v) {
			callback.addComment(body);
		}
	}

	public class AddOrDelFav implements OnClickListener {
		private Body body;
		private Callback callback;

		AddOrDelFav(Body body, Callback callback) {
			this.body = body;
			this.callback = callback;
		}

		@Override
		public void onClick(View v) {
			if(Boolean.parseBoolean(body.getIsCollected())) {
				callback.delFav(body);
			} else {
				callback.addFav(body);
			}
		}
	}

	public class AddOnclick implements OnClickListener {
		private ViewHolder holder;
		private Callback callback;
		private Body body;

		public AddOnclick(ViewHolder holder, Body body, Callback callback) {
			this.holder = holder;
			this.body = body;
			this.callback = callback;
		}

		@Override
		public void onClick(View v) {
			holder.addToCartIv.setVisibility(View.GONE);
			holder.num_tv.setVisibility(View.VISIBLE);
			holder.up_num_ib.setVisibility(View.VISIBLE);
			holder.down_num_ib.setVisibility(View.VISIBLE);

			holder.num_tv.setText("1");

			callback.addToCart(body);
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
			callback.updateCart(body,
					Integer.parseInt(holder.num_tv.getText().toString().trim()));
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
				holder.addToCartIv.setVisibility(View.VISIBLE);
				holder.num_tv.setVisibility(View.GONE);
				holder.up_num_ib.setVisibility(View.GONE);
				holder.down_num_ib.setVisibility(View.GONE);

				callback.deleCart(body);
				return;
			}

			holder.num_tv.setText(String.valueOf(Integer.parseInt(holder.num_tv
					.getText().toString()) - 1));
			callback.updateCart(body,
					Integer.parseInt(holder.num_tv.getText().toString().trim()));
		}
	}

	private static class ViewHolder {
		private ImageView good_iv;
		private ImageView addToCartIv;
		private ImageButton up_num_ib;
		private TextView num_tv;
		private ImageButton down_num_ib;
		private TextView commentTv;
		private TextView good_name_tv;
		private TextView good_mall_price_tv;
		private TextView priceTv;
		private TextView hasMaiTv;
		private ImageView commentIv;
		private ImageView favIv;
		private RelativeLayout goodRl;
	}
}