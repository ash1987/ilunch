package com.company.ilunch.net;

/**
 * 描述：接口管理
 */

public class HttpUrlManager {
	public final static String ROOT_HTTP_URL = "http://www.ilunch.com.cn";
	// 根域名
	public final static String HTTP_URL = "http://www.ilunch.com.cn/awcwcf/Service1.svc";
	//用户登陆
	public final static String LOGIN_STRING = "UserLogin";
	//获取菜品分类
	public final static String GET_SHOP_DATA_STRING = "GetShopData";
	//获取菜品列表
	public final static String GET_FOOD_LIST_STRING = "GetFoodList";
	//获取城市列表
	public final static String GET_CITY_LIST_STRING = "GetCity";
	//獲取區域列表
	public final static String GET_SECTION_LIST_STRING = "GetSection";
	//獲取大廈列表 
	public final static String GET_BUILDING_LIST_STRING = "GetBuilding";
	//提交互动
	public final static String ADD_INTERACTION_STRING = "AddInteraction";
	//获取评论列表
	public final static String GET_INTERACTION_LIST_STRING = "GetInteractionList";
	//获取收藏列表
	public final static String GET_MY_COLLECT_STRING = "GetMyCollect";
	//添加地址
	public final static String ADD_ADD_MY_ADDRESS_STRING = "AddMyAddress";
	//获取地址列表
	public final static String GET_MY_ADDRESS_STRING = "GetMyAddress";
	//删除收获地址
	public final static String DEL_MY_ADDRESS_STRING = "DelMyAddress";
	//更新地址
	public final static String UPDATE_MY_ADDRESS_STRING = "UpdateMyAddress";
	//加入购物车
	public final static String ADD_TO_CART_STRING = "AddToCart";
	//更新购物车
	public final static String UPDATE_CART_STRING = "UpdateCart";
	//获取购物车列表
	public final static String GET_CART_LIST_STRING = "GetCartList";
	//获取备注
	public final static String GET_STEMPLATE_LIST_STRING = "GetSTemplateList";
	//提交订单
	public final static String SUBMIT_MY_ORDER_STRING = "SubmitOrder";
	//获取订单列表
	public final static String GET_MY_ORDER_LIST_STRING = "GetMyOrderList";
	//添加收藏
	public final static String ADD_MY_COLLECT_STRING = "AddMyCollect";
	//删除收藏
	public final static String DEL_MY_COLLECT_STRING = "DelMyCollect";
	//发起评论
	public final static String ADD_COMMENT_STRING = "AddComment";
	//获取评论列表
	public final static String GET_COMMENT_STRING = "GetComment";
	//删除购物车
	public final static String DEL_CART_STRING = "DelCart";
	//更新订单状态
	public final static String UPDATE_ORDER_STRING = "UpdateOrder";
	//获取商家菜品列表
	public final static String GET_FOODLIST_BY_TOGO_STRING = "GetFoodListByTogo";
	
	
	private final static int URL_FLAG = 0;// 0为正式地址，1为测试地址

	// 根域名
	public final static String[] HTTP_ROOT = new String[] {
			"http://www.weetmall.com", "http://www.zhiyin.cn" };
	// 路径
	private final static String[] PATH_STRING = new String[] {
			"/index.php?g=api&m=index&a=", "/weetmallapi/mbinterface.php?act=" };
	// 手机登录接口
	private final static String MOBILE_LOGIN_STRING = "login_mobile";
	// 邮箱登录接口
	private final static String EMAIL_LOGIN_STRING = "login_email";
	// 第三方登录检测是否已经绑定过帐号
	private final static String BIND_LOGIN_STRING = "bind_login";
	// 手机注册接口
	private final static String MOBILE_REGISTER_STRING = "reg_mobile";
	// 邮箱注册接口
	private final static String EMAIL_REGISTER_STRING = "reg_email";
	// 第三方登录绑定手机
	private final static String MOBILE_BIND_STRING = "open_reg_mobile";
	// 第三方登录绑定邮箱
	private final static String EMAIL_BIND_STRING = "open_reg_email";
	// 短信发送接口
	private final static String SEND_SMS_STRING = "send_sms";
	// 获取用户头像接口
	private final static String GET_AVATAR_STRING = "get_avatar";
	// 获取产品详情接口
	private final static String GET_GOODS_DETAIL_STRING = "get_goods_detail_byid";
	// 申请提现接口
	private final static String APPLY_CASH_STRING = "money_aply";
	// 修改用户信息接口
	private final static String UPDATE_PROFILE_STRING = "update_profile";
	// 修改用户头像接口
	private final static String UPDATE_USERAVATAR_STRING = "update_useravatar";
	// 添加收货地址接口
	private final static String ADD_ADDRESS_STRING = "add_user_address";
	// 修改收货地址接口
	private final static String EDIT_ADDRESS_STRING = "update_user_address";
	// 收货地址列表
	private final static String ADDRESS_LIST_STRING = "get_user_address";
	// 删除收货地址接口
	private final static String DEL_ADDRESS_STRING = "del_user_address";
	// 获取佣金列表接口
	private final static String GET_YONGJIN_LIST_STRING = "IndexYongjin";
	// 添加收藏商品列表接口
	private final static String ADD_FAVORITE_STRING = "add_my_collect";
	// 删除收藏商品列表接口
	private final static String DEL_FAVORITE_STRING = "del_my_collect";
	// 获取收藏商品列表接口
	private final static String FAVORITE_LIST_STRING = "get_my_collect";
	// 获取推荐产品列表接口
	private final static String GET_RECOMMEND_LIST_STRING = "IndexTuijian";
	// 获取产品列表接口
	private final static String GET_GOODS_LIST_STRING = "get_goods_list_bycatid";
	// 添加银行卡接口
	private final static String ADD_MY_CARD_STRING = "add_my_card";
	// 获取产品分享接口
	private final static String GET_GOODS_SHARE_STRING = "get_sharelist_byuserid";
	// 获取用户绑定银行卡接口
	private final static String GET_MY_BANK_CARD_STRING = "get_my_card";
	// 获取充值详情接口
	private final static String GET_USER_RECHARGE_STRING = "user_recharge";
	// 获取我的优惠券接口
	private final static String GET_MY_COUPON_BYID_STRING = "get_coupon_byuserid";
	// 提交订单接口
	private final static String SUBMIT_ORDER_STRING = "submitorder";
	// 充值记录接口
	private final static String GET_RECHARGE_LIST_STRING = "user_recharge_log";
	// 订单列表接口
	private final static String GET_ORDER_LIST_STRING = "get_orderlist_byuserid";
	// 订单详情接口
	private final static String ORDER_DETAIL_STRING = "get_orderlist_byid";

	// 生成充值订单接口
	private final static String CREATE_RECHARGE_ORDER_STRING = "user_recharge";
	// 充值订单详情
	private final static String GET_RECHARGE_ORDER_DETAIL = "get_recharge_details";
	// 添加分享接口
	private final static String ADD_SHARE_STRING = "add_share";
	// 特卖广告接口
	private final static String Get_TM_ADS_LIST_STRING = "teAdds";
	// 专题列表接口
	private final static String Get_ZT_LIST_STRING = "columnList";
	// 特卖列表接口
	private final static String Get_TM_LIST_STRING = "teGoods";
	// 佣金广告接口
	private final static String Get_YONGJIN_ADS_STRING = "yongjinHelpPic";

	// 找回密码接口(手机号)
	private final static String FIND_PW_MOBILE = "findPwdSendCode";

	// 找回密码接口(邮箱)
	private final static String FIND_PW_EMAIL = "findPwdEmail";

	// 重置密码接口（手机号）
	private final static String RESET_PW_MOBILE = "resetPassword";

	// 重置密码接口（邮箱）
	private final static String RESET_PW_EMAIL = "resetPasswordEmail";

	// 支付订单去支付接口
	private final static String ORDERPAYAPI_STRING = "orderpayapi";

	// 订单接口
	private final static String ORDER_STATUS_STRING = "verifystatus";

	// 快捷支付手机验证
	private final static String MOBILE_QUICK_BUY = "quickbuy";

	// 快捷支付手机验证(添加地址)
	private final static String QUICKBUY_ADD_ADDRESS = "savetmpaddress";

	// 升级
	private final static String UPDATE_STRING = "checkVersion";

	// 积分列表
	private final static String GET_POINTSLIST_BYUSERID = "get_pointslist_byuserid";

	// 佣金列表
	private final static String GETMYYONGJIN = "GetMyYongjin";

	// 提现记录
	private final static String USER_MONEYLOG = "userMoneyLog";

	// 快速查询短信
	private final static String QUICK_ORDER_SMS = "quickOrderSendCode";

	// 快速查询接口
	private final static String QUICK_ORDER_SEARCH = "quickOrderSearch";

	public final static String getLoginUrl(int loginType) {
		switch (loginType) {
		case 0:
			return HTTP_ROOT[URL_FLAG] + PATH_STRING[URL_FLAG]
					+ MOBILE_LOGIN_STRING;
		case 1:
			return HTTP_ROOT[URL_FLAG] + PATH_STRING[URL_FLAG]
					+ EMAIL_LOGIN_STRING;
		default:
			break;
		}
		return null;
	}

	public final static String getBindLoginUrl() {
		return HTTP_ROOT[URL_FLAG] + PATH_STRING[URL_FLAG] + BIND_LOGIN_STRING;
	}

	public final static String getRegisterUrl(int registerType) {
		switch (registerType) {
		case 1:// 手机注册
			return HTTP_ROOT[URL_FLAG] + PATH_STRING[URL_FLAG]
					+ MOBILE_REGISTER_STRING;
		case 2:// 邮箱注册
			return HTTP_ROOT[URL_FLAG] + PATH_STRING[URL_FLAG]
					+ EMAIL_REGISTER_STRING;
		case 3:// 第三方绑定手机
			return HTTP_ROOT[URL_FLAG] + PATH_STRING[URL_FLAG]
					+ MOBILE_BIND_STRING;
		case 4:// 第三方绑定邮箱
			return HTTP_ROOT[URL_FLAG] + PATH_STRING[URL_FLAG]
					+ EMAIL_BIND_STRING;
		default:
			return null;
		}
	}

	public final static String getSendSmsUrl() {
		return HTTP_ROOT[URL_FLAG] + PATH_STRING[URL_FLAG] + SEND_SMS_STRING;
	}

	public final static String getAvatarUrl() {
		return HTTP_ROOT[URL_FLAG] + PATH_STRING[URL_FLAG] + GET_AVATAR_STRING;
	}

	public final static String getGoodsDetailUrl() {
		return HTTP_ROOT[URL_FLAG] + PATH_STRING[URL_FLAG]
				+ GET_GOODS_DETAIL_STRING;
	}

	public final static String getApplyCashUrl() {
		return HTTP_ROOT[URL_FLAG] + PATH_STRING[URL_FLAG] + APPLY_CASH_STRING;
	}

	public final static String getUpdateProfileUrl() {
		return HTTP_ROOT[URL_FLAG] + PATH_STRING[URL_FLAG]
				+ UPDATE_PROFILE_STRING;
	}

	public final static String getUpdateUseravatarUrl() {
		return HTTP_ROOT[URL_FLAG] + PATH_STRING[URL_FLAG]
				+ UPDATE_USERAVATAR_STRING;
	}

	public final static String getAddAddressUrl() {
		return HTTP_ROOT[URL_FLAG] + PATH_STRING[URL_FLAG] + ADD_ADDRESS_STRING;
	}

	public final static String getEditAddressUrl() {
		return HTTP_ROOT[URL_FLAG] + PATH_STRING[URL_FLAG]
				+ EDIT_ADDRESS_STRING;
	}

	public final static String getAddressListUrl() {
		return HTTP_ROOT[URL_FLAG] + PATH_STRING[URL_FLAG]
				+ ADDRESS_LIST_STRING;
	}

	public final static String getDelAddressUrl() {
		return HTTP_ROOT[URL_FLAG] + PATH_STRING[URL_FLAG] + DEL_ADDRESS_STRING;
	}

	public final static String getFavoriteListUrl() {
		return HTTP_ROOT[URL_FLAG] + PATH_STRING[URL_FLAG]
				+ FAVORITE_LIST_STRING;
	}

	public final static String getAddOrDelFavoriteUrl(int flag) {
		String[] urls = new String[] { ADD_FAVORITE_STRING, DEL_FAVORITE_STRING };
		return HTTP_ROOT[URL_FLAG] + PATH_STRING[URL_FLAG] + urls[flag];
	}

	public final static String getYongjinListUrl() {
		return HTTP_ROOT[URL_FLAG] + PATH_STRING[URL_FLAG]
				+ GET_YONGJIN_LIST_STRING;
	}

	public final static String getRecommendListUrl() {
		return HTTP_ROOT[URL_FLAG] + PATH_STRING[URL_FLAG]
				+ GET_RECOMMEND_LIST_STRING;
	}

	public final static String getGoodsListUrl() {
		return HTTP_ROOT[URL_FLAG] + PATH_STRING[URL_FLAG]
				+ GET_GOODS_LIST_STRING;
	}

	public final static String getAddMyCardUrl() {
		return HTTP_ROOT[URL_FLAG] + PATH_STRING[URL_FLAG] + ADD_MY_CARD_STRING;
	}

	public final static String getGoodsShareListUrl() {
		return HTTP_ROOT[URL_FLAG] + PATH_STRING[URL_FLAG]
				+ GET_GOODS_SHARE_STRING;
	}

	public final static String getMyBankCardUrl() {
		return HTTP_ROOT[URL_FLAG] + PATH_STRING[URL_FLAG]
				+ GET_MY_BANK_CARD_STRING;
	}

	public final static String getUserRechargeUrl() {
		return HTTP_ROOT[URL_FLAG] + PATH_STRING[URL_FLAG]
				+ GET_USER_RECHARGE_STRING;
	}

	public final static String getMyCouponByUserIdUrl() {
		return HTTP_ROOT[URL_FLAG] + PATH_STRING[URL_FLAG]
				+ GET_MY_COUPON_BYID_STRING;
	}

	public final static String getSubmitOrderUrl() {
		return HTTP_ROOT[URL_FLAG] + PATH_STRING[URL_FLAG]
				+ SUBMIT_ORDER_STRING;
	}

	public final static String getRechargeListUrl() {
		return HTTP_ROOT[URL_FLAG] + PATH_STRING[URL_FLAG]
				+ GET_RECHARGE_LIST_STRING;
	}

	public final static String getOrderDetailUrl() {
		return HTTP_ROOT[URL_FLAG] + PATH_STRING[URL_FLAG]
				+ ORDER_DETAIL_STRING;
	}

	public final static String getOrderListUrl() {
		return HTTP_ROOT[URL_FLAG] + PATH_STRING[URL_FLAG]
				+ GET_ORDER_LIST_STRING;
	}

	public final static String getCreateRechargeOrderUrl() {
		return HTTP_ROOT[URL_FLAG] + PATH_STRING[URL_FLAG]
				+ CREATE_RECHARGE_ORDER_STRING;
	}

	public final static String getRechargeOrderDetail() {
		return HTTP_ROOT[URL_FLAG] + PATH_STRING[URL_FLAG]
				+ GET_RECHARGE_ORDER_DETAIL;
	}

	public final static String getAddShareUrl() {
		return HTTP_ROOT[URL_FLAG] + PATH_STRING[URL_FLAG] + ADD_SHARE_STRING;
	}

	public final static String getTmAdsUrl() {
		return HTTP_ROOT[URL_FLAG] + PATH_STRING[URL_FLAG]
				+ Get_TM_ADS_LIST_STRING;
	}

	public final static String getZtListUrl() {
		return HTTP_ROOT[URL_FLAG] + PATH_STRING[URL_FLAG] + Get_ZT_LIST_STRING;
	}

	public final static String getTmListUrl() {
		return HTTP_ROOT[URL_FLAG] + PATH_STRING[URL_FLAG] + Get_TM_LIST_STRING;
	}

	public final static String getYongjinUrl() {
		return HTTP_ROOT[URL_FLAG] + PATH_STRING[URL_FLAG]
				+ Get_YONGJIN_ADS_STRING;
	}

	public final static String findPwByMobileUrl() {
		return HTTP_ROOT[URL_FLAG] + PATH_STRING[URL_FLAG] + FIND_PW_MOBILE;
	}

	public final static String findPwByEmailUrl() {
		return HTTP_ROOT[URL_FLAG] + PATH_STRING[URL_FLAG] + FIND_PW_EMAIL;
	}

	public final static String resetPwByMobileUrl() {
		return HTTP_ROOT[URL_FLAG] + PATH_STRING[URL_FLAG] + RESET_PW_MOBILE;
	}

	public final static String resetPwByEmailUrl() {
		return HTTP_ROOT[URL_FLAG] + PATH_STRING[URL_FLAG] + RESET_PW_EMAIL;
	}

	public final static String getOrderPayApiUrl() {
		return HTTP_ROOT[URL_FLAG] + PATH_STRING[URL_FLAG] + ORDERPAYAPI_STRING;
	}

	public final static String orderVerifyUrl() {
		return HTTP_ROOT[URL_FLAG] + PATH_STRING[URL_FLAG]
				+ ORDER_STATUS_STRING;
	}

	public final static String mobileQuichBuyUrl() {
		return HTTP_ROOT[URL_FLAG] + PATH_STRING[URL_FLAG] + MOBILE_QUICK_BUY;
	}

	public final static String quichBuyAddressUrl() {
		return HTTP_ROOT[URL_FLAG] + PATH_STRING[URL_FLAG]
				+ QUICKBUY_ADD_ADDRESS;
	}

	public final static String updateUrl() {
		return HTTP_ROOT[URL_FLAG] + PATH_STRING[URL_FLAG] + UPDATE_STRING;
	}

	public final static String getPointsListUrl() {
		return HTTP_ROOT[URL_FLAG] + PATH_STRING[URL_FLAG]
				+ GET_POINTSLIST_BYUSERID;
	}

	public final static String getMyYongjinUrl() {
		return HTTP_ROOT[URL_FLAG] + PATH_STRING[URL_FLAG] + GETMYYONGJIN;
	}

	public final static String getUserMoneyLogUrl() {
		return HTTP_ROOT[URL_FLAG] + PATH_STRING[URL_FLAG] + USER_MONEYLOG;
	}

	public final static String getQuickSearchSmsUrl() {
		return HTTP_ROOT[URL_FLAG] + PATH_STRING[URL_FLAG] + QUICK_ORDER_SMS;
	}

	public final static String getQuickOrderSearchUrl() {
		return HTTP_ROOT[URL_FLAG] + PATH_STRING[URL_FLAG] + QUICK_ORDER_SEARCH;
	}
	
	public final static String getUpdateOrderStatusUrl() {
		return HTTP_ROOT[URL_FLAG] + PATH_STRING[URL_FLAG] + UPDATE_ORDER_STRING;
	}
	
	public final static String getFoodListByTogoUrl() {
		return HTTP_ROOT[URL_FLAG] + PATH_STRING[URL_FLAG] + GET_FOODLIST_BY_TOGO_STRING;
	}
}