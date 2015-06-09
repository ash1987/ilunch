package com.company.ilunch.base;

import android.app.Dialog;
import android.support.v4.app.Fragment;
import android.widget.TextView;

import com.company.ilunch.R;

/**
 * 版权：融贯资讯 <br/>
 * 作者：xingjian.lan@rogrand.com <br/>
 * 生成日期：2014-4-16 <br/>
 * 描述：Frament基类
 */

public abstract class BaseFragment extends Fragment {
	private Dialog mProgressDialog;
	
	/**
	 * 显示等待框
	 * 
	 * @param title
	 * @param message
	 */
	public void showProgress(String title, String message) {
		try {
			if (mProgressDialog != null && mProgressDialog.isShowing()) {
				return;
			}
			mProgressDialog = new Dialog(this.getActivity(), R.style.CustomDialog);
			mProgressDialog.setContentView(R.layout.dialog_progress);
			TextView textView = (TextView) mProgressDialog
					.findViewById(R.id.progress_msg);
			textView.setText(message);
			mProgressDialog.show();
		} catch (OutOfMemoryError e) {
			e.printStackTrace();
		}
	}

	/**
	 * 取消等待框
	 */
	public void dismissProgress() {
		if (mProgressDialog != null && mProgressDialog.isShowing()) {
			try {
				mProgressDialog.dismiss();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
