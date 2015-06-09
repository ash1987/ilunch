/*
 * Copyright (C) 2010 The MobileSecurePay Project
 * All right reserved.
 * author: shiqun.shi@alipay.com
 * 
 *  提示：如何获取安全校验码和合作身份者id
 *  1.用您的签约支付宝账号登录支付宝网站(www.alipay.com)
 *  2.点击“商家服务”(https://b.alipay.com/order/myorder.htm)
 *  3.点击“查询合作者身份(pid)”、“查询安全校验码(key)”
 */

package com.company.ilunch.alipay;

//
// 请参考 Android平台安全支付服务(msp)应用开发接口(4.2 RSA算法签名)部分，并使用压缩包中的openssl RSA密钥生成工具，生成一套RSA公私钥。
// 这里签名时，只需要使用生成的RSA私钥。
// Note: 为安全起见，使用RSA私钥进行签名的操作过程，应该尽量放到商家服务器端去进行。
public final class Keys {

	// 合作身份者id，以2088开头的16位纯数字
	public static final String DEFAULT_PARTNER = "2088411103458061";

	// 收款支付宝账号
	public static final String DEFAULT_SELLER = "weetmall@163.com";

	// 商户私钥，自助生成
	public static final String PRIVATE = "MIICWwIBAAKBgQCyP5gEgfcAH6n397R4iNp8DkqLl9aNttnobC9xAUcDRqaGsSfzOpe+PlHpzmH9K9W038q4govKVjqhK+9GC/cGY0mVogWJlxyxp6ujnk3NFsi04gtqM38t7uGGQYwnatOvCfaDntU0iFC2ZzF1J3X9+JL1Y78FqgH0blq3NZRojwIDAQABAoGAbpW7p1hGOGJsnbWk1n75JavsHaym5KbDjEnKKQ7E0sh7OecgxOreB548TZTIADy4yg1phI0hUveqSiGqwcdciiXO3HSuwrIcO5RbzacMcbuGEY2iFfrp95nHpYC3ix4Y+FlJmdl/X5oHMB8XvqVzKtpa2Ue3b07tlB9GjOjp/xkCQQDcpfzGtD9wPwU/FSW+ft2Hl/1OlLBpAosEUrk31pPqDJyYkHCwgDGFwJjpWUF1uEB+5ZgsbgYWji+zPCyycptFAkEAzs6Mb+IIPkg9Yflv/4p6UrDSDCPtCueBipNwgxAzDYFGbF/nGh8IgQ5q273cJ/LQ1t7Cui4KksKl16S52dhHwwJASF5svONrly4kU19lRMipcgqouzhLb/W0kNNtCmJnmzFNv0BIpas2Eu9rd8WdkbW/+Z7mPVRMzOMPxBKtek3anQJAQ8taJmNGqIQt+yw2FuNibjEg6nHshyKVSMREofJxif/TAjv+GoYJ16TOSvLWdRIYfBfSZg4p0SqWCzco5c0M2QJAfLEYmNf/yQ+VOkaZiyHn+wSpey26aziCdoBLFWCRwSllxXdYteLVuPm+0d2rmnvu/WW+0wfinXdgfMzFyjSd2A==";

	public static final String PUBLIC = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCnxj/9qwVfgoUh/y2W89L6BkRAFljhNhgPdyPuBV64bfQNN1PjbCzkIM6qRdKBoLPXmKKMiFYnkd6rAoprih3/PrQEB/VsW8OoM8fxn67UDYuyBTqA23MML9q1+ilIZwBC2AQ2UBVOrFXfFl75p6/B5KsiNG9zpgmLCUYuLkxpLQIDAQAB";

}
