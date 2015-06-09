package com.company.ilunch.bean;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * 更新购物车返回信息JavaBean
 */
public class UpdateCartBean {
	@JSONField(name = "Body")
	private Body body;
	@JSONField(name = "Head")
	private Head head;

	public Body getBody() {
		return body;
	}

	public void setBody(Body body) {
		this.body = body;
	}

	public Head getHead() {
		return head;
	}

	public void setHead(Head head) {
		this.head = head;
	}

	public static class Head {
		@JSONField(name = "ResultCode")
		private String resultCode;
		@JSONField(name = "ResultInfo")
		private String resultInfo;

		public String getResultCode() {
			return resultCode;
		}

		public void setResultCode(String resultCode) {
			this.resultCode = resultCode;
		}

		public String getResultInfo() {
			return resultInfo;
		}

		public void setResultInfo(String resultInfo) {
			this.resultInfo = resultInfo;
		}
	}

	public static class Body {
		@JSONField(name = "CarNum")
		private String carNum;
		@JSONField(name = "CarPrice")
		private String carPrice;

		public String getCarNum() {
			return carNum;
		}

		public void setCarNum(String carNum) {
			this.carNum = carNum;
		}

		public String getCarPrice() {
			return carPrice;
		}

		public void setCarPrice(String carPrice) {
			this.carPrice = carPrice;
		}
	}
}
