package com.company.ilunch.bean;

import java.util.ArrayList;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * 提交订单返回信息JavaBean
 */
public class SubmitMyOrderBean {
	@JSONField(name = "Body")
	private ArrayList<Body> body;
	@JSONField(name = "Head")
	private Head head;

	public ArrayList<Body> getBody() {
		return body;
	}

	public void setBody(ArrayList<Body> body) {
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
		@JSONField(name = "OrderId")
		private String orderId;

		public String getOrderId() {
			return orderId;
		}

		public void setOrderId(String orderId) {
			this.orderId = orderId;
		}
	}
}
