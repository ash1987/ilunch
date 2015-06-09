package com.company.ilunch.xunhuanviewpager;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.company.ilunch.base.WebViewBaseActivity;

public class InfiniteLoopViewPager extends ViewPager {
	private WebViewBaseActivity act;
	private Handler handler;
	
	public InfiniteLoopViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public InfiniteLoopViewPager(Context context) {
		super(context);
	}

	@Override
	public void setAdapter(PagerAdapter adapter) {
		super.setAdapter(adapter);
		//设置当前展示的位置
		setCurrentItem(0);
	}
	
	public void setInfinateAdapter(Activity act,Handler handler,PagerAdapter adapter){
		this.act = (WebViewBaseActivity)act;
		this.handler = handler;
		setAdapter(adapter);
	}
	
	@Override
	public void setCurrentItem(int item) {
		item = getOffsetAmount() + (item % getAdapter().getCount());
		super.setCurrentItem(item);
	}
	/**
	 * 从0开始向左可以滑动的次数
	 * @return
	 */
	private int getOffsetAmount() {
		if (getAdapter() instanceof InfiniteLoopViewPagerAdapter) {
			InfiniteLoopViewPagerAdapter infiniteAdapter = (InfiniteLoopViewPagerAdapter) getAdapter();
			// 允许向前滚动100000次
			return infiniteAdapter.getRealCount() * 100000;
		} else {
			return 0;
		}
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		return super.onInterceptTouchEvent(ev);
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		int action = ev.getAction();
		if (action == MotionEvent.ACTION_DOWN) {
			act.isRun = false;
			act.isDown = true;
			handler.removeCallbacksAndMessages(null);
		} else if (action == MotionEvent.ACTION_MOVE) {
			act.isDown = true;
			act.isRun = false;
			handler.removeCallbacksAndMessages(null);
		} else if (action == MotionEvent.ACTION_UP) {
			act.isRun = true;
			act.isDown = false;
			handler.removeCallbacksAndMessages(null);
			handler.sendEmptyMessageDelayed(1, 500);
		}
		return super.dispatchTouchEvent(ev);
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		return super.onTouchEvent(ev);
	}
	
	@Override
	public void setOffscreenPageLimit(int limit) {
		super.setOffscreenPageLimit(limit);
	}
}
