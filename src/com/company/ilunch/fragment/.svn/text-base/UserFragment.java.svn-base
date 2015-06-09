package com.company.ilunch.fragment;

import java.io.File;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.company.ilunch.R;
import com.company.ilunch.IlunchApplication;
import com.company.ilunch.preferences.LoginPreference;
import com.company.ilunch.ui.AddressManagerActivity;
import com.company.ilunch.ui.LoginActivity;
import com.company.ilunch.ui.MyCommentActivity;
import com.company.ilunch.ui.MyFavActivity;
import com.company.ilunch.ui.MyOrderActivity;
import com.company.ilunch.ui.MyOrdersActivity;
import com.company.ilunch.ui.SettingActivity;
import com.company.ilunch.utils.FileUtils;
import com.company.ilunch.widget.CustomImageView;
import com.nostra13.universalimageloader.core.ImageLoader;

/*
 * 个人中心
 */
public class UserFragment extends Fragment implements OnClickListener {
	public static final String FRAGMENT_TAG = UserFragment.class
			.getSimpleName();

	private static final int PICKREPORT = 0x0a;
	private static final int TAKEREPORT = 0x0b;
	private static final int ZOOMPORT = 0x0c;
	private static final int RESULT_OK = -1;

	private LoginPreference loginPreference;
	private ImageLoader imageLoader;
	private ImageButton settingIb;// 设置
	private Button loginBt;// 登录
	private CustomImageView userCIV;// 用户头像
	private TextView userTv;// 用户名称
	private TextView loginDescTv;//
	private RelativeLayout scAddressRl;// 收货地址
	private RelativeLayout myOrderRl;// 我的订单
	private RelativeLayout myFavRl;// 我的收藏
	private RelativeLayout myCommentRl;// 我的评论
	private RelativeLayout kfCenterRl;// 客服中心

	// 修改头像
	private View updateUserPicView;// 上传头像窗口
	private Dialog updataUserPicDialog;
	private Button btnTakePhoto;
	private Button btnPickPhoto;
	private Button btnCancel;

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.user_fragment_layout, null);

		initData();
		initView(view);
		setAttribute();

		return view;
	}

	private void initData() {
		this.imageLoader = ImageLoader.getInstance();
		loginPreference = new LoginPreference(UserFragment.this.getActivity());
	}

	// 初始化控件
	private void initView(View view) {
		settingIb = (ImageButton) view.findViewById(R.id.settingIb);
		loginBt = (Button) view.findViewById(R.id.loginBt);
		userCIV = (CustomImageView) view.findViewById(R.id.userCIV);
		userTv = (TextView) view.findViewById(R.id.userTv);
		loginDescTv = (TextView) view.findViewById(R.id.loginDescTv);
		scAddressRl = (RelativeLayout) view.findViewById(R.id.scAddressRl);
		myOrderRl = (RelativeLayout) view.findViewById(R.id.myOrderRl);
		myFavRl = (RelativeLayout) view.findViewById(R.id.myFavRl);
		myCommentRl = (RelativeLayout) view.findViewById(R.id.myCommentRl);
		kfCenterRl = (RelativeLayout) view.findViewById(R.id.kfCenterRl);

		updateUserPicView = UserFragment.this.getActivity().getLayoutInflater()
				.inflate(R.layout.picture_popupwindow, null);
		btnTakePhoto = (Button) updateUserPicView
				.findViewById(R.id.btn_take_photo);
		btnPickPhoto = (Button) updateUserPicView
				.findViewById(R.id.btn_pick_photo);
		btnCancel = (Button) updateUserPicView.findViewById(R.id.btn_cancel);
	}

	// 设置控件属性
	private void setAttribute() {
		settingIb.setOnClickListener(this);
		loginBt.setOnClickListener(this);
		scAddressRl.setOnClickListener(this);
		myOrderRl.setOnClickListener(this);
		myFavRl.setOnClickListener(this);
		myCommentRl.setOnClickListener(this);
		kfCenterRl.setOnClickListener(this);
		userCIV.setOnClickListener(this);
		btnTakePhoto.setOnClickListener(this);
		btnPickPhoto.setOnClickListener(this);
		btnCancel.setOnClickListener(this);
		
		// 初始化修改头像对话框
		InitUserPicDialog();
	}

	@Override
	public void onResume() {
		super.onResume();

		if (loginPreference.getLoginState()) {
			// 登录
			loginDescTv.setVisibility(View.GONE);
			loginBt.setVisibility(View.GONE);
			userCIV.setVisibility(View.VISIBLE);
			userTv.setVisibility(View.VISIBLE);

			imageLoader.displayImage(loginPreference.getPicture(), userCIV,
					IlunchApplication.initDisplayOptions(true,
							R.drawable.ic_launcher));
		} else {
			// 未登录
			loginDescTv.setVisibility(View.VISIBLE);
			loginBt.setVisibility(View.VISIBLE);
			userCIV.setVisibility(View.GONE);
			userTv.setVisibility(View.GONE);
		}

		userTv.setText(loginPreference.getUserName());
	}

	/**
	 * 图片裁剪大图方法 裁剪图片宽高获得新图片〉 图片
	 */

	public void startPhotoZoom(Uri uri, Uri imageUri) {
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/*");
		// 下面这个crop=true是设置在开启的Intent中设置显示的VIEW可裁剪
		intent.putExtra("crop", "true");
		// aspectX aspectY 是宽高的比例
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		// outputX outputY 是裁剪图片宽高
		intent.putExtra("outputX", 640);
		intent.putExtra("outputY", 640);
		intent.putExtra("return-data", false);
		intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
		intent.putExtra("noFaceDetection", true);
		intent.putExtra("scale", true);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
		startActivityForResult(intent, ZOOMPORT);
	}

	public void ImagePath(String path) {
		File temp = new File(path);
		Uri imageUri = Uri.parse("file://" + path);
		startPhotoZoom(Uri.fromFile(temp), imageUri);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		switch (requestCode) {
		case PICKREPORT:
			if (resultCode == RESULT_OK) {
				Uri imageUri = Uri.parse("file://"
						+ Environment.getExternalStorageDirectory()
						+ FileUtils.getImagePath() + File.separator
						+ loginPreference.getUserName() + ".jpg");
				startPhotoZoom(data.getData(), imageUri);
			}
			break;
		case TAKEREPORT:
			if (resultCode == RESULT_OK) {
				ImagePath(Environment.getExternalStorageDirectory()
						+ FileUtils.getImagePath() + File.separator
						+ loginPreference.getUserName() + ".jpg");
			}
			break;
		case ZOOMPORT:
			if (resultCode == RESULT_OK) {
				
			}
			break;
		}
	}

	/*
	 * 修改头像初始化
	 */
	private void InitUserPicDialog() {
		updataUserPicDialog = new Dialog(UserFragment.this.getActivity(), R.style.ShareDialog);
		updataUserPicDialog.setContentView(updateUserPicView, new LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
		Window window = updataUserPicDialog.getWindow();
		// 设置显示动画
		window.setWindowAnimations(R.style.shareAnimation);
		window.setGravity(Gravity.BOTTOM | Gravity.LEFT | Gravity.RIGHT);
		// 设置点击外围消散
		updataUserPicDialog.setCanceledOnTouchOutside(true);
	}

	@Override
	public void onClick(View v) {
		if (v == null) {
			return;
		}

		switch (v.getId()) {
		case R.id.settingIb:
			startActivity(new Intent(UserFragment.this.getActivity(),
					SettingActivity.class));
			break;
		case R.id.loginBt:
			startActivity(new Intent(UserFragment.this.getActivity(),
					LoginActivity.class));
			break;
		case R.id.scAddressRl:
			startActivity(new Intent(UserFragment.this.getActivity(),
					AddressManagerActivity.class));
			break;
		case R.id.myOrderRl:
			startActivity(new Intent(UserFragment.this.getActivity(),
					MyOrdersActivity.class));
			break;
		case R.id.myFavRl:
			startActivity(new Intent(UserFragment.this.getActivity(),
					MyFavActivity.class));
			break;
		case R.id.myCommentRl:
			startActivity(new Intent(UserFragment.this.getActivity(),
					MyCommentActivity.class));
			break;
		case R.id.kfCenterRl:

			break;
		case R.id.userCIV:
			updataUserPicDialog.show();
			break;
		case R.id.btn_take_photo:
			updataUserPicDialog.dismiss();
			Intent takeIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
			String filePath = Environment.getExternalStorageDirectory()
					+ FileUtils.getImagePath() + File.separator
					+ loginPreference.getUserName() + ".jpg";
			takeIntent.putExtra(MediaStore.EXTRA_OUTPUT,
					Uri.fromFile(new File(filePath)));
			startActivityForResult(takeIntent, TAKEREPORT);
			break;
		case R.id.btn_pick_photo:
			updataUserPicDialog.dismiss();
			Intent pickIntent = new Intent(Intent.ACTION_PICK, null);
			pickIntent.setDataAndType(
					MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
			startActivityForResult(pickIntent, PICKREPORT);
			break;
		case R.id.btn_cancel:
			updataUserPicDialog.dismiss();
			break;
		default:
			break;
		}
	}
}