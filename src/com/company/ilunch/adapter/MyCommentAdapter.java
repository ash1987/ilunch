package com.company.ilunch.adapter;

import java.util.ArrayList;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.company.ilunch.R;
import com.company.ilunch.bean.CommentBean.Body;
import com.company.ilunch.utils.AndroidUtils;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

/**
 * 我的点评列表适配器
 */
public class MyCommentAdapter extends BaseAdapter {

	private Context mContext;
	private ArrayList<Body> commentLists;
	private Callback callback;
	private ImageLoader imageLoader;
	private DisplayImageOptions options;

	public interface Callback {
		public void deleItem(int pos);
	}

	public MyCommentAdapter(Context mContext, ArrayList<Body> commentLists,
			Callback delCallback) {
		this.mContext = mContext;
		this.commentLists = commentLists;
		this.callback = delCallback;

		this.imageLoader = ImageLoader.getInstance();
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
		if (commentLists == null) {
			return 0;
		}
		return commentLists.size();
	}

	@Override
	public Object getItem(int arg0) {
		return commentLists.get(arg0);
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
					R.layout.mycomment_item_layout, null);
			holder.commentImage = (ImageView) converView
					.findViewById(R.id.commentImage);
			holder.commentGoodName = (TextView) converView
					.findViewById(R.id.commentGoodName);
			holder.commentContentTv = (TextView) converView
					.findViewById(R.id.commentContentTv);
			holder.deleteRl = (RelativeLayout) converView
					.findViewById(R.id.deleteRl);
			
			converView.setTag(holder);
		} else {
			holder = (ViewHolder) converView.getTag();
		}
		
		imageLoader
		.displayImage(AndroidUtils.getImgUrlOnServer(commentLists.get(position)
				.getPicture()), holder.commentImage, options);
		
		holder.commentGoodName.setText(commentLists.get(position).getFoodName());
		holder.commentContentTv.setText(commentLists.get(position).getComment());
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
			callback.deleItem(pos);
		}
	}

	private static class ViewHolder {
		private ImageView commentImage;// 商品图片
		private TextView commentGoodName;// 商品名称
		private TextView commentContentTv;
		private RelativeLayout deleteRl;
	}
}
