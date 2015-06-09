package com.allthelucky.common.view.network;

import java.util.List;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import com.allthelucky.common.view.ImageIndicatorView;
import com.company.ilunch.R;
import com.company.ilunch.IlunchApplication;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * Network ImageIndicatorView, by urls
 * 
 * @author
 * 
 */
public class NetworkImageIndicatorView extends ImageIndicatorView {
	private ImageLoader imageLoader;

	public NetworkImageIndicatorView(Context context, AttributeSet attrs) {
		super(context, attrs);

		imageLoader = ImageLoader.getInstance();
	}

	public NetworkImageIndicatorView(Context context) {
		super(context);

		imageLoader = ImageLoader.getInstance();
	}

	/**
	 * 设置显示图片URL列表
	 * 
	 * @param urlList
	 *            URL列表
	 */
	public void setupLayoutByImageUrl(final List<String> urlList) {
		if (urlList == null)
			throw new NullPointerException();

		final int len = urlList.size();
		if (len > 0) {
			for (int index = 0; index < len; index++) {
				final ImageView pageItem = new ImageView(getContext());
				pageItem.setLayoutParams(new LayoutParams(
						LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
				pageItem.setScaleType(ScaleType.FIT_XY);
				imageLoader.displayImage(urlList.get(index), pageItem,
						IlunchApplication.initDisplayOptions(true,
								R.drawable.img_loading));
				addViewItem(pageItem);
			}
		}
	}
}
