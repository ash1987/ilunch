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
	public static final String DEFAULT_PARTNER = "2088511589965339";

	// 收款支付宝账号
	public static final String DEFAULT_SELLER = "shouyin@ilunch.com.cn";

	// 商户私钥，自助生成
	public static final String PRIVATE = "MIICWwIBAAKBgQC6BJ30u4B+l/p9v3cYrrUUUK7ofkuBlCHzo1e43kICX2T27lBbuotvvnxVLjLTBJ14rB8Ms1SsgIh2HZnl4dZvI0Hf4XbusjU+lF7E40cSGcKuVNgLO5pV44QC+e8HIKpDb8/Y7XANbqUfxSgVgMWR9/jAjRqSVFL8P7CQnMBnLwIDAQABAoGAE2IZx3+mQwq5GlNSKKtDySsjHqzSV11FMGsL4AKg5Dhmf+iTTQuiLfGuaP3YY+6uLf3ZemxRXWxDS/OHrtGak2qWretK7QYk7uzW+aMhRDk3hC2mUoEw38JN/6iyfM0cS8nZvxb+Kv97r21t5rPy588kW0AWFCVP49x4WsIOO9kCQQDj5kutE8UiIJs0w0VAtv2KX6oYkDhSRKlf5pbSRMQ6kvZr0oVsMqnjuy/INt8hhxgSNQyDt9B6CVLTA+JV0Y5tAkEA0PRR90BmXUl+FQyUXIpU3rgsMMpsZvo+mFEjLcUe9rG237gqIPaJ1INWn6lrYnH0hC3aLpkkd4H2COm9JOUaiwJAHWCSYL0rrDqHx0Vkp/Luu+eZBZGRsQkozDPtPS2gHezT1xJMIKZnP/PhNO3d1XSUpcQw2MnZFeIdyxW1F1rsAQJAPUOVEKhKGwnkNKm2ihdo4NAg7ME83Uh7t876QH4uIOhkFN37RCTkrnE/oZpbyMZeia6j7Mur3Ho3N8/ITBv8FwJAD0RLvgSRIWh648DyWe/opnK2oeRqhVKXFHfPlFrLbfz2cB+GpUO/C8l6k4yL93EnVjyGAm61RJFjlaS4EtX32w==";

	public static final String PUBLIC = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQC6BJ30u4B+l/p9v3cYrrUUUK7ofkuBlCHzo1e43kICX2T27lBbuotvvnxVLjLTBJ14rB8Ms1SsgIh2HZnl4dZvI0Hf4XbusjU+lF7E40cSGcKuVNgLO5pV44QC+e8HIKpDb8/Y7XANbqUfxSgVgMWR9/jAjRqSVFL8P7CQnMBnLwIDAQAB";

}
